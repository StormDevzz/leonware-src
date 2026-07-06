// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.IOException;
import java.util.Objects;
import java.io.OutputStream;
import java.io.InputStream;

public abstract class AFInputStream extends InputStream implements FileDescriptorAccess
{
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    
    AFInputStream() {
    }
    
    @Override
    public long transferTo(final OutputStream out) throws IOException {
        Objects.requireNonNull(out, "out");
        long transferred = 0L;
        final byte[] buffer = new byte[8192];
        int read;
        while ((read = this.read(buffer, 0, 8192)) >= 0) {
            out.write(buffer, 0, read);
            transferred += read;
        }
        return transferred;
    }
}
