package org.newsclub.net.unix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.newsclub.net.unix.AFSocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSYSTEMSocketAddress.class */
public final class AFSYSTEMSocketAddress extends AFSocketAddress {
    private static final long serialVersionUID = 1;
    private static AFAddressFamily<AFSYSTEMSocketAddress> afSystem;
    private static final String SELECTOR_PROVIDER_CLASS = "org.newsclub.net.unix.darwin.system.AFSYSTEMSelectorProvider";

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSYSTEMSocketAddress$SysAddr.class */
    @NonNullByDefault
    public static final class SysAddr extends NamedInteger {
        private static final long serialVersionUID = 1;
        public static final SysAddr AF_SYS_CONTROL;
        private static final SysAddr[] VALUES;

        static {
            SysAddr sysAddr = new SysAddr("AF_SYS_CONTROL", 2);
            AF_SYS_CONTROL = sysAddr;
            VALUES = (SysAddr[]) init(new SysAddr[]{sysAddr});
        }

        private SysAddr(int id) {
            super(id);
        }

        private SysAddr(String name, int id) {
            super(name, id);
        }

        public static SysAddr ofValue(int v) {
            return (SysAddr) ofValue(VALUES, SysAddr::new, v);
        }
    }

    private AFSYSTEMSocketAddress(int port, byte[] socketAddress, ByteBuffer nativeAddress) throws SocketException {
        super(port, socketAddress, nativeAddress, addressFamily());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static AFSYSTEMSocketAddress newAFSocketAddress(int port, byte[] socketAddress, ByteBuffer nativeAddress) throws SocketException {
        return (AFSYSTEMSocketAddress) newDeserializedAFSocketAddress(port, socketAddress, nativeAddress, addressFamily(), AFSYSTEMSocketAddress::new);
    }

    public static AFSYSTEMSocketAddress ofSysAddrIdUnit(int javaPort, SysAddr sysAddr, int id, int unit) throws SocketException {
        return (AFSYSTEMSocketAddress) resolveAddress(toBytes(sysAddr, id, unit), javaPort, addressFamily());
    }

    public static AFSYSTEMSocketAddress ofSysAddrIdUnit(SysAddr sysAddr, int id, int unit) throws SocketException {
        return ofSysAddrIdUnit(0, sysAddr, id, unit);
    }

    public static AFSYSTEMSocketAddress unwrap(InetAddress address, int port) throws SocketException {
        return (AFSYSTEMSocketAddress) AFSocketAddress.unwrap(address, port, addressFamily());
    }

    public static AFSYSTEMSocketAddress unwrap(String hostname, int port) throws SocketException {
        return (AFSYSTEMSocketAddress) AFSocketAddress.unwrap(hostname, port, addressFamily());
    }

    public static AFSYSTEMSocketAddress unwrap(SocketAddress address) throws SocketException {
        Objects.requireNonNull(address);
        if (!isSupportedAddress(address)) {
            throw new SocketException("Unsupported address");
        }
        return (AFSYSTEMSocketAddress) address;
    }

    @Override // java.net.InetSocketAddress
    public String toString() {
        int port = getPort();
        byte[] bytes = getBytes();
        if (bytes.length != 32) {
            return getClass().getName() + "[" + (port == 0 ? "" : "port=" + port) + ";UNKNOWN]";
        }
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        SysAddr sysAddr = SysAddr.ofValue(bb.getInt());
        int id = bb.getInt();
        int unit = bb.getInt();
        return getClass().getName() + "[" + (port == 0 ? "" : "port=" + port + ";") + sysAddr + ";id=" + id + ";unit=" + unit + "]";
    }

    public SysAddr getSysAddr() {
        ByteBuffer bb = ByteBuffer.wrap(getBytes());
        return SysAddr.ofValue(bb.getInt(0));
    }

    public int getId() {
        ByteBuffer bb = ByteBuffer.wrap(getBytes());
        return bb.getInt(4);
    }

    public int getUnit() {
        ByteBuffer bb = ByteBuffer.wrap(getBytes());
        return bb.getInt(8);
    }

    @Override // org.newsclub.net.unix.AFSocketAddress
    public boolean hasFilename() {
        return false;
    }

    @Override // org.newsclub.net.unix.AFSocketAddress
    public File getFile() throws FileNotFoundException {
        throw new FileNotFoundException("no file");
    }

    public static boolean isSupportedAddress(InetAddress addr) {
        return AFSocketAddress.isSupportedAddress(addr, addressFamily());
    }

    public static boolean isSupportedAddress(SocketAddress addr) {
        return addr instanceof AFSYSTEMSocketAddress;
    }

    private static byte[] toBytes(SysAddr sysAddr, int id, int unit) {
        ByteBuffer bb = ByteBuffer.allocate(32);
        bb.putInt(sysAddr.value());
        bb.putInt(id);
        bb.putInt(unit);
        bb.putInt(0);
        bb.putInt(0);
        bb.putInt(0);
        bb.putInt(0);
        bb.putInt(0);
        return (byte[]) bb.flip().array();
    }

    public static synchronized AFAddressFamily<AFSYSTEMSocketAddress> addressFamily() {
        if (afSystem == null) {
            afSystem = AFAddressFamily.registerAddressFamily("system", AFSYSTEMSocketAddress.class, new AFSocketAddressConfig<AFSYSTEMSocketAddress>() { // from class: org.newsclub.net.unix.AFSYSTEMSocketAddress.1
                private final AFSocketAddress.AFSocketAddressConstructor<AFSYSTEMSocketAddress> addrConstr;

                {
                    this.addrConstr = AFSocketAddress.isUseDeserializationForInit() ? (x$0, x$1, x$2) -> {
                        return AFSYSTEMSocketAddress.newAFSocketAddress(x$0, x$1, x$2);
                    } : (x$02, x$12, x$22) -> {
                        return new AFSYSTEMSocketAddress(x$02, x$12, x$22);
                    };
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.newsclub.net.unix.AFSocketAddressConfig
                public AFSYSTEMSocketAddress parseURI(URI u, int port) throws SocketException {
                    return AFSYSTEMSocketAddress.of(u, port);
                }

                @Override // org.newsclub.net.unix.AFSocketAddressConfig
                protected AFSocketAddress.AFSocketAddressConstructor<AFSYSTEMSocketAddress> addressConstructor() {
                    return this.addrConstr;
                }

                @Override // org.newsclub.net.unix.AFSocketAddressConfig
                protected String selectorProviderClassname() {
                    return AFSYSTEMSocketAddress.SELECTOR_PROVIDER_CLASS;
                }

                @Override // org.newsclub.net.unix.AFSocketAddressConfig
                protected Set<String> uriSchemes() {
                    return new HashSet(Arrays.asList("afsystem"));
                }
            });
            try {
                Class.forName(SELECTOR_PROVIDER_CLASS);
            } catch (ClassNotFoundException e) {
            }
        }
        return afSystem;
    }

    public static AFSYSTEMSocketAddress of(URI uri) throws SocketException {
        return of(uri, -1);
    }

    public static AFSYSTEMSocketAddress of(URI uri, int overridePort) throws SocketException {
        switch (uri.getScheme()) {
            case "afsystem":
                String host = uri.getHost();
                String host2 = host;
                if (host == null || host2.isEmpty()) {
                    String ssp = uri.getSchemeSpecificPart();
                    if (ssp == null || !ssp.startsWith("//")) {
                        throw new SocketException("Unsupported URI: " + uri);
                    }
                    String ssp2 = ssp.substring(2);
                    int i = ssp2.indexOf(47);
                    host2 = i == -1 ? ssp2 : ssp2.substring(0, i);
                    if (host2.isEmpty()) {
                        throw new SocketException("Unsupported URI: " + uri);
                    }
                }
                ByteBuffer bb = ByteBuffer.allocate(32);
                for (String p : host2.split("\\.")) {
                    try {
                        int v = parseUnsignedInt(p, 10);
                        bb.putInt(v);
                    } catch (NumberFormatException e) {
                        throw ((SocketException) new SocketException("Unsupported URI: " + uri).initCause(e));
                    }
                }
                bb.flip();
                if (bb.remaining() > 32) {
                    throw new SocketException("Unsupported URI: " + uri);
                }
                SysAddr.ofValue(bb.getInt());
                bb.getInt();
                bb.getInt();
                while (bb.remaining() > 0) {
                    if (bb.getInt() != 0) {
                        throw new SocketException("Unsupported URI: " + uri);
                    }
                }
                return (AFSYSTEMSocketAddress) resolveAddress(bb.array(), uri.getPort(), addressFamily());
            default:
                throw new SocketException("Unsupported URI scheme: " + uri.getScheme());
        }
    }

    @Override // org.newsclub.net.unix.AFSocketAddress
    public URI toURI(String scheme, URI template) throws IOException {
        switch (scheme) {
            case "afsystem":
                ByteBuffer bb = ByteBuffer.wrap(getBytes());
                StringBuilder sb = new StringBuilder();
                while (bb.remaining() > 0) {
                    sb.append(toUnsignedString(bb.getInt()));
                    if (bb.remaining() > 0) {
                        sb.append('.');
                    }
                }
                return new HostAndPort(sb.toString(), getPort()).toURI(scheme, template);
            default:
                return super.toURI(scheme, template);
        }
    }
}
