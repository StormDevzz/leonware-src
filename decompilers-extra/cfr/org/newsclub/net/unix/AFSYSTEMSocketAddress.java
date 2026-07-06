/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jdt.annotation.NonNull
 *  org.eclipse.jdt.annotation.NonNullByDefault
 */
package org.newsclub.net.unix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.newsclub.net.unix.AFAddressFamily;
import org.newsclub.net.unix.AFSocketAddress;
import org.newsclub.net.unix.AFSocketAddressConfig;
import org.newsclub.net.unix.HostAndPort;
import org.newsclub.net.unix.NamedInteger;

public final class AFSYSTEMSocketAddress
extends AFSocketAddress {
    private static final long serialVersionUID = 1L;
    private static AFAddressFamily<AFSYSTEMSocketAddress> afSystem;
    private static final String SELECTOR_PROVIDER_CLASS = "org.newsclub.net.unix.darwin.system.AFSYSTEMSelectorProvider";

    private AFSYSTEMSocketAddress(int port, byte[] socketAddress, ByteBuffer nativeAddress) throws SocketException {
        super(port, socketAddress, nativeAddress, AFSYSTEMSocketAddress.addressFamily());
    }

    private static AFSYSTEMSocketAddress newAFSocketAddress(int port, byte[] socketAddress, ByteBuffer nativeAddress) throws SocketException {
        return AFSYSTEMSocketAddress.newDeserializedAFSocketAddress(port, socketAddress, nativeAddress, AFSYSTEMSocketAddress.addressFamily(), AFSYSTEMSocketAddress::new);
    }

    public static AFSYSTEMSocketAddress ofSysAddrIdUnit(int javaPort, SysAddr sysAddr, int id, int unit) throws SocketException {
        return AFSYSTEMSocketAddress.resolveAddress(AFSYSTEMSocketAddress.toBytes(sysAddr, id, unit), javaPort, AFSYSTEMSocketAddress.addressFamily());
    }

    public static AFSYSTEMSocketAddress ofSysAddrIdUnit(SysAddr sysAddr, int id, int unit) throws SocketException {
        return AFSYSTEMSocketAddress.ofSysAddrIdUnit(0, sysAddr, id, unit);
    }

    public static AFSYSTEMSocketAddress unwrap(InetAddress address, int port) throws SocketException {
        return AFSocketAddress.unwrap(address, port, AFSYSTEMSocketAddress.addressFamily());
    }

    public static AFSYSTEMSocketAddress unwrap(String hostname, int port) throws SocketException {
        return AFSocketAddress.unwrap(hostname, port, AFSYSTEMSocketAddress.addressFamily());
    }

    public static AFSYSTEMSocketAddress unwrap(SocketAddress address) throws SocketException {
        Objects.requireNonNull(address);
        if (!AFSYSTEMSocketAddress.isSupportedAddress(address)) {
            throw new SocketException("Unsupported address");
        }
        return (AFSYSTEMSocketAddress)address;
    }

    @Override
    public String toString() {
        int port = this.getPort();
        byte[] bytes = this.getBytes();
        if (bytes.length != 32) {
            return this.getClass().getName() + "[" + (port == 0 ? "" : "port=" + port) + ";UNKNOWN]";
        }
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        SysAddr sysAddr = SysAddr.ofValue(bb.getInt());
        int id = bb.getInt();
        int unit = bb.getInt();
        return this.getClass().getName() + "[" + (port == 0 ? "" : "port=" + port + ";") + sysAddr + ";id=" + id + ";unit=" + unit + "]";
    }

    public @NonNull SysAddr getSysAddr() {
        ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        return SysAddr.ofValue(bb.getInt(0));
    }

    public int getId() {
        ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        return bb.getInt(4);
    }

    public int getUnit() {
        ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        return bb.getInt(8);
    }

    @Override
    public boolean hasFilename() {
        return false;
    }

    @Override
    public File getFile() throws FileNotFoundException {
        throw new FileNotFoundException("no file");
    }

    public static boolean isSupportedAddress(InetAddress addr) {
        return AFSocketAddress.isSupportedAddress(addr, AFSYSTEMSocketAddress.addressFamily());
    }

    public static boolean isSupportedAddress(SocketAddress addr) {
        return addr instanceof AFSYSTEMSocketAddress;
    }

    private static byte[] toBytes(SysAddr sysAddr, int id, int unit) {
        ByteBuffer bb = ByteBuffer.allocate(32);
        bb.putInt(sysAddr.value());
        bb.putInt(id);
        bb.putInt(unit);
        bb.putInt(0);
        bb.putInt(0);
        bb.putInt(0);
        bb.putInt(0);
        bb.putInt(0);
        return (byte[])bb.flip().array();
    }

    public static synchronized AFAddressFamily<AFSYSTEMSocketAddress> addressFamily() {
        if (afSystem == null) {
            afSystem = AFAddressFamily.registerAddressFamily("system", AFSYSTEMSocketAddress.class, new AFSocketAddressConfig<AFSYSTEMSocketAddress>(){
                private final AFSocketAddress.AFSocketAddressConstructor<AFSYSTEMSocketAddress> addrConstr = AFSocketAddress.isUseDeserializationForInit() ? (x$0, x$1, x$2) -> AFSYSTEMSocketAddress.access$100(x$0, x$1, x$2) : (x$0, x$1, x$2) -> new AFSYSTEMSocketAddress(x$0, x$1, x$2);

                @Override
                protected AFSYSTEMSocketAddress parseURI(URI u, int port) throws SocketException {
                    return AFSYSTEMSocketAddress.of(u, port);
                }

                @Override
                protected AFSocketAddress.AFSocketAddressConstructor<AFSYSTEMSocketAddress> addressConstructor() {
                    return this.addrConstr;
                }

                @Override
                protected String selectorProviderClassname() {
                    return AFSYSTEMSocketAddress.SELECTOR_PROVIDER_CLASS;
                }

                @Override
                protected Set<String> uriSchemes() {
                    return new HashSet<String>(Arrays.asList("afsystem"));
                }
            });
            try {
                Class.forName(SELECTOR_PROVIDER_CLASS);
            }
            catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
        }
        return afSystem;
    }

    public static AFSYSTEMSocketAddress of(URI uri) throws SocketException {
        return AFSYSTEMSocketAddress.of(uri, -1);
    }

    public static AFSYSTEMSocketAddress of(URI uri, int overridePort) throws SocketException {
        switch (uri.getScheme()) {
            case "afsystem": {
                break;
            }
            default: {
                throw new SocketException("Unsupported URI scheme: " + uri.getScheme());
            }
        }
        String host = uri.getHost();
        if (host == null || host.isEmpty()) {
            String ssp = uri.getSchemeSpecificPart();
            if (ssp == null || !ssp.startsWith("//")) {
                throw new SocketException("Unsupported URI: " + uri);
            }
            int i = (ssp = ssp.substring(2)).indexOf(47);
            String string = host = i == -1 ? ssp : ssp.substring(0, i);
            if (host.isEmpty()) {
                throw new SocketException("Unsupported URI: " + uri);
            }
        }
        ByteBuffer bb = ByteBuffer.allocate(32);
        for (String p : host.split("\\.")) {
            int v;
            try {
                v = AFSYSTEMSocketAddress.parseUnsignedInt(p, 10);
            }
            catch (NumberFormatException e) {
                throw (SocketException)new SocketException("Unsupported URI: " + uri).initCause(e);
            }
            bb.putInt(v);
        }
        bb.flip();
        if (bb.remaining() > 32) {
            throw new SocketException("Unsupported URI: " + uri);
        }
        SysAddr.ofValue(bb.getInt());
        bb.getInt();
        bb.getInt();
        while (bb.remaining() > 0) {
            if (bb.getInt() == 0) continue;
            throw new SocketException("Unsupported URI: " + uri);
        }
        return AFSYSTEMSocketAddress.resolveAddress(bb.array(), uri.getPort(), AFSYSTEMSocketAddress.addressFamily());
    }

    @Override
    public URI toURI(String scheme, URI template) throws IOException {
        switch (scheme) {
            case "afsystem": {
                break;
            }
            default: {
                return super.toURI(scheme, template);
            }
        }
        ByteBuffer bb = ByteBuffer.wrap(this.getBytes());
        StringBuilder sb = new StringBuilder();
        while (bb.remaining() > 0) {
            sb.append(AFSYSTEMSocketAddress.toUnsignedString(bb.getInt()));
            if (bb.remaining() <= 0) continue;
            sb.append('.');
        }
        return new HostAndPort(sb.toString(), this.getPort()).toURI(scheme, template);
    }

    @NonNullByDefault
    public static final class SysAddr
    extends NamedInteger {
        private static final long serialVersionUID = 1L;
        public static final SysAddr AF_SYS_CONTROL = new SysAddr("AF_SYS_CONTROL", 2);
        private static final @NonNull SysAddr[] VALUES = (SysAddr[])SysAddr.init((NamedInteger[])new SysAddr[]{AF_SYS_CONTROL});

        private SysAddr(int id) {
            super(id);
        }

        private SysAddr(String name, int id) {
            super(name, id);
        }

        public static SysAddr ofValue(int v) {
            return (SysAddr)SysAddr.ofValue((NamedInteger[])VALUES, SysAddr::new, (int)v);
        }
    }
}

