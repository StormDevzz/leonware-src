package org.newsclub.net.unix;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.SocketException;
import java.net.URI;
import java.nio.channels.UnsupportedAddressTypeException;
import java.nio.channels.spi.SelectorProvider;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.newsclub.net.unix.AFServerSocket;
import org.newsclub.net.unix.AFSocket;
import org.newsclub.net.unix.AFSocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFAddressFamily.class */
public final class AFAddressFamily<A extends AFSocketAddress> {
    private static final Map<String, AFAddressFamily<?>> AF_MAP = Collections.synchronizedMap(new HashMap());
    private static final Map<String, AFAddressFamily<?>> URI_SCHEMES = Collections.synchronizedMap(new HashMap());
    private static final AtomicBoolean DEFERRED_INIT_DONE = new AtomicBoolean(false);
    private final int domain;
    private AFSocketAddress.AFSocketAddressConstructor<A> addressConstructor;
    private Class<A> addressClass;
    private final String juxString;
    private final String juxInetAddressSuffix;
    private final String addressClassname;
    private String selectorProviderClassname;
    private AFSocket.Constructor<A> socketConstructor;
    private AFServerSocket.Constructor<A> serverSocketConstructor;
    private AFSocketAddressConfig<A> addressConfig;
    private SelectorProvider selectorProvider = null;

    static {
        NativeUnixSocket.isLoaded();
    }

    private AFAddressFamily(String juxString, int domain, String addressClassname) {
        this.juxString = juxString;
        this.domain = domain;
        this.addressClassname = addressClassname;
        this.juxInetAddressSuffix = "." + juxString + ".junixsocket";
    }

    static synchronized <A extends AFSocketAddress> AFAddressFamily<A> registerAddressFamily(String juxString, int domain, String addressClassname) {
        AFAddressFamily<A> aFAddressFamily = (AFAddressFamily) AF_MAP.get(juxString);
        if (aFAddressFamily != null) {
            if (aFAddressFamily.getDomain() != domain) {
                throw new IllegalStateException("Wrong domain for address family " + juxString + ": " + aFAddressFamily.getDomain() + " vs. " + domain);
            }
            return aFAddressFamily;
        }
        AFAddressFamily<A> aFAddressFamily2 = new AFAddressFamily<>(juxString, domain, addressClassname);
        AF_MAP.put(juxString, aFAddressFamily2);
        return aFAddressFamily2;
    }

    static synchronized void triggerInit() {
        for (AFAddressFamily<?> af : new HashSet(AF_MAP.values())) {
            if (((AFAddressFamily) af).addressClassname != null) {
                try {
                    Class<?> clz = Class.forName(((AFAddressFamily) af).addressClassname);
                    clz.getMethod("addressFamily", new Class[0]).invoke(null, new Object[0]);
                } catch (Exception e) {
                }
            }
        }
    }

    static synchronized AFAddressFamily<?> getAddressFamily(String juxString) {
        return AF_MAP.get(juxString);
    }

    static AFAddressFamily<?> getAddressFamily(URI uri) {
        checkDeferredInit();
        Objects.requireNonNull(uri, "uri");
        String scheme = uri.getScheme();
        return URI_SCHEMES.get(scheme);
    }

    static void checkDeferredInit() {
        if (DEFERRED_INIT_DONE.compareAndSet(false, true)) {
            NativeUnixSocket.isLoaded();
            triggerInit();
        }
    }

    int getDomain() {
        return this.domain;
    }

    String getJuxString() {
        return this.juxString;
    }

    AFSocketAddress.AFSocketAddressConstructor<A> getAddressConstructor() {
        if (this.addressConstructor == null) {
            throw new UnsupportedAddressTypeException();
        }
        return this.addressConstructor;
    }

    private synchronized void checkProvider() {
        if (this.socketConstructor == null && this.selectorProvider == null) {
            try {
                getSelectorProvider();
            } catch (IllegalStateException e) {
            }
        }
    }

    AFSocket.Constructor<A> getSocketConstructor() {
        checkProvider();
        if (this.socketConstructor == null) {
            throw new UnsupportedAddressTypeException();
        }
        return this.socketConstructor;
    }

    AFServerSocket.Constructor<A> getServerSocketConstructor() {
        checkProvider();
        if (this.serverSocketConstructor == null) {
            throw new UnsupportedAddressTypeException();
        }
        return this.serverSocketConstructor;
    }

    Class<A> getSocketAddressClass() {
        if (this.addressClass == null) {
            throw new UnsupportedAddressTypeException();
        }
        return this.addressClass;
    }

    String getJuxInetAddressSuffix() {
        return this.juxInetAddressSuffix;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static synchronized <A extends AFSocketAddress> AFAddressFamily<A> registerAddressFamily(String str, Class<A> cls, AFSocketAddressConfig<A> aFSocketAddressConfig) {
        AFAddressFamily<A> aFAddressFamily = (AFAddressFamily<A>) getAddressFamily(str);
        if (aFAddressFamily == 0) {
            throw new IllegalStateException("Address family not supported by native code: " + str);
        }
        if (((AFAddressFamily) aFAddressFamily).addressClassname != null && !cls.getName().equals(((AFAddressFamily) aFAddressFamily).addressClassname)) {
            throw new IllegalStateException("Unexpected classname for address family " + str + ": " + cls.getName() + "; expected: " + ((AFAddressFamily) aFAddressFamily).addressClassname);
        }
        if (((AFAddressFamily) aFAddressFamily).addressConstructor != null || ((AFAddressFamily) aFAddressFamily).addressClass != null) {
            throw new IllegalStateException("Already registered: " + str);
        }
        ((AFAddressFamily) aFAddressFamily).addressConfig = aFSocketAddressConfig;
        ((AFAddressFamily) aFAddressFamily).addressConstructor = aFSocketAddressConfig.addressConstructor();
        ((AFAddressFamily) aFAddressFamily).addressClass = cls;
        synchronized (aFAddressFamily) {
            ((AFAddressFamily) aFAddressFamily).selectorProviderClassname = aFSocketAddressConfig.selectorProviderClassname();
        }
        for (String str2 : aFSocketAddressConfig.uriSchemes()) {
            if (str2.isEmpty()) {
                throw new IllegalStateException("Invalid URI scheme; cannot register " + str2 + " for " + str);
            }
            if (URI_SCHEMES.containsKey(str2)) {
                throw new IllegalStateException("URI scheme already registered; cannot register " + str2 + " for " + str);
            }
            URI_SCHEMES.put(str2, (AFAddressFamily<?>) aFAddressFamily);
        }
        return aFAddressFamily;
    }

    public static synchronized <A extends AFSocketAddress> AFAddressFamily<A> registerAddressFamilyImpl(String str, AFAddressFamily<A> aFAddressFamily, AFAddressFamilyConfig<A> aFAddressFamilyConfig) {
        Objects.requireNonNull(aFAddressFamily);
        Objects.requireNonNull(aFAddressFamilyConfig);
        AFAddressFamily<A> aFAddressFamily2 = (AFAddressFamily<A>) getAddressFamily(str);
        if (aFAddressFamily2 == null) {
            throw new IllegalStateException("Unknown address family: " + str);
        }
        if (aFAddressFamily != aFAddressFamily2) {
            throw new IllegalStateException("Address family inconsistency: " + str);
        }
        if (((AFAddressFamily) aFAddressFamily2).socketConstructor != null) {
            throw new IllegalStateException("Already registered: " + str);
        }
        ((AFAddressFamily) aFAddressFamily2).socketConstructor = aFAddressFamilyConfig.socketConstructor();
        ((AFAddressFamily) aFAddressFamily2).serverSocketConstructor = aFAddressFamilyConfig.serverSocketConstructor();
        FileDescriptorCast.registerCastingProviders(aFAddressFamilyConfig);
        return aFAddressFamily2;
    }

    AFSocketImplExtensions<A> initImplExtensions(AncillaryDataSupport ancillaryDataSupport) {
        switch (getDomain()) {
            case 30:
                return new AFTIPCSocketImplExtensions(ancillaryDataSupport);
            case 32:
                return new AFSYSTEMSocketImplExtensions(ancillaryDataSupport);
            case 40:
                return new AFVSOCKSocketImplExtensions(ancillaryDataSupport);
            default:
                throw new UnsupportedOperationException();
        }
    }

    public AFSocket<?> newSocket() throws IOException {
        try {
            return getSocketConstructor().newInstance(null, null);
        } catch (UnsupportedOperationException e) {
            throw ((SocketException) new SocketException().initCause(e));
        }
    }

    public AFServerSocket<?> newServerSocket() throws IOException {
        try {
            return getServerSocketConstructor().newInstance(null);
        } catch (UnsupportedOperationException e) {
            throw ((SocketException) new SocketException().initCause(e));
        }
    }

    public AFSocketChannel<?> newSocketChannel() throws IOException {
        return newSocket().getChannel();
    }

    public AFServerSocketChannel<?> newServerSocketChannel() throws IOException {
        return newServerSocket().getChannel();
    }

    AFSocketAddress parseURI(URI u, int overridePort) throws SocketException {
        if (this.addressConfig == null) {
            throw new SocketException("Cannot instantiate addresses of type " + this.addressClass);
        }
        return this.addressConfig.parseURI(u, overridePort);
    }

    public static synchronized Set<String> uriSchemes() {
        checkDeferredInit();
        return Collections.unmodifiableSet(URI_SCHEMES.keySet());
    }

    public synchronized SelectorProvider getSelectorProvider() {
        if (this.selectorProvider != null) {
            return this.selectorProvider;
        }
        if (this.selectorProviderClassname == null) {
            return null;
        }
        try {
            this.selectorProvider = (SelectorProvider) Class.forName(this.selectorProviderClassname).getMethod("provider", new Class[0]).invoke(null, new Object[0]);
            return this.selectorProvider;
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | RuntimeException | InvocationTargetException e) {
            throw new IllegalStateException("Cannot instantiate selector provider for " + this.addressClassname, e);
        }
    }
}
