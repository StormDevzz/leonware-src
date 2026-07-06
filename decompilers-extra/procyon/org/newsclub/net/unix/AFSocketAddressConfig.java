// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.util.Set;
import java.net.SocketException;
import java.net.URI;

public abstract class AFSocketAddressConfig<A extends AFSocketAddress>
{
    protected AFSocketAddressConfig() {
    }
    
    protected abstract A parseURI(final URI p0, final int p1) throws SocketException;
    
    protected abstract AFSocketAddress.AFSocketAddressConstructor<A> addressConstructor();
    
    protected abstract String selectorProviderClassname();
    
    protected abstract Set<String> uriSchemes();
}
