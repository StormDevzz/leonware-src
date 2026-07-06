/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jdt.annotation.Nullable
 */
package org.newsclub.net.unix;

import java.net.SocketAddress;
import org.eclipse.jdt.annotation.Nullable;
import org.newsclub.net.unix.AFSomeSocketThing;

public interface AFSomeSocket
extends AFSomeSocketThing {
    public @Nullable SocketAddress getRemoteSocketAddress();
}

