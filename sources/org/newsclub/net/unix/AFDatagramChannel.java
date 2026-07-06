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
import java.nio.channels.NetworkChannel;
import java.util.Set;
import org.newsclub.net.unix.AFSocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFDatagramChannel.class */
public abstract class AFDatagramChannel<A extends AFSocketAddress> extends DatagramChannel implements AFSomeSocket, AFSocketExtensions, AFSomeSocketChannel {
    private final AFDatagramSocket<A> afSocket;

    @Override // java.nio.channels.DatagramChannel, java.nio.channels.NetworkChannel
    public /* bridge */ /* synthetic */ DatagramChannel setOption(SocketOption socketOption, Object obj) throws IOException {
        return setOption((SocketOption<Object>) socketOption, obj);
    }

    @Override // java.nio.channels.DatagramChannel, java.nio.channels.NetworkChannel
    public /* bridge */ /* synthetic */ NetworkChannel setOption(SocketOption socketOption, Object obj) throws IOException {
        return setOption((SocketOption<Object>) socketOption, obj);
    }

    protected AFDatagramChannel(AFSelectorProvider<A> selectorProvider, AFDatagramSocket<A> socket) {
        super(selectorProvider);
        this.afSocket = socket;
    }

    protected final AFDatagramSocket<A> getAFSocket() {
        return this.afSocket;
    }

    @Override // java.nio.channels.MulticastChannel
    public final MembershipKey join(InetAddress group, NetworkInterface interf) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // java.nio.channels.MulticastChannel
    public final MembershipKey join(InetAddress group, NetworkInterface interf, InetAddress source) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // java.nio.channels.DatagramChannel, java.nio.channels.NetworkChannel
    public final AFDatagramChannel<A> bind(SocketAddress local) throws IOException {
        this.afSocket.bind(local);
        return this;
    }

    @Override // java.nio.channels.DatagramChannel
    @SuppressFBWarnings({"EI_EXPOSE_REP"})
    public final AFDatagramSocket<A> socket() {
        return this.afSocket;
    }

    public final boolean isBound() {
        return this.afSocket.isBound();
    }

    @Override // java.nio.channels.DatagramChannel
    public final boolean isConnected() {
        return this.afSocket.isConnected();
    }

    @Override // java.nio.channels.DatagramChannel
    public final AFDatagramChannel<A> connect(SocketAddress remote) throws IOException {
        this.afSocket.connect(remote);
        return this;
    }

    @Override // java.nio.channels.DatagramChannel
    public final AFDatagramChannel<A> disconnect() throws IOException {
        this.afSocket.disconnect();
        return this;
    }

    @Override // java.nio.channels.DatagramChannel
    public final A getRemoteAddress() throws IOException {
        return (A) getRemoteSocketAddress();
    }

    @Override // org.newsclub.net.unix.AFSomeSocket
    public final A getRemoteSocketAddress() {
        return (A) this.afSocket.getRemoteSocketAddress();
    }

    @Override // java.nio.channels.DatagramChannel, java.nio.channels.NetworkChannel
    public final A getLocalAddress() throws IOException {
        return (A) getLocalSocketAddress();
    }

    @Override // org.newsclub.net.unix.AFSomeSocketThing
    public final A getLocalSocketAddress() {
        return (A) this.afSocket.getLocalSocketAddress();
    }

    @Override // java.nio.channels.DatagramChannel
    public final A receive(ByteBuffer byteBuffer) throws IOException {
        return (A) this.afSocket.getAFImpl().receive(byteBuffer);
    }

    @Override // java.nio.channels.DatagramChannel
    public final int send(ByteBuffer src, SocketAddress target) throws IOException {
        return this.afSocket.getAFImpl().send(src, target);
    }

    @Override // java.nio.channels.DatagramChannel, java.nio.channels.ReadableByteChannel
    public final int read(ByteBuffer dst) throws IOException {
        return this.afSocket.getAFImpl().read(dst, null);
    }

    @Override // java.nio.channels.DatagramChannel, java.nio.channels.ScatteringByteChannel
    public final long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        if (length == 0) {
            return 0L;
        }
        return read(dsts[offset]);
    }

    @Override // java.nio.channels.DatagramChannel, java.nio.channels.WritableByteChannel
    public final int write(ByteBuffer src) throws IOException {
        return this.afSocket.getAFImpl().write(src);
    }

    @Override // java.nio.channels.DatagramChannel, java.nio.channels.GatheringByteChannel
    public final long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        if (length == 0) {
            return 0L;
        }
        return write(srcs[offset]);
    }

    @Override // java.nio.channels.spi.AbstractSelectableChannel
    protected final void implCloseSelectableChannel() throws IOException {
        getAFSocket().close();
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

    @Override // java.nio.channels.DatagramChannel, java.nio.channels.NetworkChannel
    public final <T> AFDatagramChannel<A> setOption(SocketOption<T> name, T value) throws IOException {
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

    @Override // java.nio.channels.NetworkChannel
    public final Set<SocketOption<?>> supportedOptions() {
        return SocketOptionsMapper.SUPPORTED_SOCKET_OPTIONS;
    }

    final AFSocketCore getAFCore() {
        return this.afSocket.getAFImpl().getCore();
    }

    @Override // org.newsclub.net.unix.FileDescriptorAccess
    public final FileDescriptor getFileDescriptor() throws IOException {
        return this.afSocket.getFileDescriptor();
    }

    public final boolean isDeleteOnClose() {
        return this.afSocket.isDeleteOnClose();
    }

    public final void setDeleteOnClose(boolean b) {
        this.afSocket.setDeleteOnClose(b);
    }

    @Override // org.newsclub.net.unix.AFSomeSocketThing
    public void setShutdownOnClose(boolean enabled) {
        getAFCore().setShutdownOnClose(enabled);
    }
}
