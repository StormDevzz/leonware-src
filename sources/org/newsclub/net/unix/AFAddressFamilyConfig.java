package org.newsclub.net.unix;

import org.newsclub.net.unix.AFDatagramSocket;
import org.newsclub.net.unix.AFServerSocket;
import org.newsclub.net.unix.AFSocket;
import org.newsclub.net.unix.AFSocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFAddressFamilyConfig.class */
public abstract class AFAddressFamilyConfig<A extends AFSocketAddress> {
    protected abstract Class<? extends AFSocket<A>> socketClass();

    protected abstract AFSocket.Constructor<A> socketConstructor();

    protected abstract Class<? extends AFServerSocket<A>> serverSocketClass();

    protected abstract AFServerSocket.Constructor<A> serverSocketConstructor();

    protected abstract Class<? extends AFSocketChannel<A>> socketChannelClass();

    protected abstract Class<? extends AFServerSocketChannel<A>> serverSocketChannelClass();

    protected abstract Class<? extends AFDatagramSocket<A>> datagramSocketClass();

    protected abstract AFDatagramSocket.Constructor<A> datagramSocketConstructor();

    protected abstract Class<? extends AFDatagramChannel<A>> datagramChannelClass();

    protected AFAddressFamilyConfig() {
    }
}
