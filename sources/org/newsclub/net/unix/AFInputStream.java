package org.newsclub.net.unix;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFInputStream.class */
public abstract class AFInputStream extends InputStream implements FileDescriptorAccess {
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    AFInputStream() {
    }

    @Override // java.io.InputStream
    public long transferTo(OutputStream out) throws IOException {
        Objects.requireNonNull(out, "out");
        long transferred = 0;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        while (true) {
            int read = read(buffer, 0, DEFAULT_BUFFER_SIZE);
            if (read >= 0) {
                out.write(buffer, 0, read);
                transferred += (long) read;
            } else {
                return transferred;
            }
        }
    }
}
