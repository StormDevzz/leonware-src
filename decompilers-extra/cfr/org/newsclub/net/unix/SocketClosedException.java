/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.net.SocketException;

public final class SocketClosedException
extends SocketException {
    private static final long serialVersionUID = 1L;

    public SocketClosedException() {
    }

    public SocketClosedException(String msg) {
        super(msg);
    }
}

