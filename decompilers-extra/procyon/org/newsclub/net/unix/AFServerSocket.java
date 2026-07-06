// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;
import java.util.Objects;
import java.net.SocketOption;
import java.net.InetAddress;
import java.io.Closeable;
import java.io.File;
import java.nio.channels.IllegalBlockingModeException;
import java.net.SocketImpl;
import java.net.SocketAddress;
import java.net.SocketException;
import java.io.IOException;
import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.FileDescriptor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.net.ServerSocket;

public abstract class AFServerSocket<A extends AFSocketAddress> extends ServerSocket implements AFSomeSocketThing
{
    private final AFSocketImpl<A> implementation;
    private A boundEndpoint;
    private final Closeables closeables;
    private final AtomicBoolean created;
    private final AtomicBoolean deleteOnClose;
    private final AFServerSocketChannel<?> channel;
    private SocketAddressFilter bindFilter;
    
    @SuppressFBWarnings({ "CT_CONSTRUCTOR_THROW" })
    protected AFServerSocket() throws IOException {
        this((FileDescriptor)null);
    }
    
    @SuppressFBWarnings({ "CT_CONSTRUCTOR_THROW" })
    protected AFServerSocket(final FileDescriptor fdObj) throws IOException {
        this.closeables = new Closeables();
        this.created = new AtomicBoolean(false);
        this.deleteOnClose = new AtomicBoolean(true);
        this.channel = this.newChannel();
        NativeUnixSocket.initServerImpl(this, this.implementation = this.newImpl(fdObj));
        this.getAFImpl().setOption(4, true);
    }
    
    protected abstract AFServerSocketChannel<?> newChannel();
    
    protected abstract AFSocketImpl<A> newImpl(final FileDescriptor p0) throws IOException;
    
    protected static <A extends AFSocketAddress> AFServerSocket<A> newInstance(final Constructor<A> instanceSupplier) throws IOException {
        return instanceSupplier.newInstance(null);
    }
    
    protected static <A extends AFSocketAddress> AFServerSocket<A> newInstance(final Constructor<A> instanceSupplier, final FileDescriptor fdObj, final int localPort, final int remotePort) throws IOException {
        if (fdObj == null) {
            return instanceSupplier.newInstance(null);
        }
        final int status = NativeUnixSocket.socketStatus(fdObj);
        if (!fdObj.valid() || status == -1) {
            throw new SocketException("Not a valid socket");
        }
        final AFServerSocket<A> socket = instanceSupplier.newInstance(fdObj);
        socket.getAFImpl().updatePorts(localPort, remotePort);
        switch (status) {
            case 2: {
                throw new SocketException("Not a ServerSocket");
            }
            case 1: {
                socket.bind(AFSocketAddress.INTERNAL_DUMMY_BIND);
                socket.setBoundEndpoint(AFSocketAddress.getSocketAddress(fdObj, false, localPort, socket.addressFamily()));
                break;
            }
            case 0: {
                break;
            }
            default: {
                throw new IllegalStateException("Invalid socketStatus response: " + status);
            }
        }
        socket.getAFImpl().setSocketAddress(socket.getLocalSocketAddress());
        return socket;
    }
    
    protected static <A extends AFSocketAddress> AFServerSocket<A> bindOn(final Constructor<A> instanceSupplier, final AFSocketAddress addr) throws IOException {
        final AFServerSocket<A> socket = instanceSupplier.newInstance(null);
        socket.bind(addr);
        return socket;
    }
    
    protected static <A extends AFSocketAddress> AFServerSocket<A> bindOn(final Constructor<A> instanceSupplier, final A addr, final boolean deleteOnClose) throws IOException {
        final AFServerSocket<A> socket = instanceSupplier.newInstance(null);
        socket.bind(addr);
        socket.setDeleteOnClose(deleteOnClose);
        return socket;
    }
    
    protected static <A extends AFSocketAddress> AFServerSocket<A> forceBindOn(final Constructor<A> instanceSupplier, final A forceAddr) throws IOException {
        final AFServerSocket<A> socket = instanceSupplier.newInstance(null);
        return socket.forceBindAddress(forceAddr);
    }
    
    public final AFServerSocket<A> forceBindAddress(final SocketAddress endpoint) {
        return this.bindHook(orig -> (orig == null) ? null : endpoint);
    }
    
    @Override
    public final void bind(SocketAddress endpoint, final int backlog) throws IOException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        boolean bindErrorOk;
        if (this.bindFilter != null) {
            endpoint = this.bindFilter.apply(endpoint);
            bindErrorOk = (endpoint != null && this.isBound());
        }
        else {
            bindErrorOk = false;
        }
        if (!(endpoint instanceof AFSocketAddress)) {
            throw new IllegalArgumentException("Can only bind to endpoints of type " + AFSocketAddress.class.getName() + ": " + endpoint);
        }
        A endpointCast;
        try {
            endpointCast = (A)endpoint;
        }
        catch (final ClassCastException e) {
            throw new IllegalArgumentException("Can only bind to specific endpoints", e);
        }
        try {
            this.getAFImpl().bind(endpoint, this.getReuseAddress() ? 1 : 0);
        }
        catch (final SocketException e2) {
            if (bindErrorOk) {
                return;
            }
            throw e2;
        }
        this.setBoundEndpoint(this.getAFImpl().getLocalSocketAddress());
        if (this.boundEndpoint0() == null) {
            this.setBoundEndpoint(endpointCast);
        }
        if (endpoint == AFSocketAddress.INTERNAL_DUMMY_BIND) {
            return;
        }
        this.implementation.listen(backlog);
    }
    
    @Override
    public final boolean isBound() {
        return this.boundEndpoint0() != null && this.implementation.getFD().valid();
    }
    
    @Override
    public final boolean isClosed() {
        return super.isClosed() || (this.isBound() && !this.implementation.getFD().valid()) || this.implementation.isClosed();
    }
    
    @Override
    public AFSocket<A> accept() throws IOException {
        return this.accept1(true);
    }
    
    AFSocket<A> accept1(final boolean throwOnFail) throws IOException {
        final AFSocket<A> as = this.newSocketInstance();
        final boolean success = this.implementation.accept0(as.getAFImpl(false));
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
    
    protected abstract AFSocket<A> newSocketInstance() throws IOException;
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" + (Object)(this.isBound() ? this.boundEndpoint0() : "unbound") + "]";
    }
    
    @Override
    public synchronized void close() throws IOException {
        if (this.isClosed()) {
            return;
        }
        final boolean localSocketAddressValid = this.isLocalSocketAddressValid();
        final AFSocketAddress endpoint = this.boundEndpoint;
        IOException superException = null;
        try {
            super.close();
        }
        catch (final IOException e) {
            superException = e;
        }
        if (this.implementation != null) {
            try {
                this.implementation.close();
            }
            catch (final IOException e) {
                if (superException == null) {
                    superException = e;
                }
                else {
                    superException.addSuppressed(e);
                }
            }
        }
        IOException ex = null;
        try {
            this.closeables.close(superException);
        }
        finally {
            if (endpoint != null && endpoint.hasFilename() && localSocketAddressValid && this.isDeleteOnClose()) {
                final File f = endpoint.getFile();
                if (!f.delete() && f.exists()) {
                    ex = new IOException("Could not delete socket file after close: " + f);
                }
            }
        }
        if (ex != null) {
            throw ex;
        }
    }
    
    public final void addCloseable(final Closeable closeable) {
        this.closeables.add(closeable);
    }
    
    public final void removeCloseable(final Closeable closeable) {
        this.closeables.remove(closeable);
    }
    
    public static boolean isSupported() {
        return NativeUnixSocket.isLoaded();
    }
    
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    @Override
    public final A getLocalSocketAddress() {
        A ep = this.boundEndpoint0();
        if (ep == null) {
            ep = this.getAFImpl().getLocalSocketAddress();
            this.setBoundEndpoint(ep);
        }
        return ep;
    }
    
    private synchronized A boundEndpoint0() {
        return this.boundEndpoint;
    }
    
    public boolean isLocalSocketAddressValid() {
        if (this.isClosed()) {
            return false;
        }
        final A addr = this.getLocalSocketAddress();
        return addr != null && addr.equals(this.getAFImpl().getLocalSocketAddress());
    }
    
    final synchronized void setBoundEndpoint(final A addr) {
        this.boundEndpoint = addr;
        int port;
        if (addr == null) {
            port = -1;
        }
        else {
            port = addr.getPort();
        }
        this.getAFImpl().updatePorts(port, -1);
    }
    
    @Override
    public final int getLocalPort() {
        if (this.boundEndpoint0() == null) {
            this.setBoundEndpoint(this.getAFImpl().getLocalSocketAddress());
        }
        if (this.boundEndpoint0() == null) {
            return -1;
        }
        return this.getAFImpl().getLocalPort1();
    }
    
    public final boolean isDeleteOnClose() {
        return this.deleteOnClose.get();
    }
    
    public final void setDeleteOnClose(final boolean b) {
        this.deleteOnClose.set(b);
    }
    
    final AFSocketImpl<A> getAFImpl() {
        if (this.created.compareAndSet(false, true)) {
            try {
                this.getAFImpl().create(true);
                this.getSoTimeout();
            }
            catch (final IOException ex) {}
        }
        return this.implementation;
    }
    
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    @Override
    public AFServerSocketChannel<?> getChannel() {
        return this.channel;
    }
    
    @Override
    public final FileDescriptor getFileDescriptor() throws IOException {
        return this.implementation.getFileDescriptor();
    }
    
    protected final AFAddressFamily<A> addressFamily() {
        return this.getAFImpl().getAddressFamily();
    }
    
    public final AFServerSocket<A> bindHook(final SocketAddressFilter hook) {
        this.bindFilter = hook;
        return this;
    }
    
    @Override
    public void bind(final SocketAddress endpoint) throws IOException {
        this.bind(endpoint, 50);
    }
    
    @Override
    public InetAddress getInetAddress() {
        if (!this.isBound()) {
            return null;
        }
        return this.getAFImpl().getInetAddress();
    }
    
    @Override
    public synchronized void setReceiveBufferSize(final int size) throws SocketException {
        if (size <= 0) {
            throw new IllegalArgumentException("receive buffer size must be a positive number");
        }
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        this.getAFImpl().setOption(4098, size);
    }
    
    @Override
    public synchronized int getReceiveBufferSize() throws SocketException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        int result = 0;
        final Object o = this.getAFImpl().getOption(4098);
        if (o instanceof Number) {
            result = ((Number)o).intValue();
        }
        return result;
    }
    
    @Override
    public void setSoTimeout(final int timeout) throws SocketException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout < 0");
        }
        this.getAFImpl().setOption(4102, timeout);
    }
    
    @Override
    public int getSoTimeout() throws IOException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        final Object o = this.getAFImpl().getOption(4102);
        if (o instanceof Number) {
            return ((Number)o).intValue();
        }
        return 0;
    }
    
    @Override
    public void setReuseAddress(final boolean on) throws SocketException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        this.getAFImpl().setOption(4, on);
    }
    
    @Override
    public boolean getReuseAddress() throws SocketException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        return (boolean)this.getAFImpl().getOption(4);
    }
    
    @Override
    public void setPerformancePreferences(final int connectionTime, final int latency, final int bandwidth) {
    }
    
    @Override
    public <T> T getOption(final SocketOption<T> name) throws IOException {
        Objects.requireNonNull(name);
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        return this.getAFImpl().getOption(name);
    }
    
    @Override
    public <T> ServerSocket setOption(final SocketOption<T> name, final T value) throws IOException {
        Objects.requireNonNull(name);
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        this.getAFImpl().setOption(name, value);
        return this;
    }
    
    @Override
    public Set<SocketOption<?>> supportedOptions() {
        return this.getAFImpl().supportedOptions();
    }
    
    @Override
    public void setShutdownOnClose(final boolean enabled) {
        this.getAFImpl().getCore().setShutdownOnClose(enabled);
    }
    
    public interface Constructor<A extends AFSocketAddress>
    {
        AFServerSocket<A> newInstance(final FileDescriptor p0) throws IOException;
    }
}
