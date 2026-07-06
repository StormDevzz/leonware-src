package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.ProtocolFamily;
import java.net.SocketException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFUNIXDatagramChannel.class */
public final class AFUNIXDatagramChannel extends AFDatagramChannel<AFUNIXSocketAddress> implements AFUNIXSocketExtensions {
    AFUNIXDatagramChannel(AFUNIXDatagramSocket socket) {
        super(AFUNIXSelectorProvider.getInstance(), socket);
    }

    public static AFUNIXDatagramChannel open() throws IOException {
        return AFUNIXSelectorProvider.provider().openDatagramChannel();
    }

    public static AFUNIXDatagramChannel open(ProtocolFamily family) throws IOException {
        return AFUNIXSelectorProvider.provider().openDatagramChannel(family);
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
