/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.kohlschutter.annotations.compiletime.SuppressFBWarnings
 */
package org.newsclub.net.unix;

import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.spi.SelectorProvider;
import org.newsclub.net.unix.AFCore;
import org.newsclub.net.unix.AFSelectorProvider;
import org.newsclub.net.unix.AFUNIXSelectorProvider;
import org.newsclub.net.unix.FileDescriptorAccess;
import org.newsclub.net.unix.NativeUnixSocket;

public final class AFPipe
extends Pipe
implements Closeable {
    private final AFCore sourceCore;
    private final AFCore sinkCore;
    private final SourceChannel sourceChannel;
    private final SinkChannel sinkChannel;
    private final int options;

    AFPipe(AFSelectorProvider<?> provider, boolean selectable) throws IOException {
        NativeUnixSocket.ensureSupported();
        this.sourceCore = new AFCore(this, null);
        this.sinkCore = new AFCore(this, null);
        boolean isSocket = NativeUnixSocket.initPipe(this.sourceCore.fd, this.sinkCore.fd, selectable);
        this.options = isSocket ? 0 : 8;
        this.sourceChannel = new SourceChannel(provider);
        this.sinkChannel = new SinkChannel(provider);
    }

    @Override
    @SuppressFBWarnings(value={"EI_EXPOSE_REP"})
    public SourceChannel source() {
        return this.sourceChannel;
    }

    @Override
    @SuppressFBWarnings(value={"EI_EXPOSE_REP"})
    public SinkChannel sink() {
        return this.sinkChannel;
    }

    FileDescriptor sourceFD() {
        return this.sourceCore.fd;
    }

    FileDescriptor sinkFD() {
        return this.sinkCore.fd;
    }

    @Override
    public void close() throws IOException {
        try {
            this.source().close();
        }
        finally {
            this.sink().close();
        }
    }

    int getOptions() {
        return this.options;
    }

    public static AFPipe open() throws IOException {
        return AFUNIXSelectorProvider.provider().openPipe();
    }

    public final class SourceChannel
    extends Pipe.SourceChannel
    implements FileDescriptorAccess {
        SourceChannel(SelectorProvider provider) {
            super(provider);
        }

        @Override
        public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
            if (length == 0) {
                return 0L;
            }
            return this.read(dsts[offset]);
        }

        @Override
        public long read(ByteBuffer[] dsts) throws IOException {
            return this.read(dsts, 0, dsts.length);
        }

        @Override
        public int read(ByteBuffer dst) throws IOException {
            return AFPipe.this.sourceCore.read(dst, null, AFPipe.this.options);
        }

        @Override
        protected void implConfigureBlocking(boolean block) throws IOException {
            AFPipe.this.sourceCore.implConfigureBlocking(block);
        }

        @Override
        protected void implCloseSelectableChannel() throws IOException {
            AFPipe.this.sourceCore.close();
        }

        @Override
        public FileDescriptor getFileDescriptor() throws IOException {
            return ((AFPipe)AFPipe.this).sourceCore.fd;
        }
    }

    public final class SinkChannel
    extends Pipe.SinkChannel
    implements FileDescriptorAccess {
        SinkChannel(SelectorProvider provider) {
            super(provider);
        }

        @Override
        public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
            if (length == 0) {
                return 0L;
            }
            return this.write(srcs[offset]);
        }

        @Override
        public long write(ByteBuffer[] srcs) throws IOException {
            return this.write(srcs, 0, srcs.length);
        }

        @Override
        public int write(ByteBuffer src) throws IOException {
            return AFPipe.this.sinkCore.write(src, null, AFPipe.this.options);
        }

        @Override
        protected void implConfigureBlocking(boolean block) throws IOException {
            AFPipe.this.sinkCore.implConfigureBlocking(block);
        }

        @Override
        protected void implCloseSelectableChannel() throws IOException {
            AFPipe.this.sinkCore.close();
        }

        @Override
        public FileDescriptor getFileDescriptor() throws IOException {
            return ((AFPipe)AFPipe.this).sinkCore.fd;
        }
    }
}

