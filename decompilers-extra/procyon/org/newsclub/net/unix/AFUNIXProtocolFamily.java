// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.IOException;

public enum AFUNIXProtocolFamily implements AFProtocolFamily
{
    UNIX;
    
    @Override
    public AFDatagramChannel<?> openDatagramChannel() throws IOException {
        return AFUNIXDatagramChannel.open();
    }
    
    @Override
    public AFServerSocketChannel<?> openServerSocketChannel() throws IOException {
        return AFUNIXServerSocketChannel.open();
    }
    
    @Override
    public AFSocketChannel<?> openSocketChannel() throws IOException {
        return AFUNIXSocketChannel.open();
    }
}
