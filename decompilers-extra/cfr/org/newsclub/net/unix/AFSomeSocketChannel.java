/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.SelectableChannel;
import org.newsclub.net.unix.AFSomeSocketThing;
import org.newsclub.net.unix.FileDescriptorAccess;

public interface AFSomeSocketChannel
extends Closeable,
FileDescriptorAccess,
AFSomeSocketThing {
    public boolean isBlocking();

    public SelectableChannel configureBlocking(boolean var1) throws IOException;
}

