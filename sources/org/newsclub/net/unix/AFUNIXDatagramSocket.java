package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.SocketException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFUNIXDatagramSocket.class */
public final class AFUNIXDatagramSocket extends AFDatagramSocket<AFUNIXSocketAddress> implements AFUNIXSocketExtensions {
    AFUNIXDatagramSocket(FileDescriptor fd) throws IOException {
        super(new AFUNIXDatagramSocketImpl(fd));
    }

    private AFUNIXDatagramSocket(FileDescriptor fd, AFSocketType socketType) throws IOException {
        super(new AFUNIXDatagramSocketImpl(fd, socketType));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.newsclub.net.unix.AFDatagramSocket
    public AFUNIXDatagramChannel newChannel() {
        return new AFUNIXDatagramChannel(this);
    }

    public static AFUNIXDatagramSocket newInstance() throws IOException {
        return (AFUNIXDatagramSocket) newInstance(AFUNIXDatagramSocket::new);
    }

    public static AFUNIXDatagramSocket newInstance(AFSocketType socketType) throws IOException {
        return (AFUNIXDatagramSocket) newInstance(fd -> {
            return new AFUNIXDatagramSocket(fd, socketType);
        });
    }

    static AFUNIXDatagramSocket newInstance(FileDescriptor fdObj, int localPort, int remotePort) throws IOException {
        return (AFUNIXDatagramSocket) newInstance(AFUNIXDatagramSocket::new, fdObj, localPort, remotePort);
    }

    @Override // org.newsclub.net.unix.AFDatagramSocket, java.net.DatagramSocket
    public AFUNIXDatagramChannel getChannel() {
        return (AFUNIXDatagramChannel) super.getChannel();
    }

    @Override // org.newsclub.net.unix.AFUNIXSocketExtensions
    public FileDescriptor[] getReceivedFileDescriptors() throws IOException {
        return getAncillaryDataSupport().getReceivedFileDescriptors();
    }

    @Override // org.newsclub.net.unix.AFUNIXSocketExtensions
    public void clearReceivedFileDescriptors() {
        getAncillaryDataSupport().clearReceivedFileDescriptors();
    }

    @Override // org.newsclub.net.unix.AFUNIXSocketExtensions
    public void setOutboundFileDescriptors(FileDescriptor... fdescs) throws IOException {
        if (fdescs != null && fdescs.length > 0 && !isConnected()) {
            throw new SocketException("Not connected");
        }
        getAncillaryDataSupport().setOutboundFileDescriptors(fdescs);
    }

    @Override // org.newsclub.net.unix.AFUNIXSocketExtensions
    public boolean hasOutboundFileDescriptors() {
        return getAncillaryDataSupport().hasOutboundFileDescriptors();
    }

    @Override // org.newsclub.net.unix.AFUNIXSocketExtensions
    public AFUNIXSocketCredentials getPeerCredentials() throws IOException {
        if (isClosed() || !isConnected()) {
            throw new SocketException("Not connected");
        }
        return ((AFUNIXDatagramSocketImpl) getAFImpl()).getPeerCredentials();
    }

    @Override // org.newsclub.net.unix.AFDatagramSocket
    protected AFDatagramSocket<AFUNIXSocketAddress> newDatagramSocketInstance() throws IOException {
        return new AFUNIXDatagramSocket(null);
    }
}
