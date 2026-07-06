package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFCore.class */
class AFCore extends CleanableState {
    private static final int TL_BUFFER_MIN_CAPACITY = 8192;
    private final AtomicBoolean closed;
    final FileDescriptor fd;
    final AncillaryDataSupport ancillaryDataSupport;
    private final boolean datagramMode;
    private boolean blocking;
    private boolean cleanFd;
    private static final ThreadLocal<ByteBuffer> TL_BUFFER = new ThreadLocal<>();
    private static final String PROP_TL_BUFFER_MAX_CAPACITY = "org.newsclub.net.unix.thread-local-buffer.max-capacity";
    private static final int TL_BUFFER_MAX_CAPACITY = Integer.parseInt(System.getProperty(PROP_TL_BUFFER_MAX_CAPACITY, Integer.toString(1048576)));

    AFCore(Object observed, FileDescriptor fd, AncillaryDataSupport ancillaryDataSupport, boolean datagramMode) {
        super(observed);
        this.closed = new AtomicBoolean(false);
        this.blocking = true;
        this.cleanFd = true;
        this.datagramMode = datagramMode;
        this.ancillaryDataSupport = ancillaryDataSupport;
        this.fd = fd == null ? new FileDescriptor() : fd;
    }

    AFCore(Object observed, FileDescriptor fd) {
        this(observed, fd, null, false);
    }

    @Override // org.newsclub.net.unix.CleanableState
    protected final void doClean() {
        if (this.fd != null && this.fd.valid() && this.cleanFd) {
            try {
                doClose();
            } catch (IOException e) {
            }
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
        FileDescriptor fdesc = validFd();
        if (fdesc == null) {
            this.closed.set(true);
            throw new SocketClosedException("Not open");
        }
        return fdesc;
    }

    synchronized FileDescriptor validFd() {
        FileDescriptor descriptor;
        if (!isClosed() && (descriptor = this.fd) != null && descriptor.valid()) {
            return descriptor;
        }
        return null;
    }

    int read(ByteBuffer dst) throws IOException {
        return read(dst, null, 0);
    }

    int read(ByteBuffer dst, ByteBuffer socketAddressBuffer, int options) throws IOException {
        ByteBuffer buf;
        int pos;
        int remaining = dst.remaining();
        if (remaining == 0) {
            return 0;
        }
        FileDescriptor fdesc = validFdOrException();
        int dstPos = dst.position();
        boolean direct = dst.isDirect();
        if (direct) {
            buf = dst;
            pos = dstPos;
        } else {
            buf = getThreadLocalDirectByteBuffer(remaining);
            remaining = Math.min(remaining, buf.remaining());
            pos = buf.position();
        }
        if (!this.blocking) {
            options |= 4;
        }
        int count = NativeUnixSocket.receive(fdesc, buf, pos, remaining, socketAddressBuffer, options, this.ancillaryDataSupport, 0);
        if (count == -1) {
            return count;
        }
        if (direct) {
            if (count < 0) {
                throw new IllegalStateException();
            }
            dst.position(pos + count);
        } else {
            int oldLimit = buf.limit();
            if (count < oldLimit) {
                buf.limit(count);
            }
            while (buf.hasRemaining()) {
                try {
                    dst.put(buf);
                } finally {
                    if (count < oldLimit) {
                        buf.limit(oldLimit);
                    }
                }
            }
        }
        return count;
    }

    int write(ByteBuffer src) throws IOException {
        return write(src, null, 0);
    }

    int write(ByteBuffer src, SocketAddress target, int options) throws IOException {
        ByteBuffer addressTo;
        int addressToLen;
        ByteBuffer buf;
        int bufPos;
        int remaining = src.remaining();
        if (remaining == 0) {
            return 0;
        }
        FileDescriptor fdesc = validFdOrException();
        if (target == null) {
            addressTo = null;
            addressToLen = 0;
        } else {
            addressTo = AFSocketAddress.SOCKETADDRESS_BUFFER_TL.get();
            addressToLen = AFSocketAddress.unwrapAddressDirectBufferInternal(addressTo, target);
        }
        if (!this.blocking) {
            options |= 4;
        }
        int pos = src.position();
        boolean isDirect = src.isDirect();
        if (isDirect) {
            buf = src;
            bufPos = pos;
        } else {
            buf = getThreadLocalDirectByteBuffer(remaining);
            remaining = Math.min(remaining, buf.remaining());
            bufPos = buf.position();
            while (src.hasRemaining() && buf.hasRemaining()) {
                buf.put(src);
            }
            buf.position(bufPos);
        }
        if (this.datagramMode) {
            options |= 16;
        }
        int written = NativeUnixSocket.send(fdesc, buf, bufPos, remaining, addressTo, addressToLen, options, this.ancillaryDataSupport);
        src.position(pos + written);
        return written;
    }

    ByteBuffer getThreadLocalDirectByteBuffer(int capacity) {
        if (capacity > TL_BUFFER_MAX_CAPACITY && TL_BUFFER_MAX_CAPACITY > 0) {
            return ByteBuffer.allocateDirect(capacity);
        }
        if (capacity < TL_BUFFER_MIN_CAPACITY) {
            capacity = TL_BUFFER_MIN_CAPACITY;
        }
        ByteBuffer buffer = TL_BUFFER.get();
        if (buffer == null || capacity > buffer.capacity()) {
            buffer = ByteBuffer.allocateDirect(capacity);
            TL_BUFFER.set(buffer);
        }
        buffer.clear();
        return buffer;
    }

    void implConfigureBlocking(boolean block) throws IOException {
        NativeUnixSocket.configureBlocking(validFdOrException(), block);
        this.blocking = block;
    }

    boolean isBlocking() {
        return this.blocking;
    }
}
