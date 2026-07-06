/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.IOException;
import org.newsclub.net.unix.AFDatagramChannel;
import org.newsclub.net.unix.AFProtocolFamily;
import org.newsclub.net.unix.AFServerSocketChannel;
import org.newsclub.net.unix.AFSocketChannel;
import org.newsclub.net.unix.AFUNIXDatagramChannel;
import org.newsclub.net.unix.AFUNIXServerSocketChannel;
import org.newsclub.net.unix.AFUNIXSocketChannel;

public enum AFUNIXProtocolFamily implements AFProtocolFamily
{
    UNIX;


    @Override
    public AFDatagramChannel<?> openDatagramChannel() throws IOException {
        return AFUNIXDatagramChannel.open();
    }

    @Override
    public AFServerSocketChannel<?> openServerSocketChannel() throws IOException {
        return AFUNIXServerSocketChannel.open();
    }

    @Override
    public AFSocketChannel<?> openSocketChannel() throws IOException {
        return AFUNIXSocketChannel.open();
    }
}

