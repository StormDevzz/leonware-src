/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.net.SocketException;

public final class ConnectionResetSocketException
extends SocketException {
    private static final long serialVersionUID = 1L;

    public ConnectionResetSocketException() {
    }

    public ConnectionResetSocketException(String msg) {
        super(msg);
    }
}

