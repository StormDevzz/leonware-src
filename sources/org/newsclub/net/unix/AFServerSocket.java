package org.newsclub.net.unix;

import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketOption;
import java.nio.channels.IllegalBlockingModeException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.newsclub.net.unix.AFSocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFServerSocket.class */
public abstract class AFServerSocket<A extends AFSocketAddress> extends ServerSocket implements AFSomeSocketThing {
    private final AFSocketImpl<A> implementation;
    private A boundEndpoint;
    private final Closeables closeables;
    private final AtomicBoolean created;
    private final AtomicBoolean deleteOnClose;
    private final AFServerSocketChannel<?> channel;
    private SocketAddressFilter bindFilter;

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFServerSocket$Constructor.class */
    public interface Constructor<A extends AFSocketAddress> {
        AFServerSocket<A> newInstance(FileDescriptor fileDescriptor) throws IOException;
    }

    protected abstract AFServerSocketChannel<?> newChannel();

    protected abstract AFSocketImpl<A> newImpl(FileDescriptor fileDescriptor) throws IOException;

    protected abstract AFSocket<A> newSocketInstance() throws IOException;

    @SuppressFBWarnings({"CT_CONSTRUCTOR_THROW"})
    protected AFServerSocket() throws IOException {
        this(null);
    }

    @SuppressFBWarnings({"CT_CONSTRUCTOR_THROW"})
    protected AFServerSocket(FileDescriptor fdObj) throws IOException {
        this.closeables = new Closeables();
        this.created = new AtomicBoolean(false);
        this.deleteOnClose = new AtomicBoolean(true);
        this.channel = newChannel();
        this.implementation = newImpl(fdObj);
        NativeUnixSocket.initServerImpl(this, this.implementation);
        getAFImpl().setOption(4, (Object) true);
    }

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
            case 0:
                break;
            case 1:
                socket.bind(AFSocketAddress.INTERNAL_DUMMY_BIND);
                socket.setBoundEndpoint(AFSocketAddress.getSocketAddress(fdObj, false, localPort, socket.addressFamily()));
                break;
            case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                throw new SocketException("Not a ServerSocket");
            default:
                throw new IllegalStateException("Invalid socketStatus response: " + status);
        }
        socket.getAFImpl().setSocketAddress(socket.getLocalSocketAddress());
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
        return bindHook(orig -> {
            if (orig == null) {
                return null;
            }
            return endpoint;
        });
    }

    @Override // java.net.ServerSocket
    public final void bind(SocketAddress endpoint, int backlog) throws IOException {
        boolean bindErrorOk;
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (this.bindFilter != null) {
            endpoint = this.bindFilter.apply(endpoint);
            bindErrorOk = endpoint != null && isBound();
        } else {
            bindErrorOk = false;
        }
        if (!(endpoint instanceof AFSocketAddress)) {
            throw new IllegalArgumentException("Can only bind to endpoints of type " + AFSocketAddress.class.getName() + ": " + endpoint);
        }
        try {
            AFSocketAddress aFSocketAddress = (AFSocketAddress) endpoint;
            try {
                getAFImpl().bind(endpoint, getReuseAddress() ? 1 : 0);
                setBoundEndpoint(getAFImpl().getLocalSocketAddress());
                if (boundEndpoint0() == null) {
                    setBoundEndpoint(aFSocketAddress);
                }
                if (endpoint == AFSocketAddress.INTERNAL_DUMMY_BIND) {
                    return;
                }
                this.implementation.listen(backlog);
            } catch (SocketException e) {
                if (bindErrorOk) {
                } else {
                    throw e;
                }
            }
        } catch (ClassCastException e2) {
            throw new IllegalArgumentException("Can only bind to specific endpoints", e2);
        }
    }

    @Override // java.net.ServerSocket
    public final boolean isBound() {
        return boundEndpoint0() != null && this.implementation.getFD().valid();
    }

    @Override // java.net.ServerSocket
    public final boolean isClosed() {
        return super.isClosed() || (isBound() && !this.implementation.getFD().valid()) || this.implementation.isClosed();
    }

    @Override // java.net.ServerSocket
    public AFSocket<A> accept() throws IOException {
        return accept1(true);
    }

    AFSocket<A> accept1(boolean throwOnFail) throws IOException {
        AFSocket<A> as = newSocketInstance();
        boolean success = this.implementation.accept0(as.getAFImpl(false));
        if (isClosed()) {
            throw new SocketClosedException("Socket is closed");
        }
        if (!success) {
            if (!throwOnFail || getChannel().isBlocking()) {
                return null;
            }
            throw new IllegalBlockingModeException();
        }
        as.getAFImpl(true);
        as.connect(AFSocketAddress.INTERNAL_DUMMY_CONNECT);
        as.getAFImpl().updatePorts(getAFImpl().getLocalPort1(), getAFImpl().getRemotePort());
        return as;
    }

    @Override // java.net.ServerSocket
    public String toString() {
        return getClass().getSimpleName() + "[" + (isBound() ? boundEndpoint0() : "unbound") + "]";
    }

    @Override // java.net.ServerSocket, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        if (isClosed()) {
            return;
        }
        boolean localSocketAddressValid = isLocalSocketAddressValid();
        AFSocketAddress endpoint = this.boundEndpoint;
        IOException superException = null;
        try {
            super.close();
        } catch (IOException e) {
            superException = e;
        }
        if (this.implementation != null) {
            try {
                this.implementation.close();
            } catch (IOException e2) {
                if (superException == null) {
                    superException = e2;
                } else {
                    superException.addSuppressed(e2);
                }
            }
        }
        IOException ex = null;
        try {
            this.closeables.close(superException);
            if (endpoint != null && endpoint.hasFilename() && localSocketAddressValid && isDeleteOnClose()) {
                File f = endpoint.getFile();
                if (!f.delete() && f.exists()) {
                    ex = new IOException("Could not delete socket file after close: " + f);
                }
            }
            if (ex != null) {
                throw ex;
            }
        } catch (Throwable th) {
            if (endpoint != null && endpoint.hasFilename() && localSocketAddressValid && isDeleteOnClose()) {
                File f2 = endpoint.getFile();
                if (!f2.delete() && f2.exists()) {
                    new IOException("Could not delete socket file after close: " + f2);
                }
            }
            throw th;
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

    @Override // java.net.ServerSocket, org.newsclub.net.unix.AFSomeSocketThing
    @SuppressFBWarnings({"EI_EXPOSE_REP"})
    public final A getLocalSocketAddress() {
        AFSocketAddress aFSocketAddressBoundEndpoint0 = boundEndpoint0();
        if (aFSocketAddressBoundEndpoint0 == null) {
            aFSocketAddressBoundEndpoint0 = getAFImpl().getLocalSocketAddress();
            setBoundEndpoint(aFSocketAddressBoundEndpoint0);
        }
        return (A) aFSocketAddressBoundEndpoint0;
    }

    private synchronized A boundEndpoint0() {
        return this.boundEndpoint;
    }

    public boolean isLocalSocketAddressValid() {
        AFSocketAddress localSocketAddress;
        if (isClosed() || (localSocketAddress = getLocalSocketAddress()) == null) {
            return false;
        }
        return localSocketAddress.equals(getAFImpl().getLocalSocketAddress());
    }

    final synchronized void setBoundEndpoint(A addr) {
        int port;
        this.boundEndpoint = addr;
        if (addr == null) {
            port = -1;
        } else {
            port = addr.getPort();
        }
        getAFImpl().updatePorts(port, -1);
    }

    @Override // java.net.ServerSocket
    public final int getLocalPort() {
        if (boundEndpoint0() == null) {
            setBoundEndpoint(getAFImpl().getLocalSocketAddress());
        }
        if (boundEndpoint0() == null) {
            return -1;
        }
        return getAFImpl().getLocalPort1();
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
                getAFImpl().create(true);
                getSoTimeout();
            } catch (IOException e) {
            }
        }
        return this.implementation;
    }

    @Override // java.net.ServerSocket
    @SuppressFBWarnings({"EI_EXPOSE_REP"})
    public AFServerSocketChannel<?> getChannel() {
        return this.channel;
    }

    @Override // org.newsclub.net.unix.FileDescriptorAccess
    public final FileDescriptor getFileDescriptor() throws IOException {
        return this.implementation.getFileDescriptor();
    }

    protected final AFAddressFamily<A> addressFamily() {
        return getAFImpl().getAddressFamily();
    }

    public final AFServerSocket<A> bindHook(SocketAddressFilter hook) {
        this.bindFilter = hook;
        return this;
    }

    @Override // java.net.ServerSocket
    public void bind(SocketAddress endpoint) throws IOException {
        bind(endpoint, 50);
    }

    @Override // java.net.ServerSocket
    public InetAddress getInetAddress() {
        if (!isBound()) {
            return null;
        }
        return getAFImpl().getInetAddress();
    }

    @Override // java.net.ServerSocket
    public synchronized void setReceiveBufferSize(int size) throws SocketException {
        if (size <= 0) {
            throw new IllegalArgumentException("receive buffer size must be a positive number");
        }
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        getAFImpl().setOption(4098, Integer.valueOf(size));
    }

    @Override // java.net.ServerSocket
    public synchronized int getReceiveBufferSize() throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        int result = 0;
        Object o = getAFImpl().getOption(4098);
        if (o instanceof Number) {
            result = ((Number) o).intValue();
        }
        return result;
    }

    @Override // java.net.ServerSocket
    public void setSoTimeout(int timeout) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout < 0");
        }
        getAFImpl().setOption(4102, Integer.valueOf(timeout));
    }

    @Override // java.net.ServerSocket
    public int getSoTimeout() throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        Object o = getAFImpl().getOption(4102);
        if (o instanceof Number) {
            return ((Number) o).intValue();
        }
        return 0;
    }

    @Override // java.net.ServerSocket
    public void setReuseAddress(boolean on) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        getAFImpl().setOption(4, Boolean.valueOf(on));
    }

    @Override // java.net.ServerSocket
    public boolean getReuseAddress() throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        return ((Boolean) getAFImpl().getOption(4)).booleanValue();
    }

    @Override // java.net.ServerSocket
    public void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
    }

    @Override // java.net.ServerSocket
    public <T> T getOption(SocketOption<T> socketOption) throws IOException {
        Objects.requireNonNull(socketOption);
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        return (T) getAFImpl().getOption(socketOption);
    }

    @Override // java.net.ServerSocket
    public <T> ServerSocket setOption(SocketOption<T> name, T value) throws IOException {
        Objects.requireNonNull(name);
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        getAFImpl().setOption(name, value);
        return this;
    }

    @Override // java.net.ServerSocket
    public Set<SocketOption<?>> supportedOptions() {
        return getAFImpl().supportedOptions();
    }

    @Override // org.newsclub.net.unix.AFSomeSocketThing
    public void setShutdownOnClose(boolean enabled) {
        getAFImpl().getCore().setShutdownOnClose(enabled);
    }
}
