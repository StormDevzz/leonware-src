/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

public enum AFSocketType {
    SOCK_STREAM(1),
    SOCK_DGRAM(2),
    SOCK_RAW(3),
    SOCK_RDM(4),
    SOCK_SEQPACKET(5);

    private final int id;

    private AFSocketType(int id) {
        this.id = id;
    }

    int getId() {
        return this.id;
    }
}

