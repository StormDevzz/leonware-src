// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.net.SocketException;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.io.FileDescriptor;
import java.nio.channels.SelectableChannel;
import java.nio.channels.ClosedSelectorException;
import java.net.SocketTimeoutException;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.io.IOException;
import java.util.Objects;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.atomic.AtomicInteger;
import java.nio.channels.SelectionKey;
import java.util.Set;
import java.util.Map;
import java.nio.ByteBuffer;
import java.nio.channels.spi.AbstractSelector;

final class AFSelector extends AbstractSelector
{
    private final AFPipe selectorPipe;
    private final PollFd selectorPipePollFd;
    private final ByteBuffer pipeMsgWakeUp;
    private final ByteBuffer pipeMsgReceiveBuffer;
    private final Map<AFSelectionKey, Integer> keysRegistered;
    private final Set<AFSelectionKey> keysRegisteredKeySet;
    private final Set<SelectionKey> keysRegisteredPublic;
    private final AtomicInteger selectCount;
    private final MapValueSet<SelectionKey, Integer> selectedKeysSet;
    private final Set<SelectionKey> selectedKeysPublic;
    private PollFd pollFd;
    
    AFSelector(final AFSelectorProvider<?> provider) throws IOException {
        super(provider);
        this.pipeMsgWakeUp = ByteBuffer.allocate(1);
        this.pipeMsgReceiveBuffer = ByteBuffer.allocateDirect(256);
        this.keysRegistered = new ConcurrentHashMap<AFSelectionKey, Integer>();
        this.keysRegisteredKeySet = this.keysRegistered.keySet();
        this.keysRegisteredPublic = Collections.unmodifiableSet((Set<? extends SelectionKey>)this.keysRegisteredKeySet);
        this.selectCount = new AtomicInteger(0);
        final Map<AFSelectionKey, Integer> keysRegistered = this.keysRegistered;
        final AtomicInteger selectCount = this.selectCount;
        Objects.requireNonNull(selectCount);
        this.selectedKeysSet = new MapValueSet<SelectionKey, Integer>((Map<?, Object>)keysRegistered, (MapValueSet.ValueSupplier<Object>)selectCount::get, 0);
        this.selectedKeysPublic = new UngrowableSet<SelectionKey>(this.selectedKeysSet);
        this.pollFd = null;
        this.selectorPipe = AFUNIXSelectorProvider.getInstance().openSelectablePipe();
        this.selectorPipePollFd = new PollFd(this.selectorPipe.sourceFD());
    }
    
    @Override
    protected SelectionKey register(final AbstractSelectableChannel ch, final int ops, final Object att) {
        final AFSelectionKey key = new AFSelectionKey(this, ch, ops, att);
        synchronized (this) {
            this.pollFd = null;
            this.selectedKeysSet.markRemoved(key);
        }
        return key;
    }
    
    @Override
    public Set<SelectionKey> keys() {
        return this.keysRegisteredPublic;
    }
    
    @Override
    public Set<SelectionKey> selectedKeys() {
        return this.selectedKeysPublic;
    }
    
    @Override
    public int selectNow() throws IOException {
        return this.select0(0);
    }
    
    @Override
    public int select(long timeout) throws IOException {
        if (timeout > 2147483647L) {
            timeout = 2147483647L;
        }
        else if (timeout < 0L) {
            throw new IllegalArgumentException("Timeout must not be negative");
        }
        return this.select0((int)timeout);
    }
    
    @Override
    public int select() throws IOException {
        try {
            return this.select0(-1);
        }
        catch (final SocketTimeoutException e) {
            return 0;
        }
    }
    
    private int select0(final int timeout) throws IOException {
        final int selectId = this.updateSelectCount();
        PollFd pfd;
        synchronized (this) {
            if (!this.isOpen()) {
                throw new ClosedSelectorException();
            }
            final PollFd initPollFd = this.initPollFd(this.pollFd);
            this.pollFd = initPollFd;
            pfd = initPollFd;
        }
        int num;
        try {
            this.begin();
            num = NativeUnixSocket.poll(pfd, timeout);
        }
        finally {
            this.end();
        }
        synchronized (this) {
            pfd = this.pollFd;
            if (pfd != null) {
                final AFSelectionKey[] keys = pfd.keys;
                if (keys != null) {
                    for (final AFSelectionKey key : keys) {
                        if (key != null && key.hasOpInvalid()) {
                            final SelectableChannel ch = key.channel();
                            if (ch != null && ch.isOpen()) {
                                ch.close();
                            }
                        }
                    }
                }
            }
            if (num > 0) {
                this.consumeAllBytesAfterPoll();
                this.setOpsReady(pfd, selectId);
            }
            return this.selectedKeysSet.size();
        }
    }
    
    private synchronized void consumeAllBytesAfterPoll() throws IOException {
        if (this.pollFd == null) {
            return;
        }
        if ((this.pollFd.rops[0] & 0x1) == 0x0) {
            return;
        }
        final int options = this.selectorPipe.getOptions();
        final int maxReceive;
        final int bytesReceived;
        synchronized (this.pipeMsgReceiveBuffer) {
            this.pipeMsgReceiveBuffer.clear();
            maxReceive = this.pipeMsgReceiveBuffer.remaining();
            bytesReceived = NativeUnixSocket.receive(this.pollFd.fds[0], this.pipeMsgReceiveBuffer, 0, maxReceive, null, options, null, 1);
        }
        if (bytesReceived == maxReceive && maxReceive > 0) {
            int read;
            do {
                if ((read = NativeUnixSocket.poll(this.selectorPipePollFd, 0)) > 0) {
                    synchronized (this.pipeMsgReceiveBuffer) {
                        this.pipeMsgReceiveBuffer.clear();
                        read = NativeUnixSocket.receive(this.selectorPipePollFd.fds[0], this.pipeMsgReceiveBuffer, 0, maxReceive, null, options, null, 1);
                    }
                }
            } while (read == maxReceive && read > 0);
        }
    }
    
    private int updateSelectCount() {
        int selectId = this.selectCount.incrementAndGet();
        if (selectId == 0) {
            this.selectedKeysSet.markAllRemoved();
            selectId = this.selectCount.incrementAndGet();
        }
        return selectId;
    }
    
    private void setOpsReady(final PollFd pfd, final int selectId) {
        if (pfd != null) {
            for (int i = 1; i < pfd.rops.length; ++i) {
                final int rops = pfd.rops[i];
                final AFSelectionKey key = pfd.keys[i];
                key.setOpsReady(rops);
                if (rops != 0 && this.keysRegistered.containsKey(key)) {
                    this.keysRegistered.put(key, selectId);
                }
            }
        }
    }
    
    private PollFd initPollFd(PollFd existingPollFd) throws IOException {
        synchronized (this) {
            final Iterator<AFSelectionKey> it = this.keysRegisteredKeySet.iterator();
            while (it.hasNext()) {
                final AFSelectionKey key = it.next();
                if (!key.getAFCore().fd.valid() || !key.isValid()) {
                    key.cancelNoRemove();
                    it.remove();
                    existingPollFd = null;
                }
                else {
                    key.setOpsReady(0);
                }
            }
            if (existingPollFd != null && existingPollFd.keys != null && existingPollFd.keys.length - 1 == this.keysRegistered.size()) {
                boolean needsUpdate = false;
                int i = 1;
                for (final AFSelectionKey key2 : this.keysRegisteredKeySet) {
                    if (existingPollFd.keys[i] != key2 || !key2.isValid()) {
                        needsUpdate = true;
                        break;
                    }
                    existingPollFd.ops[i] = key2.interestOps();
                    ++i;
                }
                if (!needsUpdate) {
                    return existingPollFd;
                }
            }
            int keysToPoll = this.keysRegistered.size();
            for (final AFSelectionKey key3 : this.keysRegisteredKeySet) {
                if (!key3.isValid()) {
                    --keysToPoll;
                }
            }
            final int size = keysToPoll + 1;
            final FileDescriptor[] fds = new FileDescriptor[size];
            final int[] ops = new int[size];
            final AFSelectionKey[] keys = new AFSelectionKey[size];
            fds[0] = this.selectorPipe.sourceFD();
            ops[0] = 1;
            int j = 1;
            for (final AFSelectionKey key4 : this.keysRegisteredKeySet) {
                if (!key4.isValid()) {
                    continue;
                }
                keys[j] = key4;
                fds[j] = key4.getAFCore().fd;
                ops[j] = key4.interestOps();
                ++j;
            }
            return new PollFd(keys, fds, ops);
        }
    }
    
    @Override
    protected void implCloseSelector() throws IOException {
        this.wakeup();
        final Set<SelectionKey> keys;
        synchronized (this) {
            keys = this.keys();
            this.keysRegistered.clear();
        }
        for (final SelectionKey key : keys) {
            ((AFSelectionKey)key).cancelNoRemove();
        }
        this.selectorPipe.close();
    }
    
    @Override
    public Selector wakeup() {
        if (this.isOpen()) {
            try {
                synchronized (this.pipeMsgWakeUp) {
                    this.pipeMsgWakeUp.clear();
                    try {
                        this.selectorPipe.sink().write(this.pipeMsgWakeUp);
                    }
                    catch (final SocketException e) {
                        if (this.selectorPipe.sinkFD().valid()) {
                            throw e;
                        }
                    }
                }
            }
            catch (final IOException e2) {
                StackTraceUtil.printStackTrace(e2);
            }
        }
        return this;
    }
    
    synchronized void remove(final AFSelectionKey key) {
        this.selectedKeysSet.remove(key);
        this.deregister(key);
        this.pollFd = null;
    }
    
    private void deregister(final AFSelectionKey key) {
        try {
            NativeUnixSocket.deregisterSelectionKey((AbstractSelectableChannel)key.channel(), key);
        }
        catch (final ClassCastException ex) {}
    }
    
    static final class PollFd
    {
        final FileDescriptor[] fds;
        final int[] ops;
        final int[] rops;
        final AFSelectionKey[] keys;
        
        PollFd(final FileDescriptor pipeSourceFd) {
            this(pipeSourceFd, 1);
        }
        
        PollFd(final FileDescriptor pipeSourceFd, final int op) {
            this.fds = new FileDescriptor[] { pipeSourceFd };
            this.ops = new int[] { op };
            this.rops = new int[1];
            this.keys = null;
        }
        
        PollFd(final AFSelectionKey[] keys, final FileDescriptor[] fds, final int[] ops) {
            this.keys = keys;
            if (fds.length != ops.length) {
                throw new IllegalStateException();
            }
            this.fds = fds;
            this.ops = ops;
            this.rops = new int[ops.length];
        }
    }
}
