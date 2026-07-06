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
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.newsclub.net.unix.AFSocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFVSOCKSocketAddress.class */
public final class AFVSOCKSocketAddress extends AFSocketAddress {
    private static final long serialVersionUID = 1;
    private static final Pattern PAT_VSOCK_URI_HOST_AND_PORT = Pattern.compile("^(?<port>any|[0-9a-fx\\-]+)(\\.(?<cid>any|hypervisor|local|host|[0-9a-fx\\-]+))?(?:\\:(?<javaPort>[0-9]+))?$");
    private static AFAddressFamily<AFVSOCKSocketAddress> afVsock;
    public static final int VMADDR_CID_ANY = -1;
    public static final int VMADDR_CID_HYPERVISOR = 0;
    public static final int VMADDR_CID_LOCAL = 1;
    public static final int VMADDR_CID_HOST = 2;
    public static final int VMADDR_PORT_ANY = -1;

    private AFVSOCKSocketAddress(int port, byte[] socketAddress, ByteBuffer nativeAddress) throws SocketException {
        super(port, socketAddress, nativeAddress, addressFamily());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static AFVSOCKSocketAddress newAFSocketAddress(int port, byte[] socketAddress, ByteBuffer nativeAddress) throws SocketException {
        return (AFVSOCKSocketAddress) newDeserializedAFSocketAddress(port, socketAddress, nativeAddress, addressFamily(), AFVSOCKSocketAddress::new);
    }

    public static AFVSOCKSocketAddress ofPortAndCID(int port, int cid) throws SocketException {
        return ofPortAndCID(-1, port, cid);
    }

    public static AFVSOCKSocketAddress ofHypervisorPort(int port) throws SocketException {
        return ofPortAndCID(port, 0);
    }

    public static AFVSOCKSocketAddress ofAnyHypervisorPort() throws SocketException {
        return ofPortAndCID(-1, 0);
    }

    public static AFVSOCKSocketAddress ofLocalPort(int port) throws SocketException {
        return ofPortAndCID(port, 1);
    }

    public static AFVSOCKSocketAddress ofAnyLocalPort() throws SocketException {
        return ofPortAndCID(-1, 1);
    }

    public static AFVSOCKSocketAddress ofHostPort(int port) throws SocketException {
        return ofPortAndCID(port, 2);
    }

    public static AFVSOCKSocketAddress ofAnyHostPort() throws SocketException {
        return ofPortAndCID(-1, 2);
    }

    public static AFVSOCKSocketAddress ofAnyPort() throws SocketException {
        return ofPortAndCID(-1, -1);
    }

    public static AFVSOCKSocketAddress ofPortWithAnyCID(int port) throws SocketException {
        return ofPortAndCID(port, -1);
    }

    public static AFVSOCKSocketAddress ofPortAndCID(int javaPort, int vsockPort, int cid) throws SocketException {
        return (AFVSOCKSocketAddress) resolveAddress(toBytes(vsockPort, cid), javaPort, addressFamily());
    }

    public static AFVSOCKSocketAddress unwrap(InetAddress address, int port) throws SocketException {
        return (AFVSOCKSocketAddress) AFSocketAddress.unwrap(address, port, addressFamily());
    }

    public static AFVSOCKSocketAddress unwrap(String hostname, int port) throws SocketException {
        return (AFVSOCKSocketAddress) AFSocketAddress.unwrap(hostname, port, addressFamily());
    }

    public static AFVSOCKSocketAddress unwrap(SocketAddress address) throws SocketException {
        Objects.requireNonNull(address);
        if (!isSupportedAddress(address)) {
            throw new SocketException("Unsupported address");
        }
        return (AFVSOCKSocketAddress) address;
    }

    public int getVSOCKPort() {
        ByteBuffer bb = ByteBuffer.wrap(getBytes());
        int a = bb.getInt(4);
        return a;
    }

    public int getVSOCKCID() {
        ByteBuffer bb = ByteBuffer.wrap(getBytes());
        int a = bb.getInt(8);
        return a;
    }

    public int getVSOCKReserved1() {
        ByteBuffer bb = ByteBuffer.wrap(getBytes());
        int a = bb.getInt(0);
        return a;
    }

    @Override // java.net.InetSocketAddress
    public String toString() {
        String vsockPortString;
        int port = getPort();
        byte[] bytes = getBytes();
        if (bytes.length != 12) {
            return getClass().getName() + "[" + (port == 0 ? "" : "port=" + port) + ";UNKNOWN]";
        }
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        int reserved1 = bb.getInt();
        int vsockPort = bb.getInt();
        int cid = bb.getInt();
        if (vsockPort >= -1) {
            vsockPortString = Integer.toString(vsockPort);
        } else {
            vsockPortString = String.format(Locale.ENGLISH, "0x%08x", Integer.valueOf(vsockPort));
        }
        String typeString = (reserved1 == 0 ? "" : "reserved1=" + reserved1 + ";") + "vsockPort=" + vsockPortString + ";cid=" + cid;
        return getClass().getName() + "[" + (port == 0 ? "" : "port=" + port + ";") + typeString + "]";
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
        return addr instanceof AFVSOCKSocketAddress;
    }

    private static byte[] toBytes(int port, int cid) {
        ByteBuffer bb = ByteBuffer.allocate(12);
        bb.putInt(0);
        bb.putInt(port);
        bb.putInt(cid);
        return (byte[]) bb.flip().array();
    }

    public static synchronized AFAddressFamily<AFVSOCKSocketAddress> addressFamily() {
        if (afVsock == null) {
            afVsock = AFAddressFamily.registerAddressFamily("vsock", AFVSOCKSocketAddress.class, new AFSocketAddressConfig<AFVSOCKSocketAddress>() { // from class: org.newsclub.net.unix.AFVSOCKSocketAddress.1
                private final AFSocketAddress.AFSocketAddressConstructor<AFVSOCKSocketAddress> addrConstr;

                {
                    this.addrConstr = AFSocketAddress.isUseDeserializationForInit() ? (x$0, x$1, x$2) -> {
                        return AFVSOCKSocketAddress.newAFSocketAddress(x$0, x$1, x$2);
                    } : (x$02, x$12, x$22) -> {
                        return new AFVSOCKSocketAddress(x$02, x$12, x$22);
                    };
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.newsclub.net.unix.AFSocketAddressConfig
                public AFVSOCKSocketAddress parseURI(URI u, int port) throws SocketException {
                    return AFVSOCKSocketAddress.of(u, port);
                }

                @Override // org.newsclub.net.unix.AFSocketAddressConfig
                protected AFSocketAddress.AFSocketAddressConstructor<AFVSOCKSocketAddress> addressConstructor() {
                    return this.addrConstr;
                }

                @Override // org.newsclub.net.unix.AFSocketAddressConfig
                protected String selectorProviderClassname() {
                    return "org.newsclub.net.unix.vsock.AFVSOCKSelectorProvider";
                }

                @Override // org.newsclub.net.unix.AFSocketAddressConfig
                protected Set<String> uriSchemes() {
                    return new HashSet(Arrays.asList("vsock", "http+vsock", "https+vsock"));
                }
            });
            try {
                Class.forName("org.newsclub.net.unix.vsock.AFVSOCKSelectorProvider");
            } catch (ClassNotFoundException e) {
            }
        }
        return afVsock;
    }

    public static AFVSOCKSocketAddress of(URI uri) throws SocketException {
        return of(uri, -1);
    }

    public static AFVSOCKSocketAddress of(URI uri, int overridePort) throws SocketException {
        String cidStr;
        String portStr;
        int cid;
        int port;
        int at;
        switch (uri.getScheme()) {
            case "vsock":
            case "http+vsock":
            case "https+vsock":
                String host = uri.getHost();
                if (host == null) {
                    host = uri.getAuthority();
                    if (host != null && (at = host.indexOf(64)) >= 0) {
                        host = host.substring(at + 1);
                    }
                }
                if (host == null) {
                    throw new SocketException("Cannot get hostname from URI: " + uri);
                }
                try {
                    Matcher m = PAT_VSOCK_URI_HOST_AND_PORT.matcher(host);
                    if (!m.matches()) {
                        throw new SocketException("Invalid VSOCK URI: " + uri);
                    }
                    cidStr = m.group("cid");
                    portStr = m.group("port");
                    String javaPortStr = m.group("javaPort");
                    switch (cidStr == null ? "" : cidStr) {
                        case "":
                        case "any":
                            cid = -1;
                            break;
                        case "hypervisor":
                            cid = 0;
                            break;
                        case "local":
                            cid = 1;
                            break;
                        case "host":
                            cid = 2;
                            break;
                        default:
                            cid = parseInt(cidStr);
                            break;
                    }
                    switch (portStr == null ? "" : portStr) {
                        case "any":
                        case "":
                            port = -1;
                            break;
                        default:
                            port = parseInt(portStr);
                            break;
                    }
                    int javaPort = overridePort != -1 ? overridePort : uri.getPort();
                    if (javaPortStr != null && !javaPortStr.isEmpty()) {
                        javaPort = parseInt(javaPortStr);
                    }
                    return ofPortAndCID(javaPort, port, cid);
                } catch (IllegalArgumentException e) {
                    throw ((SocketException) new SocketException("Invalid VSOCK URI: " + uri).initCause(e));
                }
            default:
                throw new SocketException("Unsupported URI scheme: " + uri.getScheme());
        }
    }

    @Override // org.newsclub.net.unix.AFSocketAddress
    public URI toURI(String scheme, URI template) throws IOException {
        String portStr;
        String cidStr;
        switch (scheme) {
            case "vsock":
            case "http+vsock":
            case "https+vsock":
                byte[] bytes = getBytes();
                if (bytes.length == 12) {
                    StringBuilder sb = new StringBuilder();
                    int port = getVSOCKPort();
                    switch (port) {
                        case -1:
                            portStr = "any";
                            break;
                        default:
                            portStr = toUnsignedString(port);
                            break;
                    }
                    sb.append(portStr);
                    sb.append('.');
                    int cid = getVSOCKCID();
                    switch (cid) {
                        case -1:
                            cidStr = "any";
                            break;
                        case 0:
                            cidStr = "hypervisor";
                            break;
                        case 1:
                            cidStr = "local";
                            break;
                        case VMADDR_CID_HOST /* 2 */:
                            cidStr = "host";
                            break;
                        default:
                            cidStr = toUnsignedString(cid);
                            break;
                    }
                    sb.append(cidStr);
                    break;
                } else {
                    break;
                }
                break;
        }
        return super.toURI(scheme, template);
    }

    private static int parseInt(String v) {
        if (v.startsWith("0x")) {
            return parseUnsignedInt(v.substring(2), 16);
        }
        if (v.startsWith("-")) {
            return Integer.parseInt(v);
        }
        return parseUnsignedInt(v, 10);
    }

    @Override // org.newsclub.net.unix.AFSocketAddress
    public boolean covers(AFSocketAddress covered) {
        if (super.covers(covered)) {
            return true;
        }
        if (covered instanceof AFVSOCKSocketAddress) {
            AFVSOCKSocketAddress other = (AFVSOCKSocketAddress) covered;
            if (getVSOCKCID() == -1) {
                return getVSOCKPort() == -1 || getVSOCKPort() == other.getVSOCKPort();
            }
            if (getVSOCKPort() == -1) {
                return getVSOCKCID() == other.getVSOCKCID();
            }
        }
        return equals(covered);
    }
}
