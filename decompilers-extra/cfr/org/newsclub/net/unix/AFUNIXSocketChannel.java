/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketException;
import org.newsclub.net.unix.AFSocketChannel;
import org.newsclub.net.unix.AFUNIXSelectorProvider;
import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import org.newsclub.net.unix.AFUNIXSocketCredentials;
import org.newsclub.net.unix.AFUNIXSocketExtensions;

public final class AFUNIXSocketChannel
extends AFSocketChannel<AFUNIXSocketAddress>
implements AFUNIXSocketExtensions {
    AFUNIXSocketChannel(AFUNIXSocket socket) {
        super(socket, AFUNIXSelectorProvider.getInstance());
    }

    public static AFUNIXSocketChannel open() throws IOException {
        return (AFUNIXSocketChannel)AFSocketChannel.open(AFUNIXSocket::newLenientInstance);
    }

    public static AFUNIXSocketChannel open(SocketAddress remote) throws IOException {
        return (AFUNIXSocketChannel)AFSocketChannel.open(AFUNIXSocket::newLenientInstance, remote);
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

