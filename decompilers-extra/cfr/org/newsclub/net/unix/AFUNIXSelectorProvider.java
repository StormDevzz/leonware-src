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
import org.newsclub.net.unix.AFSelectorProvider;
import org.newsclub.net.unix.AFServerSocket;
import org.newsclub.net.unix.AFServerSocketChannel;
import org.newsclub.net.unix.AFSocket;
import org.newsclub.net.unix.AFSocketChannel;
import org.newsclub.net.unix.AFSocketPair;
import org.newsclub.net.unix.AFSocketType;
import org.newsclub.net.unix.AFSomeSocket;
import org.newsclub.net.unix.AFUNIXDatagramChannel;
import org.newsclub.net.unix.AFUNIXDatagramSocket;
import org.newsclub.net.unix.AFUNIXProtocolFamily;
import org.newsclub.net.unix.AFUNIXServerSocket;
import org.newsclub.net.unix.AFUNIXServerSocketChannel;
import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import org.newsclub.net.unix.AFUNIXSocketChannel;
import org.newsclub.net.unix.AFUNIXSocketPair;

public final class AFUNIXSelectorProvider
extends AFSelectorProvider<AFUNIXSocketAddress> {
    private static final AFUNIXSelectorProvider INSTANCE = new AFUNIXSelectorProvider();
    static final AFAddressFamily<@NonNull AFUNIXSocketAddress> AF_UNIX = AFAddressFamily.registerAddressFamilyImpl("un", AFUNIXSocketAddress.AF_UNIX, new AFAddressFamilyConfig<AFUNIXSocketAddress>(){

        @Override
        public Class<? extends AFSocket<AFUNIXSocketAddress>> socketClass() {
            return AFUNIXSocket.class;
        }

        @Override
        public AFSocket.Constructor<AFUNIXSocketAddress> socketConstructor() {
            return AFUNIXSocket::new;
        }

        @Override
        public Class<? extends AFServerSocket<AFUNIXSocketAddress>> serverSocketClass() {
            return AFUNIXServerSocket.class;
        }

        @Override
        public AFServerSocket.Constructor<AFUNIXSocketAddress> serverSocketConstructor() {
            return AFUNIXServerSocket::new;
        }

        @Override
        public Class<? extends AFSocketChannel<AFUNIXSocketAddress>> socketChannelClass() {
            return AFUNIXSocketChannel.class;
        }

        @Override
        public Class<? extends AFServerSocketChannel<AFUNIXSocketAddress>> serverSocketChannelClass() {
            return AFUNIXServerSocketChannel.class;
        }

        @Override
        public Class<? extends AFDatagramSocket<AFUNIXSocketAddress>> datagramSocketClass() {
            return AFUNIXDatagramSocket.class;
        }

        @Override
        public AFDatagramSocket.Constructor<AFUNIXSocketAddress> datagramSocketConstructor() {
            return AFUNIXDatagramSocket::new;
        }

        @Override
        public Class<? extends AFDatagramChannel<AFUNIXSocketAddress>> datagramChannelClass() {
            return AFUNIXDatagramChannel.class;
        }
    });

    private AFUNIXSelectorProvider() {
    }

    @SuppressFBWarnings(value={"MS_EXPOSE_REP"})
    public static AFUNIXSelectorProvider getInstance() {
        return INSTANCE;
    }

    public static AFUNIXSelectorProvider provider() {
        return AFUNIXSelectorProvider.getInstance();
    }

    @Override
    protected <P extends AFSomeSocket> AFSocketPair<P> newSocketPair(P s1, P s2) {
        return new AFUNIXSocketPair<P>(s1, s2);
    }

    public AFUNIXSocketPair<AFUNIXSocketChannel> openSocketChannelPair() throws IOException {
        return (AFUNIXSocketPair)super.openSocketChannelPair();
    }

    public AFUNIXSocketPair<AFUNIXDatagramChannel> openDatagramChannelPair() throws IOException {
        return (AFUNIXSocketPair)super.openDatagramChannelPair();
    }

    public AFUNIXSocketPair<AFUNIXDatagramChannel> openDatagramChannelPair(AFSocketType type) throws IOException {
        return (AFUNIXSocketPair)super.openDatagramChannelPair(type);
    }

    protected AFUNIXSocket newSocket() throws IOException {
        return AFUNIXSocket.newInstance();
    }

    @Override
    public AFUNIXDatagramChannel openDatagramChannel() throws IOException {
        return AFUNIXDatagramSocket.newInstance().getChannel();
    }

    public AFUNIXDatagramChannel openDatagramChannel(AFSocketType type) throws IOException {
        return AFUNIXDatagramSocket.newInstance(type).getChannel();
    }

    @Override
    public AFUNIXDatagramChannel openDatagramChannel(ProtocolFamily family) throws IOException {
        return (AFUNIXDatagramChannel)super.openDatagramChannel(family);
    }

    @Override
    public AFUNIXServerSocketChannel openServerSocketChannel() throws IOException {
        return AFUNIXServerSocket.newInstance().getChannel();
    }

    public AFUNIXServerSocketChannel openServerSocketChannel(SocketAddress sa) throws IOException {
        return AFUNIXServerSocket.bindOn(AFUNIXSocketAddress.unwrap(sa)).getChannel();
    }

    @Override
    public AFUNIXSocketChannel openSocketChannel() throws IOException {
        return (AFUNIXSocketChannel)super.openSocketChannel();
    }

    public AFUNIXSocketChannel openSocketChannel(SocketAddress sa) throws IOException {
        return AFUNIXSocket.connectTo(AFUNIXSocketAddress.unwrap(sa)).getChannel();
    }

    @Override
    protected ProtocolFamily protocolFamily() {
        return AFUNIXProtocolFamily.UNIX;
    }

    @Override
    protected AFAddressFamily<@NonNull AFUNIXSocketAddress> addressFamily() {
        return AFUNIXSocketAddress.AF_UNIX;
    }
}

