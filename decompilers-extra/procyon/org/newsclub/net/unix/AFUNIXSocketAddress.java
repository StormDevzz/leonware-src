// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Set;
import java.net.URISyntaxException;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.net.InetAddress;
import java.util.Objects;
import java.net.SocketAddress;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.io.File;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public final class AFUNIXSocketAddress extends AFSocketAddress
{
    private static final long serialVersionUID = 1L;
    private static final Charset ADDRESS_CHARSET;
    static final AFAddressFamily<AFUNIXSocketAddress> AF_UNIX;
    
    private AFUNIXSocketAddress(final int port, final byte[] socketAddress, final ByteBuffer nativeAddress) throws SocketException {
        super(port, socketAddress, nativeAddress, AFUNIXSocketAddress.AF_UNIX);
    }
    
    @Deprecated
    public AFUNIXSocketAddress(final File socketFile) throws SocketException {
        this(socketFile, 0);
    }
    
    @Deprecated
    public AFUNIXSocketAddress(final File socketFile, final int port) throws SocketException {
        this(port, of(socketFile, port).getPathAsBytes(), of(socketFile, port).getNativeAddressDirectBuffer());
    }
    
    static AFUNIXSocketAddress newAFSocketAddress(final int port, final byte[] socketAddress, final ByteBuffer nativeAddress) throws SocketException {
        return AFSocketAddress.newDeserializedAFSocketAddress(port, socketAddress, nativeAddress, AFUNIXSocketAddress.AF_UNIX, AFUNIXSocketAddress::new);
    }
    
    public static AFUNIXSocketAddress of(final File socketFile) throws SocketException {
        return of(socketFile, 0);
    }
    
    public static AFUNIXSocketAddress of(final File socketFile, final int port) throws SocketException {
        return of(socketFile.getPath().getBytes(AFUNIXSocketAddress.ADDRESS_CHARSET), port);
    }
    
    public static AFUNIXSocketAddress of(final byte[] socketAddress) throws SocketException {
        return of(socketAddress, 0);
    }
    
    public static AFUNIXSocketAddress of(final byte[] socketAddress, final int port) throws SocketException {
        return AFSocketAddress.resolveAddress(socketAddress, port, AFUNIXSocketAddress.AF_UNIX);
    }
    
    public static AFUNIXSocketAddress of(final Path socketPath) throws SocketException {
        return of(socketPath, 0);
    }
    
    public static AFUNIXSocketAddress of(final Path socketPath, final int port) throws SocketException {
        if (!PathUtil.isPathInDefaultFileSystem(socketPath)) {
            throw new SocketException("Path is not in the default file system");
        }
        return of(socketPath.toString().getBytes(AFUNIXSocketAddress.ADDRESS_CHARSET), port);
    }
    
    public static AFUNIXSocketAddress of(final URI u) throws SocketException {
        return of(u, -1);
    }
    
    public static AFUNIXSocketAddress of(final URI u, final int overridePort) throws SocketException {
        final String scheme = u.getScheme();
        switch (scheme) {
            case "file":
            case "unix": {
                String path = u.getPath();
                if (path == null || path.isEmpty()) {
                    final String auth = u.getAuthority();
                    if (auth == null || auth.isEmpty() || u.getRawSchemeSpecificPart().indexOf(64) != -1) {
                        throw new SocketException("Cannot find UNIX socket path component from URI: " + u);
                    }
                    path = auth;
                }
                return of(new File(path), (overridePort != -1) ? overridePort : u.getPort());
            }
            case "http+unix":
            case "https+unix": {
                final HostAndPort hp = HostAndPort.parseFrom(u);
                return of(new File(hp.getHostname()), (overridePort != -1) ? overridePort : hp.getPort());
            }
            default: {
                throw new SocketException("Invalid URI");
            }
        }
    }
    
    public static AFUNIXSocketAddress ofNewTempFile() throws IOException {
        return ofNewTempPath(0);
    }
    
    public static AFUNIXSocketAddress ofNewTempPath(final int port) throws IOException {
        return of(newTempPath(true), port);
    }
    
    public static AFUNIXSocketAddress of(final SocketAddress address) throws IOException {
        final AFUNIXSocketAddress addr = unwrap(Objects.requireNonNull(address));
        if (addr == null) {
            throw new SocketException("Could not convert SocketAddress to AFUNIXSocketAddress");
        }
        return addr;
    }
    
    static File newTempPath(final boolean deleteOnExit) throws IOException {
        final File f = File.createTempFile("jux", ".sock");
        if (deleteOnExit) {
            f.deleteOnExit();
        }
        if (!f.delete() && f.exists()) {
            throw new IOException("Could not delete temporary file that we just created: " + f);
        }
        return f;
    }
    
    public static AFUNIXSocketAddress unwrap(final InetAddress address, final int port) throws SocketException {
        return AFSocketAddress.unwrap(address, port, AFUNIXSocketAddress.AF_UNIX);
    }
    
    public static AFUNIXSocketAddress unwrap(final SocketAddress address) throws SocketException {
        Objects.requireNonNull(address);
        final AFSupplier<AFUNIXSocketAddress> supplier = supportedAddressSupplier(address);
        if (supplier == null) {
            throw new SocketException("Unsupported address");
        }
        return supplier.get();
    }
    
    public static AFUNIXSocketAddress unwrap(final String hostname, final int port) throws SocketException {
        return AFSocketAddress.unwrap(hostname, port, AFUNIXSocketAddress.AF_UNIX);
    }
    
    public static AFUNIXSocketAddress inAbstractNamespace(final String name) throws SocketException {
        return inAbstractNamespace(name, 0);
    }
    
    public static AFUNIXSocketAddress inAbstractNamespace(final String name, final int port) throws SocketException {
        final byte[] bytes = name.getBytes(AFUNIXSocketAddress.ADDRESS_CHARSET);
        final byte[] addr = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, addr, 1, bytes.length);
        return of(addr, port);
    }
    
    private static String prettyPrint(final byte[] data) {
        final int dataLength = data.length;
        if (dataLength == 0) {
            return "";
        }
        final StringBuilder sb = new StringBuilder(dataLength + 16);
        for (final byte c : data) {
            if (c >= 32 && c < 127) {
                sb.append((char)c);
            }
            else {
                sb.append("\\x");
                sb.append(String.format(Locale.ENGLISH, "%02x", c));
            }
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        final int port = this.getPort();
        return this.getClass().getName() + "[" + ((port == 0) ? "" : ("port=" + port + ";")) + "path=" + prettyPrint(this.getBytes()) + "]";
    }
    
    public String getPath() {
        final byte[] bytes = this.getBytes();
        if (bytes.length == 0) {
            return "";
        }
        if (bytes[0] != 0) {
            return new String(bytes, AFUNIXSocketAddress.ADDRESS_CHARSET);
        }
        final byte[] by = bytes.clone();
        for (int i = 0; i < by.length; ++i) {
            final byte b = by[i];
            if (b == 0) {
                by[i] = 64;
            }
            else if (b < 32 || b >= 127) {
                by[i] = 46;
            }
        }
        return new String(by, StandardCharsets.US_ASCII);
    }
    
    public static Charset addressCharset() {
        return AFUNIXSocketAddress.ADDRESS_CHARSET;
    }
    
    public byte[] getPathAsBytes() {
        return this.getBytes().clone();
    }
    
    public boolean isInAbstractNamespace() {
        final byte[] bytes = this.getBytes();
        return bytes.length > 0 && bytes[0] == 0;
    }
    
    @Override
    public boolean hasFilename() {
        final byte[] bytes = this.getBytes();
        return bytes.length > 0 && bytes[0] != 0;
    }
    
    @Override
    public File getFile() throws FileNotFoundException {
        if (this.isInAbstractNamespace()) {
            throw new FileNotFoundException("Socket is in abstract namespace");
        }
        final byte[] bytes = this.getBytes();
        if (bytes.length == 0) {
            throw new FileNotFoundException("No name");
        }
        return new File(new String(bytes, AFUNIXSocketAddress.ADDRESS_CHARSET));
    }
    
    public static boolean isSupportedAddress(final InetAddress addr) {
        return AFInetAddress.isSupportedAddress(addr, AFUNIXSocketAddress.AF_UNIX);
    }
    
    public static boolean isSupportedAddress(final SocketAddress addr) {
        return supportedAddressSupplier(addr) != null;
    }
    
    static AFSupplier<AFUNIXSocketAddress> supportedAddressSupplier(final SocketAddress addr) {
        if (addr == null) {
            return null;
        }
        if (addr instanceof AFUNIXSocketAddress) {
            return (AFSupplier<AFUNIXSocketAddress>)(() -> addr);
        }
        return SocketAddressUtil.supplyAFUNIXSocketAddress(addr);
    }
    
    public static AFAddressFamily<AFUNIXSocketAddress> addressFamily() {
        return AFUNIXSelectorProvider.getInstance().addressFamily();
    }
    
    @Override
    public URI toURI(final String scheme, final URI template) throws IOException {
        switch (scheme) {
            case "unix":
            case "file": {
                try {
                    if (this.getPort() > 0 && !"file".equals(scheme)) {
                        return new URI(scheme, null, "localhost", this.getPort(), this.getPath(), null, null);
                    }
                    return new URI(scheme, null, null, -1, this.getPath(), null, null);
                }
                catch (final URISyntaxException e) {
                    throw new IOException(e);
                }
            }
            case "http+unix":
            case "https+unix": {
                final HostAndPort hp = new HostAndPort(this.getPath(), this.getPort());
                return hp.toURI(scheme, template);
            }
            default: {
                return super.toURI(scheme, template);
            }
        }
    }
    
    @Override
    public AFUNIXSocket newConnectedSocket() throws IOException {
        return (AFUNIXSocket)super.newConnectedSocket();
    }
    
    @Override
    public AFUNIXServerSocket newBoundServerSocket() throws IOException {
        return (AFUNIXServerSocket)super.newBoundServerSocket();
    }
    
    @Override
    public AFUNIXServerSocket newForceBoundServerSocket() throws IOException {
        return (AFUNIXServerSocket)super.newForceBoundServerSocket();
    }
    
    static {
        ADDRESS_CHARSET = Charset.defaultCharset();
        AF_UNIX = AFAddressFamily.registerAddressFamily("un", AFUNIXSocketAddress.class, new AFSocketAddressConfig<AFUNIXSocketAddress>() {
            private final AFSocketAddressConstructor<AFUNIXSocketAddress> addrConstr = AFSocketAddress.isUseDeserializationForInit() ? AFUNIXSocketAddress::newAFSocketAddress : ((x$0, x$1, x$2) -> new AFUNIXSocketAddress(x$0, x$1, x$2, (AFUNIXSocketAddress$1)null));
            
            public AFUNIXSocketAddress parseURI(final URI u, final int port) throws SocketException {
                return AFUNIXSocketAddress.of(u, port);
            }
            
            @Override
            protected AFSocketAddressConstructor<AFUNIXSocketAddress> addressConstructor() {
                return this.addrConstr;
            }
            
            @Override
            protected String selectorProviderClassname() {
                return AFUNIXSelectorProvider.class.getName();
            }
            
            @Override
            protected Set<String> uriSchemes() {
                return new HashSet<String>(Arrays.asList("unix", "http+unix", "https+unix"));
            }
        });
    }
}
