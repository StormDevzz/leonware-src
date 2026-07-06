// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.ObjectOutputStream;
import java.util.Locale;
import java.net.URI;
import java.util.HashMap;
import java.util.Objects;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.InetAddress;
import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.nio.ByteBuffer;
import java.util.Map;
import com.google.errorprone.annotations.Immutable;
import java.net.InetSocketAddress;

@Immutable
public abstract class AFSocketAddress extends InetSocketAddress
{
    private static final long serialVersionUID = 1L;
    static final AFSocketAddress INTERNAL_DUMMY_BIND;
    static final AFSocketAddress INTERNAL_DUMMY_CONNECT;
    static final AFSocketAddress INTERNAL_DUMMY_DONT_CONNECT;
    private static final int SOCKADDR_NATIVE_FAMILY_OFFSET;
    private static final int SOCKADDR_NATIVE_DATA_OFFSET;
    private static final int SOCKADDR_MAX_LEN;
    private static final Map<AFAddressFamily<?>, Map<Integer, Map<ByteBuffer, AFSocketAddress>>> ADDRESS_CACHE;
    static final ThreadLocal<ByteBuffer> SOCKETADDRESS_BUFFER_TL;
    private static final boolean USE_DESERIALIZATION_FOR_INIT;
    @SuppressFBWarnings({ "JCIP_FIELD_ISNT_FINAL_IN_IMMUTABLE_CLASS" })
    private byte[] bytes;
    @SuppressFBWarnings({ "JCIP_FIELD_ISNT_FINAL_IN_IMMUTABLE_CLASS" })
    private InetAddress inetAddress;
    private transient ByteBuffer nativeAddress;
    private transient AFAddressFamily<?> addressFamily;
    
    @SuppressFBWarnings({ "CT_CONSTRUCTOR_THROW" })
    protected AFSocketAddress(final int port, final byte[] socketAddress, final ByteBuffer nativeAddress, final AFAddressFamily<?> af) throws SocketException {
        super(AFInetAddress.createUnresolvedHostname(socketAddress, af), (port >= 0 && port <= 65535) ? port : 0);
        this.inetAddress = null;
        initAFSocketAddress(this, port, socketAddress, nativeAddress, af);
    }
    
    AFSocketAddress(final Class<SentinelSocketAddress> clazz, final int port) {
        super(InetAddress.getLoopbackAddress(), port);
        this.inetAddress = null;
        this.nativeAddress = null;
        this.bytes = new byte[0];
        this.addressFamily = null;
    }
    
    private static void initAFSocketAddress(final AFSocketAddress addr, final int port, final byte[] socketAddress, final ByteBuffer nativeAddress, final AFAddressFamily<?> af) throws SocketException {
        if (socketAddress.length == 0) {
            throw new SocketException("Illegal address length: " + socketAddress.length);
        }
        addr.nativeAddress = ((nativeAddress == null) ? null : ((ByteBuffer)nativeAddress.duplicate().rewind()));
        if (port < -1) {
            throw new IllegalArgumentException("port out of range");
        }
        if (port > 65535) {
            if (!NativeUnixSocket.isLoaded()) {
                throw (SocketException)new SocketException("Cannot set SocketAddress port - junixsocket JNI library is not available").initCause(NativeUnixSocket.unsupportedException());
            }
            NativeUnixSocket.setPort1(addr, port);
        }
        addr.bytes = socketAddress.clone();
        addr.addressFamily = af;
    }
    
    protected static <A extends AFSocketAddress> A newDeserializedAFSocketAddress(final int port, final byte[] socketAddress, final ByteBuffer nativeAddress, final AFAddressFamily<A> af, final AFSocketAddressConstructor<A> constructor) throws SocketException {
        final String hostname = AFInetAddress.createUnresolvedHostname(socketAddress, af);
        if (hostname == null || hostname.isEmpty()) {
            return constructor.newAFSocketAddress(port, socketAddress, nativeAddress);
        }
        try (final ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(craftSerializedObject(af.getSocketAddressClass(), hostname, (port >= 0 && port <= 65535) ? port : 0)))) {
            final A addr = (A)oin.readObject();
            initAFSocketAddress(addr, port, socketAddress, nativeAddress, af);
            final AFSocketAddress afSocketAddress = addr;
            return (A)afSocketAddress;
        }
        catch (final SocketException e) {
            throw e;
        }
        catch (final ClassNotFoundException | IOException e2) {
            throw (SocketException)new SocketException("Unexpected deserialization problem").initCause(e2);
        }
    }
    
    private static byte[] craftSerializedObject(final Class<? extends AFSocketAddress> className, final String hostname, final int port) {
        final ByteBuffer bb = ByteBuffer.allocate(768);
        bb.putShort((short)(-21267));
        bb.putShort((short)5);
        bb.put((byte)115);
        bb.put((byte)114);
        putShortLengthUtf8(bb, className.getName());
        bb.putLong(1L);
        bb.putInt(33554552);
        bb.put((byte)114);
        putShortLengthUtf8(bb, AFSocketAddress.class.getName());
        bb.putLong(1L);
        bb.putInt(50332251);
        putShortLengthUtf8(bb, "bytes");
        bb.putInt(1946157659);
        bb.putShort((short)16972);
        putShortLengthUtf8(bb, "inetAddress");
        bb.put((byte)116);
        putShortLengthEncodedClassName(bb, InetAddress.class);
        bb.putShort((short)30834);
        putShortLengthUtf8(bb, InetSocketAddress.class.getName());
        bb.putLong(5076001401234631237L);
        bb.putInt(50332489);
        putShortLengthUtf8(bb, "port");
        bb.put((byte)76);
        putShortLengthUtf8(bb, "addr");
        bb.putInt(1895857664);
        bb.putShort((short)844);
        putShortLengthUtf8(bb, "hostname");
        bb.put((byte)116);
        putShortLengthEncodedClassName(bb, String.class);
        bb.putShort((short)30834);
        putShortLengthUtf8(bb, SocketAddress.class.getName());
        bb.putLong(5215720748342549866L);
        bb.putInt(33554552);
        bb.put((byte)112);
        bb.putInt(port);
        bb.putShort((short)28788);
        putShortLengthUtf8(bb, hostname);
        bb.putInt(2020634743);
        bb.put((byte)11);
        putShortLengthUtf8(bb, "undefined");
        bb.put((byte)120);
        bb.flip();
        final byte[] buf = new byte[bb.remaining()];
        bb.get(buf);
        return buf;
    }
    
    private static void putShortLengthEncodedClassName(final ByteBuffer bb, final Class<?> klazz) {
        putShortLengthUtf8(bb, "L" + klazz.getName().replace('.', '/') + ";");
    }
    
    private static void putShortLengthUtf8(final ByteBuffer bb, final String s) {
        final byte[] utf8 = s.getBytes(StandardCharsets.UTF_8);
        bb.putShort((short)utf8.length);
        bb.put(utf8);
    }
    
    protected static boolean isUseDeserializationForInit() {
        return AFSocketAddress.USE_DESERIALIZATION_FOR_INIT;
    }
    
    public abstract boolean hasFilename();
    
    public abstract File getFile() throws FileNotFoundException;
    
    public final AFAddressFamily<?> getAddressFamily() {
        return this.addressFamily;
    }
    
    protected static final InetAddress getInetAddress(final FileDescriptor fdesc, final boolean peerName, final AFAddressFamily<?> af) {
        if (!fdesc.valid()) {
            return null;
        }
        final byte[] addr = NativeUnixSocket.sockname(af.getDomain(), fdesc, peerName);
        if (addr == null) {
            return null;
        }
        return AFInetAddress.wrapAddress(addr, af);
    }
    
    protected static final <A extends AFSocketAddress> A getSocketAddress(final FileDescriptor fdesc, final boolean requestPeerName, final int port, final AFAddressFamily<A> af) {
        if (!fdesc.valid()) {
            return null;
        }
        final byte[] addr = NativeUnixSocket.sockname(af.getDomain(), fdesc, requestPeerName);
        if (addr == null) {
            return null;
        }
        try {
            return unwrap(AFInetAddress.wrapAddress(addr, af), port, af);
        }
        catch (final SocketException e) {
            throw new IllegalStateException(e);
        }
    }
    
    static final AFSocketAddress preprocessSocketAddress(final Class<? extends AFSocketAddress> supportedAddressClass, SocketAddress endpoint, final AFSocketAddressFromHostname<?> afh) throws SocketException {
        Objects.requireNonNull(endpoint);
        if (endpoint instanceof SentinelSocketAddress) {
            return (SentinelSocketAddress)endpoint;
        }
        if (!(endpoint instanceof AFSocketAddress) && afh != null && endpoint instanceof InetSocketAddress) {
            final InetSocketAddress isa = (InetSocketAddress)endpoint;
            final String hostname = isa.getHostString();
            if (afh.isHostnameSupported(hostname)) {
                try {
                    endpoint = afh.addressFromHost(hostname, isa.getPort());
                }
                catch (final SocketException e) {
                    throw e;
                }
            }
        }
        Objects.requireNonNull(endpoint);
        if (!supportedAddressClass.isAssignableFrom(endpoint.getClass())) {
            throw new IllegalArgumentException("Can only connect to endpoints of type " + supportedAddressClass.getName() + ", got: " + endpoint.getClass() + ": " + endpoint);
        }
        return (AFSocketAddress)endpoint;
    }
    
    protected final byte[] getBytes() {
        return this.bytes;
    }
    
    public final InetAddress wrapAddress() {
        return AFInetAddress.wrapAddress(this.bytes, this.getAddressFamily());
    }
    
    protected static final <A extends AFSocketAddress> A resolveAddress(final byte[] socketAddress, int port, final AFAddressFamily<A> af) throws SocketException {
        if (socketAddress.length == 0) {
            throw new SocketException("Address cannot be empty");
        }
        if (port == -1) {
            port = 0;
        }
        final ByteBuffer direct = AFSocketAddress.SOCKETADDRESS_BUFFER_TL.get();
        final int limit = NativeUnixSocket.isLoaded() ? NativeUnixSocket.bytesToSockAddr(af.getDomain(), direct, socketAddress) : -1;
        if (limit == -1) {
            return af.getAddressConstructor().newAFSocketAddress(port, socketAddress, null);
        }
        if (limit > AFSocketAddress.SOCKADDR_MAX_LEN) {
            throw new IllegalStateException("Unexpected address length");
        }
        direct.rewind();
        direct.limit();
        A instance;
        synchronized (AFSocketAddress.class) {
            Map<Integer, Map<ByteBuffer, AFSocketAddress>> mapPorts = AFSocketAddress.ADDRESS_CACHE.get(af);
            Map<ByteBuffer, AFSocketAddress> map;
            if (mapPorts == null) {
                instance = null;
                mapPorts = new HashMap<Integer, Map<ByteBuffer, AFSocketAddress>>();
                map = new HashMap<ByteBuffer, AFSocketAddress>();
                mapPorts.put(port, map);
                AFSocketAddress.ADDRESS_CACHE.put(af, mapPorts);
            }
            else {
                map = mapPorts.get(port);
                if (map == null) {
                    instance = null;
                    map = new HashMap<ByteBuffer, AFSocketAddress>();
                    mapPorts.put(port, map);
                }
                else {
                    instance = (A)map.get(direct);
                }
            }
            if (instance == null) {
                ByteBuffer key = newSockAddrKeyBuffer(limit);
                key.put(direct);
                key = key.asReadOnlyBuffer();
                instance = af.getAddressConstructor().newAFSocketAddress(port, socketAddress, key);
                map.put(key, instance);
            }
        }
        return instance;
    }
    
    static final <A extends AFSocketAddress> A ofInternal(ByteBuffer socketAddressBuffer, final AFAddressFamily<A> af) throws SocketException {
        synchronized (AFSocketAddress.class) {
            socketAddressBuffer.rewind();
            final Map<Integer, Map<ByteBuffer, AFSocketAddress>> mapPorts = AFSocketAddress.ADDRESS_CACHE.get(af);
            if (mapPorts != null) {
                final Map<ByteBuffer, AFSocketAddress> map = mapPorts.get(0);
                if (map != null) {
                    final A address = (A)map.get(socketAddressBuffer);
                    if (address != null) {
                        return address;
                    }
                }
            }
            if (!socketAddressBuffer.isDirect()) {
                final ByteBuffer buf = getNativeAddressDirectBuffer(Math.min(socketAddressBuffer.limit(), AFSocketAddress.SOCKADDR_MAX_LEN));
                buf.put(socketAddressBuffer);
                socketAddressBuffer = buf;
            }
            final byte[] sockAddrToBytes = NativeUnixSocket.sockAddrToBytes(af.getDomain(), socketAddressBuffer);
            if (sockAddrToBytes == null) {
                return null;
            }
            return resolveAddress(sockAddrToBytes, 0, af);
        }
    }
    
    protected final synchronized InetAddress getInetAddress(final AFAddressFamily<?> af) {
        if (this.inetAddress == null) {
            this.inetAddress = AFInetAddress.wrapAddress(this.bytes, af);
        }
        return this.inetAddress;
    }
    
    protected final InetAddress getInetAddress() {
        return this.getInetAddress(this.getAddressFamily());
    }
    
    static final ByteBuffer newSockAddrDirectBuffer(final int length) {
        return ByteBuffer.allocateDirect(length);
    }
    
    static final ByteBuffer newSockAddrKeyBuffer(final int length) {
        return ByteBuffer.allocate(length);
    }
    
    protected static final <A extends AFSocketAddress> A unwrap(final InetAddress address, final int port, final AFAddressFamily<A> af) throws SocketException {
        Objects.requireNonNull(address);
        return resolveAddress(AFInetAddress.unwrapAddress(address, af), port, af);
    }
    
    protected static final <A extends AFSocketAddress> A unwrap(final String hostname, final int port, final AFAddressFamily<A> af) throws SocketException {
        Objects.requireNonNull(hostname);
        return resolveAddress(AFInetAddress.unwrapAddress(hostname, af), port, af);
    }
    
    static final int unwrapAddressDirectBufferInternal(final ByteBuffer socketAddressBuffer, SocketAddress address) throws SocketException {
        if (!NativeUnixSocket.isLoaded()) {
            throw new SocketException("Unsupported operation; junixsocket native library is not loaded");
        }
        Objects.requireNonNull(address);
        if (!(address instanceof AFSocketAddress)) {
            final AFSupplier<? extends AFSocketAddress> supp = AFUNIXSocketAddress.supportedAddressSupplier(address);
            address = ((supp == null) ? null : ((SocketAddress)supp.get()));
            if (address == null) {
                throw new SocketException("Unsupported address");
            }
        }
        final AFSocketAddress socketAddress = (AFSocketAddress)address;
        final byte[] addr = socketAddress.getBytes();
        final int domain = socketAddress.getAddressFamily().getDomain();
        final int len = NativeUnixSocket.bytesToSockAddr(domain, socketAddressBuffer, addr);
        if (len == -1) {
            throw new SocketException("Unsupported domain");
        }
        return len;
    }
    
    final ByteBuffer getNativeAddressDirectBuffer() throws SocketException {
        ByteBuffer address = this.nativeAddress;
        if (address == null) {
            throw (SocketException)new SocketException("Cannot access native address").initCause(NativeUnixSocket.unsupportedException());
        }
        address = address.duplicate();
        final ByteBuffer direct = getNativeAddressDirectBuffer(address.limit());
        address.position();
        direct.put(address);
        return direct;
    }
    
    static final ByteBuffer getNativeAddressDirectBuffer(final int limit) {
        final ByteBuffer direct = AFSocketAddress.SOCKETADDRESS_BUFFER_TL.get();
        direct.position();
        direct.limit();
        return direct;
    }
    
    protected static final boolean isSupportedAddress(final InetAddress addr, final AFAddressFamily<?> af) {
        return AFInetAddress.isSupportedAddress(addr, af);
    }
    
    public final void writeNativeAddressTo(final ByteBuffer buf) throws IOException {
        if (this.nativeAddress == null) {
            throw (SocketException)new SocketException("Cannot access native address").initCause(NativeUnixSocket.unsupportedException());
        }
        buf.put(this.nativeAddress);
    }
    
    public AFSocket<?> newConnectedSocket() throws IOException {
        final AFSocket<?> socket = this.getAddressFamily().newSocket();
        socket.connect(this);
        return socket;
    }
    
    public AFServerSocket<?> newBoundServerSocket() throws IOException {
        final AFServerSocket<?> serverSocket = this.getAddressFamily().newServerSocket();
        serverSocket.bind(this);
        return serverSocket;
    }
    
    public AFServerSocket<?> newForceBoundServerSocket() throws IOException {
        final AFServerSocket<?> serverSocket = this.getAddressFamily().newServerSocket();
        serverSocket.forceBindAddress(this).bind(this);
        return serverSocket;
    }
    
    public static AFSocketAddress of(final URI u) throws SocketException {
        return of(u, -1);
    }
    
    public static AFSocketAddress of(final URI u, final int overridePort) throws SocketException {
        final AFAddressFamily<?> af = AFAddressFamily.getAddressFamily(u);
        if (af == null) {
            throw new SocketException("Cannot resolve AFSocketAddress from URI scheme: " + u.getScheme());
        }
        return af.parseURI(u, overridePort);
    }
    
    public URI toURI(final String scheme, final URI template) throws IOException {
        throw new IOException("Unsupported operation");
    }
    
    public String toSocatAddressString(final AFSocketType socketType, final AFSocketProtocol socketProtocol) throws IOException {
        if (AFSocketAddress.SOCKADDR_NATIVE_FAMILY_OFFSET == -1 || AFSocketAddress.SOCKADDR_NATIVE_DATA_OFFSET == -1) {
            return null;
        }
        if (this.nativeAddress == null) {
            throw (SocketException)new SocketException("Cannot access native address").initCause(NativeUnixSocket.unsupportedException());
        }
        if (socketProtocol != null && socketProtocol.getId() != 0) {
            throw new IOException("Protocol not (yet) supported");
        }
        final int family = this.nativeAddress.get(AFSocketAddress.SOCKADDR_NATIVE_FAMILY_OFFSET) & 0xFF;
        final int type = (socketType == null) ? -1 : NativeUnixSocket.sockTypeToNative(socketType.getId());
        final StringBuilder sb = new StringBuilder();
        sb.append(family);
        if (type != -1) {
            sb.append(':');
            sb.append(type);
        }
        if (socketProtocol != null) {
            sb.append(':');
            sb.append(socketProtocol.getId());
        }
        sb.append(":x");
        int n;
        for (n = this.nativeAddress.limit(); n > 1 && this.nativeAddress.get(n - 1) == 0; --n) {}
        for (int pos = AFSocketAddress.SOCKADDR_NATIVE_DATA_OFFSET; pos < n; ++pos) {
            final byte b = this.nativeAddress.get(pos);
            sb.append(String.format(Locale.ENGLISH, "%02x", b));
        }
        return sb.toString();
    }
    
    public boolean covers(final AFSocketAddress other) {
        return this.equals(other);
    }
    
    private void readObject(final ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        final String af = in.readUTF();
        if ("undefined".equals(af)) {
            this.addressFamily = null;
        }
        else {
            this.addressFamily = Objects.requireNonNull(AFAddressFamily.getAddressFamily(af), "address family");
        }
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeUTF((this.addressFamily == null) ? "undefined" : this.addressFamily.getJuxString());
    }
    
    static String toUnsignedString(final int i) {
        return Long.toString(toUnsignedLong(i));
    }
    
    static String toUnsignedString(final int i, final int radix) {
        return Long.toUnsignedString(toUnsignedLong(i), radix);
    }
    
    private static long toUnsignedLong(final long x) {
        return x & 0xFFFFFFFFL;
    }
    
    protected static int parseUnsignedInt(final String s, final int radix) throws NumberFormatException {
        if (s == null || s.isEmpty()) {
            throw new NumberFormatException("Cannot parse null or empty string");
        }
        final int len = s.length();
        if (s.startsWith("-")) {
            throw new NumberFormatException("Illegal leading minus sign on unsigned string " + s);
        }
        if (len <= 5 || (radix == 10 && len <= 9)) {
            return Integer.parseInt(s, radix);
        }
        final long ell = Long.parseLong(s, radix);
        if ((ell & 0xFFFFFFFF00000000L) == 0x0L) {
            return (int)ell;
        }
        throw new NumberFormatException("String value exceeds range of unsigned int: " + s);
    }
    
    static {
        INTERNAL_DUMMY_BIND = new SentinelSocketAddress(0);
        INTERNAL_DUMMY_CONNECT = new SentinelSocketAddress(1);
        INTERNAL_DUMMY_DONT_CONNECT = new SentinelSocketAddress(2);
        SOCKADDR_NATIVE_FAMILY_OFFSET = (NativeUnixSocket.isLoaded() ? NativeUnixSocket.sockAddrNativeFamilyOffset() : -1);
        SOCKADDR_NATIVE_DATA_OFFSET = (NativeUnixSocket.isLoaded() ? NativeUnixSocket.sockAddrNativeDataOffset() : -1);
        SOCKADDR_MAX_LEN = (NativeUnixSocket.isLoaded() ? NativeUnixSocket.sockAddrLength(0) : 256);
        ADDRESS_CACHE = new HashMap<AFAddressFamily<?>, Map<Integer, Map<ByteBuffer, AFSocketAddress>>>();
        SOCKETADDRESS_BUFFER_TL = new ThreadLocal<ByteBuffer>() {
            @Override
            protected ByteBuffer initialValue() {
                return AFSocketAddress.newSockAddrDirectBuffer(AFSocketAddress.SOCKADDR_MAX_LEN);
            }
        };
        final String v = System.getProperty("org.newsclub.net.unix.AFSocketAddress.deserialize", "");
        USE_DESERIALIZATION_FOR_INIT = (v.isEmpty() ? NativeLibraryLoader.isAndroid() : Boolean.parseBoolean(v));
    }
    
    @FunctionalInterface
    protected interface AFSocketAddressConstructor<T extends AFSocketAddress>
    {
        T newAFSocketAddress(final int p0, final byte[] p1, final ByteBuffer p2) throws SocketException;
    }
}
