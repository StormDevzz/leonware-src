/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.newsclub.net.unix.AFAddressFamily;
import org.newsclub.net.unix.AFCore;
import org.newsclub.net.unix.AFSocketAddress;
import org.newsclub.net.unix.AFSocketOption;
import org.newsclub.net.unix.AncillaryDataSupport;
import org.newsclub.net.unix.NamedInteger;
import org.newsclub.net.unix.NativeUnixSocket;

class AFSocketCore
extends AFCore {
    private final AtomicInteger pendingAccepts = new AtomicInteger(0);
    private static final int SHUT_RD_WR = 2;
    final AtomicLong inode = new AtomicLong(-1L);
    AFSocketAddress socketAddress;
    private final AFAddressFamily<?> af;
    private boolean shutdownOnClose = true;

    protected AFSocketCore(Object observed, FileDescriptor fd, AncillaryDataSupport ancillaryDataSupport, AFAddressFamily<?> af, boolean datagramMode) {
        super(observed, fd, ancillaryDataSupport, datagramMode);
        this.af = af;
    }

    protected AFAddressFamily<?> addressFamily() {
        return this.af;
    }

    @Override
    protected void doClose() throws IOException {
        if (this.isShutdownOnClose()) {
            NativeUnixSocket.shutdown(this.fd, 2);
            this.unblockAccepts();
        }
        super.doClose();
    }

    protected void unblockAccepts() {
    }

    AFSocketAddress receive(ByteBuffer dst) throws IOException {
        ByteBuffer socketAddressBuffer = AFSocketAddress.SOCKETADDRESS_BUFFER_TL.get();
        int read = this.read(dst, socketAddressBuffer, 0);
        if (read > 0) {
            return AFSocketAddress.ofInternal(socketAddressBuffer, this.af);
        }
        return null;
    }

    boolean isConnected(boolean boundOk) {
        try {
            if (this.fd.valid()) {
                switch (NativeUnixSocket.socketStatus(this.fd)) {
                    case 2: {
                        return true;
                    }
                    case 1: {
                        if (!boundOk) break;
                        return true;
                    }
                }
            }
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return false;
    }

    <T> T getOption(AFSocketOption<T> name) throws IOException {
        Class<T> type = name.type();
        if (Boolean.class.isAssignableFrom(type)) {
            return (T)Boolean.valueOf(NativeUnixSocket.getSocketOption(this.fd, name.level(), name.optionName(), Integer.class) != 0);
        }
        if (NamedInteger.HasOfValue.class.isAssignableFrom(type)) {
            int v = NativeUnixSocket.getSocketOption(this.fd, name.level(), name.optionName(), Integer.class);
            try {
                return (T)type.getMethod("ofValue", Integer.TYPE).invoke(null, v);
            }
            catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
                throw new IOException("Value casting problem", e);
            }
        }
        return NativeUnixSocket.getSocketOption(this.fd, name.level(), name.optionName(), type);
    }

    <T> void setOption(AFSocketOption<T> name, T value) throws IOException {
        Object val = value instanceof Boolean ? Integer.valueOf((Boolean)value != false ? 1 : 0) : (value instanceof NamedInteger ? Integer.valueOf(((NamedInteger)value).value()) : value);
        int level = name.level();
        int optionName = name.optionName();
        NativeUnixSocket.setSocketOption(this.fd, level, optionName, val);
        if (level == 271 && optionName == 135) {
            try {
                Thread.sleep(1L);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
    }

    protected void incPendingAccepts() throws SocketException {
        if (this.pendingAccepts.incrementAndGet() >= Integer.MAX_VALUE) {
            throw new SocketException("Too many pending accepts");
        }
    }

    protected void decPendingAccepts() throws SocketException {
        this.pendingAccepts.decrementAndGet();
    }

    protected boolean hasPendingAccepts() {
        return this.pendingAccepts.get() > 0;
    }

    boolean isShutdownOnClose() {
        return this.shutdownOnClose;
    }

    void setShutdownOnClose(boolean enabled) {
        this.shutdownOnClose = enabled;
    }
}

