/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Objects;
import javax.net.SocketFactory;
import org.newsclub.net.unix.AFSocket;
import org.newsclub.net.unix.AFSocketAddress;
import org.newsclub.net.unix.AFSocketAddressFromHostname;

public abstract class AFSocketFactory<A extends AFSocketAddress>
extends SocketFactory
implements AFSocketAddressFromHostname<A> {
    protected AFSocketFactory() {
    }

    protected final boolean isInetAddressSupported(InetAddress address) {
        return address != null && this.isHostnameSupported(address.getHostName());
    }

    @Override
    public abstract Socket createSocket() throws SocketException;

    protected abstract Socket connectTo(A var1) throws IOException;

    private Socket connectTo(SocketAddress addr) throws IOException {
        if (addr instanceof AFSocketAddress) {
            return this.connectTo((A)((AFSocketAddress)addr));
        }
        Socket sock = new Socket();
        sock.connect(addr);
        return sock;
    }

    @Override
    public final Socket createSocket(String host, int port) throws IOException {
        if (!this.isHostnameSupported(host)) {
            throw new SocketException("Unsupported hostname");
        }
        if (port < 0) {
            throw new IllegalArgumentException("Illegal port");
        }
        SocketAddress socketAddress = this.addressFromHost(host, port);
        return this.connectTo(socketAddress);
    }

    @Override
    public final Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
        if (!this.isHostnameSupported(host)) {
            throw new SocketException("Unsupported hostname");
        }
        if (localPort < 0) {
            throw new IllegalArgumentException("Illegal local port");
        }
        return this.createSocket(host, port);
    }

    @Override
    public final Socket createSocket(InetAddress address, int port) throws IOException {
        if (!this.isInetAddressSupported(address)) {
            throw new SocketException("Unsupported address");
        }
        String hostname = address.getHostName();
        if (!this.isHostnameSupported(hostname)) {
            throw new SocketException("Unsupported hostname");
        }
        return this.createSocket(hostname, port);
    }

    @Override
    public final Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        if (!this.isInetAddressSupported(address)) {
            throw new SocketException("Unsupported address");
        }
        if (localPort < 0) {
            throw new IllegalArgumentException("Illegal local port");
        }
        return this.createSocket(address, port);
    }

    public static final class FixedAddressSocketFactory
    extends AFSocketFactory<AFSocketAddress> {
        private final SocketAddress forceAddr;

        public FixedAddressSocketFactory(SocketAddress address) {
            this.forceAddr = Objects.requireNonNull(address);
        }

        @Override
        public boolean isHostnameSupported(String host) {
            return true;
        }

        @Override
        public SocketAddress addressFromHost(String host, int port) throws SocketException {
            return this.forceAddr;
        }

        @Override
        public Socket createSocket() throws SocketException {
            try {
                if (this.forceAddr instanceof AFSocketAddress) {
                    AFSocket<?> socket = ((AFSocketAddress)this.forceAddr).getAddressFamily().newSocket();
                    socket.forceConnectAddress(this.forceAddr);
                    return socket;
                }
                return new Socket(){

                    @Override
                    public void connect(SocketAddress endpoint) throws IOException {
                        super.connect(forceAddr);
                    }

                    @Override
                    public void connect(SocketAddress endpoint, int timeout) throws IOException {
                        super.connect(forceAddr, timeout);
                    }
                };
            }
            catch (SocketException e) {
                throw e;
            }
            catch (IOException e) {
                throw (SocketException)new SocketException().initCause(e);
            }
        }

        @Override
        protected Socket connectTo(AFSocketAddress addr) throws IOException {
            Socket sock = this.createSocket();
            sock.connect(this.forceAddr);
            return sock;
        }
    }
}

