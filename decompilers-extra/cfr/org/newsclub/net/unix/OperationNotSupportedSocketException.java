/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import org.newsclub.net.unix.InvalidSocketException;

public class OperationNotSupportedSocketException
extends InvalidSocketException {
    private static final long serialVersionUID = 1L;

    public OperationNotSupportedSocketException() {
    }

    public OperationNotSupportedSocketException(String msg) {
        super(msg);
    }
}

