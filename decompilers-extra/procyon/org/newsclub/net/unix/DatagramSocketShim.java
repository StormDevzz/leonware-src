// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.IOException;
import java.net.DatagramSocketImpl;
import java.net.DatagramSocket;

abstract class DatagramSocketShim extends DatagramSocket
{
    protected DatagramSocketShim(final DatagramSocketImpl impl) {
        super(impl);
    }
    
    public abstract <T> T getOption(final AFSocketOption<T> p0) throws IOException;
    
    public abstract <T> DatagramSocket setOption(final AFSocketOption<T> p0, final T p1) throws IOException;
}
