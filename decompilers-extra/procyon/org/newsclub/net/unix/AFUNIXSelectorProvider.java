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

public final class AFUNIXSelectorProvider extends AFSelectorProvider<AFUNIXSocketAddress>
{
    private static final AFUNIXSelectorProvider INSTANCE;
    static final AFAddressFamily<AFUNIXSocketAddress> AF_UNIX;
    
    private AFUNIXSelectorProvider() {
    }
    
    @SuppressFBWarnings({ "MS_EXPOSE_REP" })
    public static AFUNIXSelectorProvider getInstance() {
        return AFUNIXSelectorProvider.INSTANCE;
    }
    
    public static AFUNIXSelectorProvider provider() {
        return getInstance();
    }
    
    @Override
    protected <P extends AFSomeSocket> AFSocketPair<P> newSocketPair(final P s1, final P s2) {
        return new AFUNIXSocketPair<P>(s1, s2);
    }
    
    @Override
    public AFUNIXSocketPair<AFUNIXSocketChannel> openSocketChannelPair() throws IOException {
        return (AFUNIXSocketPair)super.openSocketChannelPair();
    }
    
    @Override
    public AFUNIXSocketPair<AFUNIXDatagramChannel> openDatagramChannelPair() throws IOException {
        return (AFUNIXSocketPair)super.openDatagramChannelPair();
    }
    
    @Override
    public AFUNIXSocketPair<AFUNIXDatagramChannel> openDatagramChannelPair(final AFSocketType type) throws IOException {
        return (AFUNIXSocketPair)super.openDatagramChannelPair(type);
    }
    
    @Override
    protected AFUNIXSocket newSocket() throws IOException {
        return AFUNIXSocket.newInstance();
    }
    
    @Override
    public AFUNIXDatagramChannel openDatagramChannel() throws IOException {
        return AFUNIXDatagramSocket.newInstance().getChannel();
    }
    
    @Override
    public AFUNIXDatagramChannel openDatagramChannel(final AFSocketType type) throws IOException {
        return AFUNIXDatagramSocket.newInstance(type).getChannel();
    }
    
    @Override
    public AFUNIXDatagramChannel openDatagramChannel(final ProtocolFamily family) throws IOException {
        return (AFUNIXDatagramChannel)super.openDatagramChannel(family);
    }
    
    @Override
    public AFUNIXServerSocketChannel openServerSocketChannel() throws IOException {
        return AFUNIXServerSocket.newInstance().getChannel();
    }
    
    @Override
    public AFUNIXServerSocketChannel openServerSocketChannel(final SocketAddress sa) throws IOException {
        return AFUNIXServerSocket.bindOn(AFUNIXSocketAddress.unwrap(sa)).getChannel();
    }
    
    @Override
    public AFUNIXSocketChannel openSocketChannel() throws IOException {
        return (AFUNIXSocketChannel)super.openSocketChannel();
    }
    
    @Override
    public AFUNIXSocketChannel openSocketChannel(final SocketAddress sa) throws IOException {
        return AFUNIXSocket.connectTo(AFUNIXSocketAddress.unwrap(sa)).getChannel();
    }
    
    @Override
    protected ProtocolFamily protocolFamily() {
        return AFUNIXProtocolFamily.UNIX;
    }
    
    @Override
    protected AFAddressFamily<AFUNIXSocketAddress> addressFamily() {
        return AFUNIXSocketAddress.AF_UNIX;
    }
    
    static {
        INSTANCE = new AFUNIXSelectorProvider();
        AF_UNIX = AFAddressFamily.registerAddressFamilyImpl("un", AFUNIXSocketAddress.AF_UNIX, new AFAddressFamilyConfig<AFUNIXSocketAddress>() {
            public Class<? extends AFSocket<AFUNIXSocketAddress>> socketClass() {
                return AFUNIXSocket.class;
            }
            
            public AFSocket.Constructor<AFUNIXSocketAddress> socketConstructor() {
                return AFUNIXSocket::new;
            }
            
            public Class<? extends AFServerSocket<AFUNIXSocketAddress>> serverSocketClass() {
                return AFUNIXServerSocket.class;
            }
            
            public AFServerSocket.Constructor<AFUNIXSocketAddress> serverSocketConstructor() {
                return AFUNIXServerSocket::new;
            }
            
            public Class<? extends AFSocketChannel<AFUNIXSocketAddress>> socketChannelClass() {
                return AFUNIXSocketChannel.class;
            }
            
            public Class<? extends AFServerSocketChannel<AFUNIXSocketAddress>> serverSocketChannelClass() {
                return AFUNIXServerSocketChannel.class;
            }
            
            public Class<? extends AFDatagramSocket<AFUNIXSocketAddress>> datagramSocketClass() {
                return AFUNIXDatagramSocket.class;
            }
            
            public AFDatagramSocket.Constructor<AFUNIXSocketAddress> datagramSocketConstructor() {
                return AFUNIXDatagramSocket::new;
            }
            
            public Class<? extends AFDatagramChannel<AFUNIXSocketAddress>> datagramChannelClass() {
                return AFUNIXDatagramChannel.class;
            }
        });
    }
}
