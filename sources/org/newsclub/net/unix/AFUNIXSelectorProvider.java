package org.newsclub.net.unix;

import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.IOException;
import java.net.ProtocolFamily;
import java.net.SocketAddress;
import org.newsclub.net.unix.AFDatagramSocket;
import org.newsclub.net.unix.AFServerSocket;
import org.newsclub.net.unix.AFSocket;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFUNIXSelectorProvider.class */
public final class AFUNIXSelectorProvider extends AFSelectorProvider<AFUNIXSocketAddress> {
    private static final AFUNIXSelectorProvider INSTANCE = new AFUNIXSelectorProvider();
    static final AFAddressFamily<AFUNIXSocketAddress> AF_UNIX = AFAddressFamily.registerAddressFamilyImpl("un", AFUNIXSocketAddress.AF_UNIX, new AFAddressFamilyConfig<AFUNIXSocketAddress>() { // from class: org.newsclub.net.unix.AFUNIXSelectorProvider.1
        @Override // org.newsclub.net.unix.AFAddressFamilyConfig
        public Class<? extends AFSocket<AFUNIXSocketAddress>> socketClass() {
            return AFUNIXSocket.class;
        }

        @Override // org.newsclub.net.unix.AFAddressFamilyConfig
        public AFSocket.Constructor<AFUNIXSocketAddress> socketConstructor() {
            return AFUNIXSocket::new;
        }

        @Override // org.newsclub.net.unix.AFAddressFamilyConfig
        public Class<? extends AFServerSocket<AFUNIXSocketAddress>> serverSocketClass() {
            return AFUNIXServerSocket.class;
        }

        @Override // org.newsclub.net.unix.AFAddressFamilyConfig
        public AFServerSocket.Constructor<AFUNIXSocketAddress> serverSocketConstructor() {
            return AFUNIXServerSocket::new;
        }

        @Override // org.newsclub.net.unix.AFAddressFamilyConfig
        public Class<? extends AFSocketChannel<AFUNIXSocketAddress>> socketChannelClass() {
            return AFUNIXSocketChannel.class;
        }

        @Override // org.newsclub.net.unix.AFAddressFamilyConfig
        public Class<? extends AFServerSocketChannel<AFUNIXSocketAddress>> serverSocketChannelClass() {
            return AFUNIXServerSocketChannel.class;
        }

        @Override // org.newsclub.net.unix.AFAddressFamilyConfig
        public Class<? extends AFDatagramSocket<AFUNIXSocketAddress>> datagramSocketClass() {
            return AFUNIXDatagramSocket.class;
        }

        @Override // org.newsclub.net.unix.AFAddressFamilyConfig
        public AFDatagramSocket.Constructor<AFUNIXSocketAddress> datagramSocketConstructor() {
            return AFUNIXDatagramSocket::new;
        }

        @Override // org.newsclub.net.unix.AFAddressFamilyConfig
        public Class<? extends AFDatagramChannel<AFUNIXSocketAddress>> datagramChannelClass() {
            return AFUNIXDatagramChannel.class;
        }
    });

    private AFUNIXSelectorProvider() {
    }

    @SuppressFBWarnings({"MS_EXPOSE_REP"})
    public static AFUNIXSelectorProvider getInstance() {
        return INSTANCE;
    }

    public static AFUNIXSelectorProvider provider() {
        return getInstance();
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider
    protected <P extends AFSomeSocket> AFSocketPair<P> newSocketPair(P s1, P s2) {
        return new AFUNIXSocketPair(s1, s2);
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider
    public AFUNIXSocketPair<AFUNIXSocketChannel> openSocketChannelPair() throws IOException {
        return (AFUNIXSocketPair) super.openSocketChannelPair();
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider
    public AFUNIXSocketPair<AFUNIXDatagramChannel> openDatagramChannelPair() throws IOException {
        return (AFUNIXSocketPair) super.openDatagramChannelPair();
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider
    public AFUNIXSocketPair<AFUNIXDatagramChannel> openDatagramChannelPair(AFSocketType type) throws IOException {
        return (AFUNIXSocketPair) super.openDatagramChannelPair(type);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.newsclub.net.unix.AFSelectorProvider
    public AFUNIXSocket newSocket() throws IOException {
        return AFUNIXSocket.newInstance();
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider, java.nio.channels.spi.SelectorProvider
    public AFUNIXDatagramChannel openDatagramChannel() throws IOException {
        return AFUNIXDatagramSocket.newInstance().getChannel();
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider
    public AFUNIXDatagramChannel openDatagramChannel(AFSocketType type) throws IOException {
        return AFUNIXDatagramSocket.newInstance(type).getChannel();
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider, java.nio.channels.spi.SelectorProvider
    public AFUNIXDatagramChannel openDatagramChannel(ProtocolFamily family) throws IOException {
        return (AFUNIXDatagramChannel) super.openDatagramChannel(family);
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider, java.nio.channels.spi.SelectorProvider
    public AFUNIXServerSocketChannel openServerSocketChannel() throws IOException {
        return AFUNIXServerSocket.newInstance().getChannel();
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider
    public AFUNIXServerSocketChannel openServerSocketChannel(SocketAddress sa) throws IOException {
        return AFUNIXServerSocket.bindOn(AFUNIXSocketAddress.unwrap(sa)).getChannel();
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider, java.nio.channels.spi.SelectorProvider
    public AFUNIXSocketChannel openSocketChannel() throws IOException {
        return (AFUNIXSocketChannel) super.openSocketChannel();
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider
    public AFUNIXSocketChannel openSocketChannel(SocketAddress sa) throws IOException {
        return AFUNIXSocket.connectTo(AFUNIXSocketAddress.unwrap(sa)).getChannel();
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider
    protected ProtocolFamily protocolFamily() {
        return AFUNIXProtocolFamily.UNIX;
    }

    @Override // org.newsclub.net.unix.AFSelectorProvider
    protected AFAddressFamily<AFUNIXSocketAddress> addressFamily() {
        return AFUNIXSocketAddress.AF_UNIX;
    }
}
