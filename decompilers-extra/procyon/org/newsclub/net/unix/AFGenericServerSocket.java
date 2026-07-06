// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.net.SocketException;
import java.io.IOException;
import java.io.FileDescriptor;

final class AFGenericServerSocket extends AFServerSocket<AFGenericSocketAddress>
{
    AFGenericServerSocket(final FileDescriptor fdObj) throws IOException {
        super(fdObj);
    }
    
    @Override
    protected AFServerSocketChannel<AFGenericSocketAddress> newChannel() {
        return new AFGenericServerSocketChannel(this);
    }
    
    @Override
    public AFGenericServerSocketChannel getChannel() {
        return (AFGenericServerSocketChannel)super.getChannel();
    }
    
    public static AFGenericServerSocket newInstance() throws IOException {
        return (AFGenericServerSocket)AFServerSocket.newInstance(AFGenericServerSocket::new);
    }
    
    static AFGenericServerSocket newInstance(final FileDescriptor fdObj, final int localPort, final int remotePort) throws IOException {
        return (AFGenericServerSocket)AFServerSocket.newInstance(AFGenericServerSocket::new, fdObj, localPort, remotePort);
    }
    
    public static AFGenericServerSocket bindOn(final AFGenericSocketAddress addr) throws IOException {
        return (AFGenericServerSocket)AFServerSocket.bindOn(AFGenericServerSocket::new, addr);
    }
    
    public static AFGenericServerSocket bindOn(final AFGenericSocketAddress addr, final boolean deleteOnClose) throws IOException {
        return (AFGenericServerSocket)AFServerSocket.bindOn(AFGenericServerSocket::new, addr, deleteOnClose);
    }
    
    public static AFGenericServerSocket forceBindOn(final AFGenericSocketAddress forceAddr) throws IOException {
        return (AFGenericServerSocket)AFServerSocket.forceBindOn(AFGenericServerSocket::new, forceAddr);
    }
    
    @Override
    protected AFSocketImpl<AFGenericSocketAddress> newImpl(final FileDescriptor fdObj) throws SocketException {
        return new AFGenericSocketImpl(fdObj);
    }
    
    @Override
    protected AFSocket<AFGenericSocketAddress> newSocketInstance() throws IOException {
        return AFGenericSocket.newInstance();
    }
    
    @Override
    public AFGenericSocket accept() throws IOException {
        return (AFGenericSocket)super.accept();
    }
}
