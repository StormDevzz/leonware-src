// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.net.SocketException;
import java.io.FileDescriptor;

final class AFGenericSocketImpl extends AFSocketImpl<AFGenericSocketAddress>
{
    AFGenericSocketImpl(final FileDescriptor fdObj) {
        super(AFGenericSelectorProvider.AF_GENERIC, fdObj);
    }
    
    @Override
    public Object getOption(final int optID) throws SocketException {
        return this.getOptionLenient(optID);
    }
    
    @Override
    public void setOption(final int optID, final Object value) throws SocketException {
        this.setOptionLenient(optID, value);
    }
}
