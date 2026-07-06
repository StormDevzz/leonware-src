// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.IOException;
import java.net.SocketAddress;

@FunctionalInterface
public interface SocketAddressFilter
{
    SocketAddress apply(final SocketAddress p0) throws IOException;
}
