package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/FileDescriptorAccess.class */
public interface FileDescriptorAccess {
    FileDescriptor getFileDescriptor() throws IOException;
}
