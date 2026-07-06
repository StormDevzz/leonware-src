/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

public enum AFSocketProtocol {
    DEFAULT(0);

    private final int id;

    private AFSocketProtocol(int id) {
        this.id = id;
    }

    int getId() {
        return this.id;
    }
}

