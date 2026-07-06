package org.newsclub.net.unix;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFUNIXSocketCapability.class */
@Deprecated
public enum AFUNIXSocketCapability {
    CAPABILITY_PEER_CREDENTIALS(0),
    CAPABILITY_ANCILLARY_MESSAGES(1),
    CAPABILITY_FILE_DESCRIPTORS(2),
    CAPABILITY_ABSTRACT_NAMESPACE(3),
    CAPABILITY_DATAGRAMS(4),
    CAPABILITY_NATIVE_SOCKETPAIR(5);

    private final int bitmask;

    AFUNIXSocketCapability(int bit) {
        this.bitmask = 1 << bit;
    }

    int getBitmask() {
        return this.bitmask;
    }
}
