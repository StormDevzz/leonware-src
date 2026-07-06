// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.net.SocketException;
import java.io.IOException;
import java.io.FileDescriptor;

class AFUNIXSocketImpl extends AFSocketImpl<AFUNIXSocketAddress>
{
    protected AFUNIXSocketImpl(final FileDescriptor fdObj) {
        super(AFUNIXSocketAddress.AF_UNIX, fdObj);
    }
    
    AFUNIXSocketCredentials getPeerCredentials() throws IOException {
        return NativeUnixSocket.peerCredentials(this.fd, new AFUNIXSocketCredentials());
    }
    
    final FileDescriptor[] getReceivedFileDescriptors() {
        return this.ancillaryDataSupport.getReceivedFileDescriptors();
    }
    
    final void clearReceivedFileDescriptors() {
        this.ancillaryDataSupport.clearReceivedFileDescriptors();
    }
    
    final void receiveFileDescriptors(final int[] fds) throws IOException {
        this.ancillaryDataSupport.receiveFileDescriptors(fds);
    }
    
    final void setOutboundFileDescriptors(final FileDescriptor... fdescs) throws IOException {
        this.ancillaryDataSupport.setOutboundFileDescriptors(fdescs);
    }
    
    final boolean hasOutboundFileDescriptors() {
        return this.ancillaryDataSupport.hasOutboundFileDescriptors();
    }
    
    static final class Lenient extends AFUNIXSocketImpl
    {
        Lenient(final FileDescriptor fdObj) throws SocketException {
            super(fdObj);
        }
        
        @Override
        public void setOption(final int optID, final Object value) throws SocketException {
            super.setOptionLenient(optID, value);
        }
        
        @Override
        public Object getOption(final int optID) throws SocketException {
            return super.getOptionLenient(optID);
        }
    }
}
