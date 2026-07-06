// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

public interface AFSocketExtensions
{
    int getAncillaryReceiveBufferSize();
    
    void setAncillaryReceiveBufferSize(final int p0);
    
    void ensureAncillaryReceiveBufferSize(final int p0);
}
