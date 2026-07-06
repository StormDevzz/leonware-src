// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.nio.channels.DatagramChannel;
import java.io.IOException;
import java.io.FileDescriptor;

final class AFGenericDatagramSocket extends AFDatagramSocket<AFGenericSocketAddress> implements AFGenericSocketExtensions
{
    AFGenericDatagramSocket(final FileDescriptor fd) throws IOException {
        this(fd, AFSocketType.SOCK_DGRAM);
    }
    
    AFGenericDatagramSocket(final FileDescriptor fd, final AFSocketType socketType) throws IOException {
        super(new AFGenericDatagramSocketImpl(fd, socketType));
    }
    
    @Override
    protected AFGenericDatagramChannel newChannel() {
        return new AFGenericDatagramChannel(this);
    }
    
    public static AFGenericDatagramSocket newInstance() throws IOException {
        return (AFGenericDatagramSocket)AFDatagramSocket.newInstance(AFGenericDatagramSocket::new);
    }
    
    public static AFGenericDatagramSocket newInstance(final AFSocketType socketType) throws IOException {
        return (AFGenericDatagramSocket)AFDatagramSocket.newInstance(fd -> new AFGenericDatagramSocket(fd, socketType));
    }
    
    @Override
    public AFGenericDatagramChannel getChannel() {
        return (AFGenericDatagramChannel)super.getChannel();
    }
    
    @Override
    protected AFGenericSocketImplExtensions getImplExtensions() {
        return (AFGenericSocketImplExtensions)super.getImplExtensions();
    }
    
    @Override
    protected AFDatagramSocket<AFGenericSocketAddress> newDatagramSocketInstance() throws IOException {
        return new AFGenericDatagramSocket((FileDescriptor)null);
    }
}
