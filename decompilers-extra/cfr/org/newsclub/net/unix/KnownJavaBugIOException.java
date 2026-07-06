/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.IOException;

public class KnownJavaBugIOException
extends IOException {
    private static final long serialVersionUID = 1L;

    public KnownJavaBugIOException() {
    }

    public KnownJavaBugIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public KnownJavaBugIOException(String message) {
        super(message);
    }

    public KnownJavaBugIOException(Throwable cause) {
        super(cause);
    }
}

