// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.net.SocketException;
import java.io.FileDescriptor;
import java.net.SocketAddress;
import java.io.IOException;

public final class AFUNIXSocketChannel extends AFSocketChannel<AFUNIXSocketAddress> implements AFUNIXSocketExtensions
{
    AFUNIXSocketChannel(final AFUNIXSocket socket) {
        super(socket, AFUNIXSelectorProvider.getInstance());
    }
    
    public static AFUNIXSocketChannel open() throws IOException {
        return (AFUNIXSocketChannel)AFSocketChannel.open(AFUNIXSocket::newLenientInstance);
    }
    
    public static AFUNIXSocketChannel open(final SocketAddress remote) throws IOException {
        return (AFUNIXSocketChannel)AFSocketChannel.open(AFUNIXSocket::newLenientInstance, remote);
    }
    
    @Override
    public FileDescriptor[] getReceivedFileDescriptors() throws IOException {
        return ((AFUNIXSocketExtensions)this.getAFSocket()).getReceivedFileDescriptors();
    }
    
    @Override
    public void clearReceivedFileDescriptors() {
        ((AFUNIXSocketExtensions)this.getAFSocket()).clearReceivedFileDescriptors();
    }
    
    @Override
    public void setOutboundFileDescriptors(final FileDescriptor... fdescs) throws IOException {
        if (fdescs != null && fdescs.length > 0 && !this.isConnected()) {
            throw new SocketException("Not connected");
        }
        ((AFUNIXSocketExtensions)this.getAFSocket()).setOutboundFileDescriptors(fdescs);
    }
    
    @Override
    public boolean hasOutboundFileDescriptors() {
        return ((AFUNIXSocketExtensions)this.getAFSocket()).hasOutboundFileDescriptors();
    }
    
    @Override
    public AFUNIXSocketCredentials getPeerCredentials() throws IOException {
        return ((AFUNIXSocketExtensions)this.getAFSocket()).getPeerCredentials();
    }
}
