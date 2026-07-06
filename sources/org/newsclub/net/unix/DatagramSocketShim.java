package org.newsclub.net.unix;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramSocketImpl;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/DatagramSocketShim.class */
abstract class DatagramSocketShim extends DatagramSocket {
    public abstract <T> T getOption(AFSocketOption<T> aFSocketOption) throws IOException;

    public abstract <T> DatagramSocket setOption(AFSocketOption<T> aFSocketOption, T t) throws IOException;

    protected DatagramSocketShim(DatagramSocketImpl impl) {
        super(impl);
    }
}
