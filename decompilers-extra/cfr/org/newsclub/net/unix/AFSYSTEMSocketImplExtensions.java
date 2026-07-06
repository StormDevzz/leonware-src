/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Objects;
import org.newsclub.net.unix.AFSYSTEMSocketAddress;
import org.newsclub.net.unix.AFSocketImplExtensions;
import org.newsclub.net.unix.AncillaryDataSupport;
import org.newsclub.net.unix.NativeUnixSocket;

public final class AFSYSTEMSocketImplExtensions
implements AFSocketImplExtensions<AFSYSTEMSocketAddress> {
    AFSYSTEMSocketImplExtensions(AncillaryDataSupport ancillaryDataSupport) {
    }

    public int getKernelControlId(FileDescriptor fd, String name) throws IOException {
        return NativeUnixSocket.systemResolveCtlId(fd, Objects.requireNonNull(name));
    }
}

