package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.SocketException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFUNIXSocketImpl.class */
class AFUNIXSocketImpl extends AFSocketImpl<AFUNIXSocketAddress> {
    protected AFUNIXSocketImpl(FileDescriptor fdObj) {
        super(AFUNIXSocketAddress.AF_UNIX, fdObj);
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFUNIXSocketImpl$Lenient.class */
    static final class Lenient extends AFUNIXSocketImpl {
        Lenient(FileDescriptor fdObj) throws SocketException {
            super(fdObj);
        }

        @Override // org.newsclub.net.unix.AFSocketImpl, java.net.SocketOptions
        public void setOption(int optID, Object value) throws SocketException {
            super.setOptionLenient(optID, value);
        }

        @Override // org.newsclub.net.unix.AFSocketImpl, java.net.SocketOptions
        public Object getOption(int optID) throws SocketException {
            return super.getOptionLenient(optID);
        }
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

    final void setOutboundFileDescriptors(FileDescriptor... fdescs) throws IOException {
        this.ancillaryDataSupport.setOutboundFileDescriptors(fdescs);
    }

    final boolean hasOutboundFileDescriptors() {
        return this.ancillaryDataSupport.hasOutboundFileDescriptors();
    }
}
