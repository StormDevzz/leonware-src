// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.nio.channels.DatagramChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.net.SocketAddress;
import java.net.ProtocolFamily;
import java.io.IOException;
import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;

final class AFGenericSelectorProvider extends AFSelectorProvider<AFGenericSocketAddress>
{
    private static final AFGenericSelectorProvider INSTANCE;
    static final AFAddressFamily<AFGenericSocketAddress> AF_GENERIC;
    
    private AFGenericSelectorProvider() {
    }
    
    @SuppressFBWarnings({ "MS_EXPOSE_REP" })
    public static AFGenericSelectorProvider getInstance() {
        return AFGenericSelectorProvider.INSTANCE;
    }
    
    public static AFGenericSelectorProvider provider() {
        return getInstance();
    }
    
    @Override
    protected <P extends AFSomeSocket> AFSocketPair<P> newSocketPair(final P s1, final P s2) {
        return new AFGenericSocketPair<P>(s1, s2);
    }
    
    @Override
    public AFGenericSocketPair<AFGenericSocketChannel> openSocketChannelPair() throws IOException {
        return (AFGenericSocketPair)super.openSocketChannelPair();
    }
    
    @Override
    public AFGenericSocketPair<AFGenericDatagramChannel> openDatagramChannelPair() throws IOException {
        return (AFGenericSocketPair)super.openDatagramChannelPair();
    }
    
    @Override
    public AFGenericSocketPair<AFGenericDatagramChannel> openDatagramChannelPair(final AFSocketType type) throws IOException {
        return (AFGenericSocketPair)super.openDatagramChannelPair(type);
    }
    
    @Override
    protected AFGenericSocket newSocket() throws IOException {
        return AFGenericSocket.newInstance();
    }
    
    @Override
    public AFGenericDatagramChannel openDatagramChannel() throws IOException {
        return AFGenericDatagramSocket.newInstance().getChannel();
    }
    
    @Override
    public AFGenericDatagramChannel openDatagramChannel(final AFSocketType type) throws IOException {
        return AFGenericDatagramSocket.newInstance(type).getChannel();
    }
    
    @Override
    public AFGenericDatagramChannel openDatagramChannel(final ProtocolFamily family) throws IOException {
        return (AFGenericDatagramChannel)super.openDatagramChannel(family);
    }
    
    @Override
    public AFGenericServerSocketChannel openServerSocketChannel() throws IOException {
        return AFGenericServerSocket.newInstance().getChannel();
    }
    
    @Override
    public AFGenericServerSocketChannel openServerSocketChannel(final SocketAddress sa) throws IOException {
        return AFGenericServerSocket.bindOn(AFGenericSocketAddress.unwrap(sa)).getChannel();
    }
    
    @Override
    public AFGenericSocketChannel openSocketChannel() throws IOException {
        return (AFGenericSocketChannel)super.openSocketChannel();
    }
    
    @Override
    public AFGenericSocketChannel openSocketChannel(final SocketAddress sa) throws IOException {
        return AFGenericSocket.connectTo(AFGenericSocketAddress.unwrap(sa)).getChannel();
    }
    
    @Override
    protected ProtocolFamily protocolFamily() {
        return AFGenericProtocolFamily.GENERIC;
    }
    
    @Override
    protected AFAddressFamily<AFGenericSocketAddress> addressFamily() {
        return AFGenericSelectorProvider.AF_GENERIC;
    }
    
    static {
        INSTANCE = new AFGenericSelectorProvider();
        AF_GENERIC = AFAddressFamily.registerAddressFamilyImpl("generic", AFGenericSocketAddress.addressFamily(), new AFAddressFamilyConfig<AFGenericSocketAddress>() {
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
    }
}
