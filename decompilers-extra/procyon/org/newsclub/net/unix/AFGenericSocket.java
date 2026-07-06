// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.nio.channels.SocketChannel;
import java.io.IOException;
import java.net.SocketException;
import java.io.FileDescriptor;

final class AFGenericSocket extends AFSocket<AFGenericSocketAddress> implements AFGenericSocketExtensions
{
    private static AFGenericSocketImplExtensions staticExtensions;
    
    AFGenericSocket(final FileDescriptor fdObj, final AFSocketFactory<AFGenericSocketAddress> factory) throws SocketException {
        super(new AFGenericSocketImpl(fdObj), factory);
    }
    
    private static synchronized AFGenericSocketImplExtensions getStaticImplExtensions() throws IOException {
        if (AFGenericSocket.staticExtensions == null) {
            try (final AFGenericSocket socket = new AFGenericSocket(null, (AFSocketFactory<AFGenericSocketAddress>)null)) {
                AFGenericSocket.staticExtensions = (AFGenericSocketImplExtensions)socket.getImplExtensions();
            }
        }
        return AFGenericSocket.staticExtensions;
    }
    
    public static boolean isSupported() {
        return AFSocket.isSupported();
    }
    
    @Override
    protected AFGenericSocketChannel newChannel() {
        return new AFGenericSocketChannel(this);
    }
    
    public static AFGenericSocket newInstance() throws IOException {
        return (AFGenericSocket)AFSocket.newInstance(AFGenericSocket::new, null);
    }
    
    static AFGenericSocket newInstance(final AFGenericSocketFactory factory) throws SocketException {
        return (AFGenericSocket)AFSocket.newInstance(AFGenericSocket::new, (AFSocketFactory<AFSocketAddress>)factory);
    }
    
    public static AFGenericSocket newStrictInstance() throws IOException {
        return (AFGenericSocket)AFSocket.newInstance(AFGenericSocket::new, null);
    }
    
    public static AFGenericSocket connectTo(final AFGenericSocketAddress addr) throws IOException {
        return (AFGenericSocket)AFSocket.connectTo(AFGenericSocket::new, addr);
    }
    
    @Override
    public AFGenericSocketChannel getChannel() {
        return (AFGenericSocketChannel)super.getChannel();
    }
    
    static {
        AFGenericSocket.staticExtensions = null;
    }
}
