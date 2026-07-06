package org.newsclub.net.unix;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Objects;
import javax.net.SocketFactory;
import org.newsclub.net.unix.AFSocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSocketFactory.class */
public abstract class AFSocketFactory<A extends AFSocketAddress> extends SocketFactory implements AFSocketAddressFromHostname<A> {
    @Override // javax.net.SocketFactory
    public abstract Socket createSocket() throws SocketException;

    protected abstract Socket connectTo(A a) throws IOException;

    protected AFSocketFactory() {
    }

    protected final boolean isInetAddressSupported(InetAddress address) {
        return address != null && isHostnameSupported(address.getHostName());
    }

    private Socket connectTo(SocketAddress addr) throws IOException {
        if (addr instanceof AFSocketAddress) {
            return connectTo((AFSocketAddress) addr);
        }
        Socket sock = new Socket();
        sock.connect(addr);
        return sock;
    }

    @Override // javax.net.SocketFactory
    public final Socket createSocket(String host, int port) throws IOException {
        if (!isHostnameSupported(host)) {
            throw new SocketException("Unsupported hostname");
        }
        if (port < 0) {
            throw new IllegalArgumentException("Illegal port");
        }
        SocketAddress socketAddress = addressFromHost(host, port);
        return connectTo(socketAddress);
    }

    @Override // javax.net.SocketFactory
    public final Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
        if (!isHostnameSupported(host)) {
            throw new SocketException("Unsupported hostname");
        }
        if (localPort < 0) {
            throw new IllegalArgumentException("Illegal local port");
        }
        return createSocket(host, port);
    }

    @Override // javax.net.SocketFactory
    public final Socket createSocket(InetAddress address, int port) throws IOException {
        if (!isInetAddressSupported(address)) {
            throw new SocketException("Unsupported address");
        }
        String hostname = address.getHostName();
        if (!isHostnameSupported(hostname)) {
            throw new SocketException("Unsupported hostname");
        }
        return createSocket(hostname, port);
    }

    @Override // javax.net.SocketFactory
    public final Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        if (!isInetAddressSupported(address)) {
            throw new SocketException("Unsupported address");
        }
        if (localPort < 0) {
            throw new IllegalArgumentException("Illegal local port");
        }
        return createSocket(address, port);
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSocketFactory$FixedAddressSocketFactory.class */
    public static final class FixedAddressSocketFactory extends AFSocketFactory<AFSocketAddress> {
        private final SocketAddress forceAddr;

        public FixedAddressSocketFactory(SocketAddress address) {
            this.forceAddr = (SocketAddress) Objects.requireNonNull(address);
        }

        @Override // org.newsclub.net.unix.AFSocketAddressFromHostname
        public boolean isHostnameSupported(String host) {
            return true;
        }

        @Override // org.newsclub.net.unix.AFSocketAddressFromHostname
        public SocketAddress addressFromHost(String host, int port) throws SocketException {
            return this.forceAddr;
        }

        @Override // org.newsclub.net.unix.AFSocketFactory, javax.net.SocketFactory
        public Socket createSocket() throws SocketException {
            try {
                if (this.forceAddr instanceof AFSocketAddress) {
                    AFSocket<?> socket = ((AFSocketAddress) this.forceAddr).getAddressFamily().newSocket();
                    socket.forceConnectAddress(this.forceAddr);
                    return socket;
                }
                return new Socket() { // from class: org.newsclub.net.unix.AFSocketFactory.FixedAddressSocketFactory.1
                    @Override // java.net.Socket
                    public void connect(SocketAddress endpoint) throws IOException {
                        super.connect(FixedAddressSocketFactory.this.forceAddr);
                    }

                    @Override // java.net.Socket
                    public void connect(SocketAddress endpoint, int timeout) throws IOException {
                        super.connect(FixedAddressSocketFactory.this.forceAddr, timeout);
                    }
                };
            } catch (SocketException e) {
                throw e;
            } catch (IOException e2) {
                throw ((SocketException) new SocketException().initCause(e2));
            }
        }

        @Override // org.newsclub.net.unix.AFSocketFactory
        protected Socket connectTo(AFSocketAddress addr) throws IOException {
            Socket sock = createSocket();
            sock.connect(this.forceAddr);
            return sock;
        }
    }
}
