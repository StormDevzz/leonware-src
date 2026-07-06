package org.newsclub.net.unix;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFOutputStream.class */
public abstract class AFOutputStream extends OutputStream implements FileDescriptorAccess {
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    AFOutputStream() {
    }

    public long transferFrom(InputStream in) throws IOException {
        Objects.requireNonNull(in, "in");
        if (in instanceof AFInputStream) {
            return ((AFInputStream) in).transferTo(this);
        }
        long transferred = 0;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        while (true) {
            int read = in.read(buffer, 0, DEFAULT_BUFFER_SIZE);
            if (read >= 0) {
                write(buffer, 0, read);
                transferred += (long) read;
            } else {
                return transferred;
            }
        }
    }
}
