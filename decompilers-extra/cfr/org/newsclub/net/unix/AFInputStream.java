/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import org.newsclub.net.unix.FileDescriptorAccess;

public abstract class AFInputStream
extends InputStream
implements FileDescriptorAccess {
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    AFInputStream() {
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        int read;
        Objects.requireNonNull(out, "out");
        long transferred = 0L;
        byte[] buffer = new byte[8192];
        while ((read = this.read(buffer, 0, 8192)) >= 0) {
            out.write(buffer, 0, read);
            transferred += (long)read;
        }
        return transferred;
    }
}

