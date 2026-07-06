// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.IOException;
import java.io.FileDescriptor;

final class AFGenericDatagramSocketImpl extends AFDatagramSocketImpl<AFGenericSocketAddress>
{
    AFGenericDatagramSocketImpl(final FileDescriptor fd, final AFSocketType socketType) throws IOException {
        super(AFGenericSelectorProvider.AF_GENERIC, fd, socketType);
    }
}
