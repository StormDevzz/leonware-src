package org.newsclub.net.unix;

import java.io.IOException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/KnownJavaBugIOException.class */
public class KnownJavaBugIOException extends IOException {
    private static final long serialVersionUID = 1;

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
