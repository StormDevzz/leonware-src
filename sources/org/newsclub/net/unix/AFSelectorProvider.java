package org.newsclub.net.unix;

import java.io.IOException;
import java.net.ProtocolFamily;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Pipe;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;
import java.util.Objects;
import org.newsclub.net.unix.AFSocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSelectorProvider.class */
public abstract class AFSelectorProvider<A extends AFSocketAddress> extends SelectorProviderShim {
    private static final SelectorProvider AF_PROVIDER = new SelectorProviderShim() { // from class: org.newsclub.net.unix.AFSelectorProvider.1
        @Override // java.nio.channels.spi.SelectorProvider
        public SocketChannel openSocketChannel() throws IOException {
            throw new UnsupportedOperationException("Use openSocketChannel(ProtocolFamily) or a specific AFSelectorProvider subclass");
        }

        @Override // java.nio.channels.spi.SelectorProvider
        public ServerSocketChannel openServerSocketChannel() throws IOException {
            throw new UnsupportedOperationException("Use openServerSocketChannel(ProtocolFamily) or a specific AFSelectorProvider subclass");
        }

        @Override // java.nio.channels.spi.SelectorProvider
        public DatagramChannel openDatagramChannel() throws IOException {
            throw new UnsupportedOperationException("Use openDatagramChannel(ProtocolFamily) or a specific AFSelectorProvider subclass");
        }

        @Override // java.nio.channels.spi.SelectorProvider
        public AbstractSelector openSelector() throws IOException {
            throw new UnsupportedOperationException("Use a specific AFSelectorProvider subclass");
        }

        @Override // java.nio.channels.spi.SelectorProvider
        public Pipe openPipe() throws IOException {
            throw new UnsupportedOperationException("Use a specific AFSelectorProvider subclass");
        }

        @Override // java.nio.channels.spi.SelectorProvider
        public DatagramChannel openDatagramChannel(ProtocolFamily family) throws IOException {
            Objects.requireNonNull(family);
            if (family instanceof AFProtocolFamily) {
                return ((AFProtocolFamily) family).openDatagramChannel();
            }
            throw new UnsupportedOperationException("Unsupported protocol family");
        }

        @Override // org.newsclub.net.unix.SelectorProviderShim
        public SocketChannel openSocketChannel(ProtocolFamily family) throws IOException {
            Objects.requireNonNull(family);
            if (family instanceof AFProtocolFamily) {
                return ((AFProtocolFamily) family).openSocketChannel();
            }
            throw new UnsupportedOperationException("Unsupported protocol family");
        }

        @Override // org.newsclub.net.unix.SelectorProviderShim
        public ServerSocketChannel openServerSocketChannel(ProtocolFamily family) throws IOException {
            Objects.requireNonNull(family);
            if (family instanceof AFProtocolFamily) {
                return ((AFProtocolFamily) family).openServerSocketChannel();
            }
            throw new UnsupportedOperationException("Unsupported protocol family");
        }
    };

    protected abstract <Y extends AFSomeSocket> AFSocketPair<Y> newSocketPair(Y y, Y y2);

    protected abstract AFSocket<A> newSocket() throws IOException;

    protected abstract ProtocolFamily protocolFamily();

    protected abstract AFAddressFamily<A> addressFamily();

    @Override // java.nio.channels.spi.SelectorProvider
    public abstract AFDatagramChannel<A> openDatagramChannel() throws IOException;

    public abstract AFDatagramChannel<A> openDatagramChannel(AFSocketType aFSocketType) throws IOException;

    @Override // java.nio.channels.spi.SelectorProvider
    public abstract AFServerSocketChannel<A> openServerSocketChannel() throws IOException;

    public abstract AFServerSocketChannel<A> openServerSocketChannel(SocketAddress socketAddress) throws IOException;

    public abstract AFSocketChannel<A> openSocketChannel(SocketAddress socketAddress) throws IOException;

    protected AFSelectorProvider() {
    }

    private AFPipe newPipe(boolean selectable) throws IOException {
        return new AFPipe(this, selectable);
    }

    protected final int domainId() {
        return addressFamily().getDomain();
    }

    public AFSocketPair<? extends AFSocketChannel<A>> openSocketChannelPair() throws IOException {
        AFSocketChannel<A> aFSocketChannelOpenSocketChannel = openSocketChannel();
        AFSocketChannel<A> aFSocketChannelOpenSocketChannel2 = openSocketChannel();
        NativeUnixSocket.socketPair(domainId(), 1, aFSocketChannelOpenSocketChannel.getAFCore().fd, aFSocketChannelOpenSocketChannel2.getAFCore().fd);
        aFSocketChannelOpenSocketChannel.socket().internalDummyConnect();
        aFSocketChannelOpenSocketChannel2.socket().internalDummyConnect();
        return (AFSocketPair<? extends AFSocketChannel<A>>) newSocketPair(aFSocketChannelOpenSocketChannel, aFSocketChannelOpenSocketChannel2);
    }

    public AFSocketPair<? extends AFDatagramChannel<A>> openDatagramChannelPair() throws IOException {
        return openDatagramChannelPair(AFSocketType.SOCK_DGRAM);
    }

    public AFSocketPair<? extends AFDatagramChannel<A>> openDatagramChannelPair(AFSocketType aFSocketType) throws IOException {
        ProtocolFamily protocolFamily = protocolFamily();
        AFDatagramChannel<A> aFDatagramChannelOpenDatagramChannel = openDatagramChannel(protocolFamily);
        AFDatagramChannel<A> aFDatagramChannelOpenDatagramChannel2 = openDatagramChannel(protocolFamily);
        NativeUnixSocket.socketPair(domainId(), aFSocketType.getId(), aFDatagramChannelOpenDatagramChannel.getAFCore().fd, aFDatagramChannelOpenDatagramChannel2.getAFCore().fd);
        aFDatagramChannelOpenDatagramChannel.socket().internalDummyBind();
        aFDatagramChannelOpenDatagramChannel2.socket().internalDummyBind();
        aFDatagramChannelOpenDatagramChannel.socket().internalDummyConnect();
        aFDatagramChannelOpenDatagramChannel2.socket().internalDummyConnect();
        return (AFSocketPair<? extends AFDatagramChannel<A>>) newSocketPair(aFDatagramChannelOpenDatagramChannel, aFDatagramChannelOpenDatagramChannel2);
    }

    @Override // java.nio.channels.spi.SelectorProvider
    public final AFPipe openPipe() throws IOException {
        return newPipe(false);
    }

    final AFPipe openSelectablePipe() throws IOException {
        return newPipe(true);
    }

    @Override // java.nio.channels.spi.SelectorProvider
    public final AbstractSelector openSelector() throws IOException {
        return new AFSelector(this);
    }

    @Override // java.nio.channels.spi.SelectorProvider
    public AFSocketChannel<A> openSocketChannel() throws IOException {
        return newSocket().getChannel();
    }

    @Override // org.newsclub.net.unix.SelectorProviderShim
    public AFSocketChannel<A> openSocketChannel(ProtocolFamily family) throws IOException {
        Objects.requireNonNull(family);
        if (protocolFamily().equals(family) || ((family instanceof StandardProtocolFamily) && protocolFamily().name().equals(family.name()))) {
            return openSocketChannel();
        }
        throw new UnsupportedOperationException("Protocol family not supported");
    }

    @Override // org.newsclub.net.unix.SelectorProviderShim
    public AFServerSocketChannel<A> openServerSocketChannel(ProtocolFamily family) throws IOException {
        Objects.requireNonNull(family);
        if (protocolFamily().equals(family) || ((family instanceof StandardProtocolFamily) && protocolFamily().name().equals(family.name()))) {
            return openServerSocketChannel();
        }
        throw new UnsupportedOperationException("Protocol family not supported");
    }

    @Override // java.nio.channels.spi.SelectorProvider
    public AFDatagramChannel<A> openDatagramChannel(ProtocolFamily family) throws IOException {
        Objects.requireNonNull(family);
        if (protocolFamily().equals(family) || ((family instanceof StandardProtocolFamily) && protocolFamily().name().equals(family.name()))) {
            return openDatagramChannel();
        }
        throw new UnsupportedOperationException("Protocol family not supported");
    }

    public static SelectorProvider provider() {
        return AF_PROVIDER;
    }
}
