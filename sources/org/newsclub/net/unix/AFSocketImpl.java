package org.newsclub.net.unix;

import java.io.EOFException;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.SocketOption;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.newsclub.net.unix.AFSocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSocketImpl.class */
public abstract class AFSocketImpl<A extends AFSocketAddress> extends SocketImplShim {
    private static final int SHUTDOWN_RD_WR = 3;
    private final AFSocketStreamCore core;
    private final AFInputStream in;
    private final AFOutputStream out;
    private final AFAddressFamily<A> addressFamily;
    final AncillaryDataSupport ancillaryDataSupport = new AncillaryDataSupport();
    private final AtomicBoolean bound = new AtomicBoolean(false);
    private Boolean createType = null;
    private final AtomicBoolean connected = new AtomicBoolean(false);
    private volatile boolean closedInputStream = false;
    private volatile boolean closedOutputStream = false;
    private boolean reuseAddr = true;
    private final AtomicInteger socketTimeout = new AtomicInteger(0);
    private int shutdownState = 0;
    private AFSocketImplExtensions<A> implExtensions = null;

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSocketImpl$AFSocketStreamCore.class */
    static final class AFSocketStreamCore extends AFSocketCore {
        AFSocketStreamCore(AFSocketImpl<?> observed, FileDescriptor fd, AncillaryDataSupport ancillaryDataSupport, AFAddressFamily<?> af) {
            super(observed, fd, ancillaryDataSupport, af, false);
        }

        void createSocket(FileDescriptor fdTarget, AFSocketType type) throws IOException {
            NativeUnixSocket.createSocket(fdTarget, addressFamily().getDomain(), type.getId());
        }

        @Override // org.newsclub.net.unix.AFSocketCore
        protected void unblockAccepts() {
            if (this.socketAddress == null || this.socketAddress.getBytes() == null || this.inode.get() < 0) {
                return;
            }
            while (hasPendingAccepts()) {
                try {
                    FileDescriptor tmpFd = new FileDescriptor();
                    try {
                        createSocket(tmpFd, AFSocketType.SOCK_STREAM);
                        ByteBuffer ab = this.socketAddress.getNativeAddressDirectBuffer();
                        NativeUnixSocket.connect(ab, ab.limit(), tmpFd, this.inode.get());
                        if (isShutdownOnClose()) {
                            try {
                                NativeUnixSocket.shutdown(tmpFd, 2);
                            } catch (Exception e) {
                            }
                        }
                        try {
                            NativeUnixSocket.close(tmpFd);
                        } catch (Exception e2) {
                        }
                    } catch (IOException e3) {
                        return;
                    }
                } catch (RuntimeException e4) {
                }
                try {
                    Thread.sleep(5L);
                } catch (InterruptedException e5) {
                }
            }
        }
    }

    protected AFSocketImpl(AFAddressFamily<A> addressFamily, FileDescriptor fdObj) {
        this.addressFamily = addressFamily;
        this.address = InetAddress.getLoopbackAddress();
        this.core = new AFSocketStreamCore(this, fdObj, this.ancillaryDataSupport, addressFamily);
        this.fd = this.core.fd;
        this.in = newInputStream();
        this.out = newOutputStream();
    }

    protected final AFInputStream newInputStream() {
        return new AFInputStreamImpl();
    }

    protected final AFOutputStream newOutputStream() {
        return new AFOutputStreamImpl();
    }

    final FileDescriptor getFD() {
        return this.fd;
    }

    final boolean isConnected() {
        if (this.connected.get()) {
            return true;
        }
        if (!isClosed() && this.core.isConnected(false)) {
            this.connected.set(true);
            return true;
        }
        return false;
    }

    final boolean isBound() {
        if (this.bound.get()) {
            return true;
        }
        if (!isClosed() && this.core.isConnected(true)) {
            this.bound.set(true);
            return true;
        }
        return false;
    }

    final AFSocketCore getCore() {
        return this.core;
    }

    boolean isClosed() {
        return this.core.isClosed();
    }

    @Override // java.net.SocketImpl
    protected final void accept(SocketImpl socket) throws IOException {
        accept0(socket);
    }

    final boolean accept0(SocketImpl socket) throws IOException {
        FileDescriptor fdesc = this.core.validFdOrException();
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (!isBound()) {
            throw new SocketException("Socket is not bound");
        }
        AFSocketAddress socketAddress = this.core.socketAddress;
        AFSocketAddress boundSocketAddress = getLocalSocketAddress();
        if (boundSocketAddress != null) {
            socketAddress = boundSocketAddress;
            this.core.socketAddress = boundSocketAddress;
        }
        if (socketAddress == null) {
            throw new SocketException("Socket is not bound");
        }
        AFSocketImpl<A> si = (AFSocketImpl) socket;
        this.core.incPendingAccepts();
        try {
            ByteBuffer ab = socketAddress.getNativeAddressDirectBuffer();
            try {
            } catch (SocketException e) {
                if (!isBound() || isClosed()) {
                    if (getCore().isShutdownOnClose()) {
                        try {
                            NativeUnixSocket.shutdown(si.fd, 2);
                        } catch (Exception e2) {
                        }
                    }
                    try {
                        NativeUnixSocket.close(si.fd);
                    } catch (Exception e3) {
                    }
                    if (e != null) {
                        throw e;
                    }
                    throw new SocketClosedException("Socket is closed");
                }
                if (e != null) {
                    throw e;
                }
            } catch (Throwable th) {
                if (isBound() && !isClosed()) {
                    if (0 != 0) {
                        throw null;
                    }
                    throw th;
                }
                if (getCore().isShutdownOnClose()) {
                    try {
                        NativeUnixSocket.shutdown(si.fd, 2);
                    } catch (Exception e4) {
                    }
                }
                try {
                    NativeUnixSocket.close(si.fd);
                } catch (Exception e5) {
                }
                if (0 != 0) {
                    throw null;
                }
                throw new SocketClosedException("Socket is closed");
            }
            if (!NativeUnixSocket.accept(ab, ab.limit(), fdesc, si.fd, this.core.inode.get(), this.socketTimeout.get())) {
                if (isBound() && !isClosed()) {
                    if (0 != 0) {
                        throw null;
                    }
                    return false;
                }
                if (getCore().isShutdownOnClose()) {
                    try {
                        NativeUnixSocket.shutdown(si.fd, 2);
                    } catch (Exception e6) {
                    }
                }
                try {
                    NativeUnixSocket.close(si.fd);
                } catch (Exception e7) {
                }
                if (0 != 0) {
                    throw null;
                }
                throw new SocketClosedException("Socket is closed");
            }
            if (!isBound() || isClosed()) {
                if (getCore().isShutdownOnClose()) {
                    try {
                        NativeUnixSocket.shutdown(si.fd, 2);
                    } catch (Exception e8) {
                    }
                }
                try {
                    NativeUnixSocket.close(si.fd);
                } catch (Exception e9) {
                }
                if (0 != 0) {
                    throw null;
                }
                throw new SocketClosedException("Socket is closed");
            }
            if (0 != 0) {
                throw null;
            }
            this.core.decPendingAccepts();
            si.setSocketAddress(socketAddress);
            si.connected.set(true);
            return true;
        } finally {
            this.core.decPendingAccepts();
        }
    }

    final void setSocketAddress(AFSocketAddress socketAddress) {
        if (socketAddress == null) {
            this.core.socketAddress = null;
            this.address = null;
            this.localport = -1;
        } else {
            this.core.socketAddress = socketAddress;
            this.address = socketAddress.getAddress();
            if (this.localport <= 0) {
                this.localport = socketAddress.getPort();
            }
        }
    }

    @Override // java.net.SocketImpl
    protected final int available() throws IOException {
        FileDescriptor fdesc = this.core.validFdOrException();
        return NativeUnixSocket.available(fdesc, this.core.getThreadLocalDirectByteBuffer(0));
    }

    final void bind(SocketAddress addr, int options) throws IOException {
        if (addr == null) {
            throw new IllegalArgumentException("Cannot bind to null address");
        }
        if (!(addr instanceof AFSocketAddress)) {
            throw new SocketException("Cannot bind to this type of address: " + addr.getClass());
        }
        this.bound.set(true);
        if (addr == AFSocketAddress.INTERNAL_DUMMY_BIND) {
            this.core.inode.set(0L);
            return;
        }
        AFSocketAddress socketAddress = (AFSocketAddress) addr;
        setSocketAddress(socketAddress);
        ByteBuffer ab = socketAddress.getNativeAddressDirectBuffer();
        this.core.inode.set(NativeUnixSocket.bind(ab, ab.limit(), this.fd, options));
        this.core.validFdOrException();
    }

    @Override // java.net.SocketImpl
    protected final void bind(InetAddress host, int port) throws IOException {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkClose() throws IOException {
        if (this.closedInputStream && this.closedOutputStream) {
            close();
        }
    }

    @Override // java.net.SocketImpl
    protected final void close() throws IOException {
        shutdown();
        this.core.runCleaner();
    }

    @Override // java.net.SocketImpl
    protected final void connect(String host, int port) throws IOException {
        throw new SocketException("Cannot bind to this type of address: " + InetAddress.class);
    }

    @Override // java.net.SocketImpl
    protected final void connect(InetAddress address, int port) throws IOException {
        throw new SocketException("Cannot bind to this type of address: " + InetAddress.class);
    }

    @Override // java.net.SocketImpl
    protected final void connect(SocketAddress addr, int connectTimeout) throws IOException {
        connect0(addr, connectTimeout);
    }

    final boolean connect0(SocketAddress addr, int connectTimeout) throws IOException {
        if (addr == AFSocketAddress.INTERNAL_DUMMY_CONNECT) {
            this.connected.set(true);
            return true;
        }
        if (addr == AFSocketAddress.INTERNAL_DUMMY_DONT_CONNECT) {
            return false;
        }
        if (!(addr instanceof AFSocketAddress)) {
            throw new SocketException("Cannot connect to this type of address: " + addr.getClass());
        }
        AFSocketAddress socketAddress = (AFSocketAddress) addr;
        ByteBuffer ab = socketAddress.getNativeAddressDirectBuffer();
        boolean success = false;
        boolean ignoreSpuriousTimeout = true;
        do {
            try {
                success = NativeUnixSocket.connect(ab, ab.limit(), this.fd, -1L);
                break;
            } catch (SocketTimeoutException e) {
                if (ignoreSpuriousTimeout) {
                    Object o = getOption(4102);
                    if (o instanceof Integer) {
                        if (((Integer) o).intValue() == 0) {
                            ignoreSpuriousTimeout = false;
                        }
                    } else if (o == null) {
                        ignoreSpuriousTimeout = false;
                    }
                }
                throw e;
            }
        } while (!Thread.interrupted());
        if (success) {
            setSocketAddress(socketAddress);
            this.connected.set(true);
        }
        this.core.validFdOrException();
        return success;
    }

    @Override // java.net.SocketImpl
    protected final void create(boolean stream) throws IOException {
        if (isClosed()) {
            throw new SocketException("Already closed");
        }
        if (this.fd.valid()) {
            if (this.createType != null) {
                if (this.createType.booleanValue() != stream) {
                    throw new IllegalStateException("Already created with different mode");
                }
                return;
            } else {
                this.createType = Boolean.valueOf(stream);
                return;
            }
        }
        this.createType = Boolean.valueOf(stream);
        createSocket(this.fd, stream ? AFSocketType.SOCK_STREAM : AFSocketType.SOCK_DGRAM);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // java.net.SocketImpl
    public final AFInputStream getInputStream() throws IOException {
        if (!isConnected() && !isBound()) {
            throw new SocketClosedException("Not connected/not bound");
        }
        this.core.validFdOrException();
        return this.in;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // java.net.SocketImpl
    public final AFOutputStream getOutputStream() throws IOException {
        if (!isClosed() && !isBound()) {
            throw new SocketClosedException("Not connected/not bound");
        }
        this.core.validFdOrException();
        return this.out;
    }

    @Override // java.net.SocketImpl
    protected final void listen(int backlog) throws IOException {
        FileDescriptor fdesc = this.core.validFdOrException();
        if (backlog <= 0) {
            backlog = 50;
        }
        NativeUnixSocket.listen(fdesc, backlog);
    }

    @Override // java.net.SocketImpl
    protected final boolean supportsUrgentData() {
        return false;
    }

    @Override // java.net.SocketImpl
    protected final void sendUrgentData(int data) throws IOException {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSocketImpl$AFInputStreamImpl.class */
    private final class AFInputStreamImpl extends AFInputStream {
        private volatile boolean streamClosed;
        private final AtomicBoolean eofReached;
        private final int opt;

        private AFInputStreamImpl() {
            this.streamClosed = false;
            this.eofReached = new AtomicBoolean(false);
            this.opt = AFSocketImpl.this.core.isBlocking() ? 0 : 4;
        }

        @Override // java.io.InputStream
        public int read(byte[] buf, int off, int len) throws IOException {
            if (this.streamClosed) {
                throw new SocketClosedException("This InputStream has already been closed.");
            }
            if (!this.eofReached.get()) {
                FileDescriptor fdesc = AFSocketImpl.this.core.validFdOrException();
                if (len == 0) {
                    return 0;
                }
                if (off < 0 || len < 0 || len > buf.length - off) {
                    throw new IndexOutOfBoundsException();
                }
                try {
                    return NativeUnixSocket.read(fdesc, buf, off, len, this.opt, AFSocketImpl.this.ancillaryDataSupport, AFSocketImpl.this.socketTimeout.get());
                } catch (EOFException e) {
                    this.eofReached.set(true);
                    throw e;
                }
            }
            return -1;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            FileDescriptor fdesc = AFSocketImpl.this.core.validFdOrException();
            if (this.eofReached.get()) {
                return -1;
            }
            int byteRead = NativeUnixSocket.read(fdesc, null, 0, 1, this.opt, AFSocketImpl.this.ancillaryDataSupport, AFSocketImpl.this.socketTimeout.get());
            if (byteRead < 0) {
                this.eofReached.set(true);
                return -1;
            }
            return byteRead;
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public synchronized void close() throws IOException {
            this.streamClosed = true;
            FileDescriptor fdesc = AFSocketImpl.this.core.validFd();
            if (fdesc != null && AFSocketImpl.this.getCore().isShutdownOnClose()) {
                NativeUnixSocket.shutdown(fdesc, 0);
            }
            AFSocketImpl.this.closedInputStream = true;
            AFSocketImpl.this.checkClose();
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            if (this.streamClosed) {
                throw new SocketClosedException("This InputStream has already been closed.");
            }
            return AFSocketImpl.this.available();
        }

        @Override // org.newsclub.net.unix.FileDescriptorAccess
        public FileDescriptor getFileDescriptor() throws IOException {
            return AFSocketImpl.this.getFD();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean checkWriteInterruptedException(int bytesTransferred) throws InterruptedIOException {
        if (Thread.interrupted()) {
            InterruptedIOException ex = new InterruptedIOException("Thread interrupted during write");
            ex.bytesTransferred = bytesTransferred;
            Thread.currentThread().interrupt();
            throw ex;
        }
        return true;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSocketImpl$AFOutputStreamImpl.class */
    private final class AFOutputStreamImpl extends AFOutputStream {
        private volatile boolean streamClosed;
        private final int opt;

        private AFOutputStreamImpl() {
            this.streamClosed = false;
            this.opt = AFSocketImpl.this.core.isBlocking() ? 0 : 4;
        }

        /* JADX WARN: Incorrect condition in loop: B:4:0x001f */
        @Override // java.io.OutputStream
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void write(int r8) throws java.io.IOException {
            /*
                r7 = this;
                r0 = r7
                org.newsclub.net.unix.AFSocketImpl r0 = org.newsclub.net.unix.AFSocketImpl.this
                org.newsclub.net.unix.AFSocketImpl$AFSocketStreamCore r0 = org.newsclub.net.unix.AFSocketImpl.access$200(r0)
                java.io.FileDescriptor r0 = r0.validFdOrException()
                r9 = r0
            Lb:
                r0 = r9
                r1 = 0
                r2 = r8
                r3 = 1
                r4 = r7
                int r4 = r4.opt
                r5 = r7
                org.newsclub.net.unix.AFSocketImpl r5 = org.newsclub.net.unix.AFSocketImpl.this
                org.newsclub.net.unix.AncillaryDataSupport r5 = r5.ancillaryDataSupport
                int r0 = org.newsclub.net.unix.NativeUnixSocket.write(r0, r1, r2, r3, r4, r5)
                r10 = r0
                r0 = r10
                if (r0 == 0) goto L25
                goto L2c
            L25:
                r0 = 0
                boolean r0 = org.newsclub.net.unix.AFSocketImpl.access$600(r0)
                if (r0 != 0) goto Lb
            L2c:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.newsclub.net.unix.AFSocketImpl.AFOutputStreamImpl.write(int):void");
        }

        @Override // java.io.OutputStream
        public void write(byte[] buf, int off, int len) throws IOException {
            if (this.streamClosed) {
                throw new SocketException("This OutputStream has already been closed.");
            }
            if (len >= 0 && off >= 0 && len <= buf.length - off) {
                FileDescriptor fdesc = AFSocketImpl.this.core.validFdOrException();
                if (len == 0 && !AFSocket.supports(AFSocketCapability.CAPABILITY_ZERO_LENGTH_SEND)) {
                    return;
                }
                int writtenTotal = 0;
                do {
                    int written = NativeUnixSocket.write(fdesc, buf, off, len, this.opt, AFSocketImpl.this.ancillaryDataSupport);
                    if (written < 0) {
                        if (len == 0) {
                            return;
                        } else {
                            throw new IOException("Unspecific error while writing");
                        }
                    } else {
                        len -= written;
                        off += written;
                        writtenTotal += written;
                        if (len <= 0) {
                            return;
                        }
                    }
                } while (AFSocketImpl.checkWriteInterruptedException(writtenTotal));
                return;
            }
            throw new IndexOutOfBoundsException();
        }

        @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public synchronized void close() throws IOException {
            if (this.streamClosed) {
                return;
            }
            this.streamClosed = true;
            FileDescriptor fdesc = AFSocketImpl.this.core.validFd();
            if (fdesc != null && AFSocketImpl.this.getCore().isShutdownOnClose()) {
                NativeUnixSocket.shutdown(fdesc, 1);
            }
            AFSocketImpl.this.closedOutputStream = true;
            AFSocketImpl.this.checkClose();
        }

        @Override // org.newsclub.net.unix.FileDescriptorAccess
        public FileDescriptor getFileDescriptor() throws IOException {
            return AFSocketImpl.this.getFD();
        }
    }

    @Override // java.net.SocketImpl
    public final String toString() {
        return super.toString() + "[fd=" + this.fd + "; addr=" + this.core.socketAddress + "; connected=" + this.connected + "; bound=" + this.bound + "]";
    }

    private static int expectInteger(Object value) throws SocketException {
        if (value == null) {
            throw ((SocketException) new SocketException("Value must not be null").initCause(new NullPointerException()));
        }
        try {
            return ((Integer) value).intValue();
        } catch (ClassCastException e) {
            throw ((SocketException) new SocketException("Unsupported value: " + value).initCause(e));
        }
    }

    private static int expectBoolean(Object value) throws SocketException {
        if (value == null) {
            throw ((SocketException) new SocketException("Value must not be null").initCause(new NullPointerException()));
        }
        try {
            return ((Boolean) value).booleanValue() ? 1 : 0;
        } catch (ClassCastException e) {
            throw ((SocketException) new SocketException("Unsupported value: " + value).initCause(e));
        }
    }

    @Override // java.net.SocketOptions
    public Object getOption(int optID) throws SocketException {
        return getOption0(optID);
    }

    private Object getOption0(int optID) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (optID == 4) {
            return Boolean.valueOf(this.reuseAddr);
        }
        FileDescriptor fdesc = this.core.validFdOrException();
        return getOptionDefault(fdesc, optID, this.socketTimeout, this.addressFamily);
    }

    static final Object getOptionDefault(FileDescriptor fdesc, int optID, AtomicInteger acceptTimeout, AFAddressFamily<?> af) throws SocketException {
        try {
            switch (optID) {
                case 1:
                    return Boolean.valueOf(NativeUnixSocket.getSocketOptionInt(fdesc, optID) != 0);
                case SHUTDOWN_RD_WR /* 3 */:
                    return 0;
                case 4:
                    return false;
                case 8:
                    try {
                        return Boolean.valueOf(NativeUnixSocket.getSocketOptionInt(fdesc, optID) != 0);
                    } catch (SocketException e) {
                        return false;
                    }
                case 15:
                    return AFSocketAddress.getInetAddress(fdesc, false, af);
                case 128:
                case 4097:
                case 4098:
                    return Integer.valueOf(NativeUnixSocket.getSocketOptionInt(fdesc, optID));
                case 4102:
                    int v = Math.max(NativeUnixSocket.getSocketOptionInt(fdesc, 4101), NativeUnixSocket.getSocketOptionInt(fdesc, 4102));
                    if (v == -1) {
                        return 0;
                    }
                    return Integer.valueOf(Math.max(acceptTimeout == null ? 0 : acceptTimeout.get(), v));
                default:
                    throw new SocketException("Unsupported option: " + optID);
            }
        } catch (SocketException e2) {
            throw e2;
        } catch (Exception e3) {
            throw ((SocketException) new SocketException("Could not get option").initCause(e3));
        }
    }

    @Override // java.net.SocketOptions
    public void setOption(int optID, Object value) throws SocketException {
        setOption0(optID, value);
    }

    private void setOption0(int optID, Object value) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (optID == 4) {
            this.reuseAddr = expectBoolean(value) != 0;
        } else {
            FileDescriptor fdesc = this.core.validFdOrException();
            setOptionDefault(fdesc, optID, value, this.socketTimeout);
        }
    }

    protected final Object getOptionLenient(int optID) throws SocketException {
        try {
            return getOption0(optID);
        } catch (SocketException e) {
            switch (optID) {
                case 1:
                case 8:
                    return false;
                default:
                    throw e;
            }
        }
    }

    protected final void setOptionLenient(int optID, Object value) throws SocketException {
        try {
            setOption0(optID, value);
        } catch (SocketException e) {
            switch (optID) {
                case 1:
                    return;
                default:
                    throw e;
            }
        }
    }

    static final void setOptionDefault(FileDescriptor fdesc, int optID, Object value, AtomicInteger acceptTimeout) throws SocketException {
        try {
            switch (optID) {
                case 1:
                    NativeUnixSocket.setSocketOptionInt(fdesc, optID, expectBoolean(value));
                    return;
                case SHUTDOWN_RD_WR /* 3 */:
                    return;
                case 4:
                    return;
                case 8:
                    try {
                        NativeUnixSocket.setSocketOptionInt(fdesc, optID, expectBoolean(value));
                        return;
                    } catch (SocketException e) {
                        return;
                    }
                case 128:
                    if (value instanceof Boolean) {
                        boolean b = ((Boolean) value).booleanValue();
                        if (b) {
                            throw new SocketException("Only accepting Boolean.FALSE here");
                        }
                        NativeUnixSocket.setSocketOptionInt(fdesc, optID, -1);
                        return;
                    }
                    NativeUnixSocket.setSocketOptionInt(fdesc, optID, expectInteger(value));
                    return;
                case 4097:
                case 4098:
                    NativeUnixSocket.setSocketOptionInt(fdesc, optID, expectInteger(value));
                    return;
                case 4102:
                    int timeout = expectInteger(value);
                    try {
                        NativeUnixSocket.setSocketOptionInt(fdesc, 4101, timeout);
                        break;
                    } catch (InvalidArgumentSocketException e2) {
                    }
                    try {
                        NativeUnixSocket.setSocketOptionInt(fdesc, 4102, timeout);
                        break;
                    } catch (InvalidArgumentSocketException e3) {
                    }
                    if (acceptTimeout != null) {
                        acceptTimeout.set(timeout);
                        return;
                    }
                    return;
                default:
                    throw new SocketException("Unsupported option: " + optID);
            }
        } catch (SocketException e4) {
            throw e4;
        } catch (Exception e5) {
            throw ((SocketException) new SocketException("Error while setting option").initCause(e5));
        }
    }

    protected final void shutdown() throws IOException {
        FileDescriptor fdesc = this.core.validFd();
        if (fdesc != null) {
            NativeUnixSocket.shutdown(fdesc, 2);
            this.shutdownState = 0;
        }
    }

    @Override // java.net.SocketImpl
    protected final void shutdownInput() throws IOException {
        FileDescriptor fdesc = this.core.validFd();
        if (fdesc != null) {
            NativeUnixSocket.shutdown(fdesc, 0);
            this.shutdownState |= 1;
            if (this.shutdownState == SHUTDOWN_RD_WR) {
                NativeUnixSocket.shutdown(fdesc, 2);
                this.shutdownState = 0;
            }
        }
    }

    @Override // java.net.SocketImpl
    protected final void shutdownOutput() throws IOException {
        FileDescriptor fdesc = this.core.validFd();
        if (fdesc != null) {
            NativeUnixSocket.shutdown(fdesc, 1);
            this.shutdownState |= 4;
            if (this.shutdownState == SHUTDOWN_RD_WR) {
                NativeUnixSocket.shutdown(fdesc, 2);
                this.shutdownState = 0;
            }
        }
    }

    final int getAncillaryReceiveBufferSize() {
        return this.ancillaryDataSupport.getAncillaryReceiveBufferSize();
    }

    final void setAncillaryReceiveBufferSize(int size) {
        this.ancillaryDataSupport.setAncillaryReceiveBufferSize(size);
    }

    final void ensureAncillaryReceiveBufferSize(int minSize) {
        this.ancillaryDataSupport.ensureAncillaryReceiveBufferSize(minSize);
    }

    AncillaryDataSupport getAncillaryDataSupport() {
        return this.ancillaryDataSupport;
    }

    final SocketAddress receive(ByteBuffer dst) throws IOException {
        return this.core.receive(dst);
    }

    final int send(ByteBuffer src, SocketAddress target) throws IOException {
        return this.core.write(src, target, 0);
    }

    final int read(ByteBuffer dst, ByteBuffer socketAddressBuffer) throws IOException {
        return this.core.read(dst, socketAddressBuffer, 0);
    }

    final int write(ByteBuffer src) throws IOException {
        return this.core.write(src);
    }

    @Override // java.net.SocketImpl
    protected final FileDescriptor getFileDescriptor() {
        return this.core.fd;
    }

    final void updatePorts(int local, int remote) {
        this.localport = local;
        if (remote >= 0) {
            this.port = remote;
        }
    }

    final A getLocalSocketAddress() {
        return (A) AFSocketAddress.getSocketAddress(getFileDescriptor(), false, this.localport, this.addressFamily);
    }

    final A getRemoteSocketAddress() {
        return (A) AFSocketAddress.getSocketAddress(getFileDescriptor(), true, this.port, this.addressFamily);
    }

    final int getLocalPort1() {
        return this.localport;
    }

    final int getRemotePort() {
        return this.port;
    }

    @Override // java.net.SocketImpl
    protected final InetAddress getInetAddress() {
        AFSocketAddress remoteSocketAddress = getRemoteSocketAddress();
        if (remoteSocketAddress == null) {
            return InetAddress.getLoopbackAddress();
        }
        return remoteSocketAddress.getInetAddress();
    }

    final void createSocket(FileDescriptor fdTarget, AFSocketType type) throws IOException {
        NativeUnixSocket.createSocket(fdTarget, this.addressFamily.getDomain(), type.getId());
    }

    final AFAddressFamily<A> getAddressFamily() {
        return this.addressFamily;
    }

    @Override // org.newsclub.net.unix.SocketImplShim, java.net.SocketImpl
    protected <T> void setOption(SocketOption<T> name, T value) throws IOException {
        if (name instanceof AFSocketOption) {
            getCore().setOption((AFSocketOption) name, value);
            return;
        }
        Integer optionId = SocketOptionsMapper.resolve(name);
        if (optionId == null) {
            super.setOption(name, value);
        } else {
            setOption(optionId.intValue(), value);
        }
    }

    @Override // org.newsclub.net.unix.SocketImplShim, java.net.SocketImpl
    protected <T> T getOption(SocketOption<T> socketOption) throws IOException {
        if (socketOption instanceof AFSocketOption) {
            return (T) getCore().getOption((AFSocketOption) socketOption);
        }
        Integer numResolve = SocketOptionsMapper.resolve(socketOption);
        if (numResolve == null) {
            return (T) super.getOption(socketOption);
        }
        return (T) getOption(numResolve.intValue());
    }

    @Override // org.newsclub.net.unix.SocketImplShim, java.net.SocketImpl
    protected Set<SocketOption<?>> supportedOptions() {
        return SocketOptionsMapper.SUPPORTED_SOCKET_OPTIONS;
    }

    protected final synchronized AFSocketImplExtensions<A> getImplExtensions() {
        if (this.implExtensions == null) {
            this.implExtensions = this.addressFamily.initImplExtensions(this.ancillaryDataSupport);
        }
        return this.implExtensions;
    }
}
