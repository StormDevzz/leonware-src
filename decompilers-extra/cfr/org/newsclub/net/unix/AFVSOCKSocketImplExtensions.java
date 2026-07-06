/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.IOException;
import org.newsclub.net.unix.AFSocketImplExtensions;
import org.newsclub.net.unix.AFVSOCKSocketAddress;
import org.newsclub.net.unix.AncillaryDataSupport;
import org.newsclub.net.unix.NativeUnixSocket;

public final class AFVSOCKSocketImplExtensions
implements AFSocketImplExtensions<AFVSOCKSocketAddress> {
    private final AncillaryDataSupport ancillaryDataSupport;

    AFVSOCKSocketImplExtensions(AncillaryDataSupport ancillaryDataSupport) {
        this.ancillaryDataSupport = ancillaryDataSupport;
    }

    public int getLocalCID() throws IOException {
        return NativeUnixSocket.vsockGetLocalCID();
    }
}

