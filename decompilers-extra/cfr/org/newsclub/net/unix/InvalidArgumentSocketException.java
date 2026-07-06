/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import org.newsclub.net.unix.InvalidSocketException;

public class InvalidArgumentSocketException
extends InvalidSocketException {
    private static final long serialVersionUID = 1L;

    public InvalidArgumentSocketException() {
    }

    public InvalidArgumentSocketException(String msg) {
        super(msg);
    }
}

