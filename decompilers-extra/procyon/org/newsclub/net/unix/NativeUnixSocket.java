// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.nio.channels.SelectionKey;
import java.net.Socket;
import java.net.SocketException;
import java.io.Closeable;
import java.net.SocketAddress;
import java.net.ServerSocket;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.io.FileDescriptor;
import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.net.InetSocketAddress;
import java.nio.channels.spi.AbstractSelectableChannel;
import com.kohlschutter.annotations.compiletime.ExcludeFromCodeCoverageGeneratedReport;
import java.util.concurrent.atomic.AtomicBoolean;

final class NativeUnixSocket
{
    private static final AtomicBoolean LOADED;
    static final int DOMAIN_GENERIC = -1;
    static final int DOMAIN_UNIX = 1;
    static final int DOMAIN_TIPC = 30;
    static final int DOMAIN_VSOCK = 40;
    static final int DOMAIN_SYSTEM = 32;
    static final int SOCK_STREAM = 1;
    static final int SOCK_DGRAM = 2;
    static final int SOCK_RAW = 3;
    static final int SOCK_RDM = 4;
    static final int SOCK_SEQPACKET = 5;
    static final int OPT_LOOKUP_SENDER = 1;
    static final int OPT_PEEK = 2;
    static final int OPT_NON_BLOCKING = 4;
    static final int OPT_NON_SOCKET = 8;
    static final int OPT_DGRAM_MODE = 16;
    static final int BIND_OPT_REUSE = 1;
    static final int SOCKETSTATUS_INVALID = -1;
    static final int SOCKETSTATUS_UNKNOWN = 0;
    static final int SOCKETSTATUS_BOUND = 1;
    static final int SOCKETSTATUS_CONNECTED = 2;
    private static Throwable initError;
    static final int SHUT_RD = 0;
    static final int SHUT_WR = 1;
    static final int SHUT_RD_WR = 2;
    
    @ExcludeFromCodeCoverageGeneratedReport(reason = "unreachable")
    private NativeUnixSocket() {
        throw new UnsupportedOperationException("No instances");
    }
    
    static boolean isLoaded() {
        return NativeUnixSocket.LOADED.get();
    }
    
    static void ensureSupported() throws UnsupportedOperationException {
        if (!isLoaded()) {
            throw unsupportedException();
        }
    }
    
    static UnsupportedOperationException unsupportedException() {
        if (!isLoaded()) {
            return (UnsupportedOperationException)new UnsupportedOperationException("junixsocket may not be fully supported on this platform").initCause(NativeUnixSocket.initError);
        }
        return null;
    }
    
    static Throwable retrieveInitError() {
        return NativeUnixSocket.initError;
    }
    
    static void initPre() {
        tryResolveClass(AbstractSelectableChannel.class.getName());
        tryResolveClass("java.lang.ProcessBuilder$RedirectPipeImpl");
        tryResolveClass(InetSocketAddress.class.getName());
        tryResolveClass(InvalidArgumentSocketException.class.getName());
        tryResolveClass(AddressUnavailableSocketException.class.getName());
        tryResolveClass(OperationNotSupportedSocketException.class.getName());
        tryResolveClass(NoSuchDeviceSocketException.class.getName());
        tryResolveClass(BrokenPipeSocketException.class.getName());
        tryResolveClass(ConnectionResetSocketException.class.getName());
        tryResolveClass(SocketClosedException.class.getName());
    }
    
    private static void tryResolveClass(final String className) {
        try {
            Class.forName(className);
        }
        catch (final Exception ex) {}
    }
    
    @SuppressFBWarnings({ "THROWS_METHOD_THROWS_CLAUSE_BASIC_EXCEPTION" })
    static native void init() throws Exception;
    
    @SuppressFBWarnings({ "THROWS_METHOD_THROWS_CLAUSE_BASIC_EXCEPTION" })
    static native void destroy() throws Exception;
    
    static native void noop();
    
    static native int capabilities();
    
    static native byte[] sockname(final int p0, final FileDescriptor p1, final boolean p2);
    
    static native long bind(final ByteBuffer p0, final int p1, final FileDescriptor p2, final int p3) throws IOException;
    
    static native void listen(final FileDescriptor p0, final int p1) throws IOException;
    
    static native boolean accept(final ByteBuffer p0, final int p1, final FileDescriptor p2, final FileDescriptor p3, final long p4, final int p5) throws IOException;
    
    static native boolean connect(final ByteBuffer p0, final int p1, final FileDescriptor p2, final long p3) throws IOException;
    
    static native boolean finishConnect(final FileDescriptor p0) throws IOException;
    
    static native void disconnect(final FileDescriptor p0) throws IOException;
    
    static native int socketStatus(final FileDescriptor p0) throws IOException;
    
    static native FileDescriptor duplicate(final FileDescriptor p0, final FileDescriptor p1) throws IOException;
    
    static native Class<?> primaryType(final FileDescriptor p0) throws IOException;
    
    static native int read(final FileDescriptor p0, final byte[] p1, final int p2, final int p3, final int p4, final AncillaryDataSupport p5, final int p6) throws IOException;
    
    static native int write(final FileDescriptor p0, final byte[] p1, final int p2, final int p3, final int p4, final AncillaryDataSupport p5) throws IOException;
    
    static native int receive(final FileDescriptor p0, final ByteBuffer p1, final int p2, final int p3, final ByteBuffer p4, final int p5, final AncillaryDataSupport p6, final int p7) throws IOException;
    
    static native int send(final FileDescriptor p0, final ByteBuffer p1, final int p2, final int p3, final ByteBuffer p4, final int p5, final int p6, final AncillaryDataSupport p7) throws IOException;
    
    static native void close(final FileDescriptor p0) throws IOException;
    
    static native void shutdown(final FileDescriptor p0, final int p1) throws IOException;
    
    static native int getSocketOptionInt(final FileDescriptor p0, final int p1) throws IOException;
    
    static native void setSocketOptionInt(final FileDescriptor p0, final int p1, final int p2) throws IOException;
    
    static native <T> T getSocketOption(final FileDescriptor p0, final int p1, final int p2, final Class<T> p3) throws IOException;
    
    static native void setSocketOption(final FileDescriptor p0, final int p1, final int p2, final Object p3) throws IOException;
    
    static native int available(final FileDescriptor p0, final ByteBuffer p1) throws IOException;
    
    static native AFUNIXSocketCredentials peerCredentials(final FileDescriptor p0, final AFUNIXSocketCredentials p1) throws IOException;
    
    static native void initServerImpl(final ServerSocket p0, final AFSocketImpl<?> p1) throws IOException;
    
    static native void createSocket(final FileDescriptor p0, final int p1, final int p2) throws IOException;
    
    static native void setPort(final SocketAddress p0, final int p1);
    
    static native void initFD(final FileDescriptor p0, final int p1) throws IOException;
    
    static native int getFD(final FileDescriptor p0) throws IOException;
    
    static native void copyFileDescriptor(final FileDescriptor p0, final FileDescriptor p1) throws IOException;
    
    static native void attachCloseable(final FileDescriptor p0, final Closeable p1) throws SocketException;
    
    static native int maxAddressLength();
    
    static native int sockAddrLength(final int p0);
    
    static native int ancillaryBufMinLen();
    
    static native byte[] sockAddrToBytes(final int p0, final ByteBuffer p1);
    
    static native int bytesToSockAddr(final int p0, final ByteBuffer p1, final byte[] p2);
    
    @SuppressFBWarnings({ "THROWS_METHOD_THROWS_RUNTIMEEXCEPTION" })
    static void setPort1(final SocketAddress addr, final int port) throws SocketException {
        if (port < 0) {
            throw new IllegalArgumentException("port out of range:" + port);
        }
        try {
            setPort(addr, port);
        }
        catch (final RuntimeException e) {
            throw e;
        }
        catch (final Exception e2) {
            throw (SocketException)new SocketException("Could not set port").initCause(e2);
        }
    }
    
    static native Socket currentRMISocket();
    
    static native boolean initPipe(final FileDescriptor p0, final FileDescriptor p1, final boolean p2) throws IOException;
    
    static native int poll(final AFSelector.PollFd p0, final int p1) throws IOException;
    
    static native void configureBlocking(final FileDescriptor p0, final boolean p1) throws IOException;
    
    static native int checkBlocking(final FileDescriptor p0) throws IOException;
    
    static native void socketPair(final int p0, final int p1, final FileDescriptor p2, final FileDescriptor p3);
    
    static native ProcessBuilder.Redirect initRedirect(final FileDescriptor p0);
    
    static native void deregisterSelectionKey(final AbstractSelectableChannel p0, final SelectionKey p1);
    
    static native byte[] tipcGetNodeId(final int p0) throws IOException;
    
    static native byte[] tipcGetLinkName(final int p0, final int p1) throws IOException;
    
    static native int sockAddrNativeDataOffset();
    
    static native int sockAddrNativeFamilyOffset();
    
    static native int sockTypeToNative(final int p0) throws IOException;
    
    static native int vsockGetLocalCID() throws IOException;
    
    static native int systemResolveCtlId(final FileDescriptor p0, final String p1) throws IOException;
    
    static void setLoaded(final boolean successful) {
        NativeUnixSocket.LOADED.compareAndSet(false, successful);
    }
    
    static {
        LOADED = new AtomicBoolean(false);
        NativeUnixSocket.initError = null;
        boolean loadSuccessful = false;
        try (final NativeLibraryLoader nll = new NativeLibraryLoader()) {
            nll.loadLibrary();
            loadSuccessful = true;
        }
        catch (final RuntimeException | Error e) {
            StackTraceUtil.printStackTraceSevere(NativeUnixSocket.initError = e);
        }
        finally {
            setLoaded(loadSuccessful);
        }
        AFAddressFamily.registerAddressFamily("generic", -1, "org.newsclub.net.unix.AFGenericSocketAddress");
        AFAddressFamily.registerAddressFamily("un", 1, "org.newsclub.net.unix.AFUNIXSocketAddress");
        AFAddressFamily.registerAddressFamily("tipc", 30, "org.newsclub.net.unix.AFTIPCSocketAddress");
        AFAddressFamily.registerAddressFamily("vsock", 40, "org.newsclub.net.unix.AFVSOCKSocketAddress");
        AFAddressFamily.registerAddressFamily("system", 32, "org.newsclub.net.unix.AFSYSTEMSocketAddress");
    }
}
