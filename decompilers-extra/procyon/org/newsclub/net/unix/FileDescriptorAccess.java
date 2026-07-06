// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.IOException;
import java.io.FileDescriptor;

public interface FileDescriptorAccess
{
    FileDescriptor getFileDescriptor() throws IOException;
}
