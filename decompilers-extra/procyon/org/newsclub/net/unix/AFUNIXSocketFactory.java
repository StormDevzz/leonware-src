// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.net.URLDecoder;
import java.net.SocketAddress;
import java.util.Objects;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.Socket;

public abstract class AFUNIXSocketFactory extends AFSocketFactory<AFUNIXSocketAddress>
{
    protected AFUNIXSocketFactory() {
    }
    
    @Override
    public Socket createSocket() throws SocketException {
        return AFUNIXSocket.newInstance(this);
    }
    
    @Override
    protected AFUNIXSocket connectTo(final AFUNIXSocketAddress addr) throws IOException {
        return AFUNIXSocket.connectTo(addr);
    }
    
    private abstract static class DefaultSocketHostnameSocketFactory extends AFUNIXSocketFactory
    {
        private static final String PROP_SOCKET_HOSTNAME = "org.newsclub.net.unix.socket.hostname";
        
        public DefaultSocketHostnameSocketFactory() {
        }
        
        @Override
        public final boolean isHostnameSupported(final String host) {
            return getDefaultSocketHostname().equals(host);
        }
        
        private static String getDefaultSocketHostname() {
            return System.getProperty("org.newsclub.net.unix.socket.hostname", "localhost");
        }
    }
    
    public static final class FactoryArg extends DefaultSocketHostnameSocketFactory
    {
        private final File socketFile;
        
        public FactoryArg(final String socketPath) {
            Objects.requireNonNull(socketPath, "Socket path was null");
            this.socketFile = new File(socketPath);
        }
        
        public FactoryArg(final File file) {
            Objects.requireNonNull(file, "File was null");
            this.socketFile = file;
        }
        
        @Override
        public AFUNIXSocketAddress addressFromHost(final String host, final int port) throws SocketException {
            return AFUNIXSocketAddress.of(this.socketFile, port);
        }
    }
    
    public static final class SystemProperty extends DefaultSocketHostnameSocketFactory
    {
        private static final String PROP_SOCKET_DEFAULT = "org.newsclub.net.unix.socket.default";
        
        @Override
        public AFUNIXSocketAddress addressFromHost(final String host, final int port) throws SocketException {
            final String path = System.getProperty("org.newsclub.net.unix.socket.default");
            if (path == null || path.isEmpty()) {
                throw new IllegalStateException("Property not configured: org.newsclub.net.unix.socket.default");
            }
            final File socketFile = new File(path);
            return AFUNIXSocketAddress.of(socketFile, port);
        }
    }
    
    public static final class URIScheme extends AFUNIXSocketFactory
    {
        private static final String FILE_SCHEME_PREFIX = "file://";
        private static final String FILE_SCHEME_PREFIX_ENCODED = "file%";
        private static final String FILE_SCHEME_LOCALHOST = "localhost";
        
        private static String stripBrackets(String host) {
            if (host.startsWith("[")) {
                if (host.endsWith("]")) {
                    host = host.substring(1, host.length() - 1);
                }
                else {
                    host = host.substring(1);
                }
            }
            return host;
        }
        
        @Override
        public boolean isHostnameSupported(String host) {
            host = stripBrackets(host);
            return host.startsWith("file://") || host.startsWith("file%");
        }
        
        @Override
        public AFUNIXSocketAddress addressFromHost(String host, final int port) throws SocketException {
            host = stripBrackets(host);
            if (host.startsWith("file%")) {
                try {
                    host = URLDecoder.decode(host, "UTF-8");
                }
                catch (final Exception e) {
                    throw (SocketException)new SocketException().initCause(e);
                }
            }
            if (!host.startsWith("file://")) {
                throw new SocketException("Unsupported scheme");
            }
            String path = host.substring("file://".length());
            if (path.startsWith("localhost")) {
                path = path.substring("localhost".length());
            }
            if (path.isEmpty()) {
                throw new SocketException("Path is empty");
            }
            if (!path.startsWith("/")) {
                throw new SocketException("Path must be absolute");
            }
            final File socketFile = new File(path);
            return AFUNIXSocketAddress.of(socketFile, port);
        }
    }
}
