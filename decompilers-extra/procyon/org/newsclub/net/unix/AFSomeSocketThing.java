// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.net.SocketAddress;
import java.io.Closeable;

public interface AFSomeSocketThing extends Closeable, FileDescriptorAccess
{
    SocketAddress getLocalSocketAddress();
    
    void setShutdownOnClose(final boolean p0);
}
