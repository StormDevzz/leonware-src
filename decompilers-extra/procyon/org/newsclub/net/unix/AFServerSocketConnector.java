// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.IOException;

public interface AFServerSocketConnector<A extends AFSocketAddress, T extends AFSocketAddress>
{
    AFServerSocket<? extends T> bind(final A p0) throws IOException;
}
