/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import org.newsclub.net.unix.AFInputStream;
import org.newsclub.net.unix.FileDescriptorAccess;

public abstract class AFOutputStream
extends OutputStream
implements FileDescriptorAccess {
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    AFOutputStream() {
    }

    public long transferFrom(InputStream in) throws IOException {
        int read;
        Objects.requireNonNull(in, "in");
        if (in instanceof AFInputStream) {
            return ((AFInputStream)in).transferTo(this);
        }
        long transferred = 0L;
        byte[] buffer = new byte[8192];
        while ((read = in.read(buffer, 0, 8192)) >= 0) {
            this.write(buffer, 0, read);
            transferred += (long)read;
        }
        return transferred;
    }
}

