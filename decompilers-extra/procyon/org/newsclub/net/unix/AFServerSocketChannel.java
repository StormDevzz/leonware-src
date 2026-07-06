// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.nio.channels.NetworkChannel;
import java.net.ServerSocket;
import java.nio.channels.SocketChannel;
import java.io.FileDescriptor;
import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.net.SocketAddress;
import java.util.Set;
import java.io.IOException;
import java.net.SocketOption;
import java.util.Objects;
import java.nio.channels.spi.SelectorProvider;
import java.nio.channels.ServerSocketChannel;

public abstract class AFServerSocketChannel<A extends AFSocketAddress> extends ServerSocketChannel implements FileDescriptorAccess, AFSomeSocketChannel
{
    private final AFServerSocket<A> afSocket;
    
    protected AFServerSocketChannel(final AFServerSocket<A> socket, final AFSelectorProvider<A> sp) {
        super(sp);
        this.afSocket = Objects.requireNonNull(socket);
    }
    
    @Override
    public <T> T getOption(final SocketOption<T> name) throws IOException {
        if (name instanceof AFSocketOption) {
            return this.getAFCore().getOption((AFSocketOption<T>)(AFSocketOption)name);
        }
        final Integer optionId = SocketOptionsMapper.resolve(name);
        if (optionId == null) {
            throw new UnsupportedOperationException("unsupported option");
        }
        return (T)this.afSocket.getAFImpl().getOption(optionId);
    }
    
    @Override
    public <T> AFServerSocketChannel<A> setOption(final SocketOption<T> name, final T value) throws IOException {
        if (name instanceof AFSocketOption) {
            this.getAFCore().setOption((AFSocketOption)name, value);
            return this;
        }
        final Integer optionId = SocketOptionsMapper.resolve(name);
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
    public final AFServerSocketChannel<A> bind(final SocketAddress local, final int backlog) throws IOException {
        this.afSocket.bind(local, backlog);
        return this;
    }
    
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    @Override
    public final AFServerSocket<A> socket() {
        return this.afSocket;
    }
    
    @Override
    public AFSocketChannel<A> accept() throws IOException {
        final AFSocket<A> socket = this.afSocket.accept1(false);
        return (socket == null) ? null : socket.getChannel();
    }
    
    @Override
    public final A getLocalAddress() {
        return this.getLocalSocketAddress();
    }
    
    @Override
    public final A getLocalSocketAddress() {
        return this.afSocket.getLocalSocketAddress();
    }
    
    public final boolean isLocalSocketAddressValid() {
        return this.afSocket.isLocalSocketAddressValid();
    }
    
    @Override
    protected final void implCloseSelectableChannel() throws IOException {
        this.afSocket.close();
    }
    
    @Override
    protected final void implConfigureBlocking(final boolean block) throws IOException {
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
        return this.socket().isDeleteOnClose();
    }
    
    public final void setDeleteOnClose(final boolean b) {
        this.socket().setDeleteOnClose(b);
    }
    
    @Override
    public void setShutdownOnClose(final boolean enabled) {
        this.socket().setShutdownOnClose(enabled);
    }
}
