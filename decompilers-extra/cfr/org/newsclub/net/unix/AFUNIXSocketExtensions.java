/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import org.newsclub.net.unix.AFSocketExtensions;
import org.newsclub.net.unix.AFUNIXSocketCredentials;

public interface AFUNIXSocketExtensions
extends AFSocketExtensions {
    public FileDescriptor[] getReceivedFileDescriptors() throws IOException;

    public void clearReceivedFileDescriptors();

    public void setOutboundFileDescriptors(FileDescriptor ... var1) throws IOException;

    public boolean hasOutboundFileDescriptors();

    public AFUNIXSocketCredentials getPeerCredentials() throws IOException;
}

