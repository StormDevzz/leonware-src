/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.net.SocketException;

public final class BrokenPipeSocketException
extends SocketException {
    private static final long serialVersionUID = 1L;

    public BrokenPipeSocketException() {
    }

    public BrokenPipeSocketException(String msg) {
        super(msg);
    }
}

