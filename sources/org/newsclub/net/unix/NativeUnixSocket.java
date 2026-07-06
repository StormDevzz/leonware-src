package org.newsclub.net.unix;

import com.kohlschutter.annotations.compiletime.ExcludeFromCodeCoverageGeneratedReport;
import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ProcessBuilder;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.concurrent.atomic.AtomicBoolean;
import org.newsclub.net.unix.AFSelector;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/NativeUnixSocket.class */
final class NativeUnixSocket {
    private static final AtomicBoolean LOADED = new AtomicBoolean(false);
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

    @SuppressFBWarnings({"THROWS_METHOD_THROWS_CLAUSE_BASIC_EXCEPTION"})
    static native void init() throws Exception;

    @SuppressFBWarnings({"THROWS_METHOD_THROWS_CLAUSE_BASIC_EXCEPTION"})
    static native void destroy() throws Exception;

    static native void noop();

    static native int capabilities();

    static native byte[] sockname(int i, FileDescriptor fileDescriptor, boolean z);

    static native long bind(ByteBuffer byteBuffer, int i, FileDescriptor fileDescriptor, int i2) throws IOException;

    static native void listen(FileDescriptor fileDescriptor, int i) throws IOException;

    static native boolean accept(ByteBuffer byteBuffer, int i, FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, long j, int i2) throws IOException;

    static native boolean connect(ByteBuffer byteBuffer, int i, FileDescriptor fileDescriptor, long j) throws IOException;

    static native boolean finishConnect(FileDescriptor fileDescriptor) throws IOException;

    static native void disconnect(FileDescriptor fileDescriptor) throws IOException;

    static native int socketStatus(FileDescriptor fileDescriptor) throws IOException;

    static native FileDescriptor duplicate(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2) throws IOException;

    static native Class<?> primaryType(FileDescriptor fileDescriptor) throws IOException;

    static native int read(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, int i3, AncillaryDataSupport ancillaryDataSupport, int i4) throws IOException;

    static native int write(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, int i3, AncillaryDataSupport ancillaryDataSupport) throws IOException;

    static native int receive(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, int i, int i2, ByteBuffer byteBuffer2, int i3, AncillaryDataSupport ancillaryDataSupport, int i4) throws IOException;

    static native int send(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, int i, int i2, ByteBuffer byteBuffer2, int i3, int i4, AncillaryDataSupport ancillaryDataSupport) throws IOException;

    static native void close(FileDescriptor fileDescriptor) throws IOException;

    static native void shutdown(FileDescriptor fileDescriptor, int i) throws IOException;

    static native int getSocketOptionInt(FileDescriptor fileDescriptor, int i) throws IOException;

    static native void setSocketOptionInt(FileDescriptor fileDescriptor, int i, int i2) throws IOException;

    static native <T> T getSocketOption(FileDescriptor fileDescriptor, int i, int i2, Class<T> cls) throws IOException;

    static native void setSocketOption(FileDescriptor fileDescriptor, int i, int i2, Object obj) throws IOException;

    static native int available(FileDescriptor fileDescriptor, ByteBuffer byteBuffer) throws IOException;

    static native AFUNIXSocketCredentials peerCredentials(FileDescriptor fileDescriptor, AFUNIXSocketCredentials aFUNIXSocketCredentials) throws IOException;

    static native void initServerImpl(ServerSocket serverSocket, AFSocketImpl<?> aFSocketImpl) throws IOException;

    static native void createSocket(FileDescriptor fileDescriptor, int i, int i2) throws IOException;

    static native void setPort(SocketAddress socketAddress, int i);

    static native void initFD(FileDescriptor fileDescriptor, int i) throws IOException;

    static native int getFD(FileDescriptor fileDescriptor) throws IOException;

    static native void copyFileDescriptor(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2) throws IOException;

    static native void attachCloseable(FileDescriptor fileDescriptor, Closeable closeable) throws SocketException;

    static native int maxAddressLength();

    static native int sockAddrLength(int i);

    static native int ancillaryBufMinLen();

    static native byte[] sockAddrToBytes(int i, ByteBuffer byteBuffer);

    static native int bytesToSockAddr(int i, ByteBuffer byteBuffer, byte[] bArr);

    static native Socket currentRMISocket();

    static native boolean initPipe(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, boolean z) throws IOException;

    static native int poll(AFSelector.PollFd pollFd, int i) throws IOException;

    static native void configureBlocking(FileDescriptor fileDescriptor, boolean z) throws IOException;

    static native int checkBlocking(FileDescriptor fileDescriptor) throws IOException;

    static native void socketPair(int i, int i2, FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2);

    static native ProcessBuilder.Redirect initRedirect(FileDescriptor fileDescriptor);

    static native void deregisterSelectionKey(AbstractSelectableChannel abstractSelectableChannel, SelectionKey selectionKey);

    static native byte[] tipcGetNodeId(int i) throws IOException;

    static native byte[] tipcGetLinkName(int i, int i2) throws IOException;

    static native int sockAddrNativeDataOffset();

    static native int sockAddrNativeFamilyOffset();

    static native int sockTypeToNative(int i) throws IOException;

    static native int vsockGetLocalCID() throws IOException;

    static native int systemResolveCtlId(FileDescriptor fileDescriptor, String str) throws IOException;

    static {
        NativeLibraryLoader nll;
        initError = null;
        try {
            try {
                nll = new NativeLibraryLoader();
            } catch (Error | RuntimeException e) {
                initError = e;
                StackTraceUtil.printStackTraceSevere(e);
                setLoaded(false);
            }
            try {
                nll.loadLibrary();
                nll.close();
                setLoaded(true);
                AFAddressFamily.registerAddressFamily("generic", -1, "org.newsclub.net.unix.AFGenericSocketAddress");
                AFAddressFamily.registerAddressFamily("un", 1, "org.newsclub.net.unix.AFUNIXSocketAddress");
                AFAddressFamily.registerAddressFamily("tipc", 30, "org.newsclub.net.unix.AFTIPCSocketAddress");
                AFAddressFamily.registerAddressFamily("vsock", 40, "org.newsclub.net.unix.AFVSOCKSocketAddress");
                AFAddressFamily.registerAddressFamily("system", DOMAIN_SYSTEM, "org.newsclub.net.unix.AFSYSTEMSocketAddress");
            } catch (Throwable th) {
                try {
                    nll.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (Throwable th3) {
            setLoaded(false);
            throw th3;
        }
    }

    @ExcludeFromCodeCoverageGeneratedReport(reason = "unreachable")
    private NativeUnixSocket() {
        throw new UnsupportedOperationException("No instances");
    }

    static boolean isLoaded() {
        return LOADED.get();
    }

    static void ensureSupported() throws UnsupportedOperationException {
        if (!isLoaded()) {
            throw unsupportedException();
        }
    }

    static UnsupportedOperationException unsupportedException() {
        if (!isLoaded()) {
            return (UnsupportedOperationException) new UnsupportedOperationException("junixsocket may not be fully supported on this platform").initCause(initError);
        }
        return null;
    }

    static Throwable retrieveInitError() {
        return initError;
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

    private static void tryResolveClass(String className) {
        try {
            Class.forName(className);
        } catch (Exception e) {
        }
    }

    @SuppressFBWarnings({"THROWS_METHOD_THROWS_RUNTIMEEXCEPTION"})
    static void setPort1(SocketAddress addr, int port) throws SocketException {
        if (port < 0) {
            throw new IllegalArgumentException("port out of range:" + port);
        }
        try {
            setPort(addr, port);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            throw ((SocketException) new SocketException("Could not set port").initCause(e2));
        }
    }

    static void setLoaded(boolean successful) {
        LOADED.compareAndSet(false, successful);
    }
}
