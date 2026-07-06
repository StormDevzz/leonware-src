// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.IOException;

public final class AFVSOCKSocketImplExtensions implements AFSocketImplExtensions<AFVSOCKSocketAddress>
{
    private final AncillaryDataSupport ancillaryDataSupport;
    
    AFVSOCKSocketImplExtensions(final AncillaryDataSupport ancillaryDataSupport) {
        this.ancillaryDataSupport = ancillaryDataSupport;
    }
    
    public int getLocalCID() throws IOException {
        return NativeUnixSocket.vsockGetLocalCID();
    }
}
