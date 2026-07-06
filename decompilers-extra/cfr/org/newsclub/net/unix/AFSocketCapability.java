/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

public enum AFSocketCapability {
    CAPABILITY_PEER_CREDENTIALS(0),
    CAPABILITY_ANCILLARY_MESSAGES(1),
    CAPABILITY_FILE_DESCRIPTORS(2),
    CAPABILITY_ABSTRACT_NAMESPACE(3),
    CAPABILITY_UNIX_DATAGRAMS(4),
    CAPABILITY_NATIVE_SOCKETPAIR(5),
    CAPABILITY_FD_AS_REDIRECT(6),
    CAPABILITY_TIPC(7),
    CAPABILITY_UNIX_DOMAIN(8),
    CAPABILITY_VSOCK(9),
    CAPABILITY_VSOCK_DGRAM(10),
    CAPABILITY_ZERO_LENGTH_SEND(11),
    CAPABILITY_UNSAFE(12),
    CAPABILITY_LARGE_PORTS(13),
    CAPABILITY_DARWIN(14);

    private final int bitmask;

    private AFSocketCapability(int bit) {
        this.bitmask = 1 << bit;
    }

    int getBitmask() {
        return this.bitmask;
    }
}

