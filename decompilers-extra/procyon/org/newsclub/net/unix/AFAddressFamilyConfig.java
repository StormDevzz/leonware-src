// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

public abstract class AFAddressFamilyConfig<A extends AFSocketAddress>
{
    protected AFAddressFamilyConfig() {
    }
    
    protected abstract Class<? extends AFSocket<A>> socketClass();
    
    protected abstract AFSocket.Constructor<A> socketConstructor();
    
    protected abstract Class<? extends AFServerSocket<A>> serverSocketClass();
    
    protected abstract AFServerSocket.Constructor<A> serverSocketConstructor();
    
    protected abstract Class<? extends AFSocketChannel<A>> socketChannelClass();
    
    protected abstract Class<? extends AFServerSocketChannel<A>> serverSocketChannelClass();
    
    protected abstract Class<? extends AFDatagramSocket<A>> datagramSocketClass();
    
    protected abstract AFDatagramSocket.Constructor<A> datagramSocketConstructor();
    
    protected abstract Class<? extends AFDatagramChannel<A>> datagramChannelClass();
}
