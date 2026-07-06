/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jdt.annotation.NonNull
 *  org.eclipse.jdt.annotation.NonNullByDefault
 */
package org.newsclub.net.unix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
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
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.newsclub.net.unix.AFAddressFamily;
import org.newsclub.net.unix.AFSocketAddress;
import org.newsclub.net.unix.AFSocketAddressConfig;
import org.newsclub.net.unix.HostAndPort;
import org.newsclub.net.unix.NamedInteger;

public final class AFTIPCSocketAddress
extends AFSocketAddress {
    private static final long serialVersionUID = 1L;
    private static final Pattern PAT_TIPC_URI_HOST_AND_PORT = Pattern.compile("^((?:(?:(?<scope>cluster|node|default|[0-9a-fx]+)\\-)?(?<type>service|service-range|socket)\\.)|(?<scope2>cluster|node|default|[0-9a-fx]+)\\-(?<type2>[0-9a-fx]+)\\.)?(?<a>[0-9a-fx]+)\\.(?<b>[0-9a-fx]+)(?:\\.(?<c>[0-9a-fx]+))?(?:\\:(?<javaPort>[0-9]+))?$");
    public static final int TIPC_TOP_SRV = 1;
    public static final int TIPC_RESERVED_TYPES = 64;
    private static AFAddressFamily<AFTIPCSocketAddress> afTipc;

    private AFTIPCSocketAddress(int port, byte[] socketAddress, ByteBuffer nativeAddress) throws SocketException {
        super(port, socketAddress, nativeAddress, AFTIPCSocketAddress.addressFamily());
    }

    private static AFTIPCSocketAddress newAFSocketAddress(int port, byte[] socketAddress, ByteBuffer nativeAddress) throws SocketException {
        return AFTIPCSocketAddress.newDeserializedAFSocketAddress(port, socketAddress, nativeAddress, AFTIPCSocketAddress.addressFamily(), AFTIPCSocketAddress::new);
    }

    public static AFTIPCSocketAddress ofService(Scope scope, int type, int instance) throws SocketException {
        return AFTIPCSocketAddress.ofService(scope, type, instance, 0);
    }

    public static AFTIPCSocketAddress ofService(int type, int instance) throws SocketException {
        return AFTIPCSocketAddress.ofService(Scope.SCOPE_CLUSTER, type, instance, 0);
    }

    public static AFTIPCSocketAddress ofService(Scope scope, int type, int instance, int domain) throws SocketException {
        return AFTIPCSocketAddress.ofService(0, scope, type, instance, domain);
    }

    public static AFTIPCSocketAddress ofService(int javaPort, Scope scope, int type, int instance, int domain) throws SocketException {
        return AFTIPCSocketAddress.resolveAddress(AFTIPCSocketAddress.toBytes(AddressType.SERVICE_ADDR, scope, type, instance, domain), javaPort, AFTIPCSocketAddress.addressFamily());
    }

    public static AFTIPCSocketAddress ofServiceRange(Scope scope, int type, int lower, int upper) throws SocketException {
        return AFTIPCSocketAddress.ofServiceRange(0, scope, type, lower, upper);
    }

    public static AFTIPCSocketAddress ofServiceRange(int type, int lower, int upper) throws SocketException {
        return AFTIPCSocketAddress.ofServiceRange(0, Scope.SCOPE_CLUSTER, type, lower, upper);
    }

    public static AFTIPCSocketAddress ofServiceRange(int javaPort, Scope scope, int type, int lower, int upper) throws SocketException {
        return AFTIPCSocketAddress.resolveAddress(AFTIPCSocketAddress.toBytes(AddressType.SERVICE_RANGE, scope, type, lower, upper), javaPort, AFTIPCSocketAddress.addressFamily());
    }

    public static AFTIPCSocketAddress ofSocket(int ref, int node) throws SocketException {
        return AFTIPCSocketAddress.ofSocket(0, ref, node);
    }

    public static AFTIPCSocketAddress ofSocket(int javaPort, int ref, int node) throws SocketException {
        return AFTIPCSocketAddress.resolveAddress(AFTIPCSocketAddress.toBytes(AddressType.SOCKET_ADDR, Scope.SCOPE_NOT_SPECIFIED, ref, node, 0), javaPort, AFTIPCSocketAddress.addressFamily());
    }

    public static AFTIPCSocketAddress ofTopologyService() throws SocketException {
        return AFTIPCSocketAddress.resolveAddress(AFTIPCSocketAddress.toBytes(AddressType.SERVICE_ADDR, Scope.SCOPE_NOT_SPECIFIED, 1, 1, 0), 0, AFTIPCSocketAddress.addressFamily());
    }

    private static int parseUnsignedInt(String v) {
        if (v.startsWith("0x")) {
            return AFTIPCSocketAddress.parseUnsignedInt(v.substring(2), 16);
        }
        return AFTIPCSocketAddress.parseUnsignedInt(v, 10);
    }

    public static AFTIPCSocketAddress unwrap(InetAddress address, int port) throws SocketException {
        return AFSocketAddress.unwrap(address, port, AFTIPCSocketAddress.addressFamily());
    }

    public static AFTIPCSocketAddress unwrap(String hostname, int port) throws SocketException {
        return AFSocketAddress.unwrap(hostname, port, AFTIPCSocketAddress.addressFamily());
    }

    public static AFTIPCSocketAddress unwrap(SocketAddress address) throws SocketException {
        Objects.requireNonNull(address);
        if (!AFTIPCSocketAddress.isSupportedAddress(address)) {
            throw new SocketException("Unsupported address");
        }
        return (AFTIPCSocketAddress)address;
    }

    public Scope getScope() {
        byte[] bytes = this.getBytes();
        if (bytes.length != 20) {
            return Scope.SCOPE_NOT_SPECIFIED;
        }
        return Scope.ofValue(ByteBuffer.wrap(bytes, 4, 4).getInt());
    }

    public int getTIPCType() {
        ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        int a = bb.getInt(8);
        return a;
    }

    public int getTIPCInstance() {
        ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        int a = bb.getInt(12);
        return a;
    }

    public int getTIPCDomain() {
        ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        int a = bb.getInt(16);
        return a;
    }

    public int getTIPCLower() {
        ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        int a = bb.getInt(8);
        return a;
    }

    public int getTIPCUpper() {
        ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        int a = bb.getInt(12);
        return a;
    }

    public int getTIPCRef() {
        ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        int a = bb.getInt(8);
        return a;
    }

    public int getTIPCNodeHash() {
        ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        int a = bb.getInt(12);
        return a;
    }

    @Override
    public String toString() {
        int port = this.getPort();
        byte[] bytes = this.getBytes();
        if (bytes.length != 20) {
            return this.getClass().getName() + "[" + (port == 0 ? "" : "port=" + port) + ";UNKNOWN]";
        }
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        int typeId = bb.getInt();
        int scopeId = bb.getInt();
        int a = bb.getInt();
        int b = bb.getInt();
        int c = bb.getInt();
        Scope scope = Scope.ofValue((byte)scopeId);
        AddressType type = AddressType.ofValue(typeId);
        String typeString = type.toDebugString(scope, a, b, c);
        return this.getClass().getName() + "[" + (port == 0 ? "" : "port=" + port + ";") + typeString + "]";
    }

    @Override
    public boolean hasFilename() {
        return false;
    }

    @Override
    public File getFile() throws FileNotFoundException {
        throw new FileNotFoundException("no file");
    }

    public static boolean isSupportedAddress(InetAddress addr) {
        return AFSocketAddress.isSupportedAddress(addr, AFTIPCSocketAddress.addressFamily());
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
        return (byte[])bb.flip().array();
    }

    public static synchronized AFAddressFamily<AFTIPCSocketAddress> addressFamily() {
        if (afTipc == null) {
            afTipc = AFAddressFamily.registerAddressFamily("tipc", AFTIPCSocketAddress.class, new AFSocketAddressConfig<AFTIPCSocketAddress>(){
                private final AFSocketAddress.AFSocketAddressConstructor<AFTIPCSocketAddress> addrConstr = AFSocketAddress.isUseDeserializationForInit() ? (x$0, x$1, x$2) -> AFTIPCSocketAddress.access$200(x$0, x$1, x$2) : (x$0, x$1, x$2) -> new AFTIPCSocketAddress(x$0, x$1, x$2);

                @Override
                protected AFTIPCSocketAddress parseURI(URI u, int port) throws SocketException {
                    return AFTIPCSocketAddress.of(u, port);
                }

                @Override
                protected AFSocketAddress.AFSocketAddressConstructor<AFTIPCSocketAddress> addressConstructor() {
                    return this.addrConstr;
                }

                @Override
                protected String selectorProviderClassname() {
                    return "org.newsclub.net.unix.tipc.AFTIPCSelectorProvider";
                }

                @Override
                protected Set<String> uriSchemes() {
                    return new HashSet<String>(Arrays.asList("tipc", "http+tipc", "https+tipc"));
                }
            });
            try {
                Class.forName("org.newsclub.net.unix.tipc.AFTIPCSelectorProvider");
            }
            catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
        }
        return afTipc;
    }

    private String toTipcInt(int v) {
        if (v < 0) {
            return "0x" + AFTIPCSocketAddress.toUnsignedString(v, 16);
        }
        return AFTIPCSocketAddress.toUnsignedString(v);
    }

    public static AFTIPCSocketAddress of(URI uri) throws SocketException {
        return AFTIPCSocketAddress.of(uri, -1);
    }

    public static AFTIPCSocketAddress of(URI uri, int overridePort) throws SocketException {
        int port;
        int at;
        switch (uri.getScheme()) {
            case "tipc": 
            case "http+tipc": 
            case "https+tipc": {
                break;
            }
            default: {
                throw new SocketException("Unsupported URI scheme: " + uri.getScheme());
            }
        }
        String host = uri.getHost();
        if (host == null && (host = uri.getAuthority()) != null && (at = host.indexOf(64)) >= 0) {
            host = host.substring(at + 1);
        }
        if (host == null) {
            throw new SocketException("Cannot get hostname from URI: " + uri);
        }
        int n = port = overridePort != -1 ? overridePort : uri.getPort();
        if (port != -1) {
            host = host + ":" + port;
        }
        try {
            int javaPort;
            Scope scope;
            AddressType addrType;
            Matcher m = PAT_TIPC_URI_HOST_AND_PORT.matcher(host);
            if (!m.matches()) {
                throw new SocketException("Invalid TIPC URI: " + uri);
            }
            String typeStr = m.group("type");
            String scopeStr = m.group("scope");
            if (typeStr == null) {
                typeStr = m.group("type2");
                scopeStr = m.group("scope2");
            }
            String strA = m.group("a");
            String strB = m.group("b");
            String strC = m.group("c");
            String javaPortStr = m.group("javaPort");
            switch (typeStr == null ? "" : typeStr) {
                case "service": {
                    addrType = AddressType.SERVICE_ADDR;
                    break;
                }
                case "service-range": {
                    addrType = AddressType.SERVICE_RANGE;
                    break;
                }
                case "socket": {
                    addrType = AddressType.SOCKET_ADDR;
                    break;
                }
                case "": {
                    addrType = AddressType.SERVICE_ADDR;
                    break;
                }
                default: {
                    addrType = AddressType.ofValue(AFTIPCSocketAddress.parseUnsignedInt(typeStr));
                }
            }
            switch (scopeStr == null ? "" : scopeStr) {
                case "cluster": {
                    scope = Scope.SCOPE_CLUSTER;
                    break;
                }
                case "node": {
                    scope = Scope.SCOPE_NODE;
                    break;
                }
                case "default": {
                    scope = Scope.SCOPE_NOT_SPECIFIED;
                    break;
                }
                case "": {
                    if (addrType == AddressType.SERVICE_ADDR || addrType == AddressType.SERVICE_RANGE) {
                        scope = Scope.SCOPE_CLUSTER;
                        break;
                    }
                    scope = Scope.SCOPE_NOT_SPECIFIED;
                    break;
                }
                default: {
                    scope = Scope.ofValue(AFTIPCSocketAddress.parseUnsignedInt(scopeStr));
                }
            }
            int a = AFTIPCSocketAddress.parseUnsignedInt(strA);
            int b = AFTIPCSocketAddress.parseUnsignedInt(strB);
            int c = strC == null || strC.isEmpty() ? (addrType == AddressType.SERVICE_RANGE ? b : 0) : AFTIPCSocketAddress.parseUnsignedInt(strC);
            int n2 = javaPort = javaPortStr == null || javaPortStr.isEmpty() ? port : Integer.parseInt(javaPortStr);
            if (overridePort != -1) {
                javaPort = overridePort;
            }
            return AFTIPCSocketAddress.resolveAddress(AFTIPCSocketAddress.toBytes(addrType, scope, a, b, c), javaPort, AFTIPCSocketAddress.addressFamily());
        }
        catch (IllegalArgumentException e) {
            throw (SocketException)new SocketException("Invalid TIPC URI: " + uri).initCause(e);
        }
    }

    @Override
    public URI toURI(String scheme, URI template) throws IOException {
        switch (scheme) {
            case "tipc": 
            case "http+tipc": 
            case "https+tipc": {
                break;
            }
            default: {
                return super.toURI(scheme, template);
            }
        }
        byte[] bytes = this.getBytes();
        if (bytes.length != 20) {
            return super.toURI(scheme, template);
        }
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
            sb.append(this.toTipcInt(scope.value()));
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
            sb.append(this.toTipcInt(addrType.value()));
        }
        if (!addrTypeImplied) {
            sb.append('.');
        }
        int a = bb.getInt();
        int b = bb.getInt();
        int c = bb.getInt();
        sb.append(this.toTipcInt(a));
        sb.append('.');
        sb.append(this.toTipcInt(b));
        if (c != 0) {
            sb.append('.');
            sb.append(this.toTipcInt(c));
        }
        return new HostAndPort(sb.toString(), this.getPort()).toURI(scheme, template);
    }

    @NonNullByDefault
    public static final class Scope
    extends NamedInteger {
        private static final long serialVersionUID = 1L;
        public static final Scope SCOPE_CLUSTER;
        public static final Scope SCOPE_NODE;
        public static final Scope SCOPE_NOT_SPECIFIED;
        private static final @NonNull Scope[] VALUES;

        private Scope(int id) {
            super(id);
        }

        private Scope(String name, int id) {
            super(name, id);
        }

        public static Scope ofValue(int v) {
            return (Scope)Scope.ofValue((NamedInteger[])VALUES, Scope::new, (int)v);
        }

        static {
            SCOPE_NOT_SPECIFIED = new Scope("SCOPE_NOT_SPECIFIED", 0);
            SCOPE_CLUSTER = new Scope("SCOPE_CLUSTER", 2);
            SCOPE_NODE = new Scope("SCOPE_NODE", 3);
            VALUES = (Scope[])Scope.init((NamedInteger[])new Scope[]{SCOPE_NOT_SPECIFIED, SCOPE_CLUSTER, SCOPE_NODE});
        }
    }

    @NonNullByDefault
    public static final class AddressType
    extends NamedInteger {
        private static final long serialVersionUID = 1L;
        public static final AddressType SERVICE_RANGE = new AddressType("SERVICE_RANGE", 1, (a, b, c) -> AddressType.formatTIPCInt(a) + "@" + AddressType.formatTIPCInt(b) + "-" + AddressType.formatTIPCInt(c));
        public static final AddressType SERVICE_ADDR = new AddressType("SERVICE_ADDR", 2, (a, b, c) -> AddressType.formatTIPCInt(a) + "@" + AddressType.formatTIPCInt(b) + (c == 0 ? "" : ":" + AddressType.formatTIPCInt(c)));
        public static final AddressType SOCKET_ADDR = new AddressType("SOCKET_ADDR", 3, (a, b, c) -> AddressType.formatTIPCInt(a) + "@" + AddressType.formatTIPCInt(b) + (c == 0 ? "" : ":" + AddressType.formatTIPCInt(c)));
        private static final @NonNull AddressType[] VALUES = (AddressType[])AddressType.init((NamedInteger[])new AddressType[]{SERVICE_RANGE, SERVICE_ADDR, SOCKET_ADDR});
        private final DebugStringProvider ds;

        private AddressType(int id) {
            super(id);
            this.ds = (a, b, c) -> ":" + AFSocketAddress.toUnsignedString(a) + ":" + AFSocketAddress.toUnsignedString(b) + ":" + AFSocketAddress.toUnsignedString(c);
        }

        private AddressType(String name, int id, DebugStringProvider ds) {
            super(name, id);
            this.ds = ds;
        }

        static AddressType ofValue(int v) {
            return (AddressType)AddressType.ofValue((NamedInteger[])VALUES, AddressType::new, (int)v);
        }

        public static String formatTIPCInt(int i) {
            return String.format(Locale.ENGLISH, "0x%08x", (long)i & 0xFFFFFFFFL);
        }

        private String toDebugString(Scope scope, int a, int b, int c) {
            if (this == SOCKET_ADDR && scope.equals(Scope.SCOPE_NOT_SPECIFIED)) {
                return this.name() + "(" + this.value() + ");" + this.ds.toDebugString(a, b, c);
            }
            return this.name() + "(" + this.value() + ");" + scope + ":" + this.ds.toDebugString(a, b, c);
        }

        @FunctionalInterface
        static interface DebugStringProvider
        extends Serializable {
            public String toDebugString(int var1, int var2, int var3);
        }
    }
}

