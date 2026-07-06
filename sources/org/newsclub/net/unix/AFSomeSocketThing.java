package org.newsclub.net.unix;

import java.io.Closeable;
import java.net.SocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSomeSocketThing.class */
public interface AFSomeSocketThing extends Closeable, FileDescriptorAccess {
    SocketAddress getLocalSocketAddress();

    void setShutdownOnClose(boolean z);
}
