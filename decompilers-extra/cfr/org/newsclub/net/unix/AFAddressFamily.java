/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jdt.annotation.NonNull
 *  org.eclipse.jdt.annotation.Nullable
 */
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
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.newsclub.net.unix.AFAddressFamilyConfig;
import org.newsclub.net.unix.AFSYSTEMSocketImplExtensions;
import org.newsclub.net.unix.AFServerSocket;
import org.newsclub.net.unix.AFServerSocketChannel;
import org.newsclub.net.unix.AFSocket;
import org.newsclub.net.unix.AFSocketAddress;
import org.newsclub.net.unix.AFSocketAddressConfig;
import org.newsclub.net.unix.AFSocketChannel;
import org.newsclub.net.unix.AFSocketImplExtensions;
import org.newsclub.net.unix.AFTIPCSocketImplExtensions;
import org.newsclub.net.unix.AFVSOCKSocketImplExtensions;
import org.newsclub.net.unix.AncillaryDataSupport;
import org.newsclub.net.unix.FileDescriptorCast;
import org.newsclub.net.unix.NativeUnixSocket;

public final class AFAddressFamily<A extends AFSocketAddress> {
    private static final Map<String, AFAddressFamily<?>> AF_MAP = Collections.synchronizedMap(new HashMap());
    private static final Map<String, AFAddressFamily<?>> URI_SCHEMES = Collections.synchronizedMap(new HashMap());
    private static final AtomicBoolean DEFERRED_INIT_DONE = new AtomicBoolean(false);
    private final int domain;
    private AFSocketAddress.AFSocketAddressConstructor<A> addressConstructor;
    private @Nullable Class<A> addressClass;
    private final String juxString;
    private final String juxInetAddressSuffix;
    private final String addressClassname;
    private String selectorProviderClassname;
    private AFSocket.Constructor<A> socketConstructor;
    private AFServerSocket.Constructor<A> serverSocketConstructor;
    private AFSocketAddressConfig<A> addressConfig;
    private SelectorProvider selectorProvider = null;

    private AFAddressFamily(String juxString, int domain, String addressClassname) {
        this.juxString = juxString;
        this.domain = domain;
        this.addressClassname = addressClassname;
        this.juxInetAddressSuffix = "." + juxString + ".junixsocket";
    }

    static synchronized <A extends AFSocketAddress> @NonNull AFAddressFamily<A> registerAddressFamily(String juxString, int domain, String addressClassname) {
        AFAddressFamily<Object> af = AF_MAP.get(juxString);
        if (af != null) {
            if (af.getDomain() != domain) {
                throw new IllegalStateException("Wrong domain for address family " + juxString + ": " + af.getDomain() + " vs. " + domain);
            }
            return af;
        }
        af = new AFAddressFamily<A>(juxString, domain, addressClassname);
        AF_MAP.put(juxString, af);
        return af;
    }

    static synchronized void triggerInit() {
        for (AFAddressFamily<?> af : new HashSet(AF_MAP.values())) {
            if (af.addressClassname == null) continue;
            try {
                Class<?> clz = Class.forName(af.addressClassname);
                clz.getMethod("addressFamily", new Class[0]).invoke(null, new Object[0]);
            }
            catch (Exception exception) {}
        }
    }

    static synchronized AFAddressFamily<?> getAddressFamily(String juxString) {
        return AF_MAP.get(juxString);
    }

    static AFAddressFamily<?> getAddressFamily(URI uri) {
        AFAddressFamily.checkDeferredInit();
        Objects.requireNonNull(uri, "uri");
        String scheme = uri.getScheme();
        return URI_SCHEMES.get(scheme);
    }

    static void checkDeferredInit() {
        if (DEFERRED_INIT_DONE.compareAndSet(false, true)) {
            NativeUnixSocket.isLoaded();
            AFAddressFamily.triggerInit();
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
                this.getSelectorProvider();
            }
            catch (IllegalStateException illegalStateException) {
                // empty catch block
            }
        }
    }

    AFSocket.Constructor<A> getSocketConstructor() {
        this.checkProvider();
        if (this.socketConstructor == null) {
            throw new UnsupportedAddressTypeException();
        }
        return this.socketConstructor;
    }

    AFServerSocket.Constructor<A> getServerSocketConstructor() {
        this.checkProvider();
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

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static synchronized <A extends AFSocketAddress> AFAddressFamily<A> registerAddressFamily(String juxString, Class<A> addressClass, AFSocketAddressConfig<A> config) {
        AFAddressFamily<?> af = AFAddressFamily.getAddressFamily(juxString);
        if (af == null) {
            throw new IllegalStateException("Address family not supported by native code: " + juxString);
        }
        if (af.addressClassname != null && !addressClass.getName().equals(af.addressClassname)) {
            throw new IllegalStateException("Unexpected classname for address family " + juxString + ": " + addressClass.getName() + "; expected: " + af.addressClassname);
        }
        if (af.addressConstructor != null || af.addressClass != null) {
            throw new IllegalStateException("Already registered: " + juxString);
        }
        af.addressConfig = config;
        af.addressConstructor = config.addressConstructor();
        af.addressClass = addressClass;
        AFAddressFamily<?> aFAddressFamily = af;
        synchronized (aFAddressFamily) {
            af.selectorProviderClassname = config.selectorProviderClassname();
        }
        for (String scheme : config.uriSchemes()) {
            if (scheme.isEmpty()) {
                throw new IllegalStateException("Invalid URI scheme; cannot register " + scheme + " for " + juxString);
            }
            if (URI_SCHEMES.containsKey(scheme)) {
                throw new IllegalStateException("URI scheme already registered; cannot register " + scheme + " for " + juxString);
            }
            URI_SCHEMES.put(scheme, af);
        }
        return af;
    }

    public static synchronized <A extends AFSocketAddress> AFAddressFamily<A> registerAddressFamilyImpl(String juxString, AFAddressFamily<A> addressFamily, AFAddressFamilyConfig<A> config) {
        Objects.requireNonNull(addressFamily);
        Objects.requireNonNull(config);
        AFAddressFamily<?> af = AFAddressFamily.getAddressFamily(juxString);
        if (af == null) {
            throw new IllegalStateException("Unknown address family: " + juxString);
        }
        if (addressFamily != af) {
            throw new IllegalStateException("Address family inconsistency: " + juxString);
        }
        if (af.socketConstructor != null) {
            throw new IllegalStateException("Already registered: " + juxString);
        }
        af.socketConstructor = config.socketConstructor();
        af.serverSocketConstructor = config.serverSocketConstructor();
        FileDescriptorCast.registerCastingProviders(config);
        return af;
    }

    AFSocketImplExtensions<A> initImplExtensions(AncillaryDataSupport ancillaryDataSupport) {
        switch (this.getDomain()) {
            case 30: {
                return new AFTIPCSocketImplExtensions(ancillaryDataSupport);
            }
            case 40: {
                return new AFVSOCKSocketImplExtensions(ancillaryDataSupport);
            }
            case 32: {
                return new AFSYSTEMSocketImplExtensions(ancillaryDataSupport);
            }
        }
        throw new UnsupportedOperationException();
    }

    public AFSocket<?> newSocket() throws IOException {
        try {
            return this.getSocketConstructor().newInstance(null, null);
        }
        catch (UnsupportedOperationException e) {
            throw (SocketException)new SocketException().initCause(e);
        }
    }

    public AFServerSocket<?> newServerSocket() throws IOException {
        try {
            return this.getServerSocketConstructor().newInstance(null);
        }
        catch (UnsupportedOperationException e) {
            throw (SocketException)new SocketException().initCause(e);
        }
    }

    public AFSocketChannel<?> newSocketChannel() throws IOException {
        return this.newSocket().getChannel();
    }

    public AFServerSocketChannel<?> newServerSocketChannel() throws IOException {
        return this.newServerSocket().getChannel();
    }

    AFSocketAddress parseURI(URI u, int overridePort) throws SocketException {
        if (this.addressConfig == null) {
            throw new SocketException("Cannot instantiate addresses of type " + this.addressClass);
        }
        return this.addressConfig.parseURI(u, overridePort);
    }

    public static synchronized Set<String> uriSchemes() {
        AFAddressFamily.checkDeferredInit();
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
            this.selectorProvider = (SelectorProvider)Class.forName(this.selectorProviderClassname).getMethod("provider", new Class[0]).invoke(null, new Object[0]);
        }
        catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | RuntimeException | InvocationTargetException e) {
            throw new IllegalStateException("Cannot instantiate selector provider for " + this.addressClassname, e);
        }
        return this.selectorProvider;
    }

    static {
        NativeUnixSocket.isLoaded();
    }
}

