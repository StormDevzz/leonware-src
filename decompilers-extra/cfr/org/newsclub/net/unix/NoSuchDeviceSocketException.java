/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import org.newsclub.net.unix.InvalidSocketException;

public class NoSuchDeviceSocketException
extends InvalidSocketException {
    private static final long serialVersionUID = 1L;

    public NoSuchDeviceSocketException() {
    }

    public NoSuchDeviceSocketException(String msg) {
        super(msg);
    }
}

