/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.SocketException;
import org.newsclub.net.unix.AFDatagramSocket;
import org.newsclub.net.unix.AFSocketType;
import org.newsclub.net.unix.AFUNIXDatagramChannel;
import org.newsclub.net.unix.AFUNIXDatagramSocketImpl;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import org.newsclub.net.unix.AFUNIXSocketCredentials;
import org.newsclub.net.unix.AFUNIXSocketExtensions;

public final class AFUNIXDatagramSocket
extends AFDatagramSocket<AFUNIXSocketAddress>
implements AFUNIXSocketExtensions {
    AFUNIXDatagramSocket(FileDescriptor fd) throws IOException {
        super(new AFUNIXDatagramSocketImpl(fd));
    }

    private AFUNIXDatagramSocket(FileDescriptor fd, AFSocketType socketType) throws IOException {
        super(new AFUNIXDatagramSocketImpl(fd, socketType));
    }

    protected AFUNIXDatagramChannel newChannel() {
        return new AFUNIXDatagramChannel(this);
    }

    public static AFUNIXDatagramSocket newInstance() throws IOException {
        return (AFUNIXDatagramSocket)AFUNIXDatagramSocket.newInstance(AFUNIXDatagramSocket::new);
    }

    public static AFUNIXDatagramSocket newInstance(AFSocketType socketType) throws IOException {
        return (AFUNIXDatagramSocket)AFUNIXDatagramSocket.newInstance((FileDescriptor fd) -> new AFUNIXDatagramSocket(fd, socketType));
    }

    static AFUNIXDatagramSocket newInstance(FileDescriptor fdObj, int localPort, int remotePort) throws IOException {
        return (AFUNIXDatagramSocket)AFUNIXDatagramSocket.newInstance(AFUNIXDatagramSocket::new, fdObj, localPort, remotePort);
    }

    @Override
    public AFUNIXDatagramChannel getChannel() {
        return (AFUNIXDatagramChannel)super.getChannel();
    }

    @Override
    public FileDescriptor[] getReceivedFileDescriptors() throws IOException {
        return this.getAncillaryDataSupport().getReceivedFileDescriptors();
    }

    @Override
    public void clearReceivedFileDescriptors() {
        this.getAncillaryDataSupport().clearReceivedFileDescriptors();
    }

    @Override
    public void setOutboundFileDescriptors(FileDescriptor ... fdescs) throws IOException {
        if (fdescs != null && fdescs.length > 0 && !this.isConnected()) {
            throw new SocketException("Not connected");
        }
        this.getAncillaryDataSupport().setOutboundFileDescriptors(fdescs);
    }

    @Override
    public boolean hasOutboundFileDescriptors() {
        return this.getAncillaryDataSupport().hasOutboundFileDescriptors();
    }

    @Override
    public AFUNIXSocketCredentials getPeerCredentials() throws IOException {
        if (this.isClosed() || !this.isConnected()) {
            throw new SocketException("Not connected");
        }
        return ((AFUNIXDatagramSocketImpl)this.getAFImpl()).getPeerCredentials();
    }

    @Override
    protected AFDatagramSocket<AFUNIXSocketAddress> newDatagramSocketInstance() throws IOException {
        return new AFUNIXDatagramSocket(null);
    }
}

