package org.newsclub.net.unix;

import java.net.SocketException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/ConnectionResetSocketException.class */
public final class ConnectionResetSocketException extends SocketException {
    private static final long serialVersionUID = 1;

    public ConnectionResetSocketException() {
    }

    public ConnectionResetSocketException(String msg) {
        super(msg);
    }
}
