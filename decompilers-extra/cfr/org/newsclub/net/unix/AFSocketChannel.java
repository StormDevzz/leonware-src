/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.kohlschutter.annotations.compiletime.SuppressFBWarnings
 *  org.eclipse.jdt.annotation.NonNull
 */
package org.newsclub.net.unix;

import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.jdt.annotation.NonNull;
import org.newsclub.net.unix.AFSelectorProvider;
import org.newsclub.net.unix.AFSocket;
import org.newsclub.net.unix.AFSocketAddress;
import org.newsclub.net.unix.AFSocketCore;
import org.newsclub.net.unix.AFSocketExtensions;
import org.newsclub.net.unix.AFSocketOption;
import org.newsclub.net.unix.AFSomeSocket;
import org.newsclub.net.unix.AFSomeSocketChannel;
import org.newsclub.net.unix.NativeUnixSocket;
import org.newsclub.net.unix.SocketOptionsMapper;

public abstract class AFSocketChannel<A extends AFSocketAddress>
extends SocketChannel
implements AFSomeSocket,
AFSocketExtensions,
AFSomeSocketChannel {
    private final @NonNull AFSocket<A> afSocket;
    private final AtomicBoolean connectPending = new AtomicBoolean(false);

    protected AFSocketChannel(AFSocket<A> socket, AFSelectorProvider<A> sp) {
        super(sp);
        this.afSocket = Objects.requireNonNull(socket);
    }

    protected final AFSocket<A> getAFSocket() {
        return this.afSocket;
    }

    protected static final <A extends AFSocketAddress> AFSocketChannel<A> open(AFSocketSupplier<A> supplier) throws IOException {
        return supplier.newInstance().getChannel();
    }

    protected static final <A extends AFSocketAddress> AFSocketChannel<A> open(AFSocketSupplier<A> supplier, SocketAddress remote) throws IOException {
        AFSocketChannel<A> sc = AFSocketChannel.open(supplier);
        try {
            sc.connect(remote);
        }
        catch (Throwable x) {
            try {
                sc.close();
            }
            catch (Throwable suppressed) {
                x.addSuppressed(suppressed);
            }
            throw x;
        }
        assert (sc.isConnected());
        return sc;
    }

    @Override
    public final <T> T getOption(SocketOption<T> name) throws IOException {
        if (name instanceof AFSocketOption) {
            return this.getAFCore().getOption((AFSocketOption)name);
        }
        Integer optionId = SocketOptionsMapper.resolve(name);
        if (optionId == null) {
            throw new UnsupportedOperationException("unsupported option");
        }
        return (T)this.afSocket.getAFImpl().getOption(optionId);
    }

    @Override
    public final <T> AFSocketChannel<A> setOption(SocketOption<T> name, T value) throws IOException {
        if (name instanceof AFSocketOption) {
            this.getAFCore().setOption((AFSocketOption)name, value);
            return this;
        }
        Integer optionId = SocketOptionsMapper.resolve(name);
        if (optionId == null) {
            throw new UnsupportedOperationException("unsupported option");
        }
        this.afSocket.getAFImpl().setOption(optionId, value);
        return this;
    }

    @Override
    public final Set<SocketOption<?>> supportedOptions() {
        return SocketOptionsMapper.SUPPORTED_SOCKET_OPTIONS;
    }

    @Override
    public final AFSocketChannel<A> bind(SocketAddress local) throws IOException {
        this.afSocket.bind(local);
        return this;
    }

    @Override
    public final AFSocketChannel<A> shutdownInput() throws IOException {
        this.afSocket.getAFImpl().shutdownInput();
        return this;
    }

    @Override
    public final AFSocketChannel<A> shutdownOutput() throws IOException {
        this.afSocket.getAFImpl().shutdownOutput();
        return this;
    }

    @Override
    @SuppressFBWarnings(value={"EI_EXPOSE_REP"})
    public final AFSocket<A> socket() {
        return this.afSocket;
    }

    @Override
    public final boolean isConnected() {
        boolean connected = this.afSocket.isConnected();
        if (connected) {
            this.connectPending.set(false);
        }
        return connected;
    }

    @Override
    public final boolean isConnectionPending() {
        return this.connectPending.get();
    }

    @Override
    public final boolean connect(SocketAddress remote) throws IOException {
        boolean connected = this.afSocket.connect0(remote, 0);
        if (!connected) {
            this.connectPending.set(true);
        }
        return connected;
    }

    @Override
    public final boolean finishConnect() throws IOException {
        boolean connected;
        if (this.isConnected()) {
            return true;
        }
        if (!this.isConnectionPending()) {
            return false;
        }
        boolean bl = connected = NativeUnixSocket.finishConnect(this.afSocket.getFileDescriptor()) || this.isConnected();
        if (connected) {
            this.connectPending.set(false);
        }
        return connected;
    }

    public final A getRemoteAddress() throws IOException {
        return (A)this.getRemoteSocketAddress();
    }

    public final A getRemoteSocketAddress() {
        return (A)this.afSocket.getRemoteSocketAddress();
    }

    @Override
    public final int read(ByteBuffer dst) throws IOException {
        return this.afSocket.getAFImpl().read(dst, null);
    }

    @Override
    public final long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        if (length == 0) {
            return 0L;
        }
        return this.read(dsts[offset]);
    }

    @Override
    public final long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        if (length == 0) {
            return 0L;
        }
        return this.write(srcs[offset]);
    }

    @Override
    public final int write(ByteBuffer src) throws IOException {
        return this.afSocket.getAFImpl().write(src);
    }

    public final A getLocalAddress() throws IOException {
        return (A)this.getLocalSocketAddress();
    }

    public final A getLocalSocketAddress() {
        return (A)this.afSocket.getLocalSocketAddress();
    }

    @Override
    protected final void implCloseSelectableChannel() throws IOException {
        this.afSocket.close();
    }

    @Override
    protected final void implConfigureBlocking(boolean block) throws IOException {
        this.getAFCore().implConfigureBlocking(block);
    }

    @Override
    public final int getAncillaryReceiveBufferSize() {
        return this.afSocket.getAncillaryReceiveBufferSize();
    }

    @Override
    public final void setAncillaryReceiveBufferSize(int size) {
        this.afSocket.setAncillaryReceiveBufferSize(size);
    }

    @Override
    public final void ensureAncillaryReceiveBufferSize(int minSize) {
        this.afSocket.ensureAncillaryReceiveBufferSize(minSize);
    }

    final AFSocketCore getAFCore() {
        return this.afSocket.getAFImpl().getCore();
    }

    @Override
    public final FileDescriptor getFileDescriptor() throws IOException {
        return this.afSocket.getFileDescriptor();
    }

    public final String toString() {
        return super.toString() + this.afSocket.toStringSuffix();
    }

    @Override
    public void setShutdownOnClose(boolean enabled) {
        this.getAFCore().setShutdownOnClose(enabled);
    }

    @FunctionalInterface
    protected static interface AFSocketSupplier<A extends AFSocketAddress> {
        public AFSocket<A> newInstance() throws IOException;
    }
}

