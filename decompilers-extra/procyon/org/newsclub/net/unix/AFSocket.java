// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.nio.channels.SocketChannel;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Closeable;
import java.net.SocketAddress;
import java.io.IOException;
import java.io.FileDescriptor;
import java.net.SocketException;
import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.net.SocketImpl;
import java.util.concurrent.atomic.AtomicBoolean;
import java.net.Socket;

public abstract class AFSocket<A extends AFSocketAddress> extends Socket implements AFSomeSocket, AFSocketExtensions
{
    static final String PROP_LIBRARY_DISABLE_CAPABILITY_PREFIX = "org.newsclub.net.unix.library.disable.";
    private static final byte[] ZERO_BYTES;
    static String loadedLibrary;
    private static Integer capabilitiesValue;
    private final AFSocketImpl<A> impl;
    private final AFSocketAddressFromHostname<A> afh;
    private final Closeables closeables;
    private final AtomicBoolean created;
    private final AFSocketChannel<A> channel;
    private SocketAddressFilter connectFilter;
    
    @SuppressFBWarnings({ "CT_CONSTRUCTOR_THROW" })
    protected AFSocket(final AFSocketImpl<A> impl, final AFSocketAddressFromHostname<A> afh) throws SocketException {
        super(impl);
        this.closeables = new Closeables();
        this.created = new AtomicBoolean(false);
        this.channel = this.newChannel();
        this.afh = afh;
        this.impl = impl;
    }
    
    protected final Class<? extends AFSocketAddress> socketAddressClass() {
        return this.getAFImpl(false).getAddressFamily().getSocketAddressClass();
    }
    
    protected abstract AFSocketChannel<A> newChannel();
    
    static <A extends AFSocketAddress> AFSocket<A> newInstance(final Constructor<A> constr, final AFSocketFactory<A> sf, final FileDescriptor fdObj, final int localPort, final int remotePort) throws IOException {
        if (!fdObj.valid()) {
            throw new SocketException("Invalid file descriptor");
        }
        final int status = NativeUnixSocket.socketStatus(fdObj);
        if (status == -1) {
            throw new SocketException("Not a valid socket");
        }
        final AFSocket<A> socket = newInstance0(constr, fdObj, sf);
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
        socket.getAFImpl().setSocketAddress(socket.getLocalSocketAddress());
        return socket;
    }
    
    protected static final <A extends AFSocketAddress> AFSocket<A> newInstance(final Constructor<A> constr, final AFSocketFactory<A> factory) throws SocketException {
        return newInstance0(constr, null, factory);
    }
    
    private static <A extends AFSocketAddress> AFSocket<A> newInstance0(final Constructor<A> constr, final FileDescriptor fdObj, final AFSocketFactory<A> factory) throws SocketException {
        return constr.newInstance(fdObj, factory);
    }
    
    protected static final <A extends AFSocketAddress> AFSocket<A> connectTo(final Constructor<A> constr, final A addr) throws IOException {
        final AFSocket<A> socket = constr.newInstance(null, null);
        socket.connect(addr);
        return socket;
    }
    
    public static final <A extends AFSocketAddress> AFSocket<?> connectTo(final A addr) throws IOException {
        final AFSocket<?> socket = addr.getAddressFamily().getSocketConstructor().newInstance(null, null);
        socket.connect(addr);
        return socket;
    }
    
    @Override
    public final void bind(final SocketAddress bindpoint) throws IOException {
        if (bindpoint == null) {
            throw new IllegalArgumentException();
        }
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (this.isBound()) {
            throw new SocketException("Already bound");
        }
        this.preprocessSocketAddress(bindpoint);
        throw new SocketException("Use AF*ServerSocket#bind or #bindOn");
    }
    
    @Override
    public final boolean isBound() {
        return this.impl.getFD().valid() && (super.isBound() || this.impl.isBound());
    }
    
    @Override
    public final boolean isConnected() {
        return this.impl.getFD().valid() && (super.isConnected() || this.impl.isConnected());
    }
    
    @Override
    public final void connect(final SocketAddress endpoint) throws IOException {
        this.connect(endpoint, 0);
    }
    
    @Override
    public final void connect(final SocketAddress endpoint, final int timeout) throws IOException {
        this.connect0(endpoint, timeout);
    }
    
    private AFSocketAddress preprocessSocketAddress(final SocketAddress endpoint) throws SocketException {
        if (endpoint == null) {
            throw new IllegalArgumentException("endpoint is null");
        }
        if (endpoint instanceof SentinelSocketAddress) {
            return (AFSocketAddress)endpoint;
        }
        return AFSocketAddress.preprocessSocketAddress(this.socketAddressClass(), endpoint, this.afh);
    }
    
    final boolean connect0(SocketAddress endpoint, final int timeout) throws IOException {
        if (timeout < 0) {
            throw new IllegalArgumentException("connect: timeout can't be negative");
        }
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (this.connectFilter != null) {
            endpoint = this.connectFilter.apply(endpoint);
        }
        final AFSocketAddress address = this.preprocessSocketAddress(endpoint);
        if (!this.isBound()) {
            this.internalDummyBind();
        }
        final boolean success = this.getAFImpl().connect0(address, timeout);
        if (success) {
            final int port = address.getPort();
            if (port > 0) {
                this.getAFImpl().updatePorts(this.getLocalPort(), port);
            }
        }
        this.internalDummyConnect();
        return success;
    }
    
    final void internalDummyConnect() throws IOException {
        if (!this.isConnected()) {
            super.connect(AFSocketAddress.INTERNAL_DUMMY_CONNECT, 0);
        }
    }
    
    final void internalDummyBind() throws IOException {
        if (!this.isBound()) {
            super.bind(AFSocketAddress.INTERNAL_DUMMY_BIND);
        }
    }
    
    @Override
    public final String toString() {
        return this.getClass().getName() + "@" + Integer.toHexString(this.hashCode()) + this.toStringSuffix();
    }
    
    final String toStringSuffix() {
        if (this.impl.getFD().valid()) {
            return "[local=" + this.getLocalSocketAddress() + ";remote=" + this.getRemoteSocketAddress() + "]";
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
        final String v = BuildProperties.getBuildProperties().get("git.build.version");
        if (v != null && !v.startsWith("$")) {
            return v;
        }
        try {
            return NativeLibraryLoader.getJunixsocketVersion();
        }
        catch (final IOException e) {
            return null;
        }
    }
    
    public static final String getLoadedLibrary() {
        return AFSocket.loadedLibrary;
    }
    
    @Override
    public final boolean isClosed() {
        return super.isClosed() || (this.isConnected() && !this.impl.getFD().valid()) || this.impl.isClosed();
    }
    
    @Override
    public final int getAncillaryReceiveBufferSize() {
        return this.impl.getAncillaryReceiveBufferSize();
    }
    
    @Override
    public final void setAncillaryReceiveBufferSize(final int size) {
        this.impl.setAncillaryReceiveBufferSize(size);
    }
    
    @Override
    public final void ensureAncillaryReceiveBufferSize(final int minSize) {
        this.impl.ensureAncillaryReceiveBufferSize(minSize);
    }
    
    private static boolean isCapDisabled(final AFSocketCapability cap) {
        return Boolean.parseBoolean(System.getProperty("org.newsclub.net.unix.library.disable." + cap.name(), "false"));
    }
    
    private static int initCapabilities() {
        if (!isSupported()) {
            return 0;
        }
        int v = NativeUnixSocket.capabilities();
        if (System.getProperty("osv.version") != null) {
            v &= ~AFSocketCapability.CAPABILITY_FD_AS_REDIRECT.getBitmask();
        }
        for (final AFSocketCapability cap : AFSocketCapability.values()) {
            if (isCapDisabled(cap)) {
                v &= ~cap.getBitmask();
            }
        }
        return v;
    }
    
    private static synchronized int capabilities() {
        if (AFSocket.capabilitiesValue == null) {
            AFSocket.capabilitiesValue = initCapabilities();
        }
        return AFSocket.capabilitiesValue;
    }
    
    @Deprecated
    public static final boolean supports(final AFUNIXSocketCapability capability) {
        return (capabilities() & capability.getBitmask()) != 0x0;
    }
    
    public static final boolean supports(final AFSocketCapability capability) {
        return (capabilities() & capability.getBitmask()) != 0x0;
    }
    
    public static final void ensureUnsafeSupported() throws IOException {
        if (!supports(AFSocketCapability.CAPABILITY_UNSAFE)) {
            throw new IOException("Unsafe operations are not supported in this environment");
        }
    }
    
    @Override
    public final synchronized void close() throws IOException {
        IOException superException = null;
        try {
            super.close();
        }
        catch (final IOException e) {
            superException = e;
        }
        this.closeables.close(superException);
    }
    
    public final void addCloseable(final Closeable closeable) {
        this.closeables.add(closeable);
    }
    
    public final void removeCloseable(final Closeable closeable) {
        this.closeables.remove(closeable);
    }
    
    final AFSocketImpl<A> getAFImpl() {
        return this.getAFImpl(true);
    }
    
    final AFSocketImpl<A> getAFImpl(final boolean createSocket) {
        if (createSocket && this.created.compareAndSet(false, true)) {
            try {
                this.getSoTimeout();
            }
            catch (final SocketException ex) {}
        }
        return this.impl;
    }
    
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    @Override
    public AFSocketChannel<A> getChannel() {
        return this.channel;
    }
    
    @Override
    public final synchronized A getRemoteSocketAddress() {
        if (!this.isConnected()) {
            return null;
        }
        return this.impl.getRemoteSocketAddress();
    }
    
    @Override
    public final A getLocalSocketAddress() {
        if (this.isClosed()) {
            return null;
        }
        return this.impl.getLocalSocketAddress();
    }
    
    @Override
    public final FileDescriptor getFileDescriptor() throws IOException {
        return this.impl.getFileDescriptor();
    }
    
    @Override
    public final AFInputStream getInputStream() throws IOException {
        return this.getAFImpl().getInputStream();
    }
    
    @Override
    public final AFOutputStream getOutputStream() throws IOException {
        return this.getAFImpl().getOutputStream();
    }
    
    protected final AFSocketImplExtensions<A> getImplExtensions() {
        return this.getAFImpl(false).getImplExtensions();
    }
    
    public final AFSocket<A> forceConnectAddress(final SocketAddress endpoint) {
        return this.connectHook(orig -> (orig == null) ? null : endpoint);
    }
    
    public final AFSocket<A> connectHook(final SocketAddressFilter hook) {
        this.connectFilter = hook;
        return this;
    }
    
    public boolean checkConnectionClosed() throws IOException {
        if (!this.isConnected()) {
            return true;
        }
        try {
            if (!supports(AFSocketCapability.CAPABILITY_ZERO_LENGTH_SEND)) {
                return false;
            }
            this.getOutputStream().write(AFSocket.ZERO_BYTES);
            return false;
        }
        catch (final SocketClosedException e) {
            return true;
        }
        catch (final IOException e2) {
            if (!this.isConnected()) {
                return true;
            }
            throw e2;
        }
    }
    
    public static boolean isRunningOnAndroid() {
        return NativeLibraryLoader.isAndroid();
    }
    
    @Override
    public void setShutdownOnClose(final boolean enabled) {
        this.getAFImpl().getCore().setShutdownOnClose(enabled);
    }
    
    static {
        ZERO_BYTES = new byte[0];
        AFSocket.capabilitiesValue = null;
    }
    
    @FunctionalInterface
    public interface Constructor<A extends AFSocketAddress>
    {
        AFSocket<A> newInstance(final FileDescriptor p0, final AFSocketFactory<A> p1) throws SocketException;
    }
}
