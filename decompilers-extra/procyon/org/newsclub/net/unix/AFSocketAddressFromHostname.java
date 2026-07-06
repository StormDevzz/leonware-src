// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.net.SocketException;
import java.net.SocketAddress;

public interface AFSocketAddressFromHostname<A extends AFSocketAddress>
{
    SocketAddress addressFromHost(final String p0, final int p1) throws SocketException;
    
    default boolean isHostnameSupported(final String host) {
        return host != null;
    }
}
