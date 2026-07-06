package org.newsclub.net.unix;

import java.io.File;
import java.io.FileNotFoundException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/SentinelSocketAddress.class */
final class SentinelSocketAddress extends AFSocketAddress {
    private static final long serialVersionUID = 1;

    SentinelSocketAddress(int port) {
        super(SentinelSocketAddress.class, port);
    }

    @Override // org.newsclub.net.unix.AFSocketAddress
    public boolean hasFilename() {
        return false;
    }

    @Override // org.newsclub.net.unix.AFSocketAddress
    public File getFile() throws FileNotFoundException {
        throw new FileNotFoundException();
    }
}
