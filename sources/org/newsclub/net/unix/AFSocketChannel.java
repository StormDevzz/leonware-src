package org.newsclub.net.unix;

import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.NetworkChannel;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.newsclub.net.unix.AFSocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSocketChannel.class */
public abstract class AFSocketChannel<A extends AFSocketAddress> extends SocketChannel implements AFSomeSocket, AFSocketExtensions, AFSomeSocketChannel {
    private final AFSocket<A> afSocket;
    private final AtomicBoolean connectPending;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSocketChannel$AFSocketSupplier.class */
    @FunctionalInterface
    protected interface AFSocketSupplier<A extends AFSocketAddress> {
        AFSocket<A> newInstance() throws IOException;
    }

    @Override // java.nio.channels.SocketChannel, java.nio.channels.NetworkChannel
    public /* bridge */ /* synthetic */ SocketChannel setOption(SocketOption socketOption, Object obj) throws IOException {
        return setOption((SocketOption<Object>) socketOption, obj);
    }

    @Override // java.nio.channels.SocketChannel, java.nio.channels.NetworkChannel
    public /* bridge */ /* synthetic */ NetworkChannel setOption(SocketOption socketOption, Object obj) throws IOException {
        return setOption((SocketOption<Object>) socketOption, obj);
    }

    static {
        $assertionsDisabled = !AFSocketChannel.class.desiredAssertionStatus();
    }

    protected AFSocketChannel(AFSocket<A> socket, AFSelectorProvider<A> sp) {
        super(sp);
        this.connectPending = new AtomicBoolean(false);
        this.afSocket = (AFSocket) Objects.requireNonNull(socket);
    }

    protected final AFSocket<A> getAFSocket() {
        return this.afSocket;
    }

    protected static final <A extends AFSocketAddress> AFSocketChannel<A> open(AFSocketSupplier<A> supplier) throws IOException {
        return supplier.newInstance().getChannel();
    }

    protected static final <A extends AFSocketAddress> AFSocketChannel<A> open(AFSocketSupplier<A> supplier, SocketAddress remote) throws IOException {
        AFSocketChannel<A> sc = open(supplier);
        try {
            sc.connect(remote);
            if ($assertionsDisabled || sc.isConnected()) {
                return sc;
            }
            throw new AssertionError();
        } catch (Throwable x) {
            try {
                sc.close();
            } catch (Throwable suppressed) {
                x.addSuppressed(suppressed);
            }
            throw x;
        }
    }

    @Override // java.nio.channels.NetworkChannel
    public final <T> T getOption(SocketOption<T> socketOption) throws IOException {
        if (socketOption instanceof AFSocketOption) {
            return (T) getAFCore().getOption((AFSocketOption) socketOption);
        }
        Integer numResolve = SocketOptionsMapper.resolve(socketOption);
        if (numResolve == null) {
            throw new UnsupportedOperationException("unsupported option");
        }
        return (T) this.afSocket.getAFImpl().getOption(numResolve.intValue());
    }

    @Override // java.nio.channels.SocketChannel, java.nio.channels.NetworkChannel
    public final <T> AFSocketChannel<A> setOption(SocketOption<T> name, T value) throws IOException {
        if (name instanceof AFSocketOption) {
            getAFCore().setOption((AFSocketOption) name, value);
            return this;
        }
        Integer optionId = SocketOptionsMapper.resolve(name);
        if (optionId == null) {
            throw new UnsupportedOperationException("unsupported option");
        }
        this.afSocket.getAFImpl().setOption(optionId.intValue(), value);
        return this;
    }

    @Override // java.nio.channels.NetworkChannel
    public final Set<SocketOption<?>> supportedOptions() {
        return SocketOptionsMapper.SUPPORTED_SOCKET_OPTIONS;
    }

    @Override // java.nio.channels.SocketChannel, java.nio.channels.NetworkChannel
    public final AFSocketChannel<A> bind(SocketAddress local) throws IOException {
        this.afSocket.bind(local);
        return this;
    }

    @Override // java.nio.channels.SocketChannel
    public final AFSocketChannel<A> shutdownInput() throws IOException {
        this.afSocket.getAFImpl().shutdownInput();
        return this;
    }

    @Override // java.nio.channels.SocketChannel
    public final AFSocketChannel<A> shutdownOutput() throws IOException {
        this.afSocket.getAFImpl().shutdownOutput();
        return this;
    }

    @Override // java.nio.channels.SocketChannel
    @SuppressFBWarnings({"EI_EXPOSE_REP"})
    public final AFSocket<A> socket() {
        return this.afSocket;
    }

    @Override // java.nio.channels.SocketChannel
    public final boolean isConnected() {
        boolean connected = this.afSocket.isConnected();
        if (connected) {
            this.connectPending.set(false);
        }
        return connected;
    }

    @Override // java.nio.channels.SocketChannel
    public final boolean isConnectionPending() {
        return this.connectPending.get();
    }

    @Override // java.nio.channels.SocketChannel
    public final boolean connect(SocketAddress remote) throws IOException {
        boolean connected = this.afSocket.connect0(remote, 0);
        if (!connected) {
            this.connectPending.set(true);
        }
        return connected;
    }

    @Override // java.nio.channels.SocketChannel
    public final boolean finishConnect() throws IOException {
        if (isConnected()) {
            return true;
        }
        if (!isConnectionPending()) {
            return false;
        }
        boolean connected = NativeUnixSocket.finishConnect(this.afSocket.getFileDescriptor()) || isConnected();
        if (connected) {
            this.connectPending.set(false);
        }
        return connected;
    }

    @Override // java.nio.channels.SocketChannel
    public final A getRemoteAddress() throws IOException {
        return (A) getRemoteSocketAddress();
    }

    @Override // org.newsclub.net.unix.AFSomeSocket
    public final A getRemoteSocketAddress() {
        return (A) this.afSocket.getRemoteSocketAddress();
    }

    @Override // java.nio.channels.SocketChannel, java.nio.channels.ReadableByteChannel
    public final int read(ByteBuffer dst) throws IOException {
        return this.afSocket.getAFImpl().read(dst, null);
    }

    @Override // java.nio.channels.SocketChannel, java.nio.channels.ScatteringByteChannel
    public final long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        if (length == 0) {
            return 0L;
        }
        return read(dsts[offset]);
    }

    @Override // java.nio.channels.SocketChannel, java.nio.channels.GatheringByteChannel
    public final long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        if (length == 0) {
            return 0L;
        }
        return write(srcs[offset]);
    }

    @Override // java.nio.channels.SocketChannel, java.nio.channels.WritableByteChannel
    public final int write(ByteBuffer src) throws IOException {
        return this.afSocket.getAFImpl().write(src);
    }

    @Override // java.nio.channels.SocketChannel, java.nio.channels.NetworkChannel
    public final A getLocalAddress() throws IOException {
        return (A) getLocalSocketAddress();
    }

    @Override // org.newsclub.net.unix.AFSomeSocketThing
    public final A getLocalSocketAddress() {
        return (A) this.afSocket.getLocalSocketAddress();
    }

    @Override // java.nio.channels.spi.AbstractSelectableChannel
    protected final void implCloseSelectableChannel() throws IOException {
        this.afSocket.close();
    }

    @Override // java.nio.channels.spi.AbstractSelectableChannel
    protected final void implConfigureBlocking(boolean block) throws IOException {
        getAFCore().implConfigureBlocking(block);
    }

    @Override // org.newsclub.net.unix.AFSocketExtensions
    public final int getAncillaryReceiveBufferSize() {
        return this.afSocket.getAncillaryReceiveBufferSize();
    }

    @Override // org.newsclub.net.unix.AFSocketExtensions
    public final void setAncillaryReceiveBufferSize(int size) {
        this.afSocket.setAncillaryReceiveBufferSize(size);
    }

    @Override // org.newsclub.net.unix.AFSocketExtensions
    public final void ensureAncillaryReceiveBufferSize(int minSize) {
        this.afSocket.ensureAncillaryReceiveBufferSize(minSize);
    }

    final AFSocketCore getAFCore() {
        return this.afSocket.getAFImpl().getCore();
    }

    @Override // org.newsclub.net.unix.FileDescriptorAccess
    public final FileDescriptor getFileDescriptor() throws IOException {
        return this.afSocket.getFileDescriptor();
    }

    public final String toString() {
        return super.toString() + this.afSocket.toStringSuffix();
    }

    @Override // org.newsclub.net.unix.AFSomeSocketThing
    public void setShutdownOnClose(boolean enabled) {
        getAFCore().setShutdownOnClose(enabled);
    }
}
