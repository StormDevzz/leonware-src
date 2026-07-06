// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.util.HashMap;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Set;
import java.io.IOException;
import java.net.SocketException;
import java.io.FileDescriptor;
import java.nio.channels.UnsupportedAddressTypeException;
import java.util.Objects;
import java.net.URI;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Map;

public final class AFAddressFamily<A extends AFSocketAddress>
{
    private static final Map<String, AFAddressFamily<?>> AF_MAP;
    private static final Map<String, AFAddressFamily<?>> URI_SCHEMES;
    private static final AtomicBoolean DEFERRED_INIT_DONE;
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
    private SelectorProvider selectorProvider;
    
    private AFAddressFamily(final String juxString, final int domain, final String addressClassname) {
        this.selectorProvider = null;
        this.juxString = juxString;
        this.domain = domain;
        this.addressClassname = addressClassname;
        this.juxInetAddressSuffix = "." + juxString + ".junixsocket";
    }
    
    static synchronized <A extends AFSocketAddress> AFAddressFamily<A> registerAddressFamily(final String juxString, final int domain, final String addressClassname) {
        AFAddressFamily<?> af = AFAddressFamily.AF_MAP.get(juxString);
        if (af == null) {
            af = new AFAddressFamily<Object>(juxString, domain, addressClassname);
            AFAddressFamily.AF_MAP.put(juxString, af);
            return (AFAddressFamily<A>)af;
        }
        if (af.getDomain() != domain) {
            throw new IllegalStateException("Wrong domain for address family " + juxString + ": " + af.getDomain() + " vs. " + domain);
        }
        return (AFAddressFamily<A>)af;
    }
    
    static synchronized void triggerInit() {
        for (final AFAddressFamily<?> af : new HashSet(AFAddressFamily.AF_MAP.values())) {
            if (af.addressClassname != null) {
                try {
                    final Class<?> clz = Class.forName(af.addressClassname);
                    clz.getMethod("addressFamily", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
                }
                catch (final Exception ex) {}
            }
        }
    }
    
    static synchronized AFAddressFamily<?> getAddressFamily(final String juxString) {
        return AFAddressFamily.AF_MAP.get(juxString);
    }
    
    static AFAddressFamily<?> getAddressFamily(final URI uri) {
        checkDeferredInit();
        Objects.requireNonNull(uri, "uri");
        final String scheme = uri.getScheme();
        return AFAddressFamily.URI_SCHEMES.get(scheme);
    }
    
    static void checkDeferredInit() {
        if (AFAddressFamily.DEFERRED_INIT_DONE.compareAndSet(false, true)) {
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
                this.getSelectorProvider();
            }
            catch (final IllegalStateException ex) {}
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
    
    public static synchronized <A extends AFSocketAddress> AFAddressFamily<A> registerAddressFamily(final String juxString, final Class<A> addressClass, final AFSocketAddressConfig<A> config) {
        final AFAddressFamily<?> af = getAddressFamily(juxString);
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
        synchronized (af) {
            af.selectorProviderClassname = config.selectorProviderClassname();
        }
        for (final String scheme : config.uriSchemes()) {
            if (scheme.isEmpty()) {
                throw new IllegalStateException("Invalid URI scheme; cannot register " + scheme + " for " + juxString);
            }
            if (AFAddressFamily.URI_SCHEMES.containsKey(scheme)) {
                throw new IllegalStateException("URI scheme already registered; cannot register " + scheme + " for " + juxString);
            }
            AFAddressFamily.URI_SCHEMES.put(scheme, af);
        }
        return (AFAddressFamily<A>)af;
    }
    
    public static synchronized <A extends AFSocketAddress> AFAddressFamily<A> registerAddressFamilyImpl(final String juxString, final AFAddressFamily<A> addressFamily, final AFAddressFamilyConfig<A> config) {
        Objects.requireNonNull(addressFamily);
        Objects.requireNonNull(config);
        final AFAddressFamily<?> af = getAddressFamily(juxString);
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
        return (AFAddressFamily<A>)af;
    }
    
    AFSocketImplExtensions<A> initImplExtensions(final AncillaryDataSupport ancillaryDataSupport) {
        switch (this.getDomain()) {
            case 30: {
                return (AFSocketImplExtensions<A>)new AFTIPCSocketImplExtensions(ancillaryDataSupport);
            }
            case 40: {
                return (AFSocketImplExtensions<A>)new AFVSOCKSocketImplExtensions(ancillaryDataSupport);
            }
            case 32: {
                return (AFSocketImplExtensions<A>)new AFSYSTEMSocketImplExtensions(ancillaryDataSupport);
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }
    
    public AFSocket<?> newSocket() throws IOException {
        try {
            return this.getSocketConstructor().newInstance(null, null);
        }
        catch (final UnsupportedOperationException e) {
            throw (SocketException)new SocketException().initCause(e);
        }
    }
    
    public AFServerSocket<?> newServerSocket() throws IOException {
        try {
            return this.getServerSocketConstructor().newInstance(null);
        }
        catch (final UnsupportedOperationException e) {
            throw (SocketException)new SocketException().initCause(e);
        }
    }
    
    public AFSocketChannel<?> newSocketChannel() throws IOException {
        return this.newSocket().getChannel();
    }
    
    public AFServerSocketChannel<?> newServerSocketChannel() throws IOException {
        return this.newServerSocket().getChannel();
    }
    
    AFSocketAddress parseURI(final URI u, final int overridePort) throws SocketException {
        if (this.addressConfig == null) {
            throw new SocketException("Cannot instantiate addresses of type " + this.addressClass);
        }
        return this.addressConfig.parseURI(u, overridePort);
    }
    
    public static synchronized Set<String> uriSchemes() {
        checkDeferredInit();
        return Collections.unmodifiableSet((Set<? extends String>)AFAddressFamily.URI_SCHEMES.keySet());
    }
    
    public synchronized SelectorProvider getSelectorProvider() {
        if (this.selectorProvider != null) {
            return this.selectorProvider;
        }
        if (this.selectorProviderClassname == null) {
            return null;
        }
        try {
            this.selectorProvider = (SelectorProvider)Class.forName(this.selectorProviderClassname).getMethod("provider", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
        }
        catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException | RuntimeException e) {
            throw new IllegalStateException("Cannot instantiate selector provider for " + this.addressClassname, e);
        }
        return this.selectorProvider;
    }
    
    static {
        AF_MAP = Collections.synchronizedMap(new HashMap<String, AFAddressFamily<?>>());
        URI_SCHEMES = Collections.synchronizedMap(new HashMap<String, AFAddressFamily<?>>());
        DEFERRED_INIT_DONE = new AtomicBoolean(false);
        NativeUnixSocket.isLoaded();
    }
}
