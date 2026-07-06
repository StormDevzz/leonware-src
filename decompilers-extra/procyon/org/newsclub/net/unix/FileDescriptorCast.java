// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.io.FileOutputStream;
import java.nio.channels.WritableByteChannel;
import java.util.Collections;
import java.util.HashMap;
import java.net.DatagramSocket;
import java.net.Socket;
import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.util.Set;
import java.io.IOException;
import java.util.Objects;
import java.io.FileInputStream;
import java.io.FileDescriptor;
import java.util.Map;

public final class FileDescriptorCast implements FileDescriptorAccess
{
    private static final Map<Class<?>, CastingProviderMap> PRIMARY_TYPE_PROVIDERS_MAP;
    private static final AFFunction<FileDescriptor, FileInputStream> FD_IS_PROVIDER;
    private static final CastingProviderMap GLOBAL_PROVIDERS_FINAL;
    private static final CastingProviderMap GLOBAL_PROVIDERS;
    private static final int FD_IN;
    private static final int FD_OUT;
    private static final int FD_ERR;
    private final FileDescriptor fdObj;
    private int localPort;
    private int remotePort;
    private final CastingProviderMap cpm;
    
    private FileDescriptorCast(final FileDescriptor fdObj, final CastingProviderMap cpm) {
        this.localPort = 0;
        this.remotePort = 0;
        this.fdObj = Objects.requireNonNull(fdObj);
        this.cpm = Objects.requireNonNull(cpm);
    }
    
    private static int getFdIfPossible(final FileDescriptor fd) {
        if (!NativeUnixSocket.isLoaded()) {
            return -1;
        }
        try {
            if (!fd.valid()) {
                return -1;
            }
            return NativeUnixSocket.getFD(fd);
        }
        catch (final IOException e) {
            return -1;
        }
    }
    
    private static void registerCastingProviders(final Class<?> primaryType, final CastingProviderMap cpm) {
        Objects.requireNonNull(primaryType);
        final CastingProviderMap prev;
        if ((prev = FileDescriptorCast.PRIMARY_TYPE_PROVIDERS_MAP.put(primaryType, cpm)) != null) {
            FileDescriptorCast.PRIMARY_TYPE_PROVIDERS_MAP.put(primaryType, prev);
            throw new IllegalStateException("Already registered: " + primaryType);
        }
    }
    
    static <A extends AFSocketAddress> void registerCastingProviders(final AFAddressFamilyConfig<A> config) {
        final Class<? extends AFSocket<A>> socketClass = config.socketClass();
        final Class<? extends AFDatagramSocket<A>> datagramSocketClass = config.datagramSocketClass();
        registerCastingProviders(socketClass, new CastingProviderMap() {
            @Override
            protected void addProviders() {
                this.addProviders(FileDescriptorCast.GLOBAL_PROVIDERS);
                final CastingProviderSocketOrChannel<AFSocket<A>> cpSocketOrChannel = (CastingProviderSocketOrChannel<AFSocket<A>>)((fdc, desiredType, isChannel) -> {
                    final Object val$config = config;
                    return reconfigure(isChannel, AFSocket.newInstance(config.socketConstructor(), null, fdc.getFileDescriptor(), fdc.localPort, fdc.remotePort));
                });
                final CastingProviderSocketOrChannel<AFServerSocket<A>> cpServerSocketOrChannel = (CastingProviderSocketOrChannel<AFServerSocket<A>>)((fdc, desiredType, isChannel) -> {
                    final Object val$config2 = config;
                    return reconfigure(isChannel, AFServerSocket.newInstance(config.serverSocketConstructor(), fdc.getFileDescriptor(), fdc.localPort, fdc.remotePort));
                });
                this.registerGenericSocketProviders();
                this.addProvider((Class<Object>)socketClass, (fdc, desiredType) -> cpSocketOrChannel.provideAs(fdc, desiredType, false));
                this.addProvider(config.serverSocketClass(), (fdc, desiredType) -> cpServerSocketOrChannel.provideAs(fdc, desiredType, false));
                this.addProvider(config.socketChannelClass(), (fdc, desiredType) -> cpSocketOrChannel.provideAs(fdc, AFSocket.class, true).getChannel());
                this.addProvider(config.serverSocketChannelClass(), (fdc, desiredType) -> cpServerSocketOrChannel.provideAs(fdc, AFServerSocket.class, true).getChannel());
            }
        });
        registerCastingProviders(datagramSocketClass, new CastingProviderMap() {
            @Override
            protected void addProviders() {
                this.addProviders(FileDescriptorCast.GLOBAL_PROVIDERS);
                final CastingProviderSocketOrChannel<AFDatagramSocket<A>> cpDatagramSocketOrChannel = (CastingProviderSocketOrChannel<AFDatagramSocket<A>>)((fdc, desiredType, isChannel) -> {
                    final Object val$config = config;
                    return reconfigure(isChannel, AFDatagramSocket.newInstance(config.datagramSocketConstructor(), fdc.getFileDescriptor(), fdc.localPort, fdc.remotePort));
                });
                this.registerGenericDatagramSocketProviders();
                this.addProvider((Class<Object>)datagramSocketClass, (fdc, desiredType) -> cpDatagramSocketOrChannel.provideAs(fdc, desiredType, false));
                this.addProvider(config.datagramChannelClass(), (fdc, desiredType) -> cpDatagramSocketOrChannel.provideAs(fdc, AFDatagramSocket.class, true).getChannel());
            }
        });
    }
    
    public static FileDescriptorCast using(final FileDescriptor fdObj) throws IOException {
        if (!fdObj.valid()) {
            throw new IOException("Not a valid file descriptor");
        }
        Class<?> primaryType = NativeUnixSocket.isLoaded() ? NativeUnixSocket.primaryType(fdObj) : null;
        if (primaryType == null) {
            primaryType = FileDescriptor.class;
        }
        triggerInit();
        final CastingProviderMap map = FileDescriptorCast.PRIMARY_TYPE_PROVIDERS_MAP.get(primaryType);
        return new FileDescriptorCast(fdObj, (map == null) ? FileDescriptorCast.GLOBAL_PROVIDERS : map);
    }
    
    public static FileDescriptorCast duplicating(final FileDescriptor fdObj) throws IOException {
        if (!fdObj.valid()) {
            throw new IOException("Not a valid file descriptor");
        }
        final FileDescriptor duplicate = NativeUnixSocket.duplicate(fdObj, new FileDescriptor());
        if (duplicate == null) {
            throw new IOException("Could not duplicate file descriptor");
        }
        return using(duplicate);
    }
    
    @Unsafe
    public static FileDescriptorCast unsafeUsing(final int fd) throws IOException {
        AFSocket.ensureUnsafeSupported();
        if (fd == -1) {
            throw new IOException("Not a valid file descriptor");
        }
        FileDescriptor fdObj;
        if (fd == FileDescriptorCast.FD_IN) {
            fdObj = FileDescriptor.in;
        }
        else if (fd == FileDescriptorCast.FD_OUT) {
            fdObj = FileDescriptor.out;
        }
        else if (fd == FileDescriptorCast.FD_ERR) {
            fdObj = FileDescriptor.err;
        }
        else {
            fdObj = null;
        }
        if (fdObj != null) {
            final int check = getFdIfPossible(fdObj);
            if (fd == check) {
                return using(fdObj);
            }
        }
        fdObj = new FileDescriptor();
        NativeUnixSocket.initFD(fdObj, fd);
        return using(fdObj);
    }
    
    private static void triggerInit() {
        for (final AFAddressFamily<?> family : new AFAddressFamily[] { AFUNIXSocketAddress.addressFamily(), AFTIPCSocketAddress.addressFamily(), AFVSOCKSocketAddress.addressFamily(), AFSYSTEMSocketAddress.addressFamily() }) {
            Objects.requireNonNull(family.getClass());
        }
    }
    
    public FileDescriptorCast withLocalPort(final int port) {
        if (port < 0) {
            throw new IllegalArgumentException();
        }
        this.localPort = port;
        return this;
    }
    
    public FileDescriptorCast withRemotePort(final int port) {
        if (port < 0) {
            throw new IllegalArgumentException();
        }
        this.remotePort = port;
        return this;
    }
    
    public <K> K as(final Class<K> desiredType) throws IOException {
        Objects.requireNonNull(desiredType);
        final CastingProvider<? extends K> provider = this.cpm.get(desiredType);
        if (provider != null) {
            final K obj = desiredType.cast(provider.provideAs(this, desiredType));
            Objects.requireNonNull(obj);
            return obj;
        }
        throw new ClassCastException("Cannot access file descriptor as " + desiredType);
    }
    
    public boolean isAvailable(final Class<?> desiredType) throws IOException {
        return this.cpm.providers.containsKey(desiredType);
    }
    
    public Set<Class<?>> availableTypes() {
        return this.cpm.classes;
    }
    
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    @Override
    public FileDescriptor getFileDescriptor() {
        return this.fdObj;
    }
    
    private static void registerGenericSocketSupport() {
        registerCastingProviders(Socket.class, new CastingProviderMap() {
            @Override
            protected void addProviders() {
                this.addProviders(FileDescriptorCast.GLOBAL_PROVIDERS);
                this.registerGenericSocketProviders();
            }
        });
        registerCastingProviders(DatagramSocket.class, new CastingProviderMap() {
            @Override
            protected void addProviders() {
                this.addProviders(FileDescriptorCast.GLOBAL_PROVIDERS);
                this.registerGenericDatagramSocketProviders();
            }
        });
    }
    
    private static <S extends AFSocket<?>> S reconfigure(final boolean isChannel, final S socket) throws IOException {
        reconfigure(isChannel, socket.getChannel());
        socket.getAFImpl().getCore().disableCleanFd();
        return socket;
    }
    
    private static <S extends AFServerSocket<?>> S reconfigure(final boolean isChannel, final S socket) throws IOException {
        reconfigure(isChannel, socket.getChannel());
        socket.getAFImpl().getCore().disableCleanFd();
        return socket;
    }
    
    private static <S extends AFDatagramSocket<?>> S reconfigure(final boolean isChannel, final S socket) throws IOException {
        reconfigure(isChannel, socket.getChannel());
        socket.getAFImpl().getCore().disableCleanFd();
        return socket;
    }
    
    private static <S extends AFSomeSocketChannel> void reconfigure(final boolean isChannel, final S socketChannel) throws IOException {
        if (isChannel) {
            reconfigureKeepBlockingState(socketChannel);
        }
        else {
            reconfigureSetBlocking(socketChannel);
        }
    }
    
    private static <S extends AFSomeSocketChannel> void reconfigureKeepBlockingState(final S socketChannel) throws IOException {
        final int result = NativeUnixSocket.checkBlocking(socketChannel.getFileDescriptor());
        boolean blocking = false;
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
    
    private static <S extends AFSomeSocketChannel> void reconfigureSetBlocking(final S socketChannel) throws IOException {
        final int result = NativeUnixSocket.checkBlocking(socketChannel.getFileDescriptor());
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
        PRIMARY_TYPE_PROVIDERS_MAP = Collections.synchronizedMap(new HashMap<Class<?>, CastingProviderMap>());
        FD_IS_PROVIDER = (AFFunction)((System.getProperty("osv.version") != null) ? (x$0 -> new LenientFileInputStream(x$0)) : FileInputStream::new);
        GLOBAL_PROVIDERS_FINAL = new CastingProviderMap() {
            @Override
            protected void addProviders() {
                this.addProvider(FileDescriptor.class, new CastingProvider<FileDescriptor>() {
                    @Override
                    public FileDescriptor provideAs(final FileDescriptorCast fdc, final Class<? super FileDescriptor> desiredType) throws IOException {
                        return fdc.getFileDescriptor();
                    }
                });
            }
        };
        GLOBAL_PROVIDERS = new CastingProviderMap() {
            @Override
            protected void addProviders() {
                this.addProvider(WritableByteChannel.class, new CastingProvider<WritableByteChannel>() {
                    @Override
                    public WritableByteChannel provideAs(final FileDescriptorCast fdc, final Class<? super WritableByteChannel> desiredType) throws IOException {
                        return new FileOutputStream(fdc.getFileDescriptor()).getChannel();
                    }
                });
                this.addProvider(ReadableByteChannel.class, new CastingProvider<ReadableByteChannel>() {
                    @Override
                    public ReadableByteChannel provideAs(final FileDescriptorCast fdc, final Class<? super ReadableByteChannel> desiredType) throws IOException {
                        return FileDescriptorCast.FD_IS_PROVIDER.apply(fdc.getFileDescriptor()).getChannel();
                    }
                });
                this.addProvider(FileChannel.class, new CastingProvider<FileChannel>() {
                    @Override
                    public FileChannel provideAs(final FileDescriptorCast fdc, final Class<? super FileChannel> desiredType) throws IOException {
                        return RAFChannelProvider.getFileChannel(fdc.getFileDescriptor());
                    }
                });
                this.addProvider(FileOutputStream.class, new CastingProvider<FileOutputStream>() {
                    @Override
                    public FileOutputStream provideAs(final FileDescriptorCast fdc, final Class<? super FileOutputStream> desiredType) throws IOException {
                        return new FileOutputStream(fdc.getFileDescriptor());
                    }
                });
                this.addProvider(FileInputStream.class, new CastingProvider<FileInputStream>() {
                    @Override
                    public FileInputStream provideAs(final FileDescriptorCast fdc, final Class<? super FileInputStream> desiredType) throws IOException {
                        return FileDescriptorCast.FD_IS_PROVIDER.apply(fdc.getFileDescriptor());
                    }
                });
                this.addProvider(FileDescriptor.class, new CastingProvider<FileDescriptor>() {
                    @Override
                    public FileDescriptor provideAs(final FileDescriptorCast fdc, final Class<? super FileDescriptor> desiredType) throws IOException {
                        return fdc.getFileDescriptor();
                    }
                });
                this.addProvider(Integer.class, new CastingProvider<Integer>() {
                    @Override
                    public Integer provideAs(final FileDescriptorCast fdc, final Class<? super Integer> desiredType) throws IOException {
                        final FileDescriptor fd = fdc.getFileDescriptor();
                        final int val = fd.valid() ? NativeUnixSocket.getFD(fd) : -1;
                        if (val == -1) {
                            throw new IOException("Not a valid file descriptor");
                        }
                        return val;
                    }
                });
                if (AFSocket.supports(AFSocketCapability.CAPABILITY_FD_AS_REDIRECT)) {
                    this.addProvider(ProcessBuilder.Redirect.class, new CastingProvider<ProcessBuilder.Redirect>() {
                        @Override
                        public ProcessBuilder.Redirect provideAs(final FileDescriptorCast fdc, final Class<? super ProcessBuilder.Redirect> desiredType) throws IOException {
                            final ProcessBuilder.Redirect red = NativeUnixSocket.initRedirect(fdc.getFileDescriptor());
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
    
    private abstract static class CastingProviderMap
    {
        private final Map<Class<?>, CastingProvider<?>> providers;
        private final Set<Class<?>> classes;
        
        protected CastingProviderMap() {
            this.providers = new HashMap<Class<?>, CastingProvider<?>>();
            this.classes = Collections.unmodifiableSet((Set<? extends Class<?>>)this.providers.keySet());
            this.addProviders();
            this.addProviders(FileDescriptorCast.GLOBAL_PROVIDERS_FINAL);
        }
        
        protected void registerGenericSocketProviders() {
            final CastingProviderSocketOrChannel<AFSocket<AFGenericSocketAddress>> cpSocketOrChannelGeneric = (CastingProviderSocketOrChannel<AFSocket<AFGenericSocketAddress>>)((fdc, desiredType, isChannel) -> reconfigure(isChannel, AFSocket.newInstance(AFGenericSocket::new, null, fdc.getFileDescriptor(), fdc.localPort, fdc.remotePort)));
            final CastingProviderSocketOrChannel<AFServerSocket<AFGenericSocketAddress>> cpServerSocketOrChannelGeneric = (CastingProviderSocketOrChannel<AFServerSocket<AFGenericSocketAddress>>)((fdc, desiredType, isChannel) -> reconfigure(isChannel, AFServerSocket.newInstance(AFGenericServerSocket::new, fdc.getFileDescriptor(), fdc.localPort, fdc.remotePort)));
            this.addProvider(AFGenericSocket.class, (fdc, desiredType) -> cpSocketOrChannelGeneric.provideAs(fdc, desiredType, false));
            this.addProvider(AFGenericServerSocket.class, (fdc, desiredType) -> cpServerSocketOrChannelGeneric.provideAs(fdc, desiredType, false));
            this.addProvider(AFGenericSocketChannel.class, (fdc, desiredType) -> cpSocketOrChannelGeneric.provideAs(fdc, AFSocket.class, true).getChannel());
            this.addProvider(AFGenericServerSocketChannel.class, (fdc, desiredType) -> cpServerSocketOrChannelGeneric.provideAs(fdc, AFServerSocket.class, true).getChannel());
        }
        
        protected void registerGenericDatagramSocketProviders() {
            final CastingProviderSocketOrChannel<AFDatagramSocket<AFGenericSocketAddress>> cpDatagramSocketOrChannelGeneric = (CastingProviderSocketOrChannel<AFDatagramSocket<AFGenericSocketAddress>>)((fdc, desiredType, isChannel) -> reconfigure(isChannel, AFDatagramSocket.newInstance(AFGenericDatagramSocket::new, fdc.getFileDescriptor(), fdc.localPort, fdc.remotePort)));
            this.addProvider(AFDatagramSocket.class, (fdc, desiredType) -> cpDatagramSocketOrChannelGeneric.provideAs(fdc, desiredType, false));
            this.addProvider(AFDatagramChannel.class, (fdc, desiredType) -> cpDatagramSocketOrChannelGeneric.provideAs(fdc, AFDatagramSocket.class, true).getChannel());
        }
        
        protected abstract void addProviders();
        
        protected final <T> void addProvider(final Class<T> type, final CastingProvider<?> cp) {
            this.addProvider0(type, cp);
        }
        
        private void addProvider0(final Class<?> type, final CastingProvider<?> cp) {
            if (this.providers.put(type, cp) != cp) {
                for (final Class<?> cl : type.getInterfaces()) {
                    this.addProvider0(cl, cp);
                }
                final Class<?> scl = type.getSuperclass();
                if (scl != null) {
                    this.addProvider0(scl, cp);
                }
            }
        }
        
        protected final void addProviders(final CastingProviderMap other) {
            if (other == null || other == this) {
                return;
            }
            this.providers.putAll(other.providers);
        }
        
        public <T> CastingProvider<? extends T> get(final Class<T> desiredType) {
            return this.providers.get(desiredType);
        }
    }
    
    private static final class LenientFileInputStream extends FileInputStream
    {
        private LenientFileInputStream(final FileDescriptor fdObj) {
            super(fdObj);
        }
        
        @Override
        public int available() throws IOException {
            try {
                return super.available();
            }
            catch (final IOException e) {
                final String msg = e.getMessage();
                if ("Invalid seek".equals(msg)) {
                    return 0;
                }
                throw e;
            }
        }
    }
    
    @FunctionalInterface
    private interface CastingProviderSocketOrChannel<T>
    {
        T provideAs(final FileDescriptorCast p0, final Class<? super T> p1, final boolean p2) throws IOException;
    }
    
    @FunctionalInterface
    private interface CastingProvider<T>
    {
        T provideAs(final FileDescriptorCast p0, final Class<? super T> p1) throws IOException;
    }
}
