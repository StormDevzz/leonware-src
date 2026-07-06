package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFUNIXSocketExtensions.class */
public interface AFUNIXSocketExtensions extends AFSocketExtensions {
    FileDescriptor[] getReceivedFileDescriptors() throws IOException;

    void clearReceivedFileDescriptors();

    void setOutboundFileDescriptors(FileDescriptor... fileDescriptorArr) throws IOException;

    boolean hasOutboundFileDescriptors();

    AFUNIXSocketCredentials getPeerCredentials() throws IOException;
}
