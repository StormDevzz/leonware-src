// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.nio.channels.DatagramChannel;
import java.nio.channels.Pipe;
import java.nio.channels.SocketChannel;
import java.nio.channels.ServerSocketChannel;
import java.net.StandardProtocolFamily;
import java.util.Objects;
import java.net.SocketAddress;
import java.nio.channels.spi.AbstractSelector;
import java.net.ProtocolFamily;
import java.io.IOException;
import java.nio.channels.spi.SelectorProvider;

public abstract class AFSelectorProvider<A extends AFSocketAddress> extends SelectorProviderShim
{
    private static final SelectorProvider AF_PROVIDER;
    
    protected AFSelectorProvider() {
    }
    
    private AFPipe newPipe(final boolean selectable) throws IOException {
        return new AFPipe(this, selectable);
    }
    
    protected abstract <Y extends AFSomeSocket> AFSocketPair<Y> newSocketPair(final Y p0, final Y p1);
    
    protected abstract AFSocket<A> newSocket() throws IOException;
    
    protected abstract ProtocolFamily protocolFamily();
    
    protected abstract AFAddressFamily<A> addressFamily();
    
    protected final int domainId() {
        return this.addressFamily().getDomain();
    }
    
    public AFSocketPair<? extends AFSocketChannel<A>> openSocketChannelPair() throws IOException {
        final AFSocketChannel<A> s1 = this.openSocketChannel();
        final AFSocketChannel<A> s2 = this.openSocketChannel();
        NativeUnixSocket.socketPair(this.domainId(), 1, s1.getAFCore().fd, s2.getAFCore().fd);
        s1.socket().internalDummyConnect();
        s2.socket().internalDummyConnect();
        return this.newSocketPair(s1, s2);
    }
    
    public AFSocketPair<? extends AFDatagramChannel<A>> openDatagramChannelPair() throws IOException {
        return this.openDatagramChannelPair(AFSocketType.SOCK_DGRAM);
    }
    
    public AFSocketPair<? extends AFDatagramChannel<A>> openDatagramChannelPair(final AFSocketType type) throws IOException {
        final ProtocolFamily pf = this.protocolFamily();
        final AFDatagramChannel<A> s1 = this.openDatagramChannel(pf);
        final AFDatagramChannel<A> s2 = this.openDatagramChannel(pf);
        NativeUnixSocket.socketPair(this.domainId(), type.getId(), s1.getAFCore().fd, s2.getAFCore().fd);
        s1.socket().internalDummyBind();
        s2.socket().internalDummyBind();
        s1.socket().internalDummyConnect();
        s2.socket().internalDummyConnect();
        return this.newSocketPair(s1, s2);
    }
    
    @Override
    public abstract AFDatagramChannel<A> openDatagramChannel() throws IOException;
    
    public abstract AFDatagramChannel<A> openDatagramChannel(final AFSocketType p0) throws IOException;
    
    @Override
    public final AFPipe openPipe() throws IOException {
        return this.newPipe(false);
    }
    
    final AFPipe openSelectablePipe() throws IOException {
        return this.newPipe(true);
    }
    
    @Override
    public final AbstractSelector openSelector() throws IOException {
        return new AFSelector(this);
    }
    
    @Override
    public abstract AFServerSocketChannel<A> openServerSocketChannel() throws IOException;
    
    public abstract AFServerSocketChannel<A> openServerSocketChannel(final SocketAddress p0) throws IOException;
    
    @Override
    public AFSocketChannel<A> openSocketChannel() throws IOException {
        return this.newSocket().getChannel();
    }
    
    public abstract AFSocketChannel<A> openSocketChannel(final SocketAddress p0) throws IOException;
    
    @Override
    public AFSocketChannel<A> openSocketChannel(final ProtocolFamily family) throws IOException {
        Objects.requireNonNull(family);
        if (this.protocolFamily().equals(family) || (family instanceof StandardProtocolFamily && this.protocolFamily().name().equals(family.name()))) {
            return this.openSocketChannel();
        }
        throw new UnsupportedOperationException("Protocol family not supported");
    }
    
    @Override
    public AFServerSocketChannel<A> openServerSocketChannel(final ProtocolFamily family) throws IOException {
        Objects.requireNonNull(family);
        if (this.protocolFamily().equals(family) || (family instanceof StandardProtocolFamily && this.protocolFamily().name().equals(family.name()))) {
            return this.openServerSocketChannel();
        }
        throw new UnsupportedOperationException("Protocol family not supported");
    }
    
    @Override
    public AFDatagramChannel<A> openDatagramChannel(final ProtocolFamily family) throws IOException {
        Objects.requireNonNull(family);
        if (this.protocolFamily().equals(family) || (family instanceof StandardProtocolFamily && this.protocolFamily().name().equals(family.name()))) {
            return this.openDatagramChannel();
        }
        throw new UnsupportedOperationException("Protocol family not supported");
    }
    
    public static SelectorProvider provider() {
        return AFSelectorProvider.AF_PROVIDER;
    }
    
    static {
        AF_PROVIDER = new SelectorProviderShim() {
            @Override
            public SocketChannel openSocketChannel() throws IOException {
                throw new UnsupportedOperationException("Use openSocketChannel(ProtocolFamily) or a specific AFSelectorProvider subclass");
            }
            
            @Override
            public ServerSocketChannel openServerSocketChannel() throws IOException {
                throw new UnsupportedOperationException("Use openServerSocketChannel(ProtocolFamily) or a specific AFSelectorProvider subclass");
            }
            
            @Override
            public DatagramChannel openDatagramChannel() throws IOException {
                throw new UnsupportedOperationException("Use openDatagramChannel(ProtocolFamily) or a specific AFSelectorProvider subclass");
            }
            
            @Override
            public AbstractSelector openSelector() throws IOException {
                throw new UnsupportedOperationException("Use a specific AFSelectorProvider subclass");
            }
            
            @Override
            public Pipe openPipe() throws IOException {
                throw new UnsupportedOperationException("Use a specific AFSelectorProvider subclass");
            }
            
            @Override
            public DatagramChannel openDatagramChannel(final ProtocolFamily family) throws IOException {
                Objects.requireNonNull(family);
                if (family instanceof AFProtocolFamily) {
                    return ((AFProtocolFamily)family).openDatagramChannel();
                }
                throw new UnsupportedOperationException("Unsupported protocol family");
            }
            
            @Override
            public SocketChannel openSocketChannel(final ProtocolFamily family) throws IOException {
                Objects.requireNonNull(family);
                if (family instanceof AFProtocolFamily) {
                    return ((AFProtocolFamily)family).openSocketChannel();
                }
                throw new UnsupportedOperationException("Unsupported protocol family");
            }
            
            @Override
            public ServerSocketChannel openServerSocketChannel(final ProtocolFamily family) throws IOException {
                Objects.requireNonNull(family);
                if (family instanceof AFProtocolFamily) {
                    return ((AFProtocolFamily)family).openServerSocketChannel();
                }
                throw new UnsupportedOperationException("Unsupported protocol family");
            }
        };
    }
}
