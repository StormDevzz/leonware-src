package org.newsclub.net.unix;

import com.google.errorprone.annotations.Immutable;
import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSocketAddress.class */
@Immutable
public abstract class AFSocketAddress extends InetSocketAddress {
    private static final long serialVersionUID = 1;
    static final AFSocketAddress INTERNAL_DUMMY_BIND = new SentinelSocketAddress(0);
    static final AFSocketAddress INTERNAL_DUMMY_CONNECT = new SentinelSocketAddress(1);
    static final AFSocketAddress INTERNAL_DUMMY_DONT_CONNECT = new SentinelSocketAddress(2);
    private static final int SOCKADDR_NATIVE_FAMILY_OFFSET;
    private static final int SOCKADDR_NATIVE_DATA_OFFSET;
    private static final int SOCKADDR_MAX_LEN;
    private static final Map<AFAddressFamily<?>, Map<Integer, Map<ByteBuffer, AFSocketAddress>>> ADDRESS_CACHE;
    static final ThreadLocal<ByteBuffer> SOCKETADDRESS_BUFFER_TL;
    private static final boolean USE_DESERIALIZATION_FOR_INIT;

    @SuppressFBWarnings({"JCIP_FIELD_ISNT_FINAL_IN_IMMUTABLE_CLASS"})
    private byte[] bytes;

    @SuppressFBWarnings({"JCIP_FIELD_ISNT_FINAL_IN_IMMUTABLE_CLASS"})
    private InetAddress inetAddress;
    private transient ByteBuffer nativeAddress;
    private transient AFAddressFamily<?> addressFamily;

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSocketAddress$AFSocketAddressConstructor.class */
    @FunctionalInterface
    protected interface AFSocketAddressConstructor<T extends AFSocketAddress> {
        T newAFSocketAddress(int i, byte[] bArr, ByteBuffer byteBuffer) throws SocketException;
    }

    public abstract boolean hasFilename();

    public abstract File getFile() throws FileNotFoundException;

    static {
        SOCKADDR_NATIVE_FAMILY_OFFSET = NativeUnixSocket.isLoaded() ? NativeUnixSocket.sockAddrNativeFamilyOffset() : -1;
        SOCKADDR_NATIVE_DATA_OFFSET = NativeUnixSocket.isLoaded() ? NativeUnixSocket.sockAddrNativeDataOffset() : -1;
        SOCKADDR_MAX_LEN = NativeUnixSocket.isLoaded() ? NativeUnixSocket.sockAddrLength(0) : 256;
        ADDRESS_CACHE = new HashMap();
        SOCKETADDRESS_BUFFER_TL = new ThreadLocal<ByteBuffer>() { // from class: org.newsclub.net.unix.AFSocketAddress.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public ByteBuffer initialValue() {
                return AFSocketAddress.newSockAddrDirectBuffer(AFSocketAddress.SOCKADDR_MAX_LEN);
            }
        };
        String v = System.getProperty("org.newsclub.net.unix.AFSocketAddress.deserialize", "");
        USE_DESERIALIZATION_FOR_INIT = v.isEmpty() ? NativeLibraryLoader.isAndroid() : Boolean.parseBoolean(v);
    }

    @SuppressFBWarnings({"CT_CONSTRUCTOR_THROW"})
    protected AFSocketAddress(int port, byte[] socketAddress, ByteBuffer nativeAddress, AFAddressFamily<?> af) throws SocketException {
        super(AFInetAddress.createUnresolvedHostname(socketAddress, af), (port < 0 || port > 65535) ? 0 : port);
        this.inetAddress = null;
        initAFSocketAddress(this, port, socketAddress, nativeAddress, af);
    }

    AFSocketAddress(Class<SentinelSocketAddress> clazz, int port) {
        super(InetAddress.getLoopbackAddress(), port);
        this.inetAddress = null;
        this.nativeAddress = null;
        this.bytes = new byte[0];
        this.addressFamily = null;
    }

    private static void initAFSocketAddress(AFSocketAddress addr, int port, byte[] socketAddress, ByteBuffer nativeAddress, AFAddressFamily<?> af) throws SocketException {
        if (socketAddress.length == 0) {
            throw new SocketException("Illegal address length: " + socketAddress.length);
        }
        addr.nativeAddress = nativeAddress == null ? null : (ByteBuffer) nativeAddress.duplicate().rewind();
        if (port < -1) {
            throw new IllegalArgumentException("port out of range");
        }
        if (port > 65535) {
            if (!NativeUnixSocket.isLoaded()) {
                throw ((SocketException) new SocketException("Cannot set SocketAddress port - junixsocket JNI library is not available").initCause(NativeUnixSocket.unsupportedException()));
            }
            NativeUnixSocket.setPort1(addr, port);
        }
        addr.bytes = (byte[]) socketAddress.clone();
        addr.addressFamily = af;
    }

    protected static <A extends AFSocketAddress> A newDeserializedAFSocketAddress(int i, byte[] bArr, ByteBuffer byteBuffer, AFAddressFamily<A> aFAddressFamily, AFSocketAddressConstructor<A> aFSocketAddressConstructor) throws SocketException {
        String strCreateUnresolvedHostname = AFInetAddress.createUnresolvedHostname(bArr, aFAddressFamily);
        if (strCreateUnresolvedHostname == null || strCreateUnresolvedHostname.isEmpty()) {
            return (A) aFSocketAddressConstructor.newAFSocketAddress(i, bArr, byteBuffer);
        }
        try {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(craftSerializedObject(aFAddressFamily.getSocketAddressClass(), strCreateUnresolvedHostname, (i < 0 || i > 65535) ? 0 : i)));
                try {
                    A a = (A) objectInputStream.readObject();
                    initAFSocketAddress(a, i, bArr, byteBuffer, aFAddressFamily);
                    objectInputStream.close();
                    return a;
                } catch (Throwable th) {
                    try {
                        objectInputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (IOException | ClassNotFoundException e) {
                throw ((SocketException) new SocketException("Unexpected deserialization problem").initCause(e));
            }
        } catch (SocketException e2) {
            throw e2;
        }
    }

    private static byte[] craftSerializedObject(Class<? extends AFSocketAddress> className, String hostname, int port) {
        ByteBuffer bb = ByteBuffer.allocate(768);
        bb.putShort((short) -21267);
        bb.putShort((short) 5);
        bb.put((byte) 115);
        bb.put((byte) 114);
        putShortLengthUtf8(bb, className.getName());
        bb.putLong(serialVersionUID);
        bb.putInt(33554552);
        bb.put((byte) 114);
        putShortLengthUtf8(bb, AFSocketAddress.class.getName());
        bb.putLong(serialVersionUID);
        bb.putInt(50332251);
        putShortLengthUtf8(bb, "bytes");
        bb.putInt(1946157659);
        bb.putShort((short) 16972);
        putShortLengthUtf8(bb, "inetAddress");
        bb.put((byte) 116);
        putShortLengthEncodedClassName(bb, InetAddress.class);
        bb.putShort((short) 30834);
        putShortLengthUtf8(bb, InetSocketAddress.class.getName());
        bb.putLong(5076001401234631237L);
        bb.putInt(50332489);
        putShortLengthUtf8(bb, "port");
        bb.put((byte) 76);
        putShortLengthUtf8(bb, "addr");
        bb.putInt(1895857664);
        bb.putShort((short) 844);
        putShortLengthUtf8(bb, "hostname");
        bb.put((byte) 116);
        putShortLengthEncodedClassName(bb, String.class);
        bb.putShort((short) 30834);
        putShortLengthUtf8(bb, SocketAddress.class.getName());
        bb.putLong(5215720748342549866L);
        bb.putInt(33554552);
        bb.put((byte) 112);
        bb.putInt(port);
        bb.putShort((short) 28788);
        putShortLengthUtf8(bb, hostname);
        bb.putInt(2020634743);
        bb.put((byte) 11);
        putShortLengthUtf8(bb, "undefined");
        bb.put((byte) 120);
        bb.flip();
        byte[] buf = new byte[bb.remaining()];
        bb.get(buf);
        return buf;
    }

    private static void putShortLengthEncodedClassName(ByteBuffer bb, Class<?> klazz) {
        putShortLengthUtf8(bb, "L" + klazz.getName().replace('.', '/') + ";");
    }

    private static void putShortLengthUtf8(ByteBuffer bb, String s) {
        byte[] utf8 = s.getBytes(StandardCharsets.UTF_8);
        bb.putShort((short) utf8.length);
        bb.put(utf8);
    }

    protected static boolean isUseDeserializationForInit() {
        return USE_DESERIALIZATION_FOR_INIT;
    }

    public final AFAddressFamily<?> getAddressFamily() {
        return this.addressFamily;
    }

    protected static final InetAddress getInetAddress(FileDescriptor fdesc, boolean peerName, AFAddressFamily<?> af) {
        byte[] addr;
        if (!fdesc.valid() || (addr = NativeUnixSocket.sockname(af.getDomain(), fdesc, peerName)) == null) {
            return null;
        }
        return AFInetAddress.wrapAddress(addr, af);
    }

    protected static final <A extends AFSocketAddress> A getSocketAddress(FileDescriptor fileDescriptor, boolean z, int i, AFAddressFamily<A> aFAddressFamily) {
        byte[] bArrSockname;
        if (!fileDescriptor.valid() || (bArrSockname = NativeUnixSocket.sockname(aFAddressFamily.getDomain(), fileDescriptor, z)) == null) {
            return null;
        }
        try {
            return (A) unwrap(AFInetAddress.wrapAddress(bArrSockname, aFAddressFamily), i, aFAddressFamily);
        } catch (SocketException e) {
            throw new IllegalStateException(e);
        }
    }

    static final AFSocketAddress preprocessSocketAddress(Class<? extends AFSocketAddress> supportedAddressClass, SocketAddress endpoint, AFSocketAddressFromHostname<?> afh) throws SocketException {
        Objects.requireNonNull(endpoint);
        if (endpoint instanceof SentinelSocketAddress) {
            return (SentinelSocketAddress) endpoint;
        }
        if (!(endpoint instanceof AFSocketAddress) && afh != null && (endpoint instanceof InetSocketAddress)) {
            InetSocketAddress isa = (InetSocketAddress) endpoint;
            String hostname = isa.getHostString();
            if (afh.isHostnameSupported(hostname)) {
                try {
                    endpoint = afh.addressFromHost(hostname, isa.getPort());
                } catch (SocketException e) {
                    throw e;
                }
            }
        }
        Objects.requireNonNull(endpoint);
        if (!supportedAddressClass.isAssignableFrom(endpoint.getClass())) {
            throw new IllegalArgumentException("Can only connect to endpoints of type " + supportedAddressClass.getName() + ", got: " + endpoint.getClass() + ": " + endpoint);
        }
        return (AFSocketAddress) endpoint;
    }

    protected final byte[] getBytes() {
        return this.bytes;
    }

    public final InetAddress wrapAddress() {
        return AFInetAddress.wrapAddress(this.bytes, getAddressFamily());
    }

    protected static final <A extends AFSocketAddress> A resolveAddress(byte[] bArr, int i, AFAddressFamily<A> aFAddressFamily) throws SocketException {
        Map<ByteBuffer, AFSocketAddress> map;
        AFSocketAddress aFSocketAddressNewAFSocketAddress;
        if (bArr.length == 0) {
            throw new SocketException("Address cannot be empty");
        }
        if (i == -1) {
            i = 0;
        }
        ByteBuffer byteBuffer = SOCKETADDRESS_BUFFER_TL.get();
        int iBytesToSockAddr = NativeUnixSocket.isLoaded() ? NativeUnixSocket.bytesToSockAddr(aFAddressFamily.getDomain(), byteBuffer, bArr) : -1;
        if (iBytesToSockAddr == -1) {
            return (A) aFAddressFamily.getAddressConstructor().newAFSocketAddress(i, bArr, null);
        }
        if (iBytesToSockAddr > SOCKADDR_MAX_LEN) {
            throw new IllegalStateException("Unexpected address length");
        }
        byteBuffer.rewind();
        byteBuffer.limit(iBytesToSockAddr);
        synchronized (AFSocketAddress.class) {
            Map<Integer, Map<ByteBuffer, AFSocketAddress>> map2 = ADDRESS_CACHE.get(aFAddressFamily);
            if (map2 == null) {
                aFSocketAddressNewAFSocketAddress = null;
                HashMap map3 = new HashMap();
                map = new HashMap();
                map3.put(Integer.valueOf(i), map);
                ADDRESS_CACHE.put(aFAddressFamily, map3);
            } else {
                map = map2.get(Integer.valueOf(i));
                if (map == null) {
                    aFSocketAddressNewAFSocketAddress = null;
                    map = new HashMap();
                    map2.put(Integer.valueOf(i), map);
                } else {
                    aFSocketAddressNewAFSocketAddress = map.get(byteBuffer);
                }
            }
            if (aFSocketAddressNewAFSocketAddress == null) {
                ByteBuffer byteBufferNewSockAddrKeyBuffer = newSockAddrKeyBuffer(iBytesToSockAddr);
                byteBufferNewSockAddrKeyBuffer.put(byteBuffer);
                ByteBuffer byteBufferAsReadOnlyBuffer = byteBufferNewSockAddrKeyBuffer.asReadOnlyBuffer();
                aFSocketAddressNewAFSocketAddress = aFAddressFamily.getAddressConstructor().newAFSocketAddress(i, bArr, byteBufferAsReadOnlyBuffer);
                map.put(byteBufferAsReadOnlyBuffer, aFSocketAddressNewAFSocketAddress);
            }
        }
        return (A) aFSocketAddressNewAFSocketAddress;
    }

    static final <A extends AFSocketAddress> A ofInternal(ByteBuffer byteBuffer, AFAddressFamily<A> aFAddressFamily) throws SocketException {
        Map<ByteBuffer, AFSocketAddress> map;
        A a;
        synchronized (AFSocketAddress.class) {
            byteBuffer.rewind();
            Map<Integer, Map<ByteBuffer, AFSocketAddress>> map2 = ADDRESS_CACHE.get(aFAddressFamily);
            if (map2 != null && (map = map2.get(0)) != null && (a = (A) map.get(byteBuffer)) != null) {
                return a;
            }
            if (!byteBuffer.isDirect()) {
                ByteBuffer nativeAddressDirectBuffer = getNativeAddressDirectBuffer(Math.min(byteBuffer.limit(), SOCKADDR_MAX_LEN));
                nativeAddressDirectBuffer.put(byteBuffer);
                byteBuffer = nativeAddressDirectBuffer;
            }
            byte[] bArrSockAddrToBytes = NativeUnixSocket.sockAddrToBytes(aFAddressFamily.getDomain(), byteBuffer);
            if (bArrSockAddrToBytes == null) {
                return null;
            }
            return (A) resolveAddress(bArrSockAddrToBytes, 0, aFAddressFamily);
        }
    }

    protected final synchronized InetAddress getInetAddress(AFAddressFamily<?> af) {
        if (this.inetAddress == null) {
            this.inetAddress = AFInetAddress.wrapAddress(this.bytes, af);
        }
        return this.inetAddress;
    }

    protected final InetAddress getInetAddress() {
        return getInetAddress(getAddressFamily());
    }

    static final ByteBuffer newSockAddrDirectBuffer(int length) {
        return ByteBuffer.allocateDirect(length);
    }

    static final ByteBuffer newSockAddrKeyBuffer(int length) {
        return ByteBuffer.allocate(length);
    }

    protected static final <A extends AFSocketAddress> A unwrap(InetAddress inetAddress, int i, AFAddressFamily<A> aFAddressFamily) throws SocketException {
        Objects.requireNonNull(inetAddress);
        return (A) resolveAddress(AFInetAddress.unwrapAddress(inetAddress, (AFAddressFamily<?>) aFAddressFamily), i, aFAddressFamily);
    }

    protected static final <A extends AFSocketAddress> A unwrap(String str, int i, AFAddressFamily<A> aFAddressFamily) throws SocketException {
        Objects.requireNonNull(str);
        return (A) resolveAddress(AFInetAddress.unwrapAddress(str, (AFAddressFamily<?>) aFAddressFamily), i, aFAddressFamily);
    }

    static final int unwrapAddressDirectBufferInternal(ByteBuffer socketAddressBuffer, SocketAddress address) throws SocketException {
        if (!NativeUnixSocket.isLoaded()) {
            throw new SocketException("Unsupported operation; junixsocket native library is not loaded");
        }
        Objects.requireNonNull(address);
        if (!(address instanceof AFSocketAddress)) {
            AFSupplier<? extends AFSocketAddress> supp = AFUNIXSocketAddress.supportedAddressSupplier(address);
            address = supp == null ? null : supp.get();
            if (address == null) {
                throw new SocketException("Unsupported address");
            }
        }
        AFSocketAddress socketAddress = (AFSocketAddress) address;
        byte[] addr = socketAddress.getBytes();
        int domain = socketAddress.getAddressFamily().getDomain();
        int len = NativeUnixSocket.bytesToSockAddr(domain, socketAddressBuffer, addr);
        if (len == -1) {
            throw new SocketException("Unsupported domain");
        }
        return len;
    }

    final ByteBuffer getNativeAddressDirectBuffer() throws SocketException {
        ByteBuffer address = this.nativeAddress;
        if (address == null) {
            throw ((SocketException) new SocketException("Cannot access native address").initCause(NativeUnixSocket.unsupportedException()));
        }
        ByteBuffer address2 = address.duplicate();
        ByteBuffer direct = getNativeAddressDirectBuffer(address2.limit());
        address2.position(0);
        direct.put(address2);
        return direct;
    }

    static final ByteBuffer getNativeAddressDirectBuffer(int limit) {
        ByteBuffer direct = SOCKETADDRESS_BUFFER_TL.get();
        direct.position(0);
        direct.limit(limit);
        return direct;
    }

    protected static final boolean isSupportedAddress(InetAddress addr, AFAddressFamily<?> af) {
        return AFInetAddress.isSupportedAddress(addr, af);
    }

    public final void writeNativeAddressTo(ByteBuffer buf) throws IOException {
        if (this.nativeAddress == null) {
            throw ((SocketException) new SocketException("Cannot access native address").initCause(NativeUnixSocket.unsupportedException()));
        }
        buf.put(this.nativeAddress);
    }

    public AFSocket<?> newConnectedSocket() throws IOException {
        AFSocket<?> socket = getAddressFamily().newSocket();
        socket.connect(this);
        return socket;
    }

    public AFServerSocket<?> newBoundServerSocket() throws IOException {
        AFServerSocket<?> serverSocket = getAddressFamily().newServerSocket();
        serverSocket.bind(this);
        return serverSocket;
    }

    public AFServerSocket<?> newForceBoundServerSocket() throws IOException {
        AFServerSocket<?> serverSocket = getAddressFamily().newServerSocket();
        serverSocket.forceBindAddress(this).bind(this);
        return serverSocket;
    }

    public static AFSocketAddress of(URI u) throws SocketException {
        return of(u, -1);
    }

    public static AFSocketAddress of(URI u, int overridePort) throws SocketException {
        AFAddressFamily<?> af = AFAddressFamily.getAddressFamily(u);
        if (af == null) {
            throw new SocketException("Cannot resolve AFSocketAddress from URI scheme: " + u.getScheme());
        }
        return af.parseURI(u, overridePort);
    }

    public URI toURI(String scheme, URI template) throws IOException {
        throw new IOException("Unsupported operation");
    }

    public String toSocatAddressString(AFSocketType socketType, AFSocketProtocol socketProtocol) throws IOException {
        if (SOCKADDR_NATIVE_FAMILY_OFFSET == -1 || SOCKADDR_NATIVE_DATA_OFFSET == -1) {
            return null;
        }
        if (this.nativeAddress == null) {
            throw ((SocketException) new SocketException("Cannot access native address").initCause(NativeUnixSocket.unsupportedException()));
        }
        if (socketProtocol != null && socketProtocol.getId() != 0) {
            throw new IOException("Protocol not (yet) supported");
        }
        int family = this.nativeAddress.get(SOCKADDR_NATIVE_FAMILY_OFFSET) & 255;
        int type = socketType == null ? -1 : NativeUnixSocket.sockTypeToNative(socketType.getId());
        StringBuilder sb = new StringBuilder();
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
        int n = this.nativeAddress.limit();
        while (n > 1 && this.nativeAddress.get(n - 1) == 0) {
            n--;
        }
        for (int pos = SOCKADDR_NATIVE_DATA_OFFSET; pos < n; pos++) {
            byte b = this.nativeAddress.get(pos);
            sb.append(String.format(Locale.ENGLISH, "%02x", Byte.valueOf(b)));
        }
        return sb.toString();
    }

    public boolean covers(AFSocketAddress other) {
        return equals(other);
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        String af = in.readUTF();
        if ("undefined".equals(af)) {
            this.addressFamily = null;
        } else {
            this.addressFamily = (AFAddressFamily) Objects.requireNonNull(AFAddressFamily.getAddressFamily(af), "address family");
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeUTF(this.addressFamily == null ? "undefined" : this.addressFamily.getJuxString());
    }

    static String toUnsignedString(int i) {
        return Long.toString(toUnsignedLong(i));
    }

    static String toUnsignedString(int i, int radix) {
        return Long.toUnsignedString(toUnsignedLong(i), radix);
    }

    private static long toUnsignedLong(long x) {
        return x & 4294967295L;
    }

    protected static int parseUnsignedInt(String s, int radix) throws NumberFormatException {
        if (s == null || s.isEmpty()) {
            throw new NumberFormatException("Cannot parse null or empty string");
        }
        int len = s.length();
        if (s.startsWith("-")) {
            throw new NumberFormatException("Illegal leading minus sign on unsigned string " + s);
        }
        if (len <= 5 || (radix == 10 && len <= 9)) {
            return Integer.parseInt(s, radix);
        }
        long ell = Long.parseLong(s, radix);
        if ((ell & (-4294967296L)) == 0) {
            return (int) ell;
        }
        throw new NumberFormatException("String value exceeds range of unsigned int: " + s);
    }
}
