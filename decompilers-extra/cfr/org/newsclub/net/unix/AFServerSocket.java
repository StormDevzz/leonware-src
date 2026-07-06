/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.kohlschutter.annotations.compiletime.SuppressFBWarnings
 *  org.eclipse.jdt.annotation.NonNull
 *  org.eclipse.jdt.annotation.Nullable
 */
package org.newsclub.net.unix;

import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketOption;
import java.nio.channels.IllegalBlockingModeException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.newsclub.net.unix.AFAddressFamily;
import org.newsclub.net.unix.AFServerSocketChannel;
import org.newsclub.net.unix.AFSocket;
import org.newsclub.net.unix.AFSocketAddress;
import org.newsclub.net.unix.AFSocketImpl;
import org.newsclub.net.unix.AFSomeSocketThing;
import org.newsclub.net.unix.Closeables;
import org.newsclub.net.unix.NativeUnixSocket;
import org.newsclub.net.unix.SocketAddressFilter;
import org.newsclub.net.unix.SocketClosedException;

public abstract class AFServerSocket<A extends AFSocketAddress>
extends ServerSocket
implements AFSomeSocketThing {
    private final AFSocketImpl<A> implementation;
    private @Nullable A boundEndpoint;
    private final Closeables closeables = new Closeables();
    private final AtomicBoolean created = new AtomicBoolean(false);
    private final AtomicBoolean deleteOnClose = new AtomicBoolean(true);
    private final AFServerSocketChannel<?> channel = this.newChannel();
    private @Nullable SocketAddressFilter bindFilter;

    @SuppressFBWarnings(value={"CT_CONSTRUCTOR_THROW"})
    protected AFServerSocket() throws IOException {
        this((FileDescriptor)null);
    }

    @SuppressFBWarnings(value={"CT_CONSTRUCTOR_THROW"})
    protected AFServerSocket(FileDescriptor fdObj) throws IOException {
        this.implementation = this.newImpl(fdObj);
        NativeUnixSocket.initServerImpl(this, this.implementation);
        this.getAFImpl().setOption(4, (Object)true);
    }

    protected abstract AFServerSocketChannel<?> newChannel();

    protected abstract AFSocketImpl<A> newImpl(FileDescriptor var1) throws IOException;

    protected static <A extends AFSocketAddress> AFServerSocket<A> newInstance(Constructor<A> instanceSupplier) throws IOException {
        return instanceSupplier.newInstance(null);
    }

    protected static <A extends AFSocketAddress> AFServerSocket<A> newInstance(Constructor<A> instanceSupplier, FileDescriptor fdObj, int localPort, int remotePort) throws IOException {
        if (fdObj == null) {
            return instanceSupplier.newInstance(null);
        }
        int status = NativeUnixSocket.socketStatus(fdObj);
        if (!fdObj.valid() || status == -1) {
            throw new SocketException("Not a valid socket");
        }
        AFServerSocket<A> socket = instanceSupplier.newInstance(fdObj);
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
        socket.getAFImpl().setSocketAddress((AFSocketAddress)socket.getLocalSocketAddress());
        return socket;
    }

    protected static <A extends AFSocketAddress> AFServerSocket<A> bindOn(Constructor<A> instanceSupplier, AFSocketAddress addr) throws IOException {
        AFServerSocket<A> socket = instanceSupplier.newInstance(null);
        socket.bind(addr);
        return socket;
    }

    protected static <A extends AFSocketAddress> AFServerSocket<A> bindOn(Constructor<A> instanceSupplier, A addr, boolean deleteOnClose) throws IOException {
        AFServerSocket<A> socket = instanceSupplier.newInstance(null);
        socket.bind(addr);
        socket.setDeleteOnClose(deleteOnClose);
        return socket;
    }

    protected static <A extends AFSocketAddress> AFServerSocket<A> forceBindOn(Constructor<A> instanceSupplier, A forceAddr) throws IOException {
        AFServerSocket<A> socket = instanceSupplier.newInstance(null);
        return socket.forceBindAddress(forceAddr);
    }

    public final AFServerSocket<A> forceBindAddress(SocketAddress endpoint) {
        return this.bindHook(orig -> orig == null ? null : endpoint);
    }

    @Override
    public final void bind(SocketAddress endpoint, int backlog) throws IOException {
        AFSocketAddress endpointCast;
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        boolean bindErrorOk = this.bindFilter != null ? (endpoint = this.bindFilter.apply(endpoint)) != null && this.isBound() : false;
        if (!(endpoint instanceof AFSocketAddress)) {
            throw new IllegalArgumentException("Can only bind to endpoints of type " + AFSocketAddress.class.getName() + ": " + endpoint);
        }
        try {
            endpointCast = (AFSocketAddress)endpoint;
        }
        catch (ClassCastException e) {
            throw new IllegalArgumentException("Can only bind to specific endpoints", e);
        }
        try {
            this.getAFImpl().bind(endpoint, this.getReuseAddress() ? 1 : 0);
        }
        catch (SocketException e) {
            if (bindErrorOk) {
                return;
            }
            throw e;
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
        return super.isClosed() || this.isBound() && !this.implementation.getFD().valid() || this.implementation.isClosed();
    }

    @Override
    public AFSocket<A> accept() throws IOException {
        return this.accept1(true);
    }

    AFSocket<A> accept1(boolean throwOnFail) throws IOException {
        AFSocket<A> as = this.newSocketInstance();
        boolean success = this.implementation.accept0(as.getAFImpl(false));
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

    protected abstract AFSocket<A> newSocketInstance() throws IOException;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" + (this.isBound() ? this.boundEndpoint0() : "unbound") + "]";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public synchronized void close() throws IOException {
        if (this.isClosed()) {
            return;
        }
        boolean localSocketAddressValid = this.isLocalSocketAddressValid();
        A endpoint = this.boundEndpoint;
        IOException superException = null;
        try {
            super.close();
        }
        catch (IOException e) {
            superException = e;
        }
        if (this.implementation != null) {
            try {
                this.implementation.close();
            }
            catch (IOException e) {
                if (superException == null) {
                    superException = e;
                }
                superException.addSuppressed(e);
            }
        }
        IOException ex = null;
        try {
            this.closeables.close(superException);
        }
        finally {
            File f;
            if (endpoint != null && ((AFSocketAddress)endpoint).hasFilename() && localSocketAddressValid && this.isDeleteOnClose() && !(f = ((AFSocketAddress)endpoint).getFile()).delete() && f.exists()) {
                ex = new IOException("Could not delete socket file after close: " + f);
            }
        }
        if (ex != null) {
            throw ex;
        }
    }

    public final void addCloseable(Closeable closeable) {
        this.closeables.add(closeable);
    }

    public final void removeCloseable(Closeable closeable) {
        this.closeables.remove(closeable);
    }

    public static boolean isSupported() {
        return NativeUnixSocket.isLoaded();
    }

    @SuppressFBWarnings(value={"EI_EXPOSE_REP"})
    public final @Nullable A getLocalSocketAddress() {
        @Nullable A ep = this.boundEndpoint0();
        if (ep == null) {
            ep = this.getAFImpl().getLocalSocketAddress();
            this.setBoundEndpoint(ep);
        }
        return ep;
    }

    private synchronized @Nullable A boundEndpoint0() {
        return this.boundEndpoint;
    }

    public boolean isLocalSocketAddressValid() {
        if (this.isClosed()) {
            return false;
        }
        @Nullable SocketAddress addr = this.getLocalSocketAddress();
        if (addr == null) {
            return false;
        }
        return ((InetSocketAddress)addr).equals(this.getAFImpl().getLocalSocketAddress());
    }

    final synchronized void setBoundEndpoint(@Nullable A addr) {
        this.boundEndpoint = addr;
        int port = addr == null ? -1 : ((InetSocketAddress)addr).getPort();
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

    public final void setDeleteOnClose(boolean b) {
        this.deleteOnClose.set(b);
    }

    final AFSocketImpl<A> getAFImpl() {
        if (this.created.compareAndSet(false, true)) {
            try {
                this.getAFImpl().create(true);
                this.getSoTimeout();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        return this.implementation;
    }

    @Override
    @SuppressFBWarnings(value={"EI_EXPOSE_REP"})
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

    public final AFServerSocket<A> bindHook(SocketAddressFilter hook) {
        this.bindFilter = hook;
        return this;
    }

    @Override
    public void bind(SocketAddress endpoint) throws IOException {
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
    public synchronized void setReceiveBufferSize(int size) throws SocketException {
        if (size <= 0) {
            throw new IllegalArgumentException("receive buffer size must be a positive number");
        }
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        this.getAFImpl().setOption(4098, (Object)size);
    }

    @Override
    public synchronized int getReceiveBufferSize() throws SocketException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        int result = 0;
        Object o = this.getAFImpl().getOption(4098);
        if (o instanceof Number) {
            result = ((Number)o).intValue();
        }
        return result;
    }

    @Override
    public void setSoTimeout(int timeout) throws SocketException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout < 0");
        }
        this.getAFImpl().setOption(4102, (Object)timeout);
    }

    @Override
    public int getSoTimeout() throws IOException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        Object o = this.getAFImpl().getOption(4102);
        if (o instanceof Number) {
            return ((Number)o).intValue();
        }
        return 0;
    }

    @Override
    public void setReuseAddress(boolean on) throws SocketException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        this.getAFImpl().setOption(4, (Object)on);
    }

    @Override
    public boolean getReuseAddress() throws SocketException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        return (Boolean)this.getAFImpl().getOption(4);
    }

    @Override
    public void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
    }

    @Override
    public <T> T getOption(SocketOption<T> name) throws IOException {
        Objects.requireNonNull(name);
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        return this.getAFImpl().getOption(name);
    }

    @Override
    public <T> ServerSocket setOption(SocketOption<T> name, T value) throws IOException {
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
    public void setShutdownOnClose(boolean enabled) {
        this.getAFImpl().getCore().setShutdownOnClose(enabled);
    }

    public static interface Constructor<A extends AFSocketAddress> {
        public @NonNull AFServerSocket<A> newInstance(FileDescriptor var1) throws IOException;
    }
}

