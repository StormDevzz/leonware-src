package org.newsclub.net.unix;

import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ProcessBuilder;
import java.net.DatagramSocket;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/FileDescriptorCast.class */
public final class FileDescriptorCast implements FileDescriptorAccess {
    private static final Map<Class<?>, CastingProviderMap> PRIMARY_TYPE_PROVIDERS_MAP = Collections.synchronizedMap(new HashMap());
    private static final AFFunction<FileDescriptor, FileInputStream> FD_IS_PROVIDER;
    private static final CastingProviderMap GLOBAL_PROVIDERS_FINAL;
    private static final CastingProviderMap GLOBAL_PROVIDERS;
    private static final int FD_IN;
    private static final int FD_OUT;
    private static final int FD_ERR;
    private final FileDescriptor fdObj;
    private int localPort = 0;
    private int remotePort = 0;
    private final CastingProviderMap cpm;

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/FileDescriptorCast$CastingProvider.class */
    @FunctionalInterface
    private interface CastingProvider<T> {
        T provideAs(FileDescriptorCast fileDescriptorCast, Class<? super T> cls) throws IOException;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/FileDescriptorCast$CastingProviderSocketOrChannel.class */
    @FunctionalInterface
    private interface CastingProviderSocketOrChannel<T> {
        T provideAs(FileDescriptorCast fileDescriptorCast, Class<? super T> cls, boolean z) throws IOException;
    }

    static {
        FD_IS_PROVIDER = System.getProperty("osv.version") != null ? x$0 -> {
            return new LenientFileInputStream(x$0);
        } : FileInputStream::new;
        GLOBAL_PROVIDERS_FINAL = new CastingProviderMap() { // from class: org.newsclub.net.unix.FileDescriptorCast.1
            @Override // org.newsclub.net.unix.FileDescriptorCast.CastingProviderMap
            protected void addProviders() {
                addProvider(FileDescriptor.class, new CastingProvider<FileDescriptor>() { // from class: org.newsclub.net.unix.FileDescriptorCast.1.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // org.newsclub.net.unix.FileDescriptorCast.CastingProvider
                    public FileDescriptor provideAs(FileDescriptorCast fdc, Class<? super FileDescriptor> desiredType) throws IOException {
                        return fdc.getFileDescriptor();
                    }
                });
            }
        };
        GLOBAL_PROVIDERS = new CastingProviderMap() { // from class: org.newsclub.net.unix.FileDescriptorCast.2
            @Override // org.newsclub.net.unix.FileDescriptorCast.CastingProviderMap
            protected void addProviders() {
                addProvider(WritableByteChannel.class, new CastingProvider<WritableByteChannel>() { // from class: org.newsclub.net.unix.FileDescriptorCast.2.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // org.newsclub.net.unix.FileDescriptorCast.CastingProvider
                    public WritableByteChannel provideAs(FileDescriptorCast fdc, Class<? super WritableByteChannel> desiredType) throws IOException {
                        return new FileOutputStream(fdc.getFileDescriptor()).getChannel();
                    }
                });
                addProvider(ReadableByteChannel.class, new CastingProvider<ReadableByteChannel>() { // from class: org.newsclub.net.unix.FileDescriptorCast.2.2
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // org.newsclub.net.unix.FileDescriptorCast.CastingProvider
                    public ReadableByteChannel provideAs(FileDescriptorCast fdc, Class<? super ReadableByteChannel> desiredType) throws IOException {
                        return ((FileInputStream) FileDescriptorCast.FD_IS_PROVIDER.apply(fdc.getFileDescriptor())).getChannel();
                    }
                });
                addProvider(FileChannel.class, new CastingProvider<FileChannel>() { // from class: org.newsclub.net.unix.FileDescriptorCast.2.3
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // org.newsclub.net.unix.FileDescriptorCast.CastingProvider
                    public FileChannel provideAs(FileDescriptorCast fdc, Class<? super FileChannel> desiredType) throws IOException {
                        return RAFChannelProvider.getFileChannel(fdc.getFileDescriptor());
                    }
                });
                addProvider(FileOutputStream.class, new CastingProvider<FileOutputStream>() { // from class: org.newsclub.net.unix.FileDescriptorCast.2.4
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // org.newsclub.net.unix.FileDescriptorCast.CastingProvider
                    public FileOutputStream provideAs(FileDescriptorCast fdc, Class<? super FileOutputStream> desiredType) throws IOException {
                        return new FileOutputStream(fdc.getFileDescriptor());
                    }
                });
                addProvider(FileInputStream.class, new CastingProvider<FileInputStream>() { // from class: org.newsclub.net.unix.FileDescriptorCast.2.5
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // org.newsclub.net.unix.FileDescriptorCast.CastingProvider
                    public FileInputStream provideAs(FileDescriptorCast fdc, Class<? super FileInputStream> desiredType) throws IOException {
                        return (FileInputStream) FileDescriptorCast.FD_IS_PROVIDER.apply(fdc.getFileDescriptor());
                    }
                });
                addProvider(FileDescriptor.class, new CastingProvider<FileDescriptor>() { // from class: org.newsclub.net.unix.FileDescriptorCast.2.6
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // org.newsclub.net.unix.FileDescriptorCast.CastingProvider
                    public FileDescriptor provideAs(FileDescriptorCast fdc, Class<? super FileDescriptor> desiredType) throws IOException {
                        return fdc.getFileDescriptor();
                    }
                });
                addProvider(Integer.class, new CastingProvider<Integer>() { // from class: org.newsclub.net.unix.FileDescriptorCast.2.7
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // org.newsclub.net.unix.FileDescriptorCast.CastingProvider
                    public Integer provideAs(FileDescriptorCast fdc, Class<? super Integer> desiredType) throws IOException {
                        FileDescriptor fd = fdc.getFileDescriptor();
                        int val = fd.valid() ? NativeUnixSocket.getFD(fd) : -1;
                        if (val == -1) {
                            throw new IOException("Not a valid file descriptor");
                        }
                        return Integer.valueOf(val);
                    }
                });
                if (AFSocket.supports(AFSocketCapability.CAPABILITY_FD_AS_REDIRECT)) {
                    addProvider(ProcessBuilder.Redirect.class, new CastingProvider<ProcessBuilder.Redirect>() { // from class: org.newsclub.net.unix.FileDescriptorCast.2.8
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // org.newsclub.net.unix.FileDescriptorCast.CastingProvider
                        public ProcessBuilder.Redirect provideAs(FileDescriptorCast fdc, Class<? super ProcessBuilder.Redirect> desiredType) throws IOException {
                            ProcessBuilder.Redirect red = NativeUnixSocket.initRedirect(fdc.getFileDescriptor());
                            if (red == null) {
                                throw new ClassCastException("Cannot access file descriptor as " + desiredType);
                            }
                            return red;
                        }
                    });
                }
            }
        };
        FD_IN = getFdIfPossible(FileDescriptor.in);
        FD_OUT = getFdIfPossible(FileDescriptor.out);
        FD_ERR = getFdIfPossible(FileDescriptor.err);
        registerGenericSocketSupport();
    }

    private FileDescriptorCast(FileDescriptor fdObj, CastingProviderMap cpm) {
        this.fdObj = (FileDescriptor) Objects.requireNonNull(fdObj);
        this.cpm = (CastingProviderMap) Objects.requireNonNull(cpm);
    }

    private static int getFdIfPossible(FileDescriptor fd) {
        if (!NativeUnixSocket.isLoaded()) {
            return -1;
        }
        try {
            if (!fd.valid()) {
                return -1;
            }
            return NativeUnixSocket.getFD(fd);
        } catch (IOException e) {
            return -1;
        }
    }

    private static void registerCastingProviders(Class<?> primaryType, CastingProviderMap cpm) {
        Objects.requireNonNull(primaryType);
        CastingProviderMap prev = PRIMARY_TYPE_PROVIDERS_MAP.put(primaryType, cpm);
        if (prev != null) {
            PRIMARY_TYPE_PROVIDERS_MAP.put(primaryType, prev);
            throw new IllegalStateException("Already registered: " + primaryType);
        }
    }

    static <A extends AFSocketAddress> void registerCastingProviders(final AFAddressFamilyConfig<A> config) {
        final Class<? extends AFSocket<A>> socketClass = config.socketClass();
        final Class<? extends AFDatagramSocket<A>> datagramSocketClass = config.datagramSocketClass();
        registerCastingProviders(socketClass, new CastingProviderMap() { // from class: org.newsclub.net.unix.FileDescriptorCast.3
            @Override // org.newsclub.net.unix.FileDescriptorCast.CastingProviderMap
            protected void addProviders() {
                addProviders(FileDescriptorCast.GLOBAL_PROVIDERS);
                AFAddressFamilyConfig aFAddressFamilyConfig = config;
                CastingProviderSocketOrChannel castingProviderSocketOrChannel = (fdc, desiredType, isChannel) -> {
                    return FileDescriptorCast.reconfigure(isChannel, AFSocket.newInstance(aFAddressFamilyConfig.socketConstructor(), (AFSocketFactory) null, fdc.getFileDescriptor(), fdc.localPort, fdc.remotePort));
                };
                AFAddressFamilyConfig aFAddressFamilyConfig2 = config;
                CastingProviderSocketOrChannel castingProviderSocketOrChannel2 = (fdc2, desiredType2, isChannel2) -> {
                    return FileDescriptorCast.reconfigure(isChannel2, AFServerSocket.newInstance(aFAddressFamilyConfig2.serverSocketConstructor(), fdc2.getFileDescriptor(), fdc2.localPort, fdc2.remotePort));
                };
                registerGenericSocketProviders();
                addProvider(socketClass, (fdc3, desiredType3) -> {
                    return castingProviderSocketOrChannel.provideAs(fdc3, desiredType3, false);
                });
                addProvider(config.serverSocketClass(), (fdc4, desiredType4) -> {
                    return castingProviderSocketOrChannel2.provideAs(fdc4, desiredType4, false);
                });
                addProvider(config.socketChannelClass(), (fdc5, desiredType5) -> {
                    return ((AFSocket) castingProviderSocketOrChannel.provideAs(fdc5, AFSocket.class, true)).getChannel();
                });
                addProvider(config.serverSocketChannelClass(), (fdc6, desiredType6) -> {
                    return ((AFServerSocket) castingProviderSocketOrChannel2.provideAs(fdc6, AFServerSocket.class, true)).getChannel();
                });
            }
        });
        registerCastingProviders(datagramSocketClass, new CastingProviderMap() { // from class: org.newsclub.net.unix.FileDescriptorCast.4
            @Override // org.newsclub.net.unix.FileDescriptorCast.CastingProviderMap
            protected void addProviders() {
                addProviders(FileDescriptorCast.GLOBAL_PROVIDERS);
                AFAddressFamilyConfig aFAddressFamilyConfig = config;
                CastingProviderSocketOrChannel castingProviderSocketOrChannel = (fdc, desiredType, isChannel) -> {
                    return FileDescriptorCast.reconfigure(isChannel, AFDatagramSocket.newInstance(aFAddressFamilyConfig.datagramSocketConstructor(), fdc.getFileDescriptor(), fdc.localPort, fdc.remotePort));
                };
                registerGenericDatagramSocketProviders();
                addProvider(datagramSocketClass, (fdc2, desiredType2) -> {
                    return castingProviderSocketOrChannel.provideAs(fdc2, desiredType2, false);
                });
                addProvider(config.datagramChannelClass(), (fdc3, desiredType3) -> {
                    return ((AFDatagramSocket) castingProviderSocketOrChannel.provideAs(fdc3, AFDatagramSocket.class, true)).getChannel();
                });
            }
        });
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/FileDescriptorCast$CastingProviderMap.class */
    private static abstract class CastingProviderMap {
        private final Map<Class<?>, CastingProvider<?>> providers = new HashMap();
        private final Set<Class<?>> classes = Collections.unmodifiableSet(this.providers.keySet());

        protected abstract void addProviders();

        protected CastingProviderMap() {
            addProviders();
            addProviders(FileDescriptorCast.GLOBAL_PROVIDERS_FINAL);
        }

        protected void registerGenericSocketProviders() {
            CastingProviderSocketOrChannel<AFSocket<AFGenericSocketAddress>> cpSocketOrChannelGeneric = (fdc, desiredType, isChannel) -> {
                return FileDescriptorCast.reconfigure(isChannel, AFSocket.newInstance(AFGenericSocket::new, (AFSocketFactory) null, fdc.getFileDescriptor(), fdc.localPort, fdc.remotePort));
            };
            CastingProviderSocketOrChannel<AFServerSocket<AFGenericSocketAddress>> cpServerSocketOrChannelGeneric = (fdc2, desiredType2, isChannel2) -> {
                return FileDescriptorCast.reconfigure(isChannel2, AFServerSocket.newInstance(AFGenericServerSocket::new, fdc2.getFileDescriptor(), fdc2.localPort, fdc2.remotePort));
            };
            addProvider(AFGenericSocket.class, (fdc3, desiredType3) -> {
                return cpSocketOrChannelGeneric.provideAs(fdc3, desiredType3, false);
            });
            addProvider(AFGenericServerSocket.class, (fdc4, desiredType4) -> {
                return cpServerSocketOrChannelGeneric.provideAs(fdc4, desiredType4, false);
            });
            addProvider(AFGenericSocketChannel.class, (fdc5, desiredType5) -> {
                return ((AFSocket) cpSocketOrChannelGeneric.provideAs(fdc5, AFSocket.class, true)).getChannel();
            });
            addProvider(AFGenericServerSocketChannel.class, (fdc6, desiredType6) -> {
                return ((AFServerSocket) cpServerSocketOrChannelGeneric.provideAs(fdc6, AFServerSocket.class, true)).getChannel();
            });
        }

        protected void registerGenericDatagramSocketProviders() {
            CastingProviderSocketOrChannel<AFDatagramSocket<AFGenericSocketAddress>> cpDatagramSocketOrChannelGeneric = (fdc, desiredType, isChannel) -> {
                return FileDescriptorCast.reconfigure(isChannel, AFDatagramSocket.newInstance(AFGenericDatagramSocket::new, fdc.getFileDescriptor(), fdc.localPort, fdc.remotePort));
            };
            addProvider(AFDatagramSocket.class, (fdc2, desiredType2) -> {
                return cpDatagramSocketOrChannelGeneric.provideAs(fdc2, desiredType2, false);
            });
            addProvider(AFDatagramChannel.class, (fdc3, desiredType3) -> {
                return ((AFDatagramSocket) cpDatagramSocketOrChannelGeneric.provideAs(fdc3, AFDatagramSocket.class, true)).getChannel();
            });
        }

        protected final <T> void addProvider(Class<T> type, CastingProvider<?> cp) {
            Objects.requireNonNull(type);
            addProvider0(type, cp);
        }

        private void addProvider0(Class<?> type, CastingProvider<?> cp) {
            if (this.providers.put(type, cp) != cp) {
                for (Class<?> cl : type.getInterfaces()) {
                    addProvider0(cl, cp);
                }
                Class<?> scl = type.getSuperclass();
                if (scl != null) {
                    addProvider0(scl, cp);
                }
            }
        }

        protected final void addProviders(CastingProviderMap other) {
            if (other == null || other == this) {
                return;
            }
            this.providers.putAll(other.providers);
        }

        public <T> CastingProvider<? extends T> get(Class<T> desiredType) {
            return (CastingProvider) this.providers.get(desiredType);
        }
    }

    public static FileDescriptorCast using(FileDescriptor fdObj) throws IOException {
        if (!fdObj.valid()) {
            throw new IOException("Not a valid file descriptor");
        }
        Class<?> primaryType = NativeUnixSocket.isLoaded() ? NativeUnixSocket.primaryType(fdObj) : null;
        if (primaryType == null) {
            primaryType = FileDescriptor.class;
        }
        triggerInit();
        CastingProviderMap map = PRIMARY_TYPE_PROVIDERS_MAP.get(primaryType);
        return new FileDescriptorCast(fdObj, map == null ? GLOBAL_PROVIDERS : map);
    }

    public static FileDescriptorCast duplicating(FileDescriptor fdObj) throws IOException {
        if (!fdObj.valid()) {
            throw new IOException("Not a valid file descriptor");
        }
        FileDescriptor duplicate = NativeUnixSocket.duplicate(fdObj, new FileDescriptor());
        if (duplicate == null) {
            throw new IOException("Could not duplicate file descriptor");
        }
        return using(duplicate);
    }

    @Unsafe
    public static FileDescriptorCast unsafeUsing(int fd) throws IOException {
        FileDescriptor fdObj;
        AFSocket.ensureUnsafeSupported();
        if (fd == -1) {
            throw new IOException("Not a valid file descriptor");
        }
        if (fd == FD_IN) {
            fdObj = FileDescriptor.in;
        } else if (fd == FD_OUT) {
            fdObj = FileDescriptor.out;
        } else if (fd == FD_ERR) {
            fdObj = FileDescriptor.err;
        } else {
            fdObj = null;
        }
        if (fdObj != null) {
            int check = getFdIfPossible(fdObj);
            if (fd == check) {
                return using(fdObj);
            }
        }
        FileDescriptor fdObj2 = new FileDescriptor();
        NativeUnixSocket.initFD(fdObj2, fd);
        return using(fdObj2);
    }

    private static void triggerInit() {
        for (AFAddressFamily<?> family : new AFAddressFamily[]{AFUNIXSocketAddress.addressFamily(), AFTIPCSocketAddress.addressFamily(), AFVSOCKSocketAddress.addressFamily(), AFSYSTEMSocketAddress.addressFamily()}) {
            Objects.requireNonNull(family.getClass());
        }
    }

    public FileDescriptorCast withLocalPort(int port) {
        if (port < 0) {
            throw new IllegalArgumentException();
        }
        this.localPort = port;
        return this;
    }

    public FileDescriptorCast withRemotePort(int port) {
        if (port < 0) {
            throw new IllegalArgumentException();
        }
        this.remotePort = port;
        return this;
    }

    public <K> K as(Class<K> desiredType) throws IOException {
        Objects.requireNonNull(desiredType);
        CastingProvider<? extends K> provider = this.cpm.get(desiredType);
        if (provider != null) {
            K obj = desiredType.cast(provider.provideAs(this, desiredType));
            Objects.requireNonNull(obj);
            return obj;
        }
        throw new ClassCastException("Cannot access file descriptor as " + desiredType);
    }

    public boolean isAvailable(Class<?> desiredType) throws IOException {
        return this.cpm.providers.containsKey(desiredType);
    }

    public Set<Class<?>> availableTypes() {
        return this.cpm.classes;
    }

    @Override // org.newsclub.net.unix.FileDescriptorAccess
    @SuppressFBWarnings({"EI_EXPOSE_REP"})
    public FileDescriptor getFileDescriptor() {
        return this.fdObj;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/FileDescriptorCast$LenientFileInputStream.class */
    private static final class LenientFileInputStream extends FileInputStream {
        private LenientFileInputStream(FileDescriptor fdObj) {
            super(fdObj);
        }

        @Override // java.io.FileInputStream, java.io.InputStream
        public int available() throws IOException {
            try {
                return super.available();
            } catch (IOException e) {
                String msg = e.getMessage();
                if ("Invalid seek".equals(msg)) {
                    return 0;
                }
                throw e;
            }
        }
    }

    private static void registerGenericSocketSupport() {
        registerCastingProviders(Socket.class, new CastingProviderMap() { // from class: org.newsclub.net.unix.FileDescriptorCast.5
            @Override // org.newsclub.net.unix.FileDescriptorCast.CastingProviderMap
            protected void addProviders() {
                addProviders(FileDescriptorCast.GLOBAL_PROVIDERS);
                registerGenericSocketProviders();
            }
        });
        registerCastingProviders(DatagramSocket.class, new CastingProviderMap() { // from class: org.newsclub.net.unix.FileDescriptorCast.6
            @Override // org.newsclub.net.unix.FileDescriptorCast.CastingProviderMap
            protected void addProviders() {
                addProviders(FileDescriptorCast.GLOBAL_PROVIDERS);
                registerGenericDatagramSocketProviders();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <S extends AFSocket<?>> S reconfigure(boolean isChannel, S socket) throws IOException {
        reconfigure(isChannel, socket.getChannel());
        socket.getAFImpl().getCore().disableCleanFd();
        return socket;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <S extends AFServerSocket<?>> S reconfigure(boolean isChannel, S socket) throws IOException {
        reconfigure(isChannel, socket.getChannel());
        socket.getAFImpl().getCore().disableCleanFd();
        return socket;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <S extends AFDatagramSocket<?>> S reconfigure(boolean isChannel, S socket) throws IOException {
        reconfigure(isChannel, socket.getChannel());
        socket.getAFImpl().getCore().disableCleanFd();
        return socket;
    }

    private static <S extends AFSomeSocketChannel> void reconfigure(boolean isChannel, S socketChannel) throws IOException {
        if (isChannel) {
            reconfigureKeepBlockingState(socketChannel);
        } else {
            reconfigureSetBlocking(socketChannel);
        }
    }

    private static <S extends AFSomeSocketChannel> void reconfigureKeepBlockingState(S socketChannel) throws IOException {
        boolean blocking;
        int result = NativeUnixSocket.checkBlocking(socketChannel.getFileDescriptor());
        switch (result) {
            case 0:
                blocking = false;
                break;
            case 1:
                blocking = true;
                break;
            case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                socketChannel.configureBlocking(false);
                socketChannel.configureBlocking(true);
                return;
            default:
                throw new OperationNotSupportedSocketException("Invalid blocking state");
        }
        socketChannel.configureBlocking(blocking);
    }

    private static <S extends AFSomeSocketChannel> void reconfigureSetBlocking(S socketChannel) throws IOException {
        int result = NativeUnixSocket.checkBlocking(socketChannel.getFileDescriptor());
        switch (result) {
            case 0:
            case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                socketChannel.configureBlocking(false);
                socketChannel.configureBlocking(true);
                return;
            case 1:
                return;
            default:
                throw new OperationNotSupportedSocketException("Invalid blocking state");
        }
    }
}
