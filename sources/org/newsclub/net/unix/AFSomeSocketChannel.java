package org.newsclub.net.unix;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.SelectableChannel;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSomeSocketChannel.class */
public interface AFSomeSocketChannel extends Closeable, FileDescriptorAccess, AFSomeSocketThing {
    boolean isBlocking();

    SelectableChannel configureBlocking(boolean z) throws IOException;
}
