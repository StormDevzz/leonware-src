// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.util.Objects;
import java.net.SocketAddress;
import java.io.IOException;
import java.net.SocketException;
import java.net.Socket;
import java.net.InetAddress;
import javax.net.SocketFactory;

public abstract class AFSocketFactory<A extends AFSocketAddress> extends SocketFactory implements AFSocketAddressFromHostname<A>
{
    protected AFSocketFactory() {
    }
    
    protected final boolean isInetAddressSupported(final InetAddress address) {
        return address != null && this.isHostnameSupported(address.getHostName());
    }
    
    @Override
    public abstract Socket createSocket() throws SocketException;
    
    protected abstract Socket connectTo(final A p0) throws IOException;
    
    private Socket connectTo(final SocketAddress addr) throws IOException {
        if (addr instanceof AFSocketAddress) {
            return this.connectTo((AFSocketAddress)addr);
        }
        final Socket sock = new Socket();
        sock.connect(addr);
        return sock;
    }
    
    @Override
    public final Socket createSocket(final String host, final int port) throws IOException {
        if (!this.isHostnameSupported(host)) {
            throw new SocketException("Unsupported hostname");
        }
        if (port < 0) {
            throw new IllegalArgumentException("Illegal port");
        }
        final SocketAddress socketAddress = this.addressFromHost(host, port);
        return this.connectTo(socketAddress);
    }
    
    @Override
    public final Socket createSocket(final String host, final int port, final InetAddress localHost, final int localPort) throws IOException {
        if (!this.isHostnameSupported(host)) {
            throw new SocketException("Unsupported hostname");
        }
        if (localPort < 0) {
            throw new IllegalArgumentException("Illegal local port");
        }
        return this.createSocket(host, port);
    }
    
    @Override
    public final Socket createSocket(final InetAddress address, final int port) throws IOException {
        if (!this.isInetAddressSupported(address)) {
            throw new SocketException("Unsupported address");
        }
        final String hostname = address.getHostName();
        if (!this.isHostnameSupported(hostname)) {
            throw new SocketException("Unsupported hostname");
        }
        return this.createSocket(hostname, port);
    }
    
    @Override
    public final Socket createSocket(final InetAddress address, final int port, final InetAddress localAddress, final int localPort) throws IOException {
        if (!this.isInetAddressSupported(address)) {
            throw new SocketException("Unsupported address");
        }
        if (localPort < 0) {
            throw new IllegalArgumentException("Illegal local port");
        }
        return this.createSocket(address, port);
    }
    
    public static final class FixedAddressSocketFactory extends AFSocketFactory<AFSocketAddress>
    {
        private final SocketAddress forceAddr;
        
        public FixedAddressSocketFactory(final SocketAddress address) {
            this.forceAddr = Objects.requireNonNull(address);
        }
        
        @Override
        public boolean isHostnameSupported(final String host) {
            return true;
        }
        
        @Override
        public SocketAddress addressFromHost(final String host, final int port) throws SocketException {
            return this.forceAddr;
        }
        
        @Override
        public Socket createSocket() throws SocketException {
            try {
                if (this.forceAddr instanceof AFSocketAddress) {
                    final AFSocket<?> socket = ((AFSocketAddress)this.forceAddr).getAddressFamily().newSocket();
                    socket.forceConnectAddress(this.forceAddr);
                    return socket;
                }
                return new Socket() {
                    @Override
                    public void connect(final SocketAddress endpoint) throws IOException {
                        super.connect(FixedAddressSocketFactory.this.forceAddr);
                    }
                    
                    @Override
                    public void connect(final SocketAddress endpoint, final int timeout) throws IOException {
                        super.connect(FixedAddressSocketFactory.this.forceAddr, timeout);
                    }
                };
            }
            catch (final SocketException e) {
                throw e;
            }
            catch (final IOException e2) {
                throw (SocketException)new SocketException().initCause(e2);
            }
        }
        
        @Override
        protected Socket connectTo(final AFSocketAddress addr) throws IOException {
            final Socket sock = this.createSocket();
            sock.connect(this.forceAddr);
            return sock;
        }
    }
}
