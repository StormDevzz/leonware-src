package org.newsclub.net.unix;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSocketType.class */
public enum AFSocketType {
    SOCK_STREAM(1),
    SOCK_DGRAM(2),
    SOCK_RAW(3),
    SOCK_RDM(4),
    SOCK_SEQPACKET(5);

    private final int id;

    AFSocketType(int id) {
        this.id = id;
    }

    int getId() {
        return this.id;
    }
}
