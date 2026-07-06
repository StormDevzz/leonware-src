// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.IOException;
import java.net.SocketException;
import java.net.Socket;

abstract class AFGenericSocketFactory extends AFSocketFactory<AFGenericSocketAddress>
{
    protected AFGenericSocketFactory() {
    }
    
    @Override
    public final Socket createSocket() throws SocketException {
        return this.configure(AFGenericSocket.newInstance(this));
    }
    
    @Override
    protected final AFGenericSocket connectTo(final AFGenericSocketAddress addr) throws IOException {
        return this.configure(AFGenericSocket.connectTo(addr));
    }
    
    protected AFGenericSocket configure(final AFGenericSocket sock) throws SocketException {
        return sock;
    }
}
