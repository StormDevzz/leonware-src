// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.nio.channels.NetworkChannel;
import java.net.DatagramSocket;
import java.io.FileDescriptor;
import java.util.Set;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.net.SocketAddress;
import java.io.IOException;
import java.nio.channels.MembershipKey;
import java.net.NetworkInterface;
import java.net.InetAddress;
import java.nio.channels.spi.SelectorProvider;
import java.nio.channels.DatagramChannel;

public abstract class AFDatagramChannel<A extends AFSocketAddress> extends DatagramChannel implements AFSomeSocket, AFSocketExtensions, AFSomeSocketChannel
{
    private final AFDatagramSocket<A> afSocket;
    
    protected AFDatagramChannel(final AFSelectorProvider<A> selectorProvider, final AFDatagramSocket<A> socket) {
        super(selectorProvider);
        this.afSocket = socket;
    }
    
    protected final AFDatagramSocket<A> getAFSocket() {
        return this.afSocket;
    }
    
    @Override
    public final MembershipKey join(final InetAddress group, final NetworkInterface interf) throws IOException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final MembershipKey join(final InetAddress group, final NetworkInterface interf, final InetAddress source) throws IOException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final AFDatagramChannel<A> bind(final SocketAddress local) throws IOException {
        this.afSocket.bind(local);
        return this;
    }
    
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    @Override
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
    public final AFDatagramChannel<A> connect(final SocketAddress remote) throws IOException {
        this.afSocket.connect(remote);
        return this;
    }
    
    @Override
    public final AFDatagramChannel<A> disconnect() throws IOException {
        this.afSocket.disconnect();
        return this;
    }
    
    @Override
    public final A getRemoteAddress() throws IOException {
        return this.getRemoteSocketAddress();
    }
    
    @Override
    public final A getRemoteSocketAddress() {
        return this.afSocket.getRemoteSocketAddress();
    }
    
    @Override
    public final A getLocalAddress() throws IOException {
        return this.getLocalSocketAddress();
    }
    
    @Override
    public final A getLocalSocketAddress() {
        return this.afSocket.getLocalSocketAddress();
    }
    
    @Override
    public final A receive(final ByteBuffer dst) throws IOException {
        return this.afSocket.getAFImpl().receive(dst);
    }
    
    @Override
    public final int send(final ByteBuffer src, final SocketAddress target) throws IOException {
        return this.afSocket.getAFImpl().send(src, target);
    }
    
    @Override
    public final int read(final ByteBuffer dst) throws IOException {
        return this.afSocket.getAFImpl().read(dst, null);
    }
    
    @Override
    public final long read(final ByteBuffer[] dsts, final int offset, final int length) throws IOException {
        if (length == 0) {
            return 0L;
        }
        return this.read(dsts[offset]);
    }
    
    @Override
    public final int write(final ByteBuffer src) throws IOException {
        return this.afSocket.getAFImpl().write(src);
    }
    
    @Override
    public final long write(final ByteBuffer[] srcs, final int offset, final int length) throws IOException {
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
    protected final void implConfigureBlocking(final boolean block) throws IOException {
        this.getAFCore().implConfigureBlocking(block);
    }
    
    @Override
    public final int getAncillaryReceiveBufferSize() {
        return this.afSocket.getAncillaryReceiveBufferSize();
    }
    
    @Override
    public final void setAncillaryReceiveBufferSize(final int size) {
        this.afSocket.setAncillaryReceiveBufferSize(size);
    }
    
    @Override
    public final void ensureAncillaryReceiveBufferSize(final int minSize) {
        this.afSocket.ensureAncillaryReceiveBufferSize(minSize);
    }
    
    @Override
    public final <T> AFDatagramChannel<A> setOption(final SocketOption<T> name, final T value) throws IOException {
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
    public final <T> T getOption(final SocketOption<T> name) throws IOException {
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
    
    public final void setDeleteOnClose(final boolean b) {
        this.afSocket.setDeleteOnClose(b);
    }
    
    @Override
    public void setShutdownOnClose(final boolean enabled) {
        this.getAFCore().setShutdownOnClose(enabled);
    }
}
