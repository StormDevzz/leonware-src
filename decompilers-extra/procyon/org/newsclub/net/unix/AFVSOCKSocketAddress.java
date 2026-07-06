// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Set;
import java.net.URI;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Locale;
import java.util.Objects;
import java.net.SocketAddress;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.regex.Pattern;

public final class AFVSOCKSocketAddress extends AFSocketAddress
{
    private static final long serialVersionUID = 1L;
    private static final Pattern PAT_VSOCK_URI_HOST_AND_PORT;
    private static AFAddressFamily<AFVSOCKSocketAddress> afVsock;
    public static final int VMADDR_CID_ANY = -1;
    public static final int VMADDR_CID_HYPERVISOR = 0;
    public static final int VMADDR_CID_LOCAL = 1;
    public static final int VMADDR_CID_HOST = 2;
    public static final int VMADDR_PORT_ANY = -1;
    
    private AFVSOCKSocketAddress(final int port, final byte[] socketAddress, final ByteBuffer nativeAddress) throws SocketException {
        super(port, socketAddress, nativeAddress, addressFamily());
    }
    
    private static AFVSOCKSocketAddress newAFSocketAddress(final int port, final byte[] socketAddress, final ByteBuffer nativeAddress) throws SocketException {
        return AFSocketAddress.newDeserializedAFSocketAddress(port, socketAddress, nativeAddress, addressFamily(), AFVSOCKSocketAddress::new);
    }
    
    public static AFVSOCKSocketAddress ofPortAndCID(final int port, final int cid) throws SocketException {
        return ofPortAndCID(-1, port, cid);
    }
    
    public static AFVSOCKSocketAddress ofHypervisorPort(final int port) throws SocketException {
        return ofPortAndCID(port, 0);
    }
    
    public static AFVSOCKSocketAddress ofAnyHypervisorPort() throws SocketException {
        return ofPortAndCID(-1, 0);
    }
    
    public static AFVSOCKSocketAddress ofLocalPort(final int port) throws SocketException {
        return ofPortAndCID(port, 1);
    }
    
    public static AFVSOCKSocketAddress ofAnyLocalPort() throws SocketException {
        return ofPortAndCID(-1, 1);
    }
    
    public static AFVSOCKSocketAddress ofHostPort(final int port) throws SocketException {
        return ofPortAndCID(port, 2);
    }
    
    public static AFVSOCKSocketAddress ofAnyHostPort() throws SocketException {
        return ofPortAndCID(-1, 2);
    }
    
    public static AFVSOCKSocketAddress ofAnyPort() throws SocketException {
        return ofPortAndCID(-1, -1);
    }
    
    public static AFVSOCKSocketAddress ofPortWithAnyCID(final int port) throws SocketException {
        return ofPortAndCID(port, -1);
    }
    
    public static AFVSOCKSocketAddress ofPortAndCID(final int javaPort, final int vsockPort, final int cid) throws SocketException {
        return AFSocketAddress.resolveAddress(toBytes(vsockPort, cid), javaPort, addressFamily());
    }
    
    public static AFVSOCKSocketAddress unwrap(final InetAddress address, final int port) throws SocketException {
        return AFSocketAddress.unwrap(address, port, addressFamily());
    }
    
    public static AFVSOCKSocketAddress unwrap(final String hostname, final int port) throws SocketException {
        return AFSocketAddress.unwrap(hostname, port, addressFamily());
    }
    
    public static AFVSOCKSocketAddress unwrap(final SocketAddress address) throws SocketException {
        Objects.requireNonNull(address);
        if (!isSupportedAddress(address)) {
            throw new SocketException("Unsupported address");
        }
        return (AFVSOCKSocketAddress)address;
    }
    
    public int getVSOCKPort() {
        final ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        final int a = bb.getInt(4);
        return a;
    }
    
    public int getVSOCKCID() {
        final ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        final int a = bb.getInt(8);
        return a;
    }
    
    public int getVSOCKReserved1() {
        final ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        final int a = bb.getInt(0);
        return a;
    }
    
    @Override
    public String toString() {
        final int port = this.getPort();
        final byte[] bytes = this.getBytes();
        if (bytes.length != 12) {
            return this.getClass().getName() + "[" + ((port == 0) ? "" : ("port=" + port)) + ";UNKNOWN]";
        }
        final ByteBuffer bb = ByteBuffer.wrap(bytes);
        final int reserved1 = bb.getInt();
        final int vsockPort = bb.getInt();
        final int cid = bb.getInt();
        String vsockPortString;
        if (vsockPort >= -1) {
            vsockPortString = Integer.toString(vsockPort);
        }
        else {
            vsockPortString = String.format(Locale.ENGLISH, "0x%08x", vsockPort);
        }
        final String typeString = ((reserved1 == 0) ? "" : ("reserved1=" + reserved1 + ";")) + "vsockPort=" + vsockPortString + ";cid=" + cid;
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
        return addr instanceof AFVSOCKSocketAddress;
    }
    
    private static byte[] toBytes(final int port, final int cid) {
        final ByteBuffer bb = ByteBuffer.allocate(12);
        bb.putInt(0);
        bb.putInt(port);
        bb.putInt(cid);
        return (byte[])bb.flip().array();
    }
    
    public static synchronized AFAddressFamily<AFVSOCKSocketAddress> addressFamily() {
        if (AFVSOCKSocketAddress.afVsock == null) {
            AFVSOCKSocketAddress.afVsock = AFAddressFamily.registerAddressFamily("vsock", AFVSOCKSocketAddress.class, new AFSocketAddressConfig<AFVSOCKSocketAddress>() {
                private final AFSocketAddressConstructor<AFVSOCKSocketAddress> addrConstr = AFSocketAddress.isUseDeserializationForInit() ? ((x$0, x$1, x$2) -> newAFSocketAddress(x$0, x$1, x$2)) : ((x$0, x$1, x$2) -> new AFVSOCKSocketAddress(x$0, x$1, x$2, (AFVSOCKSocketAddress$1)null));
                
                @Override
                protected AFVSOCKSocketAddress parseURI(final URI u, final int port) throws SocketException {
                    return AFVSOCKSocketAddress.of(u, port);
                }
                
                @Override
                protected AFSocketAddressConstructor<AFVSOCKSocketAddress> addressConstructor() {
                    return this.addrConstr;
                }
                
                @Override
                protected String selectorProviderClassname() {
                    return "org.newsclub.net.unix.vsock.AFVSOCKSelectorProvider";
                }
                
                @Override
                protected Set<String> uriSchemes() {
                    return new HashSet<String>(Arrays.asList("vsock", "http+vsock", "https+vsock"));
                }
            });
            try {
                Class.forName("org.newsclub.net.unix.vsock.AFVSOCKSelectorProvider");
            }
            catch (final ClassNotFoundException ex) {}
        }
        return AFVSOCKSocketAddress.afVsock;
    }
    
    public static AFVSOCKSocketAddress of(final URI uri) throws SocketException {
        return of(uri, -1);
    }
    
    public static AFVSOCKSocketAddress of(final URI uri, final int overridePort) throws SocketException {
        final String scheme = uri.getScheme();
        switch (scheme) {
            case "vsock":
            case "http+vsock":
            case "https+vsock": {
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
                try {
                    final Matcher m = AFVSOCKSocketAddress.PAT_VSOCK_URI_HOST_AND_PORT.matcher(host);
                    if (!m.matches()) {
                        throw new SocketException("Invalid VSOCK URI: " + uri);
                    }
                    final String cidStr = m.group("cid");
                    final String portStr = m.group("port");
                    final String javaPortStr = m.group("javaPort");
                    final String s = (cidStr == null) ? "" : cidStr;
                    int cid = 0;
                    switch (s) {
                        case "":
                        case "any": {
                            cid = -1;
                            break;
                        }
                        case "hypervisor": {
                            cid = 0;
                            break;
                        }
                        case "local": {
                            cid = 1;
                            break;
                        }
                        case "host": {
                            cid = 2;
                            break;
                        }
                        default: {
                            cid = parseInt(cidStr);
                            break;
                        }
                    }
                    final String s2 = (portStr == null) ? "" : portStr;
                    int port = 0;
                    switch (s2) {
                        case "any":
                        case "": {
                            port = -1;
                            break;
                        }
                        default: {
                            port = parseInt(portStr);
                            break;
                        }
                    }
                    int javaPort = (overridePort != -1) ? overridePort : uri.getPort();
                    if (javaPortStr != null && !javaPortStr.isEmpty()) {
                        javaPort = parseInt(javaPortStr);
                    }
                    return ofPortAndCID(javaPort, port, cid);
                }
                catch (final IllegalArgumentException e) {
                    throw (SocketException)new SocketException("Invalid VSOCK URI: " + uri).initCause(e);
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
            case "vsock":
            case "http+vsock":
            case "https+vsock": {
                final byte[] bytes = this.getBytes();
                if (bytes.length != 12) {
                    return super.toURI(scheme, template);
                }
                final StringBuilder sb = new StringBuilder();
                final int port;
                String portStr = null;
                switch (port = this.getVSOCKPort()) {
                    case -1: {
                        portStr = "any";
                        break;
                    }
                    default: {
                        portStr = AFSocketAddress.toUnsignedString(port);
                        break;
                    }
                }
                sb.append(portStr);
                sb.append('.');
                final int cid;
                String cidStr = null;
                switch (cid = this.getVSOCKCID()) {
                    case -1: {
                        cidStr = "any";
                        break;
                    }
                    case 0: {
                        cidStr = "hypervisor";
                        break;
                    }
                    case 1: {
                        cidStr = "local";
                        break;
                    }
                    case 2: {
                        cidStr = "host";
                        break;
                    }
                    default: {
                        cidStr = AFSocketAddress.toUnsignedString(cid);
                        break;
                    }
                }
                sb.append(cidStr);
                return new HostAndPort(sb.toString(), this.getPort()).toURI(scheme, template);
            }
            default: {
                return super.toURI(scheme, template);
            }
        }
    }
    
    private static int parseInt(final String v) {
        if (v.startsWith("0x")) {
            return AFSocketAddress.parseUnsignedInt(v.substring(2), 16);
        }
        if (v.startsWith("-")) {
            return Integer.parseInt(v);
        }
        return AFSocketAddress.parseUnsignedInt(v, 10);
    }
    
    @Override
    public boolean covers(final AFSocketAddress covered) {
        if (super.covers(covered)) {
            return true;
        }
        if (covered instanceof AFVSOCKSocketAddress) {
            final AFVSOCKSocketAddress other = (AFVSOCKSocketAddress)covered;
            if (this.getVSOCKCID() == -1) {
                return this.getVSOCKPort() == -1 || this.getVSOCKPort() == other.getVSOCKPort();
            }
            if (this.getVSOCKPort() == -1) {
                return this.getVSOCKCID() == other.getVSOCKCID();
            }
        }
        return this.equals(covered);
    }
    
    static {
        PAT_VSOCK_URI_HOST_AND_PORT = Pattern.compile("^(?<port>any|[0-9a-fx\\-]+)(\\.(?<cid>any|hypervisor|local|host|[0-9a-fx\\-]+))?(?:\\:(?<javaPort>[0-9]+))?$");
    }
}
