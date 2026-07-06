package org.newsclub.net.unix;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSelectionKey.class */
final class AFSelectionKey extends SelectionKey {
    private static final int OP_INVALID = 128;
    private final AFSelector sel;
    private final AFSocketCore core;
    private int ops;
    private final SelectableChannel chann;
    private final AtomicBoolean cancelled = new AtomicBoolean();
    private int opsReady;

    AFSelectionKey(AFSelector selector, AbstractSelectableChannel ch, int ops, Object att) {
        this.chann = ch;
        this.sel = selector;
        this.ops = ops;
        if (ch instanceof AFDatagramChannel) {
            this.core = ((AFDatagramChannel) ch).getAFCore();
        } else if (ch instanceof AFSocketChannel) {
            this.core = ((AFSocketChannel) ch).getAFCore();
        } else if (ch instanceof AFServerSocketChannel) {
            this.core = ((AFServerSocketChannel) ch).getAFCore();
        } else {
            throw new UnsupportedOperationException("Unsupported channel: " + ch);
        }
        attach(att);
    }

    @Override // java.nio.channels.SelectionKey
    public SelectableChannel channel() {
        return this.chann;
    }

    @Override // java.nio.channels.SelectionKey
    public Selector selector() {
        return this.sel;
    }

    @Override // java.nio.channels.SelectionKey
    public boolean isValid() {
        return !hasOpInvalid() && !this.cancelled.get() && this.chann.isOpen() && this.sel.isOpen();
    }

    boolean isCancelled() {
        return this.cancelled.get();
    }

    boolean hasOpInvalid() {
        return (this.opsReady & OP_INVALID) != 0;
    }

    boolean isSelected() {
        return readyOps() != 0;
    }

    @Override // java.nio.channels.SelectionKey
    public void cancel() {
        this.sel.remove(this);
        cancelNoRemove();
    }

    void cancelNoRemove() {
        if (!this.cancelled.compareAndSet(false, true) || !this.chann.isOpen()) {
            return;
        }
        cancel1();
    }

    private void cancel1() {
    }

    @Override // java.nio.channels.SelectionKey
    public int interestOps() {
        return this.ops;
    }

    @Override // java.nio.channels.SelectionKey
    public SelectionKey interestOps(int interestOps) {
        this.ops = interestOps;
        return this;
    }

    @Override // java.nio.channels.SelectionKey
    public int readyOps() {
        return this.opsReady & (-129);
    }

    AFSocketCore getAFCore() {
        return this.core;
    }

    void setOpsReady(int opsReady) {
        this.opsReady = opsReady;
    }

    public String toString() {
        return super.toString() + "[" + readyOps() + ";valid=" + isValid() + ";channel=" + channel() + "]";
    }
}
