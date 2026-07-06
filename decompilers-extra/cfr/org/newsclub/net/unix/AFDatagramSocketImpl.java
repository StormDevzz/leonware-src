/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jdt.annotation.NonNull
 *  org.eclipse.jdt.annotation.Nullable
 */
package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.newsclub.net.unix.AFAddressFamily;
import org.newsclub.net.unix.AFInetAddress;
import org.newsclub.net.unix.AFSocketAddress;
import org.newsclub.net.unix.AFSocketCore;
import org.newsclub.net.unix.AFSocketImpl;
import org.newsclub.net.unix.AFSocketImplExtensions;
import org.newsclub.net.unix.AFSocketType;
import org.newsclub.net.unix.AncillaryDataSupport;
import org.newsclub.net.unix.DatagramSocketImplShim;
import org.newsclub.net.unix.NativeUnixSocket;
import org.newsclub.net.unix.SocketClosedException;
import org.newsclub.net.unix.StackTraceUtil;

public abstract class AFDatagramSocketImpl<A extends AFSocketAddress>
extends DatagramSocketImplShim {
    private final AFSocketType socketType;
    private final AFSocketCore core;
    final AncillaryDataSupport ancillaryDataSupport = new AncillaryDataSupport();
    private final AtomicBoolean connected = new AtomicBoolean(false);
    private final AtomicBoolean bound = new AtomicBoolean(false);
    private final AtomicInteger socketTimeout = new AtomicInteger(0);
    private int localPort;
    private int remotePort = 0;
    private final AFAddressFamily<@NonNull A> addressFamily;
    private AFSocketImplExtensions<A> implExtensions = null;

    protected AFDatagramSocketImpl(AFAddressFamily<@NonNull A> addressFamily, FileDescriptor fd, AFSocketType socketType) {
        this.addressFamily = addressFamily;
        this.socketType = socketType;
        this.core = new AFSocketCore(this, fd, this.ancillaryDataSupport, this.getAddressFamily(), true);
        this.fd = this.core.fd;
    }

    @Override
    protected final void create() throws SocketException {
        if (this.isClosed()) {
            throw new SocketException("Already closed");
        }
        if (this.fd.valid()) {
            return;
        }
        try {
            NativeUnixSocket.createSocket(this.fd, this.getAddressFamily().getDomain(), this.socketType.getId());
        }
        catch (SocketException e) {
            throw e;
        }
        catch (IOException e) {
            throw (SocketException)new SocketException(e.getMessage()).initCause(e);
        }
    }

    @Override
    protected final void close() {
        this.core.runCleaner();
    }

    @Override
    protected final void connect(InetAddress address, int port) throws SocketException {
    }

    final void connect(AFSocketAddress socketAddress) throws IOException {
        if (socketAddress == AFSocketAddress.INTERNAL_DUMMY_CONNECT) {
            return;
        }
        ByteBuffer ab = socketAddress.getNativeAddressDirectBuffer();
        NativeUnixSocket.connect(ab, ab.limit(), this.fd, -1L);
        this.remotePort = socketAddress.getPort();
    }

    @Override
    protected final void disconnect() {
        try {
            NativeUnixSocket.disconnect(this.fd);
            this.connected.set(false);
            this.remotePort = 0;
        }
        catch (IOException e) {
            StackTraceUtil.printStackTrace(e);
        }
    }

    final AFSocketCore getCore() {
        return this.core;
    }

    @Override
    protected final FileDescriptor getFileDescriptor() {
        return this.core.fd;
    }

    final boolean isClosed() {
        return this.core.isClosed();
    }

    @Override
    protected final void bind(int lport, InetAddress laddr) throws SocketException {
    }

    final void bind(AFSocketAddress socketAddress) throws SocketException {
        if (socketAddress == AFSocketAddress.INTERNAL_DUMMY_BIND) {
            return;
        }
        try {
            ByteBuffer ab = socketAddress == null ? AFSocketAddress.getNativeAddressDirectBuffer(0) : socketAddress.getNativeAddressDirectBuffer();
            NativeUnixSocket.bind(ab, ab.limit(), this.fd, 16);
            if (socketAddress == null) {
                this.localPort = 0;
                this.bound.set(false);
            } else {
                this.localPort = socketAddress.getPort();
            }
        }
        catch (SocketException e) {
            throw e;
        }
        catch (IOException e) {
            throw (SocketException)new SocketException(e.getMessage()).initCause(e);
        }
    }

    @Override
    protected final void receive(DatagramPacket p) throws IOException {
        this.recv(p, 0);
    }

    private void recv(DatagramPacket p, int options) throws IOException {
        int len = p.getLength();
        FileDescriptor fdesc = this.core.validFdOrException();
        ByteBuffer datagramPacketBuffer = this.core.getThreadLocalDirectByteBuffer(len);
        len = Math.min(len, datagramPacketBuffer.capacity());
        ByteBuffer socketAddressBuffer = AFSocketAddress.SOCKETADDRESS_BUFFER_TL.get();
        int count = NativeUnixSocket.receive(fdesc, datagramPacketBuffer, 0, len, socketAddressBuffer, options |= this.core.isBlocking() ? 0 : 4, this.ancillaryDataSupport, this.socketTimeout.get());
        if (count > len) {
            throw new IllegalStateException("count > len: " + count + " > " + len);
        }
        if (count == -1) {
            throw new SocketTimeoutException();
        }
        if (count < 0) {
            throw new IllegalStateException("count: " + count + " < 0");
        }
        datagramPacketBuffer.limit(count);
        datagramPacketBuffer.rewind();
        datagramPacketBuffer.get(p.getData(), p.getOffset(), count);
        p.setLength(count);
        A addr = AFSocketAddress.ofInternal(socketAddressBuffer, this.getAddressFamily());
        p.setAddress(addr == null ? null : ((AFSocketAddress)addr).getInetAddress());
        p.setPort(this.remotePort);
    }

    @Override
    protected final void send(DatagramPacket p) throws IOException {
        byte[] addrBytes;
        InetAddress addr = p.getAddress();
        ByteBuffer sendToBuf = null;
        int sendToBufLen = 0;
        if (addr != null && (addrBytes = AFInetAddress.unwrapAddress(addr, this.getAddressFamily())) != null) {
            sendToBuf = AFSocketAddress.SOCKETADDRESS_BUFFER_TL.get();
            sendToBufLen = NativeUnixSocket.bytesToSockAddr(this.getAddressFamily().getDomain(), sendToBuf, addrBytes);
            sendToBuf.position(0);
            if (sendToBufLen == -1) {
                throw new SocketException("Unsupported domain");
            }
        }
        FileDescriptor fdesc = this.core.validFdOrException();
        int len = p.getLength();
        ByteBuffer datagramPacketBuffer = this.core.getThreadLocalDirectByteBuffer(len);
        datagramPacketBuffer.clear();
        datagramPacketBuffer.put(p.getData(), p.getOffset(), p.getLength());
        datagramPacketBuffer.flip();
        NativeUnixSocket.send(fdesc, datagramPacketBuffer, 0, len, sendToBuf, sendToBufLen, 16, this.ancillaryDataSupport);
    }

    @Override
    protected final int peek(InetAddress i) throws IOException {
        throw new SocketException("Unsupported operation");
    }

    @Override
    protected final int peekData(DatagramPacket p) throws IOException {
        this.recv(p, 2);
        return 0;
    }

    @Override
    @Deprecated
    protected final byte getTTL() throws IOException {
        return (byte)(this.getTimeToLive() & 0xFF);
    }

    @Override
    @Deprecated
    protected final void setTTL(byte ttl) throws IOException {
    }

    @Override
    protected final int getTimeToLive() throws IOException {
        return 0;
    }

    @Override
    protected final void setTimeToLive(int ttl) throws IOException {
    }

    @Override
    protected final void join(InetAddress inetaddr) throws IOException {
        throw new SocketException("Unsupported");
    }

    @Override
    protected final void leave(InetAddress inetaddr) throws IOException {
        throw new SocketException("Unsupported");
    }

    @Override
    protected final void joinGroup(SocketAddress mcastaddr, NetworkInterface netIf) throws IOException {
        throw new SocketException("Unsupported");
    }

    @Override
    protected final void leaveGroup(SocketAddress mcastaddr, NetworkInterface netIf) throws IOException {
        throw new SocketException("Unsupported");
    }

    @Override
    public Object getOption(int optID) throws SocketException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        FileDescriptor fdesc = this.core.validFdOrException();
        return AFSocketImpl.getOptionDefault(fdesc, optID, this.socketTimeout, this.getAddressFamily());
    }

    @Override
    public void setOption(int optID, Object value) throws SocketException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        FileDescriptor fdesc = this.core.validFdOrException();
        AFSocketImpl.setOptionDefault(fdesc, optID, value, this.socketTimeout);
    }

    final A receive(ByteBuffer dst) throws IOException {
        try {
            return (A)this.core.receive(dst);
        }
        catch (SocketClosedException e) {
            throw (ClosedChannelException)new ClosedChannelException().initCause(e);
        }
    }

    final int send(ByteBuffer src, SocketAddress target) throws IOException {
        try {
            return this.core.write(src, target, 0);
        }
        catch (SocketClosedException e) {
            throw (ClosedChannelException)new ClosedChannelException().initCause(e);
        }
    }

    final int read(ByteBuffer dst, ByteBuffer socketAddressBuffer) throws IOException {
        try {
            return this.core.read(dst, socketAddressBuffer, 0);
        }
        catch (SocketClosedException e) {
            throw (ClosedChannelException)new ClosedChannelException().initCause(e);
        }
    }

    final int write(ByteBuffer src) throws IOException {
        try {
            return this.core.write(src);
        }
        catch (SocketClosedException e) {
            throw (ClosedChannelException)new ClosedChannelException().initCause(e);
        }
    }

    final boolean isConnected() {
        if (this.connected.get()) {
            return true;
        }
        if (this.isClosed()) {
            return false;
        }
        if (this.core.isConnected(false)) {
            this.connected.set(true);
            return true;
        }
        return false;
    }

    final boolean isBound() {
        if (this.bound.get()) {
            return true;
        }
        if (this.isClosed()) {
            return false;
        }
        if (this.core.isConnected(true)) {
            this.bound.set(true);
            return true;
        }
        return false;
    }

    final void updatePorts(int local, int remote) {
        this.localPort = local;
        this.remotePort = remote;
    }

    final @Nullable A getLocalSocketAddress() {
        return AFSocketAddress.getSocketAddress(this.getFileDescriptor(), false, this.localPort, this.getAddressFamily());
    }

    final @Nullable A getRemoteSocketAddress() {
        return AFSocketAddress.getSocketAddress(this.getFileDescriptor(), true, this.remotePort, this.getAddressFamily());
    }

    protected final AFAddressFamily<@NonNull A> getAddressFamily() {
        return this.addressFamily;
    }

    protected final synchronized AFSocketImplExtensions<A> getImplExtensions() {
        if (this.implExtensions == null) {
            this.implExtensions = this.addressFamily.initImplExtensions(this.ancillaryDataSupport);
        }
        return this.implExtensions;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    final boolean accept0(AFDatagramSocketImpl<A> socket) throws IOException {
        FileDescriptor fdesc = this.core.validFdOrException();
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (!this.isBound()) {
            throw new SocketException("Socket is not bound");
        }
        AFSocketAddress socketAddress = this.core.socketAddress;
        A boundSocketAddress = this.getLocalSocketAddress();
        if (boundSocketAddress != null) {
            this.core.socketAddress = socketAddress = boundSocketAddress;
        }
        if (socketAddress == null) {
            throw new SocketException("Socket is not bound");
        }
        AFDatagramSocketImpl<A> si = socket;
        this.core.incPendingAccepts();
        try {
            ByteBuffer ab = socketAddress.getNativeAddressDirectBuffer();
            SocketException caught = null;
            try {
                if (!NativeUnixSocket.accept(ab, ab.limit(), fdesc, si.fd, this.core.inode.get(), this.socketTimeout.get())) {
                    boolean bl = false;
                    return bl;
                }
            }
            catch (SocketException e) {
                caught = e;
                return (boolean)caught;
            }
            finally {
                if (!this.isBound() || this.isClosed()) {
                    if (this.getCore().isShutdownOnClose()) {
                        try {
                            NativeUnixSocket.shutdown(si.fd, 2);
                        }
                        catch (Exception exception) {}
                    }
                    try {
                        NativeUnixSocket.close(si.fd);
                    }
                    catch (Exception exception) {}
                    if (caught != null) {
                        throw caught;
                    }
                    throw new SocketClosedException("Socket is closed");
                }
                if (caught != null) {
                    throw caught;
                }
            }
        }
        finally {
            this.core.decPendingAccepts();
        }
        si.setSocketAddress(socketAddress);
        si.connected.set(true);
        return true;
    }

    final int getLocalPort1() {
        return this.localPort;
    }

    final int getRemotePort() {
        return this.remotePort;
    }

    final void setSocketAddress(AFSocketAddress socketAddress) {
        if (socketAddress == null) {
            this.core.socketAddress = null;
            this.localPort = -1;
        } else {
            this.core.socketAddress = socketAddress;
            if (this.localPort <= 0) {
                this.localPort = socketAddress.getPort();
            }
        }
    }
}

