package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFGenericDatagramSocketImpl.class */
final class AFGenericDatagramSocketImpl extends AFDatagramSocketImpl<AFGenericSocketAddress> {
    AFGenericDatagramSocketImpl(FileDescriptor fd, AFSocketType socketType) throws IOException {
        super(AFGenericSelectorProvider.AF_GENERIC, fd, socketType);
    }
}
