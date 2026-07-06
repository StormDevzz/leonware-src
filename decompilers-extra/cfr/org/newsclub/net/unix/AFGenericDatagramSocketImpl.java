/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import org.newsclub.net.unix.AFDatagramSocketImpl;
import org.newsclub.net.unix.AFGenericSelectorProvider;
import org.newsclub.net.unix.AFGenericSocketAddress;
import org.newsclub.net.unix.AFSocketType;

final class AFGenericDatagramSocketImpl
extends AFDatagramSocketImpl<AFGenericSocketAddress> {
    AFGenericDatagramSocketImpl(FileDescriptor fd, AFSocketType socketType) throws IOException {
        super(AFGenericSelectorProvider.AF_GENERIC, fd, socketType);
    }
}

