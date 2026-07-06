// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.io.Closeable;

public interface AFSomeSocketChannel extends Closeable, FileDescriptorAccess, AFSomeSocketThing
{
    boolean isBlocking();
    
    SelectableChannel configureBlocking(final boolean p0) throws IOException;
}
