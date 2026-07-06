// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.nio.channels.ClosedChannelException;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.net.InetAddress;
import java.io.IOException;
import java.net.SocketException;
import java.io.FileDescriptor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AFDatagramSocketImpl<A extends AFSocketAddress> extends DatagramSocketImplShim
{
    private final AFSocketType socketType;
    private final AFSocketCore core;
    final AncillaryDataSupport ancillaryDataSupport;
    private final AtomicBoolean connected;
    private final AtomicBoolean bound;
    private final AtomicInteger socketTimeout;
    private int localPort;
    private int remotePort;
    private final AFAddressFamily<A> addressFamily;
    private AFSocketImplExtensions<A> implExtensions;
    
    protected AFDatagramSocketImpl(final AFAddressFamily<A> addressFamily, final FileDescriptor fd, final AFSocketType socketType) {
        this.ancillaryDataSupport = new AncillaryDataSupport();
        this.connected = new AtomicBoolean(false);
        this.bound = new AtomicBoolean(false);
        this.socketTimeout = new AtomicInteger(0);
        this.remotePort = 0;
        this.implExtensions = null;
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
        catch (final SocketException e) {
            throw e;
        }
        catch (final IOException e2) {
            throw (SocketException)new SocketException(e2.getMessage()).initCause(e2);
        }
    }
    
    @Override
    protected final void close() {
        this.core.runCleaner();
    }
    
    @Override
    protected final void connect(final InetAddress address, final int port) throws SocketException {
    }
    
    final void connect(final AFSocketAddress socketAddress) throws IOException {
        if (socketAddress == AFSocketAddress.INTERNAL_DUMMY_CONNECT) {
            return;
        }
        final ByteBuffer ab = socketAddress.getNativeAddressDirectBuffer();
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
        catch (final IOException e) {
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
    protected final void bind(final int lport, final InetAddress laddr) throws SocketException {
    }
    
    final void bind(final AFSocketAddress socketAddress) throws SocketException {
        if (socketAddress == AFSocketAddress.INTERNAL_DUMMY_BIND) {
            return;
        }
        try {
            ByteBuffer ab;
            if (socketAddress == null) {
                ab = AFSocketAddress.getNativeAddressDirectBuffer(0);
            }
            else {
                ab = socketAddress.getNativeAddressDirectBuffer();
            }
            NativeUnixSocket.bind(ab, ab.limit(), this.fd, 16);
            if (socketAddress == null) {
                this.localPort = 0;
                this.bound.set(false);
            }
            else {
                this.localPort = socketAddress.getPort();
            }
        }
        catch (final SocketException e) {
            throw e;
        }
        catch (final IOException e2) {
            throw (SocketException)new SocketException(e2.getMessage()).initCause(e2);
        }
    }
    
    @Override
    protected final void receive(final DatagramPacket p) throws IOException {
        this.recv(p, 0);
    }
    
    private void recv(final DatagramPacket p, int options) throws IOException {
        int len = p.getLength();
        final FileDescriptor fdesc = this.core.validFdOrException();
        final ByteBuffer datagramPacketBuffer = this.core.getThreadLocalDirectByteBuffer(len);
        len = Math.min(len, datagramPacketBuffer.capacity());
        options |= (this.core.isBlocking() ? 0 : 4);
        final ByteBuffer socketAddressBuffer = AFSocketAddress.SOCKETADDRESS_BUFFER_TL.get();
        final int count = NativeUnixSocket.receive(fdesc, datagramPacketBuffer, 0, len, socketAddressBuffer, options, this.ancillaryDataSupport, this.socketTimeout.get());
        if (count > len) {
            throw new IllegalStateException("count > len: " + count + " > " + len);
        }
        if (count == -1) {
            throw new SocketTimeoutException();
        }
        if (count < 0) {
            throw new IllegalStateException("count: " + count + " < 0");
        }
        datagramPacketBuffer.limit();
        datagramPacketBuffer.rewind();
        datagramPacketBuffer.get(p.getData(), p.getOffset(), count);
        p.setLength(count);
        final A addr = AFSocketAddress.ofInternal(socketAddressBuffer, this.getAddressFamily());
        p.setAddress((addr == null) ? null : addr.getInetAddress());
        p.setPort(this.remotePort);
    }
    
    @Override
    protected final void send(final DatagramPacket p) throws IOException {
        final InetAddress addr = p.getAddress();
        ByteBuffer sendToBuf = null;
        int sendToBufLen = 0;
        if (addr != null) {
            final byte[] addrBytes = AFInetAddress.unwrapAddress(addr, this.getAddressFamily());
            if (addrBytes != null) {
                sendToBuf = AFSocketAddress.SOCKETADDRESS_BUFFER_TL.get();
                sendToBufLen = NativeUnixSocket.bytesToSockAddr(this.getAddressFamily().getDomain(), sendToBuf, addrBytes);
                sendToBuf.position();
                if (sendToBufLen == -1) {
                    throw new SocketException("Unsupported domain");
                }
            }
        }
        final FileDescriptor fdesc = this.core.validFdOrException();
        final int len = p.getLength();
        final ByteBuffer datagramPacketBuffer = this.core.getThreadLocalDirectByteBuffer(len);
        datagramPacketBuffer.clear();
        datagramPacketBuffer.put(p.getData(), p.getOffset(), p.getLength());
        datagramPacketBuffer.flip();
        NativeUnixSocket.send(fdesc, datagramPacketBuffer, 0, len, sendToBuf, sendToBufLen, 16, this.ancillaryDataSupport);
    }
    
    @Override
    protected final int peek(final InetAddress i) throws IOException {
        throw new SocketException("Unsupported operation");
    }
    
    @Override
    protected final int peekData(final DatagramPacket p) throws IOException {
        this.recv(p, 2);
        return 0;
    }
    
    @Deprecated
    @Override
    protected final byte getTTL() throws IOException {
        return (byte)(this.getTimeToLive() & 0xFF);
    }
    
    @Deprecated
    @Override
    protected final void setTTL(final byte ttl) throws IOException {
    }
    
    @Override
    protected final int getTimeToLive() throws IOException {
        return 0;
    }
    
    @Override
    protected final void setTimeToLive(final int ttl) throws IOException {
    }
    
    @Override
    protected final void join(final InetAddress inetaddr) throws IOException {
        throw new SocketException("Unsupported");
    }
    
    @Override
    protected final void leave(final InetAddress inetaddr) throws IOException {
        throw new SocketException("Unsupported");
    }
    
    @Override
    protected final void joinGroup(final SocketAddress mcastaddr, final NetworkInterface netIf) throws IOException {
        throw new SocketException("Unsupported");
    }
    
    @Override
    protected final void leaveGroup(final SocketAddress mcastaddr, final NetworkInterface netIf) throws IOException {
        throw new SocketException("Unsupported");
    }
    
    @Override
    public Object getOption(final int optID) throws SocketException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        final FileDescriptor fdesc = this.core.validFdOrException();
        return AFSocketImpl.getOptionDefault(fdesc, optID, this.socketTimeout, this.getAddressFamily());
    }
    
    @Override
    public void setOption(final int optID, final Object value) throws SocketException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        final FileDescriptor fdesc = this.core.validFdOrException();
        AFSocketImpl.setOptionDefault(fdesc, optID, value, this.socketTimeout);
    }
    
    final A receive(final ByteBuffer dst) throws IOException {
        try {
            return (A)this.core.receive(dst);
        }
        catch (final SocketClosedException e) {
            throw (ClosedChannelException)new ClosedChannelException().initCause(e);
        }
    }
    
    final int send(final ByteBuffer src, final SocketAddress target) throws IOException {
        try {
            return this.core.write(src, target, 0);
        }
        catch (final SocketClosedException e) {
            throw (ClosedChannelException)new ClosedChannelException().initCause(e);
        }
    }
    
    final int read(final ByteBuffer dst, final ByteBuffer socketAddressBuffer) throws IOException {
        try {
            return this.core.read(dst, socketAddressBuffer, 0);
        }
        catch (final SocketClosedException e) {
            throw (ClosedChannelException)new ClosedChannelException().initCause(e);
        }
    }
    
    final int write(final ByteBuffer src) throws IOException {
        try {
            return this.core.write(src);
        }
        catch (final SocketClosedException e) {
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
    
    final void updatePorts(final int local, final int remote) {
        this.localPort = local;
        this.remotePort = remote;
    }
    
    final A getLocalSocketAddress() {
        return AFSocketAddress.getSocketAddress(this.getFileDescriptor(), false, this.localPort, this.getAddressFamily());
    }
    
    final A getRemoteSocketAddress() {
        return AFSocketAddress.getSocketAddress(this.getFileDescriptor(), true, this.remotePort, this.getAddressFamily());
    }
    
    protected final AFAddressFamily<A> getAddressFamily() {
        return this.addressFamily;
    }
    
    protected final synchronized AFSocketImplExtensions<A> getImplExtensions() {
        if (this.implExtensions == null) {
            this.implExtensions = this.addressFamily.initImplExtensions(this.ancillaryDataSupport);
        }
        return this.implExtensions;
    }
    
    final boolean accept0(final AFDatagramSocketImpl<A> socket) throws IOException {
        final FileDescriptor fdesc = this.core.validFdOrException();
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (!this.isBound()) {
            throw new SocketException("Socket is not bound");
        }
        AFSocketAddress socketAddress = this.core.socketAddress;
        final AFSocketAddress boundSocketAddress = this.getLocalSocketAddress();
        if (boundSocketAddress != null) {
            socketAddress = (this.core.socketAddress = boundSocketAddress);
        }
        if (socketAddress == null) {
            throw new SocketException("Socket is not bound");
        }
        final AFDatagramSocketImpl<A> si = socket;
        this.core.incPendingAccepts();
        try {
            final ByteBuffer ab = socketAddress.getNativeAddressDirectBuffer();
            SocketException caught = null;
            try {
                if (!NativeUnixSocket.accept(ab, ab.limit(), fdesc, si.fd, this.core.inode.get(), this.socketTimeout.get())) {
                    return false;
                }
            }
            catch (final SocketException e) {
                caught = e;
            }
            finally {
                if (!this.isBound() || this.isClosed()) {
                    if (this.getCore().isShutdownOnClose()) {
                        try {
                            NativeUnixSocket.shutdown(si.fd, 2);
                        }
                        catch (final Exception ex) {}
                    }
                    try {
                        NativeUnixSocket.close(si.fd);
                    }
                    catch (final Exception ex2) {}
                    if (caught != null) {
                        throw caught;
                    }
                    throw new SocketClosedException("Socket is closed");
                }
                else if (caught != null) {
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
    
    final void setSocketAddress(final AFSocketAddress socketAddress) {
        if (socketAddress == null) {
            this.core.socketAddress = null;
            this.localPort = -1;
        }
        else {
            this.core.socketAddress = socketAddress;
            if (this.localPort <= 0) {
                this.localPort = socketAddress.getPort();
            }
        }
    }
}
