/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import org.newsclub.net.unix.InvalidSocketException;

public class AddressUnavailableSocketException
extends InvalidSocketException {
    private static final long serialVersionUID = 1L;

    public AddressUnavailableSocketException() {
    }

    public AddressUnavailableSocketException(String msg) {
        super(msg);
    }
}

