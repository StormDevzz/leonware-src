/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.kohlschutter.annotations.compiletime.SuppressFBWarnings
 *  org.eclipse.jdt.annotation.NonNull
 */
package org.newsclub.net.unix;

import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.eclipse.jdt.annotation.NonNull;
import org.newsclub.net.unix.AFAddressFamily;
import org.newsclub.net.unix.AFAddressFamilyConfig;
import org.newsclub.net.unix.AFDatagramChannel;
import org.newsclub.net.unix.AFDatagramSocket;
import org.newsclub.net.unix.AFFunction;
import org.newsclub.net.unix.AFGenericDatagramSocket;
import org.newsclub.net.unix.AFGenericServerSocket;
import org.newsclub.net.unix.AFGenericServerSocketChannel;
import org.newsclub.net.unix.AFGenericSocket;
import org.newsclub.net.unix.AFGenericSocketChannel;
import org.newsclub.net.unix.AFSYSTEMSocketAddress;
import org.newsclub.net.unix.AFServerSocket;
import org.newsclub.net.unix.AFSocket;
import org.newsclub.net.unix.AFSocketAddress;
import org.newsclub.net.unix.AFSocketCapability;
import org.newsclub.net.unix.AFSomeSocketChannel;
import org.newsclub.net.unix.AFTIPCSocketAddress;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import org.newsclub.net.unix.AFVSOCKSocketAddress;
import org.newsclub.net.unix.FileDescriptorAccess;
import org.newsclub.net.unix.NativeUnixSocket;
import org.newsclub.net.unix.OperationNotSupportedSocketException;
import org.newsclub.net.unix.RAFChannelProvider;
import org.newsclub.net.unix.Unsafe;

public final class FileDescriptorCast
implements FileDescriptorAccess {
    private static final Map<Class<?>, CastingProviderMap> PRIMARY_TYPE_PROVIDERS_MAP = Collections.synchronizedMap(new HashMap());
    private static final AFFunction<FileDescriptor, FileInputStream> FD_IS_PROVIDER = System.getProperty("osv.version") != null ? x$0 -> new LenientFileInputStream((FileDescriptor)x$0) : FileInputStream::new;
    private static final CastingProviderMap GLOBAL_PROVIDERS_FINAL = new CastingProviderMap(){

        @Override
        protected void addProviders() {
            this.addProvider(FileDescriptor.class, new CastingProvider<FileDescriptor>(){

                @Override
                public FileDescriptor provideAs(FileDescriptorCast fdc, Class<? super FileDescriptor> desiredType) throws IOException {
                    return fdc.getFileDescriptor();
                }
            });
        }
    };
    private static final CastingProviderMap GLOBAL_PROVIDERS = new CastingProviderMap(){

        @Override
        protected void addProviders() {
            this.addProvider(WritableByteChannel.class, new CastingProvider<WritableByteChannel>(){

                @Override
                public WritableByteChannel provideAs(FileDescriptorCast fdc, Class<? super WritableByteChannel> desiredType) throws IOException {
                    return new FileOutputStream(fdc.getFileDescriptor()).getChannel();
                }
            });
            this.addProvider(ReadableByteChannel.class, new CastingProvider<ReadableByteChannel>(){

                @Override
                public ReadableByteChannel provideAs(FileDescriptorCast fdc, Class<? super ReadableByteChannel> desiredType) throws IOException {
                    return ((FileInputStream)FD_IS_PROVIDER.apply(fdc.getFileDescriptor())).getChannel();
                }
            });
            this.addProvider(FileChannel.class, new CastingProvider<FileChannel>(){

                @Override
                public FileChannel provideAs(FileDescriptorCast fdc, Class<? super FileChannel> desiredType) throws IOException {
                    return RAFChannelProvider.getFileChannel(fdc.getFileDescriptor());
                }
            });
            this.addProvider(FileOutputStream.class, new CastingProvider<FileOutputStream>(){

                @Override
                public FileOutputStream provideAs(FileDescriptorCast fdc, Class<? super FileOutputStream> desiredType) throws IOException {
                    return new FileOutputStream(fdc.getFileDescriptor());
                }
            });
            this.addProvider(FileInputStream.class, new CastingProvider<FileInputStream>(){

                @Override
                public FileInputStream provideAs(FileDescriptorCast fdc, Class<? super FileInputStream> desiredType) throws IOException {
                    return (FileInputStream)FD_IS_PROVIDER.apply(fdc.getFileDescriptor());
                }
            });
            this.addProvider(FileDescriptor.class, new CastingProvider<FileDescriptor>(){

                @Override
                public FileDescriptor provideAs(FileDescriptorCast fdc, Class<? super FileDescriptor> desiredType) throws IOException {
                    return fdc.getFileDescriptor();
                }
            });
            this.addProvider(Integer.class, new CastingProvider<Integer>(){

                @Override
                public Integer provideAs(FileDescriptorCast fdc, Class<? super Integer> desiredType) throws IOException {
                    int val;
                    FileDescriptor fd = fdc.getFileDescriptor();
                    int n = val = fd.valid() ? NativeUnixSocket.getFD(fd) : -1;
                    if (val == -1) {
                        throw new IOException("Not a valid file descriptor");
                    }
                    return val;
                }
            });
            if (AFSocket.supports(AFSocketCapability.CAPABILITY_FD_AS_REDIRECT)) {
                this.addProvider(ProcessBuilder.Redirect.class, new CastingProvider<ProcessBuilder.Redirect>(){

                    @Override
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
    private static final int FD_IN = FileDescriptorCast.getFdIfPossible(FileDescriptor.in);
    private static final int FD_OUT = FileDescriptorCast.getFdIfPossible(FileDescriptor.out);
    private static final int FD_ERR = FileDescriptorCast.getFdIfPossible(FileDescriptor.err);
    private final FileDescriptor fdObj;
    private int localPort = 0;
    private int remotePort = 0;
    private final CastingProviderMap cpm;

    private FileDescriptorCast(FileDescriptor fdObj, CastingProviderMap cpm) {
        this.fdObj = Objects.requireNonNull(fdObj);
        this.cpm = Objects.requireNonNull(cpm);
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
        }
        catch (IOException e) {
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

    static <A extends AFSocketAddress> void registerCastingProviders(AFAddressFamilyConfig<A> config) {
        Class<AFSocket<A>> socketClass = config.socketClass();
        Class<AFDatagramSocket<A>> datagramSocketClass = config.datagramSocketClass();
        FileDescriptorCast.registerCastingProviders(socketClass, new CastingProviderMap(){

            @Override
            protected void addProviders() {
                this.addProviders(GLOBAL_PROVIDERS);
                CastingProviderSocketOrChannel<AFSocket> cpSocketOrChannel = (fdc, desiredType, isChannel) -> FileDescriptorCast.reconfigure(isChannel, AFSocket.newInstance(config.socketConstructor(), null, fdc.getFileDescriptor(), fdc.localPort, fdc.remotePort));
                CastingProviderSocketOrChannel<AFServerSocket> cpServerSocketOrChannel = (fdc, desiredType, isChannel) -> FileDescriptorCast.reconfigure(isChannel, AFServerSocket.newInstance(config.serverSocketConstructor(), fdc.getFileDescriptor(), fdc.localPort, fdc.remotePort));
                this.registerGenericSocketProviders();
                this.addProvider(socketClass, (fdc, desiredType) -> cpSocketOrChannel.provideAs(fdc, desiredType, false));
                this.addProvider(config.serverSocketClass(), (fdc, desiredType) -> cpServerSocketOrChannel.provideAs(fdc, desiredType, false));
                this.addProvider(config.socketChannelClass(), (fdc, desiredType) -> cpSocketOrChannel.provideAs(fdc, AFSocket.class, true).getChannel());
                this.addProvider(config.serverSocketChannelClass(), (fdc, desiredType) -> cpServerSocketOrChannel.provideAs(fdc, AFServerSocket.class, true).getChannel());
            }
        });
        FileDescriptorCast.registerCastingProviders(datagramSocketClass, new CastingProviderMap(){

            @Override
            protected void addProviders() {
                this.addProviders(GLOBAL_PROVIDERS);
                CastingProviderSocketOrChannel<AFDatagramSocket> cpDatagramSocketOrChannel = (fdc, desiredType, isChannel) -> FileDescriptorCast.reconfigure(isChannel, AFDatagramSocket.newInstance(config.datagramSocketConstructor(), fdc.getFileDescriptor(), fdc.localPort, fdc.remotePort));
                this.registerGenericDatagramSocketProviders();
                this.addProvider(datagramSocketClass, (fdc, desiredType) -> cpDatagramSocketOrChannel.provideAs(fdc, desiredType, false));
                this.addProvider(config.datagramChannelClass(), (fdc, desiredType) -> cpDatagramSocketOrChannel.provideAs(fdc, AFDatagramSocket.class, true).getChannel());
            }
        });
    }

    public static FileDescriptorCast using(FileDescriptor fdObj) throws IOException {
        Class<Object> primaryType;
        if (!fdObj.valid()) {
            throw new IOException("Not a valid file descriptor");
        }
        Class<?> clazz = primaryType = NativeUnixSocket.isLoaded() ? NativeUnixSocket.primaryType(fdObj) : null;
        if (primaryType == null) {
            primaryType = FileDescriptor.class;
        }
        FileDescriptorCast.triggerInit();
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
        return FileDescriptorCast.using(duplicate);
    }

    @Unsafe
    public static FileDescriptorCast unsafeUsing(int fd) throws IOException {
        int check;
        AFSocket.ensureUnsafeSupported();
        if (fd == -1) {
            throw new IOException("Not a valid file descriptor");
        }
        FileDescriptor fdObj = fd == FD_IN ? FileDescriptor.in : (fd == FD_OUT ? FileDescriptor.out : (fd == FD_ERR ? FileDescriptor.err : null));
        if (fdObj != null && fd == (check = FileDescriptorCast.getFdIfPossible(fdObj))) {
            return FileDescriptorCast.using(fdObj);
        }
        fdObj = new FileDescriptor();
        NativeUnixSocket.initFD(fdObj, fd);
        return FileDescriptorCast.using(fdObj);
    }

    private static void triggerInit() {
        for (AFAddressFamily family : new AFAddressFamily[]{AFUNIXSocketAddress.addressFamily(), AFTIPCSocketAddress.addressFamily(), AFVSOCKSocketAddress.addressFamily(), AFSYSTEMSocketAddress.addressFamily()}) {
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

    public <K> @NonNull K as(Class<K> desiredType) throws IOException {
        Objects.requireNonNull(desiredType);
        CastingProvider<K> provider = this.cpm.get(desiredType);
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

    @Override
    @SuppressFBWarnings(value={"EI_EXPOSE_REP"})
    public FileDescriptor getFileDescriptor() {
        return this.fdObj;
    }

    private static void registerGenericSocketSupport() {
        FileDescriptorCast.registerCastingProviders(Socket.class, new CastingProviderMap(){

            @Override
            protected void addProviders() {
                this.addProviders(GLOBAL_PROVIDERS);
                this.registerGenericSocketProviders();
            }
        });
        FileDescriptorCast.registerCastingProviders(DatagramSocket.class, new CastingProviderMap(){

            @Override
            protected void addProviders() {
                this.addProviders(GLOBAL_PROVIDERS);
                this.registerGenericDatagramSocketProviders();
            }
        });
    }

    private static <S extends AFSocket<?>> S reconfigure(boolean isChannel, S socket) throws IOException {
        FileDescriptorCast.reconfigure(isChannel, socket.getChannel());
        socket.getAFImpl().getCore().disableCleanFd();
        return socket;
    }

    private static <S extends AFServerSocket<?>> S reconfigure(boolean isChannel, S socket) throws IOException {
        FileDescriptorCast.reconfigure(isChannel, socket.getChannel());
        socket.getAFImpl().getCore().disableCleanFd();
        return socket;
    }

    private static <S extends AFDatagramSocket<?>> S reconfigure(boolean isChannel, S socket) throws IOException {
        FileDescriptorCast.reconfigure(isChannel, socket.getChannel());
        socket.getAFImpl().getCore().disableCleanFd();
        return socket;
    }

    private static <S extends AFSomeSocketChannel> void reconfigure(boolean isChannel, S socketChannel) throws IOException {
        if (isChannel) {
            FileDescriptorCast.reconfigureKeepBlockingState(socketChannel);
        } else {
            FileDescriptorCast.reconfigureSetBlocking(socketChannel);
        }
    }

    private static <S extends AFSomeSocketChannel> void reconfigureKeepBlockingState(S socketChannel) throws IOException {
        boolean blocking;
        int result = NativeUnixSocket.checkBlocking(socketChannel.getFileDescriptor());
        switch (result) {
            case 0: {
                blocking = false;
                break;
            }
            case 1: {
                blocking = true;
                break;
            }
            case 2: {
                socketChannel.configureBlocking(false);
                socketChannel.configureBlocking(true);
                return;
            }
            default: {
                throw new OperationNotSupportedSocketException("Invalid blocking state");
            }
        }
        socketChannel.configureBlocking(blocking);
    }

    private static <S extends AFSomeSocketChannel> void reconfigureSetBlocking(S socketChannel) throws IOException {
        int result = NativeUnixSocket.checkBlocking(socketChannel.getFileDescriptor());
        switch (result) {
            case 0: {
                break;
            }
            case 1: {
                return;
            }
            case 2: {
                break;
            }
            default: {
                throw new OperationNotSupportedSocketException("Invalid blocking state");
            }
        }
        socketChannel.configureBlocking(false);
        socketChannel.configureBlocking(true);
    }

    static {
        FileDescriptorCast.registerGenericSocketSupport();
    }

    private static abstract class CastingProviderMap {
        private final Map<Class<?>, CastingProvider<?>> providers = new HashMap();
        private final Set<Class<?>> classes = Collections.unmodifiableSet(this.providers.keySet());

        protected CastingProviderMap() {
            this.addProviders();
            this.addProviders(GLOBAL_PROVIDERS_FINAL);
        }

        protected void registerGenericSocketProviders() {
            CastingProviderSocketOrChannel<AFSocket> cpSocketOrChannelGeneric = (fdc, desiredType, isChannel) -> FileDescriptorCast.reconfigure(isChannel, AFSocket.newInstance(AFGenericSocket::new, null, fdc.getFileDescriptor(), fdc.localPort, fdc.remotePort));
            CastingProviderSocketOrChannel<AFServerSocket> cpServerSocketOrChannelGeneric = (fdc, desiredType, isChannel) -> FileDescriptorCast.reconfigure(isChannel, AFServerSocket.newInstance(AFGenericServerSocket::new, fdc.getFileDescriptor(), fdc.localPort, fdc.remotePort));
            this.addProvider(AFGenericSocket.class, (fdc, desiredType) -> cpSocketOrChannelGeneric.provideAs(fdc, desiredType, false));
            this.addProvider(AFGenericServerSocket.class, (fdc, desiredType) -> cpServerSocketOrChannelGeneric.provideAs(fdc, desiredType, false));
            this.addProvider(AFGenericSocketChannel.class, (fdc, desiredType) -> cpSocketOrChannelGeneric.provideAs(fdc, AFSocket.class, true).getChannel());
            this.addProvider(AFGenericServerSocketChannel.class, (fdc, desiredType) -> cpServerSocketOrChannelGeneric.provideAs(fdc, AFServerSocket.class, true).getChannel());
        }

        protected void registerGenericDatagramSocketProviders() {
            CastingProviderSocketOrChannel<AFDatagramSocket> cpDatagramSocketOrChannelGeneric = (fdc, desiredType, isChannel) -> FileDescriptorCast.reconfigure(isChannel, AFDatagramSocket.newInstance(AFGenericDatagramSocket::new, fdc.getFileDescriptor(), fdc.localPort, fdc.remotePort));
            this.addProvider(AFDatagramSocket.class, (fdc, desiredType) -> cpDatagramSocketOrChannelGeneric.provideAs(fdc, desiredType, false));
            this.addProvider(AFDatagramChannel.class, (fdc, desiredType) -> cpDatagramSocketOrChannelGeneric.provideAs(fdc, AFDatagramSocket.class, true).getChannel());
        }

        protected abstract void addProviders();

        protected final <T> void addProvider(Class<T> type, CastingProvider<?> cp) {
            Objects.requireNonNull(type);
            this.addProvider0(type, cp);
        }

        private void addProvider0(Class<?> type, CastingProvider<?> cp) {
            if (this.providers.put(type, cp) != cp) {
                for (Class<?> cl : type.getInterfaces()) {
                    this.addProvider0(cl, cp);
                }
                Class<?> scl = type.getSuperclass();
                if (scl != null) {
                    this.addProvider0(scl, cp);
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
            return this.providers.get(desiredType);
        }
    }

    @FunctionalInterface
    private static interface CastingProvider<T> {
        public T provideAs(FileDescriptorCast var1, Class<? super T> var2) throws IOException;
    }

    private static final class LenientFileInputStream
    extends FileInputStream {
        private LenientFileInputStream(FileDescriptor fdObj) {
            super(fdObj);
        }

        @Override
        public int available() throws IOException {
            try {
                return super.available();
            }
            catch (IOException e) {
                String msg = e.getMessage();
                if ("Invalid seek".equals(msg)) {
                    return 0;
                }
                throw e;
            }
        }
    }

    @FunctionalInterface
    private static interface CastingProviderSocketOrChannel<T> {
        public T provideAs(FileDescriptorCast var1, Class<? super T> var2, boolean var3) throws IOException;
    }
}

