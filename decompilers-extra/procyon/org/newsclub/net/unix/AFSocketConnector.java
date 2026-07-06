// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.IOException;

public interface AFSocketConnector<A extends AFSocketAddress, T extends AFSocketAddress>
{
    AFSocket<? extends T> connect(final A p0) throws IOException;
}
