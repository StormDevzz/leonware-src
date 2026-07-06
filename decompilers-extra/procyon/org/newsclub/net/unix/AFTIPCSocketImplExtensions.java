// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.nio.charset.StandardCharsets;
import java.io.IOException;

public final class AFTIPCSocketImplExtensions implements AFSocketImplExtensions<AFTIPCSocketAddress>
{
    private final AncillaryDataSupport ancillaryDataSupport;
    
    AFTIPCSocketImplExtensions(final AncillaryDataSupport ancillaryDataSupport) {
        this.ancillaryDataSupport = ancillaryDataSupport;
    }
    
    public int[] getTIPCErrInfo() {
        return this.ancillaryDataSupport.getTIPCErrorInfo();
    }
    
    public int[] getTIPCDestName() {
        return this.ancillaryDataSupport.getTIPCDestName();
    }
    
    public byte[] getTIPCNodeId(final int peer) throws IOException {
        return NativeUnixSocket.tipcGetNodeId(peer);
    }
    
    public String getTIPCLinkName(final int peer, final int bearerId) throws IOException {
        final byte[] name = NativeUnixSocket.tipcGetLinkName(peer, bearerId);
        return (name == null) ? null : new String(name, StandardCharsets.UTF_8);
    }
}
