package org.newsclub.net.unix;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFTIPCSocketImplExtensions.class */
public final class AFTIPCSocketImplExtensions implements AFSocketImplExtensions<AFTIPCSocketAddress> {
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
        if (name == null) {
            return null;
        }
        return new String(name, StandardCharsets.UTF_8);
    }
}
