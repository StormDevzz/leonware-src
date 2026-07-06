package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFUNIXSocketChannel.class */
public final class AFUNIXSocketChannel extends AFSocketChannel<AFUNIXSocketAddress> implements AFUNIXSocketExtensions {
    AFUNIXSocketChannel(AFUNIXSocket socket) {
        super(socket, AFUNIXSelectorProvider.getInstance());
    }

    public static AFUNIXSocketChannel open() throws IOException {
        return (AFUNIXSocketChannel) AFSocketChannel.open(AFUNIXSocket::newLenientInstance);
    }

    public static AFUNIXSocketChannel open(SocketAddress remote) throws IOException {
        return (AFUNIXSocketChannel) AFSocketChannel.open(AFUNIXSocket::newLenientInstance, remote);
    }

    @Override // org.newsclub.net.unix.AFUNIXSocketExtensions
    public FileDescriptor[] getReceivedFileDescriptors() throws IOException {
        return ((AFUNIXSocketExtensions) getAFSocket()).getReceivedFileDescriptors();
    }

    @Override // org.newsclub.net.unix.AFUNIXSocketExtensions
    public void clearReceivedFileDescriptors() {
        ((AFUNIXSocketExtensions) getAFSocket()).clearReceivedFileDescriptors();
    }

    @Override // org.newsclub.net.unix.AFUNIXSocketExtensions
    public void setOutboundFileDescriptors(FileDescriptor... fdescs) throws IOException {
        if (fdescs != null && fdescs.length > 0 && !isConnected()) {
            throw new SocketException("Not connected");
        }
        ((AFUNIXSocketExtensions) getAFSocket()).setOutboundFileDescriptors(fdescs);
    }

    @Override // org.newsclub.net.unix.AFUNIXSocketExtensions
    public boolean hasOutboundFileDescriptors() {
        return ((AFUNIXSocketExtensions) getAFSocket()).hasOutboundFileDescriptors();
    }

    @Override // org.newsclub.net.unix.AFUNIXSocketExtensions
    public AFUNIXSocketCredentials getPeerCredentials() throws IOException {
        return ((AFUNIXSocketExtensions) getAFSocket()).getPeerCredentials();
    }
}
