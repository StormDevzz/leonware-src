/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.File;
import java.io.FileNotFoundException;
import org.newsclub.net.unix.AFSocketAddress;

final class SentinelSocketAddress
extends AFSocketAddress {
    private static final long serialVersionUID = 1L;

    SentinelSocketAddress(int port) {
        super(SentinelSocketAddress.class, port);
    }

    @Override
    public boolean hasFilename() {
        return false;
    }

    @Override
    public File getFile() throws FileNotFoundException {
        throw new FileNotFoundException();
    }
}

