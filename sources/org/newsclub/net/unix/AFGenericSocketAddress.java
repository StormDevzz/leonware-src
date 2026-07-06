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
import org.newsclub.net.unix.AFSocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFGenericSocketAddress.class */
public final class AFGenericSocketAddress extends AFSocketAddress {
    private static final long serialVersionUID = 1;
    private static AFAddressFamily<AFGenericSocketAddress> family;
    private static final String SELECTOR_PROVIDER_CLASS = "org.newsclub.net.unix.generic.AFGenericSelectorProvider";

    private AFGenericSocketAddress(int port, byte[] socketAddress, ByteBuffer nativeAddress) throws SocketException {
        super(port, socketAddress, nativeAddress, addressFamily());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static AFGenericSocketAddress newAFSocketAddress(int port, byte[] socketAddress, ByteBuffer nativeAddress) throws SocketException {
        return (AFGenericSocketAddress) newDeserializedAFSocketAddress(port, socketAddress, nativeAddress, addressFamily(), AFGenericSocketAddress::new);
    }

    public static AFGenericSocketAddress unwrap(InetAddress address, int port) throws SocketException {
        return (AFGenericSocketAddress) AFSocketAddress.unwrap(address, port, addressFamily());
    }

    public static AFGenericSocketAddress unwrap(String hostname, int port) throws SocketException {
        return (AFGenericSocketAddress) AFSocketAddress.unwrap(hostname, port, addressFamily());
    }

    public static AFGenericSocketAddress unwrap(SocketAddress address) throws SocketException {
        Objects.requireNonNull(address);
        if (!isSupportedAddress(address)) {
            throw new SocketException("Unsupported address");
        }
        return (AFGenericSocketAddress) address;
    }

    @Override // java.net.InetSocketAddress
    public String toString() {
        int port = getPort();
        return getClass().getName() + "[" + (port == 0 ? "" : "port=" + port + ";") + "bytes=" + Arrays.toString(getBytes()) + "]";
    }

    public byte[] toBytes() {
        byte[] bytes = getBytes();
        return Arrays.copyOf(bytes, bytes.length);
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
        return addr instanceof AFGenericSocketAddress;
    }

    public static synchronized AFAddressFamily<AFGenericSocketAddress> addressFamily() {
        if (family == null) {
            family = AFAddressFamily.registerAddressFamily("generic", AFGenericSocketAddress.class, new AFSocketAddressConfig<AFGenericSocketAddress>() { // from class: org.newsclub.net.unix.AFGenericSocketAddress.1
                private final AFSocketAddress.AFSocketAddressConstructor<AFGenericSocketAddress> addrConstr;

                {
                    this.addrConstr = AFSocketAddress.isUseDeserializationForInit() ? (x$0, x$1, x$2) -> {
                        return AFGenericSocketAddress.newAFSocketAddress(x$0, x$1, x$2);
                    } : (x$02, x$12, x$22) -> {
                        return new AFGenericSocketAddress(x$02, x$12, x$22);
                    };
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.newsclub.net.unix.AFSocketAddressConfig
                public AFGenericSocketAddress parseURI(URI u, int port) throws SocketException {
                    return AFGenericSocketAddress.of(u, port);
                }

                @Override // org.newsclub.net.unix.AFSocketAddressConfig
                protected AFSocketAddress.AFSocketAddressConstructor<AFGenericSocketAddress> addressConstructor() {
                    return this.addrConstr;
                }

                @Override // org.newsclub.net.unix.AFSocketAddressConfig
                protected String selectorProviderClassname() {
                    return AFGenericSocketAddress.SELECTOR_PROVIDER_CLASS;
                }

                @Override // org.newsclub.net.unix.AFSocketAddressConfig
                protected Set<String> uriSchemes() {
                    return Collections.emptySet();
                }
            });
            try {
                Class.forName(SELECTOR_PROVIDER_CLASS);
            } catch (ClassNotFoundException e) {
            }
        }
        return family;
    }

    public static AFGenericSocketAddress of(URI uri) throws SocketException {
        return of(uri, -1);
    }

    public static AFGenericSocketAddress of(URI uri, int overridePort) throws SocketException {
        throw new SocketException("Unsupported");
    }

    @Override // org.newsclub.net.unix.AFSocketAddress
    public URI toURI(String scheme, URI template) throws IOException {
        return super.toURI(scheme, template);
    }
}
