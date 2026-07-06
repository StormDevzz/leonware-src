/*
 * Decompiled with CFR 0.152.
 */
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
import org.newsclub.net.unix.AFAddressFamily;
import org.newsclub.net.unix.AFSocketAddress;
import org.newsclub.net.unix.AFSocketAddressConfig;
import org.newsclub.net.unix.HostAndPort;

public final class AFVSOCKSocketAddress
extends AFSocketAddress {
    private static final long serialVersionUID = 1L;
    private static final Pattern PAT_VSOCK_URI_HOST_AND_PORT = Pattern.compile("^(?<port>any|[0-9a-fx\\-]+)(\\.(?<cid>any|hypervisor|local|host|[0-9a-fx\\-]+))?(?:\\:(?<javaPort>[0-9]+))?$");
    private static AFAddressFamily<AFVSOCKSocketAddress> afVsock;
    public static final int VMADDR_CID_ANY = -1;
    public static final int VMADDR_CID_HYPERVISOR = 0;
    public static final int VMADDR_CID_LOCAL = 1;
    public static final int VMADDR_CID_HOST = 2;
    public static final int VMADDR_PORT_ANY = -1;

    private AFVSOCKSocketAddress(int port, byte[] socketAddress, ByteBuffer nativeAddress) throws SocketException {
        super(port, socketAddress, nativeAddress, AFVSOCKSocketAddress.addressFamily());
    }

    private static AFVSOCKSocketAddress newAFSocketAddress(int port, byte[] socketAddress, ByteBuffer nativeAddress) throws SocketException {
        return AFVSOCKSocketAddress.newDeserializedAFSocketAddress(port, socketAddress, nativeAddress, AFVSOCKSocketAddress.addressFamily(), AFVSOCKSocketAddress::new);
    }

    public static AFVSOCKSocketAddress ofPortAndCID(int port, int cid) throws SocketException {
        return AFVSOCKSocketAddress.ofPortAndCID(-1, port, cid);
    }

    public static AFVSOCKSocketAddress ofHypervisorPort(int port) throws SocketException {
        return AFVSOCKSocketAddress.ofPortAndCID(port, 0);
    }

    public static AFVSOCKSocketAddress ofAnyHypervisorPort() throws SocketException {
        return AFVSOCKSocketAddress.ofPortAndCID(-1, 0);
    }

    public static AFVSOCKSocketAddress ofLocalPort(int port) throws SocketException {
        return AFVSOCKSocketAddress.ofPortAndCID(port, 1);
    }

    public static AFVSOCKSocketAddress ofAnyLocalPort() throws SocketException {
        return AFVSOCKSocketAddress.ofPortAndCID(-1, 1);
    }

    public static AFVSOCKSocketAddress ofHostPort(int port) throws SocketException {
        return AFVSOCKSocketAddress.ofPortAndCID(port, 2);
    }

    public static AFVSOCKSocketAddress ofAnyHostPort() throws SocketException {
        return AFVSOCKSocketAddress.ofPortAndCID(-1, 2);
    }

    public static AFVSOCKSocketAddress ofAnyPort() throws SocketException {
        return AFVSOCKSocketAddress.ofPortAndCID(-1, -1);
    }

    public static AFVSOCKSocketAddress ofPortWithAnyCID(int port) throws SocketException {
        return AFVSOCKSocketAddress.ofPortAndCID(port, -1);
    }

    public static AFVSOCKSocketAddress ofPortAndCID(int javaPort, int vsockPort, int cid) throws SocketException {
        return AFVSOCKSocketAddress.resolveAddress(AFVSOCKSocketAddress.toBytes(vsockPort, cid), javaPort, AFVSOCKSocketAddress.addressFamily());
    }

    public static AFVSOCKSocketAddress unwrap(InetAddress address, int port) throws SocketException {
        return AFSocketAddress.unwrap(address, port, AFVSOCKSocketAddress.addressFamily());
    }

    public static AFVSOCKSocketAddress unwrap(String hostname, int port) throws SocketException {
        return AFSocketAddress.unwrap(hostname, port, AFVSOCKSocketAddress.addressFamily());
    }

    public static AFVSOCKSocketAddress unwrap(SocketAddress address) throws SocketException {
        Objects.requireNonNull(address);
        if (!AFVSOCKSocketAddress.isSupportedAddress(address)) {
            throw new SocketException("Unsupported address");
        }
        return (AFVSOCKSocketAddress)address;
    }

    public int getVSOCKPort() {
        ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        int a = bb.getInt(4);
        return a;
    }

    public int getVSOCKCID() {
        ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        int a = bb.getInt(8);
        return a;
    }

    public int getVSOCKReserved1() {
        ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        int a = bb.getInt(0);
        return a;
    }

    @Override
    public String toString() {
        int port = this.getPort();
        byte[] bytes = this.getBytes();
        if (bytes.length != 12) {
            return this.getClass().getName() + "[" + (port == 0 ? "" : "port=" + port) + ";UNKNOWN]";
        }
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        int reserved1 = bb.getInt();
        int vsockPort = bb.getInt();
        int cid = bb.getInt();
        String vsockPortString = vsockPort >= -1 ? Integer.toString(vsockPort) : String.format(Locale.ENGLISH, "0x%08x", vsockPort);
        String typeString = (reserved1 == 0 ? "" : "reserved1=" + reserved1 + ";") + "vsockPort=" + vsockPortString + ";cid=" + cid;
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
        return AFSocketAddress.isSupportedAddress(addr, AFVSOCKSocketAddress.addressFamily());
    }

    public static boolean isSupportedAddress(SocketAddress addr) {
        return addr instanceof AFVSOCKSocketAddress;
    }

    private static byte[] toBytes(int port, int cid) {
        ByteBuffer bb = ByteBuffer.allocate(12);
        bb.putInt(0);
        bb.putInt(port);
        bb.putInt(cid);
        return (byte[])bb.flip().array();
    }

    public static synchronized AFAddressFamily<AFVSOCKSocketAddress> addressFamily() {
        if (afVsock == null) {
            afVsock = AFAddressFamily.registerAddressFamily("vsock", AFVSOCKSocketAddress.class, new AFSocketAddressConfig<AFVSOCKSocketAddress>(){
                private final AFSocketAddress.AFSocketAddressConstructor<AFVSOCKSocketAddress> addrConstr = AFSocketAddress.isUseDeserializationForInit() ? (x$0, x$1, x$2) -> AFVSOCKSocketAddress.access$100(x$0, x$1, x$2) : (x$0, x$1, x$2) -> new AFVSOCKSocketAddress(x$0, x$1, x$2);

                @Override
                protected AFVSOCKSocketAddress parseURI(URI u, int port) throws SocketException {
                    return AFVSOCKSocketAddress.of(u, port);
                }

                @Override
                protected AFSocketAddress.AFSocketAddressConstructor<AFVSOCKSocketAddress> addressConstructor() {
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
            catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
        }
        return afVsock;
    }

    public static AFVSOCKSocketAddress of(URI uri) throws SocketException {
        return AFVSOCKSocketAddress.of(uri, -1);
    }

    public static AFVSOCKSocketAddress of(URI uri, int overridePort) throws SocketException {
        int at;
        switch (uri.getScheme()) {
            case "vsock": 
            case "http+vsock": 
            case "https+vsock": {
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
        try {
            int javaPort;
            int port;
            int cid;
            Matcher m = PAT_VSOCK_URI_HOST_AND_PORT.matcher(host);
            if (!m.matches()) {
                throw new SocketException("Invalid VSOCK URI: " + uri);
            }
            String cidStr = m.group("cid");
            String portStr = m.group("port");
            String javaPortStr = m.group("javaPort");
            switch (cidStr == null ? "" : cidStr) {
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
                    cid = AFVSOCKSocketAddress.parseInt(cidStr);
                }
            }
            switch (portStr == null ? "" : portStr) {
                case "any": 
                case "": {
                    port = -1;
                    break;
                }
                default: {
                    port = AFVSOCKSocketAddress.parseInt(portStr);
                }
            }
            int n = javaPort = overridePort != -1 ? overridePort : uri.getPort();
            if (javaPortStr != null && !javaPortStr.isEmpty()) {
                javaPort = AFVSOCKSocketAddress.parseInt(javaPortStr);
            }
            return AFVSOCKSocketAddress.ofPortAndCID(javaPort, port, cid);
        }
        catch (IllegalArgumentException e) {
            throw (SocketException)new SocketException("Invalid VSOCK URI: " + uri).initCause(e);
        }
    }

    @Override
    public URI toURI(String scheme, URI template) throws IOException {
        String cidStr;
        String portStr;
        switch (scheme) {
            case "vsock": 
            case "http+vsock": 
            case "https+vsock": {
                break;
            }
            default: {
                return super.toURI(scheme, template);
            }
        }
        byte[] bytes = this.getBytes();
        if (bytes.length != 12) {
            return super.toURI(scheme, template);
        }
        StringBuilder sb = new StringBuilder();
        int port = this.getVSOCKPort();
        switch (port) {
            case -1: {
                portStr = "any";
                break;
            }
            default: {
                portStr = AFVSOCKSocketAddress.toUnsignedString(port);
            }
        }
        sb.append(portStr);
        sb.append('.');
        int cid = this.getVSOCKCID();
        switch (cid) {
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
                cidStr = AFVSOCKSocketAddress.toUnsignedString(cid);
            }
        }
        sb.append(cidStr);
        return new HostAndPort(sb.toString(), this.getPort()).toURI(scheme, template);
    }

    private static int parseInt(String v) {
        if (v.startsWith("0x")) {
            return AFVSOCKSocketAddress.parseUnsignedInt(v.substring(2), 16);
        }
        if (v.startsWith("-")) {
            return Integer.parseInt(v);
        }
        return AFVSOCKSocketAddress.parseUnsignedInt(v, 10);
    }

    @Override
    public boolean covers(AFSocketAddress covered) {
        if (super.covers(covered)) {
            return true;
        }
        if (covered instanceof AFVSOCKSocketAddress) {
            AFVSOCKSocketAddress other = (AFVSOCKSocketAddress)covered;
            if (this.getVSOCKCID() == -1) {
                if (this.getVSOCKPort() == -1) {
                    return true;
                }
                return this.getVSOCKPort() == other.getVSOCKPort();
            }
            if (this.getVSOCKPort() == -1) {
                return this.getVSOCKCID() == other.getVSOCKCID();
            }
        }
        return this.equals(covered);
    }
}

