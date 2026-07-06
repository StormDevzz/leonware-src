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
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.newsclub.net.unix.AFInputStream;
import org.newsclub.net.unix.AFOutputStream;
import org.newsclub.net.unix.AFSocketAddress;
import org.newsclub.net.unix.AFSocketAddressFromHostname;
import org.newsclub.net.unix.AFSocketCapability;
import org.newsclub.net.unix.AFSocketChannel;
import org.newsclub.net.unix.AFSocketExtensions;
import org.newsclub.net.unix.AFSocketFactory;
import org.newsclub.net.unix.AFSocketImpl;
import org.newsclub.net.unix.AFSocketImplExtensions;
import org.newsclub.net.unix.AFSomeSocket;
import org.newsclub.net.unix.AFUNIXSocketCapability;
import org.newsclub.net.unix.BuildProperties;
import org.newsclub.net.unix.Closeables;
import org.newsclub.net.unix.NativeLibraryLoader;
import org.newsclub.net.unix.NativeUnixSocket;
import org.newsclub.net.unix.SentinelSocketAddress;
import org.newsclub.net.unix.SocketAddressFilter;
import org.newsclub.net.unix.SocketClosedException;

public abstract class AFSocket<A extends AFSocketAddress>
extends Socket
implements AFSomeSocket,
AFSocketExtensions {
    static final String PROP_LIBRARY_DISABLE_CAPABILITY_PREFIX = "org.newsclub.net.unix.library.disable.";
    private static final byte[] ZERO_BYTES = new byte[0];
    static String loadedLibrary;
    private static Integer capabilitiesValue;
    private final AFSocketImpl<A> impl;
    private final AFSocketAddressFromHostname<A> afh;
    private final Closeables closeables = new Closeables();
    private final AtomicBoolean created = new AtomicBoolean(false);
    private final AFSocketChannel<A> channel = this.newChannel();
    private @Nullable SocketAddressFilter connectFilter;

    @SuppressFBWarnings(value={"CT_CONSTRUCTOR_THROW"})
    protected AFSocket(AFSocketImpl<A> impl, AFSocketAddressFromHostname<A> afh) throws SocketException {
        super(impl);
        this.afh = afh;
        this.impl = impl;
    }

    protected final Class<? extends AFSocketAddress> socketAddressClass() {
        return this.getAFImpl(false).getAddressFamily().getSocketAddressClass();
    }

    protected abstract AFSocketChannel<A> newChannel();

    static <A extends AFSocketAddress> AFSocket<A> newInstance(Constructor<A> constr, AFSocketFactory<A> sf, FileDescriptor fdObj, int localPort, int remotePort) throws IOException {
        if (!fdObj.valid()) {
            throw new SocketException("Invalid file descriptor");
        }
        int status = NativeUnixSocket.socketStatus(fdObj);
        if (status == -1) {
            throw new SocketException("Not a valid socket");
        }
        AFSocket<A> socket = AFSocket.newInstance0(constr, fdObj, sf);
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
        socket.getAFImpl().setSocketAddress((AFSocketAddress)socket.getLocalSocketAddress());
        return socket;
    }

    protected static final <A extends AFSocketAddress> AFSocket<A> newInstance(Constructor<A> constr, AFSocketFactory<A> factory) throws SocketException {
        return AFSocket.newInstance0(constr, null, factory);
    }

    private static <A extends AFSocketAddress> @NonNull AFSocket<A> newInstance0(Constructor<A> constr, FileDescriptor fdObj, AFSocketFactory<A> factory) throws SocketException {
        return constr.newInstance(fdObj, factory);
    }

    protected static final <A extends AFSocketAddress> @NonNull AFSocket<A> connectTo(Constructor<A> constr, A addr) throws IOException {
        AFSocket<A> socket = constr.newInstance(null, null);
        socket.connect(addr);
        return socket;
    }

    public static final <A extends AFSocketAddress> AFSocket<?> connectTo(@NonNull A addr) throws IOException {
        AFSocket<?> socket = addr.getAddressFamily().getSocketConstructor().newInstance(null, null);
        socket.connect(addr);
        return socket;
    }

    @Override
    public final void bind(SocketAddress bindpoint) throws IOException {
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
    public final void connect(SocketAddress endpoint) throws IOException {
        this.connect(endpoint, 0);
    }

    @Override
    public final void connect(SocketAddress endpoint, int timeout) throws IOException {
        this.connect0(endpoint, timeout);
    }

    private AFSocketAddress preprocessSocketAddress(SocketAddress endpoint) throws SocketException {
        if (endpoint == null) {
            throw new IllegalArgumentException("endpoint is null");
        }
        if (endpoint instanceof SentinelSocketAddress) {
            return (AFSocketAddress)endpoint;
        }
        return AFSocketAddress.preprocessSocketAddress(this.socketAddressClass(), endpoint, this.afh);
    }

    final boolean connect0(SocketAddress endpoint, int timeout) throws IOException {
        int port;
        boolean success;
        if (timeout < 0) {
            throw new IllegalArgumentException("connect: timeout can't be negative");
        }
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (this.connectFilter != null) {
            endpoint = this.connectFilter.apply(endpoint);
        }
        AFSocketAddress address = this.preprocessSocketAddress(endpoint);
        if (!this.isBound()) {
            this.internalDummyBind();
        }
        if ((success = this.getAFImpl().connect0(address, timeout)) && (port = address.getPort()) > 0) {
            this.getAFImpl().updatePorts(this.getLocalPort(), port);
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
        String v = BuildProperties.getBuildProperties().get("git.build.version");
        if (v != null && !v.startsWith("$")) {
            return v;
        }
        try {
            return NativeLibraryLoader.getJunixsocketVersion();
        }
        catch (IOException e) {
            return null;
        }
    }

    public static final String getLoadedLibrary() {
        return loadedLibrary;
    }

    @Override
    public final boolean isClosed() {
        return super.isClosed() || this.isConnected() && !this.impl.getFD().valid() || this.impl.isClosed();
    }

    @Override
    public final int getAncillaryReceiveBufferSize() {
        return this.impl.getAncillaryReceiveBufferSize();
    }

    @Override
    public final void setAncillaryReceiveBufferSize(int size) {
        this.impl.setAncillaryReceiveBufferSize(size);
    }

    @Override
    public final void ensureAncillaryReceiveBufferSize(int minSize) {
        this.impl.ensureAncillaryReceiveBufferSize(minSize);
    }

    private static boolean isCapDisabled(AFSocketCapability cap) {
        return Boolean.parseBoolean(System.getProperty(PROP_LIBRARY_DISABLE_CAPABILITY_PREFIX + cap.name(), "false"));
    }

    private static int initCapabilities() {
        if (!AFSocket.isSupported()) {
            return 0;
        }
        int v = NativeUnixSocket.capabilities();
        if (System.getProperty("osv.version") != null) {
            v &= ~AFSocketCapability.CAPABILITY_FD_AS_REDIRECT.getBitmask();
        }
        for (AFSocketCapability cap : AFSocketCapability.values()) {
            if (!AFSocket.isCapDisabled(cap)) continue;
            v &= ~cap.getBitmask();
        }
        return v;
    }

    private static synchronized int capabilities() {
        if (capabilitiesValue == null) {
            capabilitiesValue = AFSocket.initCapabilities();
        }
        return capabilitiesValue;
    }

    @Deprecated
    public static final boolean supports(AFUNIXSocketCapability capability) {
        return (AFSocket.capabilities() & capability.getBitmask()) != 0;
    }

    public static final boolean supports(AFSocketCapability capability) {
        return (AFSocket.capabilities() & capability.getBitmask()) != 0;
    }

    public static final void ensureUnsafeSupported() throws IOException {
        if (!AFSocket.supports(AFSocketCapability.CAPABILITY_UNSAFE)) {
            throw new IOException("Unsafe operations are not supported in this environment");
        }
    }

    @Override
    public final synchronized void close() throws IOException {
        IOException superException = null;
        try {
            super.close();
        }
        catch (IOException e) {
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
        return this.getAFImpl(true);
    }

    final AFSocketImpl<A> getAFImpl(boolean createSocket) {
        if (createSocket && this.created.compareAndSet(false, true)) {
            try {
                this.getSoTimeout();
            }
            catch (SocketException socketException) {
                // empty catch block
            }
        }
        return this.impl;
    }

    @Override
    @SuppressFBWarnings(value={"EI_EXPOSE_REP"})
    public AFSocketChannel<A> getChannel() {
        return this.channel;
    }

    public final synchronized A getRemoteSocketAddress() {
        if (!this.isConnected()) {
            return null;
        }
        return this.impl.getRemoteSocketAddress();
    }

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

    public final AFSocket<A> forceConnectAddress(SocketAddress endpoint) {
        return this.connectHook(orig -> orig == null ? null : endpoint);
    }

    public final AFSocket<A> connectHook(SocketAddressFilter hook) {
        this.connectFilter = hook;
        return this;
    }

    public boolean checkConnectionClosed() throws IOException {
        if (!this.isConnected()) {
            return true;
        }
        try {
            if (!AFSocket.supports(AFSocketCapability.CAPABILITY_ZERO_LENGTH_SEND)) {
                return false;
            }
            this.getOutputStream().write(ZERO_BYTES);
            return false;
        }
        catch (SocketClosedException e) {
            return true;
        }
        catch (IOException e) {
            if (!this.isConnected()) {
                return true;
            }
            throw e;
        }
    }

    public static boolean isRunningOnAndroid() {
        return NativeLibraryLoader.isAndroid();
    }

    @Override
    public void setShutdownOnClose(boolean enabled) {
        this.getAFImpl().getCore().setShutdownOnClose(enabled);
    }

    static {
        capabilitiesValue = null;
    }

    @FunctionalInterface
    public static interface Constructor<A extends AFSocketAddress> {
        public @NonNull AFSocket<A> newInstance(FileDescriptor var1, AFSocketFactory<A> var2) throws SocketException;
    }
}

