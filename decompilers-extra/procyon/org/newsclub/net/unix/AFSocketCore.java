// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.net.SocketException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.io.FileDescriptor;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;

class AFSocketCore extends AFCore
{
    private final AtomicInteger pendingAccepts;
    private static final int SHUT_RD_WR = 2;
    final AtomicLong inode;
    AFSocketAddress socketAddress;
    private final AFAddressFamily<?> af;
    private boolean shutdownOnClose;
    
    protected AFSocketCore(final Object observed, final FileDescriptor fd, final AncillaryDataSupport ancillaryDataSupport, final AFAddressFamily<?> af, final boolean datagramMode) {
        super(observed, fd, ancillaryDataSupport, datagramMode);
        this.pendingAccepts = new AtomicInteger(0);
        this.inode = new AtomicLong(-1L);
        this.shutdownOnClose = true;
        this.af = af;
    }
    
    protected AFAddressFamily<?> addressFamily() {
        return this.af;
    }
    
    protected void doClose() throws IOException {
        if (this.isShutdownOnClose()) {
            NativeUnixSocket.shutdown(this.fd, 2);
            this.unblockAccepts();
        }
        super.doClose();
    }
    
    protected void unblockAccepts() {
    }
    
    AFSocketAddress receive(final ByteBuffer dst) throws IOException {
        final ByteBuffer socketAddressBuffer = AFSocketAddress.SOCKETADDRESS_BUFFER_TL.get();
        final int read = this.read(dst, socketAddressBuffer, 0);
        if (read > 0) {
            return AFSocketAddress.ofInternal(socketAddressBuffer, this.af);
        }
        return null;
    }
    
    boolean isConnected(final boolean boundOk) {
        try {
            if (this.fd.valid()) {
                switch (NativeUnixSocket.socketStatus(this.fd)) {
                    case 2: {
                        return true;
                    }
                    case 1: {
                        if (boundOk) {
                            return true;
                        }
                        break;
                    }
                }
            }
        }
        catch (final IOException e) {
            throw new IllegalStateException(e);
        }
        return false;
    }
    
     <T> T getOption(final AFSocketOption<T> name) throws IOException {
        final Class<T> type = name.type();
        if (Boolean.class.isAssignableFrom(type)) {
            return (T)Boolean.valueOf(NativeUnixSocket.getSocketOption(this.fd, name.level(), name.optionName(), Integer.class) != 0);
        }
        if (NamedInteger.HasOfValue.class.isAssignableFrom(type)) {
            final int v = NativeUnixSocket.getSocketOption(this.fd, name.level(), name.optionName(), Integer.class);
            try {
                return (T)type.getMethod("ofValue", Integer.TYPE).invoke(null, v);
            }
            catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                throw new IOException("Value casting problem", e);
            }
        }
        return NativeUnixSocket.getSocketOption(this.fd, name.level(), name.optionName(), type);
    }
    
     <T> void setOption(final AFSocketOption<T> name, final T value) throws IOException {
        Object val;
        if (value instanceof Boolean) {
            val = (((boolean)value) ? 1 : 0);
        }
        else if (value instanceof NamedInteger) {
            val = ((NamedInteger)value).value();
        }
        else {
            val = value;
        }
        final int level = name.level();
        final int optionName = name.optionName();
        NativeUnixSocket.setSocketOption(this.fd, level, optionName, val);
        if (level == 271 && optionName == 135) {
            try {
                Thread.sleep(1L);
            }
            catch (final InterruptedException ex) {}
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
    
    void setShutdownOnClose(final boolean enabled) {
        this.shutdownOnClose = enabled;
    }
}
