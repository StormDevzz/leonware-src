// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.net.SocketException;
import java.io.FileDescriptor;
import java.net.ProtocolFamily;
import java.io.IOException;

public final class AFUNIXDatagramChannel extends AFDatagramChannel<AFUNIXSocketAddress> implements AFUNIXSocketExtensions
{
    AFUNIXDatagramChannel(final AFUNIXDatagramSocket socket) {
        super(AFUNIXSelectorProvider.getInstance(), socket);
    }
    
    public static AFUNIXDatagramChannel open() throws IOException {
        return AFUNIXSelectorProvider.provider().openDatagramChannel();
    }
    
    public static AFUNIXDatagramChannel open(final ProtocolFamily family) throws IOException {
        return AFUNIXSelectorProvider.provider().openDatagramChannel(family);
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
