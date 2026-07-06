package org.newsclub.net.unix;

import java.net.SocketException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/BrokenPipeSocketException.class */
public final class BrokenPipeSocketException extends SocketException {
    private static final long serialVersionUID = 1;

    public BrokenPipeSocketException() {
    }

    public BrokenPipeSocketException(String msg) {
        super(msg);
    }
}
