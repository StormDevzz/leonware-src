// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.IOException;
import java.util.Objects;
import java.io.FileDescriptor;

public final class AFSYSTEMSocketImplExtensions implements AFSocketImplExtensions<AFSYSTEMSocketAddress>
{
    AFSYSTEMSocketImplExtensions(final AncillaryDataSupport ancillaryDataSupport) {
    }
    
    public int getKernelControlId(final FileDescriptor fd, final String name) throws IOException {
        return NativeUnixSocket.systemResolveCtlId(fd, Objects.requireNonNull(name));
    }
}
