/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.net.SocketException;
import org.newsclub.net.unix.AFGenericSelectorProvider;
import org.newsclub.net.unix.AFGenericSocketAddress;
import org.newsclub.net.unix.AFSocketImpl;

final class AFGenericSocketImpl
extends AFSocketImpl<AFGenericSocketAddress> {
    AFGenericSocketImpl(FileDescriptor fdObj) {
        super(AFGenericSelectorProvider.AF_GENERIC, fdObj);
    }

    @Override
    public Object getOption(int optID) throws SocketException {
        return this.getOptionLenient(optID);
    }

    @Override
    public void setOption(int optID, Object value) throws SocketException {
        this.setOptionLenient(optID, value);
    }
}

