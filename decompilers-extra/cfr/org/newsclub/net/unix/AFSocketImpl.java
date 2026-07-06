/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jdt.annotation.NonNull
 *  org.eclipse.jdt.annotation.Nullable
 */
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
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.newsclub.net.unix.AFAddressFamily;
import org.newsclub.net.unix.AFInputStream;
import org.newsclub.net.unix.AFOutputStream;
import org.newsclub.net.unix.AFSocket;
import org.newsclub.net.unix.AFSocketAddress;
import org.newsclub.net.unix.AFSocketCapability;
import org.newsclub.net.unix.AFSocketCore;
import org.newsclub.net.unix.AFSocketImplExtensions;
import org.newsclub.net.unix.AFSocketOption;
import org.newsclub.net.unix.AFSocketType;
import org.newsclub.net.unix.AncillaryDataSupport;
import org.newsclub.net.unix.InvalidArgumentSocketException;
import org.newsclub.net.unix.NativeUnixSocket;
import org.newsclub.net.unix.SocketClosedException;
import org.newsclub.net.unix.SocketImplShim;
import org.newsclub.net.unix.SocketOptionsMapper;

public abstract class AFSocketImpl<A extends AFSocketAddress>
extends SocketImplShim {
    private static final int SHUTDOWN_RD_WR = 3;
    private final AFSocketStreamCore core;
    final AncillaryDataSupport ancillaryDataSupport = new AncillaryDataSupport();
    private final AtomicBoolean bound = new AtomicBoolean(false);
    private Boolean createType = null;
    private final AtomicBoolean connected = new AtomicBoolean(false);
    private volatile boolean closedInputStream = false;
    private volatile boolean closedOutputStream = false;
    private final AFInputStream in;
    private final AFOutputStream out;
    private boolean reuseAddr = true;
    private final AtomicInteger socketTimeout = new AtomicInteger(0);
    private final AFAddressFamily<A> addressFamily;
    private int shutdownState = 0;
    private AFSocketImplExtensions<A> implExtensions = null;

    protected AFSocketImpl(AFAddressFamily<@NonNull A> addressFamily, FileDescriptor fdObj) {
        this.addressFamily = addressFamily;
        this.address = InetAddress.getLoopbackAddress();
        this.core = new AFSocketStreamCore(this, fdObj, this.ancillaryDataSupport, addressFamily);
        this.fd = this.core.fd;
        this.in = this.newInputStream();
        this.out = this.newOutputStream();
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
        if (this.isClosed()) {
            return false;
        }
        if (this.core.isConnected(false)) {
            this.connected.set(true);
            return true;
        }
        return false;
    }

    final boolean isBound() {
        if (this.bound.get()) {
            return true;
        }
        if (this.isClosed()) {
            return false;
        }
        if (this.core.isConnected(true)) {
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

    @Override
    protected final void accept(SocketImpl socket) throws IOException {
        this.accept0(socket);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    final boolean accept0(SocketImpl socket) throws IOException {
        FileDescriptor fdesc = this.core.validFdOrException();
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (!this.isBound()) {
            throw new SocketException("Socket is not bound");
        }
        AFSocketAddress socketAddress = this.core.socketAddress;
        A boundSocketAddress = this.getLocalSocketAddress();
        if (boundSocketAddress != null) {
            this.core.socketAddress = socketAddress = boundSocketAddress;
        }
        if (socketAddress == null) {
            throw new SocketException("Socket is not bound");
        }
        AFSocketImpl si = (AFSocketImpl)socket;
        this.core.incPendingAccepts();
        try {
            ByteBuffer ab = socketAddress.getNativeAddressDirectBuffer();
            SocketException caught = null;
            try {
                if (!NativeUnixSocket.accept(ab, ab.limit(), fdesc, si.fd, this.core.inode.get(), this.socketTimeout.get())) {
                    boolean bl = false;
                    return bl;
                }
            }
            catch (SocketException e) {
                caught = e;
                return (boolean)caught;
            }
            finally {
                if (!this.isBound() || this.isClosed()) {
                    if (this.getCore().isShutdownOnClose()) {
                        try {
                            NativeUnixSocket.shutdown(si.fd, 2);
                        }
                        catch (Exception exception) {}
                    }
                    try {
                        NativeUnixSocket.close(si.fd);
                    }
                    catch (Exception exception) {}
                    if (caught != null) {
                        throw caught;
                    }
                    throw new SocketClosedException("Socket is closed");
                }
                if (caught != null) {
                    throw caught;
                }
            }
        }
        finally {
            this.core.decPendingAccepts();
        }
        si.setSocketAddress(socketAddress);
        si.connected.set(true);
        return true;
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

    @Override
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
        AFSocketAddress socketAddress = (AFSocketAddress)addr;
        this.setSocketAddress(socketAddress);
        ByteBuffer ab = socketAddress.getNativeAddressDirectBuffer();
        this.core.inode.set(NativeUnixSocket.bind(ab, ab.limit(), this.fd, options));
        this.core.validFdOrException();
    }

    @Override
    protected final void bind(InetAddress host, int port) throws IOException {
    }

    private void checkClose() throws IOException {
        if (this.closedInputStream && this.closedOutputStream) {
            this.close();
        }
    }

    @Override
    protected final void close() throws IOException {
        this.shutdown();
        this.core.runCleaner();
    }

    @Override
    protected final void connect(String host, int port) throws IOException {
        throw new SocketException("Cannot bind to this type of address: " + InetAddress.class);
    }

    @Override
    protected final void connect(InetAddress address, int port) throws IOException {
        throw new SocketException("Cannot bind to this type of address: " + InetAddress.class);
    }

    @Override
    protected final void connect(SocketAddress addr, int connectTimeout) throws IOException {
        this.connect0(addr, connectTimeout);
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
        AFSocketAddress socketAddress = (AFSocketAddress)addr;
        ByteBuffer ab = socketAddress.getNativeAddressDirectBuffer();
        boolean success = false;
        boolean ignoreSpuriousTimeout = true;
        while (true) {
            try {
                success = NativeUnixSocket.connect(ab, ab.limit(), this.fd, -1L);
            }
            catch (SocketTimeoutException e) {
                if (ignoreSpuriousTimeout) {
                    Object o = this.getOption(4102);
                    if (o instanceof Integer) {
                        if ((Integer)o == 0) {
                            ignoreSpuriousTimeout = false;
                            continue;
                        }
                    } else if (o == null) {
                        ignoreSpuriousTimeout = false;
                        continue;
                    }
                }
                throw e;
                if (!Thread.interrupted()) continue;
            }
            break;
        }
        if (success) {
            this.setSocketAddress(socketAddress);
            this.connected.set(true);
        }
        this.core.validFdOrException();
        return success;
    }

    @Override
    protected final void create(boolean stream) throws IOException {
        if (this.isClosed()) {
            throw new SocketException("Already closed");
        }
        if (this.fd.valid()) {
            if (this.createType != null) {
                if (this.createType != stream) {
                    throw new IllegalStateException("Already created with different mode");
                }
            } else {
                this.createType = stream;
            }
            return;
        }
        this.createType = stream;
        this.createSocket(this.fd, stream ? AFSocketType.SOCK_STREAM : AFSocketType.SOCK_DGRAM);
    }

    @Override
    protected final AFInputStream getInputStream() throws IOException {
        if (!this.isConnected() && !this.isBound()) {
            throw new SocketClosedException("Not connected/not bound");
        }
        this.core.validFdOrException();
        return this.in;
    }

    @Override
    protected final AFOutputStream getOutputStream() throws IOException {
        if (!this.isClosed() && !this.isBound()) {
            throw new SocketClosedException("Not connected/not bound");
        }
        this.core.validFdOrException();
        return this.out;
    }

    @Override
    protected final void listen(int backlog) throws IOException {
        FileDescriptor fdesc = this.core.validFdOrException();
        if (backlog <= 0) {
            backlog = 50;
        }
        NativeUnixSocket.listen(fdesc, backlog);
    }

    @Override
    protected final boolean supportsUrgentData() {
        return false;
    }

    @Override
    protected final void sendUrgentData(int data) throws IOException {
        throw new UnsupportedOperationException();
    }

    private static boolean checkWriteInterruptedException(int bytesTransferred) throws InterruptedIOException {
        if (Thread.interrupted()) {
            InterruptedIOException ex = new InterruptedIOException("Thread interrupted during write");
            ex.bytesTransferred = bytesTransferred;
            Thread.currentThread().interrupt();
            throw ex;
        }
        return true;
    }

    @Override
    public final String toString() {
        return super.toString() + "[fd=" + this.fd + "; addr=" + this.core.socketAddress + "; connected=" + this.connected + "; bound=" + this.bound + "]";
    }

    private static int expectInteger(Object value) throws SocketException {
        if (value == null) {
            throw (SocketException)new SocketException("Value must not be null").initCause(new NullPointerException());
        }
        try {
            return (Integer)value;
        }
        catch (ClassCastException e) {
            throw (SocketException)new SocketException("Unsupported value: " + value).initCause(e);
        }
    }

    private static int expectBoolean(Object value) throws SocketException {
        if (value == null) {
            throw (SocketException)new SocketException("Value must not be null").initCause(new NullPointerException());
        }
        try {
            return (Boolean)value != false ? 1 : 0;
        }
        catch (ClassCastException e) {
            throw (SocketException)new SocketException("Unsupported value: " + value).initCause(e);
        }
    }

    @Override
    public Object getOption(int optID) throws SocketException {
        return this.getOption0(optID);
    }

    private Object getOption0(int optID) throws SocketException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (optID == 4) {
            return this.reuseAddr;
        }
        FileDescriptor fdesc = this.core.validFdOrException();
        return AFSocketImpl.getOptionDefault(fdesc, optID, this.socketTimeout, this.addressFamily);
    }

    static final Object getOptionDefault(FileDescriptor fdesc, int optID, AtomicInteger acceptTimeout, AFAddressFamily<?> af) throws SocketException {
        try {
            switch (optID) {
                case 8: {
                    try {
                        return NativeUnixSocket.getSocketOptionInt(fdesc, optID) != 0;
                    }
                    catch (SocketException e) {
                        return false;
                    }
                }
                case 1: {
                    return NativeUnixSocket.getSocketOptionInt(fdesc, optID) != 0;
                }
                case 4102: {
                    int v = Math.max(NativeUnixSocket.getSocketOptionInt(fdesc, 4101), NativeUnixSocket.getSocketOptionInt(fdesc, 4102));
                    if (v == -1) {
                        return 0;
                    }
                    return Math.max(acceptTimeout == null ? 0 : acceptTimeout.get(), v);
                }
                case 128: 
                case 4097: 
                case 4098: {
                    return NativeUnixSocket.getSocketOptionInt(fdesc, optID);
                }
                case 3: {
                    return 0;
                }
                case 15: {
                    return AFSocketAddress.getInetAddress(fdesc, false, af);
                }
                case 4: {
                    return false;
                }
            }
            throw new SocketException("Unsupported option: " + optID);
        }
        catch (SocketException e) {
            throw e;
        }
        catch (Exception e) {
            throw (SocketException)new SocketException("Could not get option").initCause(e);
        }
    }

    @Override
    public void setOption(int optID, Object value) throws SocketException {
        this.setOption0(optID, value);
    }

    private void setOption0(int optID, Object value) throws SocketException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (optID == 4) {
            this.reuseAddr = AFSocketImpl.expectBoolean(value) != 0;
            return;
        }
        FileDescriptor fdesc = this.core.validFdOrException();
        AFSocketImpl.setOptionDefault(fdesc, optID, value, this.socketTimeout);
    }

    protected final Object getOptionLenient(int optID) throws SocketException {
        try {
            return this.getOption0(optID);
        }
        catch (SocketException e) {
            switch (optID) {
                case 1: 
                case 8: {
                    return false;
                }
            }
            throw e;
        }
    }

    protected final void setOptionLenient(int optID, Object value) throws SocketException {
        try {
            this.setOption0(optID, value);
        }
        catch (SocketException e) {
            switch (optID) {
                case 1: {
                    return;
                }
            }
            throw e;
        }
    }

    static final void setOptionDefault(FileDescriptor fdesc, int optID, Object value, AtomicInteger acceptTimeout) throws SocketException {
        try {
            switch (optID) {
                case 128: {
                    if (value instanceof Boolean) {
                        boolean b = (Boolean)value;
                        if (b) {
                            throw new SocketException("Only accepting Boolean.FALSE here");
                        }
                        NativeUnixSocket.setSocketOptionInt(fdesc, optID, -1);
                        return;
                    }
                    NativeUnixSocket.setSocketOptionInt(fdesc, optID, AFSocketImpl.expectInteger(value));
                    return;
                }
                case 4102: {
                    int timeout = AFSocketImpl.expectInteger(value);
                    try {
                        NativeUnixSocket.setSocketOptionInt(fdesc, 4101, timeout);
                    }
                    catch (InvalidArgumentSocketException invalidArgumentSocketException) {
                        // empty catch block
                    }
                    try {
                        NativeUnixSocket.setSocketOptionInt(fdesc, 4102, timeout);
                    }
                    catch (InvalidArgumentSocketException invalidArgumentSocketException) {
                        // empty catch block
                    }
                    if (acceptTimeout != null) {
                        acceptTimeout.set(timeout);
                    }
                    return;
                }
                case 4097: 
                case 4098: {
                    NativeUnixSocket.setSocketOptionInt(fdesc, optID, AFSocketImpl.expectInteger(value));
                    return;
                }
                case 8: {
                    try {
                        NativeUnixSocket.setSocketOptionInt(fdesc, optID, AFSocketImpl.expectBoolean(value));
                    }
                    catch (SocketException timeout) {
                        // empty catch block
                    }
                    return;
                }
                case 1: {
                    NativeUnixSocket.setSocketOptionInt(fdesc, optID, AFSocketImpl.expectBoolean(value));
                    return;
                }
                case 3: {
                    return;
                }
                case 4: {
                    return;
                }
            }
            throw new SocketException("Unsupported option: " + optID);
        }
        catch (SocketException e) {
            throw e;
        }
        catch (Exception e) {
            throw (SocketException)new SocketException("Error while setting option").initCause(e);
        }
    }

    protected final void shutdown() throws IOException {
        FileDescriptor fdesc = this.core.validFd();
        if (fdesc != null) {
            NativeUnixSocket.shutdown(fdesc, 2);
            this.shutdownState = 0;
        }
    }

    @Override
    protected final void shutdownInput() throws IOException {
        FileDescriptor fdesc = this.core.validFd();
        if (fdesc != null) {
            NativeUnixSocket.shutdown(fdesc, 0);
            this.shutdownState |= 1;
            if (this.shutdownState == 3) {
                NativeUnixSocket.shutdown(fdesc, 2);
                this.shutdownState = 0;
            }
        }
    }

    @Override
    protected final void shutdownOutput() throws IOException {
        FileDescriptor fdesc = this.core.validFd();
        if (fdesc != null) {
            NativeUnixSocket.shutdown(fdesc, 1);
            this.shutdownState |= 4;
            if (this.shutdownState == 3) {
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

    @Override
    protected final FileDescriptor getFileDescriptor() {
        return this.core.fd;
    }

    final void updatePorts(int local, int remote) {
        this.localport = local;
        if (remote >= 0) {
            this.port = remote;
        }
    }

    final @Nullable A getLocalSocketAddress() {
        return AFSocketAddress.getSocketAddress(this.getFileDescriptor(), false, this.localport, this.addressFamily);
    }

    final @Nullable A getRemoteSocketAddress() {
        return AFSocketAddress.getSocketAddress(this.getFileDescriptor(), true, this.port, this.addressFamily);
    }

    final int getLocalPort1() {
        return this.localport;
    }

    final int getRemotePort() {
        return this.port;
    }

    @Override
    protected final InetAddress getInetAddress() {
        @Nullable A rsa = this.getRemoteSocketAddress();
        if (rsa == null) {
            return InetAddress.getLoopbackAddress();
        }
        return ((AFSocketAddress)rsa).getInetAddress();
    }

    final void createSocket(FileDescriptor fdTarget, AFSocketType type) throws IOException {
        NativeUnixSocket.createSocket(fdTarget, this.addressFamily.getDomain(), type.getId());
    }

    final AFAddressFamily<A> getAddressFamily() {
        return this.addressFamily;
    }

    @Override
    protected <T> void setOption(SocketOption<T> name, T value) throws IOException {
        if (name instanceof AFSocketOption) {
            this.getCore().setOption((AFSocketOption)name, value);
            return;
        }
        Integer optionId = SocketOptionsMapper.resolve(name);
        if (optionId == null) {
            super.setOption(name, value);
        } else {
            this.setOption(optionId, value);
        }
    }

    @Override
    protected <T> T getOption(SocketOption<T> name) throws IOException {
        if (name instanceof AFSocketOption) {
            return this.getCore().getOption((AFSocketOption)name);
        }
        Integer optionId = SocketOptionsMapper.resolve(name);
        if (optionId == null) {
            return super.getOption(name);
        }
        return (T)this.getOption(optionId);
    }

    @Override
    protected Set<SocketOption<?>> supportedOptions() {
        return SocketOptionsMapper.SUPPORTED_SOCKET_OPTIONS;
    }

    protected final synchronized AFSocketImplExtensions<A> getImplExtensions() {
        if (this.implExtensions == null) {
            this.implExtensions = this.addressFamily.initImplExtensions(this.ancillaryDataSupport);
        }
        return this.implExtensions;
    }

    static final class AFSocketStreamCore
    extends AFSocketCore {
        AFSocketStreamCore(AFSocketImpl<?> observed, FileDescriptor fd, AncillaryDataSupport ancillaryDataSupport, AFAddressFamily<?> af) {
            super(observed, fd, ancillaryDataSupport, af, false);
        }

        void createSocket(FileDescriptor fdTarget, AFSocketType type) throws IOException {
            NativeUnixSocket.createSocket(fdTarget, this.addressFamily().getDomain(), type.getId());
        }

        @Override
        protected void unblockAccepts() {
            if (this.socketAddress == null || this.socketAddress.getBytes() == null || this.inode.get() < 0L) {
                return;
            }
            while (this.hasPendingAccepts()) {
                try {
                    FileDescriptor tmpFd = new FileDescriptor();
                    try {
                        this.createSocket(tmpFd, AFSocketType.SOCK_STREAM);
                        ByteBuffer ab = this.socketAddress.getNativeAddressDirectBuffer();
                        NativeUnixSocket.connect(ab, ab.limit(), tmpFd, this.inode.get());
                    }
                    catch (IOException e) {
                        return;
                    }
                    if (this.isShutdownOnClose()) {
                        try {
                            NativeUnixSocket.shutdown(tmpFd, 2);
                        }
                        catch (Exception exception) {
                            // empty catch block
                        }
                    }
                    try {
                        NativeUnixSocket.close(tmpFd);
                    }
                    catch (Exception exception) {}
                }
                catch (RuntimeException runtimeException) {
                    // empty catch block
                }
                try {
                    Thread.sleep(5L);
                }
                catch (InterruptedException interruptedException) {}
            }
        }
    }

    private final class AFInputStreamImpl
    extends AFInputStream {
        private volatile boolean streamClosed = false;
        private final AtomicBoolean eofReached = new AtomicBoolean(false);
        private final int opt = AFSocketImpl.access$200(AFSocketImpl.this).isBlocking() ? 0 : 4;

        private AFInputStreamImpl() {
        }

        @Override
        public int read(byte[] buf, int off, int len) throws IOException {
            if (this.streamClosed) {
                throw new SocketClosedException("This InputStream has already been closed.");
            }
            if (this.eofReached.get()) {
                return -1;
            }
            FileDescriptor fdesc = AFSocketImpl.this.core.validFdOrException();
            if (len == 0) {
                return 0;
            }
            if (off < 0 || len < 0 || len > buf.length - off) {
                throw new IndexOutOfBoundsException();
            }
            try {
                return NativeUnixSocket.read(fdesc, buf, off, len, this.opt, AFSocketImpl.this.ancillaryDataSupport, AFSocketImpl.this.socketTimeout.get());
            }
            catch (EOFException e) {
                this.eofReached.set(true);
                throw e;
            }
        }

        @Override
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

        @Override
        public synchronized void close() throws IOException {
            this.streamClosed = true;
            FileDescriptor fdesc = AFSocketImpl.this.core.validFd();
            if (fdesc != null && AFSocketImpl.this.getCore().isShutdownOnClose()) {
                NativeUnixSocket.shutdown(fdesc, 0);
            }
            AFSocketImpl.this.closedInputStream = true;
            AFSocketImpl.this.checkClose();
        }

        @Override
        public int available() throws IOException {
            if (this.streamClosed) {
                throw new SocketClosedException("This InputStream has already been closed.");
            }
            return AFSocketImpl.this.available();
        }

        @Override
        public FileDescriptor getFileDescriptor() throws IOException {
            return AFSocketImpl.this.getFD();
        }
    }

    private final class AFOutputStreamImpl
    extends AFOutputStream {
        private volatile boolean streamClosed = false;
        private final int opt = AFSocketImpl.access$200(AFSocketImpl.this).isBlocking() ? 0 : 4;

        private AFOutputStreamImpl() {
        }

        @Override
        public void write(int oneByte) throws IOException {
            int written;
            FileDescriptor fdesc = AFSocketImpl.this.core.validFdOrException();
            while ((written = NativeUnixSocket.write(fdesc, null, oneByte, 1, this.opt, AFSocketImpl.this.ancillaryDataSupport)) == 0 && AFSocketImpl.checkWriteInterruptedException(0)) {
            }
        }

        @Override
        public void write(byte[] buf, int off, int len) throws IOException {
            int written;
            if (this.streamClosed) {
                throw new SocketException("This OutputStream has already been closed.");
            }
            if (len < 0 || off < 0 || len > buf.length - off) {
                throw new IndexOutOfBoundsException();
            }
            FileDescriptor fdesc = AFSocketImpl.this.core.validFdOrException();
            if (len == 0 && !AFSocket.supports(AFSocketCapability.CAPABILITY_ZERO_LENGTH_SEND)) {
                return;
            }
            int writtenTotal = 0;
            do {
                if ((written = NativeUnixSocket.write(fdesc, buf, off, len, this.opt, AFSocketImpl.this.ancillaryDataSupport)) < 0) {
                    if (len == 0) {
                        return;
                    }
                    throw new IOException("Unspecific error while writing");
                }
                off += written;
            } while ((len -= written) > 0 && AFSocketImpl.checkWriteInterruptedException(writtenTotal += written));
        }

        @Override
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

        @Override
        public FileDescriptor getFileDescriptor() throws IOException {
            return AFSocketImpl.this.getFD();
        }
    }
}

