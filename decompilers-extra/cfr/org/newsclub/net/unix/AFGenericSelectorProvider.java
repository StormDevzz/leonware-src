/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.kohlschutter.annotations.compiletime.SuppressFBWarnings
 *  org.eclipse.jdt.annotation.NonNull
 */
package org.newsclub.net.unix;

import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.IOException;
import java.net.ProtocolFamily;
import java.net.SocketAddress;
import org.eclipse.jdt.annotation.NonNull;
import org.newsclub.net.unix.AFAddressFamily;
import org.newsclub.net.unix.AFAddressFamilyConfig;
import org.newsclub.net.unix.AFDatagramChannel;
import org.newsclub.net.unix.AFDatagramSocket;
import org.newsclub.net.unix.AFGenericDatagramChannel;
import org.newsclub.net.unix.AFGenericDatagramSocket;
import org.newsclub.net.unix.AFGenericProtocolFamily;
import org.newsclub.net.unix.AFGenericServerSocket;
import org.newsclub.net.unix.AFGenericServerSocketChannel;
import org.newsclub.net.unix.AFGenericSocket;
import org.newsclub.net.unix.AFGenericSocketAddress;
import org.newsclub.net.unix.AFGenericSocketChannel;
import org.newsclub.net.unix.AFGenericSocketPair;
import org.newsclub.net.unix.AFSelectorProvider;
import org.newsclub.net.unix.AFServerSocket;
import org.newsclub.net.unix.AFServerSocketChannel;
import org.newsclub.net.unix.AFSocket;
import org.newsclub.net.unix.AFSocketChannel;
import org.newsclub.net.unix.AFSocketPair;
import org.newsclub.net.unix.AFSocketType;
import org.newsclub.net.unix.AFSomeSocket;

final class AFGenericSelectorProvider
extends AFSelectorProvider<AFGenericSocketAddress> {
    private static final AFGenericSelectorProvider INSTANCE = new AFGenericSelectorProvider();
    static final AFAddressFamily<@NonNull AFGenericSocketAddress> AF_GENERIC = AFAddressFamily.registerAddressFamilyImpl("generic", AFGenericSocketAddress.addressFamily(), new AFAddressFamilyConfig<AFGenericSocketAddress>(){

        @Override
        protected Class<? extends AFSocket<AFGenericSocketAddress>> socketClass() {
            return AFGenericSocket.class;
        }

        @Override
        protected AFSocket.Constructor<AFGenericSocketAddress> socketConstructor() {
            return AFGenericSocket::new;
        }

        @Override
        protected Class<? extends AFServerSocket<AFGenericSocketAddress>> serverSocketClass() {
            return AFGenericServerSocket.class;
        }

        @Override
        protected AFServerSocket.Constructor<AFGenericSocketAddress> serverSocketConstructor() {
            return AFGenericServerSocket::new;
        }

        @Override
        protected Class<? extends AFSocketChannel<AFGenericSocketAddress>> socketChannelClass() {
            return AFGenericSocketChannel.class;
        }

        @Override
        protected Class<? extends AFServerSocketChannel<AFGenericSocketAddress>> serverSocketChannelClass() {
            return AFGenericServerSocketChannel.class;
        }

        @Override
        protected Class<? extends AFDatagramSocket<AFGenericSocketAddress>> datagramSocketClass() {
            return AFGenericDatagramSocket.class;
        }

        @Override
        protected AFDatagramSocket.Constructor<AFGenericSocketAddress> datagramSocketConstructor() {
            return AFGenericDatagramSocket::new;
        }

        @Override
        protected Class<? extends AFDatagramChannel<AFGenericSocketAddress>> datagramChannelClass() {
            return AFGenericDatagramChannel.class;
        }
    });

    private AFGenericSelectorProvider() {
    }

    @SuppressFBWarnings(value={"MS_EXPOSE_REP"})
    public static AFGenericSelectorProvider getInstance() {
        return INSTANCE;
    }

    public static AFGenericSelectorProvider provider() {
        return AFGenericSelectorProvider.getInstance();
    }

    @Override
    protected <P extends AFSomeSocket> AFSocketPair<P> newSocketPair(P s1, P s2) {
        return new AFGenericSocketPair<P>(s1, s2);
    }

    public AFGenericSocketPair<AFGenericSocketChannel> openSocketChannelPair() throws IOException {
        return (AFGenericSocketPair)super.openSocketChannelPair();
    }

    public AFGenericSocketPair<AFGenericDatagramChannel> openDatagramChannelPair() throws IOException {
        return (AFGenericSocketPair)super.openDatagramChannelPair();
    }

    public AFGenericSocketPair<AFGenericDatagramChannel> openDatagramChannelPair(AFSocketType type) throws IOException {
        return (AFGenericSocketPair)super.openDatagramChannelPair(type);
    }

    protected AFGenericSocket newSocket() throws IOException {
        return AFGenericSocket.newInstance();
    }

    @Override
    public AFGenericDatagramChannel openDatagramChannel() throws IOException {
        return AFGenericDatagramSocket.newInstance().getChannel();
    }

    public AFGenericDatagramChannel openDatagramChannel(AFSocketType type) throws IOException {
        return AFGenericDatagramSocket.newInstance(type).getChannel();
    }

    @Override
    public AFGenericDatagramChannel openDatagramChannel(ProtocolFamily family) throws IOException {
        return (AFGenericDatagramChannel)super.openDatagramChannel(family);
    }

    @Override
    public AFGenericServerSocketChannel openServerSocketChannel() throws IOException {
        return AFGenericServerSocket.newInstance().getChannel();
    }

    public AFGenericServerSocketChannel openServerSocketChannel(SocketAddress sa) throws IOException {
        return AFGenericServerSocket.bindOn(AFGenericSocketAddress.unwrap(sa)).getChannel();
    }

    @Override
    public AFGenericSocketChannel openSocketChannel() throws IOException {
        return (AFGenericSocketChannel)super.openSocketChannel();
    }

    public AFGenericSocketChannel openSocketChannel(SocketAddress sa) throws IOException {
        return AFGenericSocket.connectTo(AFGenericSocketAddress.unwrap(sa)).getChannel();
    }

    @Override
    protected ProtocolFamily protocolFamily() {
        return AFGenericProtocolFamily.GENERIC;
    }

    @Override
    protected AFAddressFamily<@NonNull AFGenericSocketAddress> addressFamily() {
        return AF_GENERIC;
    }
}

