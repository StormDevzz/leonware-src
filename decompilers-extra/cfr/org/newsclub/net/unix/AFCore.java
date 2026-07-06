/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import org.newsclub.net.unix.AFSocketAddress;
import org.newsclub.net.unix.AncillaryDataSupport;
import org.newsclub.net.unix.CleanableState;
import org.newsclub.net.unix.NativeUnixSocket;
import org.newsclub.net.unix.SocketClosedException;

class AFCore
extends CleanableState {
    private static final ThreadLocal<ByteBuffer> TL_BUFFER = new ThreadLocal();
    private static final String PROP_TL_BUFFER_MAX_CAPACITY = "org.newsclub.net.unix.thread-local-buffer.max-capacity";
    private static final int TL_BUFFER_MIN_CAPACITY = 8192;
    private static final int TL_BUFFER_MAX_CAPACITY = Integer.parseInt(System.getProperty("org.newsclub.net.unix.thread-local-buffer.max-capacity", Integer.toString(0x100000)));
    private final AtomicBoolean closed = new AtomicBoolean(false);
    final FileDescriptor fd;
    final AncillaryDataSupport ancillaryDataSupport;
    private final boolean datagramMode;
    private boolean blocking = true;
    private boolean cleanFd = true;

    AFCore(Object observed, FileDescriptor fd, AncillaryDataSupport ancillaryDataSupport, boolean datagramMode) {
        super(observed);
        this.datagramMode = datagramMode;
        this.ancillaryDataSupport = ancillaryDataSupport;
        this.fd = fd == null ? new FileDescriptor() : fd;
    }

    AFCore(Object observed, FileDescriptor fd) {
        this(observed, fd, null, false);
    }

    @Override
    protected final void doClean() {
        if (this.fd != null && this.fd.valid() && this.cleanFd) {
            try {
                this.doClose();
            }
            catch (IOException iOException) {
                // empty catch block
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
        FileDescriptor fdesc = this.validFd();
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
        FileDescriptor descriptor = this.fd;
        if (descriptor != null && descriptor.valid()) {
            return descriptor;
        }
        return null;
    }

    int read(ByteBuffer dst) throws IOException {
        return this.read(dst, null, 0);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    int read(ByteBuffer dst, ByteBuffer socketAddressBuffer, int options) throws IOException {
        int count;
        int pos;
        ByteBuffer buf;
        int remaining = dst.remaining();
        if (remaining == 0) {
            return 0;
        }
        FileDescriptor fdesc = this.validFdOrException();
        int dstPos = dst.position();
        boolean direct = dst.isDirect();
        if (direct) {
            buf = dst;
            pos = dstPos;
        } else {
            buf = this.getThreadLocalDirectByteBuffer(remaining);
            remaining = Math.min(remaining, buf.remaining());
            pos = buf.position();
        }
        if (!this.blocking) {
            options |= 4;
        }
        if ((count = NativeUnixSocket.receive(fdesc, buf, pos, remaining, socketAddressBuffer, options, this.ancillaryDataSupport, 0)) == -1) {
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
            try {
                while (buf.hasRemaining()) {
                    dst.put(buf);
                }
            }
            finally {
                if (count < oldLimit) {
                    buf.limit(oldLimit);
                }
            }
        }
        return count;
    }

    int write(ByteBuffer src) throws IOException {
        return this.write(src, null, 0);
    }

    int write(ByteBuffer src, SocketAddress target, int options) throws IOException {
        int bufPos;
        ByteBuffer buf;
        int addressToLen;
        ByteBuffer addressTo;
        int remaining = src.remaining();
        if (remaining == 0) {
            return 0;
        }
        FileDescriptor fdesc = this.validFdOrException();
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
            buf = this.getThreadLocalDirectByteBuffer(remaining);
            remaining = Math.min(remaining, buf.remaining());
            bufPos = buf.position();
            while (src.hasRemaining() && buf.hasRemaining()) {
                buf.put(src);
            }
            buf.position(bufPos);
        }
        if (this.datagramMode) {
            options |= 0x10;
        }
        int written = NativeUnixSocket.send(fdesc, buf, bufPos, remaining, addressTo, addressToLen, options, this.ancillaryDataSupport);
        src.position(pos + written);
        return written;
    }

    ByteBuffer getThreadLocalDirectByteBuffer(int capacity) {
        ByteBuffer buffer;
        if (capacity > TL_BUFFER_MAX_CAPACITY && TL_BUFFER_MAX_CAPACITY > 0) {
            return ByteBuffer.allocateDirect(capacity);
        }
        if (capacity < 8192) {
            capacity = 8192;
        }
        if ((buffer = TL_BUFFER.get()) == null || capacity > buffer.capacity()) {
            buffer = ByteBuffer.allocateDirect(capacity);
            TL_BUFFER.set(buffer);
        }
        buffer.clear();
        return buffer;
    }

    void implConfigureBlocking(boolean block) throws IOException {
        NativeUnixSocket.configureBlocking(this.validFdOrException(), block);
        this.blocking = block;
    }

    boolean isBlocking() {
        return this.blocking;
    }
}

