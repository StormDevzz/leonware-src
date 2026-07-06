package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Objects;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSYSTEMSocketImplExtensions.class */
public final class AFSYSTEMSocketImplExtensions implements AFSocketImplExtensions<AFSYSTEMSocketAddress> {
    AFSYSTEMSocketImplExtensions(AncillaryDataSupport ancillaryDataSupport) {
    }

    public int getKernelControlId(FileDescriptor fd, String name) throws IOException {
        return NativeUnixSocket.systemResolveCtlId(fd, (String) Objects.requireNonNull(name));
    }
}
