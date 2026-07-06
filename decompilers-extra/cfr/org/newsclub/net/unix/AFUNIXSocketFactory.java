/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLDecoder;
import java.util.Objects;
import org.newsclub.net.unix.AFSocketFactory;
import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;

public abstract class AFUNIXSocketFactory
extends AFSocketFactory<AFUNIXSocketAddress> {
    protected AFUNIXSocketFactory() {
    }

    @Override
    public Socket createSocket() throws SocketException {
        return AFUNIXSocket.newInstance(this);
    }

    protected AFUNIXSocket connectTo(AFUNIXSocketAddress addr) throws IOException {
        return AFUNIXSocket.connectTo(addr);
    }

    public static final class URIScheme
    extends AFUNIXSocketFactory {
        private static final String FILE_SCHEME_PREFIX = "file://";
        private static final String FILE_SCHEME_PREFIX_ENCODED = "file%";
        private static final String FILE_SCHEME_LOCALHOST = "localhost";

        private static String stripBrackets(String host) {
            if (host.startsWith("[")) {
                host = host.endsWith("]") ? host.substring(1, host.length() - 1) : host.substring(1);
            }
            return host;
        }

        @Override
        public boolean isHostnameSupported(String host) {
            return (host = URIScheme.stripBrackets(host)).startsWith(FILE_SCHEME_PREFIX) || host.startsWith(FILE_SCHEME_PREFIX_ENCODED);
        }

        @Override
        public AFUNIXSocketAddress addressFromHost(String host, int port) throws SocketException {
            if ((host = URIScheme.stripBrackets(host)).startsWith(FILE_SCHEME_PREFIX_ENCODED)) {
                try {
                    host = URLDecoder.decode(host, "UTF-8");
                }
                catch (Exception e) {
                    throw (SocketException)new SocketException().initCause(e);
                }
            }
            if (!host.startsWith(FILE_SCHEME_PREFIX)) {
                throw new SocketException("Unsupported scheme");
            }
            String path = host.substring(FILE_SCHEME_PREFIX.length());
            if (path.startsWith(FILE_SCHEME_LOCALHOST)) {
                path = path.substring(FILE_SCHEME_LOCALHOST.length());
            }
            if (path.isEmpty()) {
                throw new SocketException("Path is empty");
            }
            if (!path.startsWith("/")) {
                throw new SocketException("Path must be absolute");
            }
            File socketFile = new File(path);
            return AFUNIXSocketAddress.of(socketFile, port);
        }
    }

    public static final class SystemProperty
    extends DefaultSocketHostnameSocketFactory {
        private static final String PROP_SOCKET_DEFAULT = "org.newsclub.net.unix.socket.default";

        @Override
        public AFUNIXSocketAddress addressFromHost(String host, int port) throws SocketException {
            String path = System.getProperty(PROP_SOCKET_DEFAULT);
            if (path == null || path.isEmpty()) {
                throw new IllegalStateException("Property not configured: org.newsclub.net.unix.socket.default");
            }
            File socketFile = new File(path);
            return AFUNIXSocketAddress.of(socketFile, port);
        }
    }

    public static final class FactoryArg
    extends DefaultSocketHostnameSocketFactory {
        private final File socketFile;

        public FactoryArg(String socketPath) {
            Objects.requireNonNull(socketPath, "Socket path was null");
            this.socketFile = new File(socketPath);
        }

        public FactoryArg(File file) {
            Objects.requireNonNull(file, "File was null");
            this.socketFile = file;
        }

        @Override
        public AFUNIXSocketAddress addressFromHost(String host, int port) throws SocketException {
            return AFUNIXSocketAddress.of(this.socketFile, port);
        }
    }

    private static abstract class DefaultSocketHostnameSocketFactory
    extends AFUNIXSocketFactory {
        private static final String PROP_SOCKET_HOSTNAME = "org.newsclub.net.unix.socket.hostname";

        @Override
        public final boolean isHostnameSupported(String host) {
            return DefaultSocketHostnameSocketFactory.getDefaultSocketHostname().equals(host);
        }

        private static String getDefaultSocketHostname() {
            return System.getProperty(PROP_SOCKET_HOSTNAME, "localhost");
        }
    }
}

