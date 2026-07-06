/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.net.SocketException;

public class InvalidSocketException
extends SocketException {
    private static final long serialVersionUID = 1L;

    public InvalidSocketException() {
    }

    public InvalidSocketException(String msg) {
        super(msg);
    }
}

