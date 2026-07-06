package org.newsclub.net.unix;

import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.channels.NetworkChannel;
import java.nio.channels.ServerSocketChannel;
import java.util.Objects;
import java.util.Set;
import org.newsclub.net.unix.AFSocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFServerSocketChannel.class */
public abstract class AFServerSocketChannel<A extends AFSocketAddress> extends ServerSocketChannel implements FileDescriptorAccess, AFSomeSocketChannel {
    private final AFServerSocket<A> afSocket;

    @Override // java.nio.channels.ServerSocketChannel, java.nio.channels.NetworkChannel
    public /* bridge */ /* synthetic */ ServerSocketChannel setOption(SocketOption socketOption, Object obj) throws IOException {
        return setOption((SocketOption<Object>) socketOption, obj);
    }

    @Override // java.nio.channels.ServerSocketChannel, java.nio.channels.NetworkChannel
    public /* bridge */ /* synthetic */ NetworkChannel setOption(SocketOption socketOption, Object obj) throws IOException {
        return setOption((SocketOption<Object>) socketOption, obj);
    }

    protected AFServerSocketChannel(AFServerSocket<A> socket, AFSelectorProvider<A> sp) {
        super(sp);
        this.afSocket = (AFServerSocket) Objects.requireNonNull(socket);
    }

    @Override // java.nio.channels.NetworkChannel
    public <T> T getOption(SocketOption<T> socketOption) throws IOException {
        if (socketOption instanceof AFSocketOption) {
            return (T) getAFCore().getOption((AFSocketOption) socketOption);
        }
        Integer numResolve = SocketOptionsMapper.resolve(socketOption);
        if (numResolve == null) {
            throw new UnsupportedOperationException("unsupported option");
        }
        return (T) this.afSocket.getAFImpl().getOption(numResolve.intValue());
    }

    @Override // java.nio.channels.ServerSocketChannel, java.nio.channels.NetworkChannel
    public <T> AFServerSocketChannel<A> setOption(SocketOption<T> name, T value) throws IOException {
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

    @Override // java.nio.channels.ServerSocketChannel
    public final AFServerSocketChannel<A> bind(SocketAddress local, int backlog) throws IOException {
        this.afSocket.bind(local, backlog);
        return this;
    }

    @Override // java.nio.channels.ServerSocketChannel
    @SuppressFBWarnings({"EI_EXPOSE_REP"})
    public final AFServerSocket<A> socket() {
        return this.afSocket;
    }

    @Override // java.nio.channels.ServerSocketChannel
    public AFSocketChannel<A> accept() throws IOException {
        AFSocket<A> socket = this.afSocket.accept1(false);
        if (socket == null) {
            return null;
        }
        return socket.getChannel();
    }

    @Override // java.nio.channels.ServerSocketChannel, java.nio.channels.NetworkChannel
    public final A getLocalAddress() {
        return (A) getLocalSocketAddress();
    }

    @Override // org.newsclub.net.unix.AFSomeSocketThing
    public final A getLocalSocketAddress() {
        return (A) this.afSocket.getLocalSocketAddress();
    }

    public final boolean isLocalSocketAddressValid() {
        return this.afSocket.isLocalSocketAddressValid();
    }

    @Override // java.nio.channels.spi.AbstractSelectableChannel
    protected final void implCloseSelectableChannel() throws IOException {
        this.afSocket.close();
    }

    @Override // java.nio.channels.spi.AbstractSelectableChannel
    protected final void implConfigureBlocking(boolean block) throws IOException {
        getAFCore().implConfigureBlocking(block);
    }

    final AFSocketCore getAFCore() {
        return this.afSocket.getAFImpl().getCore();
    }

    @Override // org.newsclub.net.unix.FileDescriptorAccess
    public final FileDescriptor getFileDescriptor() throws IOException {
        return this.afSocket.getFileDescriptor();
    }

    public final boolean isDeleteOnClose() {
        return socket().isDeleteOnClose();
    }

    public final void setDeleteOnClose(boolean b) {
        socket().setDeleteOnClose(b);
    }

    @Override // org.newsclub.net.unix.AFSomeSocketThing
    public void setShutdownOnClose(boolean enabled) {
        socket().setShutdownOnClose(enabled);
    }
}
