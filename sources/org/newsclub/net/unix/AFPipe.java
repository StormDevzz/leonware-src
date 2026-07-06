package org.newsclub.net.unix;

import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.spi.SelectorProvider;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFPipe.class */
public final class AFPipe extends Pipe implements Closeable {
    private final AFCore sourceCore;
    private final AFCore sinkCore;
    private final SourceChannel sourceChannel;
    private final SinkChannel sinkChannel;
    private final int options;

    AFPipe(AFSelectorProvider<?> provider, boolean selectable) throws IOException {
        NativeUnixSocket.ensureSupported();
        this.sourceCore = new AFCore(this, (FileDescriptor) null);
        this.sinkCore = new AFCore(this, (FileDescriptor) null);
        boolean isSocket = NativeUnixSocket.initPipe(this.sourceCore.fd, this.sinkCore.fd, selectable);
        this.options = isSocket ? 0 : 8;
        this.sourceChannel = new SourceChannel(provider);
        this.sinkChannel = new SinkChannel(provider);
    }

    @Override // java.nio.channels.Pipe
    @SuppressFBWarnings({"EI_EXPOSE_REP"})
    public SourceChannel source() {
        return this.sourceChannel;
    }

    @Override // java.nio.channels.Pipe
    @SuppressFBWarnings({"EI_EXPOSE_REP"})
    public SinkChannel sink() {
        return this.sinkChannel;
    }

    FileDescriptor sourceFD() {
        return this.sourceCore.fd;
    }

    FileDescriptor sinkFD() {
        return this.sinkCore.fd;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            source().close();
        } finally {
            sink().close();
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFPipe$SourceChannel.class */
    public final class SourceChannel extends Pipe.SourceChannel implements FileDescriptorAccess {
        SourceChannel(SelectorProvider provider) {
            super(provider);
        }

        @Override // java.nio.channels.ScatteringByteChannel
        public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
            if (length == 0) {
                return 0L;
            }
            return read(dsts[offset]);
        }

        @Override // java.nio.channels.ScatteringByteChannel
        public long read(ByteBuffer[] dsts) throws IOException {
            return read(dsts, 0, dsts.length);
        }

        @Override // java.nio.channels.ReadableByteChannel
        public int read(ByteBuffer dst) throws IOException {
            return AFPipe.this.sourceCore.read(dst, null, AFPipe.this.options);
        }

        @Override // java.nio.channels.spi.AbstractSelectableChannel
        protected void implConfigureBlocking(boolean block) throws IOException {
            AFPipe.this.sourceCore.implConfigureBlocking(block);
        }

        @Override // java.nio.channels.spi.AbstractSelectableChannel
        protected void implCloseSelectableChannel() throws IOException {
            AFPipe.this.sourceCore.close();
        }

        @Override // org.newsclub.net.unix.FileDescriptorAccess
        public FileDescriptor getFileDescriptor() throws IOException {
            return AFPipe.this.sourceCore.fd;
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFPipe$SinkChannel.class */
    public final class SinkChannel extends Pipe.SinkChannel implements FileDescriptorAccess {
        SinkChannel(SelectorProvider provider) {
            super(provider);
        }

        @Override // java.nio.channels.GatheringByteChannel
        public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
            if (length == 0) {
                return 0L;
            }
            return write(srcs[offset]);
        }

        @Override // java.nio.channels.GatheringByteChannel
        public long write(ByteBuffer[] srcs) throws IOException {
            return write(srcs, 0, srcs.length);
        }

        @Override // java.nio.channels.WritableByteChannel
        public int write(ByteBuffer src) throws IOException {
            return AFPipe.this.sinkCore.write(src, null, AFPipe.this.options);
        }

        @Override // java.nio.channels.spi.AbstractSelectableChannel
        protected void implConfigureBlocking(boolean block) throws IOException {
            AFPipe.this.sinkCore.implConfigureBlocking(block);
        }

        @Override // java.nio.channels.spi.AbstractSelectableChannel
        protected void implCloseSelectableChannel() throws IOException {
            AFPipe.this.sinkCore.close();
        }

        @Override // org.newsclub.net.unix.FileDescriptorAccess
        public FileDescriptor getFileDescriptor() throws IOException {
            return AFPipe.this.sinkCore.fd;
        }
    }

    int getOptions() {
        return this.options;
    }

    public static AFPipe open() throws IOException {
        return AFUNIXSelectorProvider.provider().openPipe();
    }
}
