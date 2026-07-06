// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.net.SocketException;
import java.nio.file.Path;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

public final class AFUNIXServerSocket extends AFServerSocket<AFUNIXSocketAddress>
{
    protected AFUNIXServerSocket() throws IOException {
    }
    
    AFUNIXServerSocket(final FileDescriptor fdObj) throws IOException {
        super(fdObj);
    }
    
    @Override
    protected AFUNIXServerSocketChannel newChannel() {
        return new AFUNIXServerSocketChannel(this);
    }
    
    @Override
    public AFUNIXServerSocketChannel getChannel() {
        return (AFUNIXServerSocketChannel)super.getChannel();
    }
    
    public static AFUNIXServerSocket newInstance() throws IOException {
        return (AFUNIXServerSocket)AFServerSocket.newInstance(AFUNIXServerSocket::new);
    }
    
    static AFUNIXServerSocket newInstance(final FileDescriptor fdObj, final int localPort, final int remotePort) throws IOException {
        return (AFUNIXServerSocket)AFServerSocket.newInstance(AFUNIXServerSocket::new, fdObj, localPort, remotePort);
    }
    
    public static AFUNIXServerSocket bindOn(final AFUNIXSocketAddress addr) throws IOException {
        return (AFUNIXServerSocket)AFServerSocket.bindOn(AFUNIXServerSocket::new, addr);
    }
    
    public static AFUNIXServerSocket bindOn(final AFUNIXSocketAddress addr, final boolean deleteOnClose) throws IOException {
        return (AFUNIXServerSocket)AFServerSocket.bindOn(AFUNIXServerSocket::new, addr, deleteOnClose);
    }
    
    public static AFUNIXServerSocket bindOn(final File path, final boolean deleteOnClose) throws IOException {
        return bindOn(path.toPath(), deleteOnClose);
    }
    
    public static AFUNIXServerSocket bindOn(final Path path, final boolean deleteOnClose) throws IOException {
        return (AFUNIXServerSocket)AFServerSocket.bindOn(AFUNIXServerSocket::new, AFUNIXSocketAddress.of(path), deleteOnClose);
    }
    
    public static AFUNIXServerSocket forceBindOn(final AFUNIXSocketAddress forceAddr) throws IOException {
        return (AFUNIXServerSocket)AFServerSocket.forceBindOn(AFUNIXServerSocket::new, forceAddr);
    }
    
    @Override
    protected AFSocketImpl<AFUNIXSocketAddress> newImpl(final FileDescriptor fdObj) throws SocketException {
        return new AFUNIXSocketImpl(fdObj);
    }
    
    @Override
    protected AFUNIXSocket newSocketInstance() throws IOException {
        return AFUNIXSocket.newInstance();
    }
    
    @Override
    public AFUNIXSocket accept() throws IOException {
        return (AFUNIXSocket)super.accept();
    }
}
