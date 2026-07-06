// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.IOException;
import java.util.Objects;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AFOutputStream extends OutputStream implements FileDescriptorAccess
{
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    
    AFOutputStream() {
    }
    
    public long transferFrom(final InputStream in) throws IOException {
        Objects.requireNonNull(in, "in");
        if (in instanceof AFInputStream) {
            return ((AFInputStream)in).transferTo(this);
        }
        long transferred = 0L;
        final byte[] buffer = new byte[8192];
        int read;
        while ((read = in.read(buffer, 0, 8192)) >= 0) {
            this.write(buffer, 0, read);
            transferred += read;
        }
        return transferred;
    }
}
