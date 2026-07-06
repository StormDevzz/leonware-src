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
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import org.newsclub.net.unix.AFAddressFamily;
import org.newsclub.net.unix.AFSocketAddress;
import org.newsclub.net.unix.AFSocketAddressConfig;

public final class AFGenericSocketAddress
extends AFSocketAddress {
    private static final long serialVersionUID = 1L;
    private static AFAddressFamily<AFGenericSocketAddress> family;
    private static final String SELECTOR_PROVIDER_CLASS = "org.newsclub.net.unix.generic.AFGenericSelectorProvider";

    private AFGenericSocketAddress(int port, byte[] socketAddress, ByteBuffer nativeAddress) throws SocketException {
        super(port, socketAddress, nativeAddress, AFGenericSocketAddress.addressFamily());
    }

    private static AFGenericSocketAddress newAFSocketAddress(int port, byte[] socketAddress, ByteBuffer nativeAddress) throws SocketException {
        return AFGenericSocketAddress.newDeserializedAFSocketAddress(port, socketAddress, nativeAddress, AFGenericSocketAddress.addressFamily(), AFGenericSocketAddress::new);
    }

    public static AFGenericSocketAddress unwrap(InetAddress address, int port) throws SocketException {
        return AFSocketAddress.unwrap(address, port, AFGenericSocketAddress.addressFamily());
    }

    public static AFGenericSocketAddress unwrap(String hostname, int port) throws SocketException {
        return AFSocketAddress.unwrap(hostname, port, AFGenericSocketAddress.addressFamily());
    }

    public static AFGenericSocketAddress unwrap(SocketAddress address) throws SocketException {
        Objects.requireNonNull(address);
        if (!AFGenericSocketAddress.isSupportedAddress(address)) {
            throw new SocketException("Unsupported address");
        }
        return (AFGenericSocketAddress)address;
    }

    @Override
    public String toString() {
        int port = this.getPort();
        return this.getClass().getName() + "[" + (port == 0 ? "" : "port=" + port + ";") + "bytes=" + Arrays.toString(this.getBytes()) + "]";
    }

    public byte[] toBytes() {
        byte[] bytes = this.getBytes();
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

    public static boolean isSupportedAddress(InetAddress addr) {
        return AFSocketAddress.isSupportedAddress(addr, AFGenericSocketAddress.addressFamily());
    }

    public static boolean isSupportedAddress(SocketAddress addr) {
        return addr instanceof AFGenericSocketAddress;
    }

    public static synchronized AFAddressFamily<AFGenericSocketAddress> addressFamily() {
        if (family == null) {
            family = AFAddressFamily.registerAddressFamily("generic", AFGenericSocketAddress.class, new AFSocketAddressConfig<AFGenericSocketAddress>(){
                private final AFSocketAddress.AFSocketAddressConstructor<AFGenericSocketAddress> addrConstr = AFSocketAddress.isUseDeserializationForInit() ? (x$0, x$1, x$2) -> AFGenericSocketAddress.access$100(x$0, x$1, x$2) : (x$0, x$1, x$2) -> new AFGenericSocketAddress(x$0, x$1, x$2);

                @Override
                protected AFGenericSocketAddress parseURI(URI u, int port) throws SocketException {
                    return AFGenericSocketAddress.of(u, port);
                }

                @Override
                protected AFSocketAddress.AFSocketAddressConstructor<AFGenericSocketAddress> addressConstructor() {
                    return this.addrConstr;
                }

                @Override
                protected String selectorProviderClassname() {
                    return AFGenericSocketAddress.SELECTOR_PROVIDER_CLASS;
                }

                @Override
                protected Set<String> uriSchemes() {
                    return Collections.emptySet();
                }
            });
            try {
                Class.forName(SELECTOR_PROVIDER_CLASS);
            }
            catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
        }
        return family;
    }

    public static AFGenericSocketAddress of(URI uri) throws SocketException {
        return AFGenericSocketAddress.of(uri, -1);
    }

    public static AFGenericSocketAddress of(URI uri, int overridePort) throws SocketException {
        throw new SocketException("Unsupported");
    }

    @Override
    public URI toURI(String scheme, URI template) throws IOException {
        return super.toURI(scheme, template);
    }
}

