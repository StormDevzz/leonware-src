// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.net.SocketAddress;
import java.net.SocketException;
import java.io.IOException;
import java.io.FileDescriptor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.nio.ByteBuffer;

class AFCore extends CleanableState
{
    private static final ThreadLocal<ByteBuffer> TL_BUFFER;
    private static final String PROP_TL_BUFFER_MAX_CAPACITY = "org.newsclub.net.unix.thread-local-buffer.max-capacity";
    private static final int TL_BUFFER_MIN_CAPACITY = 8192;
    private static final int TL_BUFFER_MAX_CAPACITY;
    private final AtomicBoolean closed;
    final FileDescriptor fd;
    final AncillaryDataSupport ancillaryDataSupport;
    private final boolean datagramMode;
    private boolean blocking;
    private boolean cleanFd;
    
    AFCore(final Object observed, final FileDescriptor fd, final AncillaryDataSupport ancillaryDataSupport, final boolean datagramMode) {
        super(observed);
        this.closed = new AtomicBoolean(false);
        this.blocking = true;
        this.cleanFd = true;
        this.datagramMode = datagramMode;
        this.ancillaryDataSupport = ancillaryDataSupport;
        this.fd = ((fd == null) ? new FileDescriptor() : fd);
    }
    
    AFCore(final Object observed, final FileDescriptor fd) {
        this(observed, fd, null, false);
    }
    
    @Override
    protected final void doClean() {
        if (this.fd != null && this.fd.valid() && this.cleanFd) {
            try {
                this.doClose();
            }
            catch (final IOException ex) {}
        }
        if (this.ancillaryDataSupport != null) {
            this.ancillaryDataSupport.close();
        }
    }
    
    void disableCleanFd() {
        this.cleanFd = false;
    }
    
    boolean isClosed() {
        return this.closed.get();
    }
    
    void doClose() throws IOException {
        if (this.closed.compareAndSet(false, true)) {
            NativeUnixSocket.close(this.fd);
        }
    }
    
    FileDescriptor validFdOrException() throws SocketException {
        final FileDescriptor fdesc = this.validFd();
        if (fdesc == null) {
            this.closed.set(true);
            throw new SocketClosedException("Not open");
        }
        return fdesc;
    }
    
    synchronized FileDescriptor validFd() {
        if (this.isClosed()) {
            return null;
        }
        final FileDescriptor descriptor = this.fd;
        if (descriptor != null && descriptor.valid()) {
            return descriptor;
        }
        return null;
    }
    
    int read(final ByteBuffer dst) throws IOException {
        return this.read(dst, null, 0);
    }
    
    int read(final ByteBuffer dst, final ByteBuffer socketAddressBuffer, int options) throws IOException {
        int remaining = dst.remaining();
        if (remaining == 0) {
            return 0;
        }
        final FileDescriptor fdesc = this.validFdOrException();
        final int dstPos = dst.position();
        final boolean direct = dst.isDirect();
        ByteBuffer buf;
        int pos;
        if (direct) {
            buf = dst;
            pos = dstPos;
        }
        else {
            buf = this.getThreadLocalDirectByteBuffer(remaining);
            remaining = Math.min(remaining, buf.remaining());
            pos = buf.position();
        }
        if (!this.blocking) {
            options |= 0x4;
        }
        final int count = NativeUnixSocket.receive(fdesc, buf, pos, remaining, socketAddressBuffer, options, this.ancillaryDataSupport, 0);
        if (count == -1) {
            return count;
        }
        if (direct) {
            if (count < 0) {
                throw new IllegalStateException();
            }
            dst.position();
        }
        else {
            final int oldLimit = buf.limit();
            if (count < oldLimit) {
                buf.limit();
            }
            try {
                while (buf.hasRemaining()) {
                    dst.put(buf);
                }
            }
            finally {
                if (count < oldLimit) {
                    buf.limit();
                }
            }
        }
        return count;
    }
    
    int write(final ByteBuffer src) throws IOException {
        return this.write(src, null, 0);
    }
    
    int write(final ByteBuffer src, final SocketAddress target, int options) throws IOException {
        int remaining = src.remaining();
        if (remaining == 0) {
            return 0;
        }
        final FileDescriptor fdesc = this.validFdOrException();
        ByteBuffer addressTo;
        int addressToLen;
        if (target == null) {
            addressTo = null;
            addressToLen = 0;
        }
        else {
            addressTo = AFSocketAddress.SOCKETADDRESS_BUFFER_TL.get();
            addressToLen = AFSocketAddress.unwrapAddressDirectBufferInternal(addressTo, target);
        }
        if (!this.blocking) {
            options |= 0x4;
        }
        final int pos = src.position();
        final boolean isDirect = src.isDirect();
        ByteBuffer buf;
        int bufPos;
        if (isDirect) {
            buf = src;
            bufPos = pos;
        }
        else {
            buf = this.getThreadLocalDirectByteBuffer(remaining);
            remaining = Math.min(remaining, buf.remaining());
            bufPos = buf.position();
            while (src.hasRemaining() && buf.hasRemaining()) {
                buf.put(src);
            }
            buf.position();
        }
        if (this.datagramMode) {
            options |= 0x10;
        }
        final int written = NativeUnixSocket.send(fdesc, buf, bufPos, remaining, addressTo, addressToLen, options, this.ancillaryDataSupport);
        src.position();
        return written;
    }
    
    ByteBuffer getThreadLocalDirectByteBuffer(int capacity) {
        if (capacity > AFCore.TL_BUFFER_MAX_CAPACITY && AFCore.TL_BUFFER_MAX_CAPACITY > 0) {
            return ByteBuffer.allocateDirect(capacity);
        }
        if (capacity < 8192) {
            capacity = 8192;
        }
        ByteBuffer buffer = AFCore.TL_BUFFER.get();
        if (buffer == null || capacity > buffer.capacity()) {
            buffer = ByteBuffer.allocateDirect(capacity);
            AFCore.TL_BUFFER.set(buffer);
        }
        buffer.clear();
        return buffer;
    }
    
    void implConfigureBlocking(final boolean block) throws IOException {
        NativeUnixSocket.configureBlocking(this.validFdOrException(), block);
        this.blocking = block;
    }
    
    boolean isBlocking() {
        return this.blocking;
    }
    
    static {
        TL_BUFFER = new ThreadLocal<ByteBuffer>();
        TL_BUFFER_MAX_CAPACITY = Integer.parseInt(System.getProperty("org.newsclub.net.unix.thread-local-buffer.max-capacity", Integer.toString(1048576)));
    }
}
