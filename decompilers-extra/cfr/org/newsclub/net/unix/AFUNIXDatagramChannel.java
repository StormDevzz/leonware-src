/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.ProtocolFamily;
import java.net.SocketException;
import org.newsclub.net.unix.AFDatagramChannel;
import org.newsclub.net.unix.AFUNIXDatagramSocket;
import org.newsclub.net.unix.AFUNIXSelectorProvider;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import org.newsclub.net.unix.AFUNIXSocketCredentials;
import org.newsclub.net.unix.AFUNIXSocketExtensions;

public final class AFUNIXDatagramChannel
extends AFDatagramChannel<AFUNIXSocketAddress>
implements AFUNIXSocketExtensions {
    AFUNIXDatagramChannel(AFUNIXDatagramSocket socket) {
        super(AFUNIXSelectorProvider.getInstance(), socket);
    }

    public static AFUNIXDatagramChannel open() throws IOException {
        return AFUNIXSelectorProvider.provider().openDatagramChannel();
    }

    public static AFUNIXDatagramChannel open(ProtocolFamily family) throws IOException {
        return AFUNIXSelectorProvider.provider().openDatagramChannel(family);
    }

    @Override
    public FileDescriptor[] getReceivedFileDescriptors() throws IOException {
        return ((AFUNIXSocketExtensions)((Object)this.getAFSocket())).getReceivedFileDescriptors();
    }

    @Override
    public void clearReceivedFileDescriptors() {
        ((AFUNIXSocketExtensions)((Object)this.getAFSocket())).clearReceivedFileDescriptors();
    }

    @Override
    public void setOutboundFileDescriptors(FileDescriptor ... fdescs) throws IOException {
        if (fdescs != null && fdescs.length > 0 && !this.isConnected()) {
            throw new SocketException("Not connected");
        }
        ((AFUNIXSocketExtensions)((Object)this.getAFSocket())).setOutboundFileDescriptors(fdescs);
    }

    @Override
    public boolean hasOutboundFileDescriptors() {
        return ((AFUNIXSocketExtensions)((Object)this.getAFSocket())).hasOutboundFileDescriptors();
    }

    @Override
    public AFUNIXSocketCredentials getPeerCredentials() throws IOException {
        return ((AFUNIXSocketExtensions)((Object)this.getAFSocket())).getPeerCredentials();
    }
}

