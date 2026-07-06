package org.newsclub.net.unix;

import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.newsclub.net.unix.AFSocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSocket.class */
public abstract class AFSocket<A extends AFSocketAddress> extends Socket implements AFSomeSocket, AFSocketExtensions {
    static final String PROP_LIBRARY_DISABLE_CAPABILITY_PREFIX = "org.newsclub.net.unix.library.disable.";
    static String loadedLibrary;
    private final AFSocketImpl<A> impl;
    private final AFSocketAddressFromHostname<A> afh;
    private final Closeables closeables;
    private final AtomicBoolean created;
    private final AFSocketChannel<A> channel;
    private SocketAddressFilter connectFilter;
    private static final byte[] ZERO_BYTES = new byte[0];
    private static Integer capabilitiesValue = null;

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSocket$Constructor.class */
    @FunctionalInterface
    public interface Constructor<A extends AFSocketAddress> {
        AFSocket<A> newInstance(FileDescriptor fileDescriptor, AFSocketFactory<A> aFSocketFactory) throws SocketException;
    }

    protected abstract AFSocketChannel<A> newChannel();

    @SuppressFBWarnings({"CT_CONSTRUCTOR_THROW"})
    protected AFSocket(AFSocketImpl<A> impl, AFSocketAddressFromHostname<A> afh) throws SocketException {
        super(impl);
        this.closeables = new Closeables();
        this.created = new AtomicBoolean(false);
        this.channel = newChannel();
        this.afh = afh;
        this.impl = impl;
    }

    protected final Class<? extends AFSocketAddress> socketAddressClass() {
        return getAFImpl(false).getAddressFamily().getSocketAddressClass();
    }

    static <A extends AFSocketAddress> AFSocket<A> newInstance(Constructor<A> constr, AFSocketFactory<A> sf, FileDescriptor fdObj, int localPort, int remotePort) throws IOException {
        if (!fdObj.valid()) {
            throw new SocketException("Invalid file descriptor");
        }
        int status = NativeUnixSocket.socketStatus(fdObj);
        if (status == -1) {
            throw new SocketException("Not a valid socket");
        }
        AFSocket<A> socket = newInstance0(constr, fdObj, sf);
        socket.getAFImpl().updatePorts(localPort, remotePort);
        switch (status) {
            case 0:
                break;
            case 1:
                socket.internalDummyBind();
                break;
            case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                socket.internalDummyConnect();
                break;
            default:
                throw new IllegalStateException("Invalid socketStatus response: " + status);
        }
        socket.getAFImpl().setSocketAddress(socket.getLocalSocketAddress());
        return socket;
    }

    protected static final <A extends AFSocketAddress> AFSocket<A> newInstance(Constructor<A> constr, AFSocketFactory<A> factory) throws SocketException {
        return newInstance0(constr, null, factory);
    }

    private static <A extends AFSocketAddress> AFSocket<A> newInstance0(Constructor<A> constr, FileDescriptor fdObj, AFSocketFactory<A> factory) throws SocketException {
        return constr.newInstance(fdObj, factory);
    }

    protected static final <A extends AFSocketAddress> AFSocket<A> connectTo(Constructor<A> constr, A addr) throws IOException {
        AFSocket<A> socket = constr.newInstance(null, null);
        socket.connect(addr);
        return socket;
    }

    public static final <A extends AFSocketAddress> AFSocket<?> connectTo(A a) throws IOException {
        AFSocket<A> aFSocketNewInstance = a.getAddressFamily().getSocketConstructor().newInstance(null, null);
        aFSocketNewInstance.connect(a);
        return aFSocketNewInstance;
    }

    @Override // java.net.Socket
    public final void bind(SocketAddress bindpoint) throws IOException {
        if (bindpoint == null) {
            throw new IllegalArgumentException();
        }
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (isBound()) {
            throw new SocketException("Already bound");
        }
        preprocessSocketAddress(bindpoint);
        throw new SocketException("Use AF*ServerSocket#bind or #bindOn");
    }

    @Override // java.net.Socket
    public final boolean isBound() {
        return this.impl.getFD().valid() && (super.isBound() || this.impl.isBound());
    }

    @Override // java.net.Socket
    public final boolean isConnected() {
        return this.impl.getFD().valid() && (super.isConnected() || this.impl.isConnected());
    }

    @Override // java.net.Socket
    public final void connect(SocketAddress endpoint) throws IOException {
        connect(endpoint, 0);
    }

    @Override // java.net.Socket
    public final void connect(SocketAddress endpoint, int timeout) throws IOException {
        connect0(endpoint, timeout);
    }

    private AFSocketAddress preprocessSocketAddress(SocketAddress endpoint) throws SocketException {
        if (endpoint == null) {
            throw new IllegalArgumentException("endpoint is null");
        }
        if (endpoint instanceof SentinelSocketAddress) {
            return (AFSocketAddress) endpoint;
        }
        return AFSocketAddress.preprocessSocketAddress(socketAddressClass(), endpoint, this.afh);
    }

    final boolean connect0(SocketAddress endpoint, int timeout) throws IOException {
        int port;
        if (timeout < 0) {
            throw new IllegalArgumentException("connect: timeout can't be negative");
        }
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (this.connectFilter != null) {
            endpoint = this.connectFilter.apply(endpoint);
        }
        AFSocketAddress address = preprocessSocketAddress(endpoint);
        if (!isBound()) {
            internalDummyBind();
        }
        boolean success = getAFImpl().connect0(address, timeout);
        if (success && (port = address.getPort()) > 0) {
            getAFImpl().updatePorts(getLocalPort(), port);
        }
        internalDummyConnect();
        return success;
    }

    final void internalDummyConnect() throws IOException {
        if (!isConnected()) {
            super.connect(AFSocketAddress.INTERNAL_DUMMY_CONNECT, 0);
        }
    }

    final void internalDummyBind() throws IOException {
        if (!isBound()) {
            super.bind(AFSocketAddress.INTERNAL_DUMMY_BIND);
        }
    }

    @Override // java.net.Socket
    public final String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode()) + toStringSuffix();
    }

    final String toStringSuffix() {
        if (this.impl.getFD().valid()) {
            return "[local=" + getLocalSocketAddress() + ";remote=" + getRemoteSocketAddress() + "]";
        }
        return "[invalid]";
    }

    public static boolean isSupported() {
        return NativeUnixSocket.isLoaded();
    }

    public static void ensureSupported() throws UnsupportedOperationException {
        NativeUnixSocket.ensureSupported();
    }

    public static final String getVersion() {
        String v = BuildProperties.getBuildProperties().get("git.build.version");
        if (v != null && !v.startsWith("$")) {
            return v;
        }
        try {
            return NativeLibraryLoader.getJunixsocketVersion();
        } catch (IOException e) {
            return null;
        }
    }

    public static final String getLoadedLibrary() {
        return loadedLibrary;
    }

    @Override // java.net.Socket
    public final boolean isClosed() {
        return super.isClosed() || (isConnected() && !this.impl.getFD().valid()) || this.impl.isClosed();
    }

    @Override // org.newsclub.net.unix.AFSocketExtensions
    public final int getAncillaryReceiveBufferSize() {
        return this.impl.getAncillaryReceiveBufferSize();
    }

    @Override // org.newsclub.net.unix.AFSocketExtensions
    public final void setAncillaryReceiveBufferSize(int size) {
        this.impl.setAncillaryReceiveBufferSize(size);
    }

    @Override // org.newsclub.net.unix.AFSocketExtensions
    public final void ensureAncillaryReceiveBufferSize(int minSize) {
        this.impl.ensureAncillaryReceiveBufferSize(minSize);
    }

    private static boolean isCapDisabled(AFSocketCapability cap) {
        return Boolean.parseBoolean(System.getProperty(PROP_LIBRARY_DISABLE_CAPABILITY_PREFIX + cap.name(), "false"));
    }

    private static int initCapabilities() {
        if (!isSupported()) {
            return 0;
        }
        int v = NativeUnixSocket.capabilities();
        if (System.getProperty("osv.version") != null) {
            v &= AFSocketCapability.CAPABILITY_FD_AS_REDIRECT.getBitmask() ^ (-1);
        }
        for (AFSocketCapability cap : AFSocketCapability.values()) {
            if (isCapDisabled(cap)) {
                v &= cap.getBitmask() ^ (-1);
            }
        }
        return v;
    }

    private static synchronized int capabilities() {
        if (capabilitiesValue == null) {
            capabilitiesValue = Integer.valueOf(initCapabilities());
        }
        return capabilitiesValue.intValue();
    }

    @Deprecated
    public static final boolean supports(AFUNIXSocketCapability capability) {
        return (capabilities() & capability.getBitmask()) != 0;
    }

    public static final boolean supports(AFSocketCapability capability) {
        return (capabilities() & capability.getBitmask()) != 0;
    }

    public static final void ensureUnsafeSupported() throws IOException {
        if (!supports(AFSocketCapability.CAPABILITY_UNSAFE)) {
            throw new IOException("Unsafe operations are not supported in this environment");
        }
    }

    @Override // java.net.Socket, java.io.Closeable, java.lang.AutoCloseable
    public final synchronized void close() throws IOException {
        IOException superException = null;
        try {
            super.close();
        } catch (IOException e) {
            superException = e;
        }
        this.closeables.close(superException);
    }

    public final void addCloseable(Closeable closeable) {
        this.closeables.add(closeable);
    }

    public final void removeCloseable(Closeable closeable) {
        this.closeables.remove(closeable);
    }

    final AFSocketImpl<A> getAFImpl() {
        return getAFImpl(true);
    }

    final AFSocketImpl<A> getAFImpl(boolean createSocket) {
        if (createSocket && this.created.compareAndSet(false, true)) {
            try {
                getSoTimeout();
            } catch (SocketException e) {
            }
        }
        return this.impl;
    }

    @Override // java.net.Socket
    @SuppressFBWarnings({"EI_EXPOSE_REP"})
    public AFSocketChannel<A> getChannel() {
        return this.channel;
    }

    @Override // java.net.Socket, org.newsclub.net.unix.AFSomeSocket
    public final synchronized A getRemoteSocketAddress() {
        if (!isConnected()) {
            return null;
        }
        return (A) this.impl.getRemoteSocketAddress();
    }

    @Override // java.net.Socket, org.newsclub.net.unix.AFSomeSocketThing
    public final A getLocalSocketAddress() {
        if (isClosed()) {
            return null;
        }
        return (A) this.impl.getLocalSocketAddress();
    }

    @Override // org.newsclub.net.unix.FileDescriptorAccess
    public final FileDescriptor getFileDescriptor() throws IOException {
        return this.impl.getFileDescriptor();
    }

    @Override // java.net.Socket
    public final AFInputStream getInputStream() throws IOException {
        return getAFImpl().getInputStream();
    }

    @Override // java.net.Socket
    public final AFOutputStream getOutputStream() throws IOException {
        return getAFImpl().getOutputStream();
    }

    protected final AFSocketImplExtensions<A> getImplExtensions() {
        return getAFImpl(false).getImplExtensions();
    }

    public final AFSocket<A> forceConnectAddress(SocketAddress endpoint) {
        return connectHook(orig -> {
            if (orig == null) {
                return null;
            }
            return endpoint;
        });
    }

    public final AFSocket<A> connectHook(SocketAddressFilter hook) {
        this.connectFilter = hook;
        return this;
    }

    public boolean checkConnectionClosed() throws IOException {
        if (!isConnected()) {
            return true;
        }
        try {
            if (!supports(AFSocketCapability.CAPABILITY_ZERO_LENGTH_SEND)) {
                return false;
            }
            getOutputStream().write(ZERO_BYTES);
            return false;
        } catch (SocketClosedException e) {
            return true;
        } catch (IOException e2) {
            if (!isConnected()) {
                return true;
            }
            throw e2;
        }
    }

    public static boolean isRunningOnAndroid() {
        return NativeLibraryLoader.isAndroid();
    }

    @Override // org.newsclub.net.unix.AFSomeSocketThing
    public void setShutdownOnClose(boolean enabled) {
        getAFImpl().getCore().setShutdownOnClose(enabled);
    }
}
