package org.newsclub.net.unix;

import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.IOException;
import java.net.ProtocolFamily;
import java.net.SocketAddress;
import org.newsclub.net.unix.AFDatagramSocket;
import org.newsclub.net.unix.AFServerSocket;
import org.newsclub.net.unix.AFSocket;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFGenericSelectorProvider.class */
final class AFGenericSelectorProvider extends AFSelectorProvider<AFGenericSocketAddress> {
    private static final AFGenericSelectorProvider INSTANCE = new AFGenericSelectorProvider();
    static final AFAddressFamily<AFGenericSocketAddress> AF_GENERIC = AFAddressFamily.registerAddressFamilyImpl("generic", AFGenericSocketAddress.addressFamily(), new AFAddressFamilyConfig<AFGenericSocketAddress>() { // from class: org.newsclub.net.unix.AFGenericSelectorProvider.1
        @Override // org.newsclub.net.unix.AFAddressFamilyConfig
        protected Class<? extends AFSocket<AFGenericSocketAddress>> socketClass() {
            return AFGenericSocket.class;
        }

        @Override // org.newsclub.net.unix.AFAddressFamilyConfig
        protected AFSocket.Constructor<AFGenericSocketAddress> socketConstructor() {
            return AFGenericSocket::new;
        }

        @Override // org.newsclub.net.unix.AFAddressFamilyConfig
        protected Class<? extends AFServerSocket<AFGenericSocketAddress>> serverSocketClass() {
            return AFGenericServerSocket.class;
        }

        @Override // org.newsclub.net.unix.AFAddressFamilyConfig
        protected AFServerSocket.Constructor<AFGenericSocketAddress> serverSocketConstructor() {
            return AFGenericServerSocket::new;
        }

        @Override // org.newsclub.net.unix.AFAddressFamilyConfig
        protected Class<? extends AFSocketChannel<AFGenericSocketAddress>> socketChannelClass() {
            return AFGenericSocketChannel.class;
        }

        @Override // org.newsclub.net.unix.AFAddressFamilyConfig
        protected Class<? extends AFServerSocketChannel<AFGenericSocketAddress>> serverSocketChannelClass() {
            return AFGenericServerSocketChannel.class;
        }

        @Override // org.newsclub.net.unix.AFAddressFamilyConfig
        protected Class<? extends AFDatagramSocket<AFGenericSocketAddress>> datagramSocketClass() {
            return AFGenericDatagramSocket.class;
        }

        @Override // org.newsclub.net.unix.AFAddressFamilyConfig
        protected AFDatagramSocket.Constructor<AFGenericSocketAddress> datagramSocketConstructor() {
            return AFGenericDatagramSocket::new;
        }

        @Override // org.newsclub.net.unix.AFAddressFamilyConfig
        protected Class<? extends AFDatagramChannel<AFGenericSocketAddress>> datagramChannelClass() {
            return AFGenericDatagramChannel.class;
        }
    });

    private AFGenericSelectorProvider() {
    }

    @SuppressFBWarnings({"MS_EXPOSE_REP"})
    public static AFGenericSelectorProvider getInstance() {
        return INSTANCE;
    }

    public static AFGenericSelectorProvider provider() {
        return getInstance();
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider
    protected <P extends AFSomeSocket> AFSocketPair<P> newSocketPair(P s1, P s2) {
        return new AFGenericSocketPair(s1, s2);
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider
    public AFGenericSocketPair<AFGenericSocketChannel> openSocketChannelPair() throws IOException {
        return (AFGenericSocketPair) super.openSocketChannelPair();
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider
    public AFGenericSocketPair<AFGenericDatagramChannel> openDatagramChannelPair() throws IOException {
        return (AFGenericSocketPair) super.openDatagramChannelPair();
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider
    public AFGenericSocketPair<AFGenericDatagramChannel> openDatagramChannelPair(AFSocketType type) throws IOException {
        return (AFGenericSocketPair) super.openDatagramChannelPair(type);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.newsclub.net.unix.AFSelectorProvider
    public AFGenericSocket newSocket() throws IOException {
        return AFGenericSocket.newInstance();
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider, java.nio.channels.spi.SelectorProvider
    public AFGenericDatagramChannel openDatagramChannel() throws IOException {
        return AFGenericDatagramSocket.newInstance().getChannel();
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider
    public AFGenericDatagramChannel openDatagramChannel(AFSocketType type) throws IOException {
        return AFGenericDatagramSocket.newInstance(type).getChannel();
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider, java.nio.channels.spi.SelectorProvider
    public AFGenericDatagramChannel openDatagramChannel(ProtocolFamily family) throws IOException {
        return (AFGenericDatagramChannel) super.openDatagramChannel(family);
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider, java.nio.channels.spi.SelectorProvider
    public AFGenericServerSocketChannel openServerSocketChannel() throws IOException {
        return AFGenericServerSocket.newInstance().getChannel();
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider
    public AFGenericServerSocketChannel openServerSocketChannel(SocketAddress sa) throws IOException {
        return AFGenericServerSocket.bindOn(AFGenericSocketAddress.unwrap(sa)).getChannel();
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider, java.nio.channels.spi.SelectorProvider
    public AFGenericSocketChannel openSocketChannel() throws IOException {
        return (AFGenericSocketChannel) super.openSocketChannel();
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider
    public AFGenericSocketChannel openSocketChannel(SocketAddress sa) throws IOException {
        return AFGenericSocket.connectTo(AFGenericSocketAddress.unwrap(sa)).getChannel();
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider
    protected ProtocolFamily protocolFamily() {
        return AFGenericProtocolFamily.GENERIC;
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider
    protected AFAddressFamily<AFGenericSocketAddress> addressFamily() {
        return AF_GENERIC;
    }
}
