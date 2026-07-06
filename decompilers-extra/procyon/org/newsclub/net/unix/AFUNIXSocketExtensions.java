// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.IOException;
import java.io.FileDescriptor;

public interface AFUNIXSocketExtensions extends AFSocketExtensions
{
    FileDescriptor[] getReceivedFileDescriptors() throws IOException;
    
    void clearReceivedFileDescriptors();
    
    void setOutboundFileDescriptors(final FileDescriptor... p0) throws IOException;
    
    boolean hasOutboundFileDescriptors();
    
    AFUNIXSocketCredentials getPeerCredentials() throws IOException;
}
