// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import org.eclipse.jdt.annotation.NonNullByDefault;
import java.io.IOException;
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

public final class AFSYSTEMSocketAddress extends AFSocketAddress
{
    private static final long serialVersionUID = 1L;
    private static AFAddressFamily<AFSYSTEMSocketAddress> afSystem;
    private static final String SELECTOR_PROVIDER_CLASS = "org.newsclub.net.unix.darwin.system.AFSYSTEMSelectorProvider";
    
    private AFSYSTEMSocketAddress(final int port, final byte[] socketAddress, final ByteBuffer nativeAddress) throws SocketException {
        super(port, socketAddress, nativeAddress, addressFamily());
    }
    
    private static AFSYSTEMSocketAddress newAFSocketAddress(final int port, final byte[] socketAddress, final ByteBuffer nativeAddress) throws SocketException {
        return AFSocketAddress.newDeserializedAFSocketAddress(port, socketAddress, nativeAddress, addressFamily(), AFSYSTEMSocketAddress::new);
    }
    
    public static AFSYSTEMSocketAddress ofSysAddrIdUnit(final int javaPort, final SysAddr sysAddr, final int id, final int unit) throws SocketException {
        return AFSocketAddress.resolveAddress(toBytes(sysAddr, id, unit), javaPort, addressFamily());
    }
    
    public static AFSYSTEMSocketAddress ofSysAddrIdUnit(final SysAddr sysAddr, final int id, final int unit) throws SocketException {
        return ofSysAddrIdUnit(0, sysAddr, id, unit);
    }
    
    public static AFSYSTEMSocketAddress unwrap(final InetAddress address, final int port) throws SocketException {
        return AFSocketAddress.unwrap(address, port, addressFamily());
    }
    
    public static AFSYSTEMSocketAddress unwrap(final String hostname, final int port) throws SocketException {
        return AFSocketAddress.unwrap(hostname, port, addressFamily());
    }
    
    public static AFSYSTEMSocketAddress unwrap(final SocketAddress address) throws SocketException {
        Objects.requireNonNull(address);
        if (!isSupportedAddress(address)) {
            throw new SocketException("Unsupported address");
        }
        return (AFSYSTEMSocketAddress)address;
    }
    
    @Override
    public String toString() {
        final int port = this.getPort();
        final byte[] bytes = this.getBytes();
        if (bytes.length != 32) {
            return this.getClass().getName() + "[" + ((port == 0) ? "" : ("port=" + port)) + ";UNKNOWN]";
        }
        final ByteBuffer bb = ByteBuffer.wrap(bytes);
        final SysAddr sysAddr = SysAddr.ofValue(bb.getInt());
        final int id = bb.getInt();
        final int unit = bb.getInt();
        return this.getClass().getName() + "[" + ((port == 0) ? "" : ("port=" + port + ";")) + sysAddr + ";id=" + id + ";unit=" + unit + "]";
    }
    
    public SysAddr getSysAddr() {
        final ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        return SysAddr.ofValue(bb.getInt(0));
    }
    
    public int getId() {
        final ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        return bb.getInt(4);
    }
    
    public int getUnit() {
        final ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        return bb.getInt(8);
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
        return addr instanceof AFSYSTEMSocketAddress;
    }
    
    private static byte[] toBytes(final SysAddr sysAddr, final int id, final int unit) {
        final ByteBuffer bb = ByteBuffer.allocate(32);
        bb.putInt(sysAddr.value());
        bb.putInt(id);
        bb.putInt(unit);
        bb.putInt(0);
        bb.putInt(0);
        bb.putInt(0);
        bb.putInt(0);
        bb.putInt(0);
        return (byte[])bb.flip().array();
    }
    
    public static synchronized AFAddressFamily<AFSYSTEMSocketAddress> addressFamily() {
        if (AFSYSTEMSocketAddress.afSystem == null) {
            AFSYSTEMSocketAddress.afSystem = AFAddressFamily.registerAddressFamily("system", AFSYSTEMSocketAddress.class, new AFSocketAddressConfig<AFSYSTEMSocketAddress>() {
                private final AFSocketAddressConstructor<AFSYSTEMSocketAddress> addrConstr = AFSocketAddress.isUseDeserializationForInit() ? ((x$0, x$1, x$2) -> newAFSocketAddress(x$0, x$1, x$2)) : ((x$0, x$1, x$2) -> new AFSYSTEMSocketAddress(x$0, x$1, x$2, (AFSYSTEMSocketAddress$1)null));
                
                @Override
                protected AFSYSTEMSocketAddress parseURI(final URI u, final int port) throws SocketException {
                    return AFSYSTEMSocketAddress.of(u, port);
                }
                
                @Override
                protected AFSocketAddressConstructor<AFSYSTEMSocketAddress> addressConstructor() {
                    return this.addrConstr;
                }
                
                @Override
                protected String selectorProviderClassname() {
                    return "org.newsclub.net.unix.darwin.system.AFSYSTEMSelectorProvider";
                }
                
                @Override
                protected Set<String> uriSchemes() {
                    return new HashSet<String>(Arrays.asList("afsystem"));
                }
            });
            try {
                Class.forName("org.newsclub.net.unix.darwin.system.AFSYSTEMSelectorProvider");
            }
            catch (final ClassNotFoundException ex) {}
        }
        return AFSYSTEMSocketAddress.afSystem;
    }
    
    public static AFSYSTEMSocketAddress of(final URI uri) throws SocketException {
        return of(uri, -1);
    }
    
    public static AFSYSTEMSocketAddress of(final URI uri, final int overridePort) throws SocketException {
        final String scheme = uri.getScheme();
        switch (scheme) {
            case "afsystem": {
                String host;
                if ((host = uri.getHost()) == null || host.isEmpty()) {
                    String ssp = uri.getSchemeSpecificPart();
                    if (ssp == null || !ssp.startsWith("//")) {
                        throw new SocketException("Unsupported URI: " + uri);
                    }
                    ssp = ssp.substring(2);
                    final int i = ssp.indexOf(47);
                    host = ((i == -1) ? ssp : ssp.substring(0, i));
                    if (host.isEmpty()) {
                        throw new SocketException("Unsupported URI: " + uri);
                    }
                }
                final ByteBuffer bb = ByteBuffer.allocate(32);
                for (final String p : host.split("\\.")) {
                    int v;
                    try {
                        v = AFSocketAddress.parseUnsignedInt(p, 10);
                    }
                    catch (final NumberFormatException e) {
                        throw (SocketException)new SocketException("Unsupported URI: " + uri).initCause(e);
                    }
                    bb.putInt(v);
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
                return AFSocketAddress.resolveAddress(bb.array(), uri.getPort(), addressFamily());
            }
            default: {
                throw new SocketException("Unsupported URI scheme: " + uri.getScheme());
            }
        }
    }
    
    @Override
    public URI toURI(final String scheme, final URI template) throws IOException {
        switch (scheme) {
            case "afsystem": {
                final ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
                final StringBuilder sb = new StringBuilder();
                while (bb.remaining() > 0) {
                    sb.append(AFSocketAddress.toUnsignedString(bb.getInt()));
                    if (bb.remaining() > 0) {
                        sb.append('.');
                    }
                }
                return new HostAndPort(sb.toString(), this.getPort()).toURI(scheme, template);
            }
            default: {
                return super.toURI(scheme, template);
            }
        }
    }
    
    @NonNullByDefault
    public static final class SysAddr extends NamedInteger
    {
        private static final long serialVersionUID = 1L;
        public static final SysAddr AF_SYS_CONTROL;
        private static final SysAddr[] VALUES;
        
        private SysAddr(final int id) {
            super(id);
        }
        
        private SysAddr(final String name, final int id) {
            super(name, id);
        }
        
        public static SysAddr ofValue(final int v) {
            return NamedInteger.ofValue(SysAddr.VALUES, SysAddr::new, v);
        }
        
        static {
            VALUES = NamedInteger.init(new SysAddr[] { AF_SYS_CONTROL = new SysAddr("AF_SYS_CONTROL", 2) });
        }
    }
}
