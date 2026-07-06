package org.newsclub.net.unix;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSocketProtocol.class */
public enum AFSocketProtocol {
    DEFAULT(0);

    private final int id;

    AFSocketProtocol(int id) {
        this.id = id;
    }

    int getId() {
        return this.id;
    }
}
