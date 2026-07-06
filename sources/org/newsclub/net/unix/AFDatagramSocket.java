package org.newsclub.net.unix;

import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.AlreadyBoundException;
import java.nio.channels.IllegalBlockingModeException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.newsclub.net.unix.AFSocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFDatagramSocket.class */
public abstract class AFDatagramSocket<A extends AFSocketAddress> extends DatagramSocketShim implements AFSomeSocket, AFSocketExtensions {
    private static final InetSocketAddress WILDCARD_ADDRESS = new InetSocketAddress(0);
    private final AFDatagramSocketImpl<A> impl;
    private final AncillaryDataSupport ancillaryDataSupport;
    private final AtomicBoolean created;
    private final AtomicBoolean deleteOnClose;
    private final AFDatagramChannel<A> channel;

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFDatagramSocket$Constructor.class */
    @FunctionalInterface
    public interface Constructor<A extends AFSocketAddress> {
        AFDatagramSocket<A> newSocket(FileDescriptor fileDescriptor) throws IOException;
    }

    protected abstract AFDatagramChannel<A> newChannel();

    protected abstract AFDatagramSocket<A> newDatagramSocketInstance() throws IOException;

    protected AFDatagramSocket(AFDatagramSocketImpl<A> impl) {
        super(impl);
        this.created = new AtomicBoolean(false);
        this.deleteOnClose = new AtomicBoolean(true);
        this.channel = newChannel();
        this.impl = impl;
        this.ancillaryDataSupport = impl.ancillaryDataSupport;
    }

    final AncillaryDataSupport getAncillaryDataSupport() {
        return this.ancillaryDataSupport;
    }

    protected final Class<? extends AFSocketAddress> socketAddressClass() {
        return this.impl.getAddressFamily().getSocketAddressClass();
    }

    protected static final <A extends AFSocketAddress> AFDatagramSocket<A> newInstance(Constructor<A> constructor) throws IOException {
        return constructor.newSocket(null);
    }

    protected static final <A extends AFSocketAddress> AFDatagramSocket<A> newInstance(Constructor<A> constructor, FileDescriptor fdObj, int localPort, int remotePort) throws IOException {
        if (fdObj == null) {
            return newInstance(constructor);
        }
        if (!fdObj.valid()) {
            throw new SocketException("Invalid file descriptor");
        }
        int status = NativeUnixSocket.socketStatus(fdObj);
        if (status == -1) {
            throw new SocketException("Not a valid socket");
        }
        AFDatagramSocket<A> socket = constructor.newSocket(fdObj);
        socket.getAFImpl().updatePorts(localPort, remotePort);
        switch (status) {
            case 0:
                break;
            case 1:
                socket.internalDummyBind();
                break;
            case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                socket.internalDummyConnect();
                break;
            default:
                throw new IllegalStateException("Invalid socketStatus response: " + status);
        }
        return socket;
    }

    @Override // java.net.DatagramSocket
    public final void connect(InetAddress address, int port) {
        throw new IllegalArgumentException("Cannot connect to InetAddress");
    }

    public final void peek(DatagramPacket p) throws IOException {
        synchronized (p) {
            if (isClosed()) {
                throw new SocketException("Socket is closed");
            }
            getAFImpl().peekData(p);
        }
    }

    @Override // java.net.DatagramSocket
    public final void send(DatagramPacket p) throws IOException {
        synchronized (p) {
            if (isClosed()) {
                throw new SocketException("Socket is closed");
            }
            if (!isBound()) {
                internalDummyBind();
            }
            getAFImpl().send(p);
        }
    }

    final void internalDummyConnect() throws SocketException {
        super.connect(AFSocketAddress.INTERNAL_DUMMY_DONT_CONNECT);
    }

    final void internalDummyBind() throws SocketException {
        bind(AFSocketAddress.INTERNAL_DUMMY_BIND);
    }

    @Override // java.net.DatagramSocket
    public final synchronized void connect(SocketAddress addr) throws SocketException {
        if (!isBound()) {
            internalDummyBind();
        }
        internalDummyConnect();
        try {
            getAFImpl().connect(AFSocketAddress.preprocessSocketAddress(socketAddressClass(), addr, null));
        } catch (SocketException e) {
            throw e;
        } catch (IOException e2) {
            throw ((SocketException) new SocketException(e2.getMessage()).initCause(e2));
        }
    }

    @Override // java.net.DatagramSocket, org.newsclub.net.unix.AFSomeSocket
    public final synchronized A getRemoteSocketAddress() {
        return (A) getAFImpl().getRemoteSocketAddress();
    }

    @Override // java.net.DatagramSocket
    public final boolean isConnected() {
        return super.isConnected() || this.impl.isConnected();
    }

    @Override // java.net.DatagramSocket
    public final boolean isBound() {
        return super.isBound() || this.impl.isBound();
    }

    @Override // java.net.DatagramSocket, java.io.Closeable, java.lang.AutoCloseable
    public final void close() {
        if (isClosed()) {
            return;
        }
        getAFImpl().close();
        boolean wasBound = isBound();
        if (wasBound && this.deleteOnClose.get()) {
            InetAddress addr = getLocalAddress();
            if (AFInetAddress.isSupportedAddress(addr, addressFamily())) {
                try {
                    AFSocketAddress socketAddress = AFSocketAddress.unwrap(addr, 0, addressFamily());
                    if (socketAddress != null && socketAddress.hasFilename()) {
                        if (!socketAddress.getFile().delete()) {
                        }
                    }
                } catch (IOException e) {
                }
            }
        }
        super.close();
    }

    @Override // java.net.DatagramSocket
    public final synchronized void bind(SocketAddress addr) throws SocketException {
        boolean isBound = isBound();
        if (isBound && addr == AFSocketAddress.INTERNAL_DUMMY_BIND) {
            return;
        }
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (!isBound) {
            try {
                super.bind(AFSocketAddress.INTERNAL_DUMMY_BIND);
            } catch (SocketException e) {
                String message = e.getMessage();
                if (message == null || !message.contains("already bound")) {
                    throw e;
                }
            } catch (AlreadyBoundException e2) {
            }
        }
        boolean isWildcardBind = WILDCARD_ADDRESS.equals(addr);
        AFSocketAddress epoint = (addr == null || isWildcardBind) ? null : AFSocketAddress.preprocessSocketAddress(socketAddressClass(), addr, null);
        if (epoint instanceof SentinelSocketAddress) {
            return;
        }
        try {
            getAFImpl().bind(epoint);
        } catch (SocketException e3) {
            if (!isWildcardBind) {
                getAFImpl().close();
                throw e3;
            }
        }
    }

    @Override // java.net.DatagramSocket, org.newsclub.net.unix.AFSomeSocketThing
    public final A getLocalSocketAddress() {
        if (isClosed() || !isBound()) {
            return null;
        }
        return (A) getAFImpl().getLocalSocketAddress();
    }

    public final boolean isDeleteOnClose() {
        return this.deleteOnClose.get();
    }

    public final void setDeleteOnClose(boolean b) {
        this.deleteOnClose.set(b);
    }

    final AFDatagramSocketImpl<A> getAFImpl() {
        if (this.created.compareAndSet(false, true)) {
            try {
                getSoTimeout();
            } catch (SocketException e) {
            }
        }
        return this.impl;
    }

    final AFDatagramSocketImpl<A> getAFImpl(boolean create) {
        if (create) {
            return getAFImpl();
        }
        return this.impl;
    }

    @Override // org.newsclub.net.unix.AFSocketExtensions
    public final int getAncillaryReceiveBufferSize() {
        return this.ancillaryDataSupport.getAncillaryReceiveBufferSize();
    }

    @Override // org.newsclub.net.unix.AFSocketExtensions
    public final void setAncillaryReceiveBufferSize(int size) {
        this.ancillaryDataSupport.setAncillaryReceiveBufferSize(size);
    }

    @Override // org.newsclub.net.unix.AFSocketExtensions
    public final void ensureAncillaryReceiveBufferSize(int minSize) {
        this.ancillaryDataSupport.ensureAncillaryReceiveBufferSize(minSize);
    }

    @Override // java.net.DatagramSocket
    public final boolean isClosed() {
        return super.isClosed() || getAFImpl().isClosed();
    }

    @Override // java.net.DatagramSocket
    @SuppressFBWarnings({"EI_EXPOSE_REP"})
    public AFDatagramChannel<A> getChannel() {
        return this.channel;
    }

    @Override // org.newsclub.net.unix.FileDescriptorAccess
    public final FileDescriptor getFileDescriptor() throws IOException {
        return getAFImpl().getFileDescriptor();
    }

    @Override // java.net.DatagramSocket
    public final void receive(DatagramPacket p) throws IOException {
        getAFImpl().receive(p);
    }

    protected final AFAddressFamily<A> addressFamily() {
        return getAFImpl().getAddressFamily();
    }

    protected AFSocketImplExtensions<A> getImplExtensions() {
        return getAFImpl(false).getImplExtensions();
    }

    @Override // org.newsclub.net.unix.DatagramSocketShim
    public <T> T getOption(AFSocketOption<T> aFSocketOption) throws IOException {
        return (T) getAFImpl().getCore().getOption(aFSocketOption);
    }

    @Override // org.newsclub.net.unix.DatagramSocketShim
    public <T> DatagramSocket setOption(AFSocketOption<T> name, T value) throws IOException {
        getAFImpl().getCore().setOption(name, value);
        return this;
    }

    public AFDatagramSocket<A> accept() throws IOException {
        return accept1(true);
    }

    public final void listen(int backlog) throws IOException {
        FileDescriptor fdesc = getAFImpl().getCore().validFdOrException();
        if (backlog <= 0) {
            backlog = 50;
        }
        NativeUnixSocket.listen(fdesc, backlog);
    }

    AFDatagramSocket<A> accept1(boolean throwOnFail) throws IOException {
        AFDatagramSocket<A> as = newDatagramSocketInstance();
        boolean success = getAFImpl().accept0(as.getAFImpl(false));
        if (isClosed()) {
            throw new SocketClosedException("Socket is closed");
        }
        if (!success) {
            if (!throwOnFail || getChannel().isBlocking()) {
                return null;
            }
            throw new IllegalBlockingModeException();
        }
        as.getAFImpl(true);
        as.connect(AFSocketAddress.INTERNAL_DUMMY_CONNECT);
        as.getAFImpl().updatePorts(getAFImpl().getLocalPort1(), getAFImpl().getRemotePort());
        return as;
    }

    @Override // org.newsclub.net.unix.AFSomeSocketThing
    public void setShutdownOnClose(boolean enabled) {
        getAFImpl().getCore().setShutdownOnClose(enabled);
    }
}
