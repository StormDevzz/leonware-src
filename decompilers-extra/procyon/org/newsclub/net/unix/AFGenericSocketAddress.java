// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.net.URI;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.net.SocketAddress;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public final class AFGenericSocketAddress extends AFSocketAddress
{
    private static final long serialVersionUID = 1L;
    private static AFAddressFamily<AFGenericSocketAddress> family;
    private static final String SELECTOR_PROVIDER_CLASS = "org.newsclub.net.unix.generic.AFGenericSelectorProvider";
    
    private AFGenericSocketAddress(final int port, final byte[] socketAddress, final ByteBuffer nativeAddress) throws SocketException {
        super(port, socketAddress, nativeAddress, addressFamily());
    }
    
    private static AFGenericSocketAddress newAFSocketAddress(final int port, final byte[] socketAddress, final ByteBuffer nativeAddress) throws SocketException {
        return AFSocketAddress.newDeserializedAFSocketAddress(port, socketAddress, nativeAddress, addressFamily(), AFGenericSocketAddress::new);
    }
    
    public static AFGenericSocketAddress unwrap(final InetAddress address, final int port) throws SocketException {
        return AFSocketAddress.unwrap(address, port, addressFamily());
    }
    
    public static AFGenericSocketAddress unwrap(final String hostname, final int port) throws SocketException {
        return AFSocketAddress.unwrap(hostname, port, addressFamily());
    }
    
    public static AFGenericSocketAddress unwrap(final SocketAddress address) throws SocketException {
        Objects.requireNonNull(address);
        if (!isSupportedAddress(address)) {
            throw new SocketException("Unsupported address");
        }
        return (AFGenericSocketAddress)address;
    }
    
    @Override
    public String toString() {
        final int port = this.getPort();
        return this.getClass().getName() + "[" + ((port == 0) ? "" : ("port=" + port + ";")) + "bytes=" + Arrays.toString(this.getBytes()) + "]";
    }
    
    public byte[] toBytes() {
        final byte[] bytes = this.getBytes();
        return Arrays.copyOf(bytes, bytes.length);
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
        return addr instanceof AFGenericSocketAddress;
    }
    
    public static synchronized AFAddressFamily<AFGenericSocketAddress> addressFamily() {
        if (AFGenericSocketAddress.family == null) {
            AFGenericSocketAddress.family = AFAddressFamily.registerAddressFamily("generic", AFGenericSocketAddress.class, new AFSocketAddressConfig<AFGenericSocketAddress>() {
                private final AFSocketAddressConstructor<AFGenericSocketAddress> addrConstr = AFSocketAddress.isUseDeserializationForInit() ? ((x$0, x$1, x$2) -> newAFSocketAddress(x$0, x$1, x$2)) : ((x$0, x$1, x$2) -> new AFGenericSocketAddress(x$0, x$1, x$2, (AFGenericSocketAddress$1)null));
                
                @Override
                protected AFGenericSocketAddress parseURI(final URI u, final int port) throws SocketException {
                    return AFGenericSocketAddress.of(u, port);
                }
                
                @Override
                protected AFSocketAddressConstructor<AFGenericSocketAddress> addressConstructor() {
                    return this.addrConstr;
                }
                
                @Override
                protected String selectorProviderClassname() {
                    return "org.newsclub.net.unix.generic.AFGenericSelectorProvider";
                }
                
                @Override
                protected Set<String> uriSchemes() {
                    return Collections.emptySet();
                }
            });
            try {
                Class.forName("org.newsclub.net.unix.generic.AFGenericSelectorProvider");
            }
            catch (final ClassNotFoundException ex) {}
        }
        return AFGenericSocketAddress.family;
    }
    
    public static AFGenericSocketAddress of(final URI uri) throws SocketException {
        return of(uri, -1);
    }
    
    public static AFGenericSocketAddress of(final URI uri, final int overridePort) throws SocketException {
        throw new SocketException("Unsupported");
    }
    
    @Override
    public URI toURI(final String scheme, final URI template) throws IOException {
        return super.toURI(scheme, template);
    }
}
