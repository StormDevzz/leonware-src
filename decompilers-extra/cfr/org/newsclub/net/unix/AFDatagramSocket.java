/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.kohlschutter.annotations.compiletime.SuppressFBWarnings
 *  org.eclipse.jdt.annotation.Nullable
 */
package org.newsclub.net.unix;

import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.AlreadyBoundException;
import java.nio.channels.IllegalBlockingModeException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.jdt.annotation.Nullable;
import org.newsclub.net.unix.AFAddressFamily;
import org.newsclub.net.unix.AFDatagramChannel;
import org.newsclub.net.unix.AFDatagramSocketImpl;
import org.newsclub.net.unix.AFInetAddress;
import org.newsclub.net.unix.AFSocketAddress;
import org.newsclub.net.unix.AFSocketExtensions;
import org.newsclub.net.unix.AFSocketImplExtensions;
import org.newsclub.net.unix.AFSocketOption;
import org.newsclub.net.unix.AFSomeSocket;
import org.newsclub.net.unix.AncillaryDataSupport;
import org.newsclub.net.unix.DatagramSocketShim;
import org.newsclub.net.unix.NativeUnixSocket;
import org.newsclub.net.unix.SentinelSocketAddress;
import org.newsclub.net.unix.SocketClosedException;

public abstract class AFDatagramSocket<A extends AFSocketAddress>
extends DatagramSocketShim
implements AFSomeSocket,
AFSocketExtensions {
    private static final InetSocketAddress WILDCARD_ADDRESS = new InetSocketAddress(0);
    private final AFDatagramSocketImpl<A> impl;
    private final AncillaryDataSupport ancillaryDataSupport;
    private final AtomicBoolean created = new AtomicBoolean(false);
    private final AtomicBoolean deleteOnClose = new AtomicBoolean(true);
    private final AFDatagramChannel<A> channel = this.newChannel();

    protected AFDatagramSocket(AFDatagramSocketImpl<A> impl) {
        super(impl);
        this.impl = impl;
        this.ancillaryDataSupport = impl.ancillaryDataSupport;
    }

    protected abstract AFDatagramChannel<A> newChannel();

    final AncillaryDataSupport getAncillaryDataSupport() {
        return this.ancillaryDataSupport;
    }

    protected final Class<? extends AFSocketAddress> socketAddressClass() {
        return this.impl.getAddressFamily().getSocketAddressClass();
    }

    protected static final <A extends AFSocketAddress> AFDatagramSocket<A> newInstance(Constructor<A> constructor) throws IOException {
        return constructor.newSocket(null);
    }

    protected static final <A extends AFSocketAddress> AFDatagramSocket<A> newInstance(Constructor<A> constructor, FileDescriptor fdObj, int localPort, int remotePort) throws IOException {
        if (fdObj == null) {
            return AFDatagramSocket.newInstance(constructor);
        }
        if (!fdObj.valid()) {
            throw new SocketException("Invalid file descriptor");
        }
        int status = NativeUnixSocket.socketStatus(fdObj);
        if (status == -1) {
            throw new SocketException("Not a valid socket");
        }
        AFDatagramSocket<A> socket = constructor.newSocket(fdObj);
        socket.getAFImpl().updatePorts(localPort, remotePort);
        switch (status) {
            case 2: {
                socket.internalDummyConnect();
                break;
            }
            case 1: {
                socket.internalDummyBind();
                break;
            }
            case 0: {
                break;
            }
            default: {
                throw new IllegalStateException("Invalid socketStatus response: " + status);
            }
        }
        return socket;
    }

    @Override
    public final void connect(InetAddress address, int port) {
        throw new IllegalArgumentException("Cannot connect to InetAddress");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final void peek(DatagramPacket p) throws IOException {
        DatagramPacket datagramPacket = p;
        synchronized (datagramPacket) {
            if (this.isClosed()) {
                throw new SocketException("Socket is closed");
            }
            this.getAFImpl().peekData(p);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public final void send(DatagramPacket p) throws IOException {
        DatagramPacket datagramPacket = p;
        synchronized (datagramPacket) {
            if (this.isClosed()) {
                throw new SocketException("Socket is closed");
            }
            if (!this.isBound()) {
                this.internalDummyBind();
            }
            this.getAFImpl().send(p);
        }
    }

    final void internalDummyConnect() throws SocketException {
        super.connect(AFSocketAddress.INTERNAL_DUMMY_DONT_CONNECT);
    }

    final void internalDummyBind() throws SocketException {
        this.bind(AFSocketAddress.INTERNAL_DUMMY_BIND);
    }

    @Override
    public final synchronized void connect(SocketAddress addr) throws SocketException {
        if (!this.isBound()) {
            this.internalDummyBind();
        }
        this.internalDummyConnect();
        try {
            this.getAFImpl().connect(AFSocketAddress.preprocessSocketAddress(this.socketAddressClass(), addr, null));
        }
        catch (SocketException e) {
            throw e;
        }
        catch (IOException e) {
            throw (SocketException)new SocketException(e.getMessage()).initCause(e);
        }
    }

    public final synchronized @Nullable A getRemoteSocketAddress() {
        return this.getAFImpl().getRemoteSocketAddress();
    }

    @Override
    public final boolean isConnected() {
        return super.isConnected() || this.impl.isConnected();
    }

    @Override
    public final boolean isBound() {
        return super.isBound() || this.impl.isBound();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void close() {
        InetAddress addr;
        if (this.isClosed()) {
            return;
        }
        this.getAFImpl().close();
        boolean wasBound = this.isBound();
        if (wasBound && this.deleteOnClose.get() && AFInetAddress.isSupportedAddress(addr = this.getLocalAddress(), this.addressFamily())) {
            try {
                A socketAddress = AFSocketAddress.unwrap(addr, 0, this.addressFamily());
                if (socketAddress != null && ((AFSocketAddress)socketAddress).hasFilename() && ((AFSocketAddress)socketAddress).getFile().delete()) {
                    // empty if block
                }
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        super.close();
    }

    @Override
    public final synchronized void bind(SocketAddress addr) throws SocketException {
        block10: {
            AFSocketAddress epoint;
            block9: {
                boolean isBound = this.isBound();
                if (isBound && addr == AFSocketAddress.INTERNAL_DUMMY_BIND) {
                    return;
                }
                if (this.isClosed()) {
                    throw new SocketException("Socket is closed");
                }
                if (!isBound) {
                    try {
                        super.bind(AFSocketAddress.INTERNAL_DUMMY_BIND);
                    }
                    catch (AlreadyBoundException alreadyBoundException) {
                    }
                    catch (SocketException e) {
                        String message = e.getMessage();
                        if (message != null && message.contains("already bound")) break block9;
                        throw e;
                    }
                }
            }
            boolean isWildcardBind = WILDCARD_ADDRESS.equals(addr);
            AFSocketAddress aFSocketAddress = epoint = addr == null || isWildcardBind ? null : AFSocketAddress.preprocessSocketAddress(this.socketAddressClass(), addr, null);
            if (epoint instanceof SentinelSocketAddress) {
                return;
            }
            try {
                this.getAFImpl().bind(epoint);
            }
            catch (SocketException e) {
                if (isWildcardBind) break block10;
                this.getAFImpl().close();
                throw e;
            }
        }
    }

    public final @Nullable A getLocalSocketAddress() {
        if (this.isClosed()) {
            return null;
        }
        if (!this.isBound()) {
            return null;
        }
        return this.getAFImpl().getLocalSocketAddress();
    }

    public final boolean isDeleteOnClose() {
        return this.deleteOnClose.get();
    }

    public final void setDeleteOnClose(boolean b) {
        this.deleteOnClose.set(b);
    }

    final AFDatagramSocketImpl<A> getAFImpl() {
        if (this.created.compareAndSet(false, true)) {
            try {
                this.getSoTimeout();
            }
            catch (SocketException socketException) {
                // empty catch block
            }
        }
        return this.impl;
    }

    final AFDatagramSocketImpl<A> getAFImpl(boolean create) {
        if (create) {
            return this.getAFImpl();
        }
        return this.impl;
    }

    @Override
    public final int getAncillaryReceiveBufferSize() {
        return this.ancillaryDataSupport.getAncillaryReceiveBufferSize();
    }

    @Override
    public final void setAncillaryReceiveBufferSize(int size) {
        this.ancillaryDataSupport.setAncillaryReceiveBufferSize(size);
    }

    @Override
    public final void ensureAncillaryReceiveBufferSize(int minSize) {
        this.ancillaryDataSupport.ensureAncillaryReceiveBufferSize(minSize);
    }

    @Override
    public final boolean isClosed() {
        return super.isClosed() || this.getAFImpl().isClosed();
    }

    @Override
    @SuppressFBWarnings(value={"EI_EXPOSE_REP"})
    public AFDatagramChannel<A> getChannel() {
        return this.channel;
    }

    @Override
    public final FileDescriptor getFileDescriptor() throws IOException {
        return this.getAFImpl().getFileDescriptor();
    }

    @Override
    public final void receive(DatagramPacket p) throws IOException {
        this.getAFImpl().receive(p);
    }

    protected final AFAddressFamily<A> addressFamily() {
        return this.getAFImpl().getAddressFamily();
    }

    protected AFSocketImplExtensions<A> getImplExtensions() {
        return this.getAFImpl(false).getImplExtensions();
    }

    @Override
    public <T> T getOption(AFSocketOption<T> name) throws IOException {
        return this.getAFImpl().getCore().getOption(name);
    }

    @Override
    public <T> DatagramSocket setOption(AFSocketOption<T> name, T value) throws IOException {
        this.getAFImpl().getCore().setOption(name, value);
        return this;
    }

    public AFDatagramSocket<A> accept() throws IOException {
        return this.accept1(true);
    }

    public final void listen(int backlog) throws IOException {
        FileDescriptor fdesc = this.getAFImpl().getCore().validFdOrException();
        if (backlog <= 0) {
            backlog = 50;
        }
        NativeUnixSocket.listen(fdesc, backlog);
    }

    protected abstract AFDatagramSocket<A> newDatagramSocketInstance() throws IOException;

    AFDatagramSocket<A> accept1(boolean throwOnFail) throws IOException {
        AFDatagramSocket<A> as = this.newDatagramSocketInstance();
        boolean success = this.getAFImpl().accept0(as.getAFImpl(false));
        if (this.isClosed()) {
            throw new SocketClosedException("Socket is closed");
        }
        if (!success) {
            if (throwOnFail) {
                if (this.getChannel().isBlocking()) {
                    return null;
                }
                throw new IllegalBlockingModeException();
            }
            return null;
        }
        as.getAFImpl(true);
        as.connect(AFSocketAddress.INTERNAL_DUMMY_CONNECT);
        as.getAFImpl().updatePorts(this.getAFImpl().getLocalPort1(), this.getAFImpl().getRemotePort());
        return as;
    }

    @Override
    public void setShutdownOnClose(boolean enabled) {
        this.getAFImpl().getCore().setShutdownOnClose(enabled);
    }

    @FunctionalInterface
    public static interface Constructor<A extends AFSocketAddress> {
        public AFDatagramSocket<A> newSocket(FileDescriptor var1) throws IOException;
    }
}

