package org.newsclub.net.unix;

import java.net.SocketException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/SocketClosedException.class */
public final class SocketClosedException extends SocketException {
    private static final long serialVersionUID = 1;

    public SocketClosedException() {
    }

    public SocketClosedException(String msg) {
        super(msg);
    }
}
