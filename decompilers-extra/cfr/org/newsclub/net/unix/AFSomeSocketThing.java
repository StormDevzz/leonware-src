/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jdt.annotation.Nullable
 */
package org.newsclub.net.unix;

import java.io.Closeable;
import java.net.SocketAddress;
import org.eclipse.jdt.annotation.Nullable;
import org.newsclub.net.unix.FileDescriptorAccess;

public interface AFSomeSocketThing
extends Closeable,
FileDescriptorAccess {
    public @Nullable SocketAddress getLocalSocketAddress();

    public void setShutdownOnClose(boolean var1);
}

