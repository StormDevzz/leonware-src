/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.IOException;
import java.net.ProtocolFamily;
import org.newsclub.net.unix.AFDatagramChannel;
import org.newsclub.net.unix.AFServerSocketChannel;
import org.newsclub.net.unix.AFSocketChannel;

public interface AFProtocolFamily
extends ProtocolFamily {
    public AFDatagramChannel<?> openDatagramChannel() throws IOException;

    public AFServerSocketChannel<?> openServerSocketChannel() throws IOException;

    public AFSocketChannel<?> openSocketChannel() throws IOException;
}

