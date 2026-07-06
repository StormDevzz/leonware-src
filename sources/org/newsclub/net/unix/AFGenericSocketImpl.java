package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.net.SocketException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFGenericSocketImpl.class */
final class AFGenericSocketImpl extends AFSocketImpl<AFGenericSocketAddress> {
    AFGenericSocketImpl(FileDescriptor fdObj) {
        super(AFGenericSelectorProvider.AF_GENERIC, fdObj);
    }

    @Override // org.newsclub.net.unix.AFSocketImpl, java.net.SocketOptions
    public Object getOption(int optID) throws SocketException {
        return getOptionLenient(optID);
    }

    @Override // org.newsclub.net.unix.AFSocketImpl, java.net.SocketOptions
    public void setOption(int optID, Object value) throws SocketException {
        setOptionLenient(optID, value);
    }
}
