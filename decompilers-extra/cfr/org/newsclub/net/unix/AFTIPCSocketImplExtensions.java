/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.newsclub.net.unix.AFSocketImplExtensions;
import org.newsclub.net.unix.AFTIPCSocketAddress;
import org.newsclub.net.unix.AncillaryDataSupport;
import org.newsclub.net.unix.NativeUnixSocket;

public final class AFTIPCSocketImplExtensions
implements AFSocketImplExtensions<AFTIPCSocketAddress> {
    private final AncillaryDataSupport ancillaryDataSupport;

    AFTIPCSocketImplExtensions(AncillaryDataSupport ancillaryDataSupport) {
        this.ancillaryDataSupport = ancillaryDataSupport;
    }

    public int[] getTIPCErrInfo() {
        return this.ancillaryDataSupport.getTIPCErrorInfo();
    }

    public int[] getTIPCDestName() {
        return this.ancillaryDataSupport.getTIPCDestName();
    }

    public byte[] getTIPCNodeId(int peer) throws IOException {
        return NativeUnixSocket.tipcGetNodeId(peer);
    }

    public String getTIPCLinkName(int peer, int bearerId) throws IOException {
        byte[] name = NativeUnixSocket.tipcGetLinkName(peer, bearerId);
        return name == null ? null : new String(name, StandardCharsets.UTF_8);
    }
}

