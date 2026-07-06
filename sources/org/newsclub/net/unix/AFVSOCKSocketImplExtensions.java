package org.newsclub.net.unix;

import java.io.IOException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFVSOCKSocketImplExtensions.class */
public final class AFVSOCKSocketImplExtensions implements AFSocketImplExtensions<AFVSOCKSocketAddress> {
    private final AncillaryDataSupport ancillaryDataSupport;

    AFVSOCKSocketImplExtensions(AncillaryDataSupport ancillaryDataSupport) {
        this.ancillaryDataSupport = ancillaryDataSupport;
    }

    public int getLocalCID() throws IOException {
        return NativeUnixSocket.vsockGetLocalCID();
    }
}
