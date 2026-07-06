package org.newsclub.net.unix;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSocketExtensions.class */
public interface AFSocketExtensions {
    int getAncillaryReceiveBufferSize();

    void setAncillaryReceiveBufferSize(int i);

    void ensureAncillaryReceiveBufferSize(int i);
}
