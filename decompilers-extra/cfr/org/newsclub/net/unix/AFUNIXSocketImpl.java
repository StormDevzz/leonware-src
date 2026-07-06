/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.SocketException;
import org.newsclub.net.unix.AFSocketImpl;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import org.newsclub.net.unix.AFUNIXSocketCredentials;
import org.newsclub.net.unix.NativeUnixSocket;

class AFUNIXSocketImpl
extends AFSocketImpl<AFUNIXSocketAddress> {
    protected AFUNIXSocketImpl(FileDescriptor fdObj) {
        super(AFUNIXSocketAddress.AF_UNIX, fdObj);
    }

    AFUNIXSocketCredentials getPeerCredentials() throws IOException {
        return NativeUnixSocket.peerCredentials(this.fd, new AFUNIXSocketCredentials());
    }

    final FileDescriptor[] getReceivedFileDescriptors() {
        return this.ancillaryDataSupport.getReceivedFileDescriptors();
    }

    final void clearReceivedFileDescriptors() {
        this.ancillaryDataSupport.clearReceivedFileDescriptors();
    }

    final void receiveFileDescriptors(int[] fds) throws IOException {
        this.ancillaryDataSupport.receiveFileDescriptors(fds);
    }

    final void setOutboundFileDescriptors(FileDescriptor ... fdescs) throws IOException {
        this.ancillaryDataSupport.setOutboundFileDescriptors(fdescs);
    }

    final boolean hasOutboundFileDescriptors() {
        return this.ancillaryDataSupport.hasOutboundFileDescriptors();
    }

    static final class Lenient
    extends AFUNIXSocketImpl {
        Lenient(FileDescriptor fdObj) throws SocketException {
            super(fdObj);
        }

        @Override
        public void setOption(int optID, Object value) throws SocketException {
            super.setOptionLenient(optID, value);
        }

        @Override
        public Object getOption(int optID) throws SocketException {
            return super.getOptionLenient(optID);
        }
    }
}

