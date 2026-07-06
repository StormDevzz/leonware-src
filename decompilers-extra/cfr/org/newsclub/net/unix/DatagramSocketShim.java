/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramSocketImpl;
import org.newsclub.net.unix.AFSocketOption;

abstract class DatagramSocketShim
extends DatagramSocket {
    protected DatagramSocketShim(DatagramSocketImpl impl) {
        super(impl);
    }

    public abstract <T> T getOption(AFSocketOption<T> var1) throws IOException;

    public abstract <T> DatagramSocket setOption(AFSocketOption<T> var1, T var2) throws IOException;
}

