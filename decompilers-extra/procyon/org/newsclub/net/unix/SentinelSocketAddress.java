// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.FileNotFoundException;
import java.io.File;

final class SentinelSocketAddress extends AFSocketAddress
{
    private static final long serialVersionUID = 1L;
    
    SentinelSocketAddress(final int port) {
        super(SentinelSocketAddress.class, port);
    }
    
    @Override
    public boolean hasFilename() {
        return false;
    }
    
    @Override
    public File getFile() throws FileNotFoundException {
        throw new FileNotFoundException();
    }
}
