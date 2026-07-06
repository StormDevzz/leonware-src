/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.kohlschutter.annotations.compiletime.SuppressFBWarnings
 *  org.eclipse.jdt.annotation.Nullable
 */
package org.newsclub.net.unix;

import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;
import java.util.Set;
import org.eclipse.jdt.annotation.Nullable;
import org.newsclub.net.unix.AFDatagramSocket;
import org.newsclub.net.unix.AFSelectorProvider;
import org.newsclub.net.unix.AFSocketAddress;
import org.newsclub.net.unix.AFSocketCore;
import org.newsclub.net.unix.AFSocketExtensions;
import org.newsclub.net.unix.AFSocketOption;
import org.newsclub.net.unix.AFSomeSocket;
import org.newsclub.net.unix.AFSomeSocketChannel;
import org.newsclub.net.unix.SocketOptionsMapper;

public abstract class AFDatagramChannel<A extends AFSocketAddress>
extends DatagramChannel
implements AFSomeSocket,
AFSocketExtensions,
AFSomeSocketChannel {
    private final AFDatagramSocket<A> afSocket;

    protected AFDatagramChannel(AFSelectorProvider<A> selectorProvider, AFDatagramSocket<A> socket) {
        super(selectorProvider);
        this.afSocket = socket;
    }

    protected final AFDatagramSocket<A> getAFSocket() {
        return this.afSocket;
    }

    @Override
    public final MembershipKey join(InetAddress group, NetworkInterface interf) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public final MembershipKey join(InetAddress group, NetworkInterface interf, InetAddress source) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public final AFDatagramChannel<A> bind(SocketAddress local) throws IOException {
        this.afSocket.bind(local);
        return this;
    }

    @Override
    @SuppressFBWarnings(value={"EI_EXPOSE_REP"})
    public final AFDatagramSocket<A> socket() {
        return this.afSocket;
    }

    public final boolean isBound() {
        return this.afSocket.isBound();
    }

    @Override
    public final boolean isConnected() {
        return this.afSocket.isConnected();
    }

    @Override
    public final AFDatagramChannel<A> connect(SocketAddress remote) throws IOException {
        this.afSocket.connect(remote);
        return this;
    }

    @Override
    public final AFDatagramChannel<A> disconnect() throws IOException {
        this.afSocket.disconnect();
        return this;
    }

    public final @Nullable A getRemoteAddress() throws IOException {
        return (A)this.getRemoteSocketAddress();
    }

    public final @Nullable A getRemoteSocketAddress() {
        return (A)this.afSocket.getRemoteSocketAddress();
    }

    public final @Nullable A getLocalAddress() throws IOException {
        return (A)this.getLocalSocketAddress();
    }

    public final @Nullable A getLocalSocketAddress() {
        return (A)this.afSocket.getLocalSocketAddress();
    }

    public final A receive(ByteBuffer dst) throws IOException {
        return this.afSocket.getAFImpl().receive(dst);
    }

    @Override
    public final int send(ByteBuffer src, SocketAddress target) throws IOException {
        return this.afSocket.getAFImpl().send(src, target);
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
    public final int write(ByteBuffer src) throws IOException {
        return this.afSocket.getAFImpl().write(src);
    }

    @Override
    public final long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        if (length == 0) {
            return 0L;
        }
        return this.write(srcs[offset]);
    }

    @Override
    protected final void implCloseSelectableChannel() throws IOException {
        this.getAFSocket().close();
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

    @Override
    public final <T> AFDatagramChannel<A> setOption(SocketOption<T> name, T value) throws IOException {
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
    public final Set<SocketOption<?>> supportedOptions() {
        return SocketOptionsMapper.SUPPORTED_SOCKET_OPTIONS;
    }

    final AFSocketCore getAFCore() {
        return this.afSocket.getAFImpl().getCore();
    }

    @Override
    public final FileDescriptor getFileDescriptor() throws IOException {
        return this.afSocket.getFileDescriptor();
    }

    public final boolean isDeleteOnClose() {
        return this.afSocket.isDeleteOnClose();
    }

    public final void setDeleteOnClose(boolean b) {
        this.afSocket.setDeleteOnClose(b);
    }

    @Override
    public void setShutdownOnClose(boolean enabled) {
        this.getAFCore().setShutdownOnClose(enabled);
    }
}

