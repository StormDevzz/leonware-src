package org.newsclub.net.unix;

import java.io.IOException;
import java.net.ProtocolFamily;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFProtocolFamily.class */
public interface AFProtocolFamily extends ProtocolFamily {
    AFDatagramChannel<?> openDatagramChannel() throws IOException;

    AFServerSocketChannel<?> openServerSocketChannel() throws IOException;

    AFSocketChannel<?> openSocketChannel() throws IOException;
}
