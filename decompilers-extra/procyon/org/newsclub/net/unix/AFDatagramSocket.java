// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.nio.channels.DatagramChannel;
import java.nio.channels.IllegalBlockingModeException;
import java.net.DatagramSocket;
import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.nio.channels.AlreadyBoundException;
import java.net.SocketAddress;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.IOException;
import java.io.FileDescriptor;
import java.net.DatagramSocketImpl;
import java.util.concurrent.atomic.AtomicBoolean;
import java.net.InetSocketAddress;

public abstract class AFDatagramSocket<A extends AFSocketAddress> extends DatagramSocketShim implements AFSomeSocket, AFSocketExtensions
{
    private static final InetSocketAddress WILDCARD_ADDRESS;
    private final AFDatagramSocketImpl<A> impl;
    private final AncillaryDataSupport ancillaryDataSupport;
    private final AtomicBoolean created;
    private final AtomicBoolean deleteOnClose;
    private final AFDatagramChannel<A> channel;
    
    protected AFDatagramSocket(final AFDatagramSocketImpl<A> impl) {
        super(impl);
        this.created = new AtomicBoolean(false);
        this.deleteOnClose = new AtomicBoolean(true);
        this.channel = this.newChannel();
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
    
    protected static final <A extends AFSocketAddress> AFDatagramSocket<A> newInstance(final Constructor<A> constructor) throws IOException {
        return constructor.newSocket(null);
    }
    
    protected static final <A extends AFSocketAddress> AFDatagramSocket<A> newInstance(final Constructor<A> constructor, final FileDescriptor fdObj, final int localPort, final int remotePort) throws IOException {
        if (fdObj == null) {
            return newInstance(constructor);
        }
        if (!fdObj.valid()) {
            throw new SocketException("Invalid file descriptor");
        }
        final int status = NativeUnixSocket.socketStatus(fdObj);
        if (status == -1) {
            throw new SocketException("Not a valid socket");
        }
        final AFDatagramSocket<A> socket = constructor.newSocket(fdObj);
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
    public final void connect(final InetAddress address, final int port) {
        throw new IllegalArgumentException("Cannot connect to InetAddress");
    }
    
    public final void peek(final DatagramPacket p) throws IOException {
        synchronized (p) {
            if (this.isClosed()) {
                throw new SocketException("Socket is closed");
            }
            this.getAFImpl().peekData(p);
        }
    }
    
    @Override
    public final void send(final DatagramPacket p) throws IOException {
        synchronized (p) {
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
    public final synchronized void connect(final SocketAddress addr) throws SocketException {
        if (!this.isBound()) {
            this.internalDummyBind();
        }
        this.internalDummyConnect();
        try {
            this.getAFImpl().connect(AFSocketAddress.preprocessSocketAddress(this.socketAddressClass(), addr, null));
        }
        catch (final SocketException e) {
            throw e;
        }
        catch (final IOException e2) {
            throw (SocketException)new SocketException(e2.getMessage()).initCause(e2);
        }
    }
    
    @Override
    public final synchronized A getRemoteSocketAddress() {
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
    
    @Override
    public final void close() {
        if (this.isClosed()) {
            return;
        }
        this.getAFImpl().close();
        final boolean wasBound = this.isBound();
        if (wasBound && this.deleteOnClose.get()) {
            final InetAddress addr = this.getLocalAddress();
            if (AFInetAddress.isSupportedAddress(addr, this.addressFamily())) {
                try {
                    final AFSocketAddress socketAddress = AFSocketAddress.unwrap(addr, 0, this.addressFamily());
                    if (socketAddress == null || !socketAddress.hasFilename() || !socketAddress.getFile().delete()) {}
                }
                catch (final IOException ex) {}
            }
        }
        super.close();
    }
    
    @Override
    public final synchronized void bind(final SocketAddress addr) throws SocketException {
        final boolean isBound = this.isBound();
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
            catch (final AlreadyBoundException ex) {}
            catch (final SocketException e) {
                final String message = e.getMessage();
                if (message == null || !message.contains("already bound")) {
                    throw e;
                }
            }
        }
        final boolean isWildcardBind = AFDatagramSocket.WILDCARD_ADDRESS.equals(addr);
        final AFSocketAddress epoint = (addr == null || isWildcardBind) ? null : AFSocketAddress.preprocessSocketAddress(this.socketAddressClass(), addr, null);
        if (epoint instanceof SentinelSocketAddress) {
            return;
        }
        try {
            this.getAFImpl().bind(epoint);
        }
        catch (final SocketException e2) {
            if (!isWildcardBind) {
                this.getAFImpl().close();
                throw e2;
            }
        }
    }
    
    @Override
    public final A getLocalSocketAddress() {
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
    
    public final void setDeleteOnClose(final boolean b) {
        this.deleteOnClose.set(b);
    }
    
    final AFDatagramSocketImpl<A> getAFImpl() {
        if (this.created.compareAndSet(false, true)) {
            try {
                this.getSoTimeout();
            }
            catch (final SocketException ex) {}
        }
        return this.impl;
    }
    
    final AFDatagramSocketImpl<A> getAFImpl(final boolean create) {
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
    public final void setAncillaryReceiveBufferSize(final int size) {
        this.ancillaryDataSupport.setAncillaryReceiveBufferSize(size);
    }
    
    @Override
    public final void ensureAncillaryReceiveBufferSize(final int minSize) {
        this.ancillaryDataSupport.ensureAncillaryReceiveBufferSize(minSize);
    }
    
    @Override
    public final boolean isClosed() {
        return super.isClosed() || this.getAFImpl().isClosed();
    }
    
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    @Override
    public AFDatagramChannel<A> getChannel() {
        return this.channel;
    }
    
    @Override
    public final FileDescriptor getFileDescriptor() throws IOException {
        return this.getAFImpl().getFileDescriptor();
    }
    
    @Override
    public final void receive(final DatagramPacket p) throws IOException {
        this.getAFImpl().receive(p);
    }
    
    protected final AFAddressFamily<A> addressFamily() {
        return this.getAFImpl().getAddressFamily();
    }
    
    protected AFSocketImplExtensions<A> getImplExtensions() {
        return this.getAFImpl(false).getImplExtensions();
    }
    
    @Override
    public <T> T getOption(final AFSocketOption<T> name) throws IOException {
        return this.getAFImpl().getCore().getOption(name);
    }
    
    @Override
    public <T> DatagramSocket setOption(final AFSocketOption<T> name, final T value) throws IOException {
        this.getAFImpl().getCore().setOption(name, value);
        return this;
    }
    
    public AFDatagramSocket<A> accept() throws IOException {
        return this.accept1(true);
    }
    
    public final void listen(int backlog) throws IOException {
        final FileDescriptor fdesc = this.getAFImpl().getCore().validFdOrException();
        if (backlog <= 0) {
            backlog = 50;
        }
        NativeUnixSocket.listen(fdesc, backlog);
    }
    
    protected abstract AFDatagramSocket<A> newDatagramSocketInstance() throws IOException;
    
    AFDatagramSocket<A> accept1(final boolean throwOnFail) throws IOException {
        final AFDatagramSocket<A> as = this.newDatagramSocketInstance();
        final boolean success = this.getAFImpl().accept0(as.getAFImpl(false));
        if (this.isClosed()) {
            throw new SocketClosedException("Socket is closed");
        }
        if (success) {
            as.getAFImpl(true);
            as.connect(AFSocketAddress.INTERNAL_DUMMY_CONNECT);
            as.getAFImpl().updatePorts(this.getAFImpl().getLocalPort1(), this.getAFImpl().getRemotePort());
            return as;
        }
        if (!throwOnFail) {
            return null;
        }
        if (this.getChannel().isBlocking()) {
            return null;
        }
        throw new IllegalBlockingModeException();
    }
    
    @Override
    public void setShutdownOnClose(final boolean enabled) {
        this.getAFImpl().getCore().setShutdownOnClose(enabled);
    }
    
    static {
        WILDCARD_ADDRESS = new InetSocketAddress(0);
    }
    
    @FunctionalInterface
    public interface Constructor<A extends AFSocketAddress>
    {
        AFDatagramSocket<A> newSocket(final FileDescriptor p0) throws IOException;
    }
}
