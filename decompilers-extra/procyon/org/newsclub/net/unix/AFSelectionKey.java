// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.nio.channels.Selector;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.concurrent.atomic.AtomicBoolean;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

final class AFSelectionKey extends SelectionKey
{
    private static final int OP_INVALID = 128;
    private final AFSelector sel;
    private final AFSocketCore core;
    private int ops;
    private final SelectableChannel chann;
    private final AtomicBoolean cancelled;
    private int opsReady;
    
    AFSelectionKey(final AFSelector selector, final AbstractSelectableChannel ch, final int ops, final Object att) {
        this.cancelled = new AtomicBoolean();
        this.chann = ch;
        this.sel = selector;
        this.ops = ops;
        if (ch instanceof AFDatagramChannel) {
            this.core = ((AFDatagramChannel)ch).getAFCore();
        }
        else if (ch instanceof AFSocketChannel) {
            this.core = ((AFSocketChannel)ch).getAFCore();
        }
        else {
            if (!(ch instanceof AFServerSocketChannel)) {
                throw new UnsupportedOperationException("Unsupported channel: " + ch);
            }
            this.core = ((AFServerSocketChannel)ch).getAFCore();
        }
        this.attach(att);
    }
    
    @Override
    public SelectableChannel channel() {
        return this.chann;
    }
    
    @Override
    public Selector selector() {
        return this.sel;
    }
    
    @Override
    public boolean isValid() {
        return !this.hasOpInvalid() && !this.cancelled.get() && this.chann.isOpen() && this.sel.isOpen();
    }
    
    boolean isCancelled() {
        return this.cancelled.get();
    }
    
    boolean hasOpInvalid() {
        return (this.opsReady & 0x80) != 0x0;
    }
    
    boolean isSelected() {
        return this.readyOps() != 0;
    }
    
    @Override
    public void cancel() {
        this.sel.remove(this);
        this.cancelNoRemove();
    }
    
    void cancelNoRemove() {
        if (!this.cancelled.compareAndSet(false, true) || !this.chann.isOpen()) {
            return;
        }
        this.cancel1();
    }
    
    private void cancel1() {
    }
    
    @Override
    public int interestOps() {
        return this.ops;
    }
    
    @Override
    public SelectionKey interestOps(final int interestOps) {
        this.ops = interestOps;
        return this;
    }
    
    @Override
    public int readyOps() {
        return this.opsReady & 0xFFFFFF7F;
    }
    
    AFSocketCore getAFCore() {
        return this.core;
    }
    
    void setOpsReady(final int opsReady) {
        this.opsReady = opsReady;
    }
    
    @Override
    public String toString() {
        return super.toString() + "[" + this.readyOps() + ";valid=" + this.isValid() + ";channel=" + this.channel() + "]";
    }
}
