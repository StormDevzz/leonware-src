// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.Locale;
import org.eclipse.jdt.annotation.NonNullByDefault;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Set;
import java.net.URI;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Objects;
import java.net.SocketAddress;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.regex.Pattern;

public final class AFTIPCSocketAddress extends AFSocketAddress
{
    private static final long serialVersionUID = 1L;
    private static final Pattern PAT_TIPC_URI_HOST_AND_PORT;
    public static final int TIPC_TOP_SRV = 1;
    public static final int TIPC_RESERVED_TYPES = 64;
    private static AFAddressFamily<AFTIPCSocketAddress> afTipc;
    
    private AFTIPCSocketAddress(final int port, final byte[] socketAddress, final ByteBuffer nativeAddress) throws SocketException {
        super(port, socketAddress, nativeAddress, addressFamily());
    }
    
    private static AFTIPCSocketAddress newAFSocketAddress(final int port, final byte[] socketAddress, final ByteBuffer nativeAddress) throws SocketException {
        return AFSocketAddress.newDeserializedAFSocketAddress(port, socketAddress, nativeAddress, addressFamily(), AFTIPCSocketAddress::new);
    }
    
    public static AFTIPCSocketAddress ofService(final Scope scope, final int type, final int instance) throws SocketException {
        return ofService(scope, type, instance, 0);
    }
    
    public static AFTIPCSocketAddress ofService(final int type, final int instance) throws SocketException {
        return ofService(Scope.SCOPE_CLUSTER, type, instance, 0);
    }
    
    public static AFTIPCSocketAddress ofService(final Scope scope, final int type, final int instance, final int domain) throws SocketException {
        return ofService(0, scope, type, instance, domain);
    }
    
    public static AFTIPCSocketAddress ofService(final int javaPort, final Scope scope, final int type, final int instance, final int domain) throws SocketException {
        return AFSocketAddress.resolveAddress(toBytes(AddressType.SERVICE_ADDR, scope, type, instance, domain), javaPort, addressFamily());
    }
    
    public static AFTIPCSocketAddress ofServiceRange(final Scope scope, final int type, final int lower, final int upper) throws SocketException {
        return ofServiceRange(0, scope, type, lower, upper);
    }
    
    public static AFTIPCSocketAddress ofServiceRange(final int type, final int lower, final int upper) throws SocketException {
        return ofServiceRange(0, Scope.SCOPE_CLUSTER, type, lower, upper);
    }
    
    public static AFTIPCSocketAddress ofServiceRange(final int javaPort, final Scope scope, final int type, final int lower, final int upper) throws SocketException {
        return AFSocketAddress.resolveAddress(toBytes(AddressType.SERVICE_RANGE, scope, type, lower, upper), javaPort, addressFamily());
    }
    
    public static AFTIPCSocketAddress ofSocket(final int ref, final int node) throws SocketException {
        return ofSocket(0, ref, node);
    }
    
    public static AFTIPCSocketAddress ofSocket(final int javaPort, final int ref, final int node) throws SocketException {
        return AFSocketAddress.resolveAddress(toBytes(AddressType.SOCKET_ADDR, Scope.SCOPE_NOT_SPECIFIED, ref, node, 0), javaPort, addressFamily());
    }
    
    public static AFTIPCSocketAddress ofTopologyService() throws SocketException {
        return AFSocketAddress.resolveAddress(toBytes(AddressType.SERVICE_ADDR, Scope.SCOPE_NOT_SPECIFIED, 1, 1, 0), 0, addressFamily());
    }
    
    private static int parseUnsignedInt(final String v) {
        if (v.startsWith("0x")) {
            return AFSocketAddress.parseUnsignedInt(v.substring(2), 16);
        }
        return AFSocketAddress.parseUnsignedInt(v, 10);
    }
    
    public static AFTIPCSocketAddress unwrap(final InetAddress address, final int port) throws SocketException {
        return AFSocketAddress.unwrap(address, port, addressFamily());
    }
    
    public static AFTIPCSocketAddress unwrap(final String hostname, final int port) throws SocketException {
        return AFSocketAddress.unwrap(hostname, port, addressFamily());
    }
    
    public static AFTIPCSocketAddress unwrap(final SocketAddress address) throws SocketException {
        Objects.requireNonNull(address);
        if (!isSupportedAddress(address)) {
            throw new SocketException("Unsupported address");
        }
        return (AFTIPCSocketAddress)address;
    }
    
    public Scope getScope() {
        final byte[] bytes = this.getBytes();
        if (bytes.length != 20) {
            return Scope.SCOPE_NOT_SPECIFIED;
        }
        return Scope.ofValue(ByteBuffer.wrap(bytes, 4, 4).getInt());
    }
    
    public int getTIPCType() {
        final ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        final int a = bb.getInt(8);
        return a;
    }
    
    public int getTIPCInstance() {
        final ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        final int a = bb.getInt(12);
        return a;
    }
    
    public int getTIPCDomain() {
        final ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        final int a = bb.getInt(16);
        return a;
    }
    
    public int getTIPCLower() {
        final ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        final int a = bb.getInt(8);
        return a;
    }
    
    public int getTIPCUpper() {
        final ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        final int a = bb.getInt(12);
        return a;
    }
    
    public int getTIPCRef() {
        final ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        final int a = bb.getInt(8);
        return a;
    }
    
    public int getTIPCNodeHash() {
        final ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        final int a = bb.getInt(12);
        return a;
    }
    
    @Override
    public String toString() {
        final int port = this.getPort();
        final byte[] bytes = this.getBytes();
        if (bytes.length != 20) {
            return this.getClass().getName() + "[" + ((port == 0) ? "" : ("port=" + port)) + ";UNKNOWN]";
        }
        final ByteBuffer bb = ByteBuffer.wrap(bytes);
        final int typeId = bb.getInt();
        final int scopeId = bb.getInt();
        final int a = bb.getInt();
        final int b = bb.getInt();
        final int c = bb.getInt();
        final Scope scope = Scope.ofValue((byte)scopeId);
        final AddressType type = AddressType.ofValue(typeId);
        final String typeString = type.toDebugString(scope, a, b, c);
        return this.getClass().getName() + "[" + ((port == 0) ? "" : ("port=" + port + ";")) + typeString + "]";
    }
    
    @Override
    public boolean hasFilename() {
        return false;
    }
    
    @Override
    public File getFile() throws FileNotFoundException {
        throw new FileNotFoundException("no file");
    }
    
    public static boolean isSupportedAddress(final InetAddress addr) {
        return AFSocketAddress.isSupportedAddress(addr, addressFamily());
    }
    
    public static boolean isSupportedAddress(final SocketAddress addr) {
        return addr instanceof AFTIPCSocketAddress;
    }
    
    private static byte[] toBytes(final AddressType addrType, final Scope scope, final int a, final int b, final int c) {
        final ByteBuffer bb = ByteBuffer.allocate(20);
        bb.putInt(addrType.value());
        bb.putInt(scope.value());
        bb.putInt(a);
        bb.putInt(b);
        bb.putInt(c);
        return (byte[])bb.flip().array();
    }
    
    public static synchronized AFAddressFamily<AFTIPCSocketAddress> addressFamily() {
        if (AFTIPCSocketAddress.afTipc == null) {
            AFTIPCSocketAddress.afTipc = AFAddressFamily.registerAddressFamily("tipc", AFTIPCSocketAddress.class, new AFSocketAddressConfig<AFTIPCSocketAddress>() {
                private final AFSocketAddressConstructor<AFTIPCSocketAddress> addrConstr = AFSocketAddress.isUseDeserializationForInit() ? ((x$0, x$1, x$2) -> newAFSocketAddress(x$0, x$1, x$2)) : ((x$0, x$1, x$2) -> new AFTIPCSocketAddress(x$0, x$1, x$2, (AFTIPCSocketAddress$1)null));
                
                @Override
                protected AFTIPCSocketAddress parseURI(final URI u, final int port) throws SocketException {
                    return AFTIPCSocketAddress.of(u, port);
                }
                
                @Override
                protected AFSocketAddressConstructor<AFTIPCSocketAddress> addressConstructor() {
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
            catch (final ClassNotFoundException ex) {}
        }
        return AFTIPCSocketAddress.afTipc;
    }
    
    private String toTipcInt(final int v) {
        if (v < 0) {
            return "0x" + AFSocketAddress.toUnsignedString(v, 16);
        }
        return AFSocketAddress.toUnsignedString(v);
    }
    
    public static AFTIPCSocketAddress of(final URI uri) throws SocketException {
        return of(uri, -1);
    }
    
    public static AFTIPCSocketAddress of(final URI uri, final int overridePort) throws SocketException {
        final String scheme = uri.getScheme();
        switch (scheme) {
            case "tipc":
            case "http+tipc":
            case "https+tipc": {
                String host = uri.getHost();
                if (host == null) {
                    host = uri.getAuthority();
                    if (host != null) {
                        final int at = host.indexOf(64);
                        if (at >= 0) {
                            host = host.substring(at + 1);
                        }
                    }
                }
                if (host == null) {
                    throw new SocketException("Cannot get hostname from URI: " + uri);
                }
                final int port = (overridePort != -1) ? overridePort : uri.getPort();
                if (port != -1) {
                    host = host + ":" + port;
                }
                try {
                    final Matcher m = AFTIPCSocketAddress.PAT_TIPC_URI_HOST_AND_PORT.matcher(host);
                    if (!m.matches()) {
                        throw new SocketException("Invalid TIPC URI: " + uri);
                    }
                    String typeStr = m.group("type");
                    String scopeStr = m.group("scope");
                    if (typeStr == null) {
                        typeStr = m.group("type2");
                        scopeStr = m.group("scope2");
                    }
                    final String strA = m.group("a");
                    final String strB = m.group("b");
                    final String strC = m.group("c");
                    final String javaPortStr = m.group("javaPort");
                    final String s = (typeStr == null) ? "" : typeStr;
                    AddressType addrType = null;
                    switch (s) {
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
                            addrType = AddressType.ofValue(parseUnsignedInt(typeStr));
                            break;
                        }
                    }
                    final String s2 = (scopeStr == null) ? "" : scopeStr;
                    Scope scope = null;
                    switch (s2) {
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
                            scope = Scope.ofValue(parseUnsignedInt(scopeStr));
                            break;
                        }
                    }
                    final int a = parseUnsignedInt(strA);
                    final int b = parseUnsignedInt(strB);
                    int c;
                    if (strC == null || strC.isEmpty()) {
                        if (addrType == AddressType.SERVICE_RANGE) {
                            c = b;
                        }
                        else {
                            c = 0;
                        }
                    }
                    else {
                        c = parseUnsignedInt(strC);
                    }
                    int javaPort = (javaPortStr == null || javaPortStr.isEmpty()) ? port : Integer.parseInt(javaPortStr);
                    if (overridePort != -1) {
                        javaPort = overridePort;
                    }
                    return AFSocketAddress.resolveAddress(toBytes(addrType, scope, a, b, c), javaPort, addressFamily());
                }
                catch (final IllegalArgumentException e) {
                    throw (SocketException)new SocketException("Invalid TIPC URI: " + uri).initCause(e);
                }
                break;
            }
            default: {
                throw new SocketException("Unsupported URI scheme: " + uri.getScheme());
            }
        }
    }
    
    @Override
    public URI toURI(final String scheme, final URI template) throws IOException {
        switch (scheme) {
            case "tipc":
            case "http+tipc":
            case "https+tipc": {
                final byte[] bytes = this.getBytes();
                if (bytes.length != 20) {
                    return super.toURI(scheme, template);
                }
                final ByteBuffer bb = ByteBuffer.wrap(bytes);
                final AddressType addrType = AddressType.ofValue(bb.getInt());
                final Scope scope = Scope.ofValue(bb.getInt());
                final StringBuilder sb = new StringBuilder();
                boolean haveScope = true;
                if (scope == Scope.SCOPE_NOT_SPECIFIED) {
                    sb.append("default-");
                }
                else if (scope == Scope.SCOPE_CLUSTER) {
                    if (addrType == AddressType.SERVICE_ADDR || addrType == AddressType.SERVICE_RANGE) {
                        haveScope = false;
                    }
                    else {
                        sb.append("cluster-");
                    }
                }
                else if (scope == Scope.SCOPE_NODE) {
                    sb.append("node-");
                }
                else {
                    sb.append(this.toTipcInt(scope.value()));
                    sb.append('-');
                }
                boolean addrTypeImplied = false;
                if (addrType == AddressType.SERVICE_ADDR) {
                    if (!haveScope) {
                        addrTypeImplied = true;
                    }
                    else {
                        sb.append("service");
                    }
                }
                else if (addrType == AddressType.SERVICE_RANGE) {
                    sb.append("service-range");
                }
                else if (addrType == AddressType.SOCKET_ADDR) {
                    sb.append("socket");
                }
                else {
                    sb.append(this.toTipcInt(addrType.value()));
                }
                if (!addrTypeImplied) {
                    sb.append('.');
                }
                final int a = bb.getInt();
                final int b = bb.getInt();
                final int c = bb.getInt();
                sb.append(this.toTipcInt(a));
                sb.append('.');
                sb.append(this.toTipcInt(b));
                if (c != 0) {
                    sb.append('.');
                    sb.append(this.toTipcInt(c));
                }
                return new HostAndPort(sb.toString(), this.getPort()).toURI(scheme, template);
            }
            default: {
                return super.toURI(scheme, template);
            }
        }
    }
    
    static {
        PAT_TIPC_URI_HOST_AND_PORT = Pattern.compile("^((?:(?:(?<scope>cluster|node|default|[0-9a-fx]+)\\-)?(?<type>service|service-range|socket)\\.)|(?<scope2>cluster|node|default|[0-9a-fx]+)\\-(?<type2>[0-9a-fx]+)\\.)?(?<a>[0-9a-fx]+)\\.(?<b>[0-9a-fx]+)(?:\\.(?<c>[0-9a-fx]+))?(?:\\:(?<javaPort>[0-9]+))?$");
    }
    
    @NonNullByDefault
    public static final class AddressType extends NamedInteger
    {
        private static final long serialVersionUID = 1L;
        public static final AddressType SERVICE_RANGE;
        public static final AddressType SERVICE_ADDR;
        public static final AddressType SOCKET_ADDR;
        private static final AddressType[] VALUES;
        private final DebugStringProvider ds;
        
        private AddressType(final int id) {
            super(id);
            this.ds = ((a, b, c) -> ":" + AFSocketAddress.toUnsignedString(a) + ":" + AFSocketAddress.toUnsignedString(b) + ":" + AFSocketAddress.toUnsignedString(c));
        }
        
        private AddressType(final String name, final int id, final DebugStringProvider ds) {
            super(name, id);
            this.ds = ds;
        }
        
        static AddressType ofValue(final int v) {
            return NamedInteger.ofValue(AddressType.VALUES, AddressType::new, v);
        }
        
        public static String formatTIPCInt(final int i) {
            return String.format(Locale.ENGLISH, "0x%08x", (long)i & 0xFFFFFFFFL);
        }
        
        private String toDebugString(final Scope scope, final int a, final int b, final int c) {
            if (this == AddressType.SOCKET_ADDR && scope.equals(Scope.SCOPE_NOT_SPECIFIED)) {
                return this.name() + "(" + this.value() + ");" + this.ds.toDebugString(a, b, c);
            }
            return this.name() + "(" + this.value() + ");" + scope + ":" + this.ds.toDebugString(a, b, c);
        }
        
        static {
            VALUES = NamedInteger.init(new AddressType[] { SERVICE_RANGE = new AddressType("SERVICE_RANGE", 1, (a, b, c) -> formatTIPCInt(a) + "@" + formatTIPCInt(b) + "-" + formatTIPCInt(c)), SERVICE_ADDR = new AddressType("SERVICE_ADDR", 2, (a, b, c) -> {
                    new StringBuilder().append(formatTIPCInt(a)).append("@").append(formatTIPCInt(b));
                    String string;
                    if (c == 0) {
                        string = "";
                    }
                    else {
                        string = ":" + formatTIPCInt(c);
                    }
                    final StringBuilder sb;
                    return sb.append(string).toString();
                }), SOCKET_ADDR = new AddressType("SOCKET_ADDR", 3, (a, b, c) -> {
                    new StringBuilder().append(formatTIPCInt(a)).append("@").append(formatTIPCInt(b));
                    String string2;
                    if (c == 0) {
                        string2 = "";
                    }
                    else {
                        string2 = ":" + formatTIPCInt(c);
                    }
                    final StringBuilder sb2;
                    return sb2.append(string2).toString();
                }) });
        }
        
        @FunctionalInterface
        interface DebugStringProvider extends Serializable
        {
            String toDebugString(final int p0, final int p1, final int p2);
        }
    }
    
    @NonNullByDefault
    public static final class Scope extends NamedInteger
    {
        private static final long serialVersionUID = 1L;
        public static final Scope SCOPE_CLUSTER;
        public static final Scope SCOPE_NODE;
        public static final Scope SCOPE_NOT_SPECIFIED;
        private static final Scope[] VALUES;
        
        private Scope(final int id) {
            super(id);
        }
        
        private Scope(final String name, final int id) {
            super(name, id);
        }
        
        public static Scope ofValue(final int v) {
            return NamedInteger.ofValue(Scope.VALUES, Scope::new, v);
        }
        
        static {
            VALUES = NamedInteger.init(new Scope[] { SCOPE_NOT_SPECIFIED = new Scope("SCOPE_NOT_SPECIFIED", 0), SCOPE_CLUSTER = new Scope("SCOPE_CLUSTER", 2), SCOPE_NODE = new Scope("SCOPE_NODE", 3) });
        }
    }
}
