package org.newsclub.net.unix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
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
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.newsclub.net.unix.AFSocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFTIPCSocketAddress.class */
public final class AFTIPCSocketAddress extends AFSocketAddress {
    private static final long serialVersionUID = 1;
    private static final Pattern PAT_TIPC_URI_HOST_AND_PORT = Pattern.compile("^((?:(?:(?<scope>cluster|node|default|[0-9a-fx]+)\\-)?(?<type>service|service-range|socket)\\.)|(?<scope2>cluster|node|default|[0-9a-fx]+)\\-(?<type2>[0-9a-fx]+)\\.)?(?<a>[0-9a-fx]+)\\.(?<b>[0-9a-fx]+)(?:\\.(?<c>[0-9a-fx]+))?(?:\\:(?<javaPort>[0-9]+))?$");
    public static final int TIPC_TOP_SRV = 1;
    public static final int TIPC_RESERVED_TYPES = 64;
    private static AFAddressFamily<AFTIPCSocketAddress> afTipc;

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFTIPCSocketAddress$AddressType.class */
    @NonNullByDefault
    public static final class AddressType extends NamedInteger {
        private static final long serialVersionUID = 1;
        public static final AddressType SERVICE_RANGE;
        public static final AddressType SERVICE_ADDR;
        public static final AddressType SOCKET_ADDR;
        private static final AddressType[] VALUES;
        private final DebugStringProvider ds;

        /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFTIPCSocketAddress$AddressType$DebugStringProvider.class */
        @FunctionalInterface
        interface DebugStringProvider extends Serializable {
            String toDebugString(int i, int i2, int i3);
        }

        private static /* synthetic */ Object $deserializeLambda$(SerializedLambda lambda) {
            switch (lambda.getImplMethodName()) {
                case "lambda$static$faadebcb$3":
                    if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("org/newsclub/net/unix/AFTIPCSocketAddress$AddressType$DebugStringProvider") && lambda.getFunctionalInterfaceMethodName().equals("toDebugString") && lambda.getFunctionalInterfaceMethodSignature().equals("(III)Ljava/lang/String;") && lambda.getImplClass().equals("org/newsclub/net/unix/AFTIPCSocketAddress$AddressType") && lambda.getImplMethodSignature().equals("(III)Ljava/lang/String;")) {
                        return (a, b, c) -> {
                            return formatTIPCInt(a) + "@" + formatTIPCInt(b) + (c == 0 ? "" : ":" + formatTIPCInt(c));
                        };
                    }
                    break;
                case "lambda$new$5c9c2537$1":
                    if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("org/newsclub/net/unix/AFTIPCSocketAddress$AddressType$DebugStringProvider") && lambda.getFunctionalInterfaceMethodName().equals("toDebugString") && lambda.getFunctionalInterfaceMethodSignature().equals("(III)Ljava/lang/String;") && lambda.getImplClass().equals("org/newsclub/net/unix/AFTIPCSocketAddress$AddressType") && lambda.getImplMethodSignature().equals("(III)Ljava/lang/String;")) {
                        return (a2, b2, c2) -> {
                            return ":" + AFSocketAddress.toUnsignedString(a2) + ":" + AFSocketAddress.toUnsignedString(b2) + ":" + AFSocketAddress.toUnsignedString(c2);
                        };
                    }
                    break;
                case "lambda$static$faadebcb$2":
                    if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("org/newsclub/net/unix/AFTIPCSocketAddress$AddressType$DebugStringProvider") && lambda.getFunctionalInterfaceMethodName().equals("toDebugString") && lambda.getFunctionalInterfaceMethodSignature().equals("(III)Ljava/lang/String;") && lambda.getImplClass().equals("org/newsclub/net/unix/AFTIPCSocketAddress$AddressType") && lambda.getImplMethodSignature().equals("(III)Ljava/lang/String;")) {
                        return (a3, b3, c3) -> {
                            return formatTIPCInt(a3) + "@" + formatTIPCInt(b3) + (c3 == 0 ? "" : ":" + formatTIPCInt(c3));
                        };
                    }
                    break;
                case "lambda$static$faadebcb$1":
                    if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("org/newsclub/net/unix/AFTIPCSocketAddress$AddressType$DebugStringProvider") && lambda.getFunctionalInterfaceMethodName().equals("toDebugString") && lambda.getFunctionalInterfaceMethodSignature().equals("(III)Ljava/lang/String;") && lambda.getImplClass().equals("org/newsclub/net/unix/AFTIPCSocketAddress$AddressType") && lambda.getImplMethodSignature().equals("(III)Ljava/lang/String;")) {
                        return (a4, b4, c4) -> {
                            return formatTIPCInt(a4) + "@" + formatTIPCInt(b4) + "-" + formatTIPCInt(c4);
                        };
                    }
                    break;
            }
            throw new IllegalArgumentException("Invalid lambda deserialization");
        }

        static {
            AddressType addressType = new AddressType("SERVICE_RANGE", 1, (a4, b4, c4) -> {
                return formatTIPCInt(a4) + "@" + formatTIPCInt(b4) + "-" + formatTIPCInt(c4);
            });
            SERVICE_RANGE = addressType;
            AddressType addressType2 = new AddressType("SERVICE_ADDR", 2, (a3, b3, c3) -> {
                return formatTIPCInt(a3) + "@" + formatTIPCInt(b3) + (c3 == 0 ? "" : ":" + formatTIPCInt(c3));
            });
            SERVICE_ADDR = addressType2;
            AddressType addressType3 = new AddressType("SOCKET_ADDR", 3, (a, b, c) -> {
                return formatTIPCInt(a) + "@" + formatTIPCInt(b) + (c == 0 ? "" : ":" + formatTIPCInt(c));
            });
            SOCKET_ADDR = addressType3;
            VALUES = (AddressType[]) init(new AddressType[]{addressType, addressType2, addressType3});
        }

        private AddressType(int id) {
            super(id);
            this.ds = (a2, b2, c2) -> {
                return ":" + AFSocketAddress.toUnsignedString(a2) + ":" + AFSocketAddress.toUnsignedString(b2) + ":" + AFSocketAddress.toUnsignedString(c2);
            };
        }

        private AddressType(String name, int id, DebugStringProvider ds) {
            super(name, id);
            this.ds = ds;
        }

        static AddressType ofValue(int v) {
            return (AddressType) ofValue(VALUES, AddressType::new, v);
        }

        public static String formatTIPCInt(int i) {
            return String.format(Locale.ENGLISH, "0x%08x", Long.valueOf(((long) i) & 4294967295L));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String toDebugString(Scope scope, int a, int b, int c) {
            if (this == SOCKET_ADDR && scope.equals(Scope.SCOPE_NOT_SPECIFIED)) {
                return name() + "(" + value() + ");" + this.ds.toDebugString(a, b, c);
            }
            return name() + "(" + value() + ");" + scope + ":" + this.ds.toDebugString(a, b, c);
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFTIPCSocketAddress$Scope.class */
    @NonNullByDefault
    public static final class Scope extends NamedInteger {
        private static final long serialVersionUID = 1;
        public static final Scope SCOPE_CLUSTER;
        public static final Scope SCOPE_NODE;
        public static final Scope SCOPE_NOT_SPECIFIED;
        private static final Scope[] VALUES;

        static {
            Scope scope = new Scope("SCOPE_NOT_SPECIFIED", 0);
            SCOPE_NOT_SPECIFIED = scope;
            Scope scope2 = new Scope("SCOPE_CLUSTER", 2);
            SCOPE_CLUSTER = scope2;
            Scope scope3 = new Scope("SCOPE_NODE", 3);
            SCOPE_NODE = scope3;
            VALUES = (Scope[]) init(new Scope[]{scope, scope2, scope3});
        }

        private Scope(int id) {
            super(id);
        }

        private Scope(String name, int id) {
            super(name, id);
        }

        public static Scope ofValue(int v) {
            return (Scope) ofValue(VALUES, Scope::new, v);
        }
    }

    private AFTIPCSocketAddress(int port, byte[] socketAddress, ByteBuffer nativeAddress) throws SocketException {
        super(port, socketAddress, nativeAddress, addressFamily());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static AFTIPCSocketAddress newAFSocketAddress(int port, byte[] socketAddress, ByteBuffer nativeAddress) throws SocketException {
        return (AFTIPCSocketAddress) newDeserializedAFSocketAddress(port, socketAddress, nativeAddress, addressFamily(), AFTIPCSocketAddress::new);
    }

    public static AFTIPCSocketAddress ofService(Scope scope, int type, int instance) throws SocketException {
        return ofService(scope, type, instance, 0);
    }

    public static AFTIPCSocketAddress ofService(int type, int instance) throws SocketException {
        return ofService(Scope.SCOPE_CLUSTER, type, instance, 0);
    }

    public static AFTIPCSocketAddress ofService(Scope scope, int type, int instance, int domain) throws SocketException {
        return ofService(0, scope, type, instance, domain);
    }

    public static AFTIPCSocketAddress ofService(int javaPort, Scope scope, int type, int instance, int domain) throws SocketException {
        return (AFTIPCSocketAddress) resolveAddress(toBytes(AddressType.SERVICE_ADDR, scope, type, instance, domain), javaPort, addressFamily());
    }

    public static AFTIPCSocketAddress ofServiceRange(Scope scope, int type, int lower, int upper) throws SocketException {
        return ofServiceRange(0, scope, type, lower, upper);
    }

    public static AFTIPCSocketAddress ofServiceRange(int type, int lower, int upper) throws SocketException {
        return ofServiceRange(0, Scope.SCOPE_CLUSTER, type, lower, upper);
    }

    public static AFTIPCSocketAddress ofServiceRange(int javaPort, Scope scope, int type, int lower, int upper) throws SocketException {
        return (AFTIPCSocketAddress) resolveAddress(toBytes(AddressType.SERVICE_RANGE, scope, type, lower, upper), javaPort, addressFamily());
    }

    public static AFTIPCSocketAddress ofSocket(int ref, int node) throws SocketException {
        return ofSocket(0, ref, node);
    }

    public static AFTIPCSocketAddress ofSocket(int javaPort, int ref, int node) throws SocketException {
        return (AFTIPCSocketAddress) resolveAddress(toBytes(AddressType.SOCKET_ADDR, Scope.SCOPE_NOT_SPECIFIED, ref, node, 0), javaPort, addressFamily());
    }

    public static AFTIPCSocketAddress ofTopologyService() throws SocketException {
        return (AFTIPCSocketAddress) resolveAddress(toBytes(AddressType.SERVICE_ADDR, Scope.SCOPE_NOT_SPECIFIED, 1, 1, 0), 0, addressFamily());
    }

    private static int parseUnsignedInt(String v) {
        if (v.startsWith("0x")) {
            return parseUnsignedInt(v.substring(2), 16);
        }
        return parseUnsignedInt(v, 10);
    }

    public static AFTIPCSocketAddress unwrap(InetAddress address, int port) throws SocketException {
        return (AFTIPCSocketAddress) AFSocketAddress.unwrap(address, port, addressFamily());
    }

    public static AFTIPCSocketAddress unwrap(String hostname, int port) throws SocketException {
        return (AFTIPCSocketAddress) AFSocketAddress.unwrap(hostname, port, addressFamily());
    }

    public static AFTIPCSocketAddress unwrap(SocketAddress address) throws SocketException {
        Objects.requireNonNull(address);
        if (!isSupportedAddress(address)) {
            throw new SocketException("Unsupported address");
        }
        return (AFTIPCSocketAddress) address;
    }

    public Scope getScope() {
        byte[] bytes = getBytes();
        if (bytes.length != 20) {
            return Scope.SCOPE_NOT_SPECIFIED;
        }
        return Scope.ofValue(ByteBuffer.wrap(bytes, 4, 4).getInt());
    }

    public int getTIPCType() {
        ByteBuffer bb = ByteBuffer.wrap(getBytes());
        int a = bb.getInt(8);
        return a;
    }

    public int getTIPCInstance() {
        ByteBuffer bb = ByteBuffer.wrap(getBytes());
        int a = bb.getInt(12);
        return a;
    }

    public int getTIPCDomain() {
        ByteBuffer bb = ByteBuffer.wrap(getBytes());
        int a = bb.getInt(16);
        return a;
    }

    public int getTIPCLower() {
        ByteBuffer bb = ByteBuffer.wrap(getBytes());
        int a = bb.getInt(8);
        return a;
    }

    public int getTIPCUpper() {
        ByteBuffer bb = ByteBuffer.wrap(getBytes());
        int a = bb.getInt(12);
        return a;
    }

    public int getTIPCRef() {
        ByteBuffer bb = ByteBuffer.wrap(getBytes());
        int a = bb.getInt(8);
        return a;
    }

    public int getTIPCNodeHash() {
        ByteBuffer bb = ByteBuffer.wrap(getBytes());
        int a = bb.getInt(12);
        return a;
    }

    @Override // java.net.InetSocketAddress
    public String toString() {
        int port = getPort();
        byte[] bytes = getBytes();
        if (bytes.length != 20) {
            return getClass().getName() + "[" + (port == 0 ? "" : "port=" + port) + ";UNKNOWN]";
        }
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        int typeId = bb.getInt();
        int scopeId = bb.getInt();
        int a = bb.getInt();
        int b = bb.getInt();
        int c = bb.getInt();
        Scope scope = Scope.ofValue((byte) scopeId);
        AddressType type = AddressType.ofValue(typeId);
        String typeString = type.toDebugString(scope, a, b, c);
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
        return addr instanceof AFTIPCSocketAddress;
    }

    private static byte[] toBytes(AddressType addrType, Scope scope, int a, int b, int c) {
        ByteBuffer bb = ByteBuffer.allocate(20);
        bb.putInt(addrType.value());
        bb.putInt(scope.value());
        bb.putInt(a);
        bb.putInt(b);
        bb.putInt(c);
        return (byte[]) bb.flip().array();
    }

    public static synchronized AFAddressFamily<AFTIPCSocketAddress> addressFamily() {
        if (afTipc == null) {
            afTipc = AFAddressFamily.registerAddressFamily("tipc", AFTIPCSocketAddress.class, new AFSocketAddressConfig<AFTIPCSocketAddress>() { // from class: org.newsclub.net.unix.AFTIPCSocketAddress.1
                private final AFSocketAddress.AFSocketAddressConstructor<AFTIPCSocketAddress> addrConstr;

                {
                    this.addrConstr = AFSocketAddress.isUseDeserializationForInit() ? (x$0, x$1, x$2) -> {
                        return AFTIPCSocketAddress.newAFSocketAddress(x$0, x$1, x$2);
                    } : (x$02, x$12, x$22) -> {
                        return new AFTIPCSocketAddress(x$02, x$12, x$22);
                    };
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.newsclub.net.unix.AFSocketAddressConfig
                public AFTIPCSocketAddress parseURI(URI u, int port) throws SocketException {
                    return AFTIPCSocketAddress.of(u, port);
                }

                @Override // org.newsclub.net.unix.AFSocketAddressConfig
                protected AFSocketAddress.AFSocketAddressConstructor<AFTIPCSocketAddress> addressConstructor() {
                    return this.addrConstr;
                }

                @Override // org.newsclub.net.unix.AFSocketAddressConfig
                protected String selectorProviderClassname() {
                    return "org.newsclub.net.unix.tipc.AFTIPCSelectorProvider";
                }

                @Override // org.newsclub.net.unix.AFSocketAddressConfig
                protected Set<String> uriSchemes() {
                    return new HashSet(Arrays.asList("tipc", "http+tipc", "https+tipc"));
                }
            });
            try {
                Class.forName("org.newsclub.net.unix.tipc.AFTIPCSelectorProvider");
            } catch (ClassNotFoundException e) {
            }
        }
        return afTipc;
    }

    private String toTipcInt(int v) {
        if (v < 0) {
            return "0x" + toUnsignedString(v, 16);
        }
        return toUnsignedString(v);
    }

    public static AFTIPCSocketAddress of(URI uri) throws SocketException {
        return of(uri, -1);
    }

    public static AFTIPCSocketAddress of(URI uri, int overridePort) throws SocketException {
        String typeStr;
        String scopeStr;
        AddressType addrType;
        Scope scope;
        int c;
        int at;
        switch (uri.getScheme()) {
            case "tipc":
            case "http+tipc":
            case "https+tipc":
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
                int port = overridePort != -1 ? overridePort : uri.getPort();
                if (port != -1) {
                    host = host + ":" + port;
                }
                try {
                    Matcher m = PAT_TIPC_URI_HOST_AND_PORT.matcher(host);
                    if (!m.matches()) {
                        throw new SocketException("Invalid TIPC URI: " + uri);
                    }
                    typeStr = m.group("type");
                    scopeStr = m.group("scope");
                    if (typeStr == null) {
                        typeStr = m.group("type2");
                        scopeStr = m.group("scope2");
                    }
                    String strA = m.group("a");
                    String strB = m.group("b");
                    String strC = m.group("c");
                    String javaPortStr = m.group("javaPort");
                    switch (typeStr == null ? "" : typeStr) {
                        case "service":
                            addrType = AddressType.SERVICE_ADDR;
                            break;
                        case "service-range":
                            addrType = AddressType.SERVICE_RANGE;
                            break;
                        case "socket":
                            addrType = AddressType.SOCKET_ADDR;
                            break;
                        case "":
                            addrType = AddressType.SERVICE_ADDR;
                            break;
                        default:
                            addrType = AddressType.ofValue(parseUnsignedInt(typeStr));
                            break;
                    }
                    switch (scopeStr == null ? "" : scopeStr) {
                        case "cluster":
                            scope = Scope.SCOPE_CLUSTER;
                            break;
                        case "node":
                            scope = Scope.SCOPE_NODE;
                            break;
                        case "default":
                            scope = Scope.SCOPE_NOT_SPECIFIED;
                            break;
                        case "":
                            if (addrType == AddressType.SERVICE_ADDR || addrType == AddressType.SERVICE_RANGE) {
                                scope = Scope.SCOPE_CLUSTER;
                                break;
                            } else {
                                scope = Scope.SCOPE_NOT_SPECIFIED;
                                break;
                            }
                            break;
                        default:
                            scope = Scope.ofValue(parseUnsignedInt(scopeStr));
                            break;
                    }
                    int a = parseUnsignedInt(strA);
                    int b = parseUnsignedInt(strB);
                    if (strC == null || strC.isEmpty()) {
                        if (addrType == AddressType.SERVICE_RANGE) {
                            c = b;
                        } else {
                            c = 0;
                        }
                    } else {
                        c = parseUnsignedInt(strC);
                    }
                    int javaPort = (javaPortStr == null || javaPortStr.isEmpty()) ? port : Integer.parseInt(javaPortStr);
                    if (overridePort != -1) {
                        javaPort = overridePort;
                    }
                    return (AFTIPCSocketAddress) resolveAddress(toBytes(addrType, scope, a, b, c), javaPort, addressFamily());
                } catch (IllegalArgumentException e) {
                    throw ((SocketException) new SocketException("Invalid TIPC URI: " + uri).initCause(e));
                }
            default:
                throw new SocketException("Unsupported URI scheme: " + uri.getScheme());
        }
    }

    @Override // org.newsclub.net.unix.AFSocketAddress
    public URI toURI(String scheme, URI template) throws IOException {
        switch (scheme) {
            case "tipc":
            case "http+tipc":
            case "https+tipc":
                byte[] bytes = getBytes();
                if (bytes.length == 20) {
                    ByteBuffer bb = ByteBuffer.wrap(bytes);
                    AddressType addrType = AddressType.ofValue(bb.getInt());
                    Scope scope = Scope.ofValue(bb.getInt());
                    StringBuilder sb = new StringBuilder();
                    boolean haveScope = true;
                    if (scope == Scope.SCOPE_NOT_SPECIFIED) {
                        sb.append("default-");
                    } else if (scope == Scope.SCOPE_CLUSTER) {
                        if (addrType == AddressType.SERVICE_ADDR || addrType == AddressType.SERVICE_RANGE) {
                            haveScope = false;
                        } else {
                            sb.append("cluster-");
                        }
                    } else if (scope == Scope.SCOPE_NODE) {
                        sb.append("node-");
                    } else {
                        sb.append(toTipcInt(scope.value()));
                        sb.append('-');
                    }
                    boolean addrTypeImplied = false;
                    if (addrType == AddressType.SERVICE_ADDR) {
                        if (!haveScope) {
                            addrTypeImplied = true;
                        } else {
                            sb.append("service");
                        }
                    } else if (addrType == AddressType.SERVICE_RANGE) {
                        sb.append("service-range");
                    } else if (addrType == AddressType.SOCKET_ADDR) {
                        sb.append("socket");
                    } else {
                        sb.append(toTipcInt(addrType.value()));
                    }
                    if (!addrTypeImplied) {
                        sb.append('.');
                    }
                    int a = bb.getInt();
                    int b = bb.getInt();
                    int c = bb.getInt();
                    sb.append(toTipcInt(a));
                    sb.append('.');
                    sb.append(toTipcInt(b));
                    if (c != 0) {
                        sb.append('.');
                        sb.append(toTipcInt(c));
                    }
                    break;
                } else {
                    break;
                }
                break;
        }
        return super.toURI(scheme, template);
    }
}
