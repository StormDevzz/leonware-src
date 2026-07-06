// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.nio.channels.DatagramChannel;
import java.net.SocketException;
import java.io.IOException;
import java.io.FileDescriptor;

public final class AFUNIXDatagramSocket extends AFDatagramSocket<AFUNIXSocketAddress> implements AFUNIXSocketExtensions
{
    AFUNIXDatagramSocket(final FileDescriptor fd) throws IOException {
        super(new AFUNIXDatagramSocketImpl(fd));
    }
    
    private AFUNIXDatagramSocket(final FileDescriptor fd, final AFSocketType socketType) throws IOException {
        super(new AFUNIXDatagramSocketImpl(fd, socketType));
    }
    
    @Override
    protected AFUNIXDatagramChannel newChannel() {
        return new AFUNIXDatagramChannel(this);
    }
    
    public static AFUNIXDatagramSocket newInstance() throws IOException {
        return (AFUNIXDatagramSocket)AFDatagramSocket.newInstance(AFUNIXDatagramSocket::new);
    }
    
    public static AFUNIXDatagramSocket newInstance(final AFSocketType socketType) throws IOException {
        return (AFUNIXDatagramSocket)AFDatagramSocket.newInstance(fd -> new AFUNIXDatagramSocket(fd, socketType));
    }
    
    static AFUNIXDatagramSocket newInstance(final FileDescriptor fdObj, final int localPort, final int remotePort) throws IOException {
        return (AFUNIXDatagramSocket)AFDatagramSocket.newInstance(AFUNIXDatagramSocket::new, fdObj, localPort, remotePort);
    }
    
    @Override
    public AFUNIXDatagramChannel getChannel() {
        return (AFUNIXDatagramChannel)super.getChannel();
    }
    
    @Override
    public FileDescriptor[] getReceivedFileDescriptors() throws IOException {
        return this.getAncillaryDataSupport().getReceivedFileDescriptors();
    }
    
    @Override
    public void clearReceivedFileDescriptors() {
        this.getAncillaryDataSupport().clearReceivedFileDescriptors();
    }
    
    @Override
    public void setOutboundFileDescriptors(final FileDescriptor... fdescs) throws IOException {
        if (fdescs != null && fdescs.length > 0 && !this.isConnected()) {
            throw new SocketException("Not connected");
        }
        this.getAncillaryDataSupport().setOutboundFileDescriptors(fdescs);
    }
    
    @Override
    public boolean hasOutboundFileDescriptors() {
        return this.getAncillaryDataSupport().hasOutboundFileDescriptors();
    }
    
    @Override
    public AFUNIXSocketCredentials getPeerCredentials() throws IOException {
        if (this.isClosed() || !this.isConnected()) {
            throw new SocketException("Not connected");
        }
        return ((AFUNIXDatagramSocketImpl)this.getAFImpl()).getPeerCredentials();
    }
    
    @Override
    protected AFDatagramSocket<AFUNIXSocketAddress> newDatagramSocketInstance() throws IOException {
        return new AFUNIXDatagramSocket((FileDescriptor)null);
    }
}
