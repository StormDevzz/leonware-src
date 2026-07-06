/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;

public interface FileDescriptorAccess {
    public FileDescriptor getFileDescriptor() throws IOException;
}

