// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.IOException;
import java.nio.channels.spi.SelectorProvider;
import java.io.FileDescriptor;
import java.io.Closeable;
import java.nio.channels.Pipe;

public final class AFPipe extends Pipe implements Closeable
{
    private final AFCore sourceCore;
    private final AFCore sinkCore;
    private final SourceChannel sourceChannel;
    private final SinkChannel sinkChannel;
    private final int options;
    
    AFPipe(final AFSelectorProvider<?> provider, final boolean selectable) throws IOException {
        NativeUnixSocket.ensureSupported();
        this.sourceCore = new AFCore(this, null);
        this.sinkCore = new AFCore(this, null);
        final boolean isSocket = NativeUnixSocket.initPipe(this.sourceCore.fd, this.sinkCore.fd, selectable);
        this.options = (isSocket ? 0 : 8);
        this.sourceChannel = new SourceChannel(provider);
        this.sinkChannel = new SinkChannel(provider);
    }
    
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    @Override
    public SourceChannel source() {
        return this.sourceChannel;
    }
    
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    @Override
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
    
    public final class SourceChannel extends Pipe.SourceChannel implements FileDescriptorAccess
    {
        SourceChannel(final SelectorProvider provider) {
            super(provider);
        }
        
        @Override
        public long read(final ByteBuffer[] dsts, final int offset, final int length) throws IOException {
            if (length == 0) {
                return 0L;
            }
            return this.read(dsts[offset]);
        }
        
        @Override
        public long read(final ByteBuffer[] dsts) throws IOException {
            return this.read(dsts, 0, dsts.length);
        }
        
        @Override
        public int read(final ByteBuffer dst) throws IOException {
            return AFPipe.this.sourceCore.read(dst, null, AFPipe.this.options);
        }
        
        @Override
        protected void implConfigureBlocking(final boolean block) throws IOException {
            AFPipe.this.sourceCore.implConfigureBlocking(block);
        }
        
        @Override
        protected void implCloseSelectableChannel() throws IOException {
            AFPipe.this.sourceCore.close();
        }
        
        @Override
        public FileDescriptor getFileDescriptor() throws IOException {
            return AFPipe.this.sourceCore.fd;
        }
    }
    
    public final class SinkChannel extends Pipe.SinkChannel implements FileDescriptorAccess
    {
        SinkChannel(final SelectorProvider provider) {
            super(provider);
        }
        
        @Override
        public long write(final ByteBuffer[] srcs, final int offset, final int length) throws IOException {
            if (length == 0) {
                return 0L;
            }
            return this.write(srcs[offset]);
        }
        
        @Override
        public long write(final ByteBuffer[] srcs) throws IOException {
            return this.write(srcs, 0, srcs.length);
        }
        
        @Override
        public int write(final ByteBuffer src) throws IOException {
            return AFPipe.this.sinkCore.write(src, null, AFPipe.this.options);
        }
        
        @Override
        protected void implConfigureBlocking(final boolean block) throws IOException {
            AFPipe.this.sinkCore.implConfigureBlocking(block);
        }
        
        @Override
        protected void implCloseSelectableChannel() throws IOException {
            AFPipe.this.sinkCore.close();
        }
        
        @Override
        public FileDescriptor getFileDescriptor() throws IOException {
            return AFPipe.this.sinkCore.fd;
        }
    }
}
