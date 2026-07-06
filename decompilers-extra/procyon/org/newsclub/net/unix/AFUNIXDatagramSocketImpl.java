// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.IOException;
import java.io.FileDescriptor;

final class AFUNIXDatagramSocketImpl extends AFDatagramSocketImpl<AFUNIXSocketAddress>
{
    AFUNIXDatagramSocketImpl(final FileDescriptor fd) throws IOException {
        this(fd, AFSocketType.SOCK_DGRAM);
    }
    
    AFUNIXDatagramSocketImpl(final FileDescriptor fd, final AFSocketType socketType) throws IOException {
        super(AFUNIXSocketAddress.AF_UNIX, fd, socketType);
    }
    
    AFUNIXSocketCredentials getPeerCredentials() throws IOException {
        return NativeUnixSocket.peerCredentials(this.fd, new AFUNIXSocketCredentials());
    }
}
