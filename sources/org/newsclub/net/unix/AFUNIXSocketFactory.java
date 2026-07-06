package org.newsclub.net.unix;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLDecoder;
import java.util.Objects;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFUNIXSocketFactory.class */
public abstract class AFUNIXSocketFactory extends AFSocketFactory<AFUNIXSocketAddress> {
    protected AFUNIXSocketFactory() {
    }

    @Override // org.newsclub.net.unix.AFSocketFactory, javax.net.SocketFactory
    public Socket createSocket() throws SocketException {
        return AFUNIXSocket.newInstance(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.newsclub.net.unix.AFSocketFactory
    public AFUNIXSocket connectTo(AFUNIXSocketAddress addr) throws IOException {
        return AFUNIXSocket.connectTo(addr);
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFUNIXSocketFactory$DefaultSocketHostnameSocketFactory.class */
    private static abstract class DefaultSocketHostnameSocketFactory extends AFUNIXSocketFactory {
        private static final String PROP_SOCKET_HOSTNAME = "org.newsclub.net.unix.socket.hostname";

        @Override // org.newsclub.net.unix.AFUNIXSocketFactory, org.newsclub.net.unix.AFSocketFactory
        protected /* bridge */ /* synthetic */ Socket connectTo(AFSocketAddress aFSocketAddress) throws IOException {
            return super.connectTo((AFUNIXSocketAddress) aFSocketAddress);
        }

        @Override // org.newsclub.net.unix.AFSocketAddressFromHostname
        public final boolean isHostnameSupported(String host) {
            return getDefaultSocketHostname().equals(host);
        }

        private static String getDefaultSocketHostname() {
            return System.getProperty(PROP_SOCKET_HOSTNAME, "localhost");
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFUNIXSocketFactory$FactoryArg.class */
    public static final class FactoryArg extends DefaultSocketHostnameSocketFactory {
        private final File socketFile;

        public FactoryArg(String socketPath) {
            Objects.requireNonNull(socketPath, "Socket path was null");
            this.socketFile = new File(socketPath);
        }

        public FactoryArg(File file) {
            Objects.requireNonNull(file, "File was null");
            this.socketFile = file;
        }

        @Override // org.newsclub.net.unix.AFSocketAddressFromHostname
        public AFUNIXSocketAddress addressFromHost(String host, int port) throws SocketException {
            return AFUNIXSocketAddress.of(this.socketFile, port);
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFUNIXSocketFactory$SystemProperty.class */
    public static final class SystemProperty extends DefaultSocketHostnameSocketFactory {
        private static final String PROP_SOCKET_DEFAULT = "org.newsclub.net.unix.socket.default";

        @Override // org.newsclub.net.unix.AFSocketAddressFromHostname
        public AFUNIXSocketAddress addressFromHost(String host, int port) throws SocketException {
            String path = System.getProperty(PROP_SOCKET_DEFAULT);
            if (path == null || path.isEmpty()) {
                throw new IllegalStateException("Property not configured: org.newsclub.net.unix.socket.default");
            }
            File socketFile = new File(path);
            return AFUNIXSocketAddress.of(socketFile, port);
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFUNIXSocketFactory$URIScheme.class */
    public static final class URIScheme extends AFUNIXSocketFactory {
        private static final String FILE_SCHEME_PREFIX = "file://";
        private static final String FILE_SCHEME_PREFIX_ENCODED = "file%";
        private static final String FILE_SCHEME_LOCALHOST = "localhost";

        @Override // org.newsclub.net.unix.AFUNIXSocketFactory, org.newsclub.net.unix.AFSocketFactory
        protected /* bridge */ /* synthetic */ Socket connectTo(AFSocketAddress aFSocketAddress) throws IOException {
            return super.connectTo((AFUNIXSocketAddress) aFSocketAddress);
        }

        private static String stripBrackets(String host) {
            if (host.startsWith("[")) {
                if (host.endsWith("]")) {
                    host = host.substring(1, host.length() - 1);
                } else {
                    host = host.substring(1);
                }
            }
            return host;
        }

        @Override // org.newsclub.net.unix.AFSocketAddressFromHostname
        public boolean isHostnameSupported(String host) {
            String host2 = stripBrackets(host);
            return host2.startsWith(FILE_SCHEME_PREFIX) || host2.startsWith(FILE_SCHEME_PREFIX_ENCODED);
        }

        @Override // org.newsclub.net.unix.AFSocketAddressFromHostname
        public AFUNIXSocketAddress addressFromHost(String host, int port) throws SocketException {
            String host2 = stripBrackets(host);
            if (host2.startsWith(FILE_SCHEME_PREFIX_ENCODED)) {
                try {
                    host2 = URLDecoder.decode(host2, "UTF-8");
                } catch (Exception e) {
                    throw ((SocketException) new SocketException().initCause(e));
                }
            }
            if (!host2.startsWith(FILE_SCHEME_PREFIX)) {
                throw new SocketException("Unsupported scheme");
            }
            String path = host2.substring(FILE_SCHEME_PREFIX.length());
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
}
