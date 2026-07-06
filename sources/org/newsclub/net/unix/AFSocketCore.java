package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.newsclub.net.unix.NamedInteger;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSocketCore.class */
class AFSocketCore extends AFCore {
    private final AtomicInteger pendingAccepts;
    private static final int SHUT_RD_WR = 2;
    final AtomicLong inode;
    AFSocketAddress socketAddress;
    private final AFAddressFamily<?> af;
    private boolean shutdownOnClose;

    protected AFSocketCore(Object observed, FileDescriptor fd, AncillaryDataSupport ancillaryDataSupport, AFAddressFamily<?> af, boolean datagramMode) {
        super(observed, fd, ancillaryDataSupport, datagramMode);
        this.pendingAccepts = new AtomicInteger(0);
        this.inode = new AtomicLong(-1L);
        this.shutdownOnClose = true;
        this.af = af;
    }

    protected AFAddressFamily<?> addressFamily() {
        return this.af;
    }

    @Override // org.newsclub.net.unix.AFCore
    protected void doClose() throws IOException {
        if (isShutdownOnClose()) {
            NativeUnixSocket.shutdown(this.fd, 2);
            unblockAccepts();
        }
        super.doClose();
    }

    protected void unblockAccepts() {
    }

    AFSocketAddress receive(ByteBuffer dst) throws IOException {
        ByteBuffer socketAddressBuffer = AFSocketAddress.SOCKETADDRESS_BUFFER_TL.get();
        int read = read(dst, socketAddressBuffer, 0);
        if (read > 0) {
            return AFSocketAddress.ofInternal(socketAddressBuffer, this.af);
        }
        return null;
    }

    boolean isConnected(boolean boundOk) {
        try {
            if (this.fd.valid()) {
                switch (NativeUnixSocket.socketStatus(this.fd)) {
                    case 1:
                        if (boundOk) {
                            return true;
                        }
                        return false;
                    case 2:
                        return true;
                    default:
                        return false;
                }
            }
            return false;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    <T> T getOption(AFSocketOption<T> aFSocketOption) throws IOException {
        Class<T> clsType = aFSocketOption.type();
        if (Boolean.class.isAssignableFrom(clsType)) {
            return (T) Boolean.valueOf(((Integer) NativeUnixSocket.getSocketOption(this.fd, aFSocketOption.level(), aFSocketOption.optionName(), Integer.class)).intValue() != 0);
        }
        if (NamedInteger.HasOfValue.class.isAssignableFrom(clsType)) {
            try {
                return (T) clsType.getMethod("ofValue", Integer.TYPE).invoke(null, Integer.valueOf(((Integer) NativeUnixSocket.getSocketOption(this.fd, aFSocketOption.level(), aFSocketOption.optionName(), Integer.class)).intValue()));
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
                throw new IOException("Value casting problem", e);
            }
        }
        return (T) NativeUnixSocket.getSocketOption(this.fd, aFSocketOption.level(), aFSocketOption.optionName(), clsType);
    }

    /* JADX WARN: Multi-variable type inference failed */
    <T> void setOption(AFSocketOption<T> name, T value) throws IOException {
        Object objValueOf;
        if (value instanceof Boolean) {
            objValueOf = Integer.valueOf(((Boolean) value).booleanValue() ? 1 : 0);
        } else if (value instanceof NamedInteger) {
            objValueOf = Integer.valueOf(((NamedInteger) value).value());
        } else {
            objValueOf = value;
        }
        int level = name.level();
        int optionName = name.optionName();
        NativeUnixSocket.setSocketOption(this.fd, level, optionName, objValueOf);
        if (level == 271 && optionName == 135) {
            try {
                Thread.sleep(1L);
            } catch (InterruptedException e) {
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
