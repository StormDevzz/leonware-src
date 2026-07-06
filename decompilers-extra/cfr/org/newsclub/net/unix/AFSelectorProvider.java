/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jdt.annotation.NonNull
 */
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
import org.eclipse.jdt.annotation.NonNull;
import org.newsclub.net.unix.AFAddressFamily;
import org.newsclub.net.unix.AFDatagramChannel;
import org.newsclub.net.unix.AFDatagramSocket;
import org.newsclub.net.unix.AFPipe;
import org.newsclub.net.unix.AFProtocolFamily;
import org.newsclub.net.unix.AFSelector;
import org.newsclub.net.unix.AFServerSocketChannel;
import org.newsclub.net.unix.AFSocket;
import org.newsclub.net.unix.AFSocketAddress;
import org.newsclub.net.unix.AFSocketChannel;
import org.newsclub.net.unix.AFSocketPair;
import org.newsclub.net.unix.AFSocketType;
import org.newsclub.net.unix.AFSomeSocket;
import org.newsclub.net.unix.NativeUnixSocket;
import org.newsclub.net.unix.SelectorProviderShim;

public abstract class AFSelectorProvider<A extends AFSocketAddress>
extends SelectorProviderShim {
    private static final SelectorProvider AF_PROVIDER = new SelectorProviderShim(){

        @Override
        public SocketChannel openSocketChannel() throws IOException {
            throw new UnsupportedOperationException("Use openSocketChannel(ProtocolFamily) or a specific AFSelectorProvider subclass");
        }

        @Override
        public ServerSocketChannel openServerSocketChannel() throws IOException {
            throw new UnsupportedOperationException("Use openServerSocketChannel(ProtocolFamily) or a specific AFSelectorProvider subclass");
        }

        @Override
        public DatagramChannel openDatagramChannel() throws IOException {
            throw new UnsupportedOperationException("Use openDatagramChannel(ProtocolFamily) or a specific AFSelectorProvider subclass");
        }

        @Override
        public AbstractSelector openSelector() throws IOException {
            throw new UnsupportedOperationException("Use a specific AFSelectorProvider subclass");
        }

        @Override
        public Pipe openPipe() throws IOException {
            throw new UnsupportedOperationException("Use a specific AFSelectorProvider subclass");
        }

        @Override
        public DatagramChannel openDatagramChannel(ProtocolFamily family) throws IOException {
            Objects.requireNonNull(family);
            if (family instanceof AFProtocolFamily) {
                return ((AFProtocolFamily)family).openDatagramChannel();
            }
            throw new UnsupportedOperationException("Unsupported protocol family");
        }

        @Override
        public SocketChannel openSocketChannel(ProtocolFamily family) throws IOException {
            Objects.requireNonNull(family);
            if (family instanceof AFProtocolFamily) {
                return ((AFProtocolFamily)family).openSocketChannel();
            }
            throw new UnsupportedOperationException("Unsupported protocol family");
        }

        @Override
        public ServerSocketChannel openServerSocketChannel(ProtocolFamily family) throws IOException {
            Objects.requireNonNull(family);
            if (family instanceof AFProtocolFamily) {
                return ((AFProtocolFamily)family).openServerSocketChannel();
            }
            throw new UnsupportedOperationException("Unsupported protocol family");
        }
    };

    protected AFSelectorProvider() {
    }

    private AFPipe newPipe(boolean selectable) throws IOException {
        return new AFPipe(this, selectable);
    }

    protected abstract <Y extends AFSomeSocket> AFSocketPair<Y> newSocketPair(Y var1, Y var2);

    protected abstract AFSocket<A> newSocket() throws IOException;

    protected abstract ProtocolFamily protocolFamily();

    protected abstract AFAddressFamily<@NonNull A> addressFamily();

    protected final int domainId() {
        return this.addressFamily().getDomain();
    }

    public AFSocketPair<? extends AFSocketChannel<A>> openSocketChannelPair() throws IOException {
        SocketChannel s1 = this.openSocketChannel();
        SocketChannel s2 = this.openSocketChannel();
        NativeUnixSocket.socketPair(this.domainId(), 1, ((AFSocketChannel)s1).getAFCore().fd, ((AFSocketChannel)s2).getAFCore().fd);
        ((AFSocket)((AFSocketChannel)s1).socket()).internalDummyConnect();
        ((AFSocket)((AFSocketChannel)s2).socket()).internalDummyConnect();
        return this.newSocketPair(s1, s2);
    }

    public AFSocketPair<? extends AFDatagramChannel<A>> openDatagramChannelPair() throws IOException {
        return this.openDatagramChannelPair(AFSocketType.SOCK_DGRAM);
    }

    public AFSocketPair<? extends AFDatagramChannel<A>> openDatagramChannelPair(AFSocketType type) throws IOException {
        ProtocolFamily pf = this.protocolFamily();
        DatagramChannel s1 = this.openDatagramChannel(pf);
        DatagramChannel s2 = this.openDatagramChannel(pf);
        NativeUnixSocket.socketPair(this.domainId(), type.getId(), ((AFDatagramChannel)s1).getAFCore().fd, ((AFDatagramChannel)s2).getAFCore().fd);
        ((AFDatagramSocket)((AFDatagramChannel)s1).socket()).internalDummyBind();
        ((AFDatagramSocket)((AFDatagramChannel)s2).socket()).internalDummyBind();
        ((AFDatagramSocket)((AFDatagramChannel)s1).socket()).internalDummyConnect();
        ((AFDatagramSocket)((AFDatagramChannel)s2).socket()).internalDummyConnect();
        return this.newSocketPair(s1, s2);
    }

    @Override
    public abstract AFDatagramChannel<A> openDatagramChannel() throws IOException;

    public abstract AFDatagramChannel<A> openDatagramChannel(AFSocketType var1) throws IOException;

    @Override
    public final AFPipe openPipe() throws IOException {
        return this.newPipe(false);
    }

    final AFPipe openSelectablePipe() throws IOException {
        return this.newPipe(true);
    }

    @Override
    public final AbstractSelector openSelector() throws IOException {
        return new AFSelector(this);
    }

    @Override
    public abstract AFServerSocketChannel<A> openServerSocketChannel() throws IOException;

    public abstract AFServerSocketChannel<A> openServerSocketChannel(SocketAddress var1) throws IOException;

    @Override
    public AFSocketChannel<A> openSocketChannel() throws IOException {
        return this.newSocket().getChannel();
    }

    public abstract AFSocketChannel<A> openSocketChannel(SocketAddress var1) throws IOException;

    @Override
    public AFSocketChannel<A> openSocketChannel(ProtocolFamily family) throws IOException {
        Objects.requireNonNull(family);
        if (this.protocolFamily().equals(family) || family instanceof StandardProtocolFamily && this.protocolFamily().name().equals(family.name())) {
            return this.openSocketChannel();
        }
        throw new UnsupportedOperationException("Protocol family not supported");
    }

    @Override
    public AFServerSocketChannel<A> openServerSocketChannel(ProtocolFamily family) throws IOException {
        Objects.requireNonNull(family);
        if (this.protocolFamily().equals(family) || family instanceof StandardProtocolFamily && this.protocolFamily().name().equals(family.name())) {
            return this.openServerSocketChannel();
        }
        throw new UnsupportedOperationException("Protocol family not supported");
    }

    @Override
    public AFDatagramChannel<A> openDatagramChannel(ProtocolFamily family) throws IOException {
        Objects.requireNonNull(family);
        if (this.protocolFamily().equals(family) || family instanceof StandardProtocolFamily && this.protocolFamily().name().equals(family.name())) {
            return this.openDatagramChannel();
        }
        throw new UnsupportedOperationException("Protocol family not supported");
    }

    public static SelectorProvider provider() {
        return AF_PROVIDER;
    }
}

