/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.kohlschutter.annotations.compiletime.SuppressFBWarnings
 *  org.eclipse.jdt.annotation.NonNull
 *  org.eclipse.jdt.annotation.Nullable
 */
package org.newsclub.net.unix;

import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.channels.ServerSocketChannel;
import java.util.Objects;
import java.util.Set;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.newsclub.net.unix.AFSelectorProvider;
import org.newsclub.net.unix.AFServerSocket;
import org.newsclub.net.unix.AFSocket;
import org.newsclub.net.unix.AFSocketAddress;
import org.newsclub.net.unix.AFSocketChannel;
import org.newsclub.net.unix.AFSocketCore;
import org.newsclub.net.unix.AFSocketOption;
import org.newsclub.net.unix.AFSomeSocketChannel;
import org.newsclub.net.unix.FileDescriptorAccess;
import org.newsclub.net.unix.SocketOptionsMapper;

public abstract class AFServerSocketChannel<A extends AFSocketAddress>
extends ServerSocketChannel
implements FileDescriptorAccess,
AFSomeSocketChannel {
    private final @NonNull AFServerSocket<A> afSocket;

    protected AFServerSocketChannel(AFServerSocket<A> socket, AFSelectorProvider<A> sp) {
        super(sp);
        this.afSocket = Objects.requireNonNull(socket);
    }

    @Override
    public <T> T getOption(SocketOption<T> name) throws IOException {
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
    public <T> AFServerSocketChannel<A> setOption(SocketOption<T> name, T value) throws IOException {
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
    public final AFServerSocketChannel<A> bind(SocketAddress local, int backlog) throws IOException {
        this.afSocket.bind(local, backlog);
        return this;
    }

    @Override
    @SuppressFBWarnings(value={"EI_EXPOSE_REP"})
    public final AFServerSocket<A> socket() {
        return this.afSocket;
    }

    @Override
    public AFSocketChannel<A> accept() throws IOException {
        AFSocket<A> socket = this.afSocket.accept1(false);
        return socket == null ? null : socket.getChannel();
    }

    public final @Nullable A getLocalAddress() {
        return (A)this.getLocalSocketAddress();
    }

    public final @Nullable A getLocalSocketAddress() {
        return (A)this.afSocket.getLocalSocketAddress();
    }

    public final boolean isLocalSocketAddressValid() {
        return this.afSocket.isLocalSocketAddressValid();
    }

    @Override
    protected final void implCloseSelectableChannel() throws IOException {
        this.afSocket.close();
    }

    @Override
    protected final void implConfigureBlocking(boolean block) throws IOException {
        this.getAFCore().implConfigureBlocking(block);
    }

    final AFSocketCore getAFCore() {
        return this.afSocket.getAFImpl().getCore();
    }

    @Override
    public final FileDescriptor getFileDescriptor() throws IOException {
        return this.afSocket.getFileDescriptor();
    }

    public final boolean isDeleteOnClose() {
        return ((AFServerSocket)this.socket()).isDeleteOnClose();
    }

    public final void setDeleteOnClose(boolean b) {
        ((AFServerSocket)this.socket()).setDeleteOnClose(b);
    }

    @Override
    public void setShutdownOnClose(boolean enabled) {
        ((AFServerSocket)this.socket()).setShutdownOnClose(enabled);
    }
}

