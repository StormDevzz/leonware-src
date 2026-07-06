package org.newsclub.net.unix;

import java.net.SocketException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/InvalidSocketException.class */
public class InvalidSocketException extends SocketException {
    private static final long serialVersionUID = 1;

    public InvalidSocketException() {
    }

    public InvalidSocketException(String msg) {
        super(msg);
    }
}
