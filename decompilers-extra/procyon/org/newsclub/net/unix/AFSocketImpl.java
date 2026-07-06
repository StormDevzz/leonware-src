// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.EOFException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.net.SocketOption;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.net.SocketException;
import java.io.IOException;
import java.net.SocketImpl;
import java.net.InetAddress;
import java.io.FileDescriptor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AFSocketImpl<A extends AFSocketAddress> extends SocketImplShim
{
    private static final int SHUTDOWN_RD_WR = 3;
    private final AFSocketStreamCore core;
    final AncillaryDataSupport ancillaryDataSupport;
    private final AtomicBoolean bound;
    private Boolean createType;
    private final AtomicBoolean connected;
    private volatile boolean closedInputStream;
    private volatile boolean closedOutputStream;
    private final AFInputStream in;
    private final AFOutputStream out;
    private boolean reuseAddr;
    private final AtomicInteger socketTimeout;
    private final AFAddressFamily<A> addressFamily;
    private int shutdownState;
    private AFSocketImplExtensions<A> implExtensions;
    
    protected AFSocketImpl(final AFAddressFamily<A> addressFamily, final FileDescriptor fdObj) {
        this.ancillaryDataSupport = new AncillaryDataSupport();
        this.bound = new AtomicBoolean(false);
        this.createType = null;
        this.connected = new AtomicBoolean(false);
        this.closedInputStream = false;
        this.closedOutputStream = false;
        this.reuseAddr = true;
        this.socketTimeout = new AtomicInteger(0);
        this.shutdownState = 0;
        this.implExtensions = null;
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
    protected final void accept(final SocketImpl socket) throws IOException {
        this.accept0(socket);
    }
    
    final boolean accept0(final SocketImpl socket) throws IOException {
        final FileDescriptor fdesc = this.core.validFdOrException();
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (!this.isBound()) {
            throw new SocketException("Socket is not bound");
        }
        AFSocketAddress socketAddress = this.core.socketAddress;
        final AFSocketAddress boundSocketAddress = this.getLocalSocketAddress();
        if (boundSocketAddress != null) {
            socketAddress = (this.core.socketAddress = boundSocketAddress);
        }
        if (socketAddress == null) {
            throw new SocketException("Socket is not bound");
        }
        final AFSocketImpl<A> si = (AFSocketImpl<A>)socket;
        this.core.incPendingAccepts();
        try {
            final ByteBuffer ab = socketAddress.getNativeAddressDirectBuffer();
            SocketException caught = null;
            try {
                if (!NativeUnixSocket.accept(ab, ab.limit(), fdesc, si.fd, this.core.inode.get(), this.socketTimeout.get())) {
                    return false;
                }
            }
            catch (final SocketException e) {
                caught = e;
            }
            finally {
                if (!this.isBound() || this.isClosed()) {
                    if (this.getCore().isShutdownOnClose()) {
                        try {
                            NativeUnixSocket.shutdown(si.fd, 2);
                        }
                        catch (final Exception ex) {}
                    }
                    try {
                        NativeUnixSocket.close(si.fd);
                    }
                    catch (final Exception ex2) {}
                    if (caught != null) {
                        throw caught;
                    }
                    throw new SocketClosedException("Socket is closed");
                }
                else if (caught != null) {
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
    
    final void setSocketAddress(final AFSocketAddress socketAddress) {
        if (socketAddress == null) {
            this.core.socketAddress = null;
            this.address = null;
            this.localport = -1;
        }
        else {
            this.core.socketAddress = socketAddress;
            this.address = socketAddress.getAddress();
            if (this.localport <= 0) {
                this.localport = socketAddress.getPort();
            }
        }
    }
    
    @Override
    protected final int available() throws IOException {
        final FileDescriptor fdesc = this.core.validFdOrException();
        return NativeUnixSocket.available(fdesc, this.core.getThreadLocalDirectByteBuffer(0));
    }
    
    final void bind(final SocketAddress addr, final int options) throws IOException {
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
        final AFSocketAddress socketAddress = (AFSocketAddress)addr;
        this.setSocketAddress(socketAddress);
        final ByteBuffer ab = socketAddress.getNativeAddressDirectBuffer();
        this.core.inode.set(NativeUnixSocket.bind(ab, ab.limit(), this.fd, options));
        this.core.validFdOrException();
    }
    
    @Override
    protected final void bind(final InetAddress host, final int port) throws IOException {
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
    protected final void connect(final String host, final int port) throws IOException {
        throw new SocketException("Cannot bind to this type of address: " + InetAddress.class);
    }
    
    @Override
    protected final void connect(final InetAddress address, final int port) throws IOException {
        throw new SocketException("Cannot bind to this type of address: " + InetAddress.class);
    }
    
    @Override
    protected final void connect(final SocketAddress addr, final int connectTimeout) throws IOException {
        this.connect0(addr, connectTimeout);
    }
    
    final boolean connect0(final SocketAddress addr, final int connectTimeout) throws IOException {
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
        final AFSocketAddress socketAddress = (AFSocketAddress)addr;
        final ByteBuffer ab = socketAddress.getNativeAddressDirectBuffer();
        boolean success = false;
        boolean ignoreSpuriousTimeout = true;
        while (true) {
            try {
                success = NativeUnixSocket.connect(ab, ab.limit(), this.fd, -1L);
            }
            catch (final SocketTimeoutException e) {
                if (ignoreSpuriousTimeout) {
                    final Object o = this.getOption(4102);
                    if (o instanceof Integer) {
                        if ((int)o != 0) {
                            throw e;
                        }
                        ignoreSpuriousTimeout = false;
                    }
                    else {
                        if (o != null) {
                            throw e;
                        }
                        ignoreSpuriousTimeout = false;
                    }
                    if (!Thread.interrupted()) {
                        continue;
                    }
                }
                throw e;
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
    protected final void create(final boolean stream) throws IOException {
        if (this.isClosed()) {
            throw new SocketException("Already closed");
        }
        if (this.fd.valid()) {
            if (this.createType != null) {
                if (this.createType != stream) {
                    throw new IllegalStateException("Already created with different mode");
                }
            }
            else {
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
        final FileDescriptor fdesc = this.core.validFdOrException();
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
    protected final void sendUrgentData(final int data) throws IOException {
        throw new UnsupportedOperationException();
    }
    
    private static boolean checkWriteInterruptedException(final int bytesTransferred) throws InterruptedIOException {
        if (Thread.interrupted()) {
            final InterruptedIOException ex = new InterruptedIOException("Thread interrupted during write");
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
    
    private static int expectInteger(final Object value) throws SocketException {
        if (value == null) {
            throw (SocketException)new SocketException("Value must not be null").initCause(new NullPointerException());
        }
        try {
            return (int)value;
        }
        catch (final ClassCastException e) {
            throw (SocketException)new SocketException("Unsupported value: " + value).initCause(e);
        }
    }
    
    private static int expectBoolean(final Object value) throws SocketException {
        if (value == null) {
            throw (SocketException)new SocketException("Value must not be null").initCause(new NullPointerException());
        }
        try {
            return ((boolean)value) ? 1 : 0;
        }
        catch (final ClassCastException e) {
            throw (SocketException)new SocketException("Unsupported value: " + value).initCause(e);
        }
    }
    
    @Override
    public Object getOption(final int optID) throws SocketException {
        return this.getOption0(optID);
    }
    
    private Object getOption0(final int optID) throws SocketException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (optID == 4) {
            return this.reuseAddr;
        }
        final FileDescriptor fdesc = this.core.validFdOrException();
        return getOptionDefault(fdesc, optID, this.socketTimeout, this.addressFamily);
    }
    
    static final Object getOptionDefault(final FileDescriptor fdesc, final int optID, final AtomicInteger acceptTimeout, final AFAddressFamily<?> af) throws SocketException {
        try {
            switch (optID) {
                case 8: {
                    try {
                        return NativeUnixSocket.getSocketOptionInt(fdesc, optID) != 0;
                    }
                    catch (final SocketException e) {
                        return false;
                    }
                }
                case 1: {
                    return NativeUnixSocket.getSocketOptionInt(fdesc, optID) != 0;
                }
                case 4102: {
                    final int v = Math.max(NativeUnixSocket.getSocketOptionInt(fdesc, 4101), NativeUnixSocket.getSocketOptionInt(fdesc, 4102));
                    if (v == -1) {
                        return 0;
                    }
                    return Math.max((acceptTimeout == null) ? 0 : acceptTimeout.get(), v);
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
                default: {
                    throw new SocketException("Unsupported option: " + optID);
                }
            }
        }
        catch (final SocketException e) {
            throw e;
        }
        catch (final Exception e2) {
            throw (SocketException)new SocketException("Could not get option").initCause(e2);
        }
    }
    
    @Override
    public void setOption(final int optID, final Object value) throws SocketException {
        this.setOption0(optID, value);
    }
    
    private void setOption0(final int optID, final Object value) throws SocketException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (optID == 4) {
            this.reuseAddr = (expectBoolean(value) != 0);
            return;
        }
        final FileDescriptor fdesc = this.core.validFdOrException();
        setOptionDefault(fdesc, optID, value, this.socketTimeout);
    }
    
    protected final Object getOptionLenient(final int optID) throws SocketException {
        try {
            return this.getOption0(optID);
        }
        catch (final SocketException e) {
            switch (optID) {
                case 1:
                case 8: {
                    return false;
                }
                default: {
                    throw e;
                }
            }
        }
    }
    
    protected final void setOptionLenient(final int optID, final Object value) throws SocketException {
        try {
            this.setOption0(optID, value);
        }
        catch (final SocketException e) {
            switch (optID) {
                case 1: {
                    return;
                }
                default: {
                    throw e;
                }
            }
        }
    }
    
    static final void setOptionDefault(final FileDescriptor fdesc, final int optID, final Object value, final AtomicInteger acceptTimeout) throws SocketException {
        try {
            switch (optID) {
                case 128: {
                    if (!(value instanceof Boolean)) {
                        NativeUnixSocket.setSocketOptionInt(fdesc, optID, expectInteger(value));
                        return;
                    }
                    final boolean b = (boolean)value;
                    if (b) {
                        throw new SocketException("Only accepting Boolean.FALSE here");
                    }
                    NativeUnixSocket.setSocketOptionInt(fdesc, optID, -1);
                    return;
                }
                case 4102: {
                    final int timeout = expectInteger(value);
                    try {
                        NativeUnixSocket.setSocketOptionInt(fdesc, 4101, timeout);
                    }
                    catch (final InvalidArgumentSocketException ex) {}
                    try {
                        NativeUnixSocket.setSocketOptionInt(fdesc, 4102, timeout);
                    }
                    catch (final InvalidArgumentSocketException ex2) {}
                    if (acceptTimeout != null) {
                        acceptTimeout.set(timeout);
                    }
                    return;
                }
                case 4097:
                case 4098: {
                    NativeUnixSocket.setSocketOptionInt(fdesc, optID, expectInteger(value));
                    return;
                }
                case 8: {
                    try {
                        NativeUnixSocket.setSocketOptionInt(fdesc, optID, expectBoolean(value));
                    }
                    catch (final SocketException ex3) {}
                    return;
                }
                case 1: {
                    NativeUnixSocket.setSocketOptionInt(fdesc, optID, expectBoolean(value));
                    return;
                }
                case 3: {
                    return;
                }
                case 4: {
                    return;
                }
                default: {
                    throw new SocketException("Unsupported option: " + optID);
                }
            }
        }
        catch (final SocketException e) {
            throw e;
        }
        catch (final Exception e2) {
            throw (SocketException)new SocketException("Error while setting option").initCause(e2);
        }
    }
    
    protected final void shutdown() throws IOException {
        final FileDescriptor fdesc = this.core.validFd();
        if (fdesc != null) {
            NativeUnixSocket.shutdown(fdesc, 2);
            this.shutdownState = 0;
        }
    }
    
    @Override
    protected final void shutdownInput() throws IOException {
        final FileDescriptor fdesc = this.core.validFd();
        if (fdesc != null) {
            NativeUnixSocket.shutdown(fdesc, 0);
            this.shutdownState |= 0x1;
            if (this.shutdownState == 3) {
                NativeUnixSocket.shutdown(fdesc, 2);
                this.shutdownState = 0;
            }
        }
    }
    
    @Override
    protected final void shutdownOutput() throws IOException {
        final FileDescriptor fdesc = this.core.validFd();
        if (fdesc != null) {
            NativeUnixSocket.shutdown(fdesc, 1);
            this.shutdownState |= 0x4;
            if (this.shutdownState == 3) {
                NativeUnixSocket.shutdown(fdesc, 2);
                this.shutdownState = 0;
            }
        }
    }
    
    final int getAncillaryReceiveBufferSize() {
        return this.ancillaryDataSupport.getAncillaryReceiveBufferSize();
    }
    
    final void setAncillaryReceiveBufferSize(final int size) {
        this.ancillaryDataSupport.setAncillaryReceiveBufferSize(size);
    }
    
    final void ensureAncillaryReceiveBufferSize(final int minSize) {
        this.ancillaryDataSupport.ensureAncillaryReceiveBufferSize(minSize);
    }
    
    AncillaryDataSupport getAncillaryDataSupport() {
        return this.ancillaryDataSupport;
    }
    
    final SocketAddress receive(final ByteBuffer dst) throws IOException {
        return this.core.receive(dst);
    }
    
    final int send(final ByteBuffer src, final SocketAddress target) throws IOException {
        return this.core.write(src, target, 0);
    }
    
    final int read(final ByteBuffer dst, final ByteBuffer socketAddressBuffer) throws IOException {
        return this.core.read(dst, socketAddressBuffer, 0);
    }
    
    final int write(final ByteBuffer src) throws IOException {
        return this.core.write(src);
    }
    
    @Override
    protected final FileDescriptor getFileDescriptor() {
        return this.core.fd;
    }
    
    final void updatePorts(final int local, final int remote) {
        this.localport = local;
        if (remote >= 0) {
            this.port = remote;
        }
    }
    
    final A getLocalSocketAddress() {
        return AFSocketAddress.getSocketAddress(this.getFileDescriptor(), false, this.localport, this.addressFamily);
    }
    
    final A getRemoteSocketAddress() {
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
        final A rsa = this.getRemoteSocketAddress();
        if (rsa == null) {
            return InetAddress.getLoopbackAddress();
        }
        return rsa.getInetAddress();
    }
    
    final void createSocket(final FileDescriptor fdTarget, final AFSocketType type) throws IOException {
        NativeUnixSocket.createSocket(fdTarget, this.addressFamily.getDomain(), type.getId());
    }
    
    final AFAddressFamily<A> getAddressFamily() {
        return this.addressFamily;
    }
    
    @Override
    protected <T> void setOption(final SocketOption<T> name, final T value) throws IOException {
        if (name instanceof AFSocketOption) {
            this.getCore().setOption((AFSocketOption)name, value);
            return;
        }
        final Integer optionId = SocketOptionsMapper.resolve(name);
        if (optionId == null) {
            super.setOption(name, value);
        }
        else {
            this.setOption(optionId, value);
        }
    }
    
    @Override
    protected <T> T getOption(final SocketOption<T> name) throws IOException {
        if (name instanceof AFSocketOption) {
            return this.getCore().getOption((AFSocketOption<T>)(AFSocketOption)name);
        }
        final Integer optionId = SocketOptionsMapper.resolve(name);
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
    
    static final class AFSocketStreamCore extends AFSocketCore
    {
        AFSocketStreamCore(final AFSocketImpl<?> observed, final FileDescriptor fd, final AncillaryDataSupport ancillaryDataSupport, final AFAddressFamily<?> af) {
            super(observed, fd, ancillaryDataSupport, af, false);
        }
        
        void createSocket(final FileDescriptor fdTarget, final AFSocketType type) throws IOException {
            NativeUnixSocket.createSocket(fdTarget, this.addressFamily().getDomain(), type.getId());
        }
        
        @Override
        protected void unblockAccepts() {
            if (this.socketAddress == null || this.socketAddress.getBytes() == null || this.inode.get() < 0L) {
                return;
            }
            while (this.hasPendingAccepts()) {
                try {
                    final FileDescriptor tmpFd = new FileDescriptor();
                    try {
                        this.createSocket(tmpFd, AFSocketType.SOCK_STREAM);
                        final ByteBuffer ab = this.socketAddress.getNativeAddressDirectBuffer();
                        NativeUnixSocket.connect(ab, ab.limit(), tmpFd, this.inode.get());
                    }
                    catch (final IOException e) {
                        return;
                    }
                    if (this.isShutdownOnClose()) {
                        try {
                            NativeUnixSocket.shutdown(tmpFd, 2);
                        }
                        catch (final Exception ex) {}
                    }
                    try {
                        NativeUnixSocket.close(tmpFd);
                    }
                    catch (final Exception ex2) {}
                }
                catch (final RuntimeException ex3) {}
                try {
                    Thread.sleep(5L);
                }
                catch (final InterruptedException ex4) {}
            }
        }
    }
    
    private final class AFInputStreamImpl extends AFInputStream
    {
        private volatile boolean streamClosed;
        private final AtomicBoolean eofReached;
        private final int opt;
        
        private AFInputStreamImpl() {
            this.streamClosed = false;
            this.eofReached = new AtomicBoolean(false);
            this.opt = (AFSocketImpl.this.core.isBlocking() ? 0 : 4);
        }
        
        @Override
        public int read(final byte[] buf, final int off, final int len) throws IOException {
            if (this.streamClosed) {
                throw new SocketClosedException("This InputStream has already been closed.");
            }
            if (this.eofReached.get()) {
                return -1;
            }
            final FileDescriptor fdesc = AFSocketImpl.this.core.validFdOrException();
            if (len == 0) {
                return 0;
            }
            if (off < 0 || len < 0 || len > buf.length - off) {
                throw new IndexOutOfBoundsException();
            }
            try {
                return NativeUnixSocket.read(fdesc, buf, off, len, this.opt, AFSocketImpl.this.ancillaryDataSupport, AFSocketImpl.this.socketTimeout.get());
            }
            catch (final EOFException e) {
                this.eofReached.set(true);
                throw e;
            }
        }
        
        @Override
        public int read() throws IOException {
            final FileDescriptor fdesc = AFSocketImpl.this.core.validFdOrException();
            if (this.eofReached.get()) {
                return -1;
            }
            final int byteRead = NativeUnixSocket.read(fdesc, null, 0, 1, this.opt, AFSocketImpl.this.ancillaryDataSupport, AFSocketImpl.this.socketTimeout.get());
            if (byteRead < 0) {
                this.eofReached.set(true);
                return -1;
            }
            return byteRead;
        }
        
        @Override
        public synchronized void close() throws IOException {
            this.streamClosed = true;
            final FileDescriptor fdesc = AFSocketImpl.this.core.validFd();
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
    
    private final class AFOutputStreamImpl extends AFOutputStream
    {
        private volatile boolean streamClosed;
        private final int opt;
        
        private AFOutputStreamImpl() {
            this.streamClosed = false;
            this.opt = (AFSocketImpl.this.core.isBlocking() ? 0 : 4);
        }
        
        @Override
        public void write(final int oneByte) throws IOException {
            final FileDescriptor fdesc = AFSocketImpl.this.core.validFdOrException();
            do {
                final int written = NativeUnixSocket.write(fdesc, null, oneByte, 1, this.opt, AFSocketImpl.this.ancillaryDataSupport);
                if (written != 0) {
                    break;
                }
            } while (checkWriteInterruptedException(0));
        }
        
        @Override
        public void write(final byte[] buf, int off, int len) throws IOException {
            if (this.streamClosed) {
                throw new SocketException("This OutputStream has already been closed.");
            }
            if (len < 0 || off < 0 || len > buf.length - off) {
                throw new IndexOutOfBoundsException();
            }
            final FileDescriptor fdesc = AFSocketImpl.this.core.validFdOrException();
            if (len == 0 && !AFSocket.supports(AFSocketCapability.CAPABILITY_ZERO_LENGTH_SEND)) {
                return;
            }
            int writtenTotal = 0;
            do {
                final int written = NativeUnixSocket.write(fdesc, buf, off, len, this.opt, AFSocketImpl.this.ancillaryDataSupport);
                if (written < 0) {
                    if (len == 0) {
                        return;
                    }
                    throw new IOException("Unspecific error while writing");
                }
                else {
                    len -= written;
                    off += written;
                    writtenTotal += written;
                }
            } while (len > 0 && checkWriteInterruptedException(writtenTotal));
        }
        
        @Override
        public synchronized void close() throws IOException {
            if (this.streamClosed) {
                return;
            }
            this.streamClosed = true;
            final FileDescriptor fdesc = AFSocketImpl.this.core.validFd();
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
