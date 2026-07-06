/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.AbstractSelector;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.newsclub.net.unix.AFPipe;
import org.newsclub.net.unix.AFSelectionKey;
import org.newsclub.net.unix.AFSelectorProvider;
import org.newsclub.net.unix.AFUNIXSelectorProvider;
import org.newsclub.net.unix.MapValueSet;
import org.newsclub.net.unix.NativeUnixSocket;
import org.newsclub.net.unix.StackTraceUtil;
import org.newsclub.net.unix.UngrowableSet;

final class AFSelector
extends AbstractSelector {
    private final AFPipe selectorPipe;
    private final PollFd selectorPipePollFd;
    private final ByteBuffer pipeMsgWakeUp = ByteBuffer.allocate(1);
    private final ByteBuffer pipeMsgReceiveBuffer = ByteBuffer.allocateDirect(256);
    private final Map<AFSelectionKey, Integer> keysRegistered = new ConcurrentHashMap<AFSelectionKey, Integer>();
    private final Set<AFSelectionKey> keysRegisteredKeySet = this.keysRegistered.keySet();
    private final Set<SelectionKey> keysRegisteredPublic = Collections.unmodifiableSet(this.keysRegisteredKeySet);
    private final AtomicInteger selectCount = new AtomicInteger(0);
    private final MapValueSet<SelectionKey, Integer> selectedKeysSet = new MapValueSet<AFSelectionKey, Integer>(this.keysRegistered, this.selectCount::get, 0);
    private final Set<SelectionKey> selectedKeysPublic = new UngrowableSet<SelectionKey>(this.selectedKeysSet);
    private PollFd pollFd = null;

    AFSelector(AFSelectorProvider<?> provider) throws IOException {
        super(provider);
        this.selectorPipe = AFUNIXSelectorProvider.getInstance().openSelectablePipe();
        this.selectorPipePollFd = new PollFd(this.selectorPipe.sourceFD());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected SelectionKey register(AbstractSelectableChannel ch, int ops, Object att) {
        AFSelectionKey key = new AFSelectionKey(this, ch, ops, att);
        AFSelector aFSelector = this;
        synchronized (aFSelector) {
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
        if (timeout > Integer.MAX_VALUE) {
            timeout = Integer.MAX_VALUE;
        } else if (timeout < 0L) {
            throw new IllegalArgumentException("Timeout must not be negative");
        }
        return this.select0((int)timeout);
    }

    @Override
    public int select() throws IOException {
        try {
            return this.select0(-1);
        }
        catch (SocketTimeoutException e) {
            return 0;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private int select0(int timeout) throws IOException {
        int num;
        PollFd pfd;
        int selectId = this.updateSelectCount();
        AFSelector aFSelector = this;
        synchronized (aFSelector) {
            if (!this.isOpen()) {
                throw new ClosedSelectorException();
            }
            pfd = this.pollFd = this.initPollFd(this.pollFd);
        }
        try {
            this.begin();
            num = NativeUnixSocket.poll(pfd, timeout);
        }
        finally {
            this.end();
        }
        AFSelector aFSelector2 = this;
        synchronized (aFSelector2) {
            AFSelectionKey[] keys;
            pfd = this.pollFd;
            if (pfd != null && (keys = pfd.keys) != null) {
                for (AFSelectionKey key : keys) {
                    SelectableChannel ch;
                    if (key == null || !key.hasOpInvalid() || (ch = key.channel()) == null || !ch.isOpen()) continue;
                    ch.close();
                }
            }
            if (num > 0) {
                this.consumeAllBytesAfterPoll();
                this.setOpsReady(pfd, selectId);
            }
            return this.selectedKeysSet.size();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private synchronized void consumeAllBytesAfterPoll() throws IOException {
        int bytesReceived;
        int maxReceive;
        if (this.pollFd == null) {
            return;
        }
        if ((this.pollFd.rops[0] & 1) == 0) {
            return;
        }
        int options = this.selectorPipe.getOptions();
        ByteBuffer byteBuffer = this.pipeMsgReceiveBuffer;
        synchronized (byteBuffer) {
            this.pipeMsgReceiveBuffer.clear();
            maxReceive = this.pipeMsgReceiveBuffer.remaining();
            bytesReceived = NativeUnixSocket.receive(this.pollFd.fds[0], this.pipeMsgReceiveBuffer, 0, maxReceive, null, options, null, 1);
        }
        if (bytesReceived == maxReceive && maxReceive > 0) {
            int read;
            do {
                if ((read = NativeUnixSocket.poll(this.selectorPipePollFd, 0)) <= 0) continue;
                ByteBuffer byteBuffer2 = this.pipeMsgReceiveBuffer;
                synchronized (byteBuffer2) {
                    this.pipeMsgReceiveBuffer.clear();
                    read = NativeUnixSocket.receive(this.selectorPipePollFd.fds[0], this.pipeMsgReceiveBuffer, 0, maxReceive, null, options, null, 1);
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

    private void setOpsReady(PollFd pfd, int selectId) {
        if (pfd != null) {
            for (int i = 1; i < pfd.rops.length; ++i) {
                int rops = pfd.rops[i];
                AFSelectionKey key = pfd.keys[i];
                key.setOpsReady(rops);
                if (rops == 0 || !this.keysRegistered.containsKey(key)) continue;
                this.keysRegistered.put(key, selectId);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private PollFd initPollFd(PollFd existingPollFd) throws IOException {
        AFSelector aFSelector = this;
        synchronized (aFSelector) {
            Iterator<AFSelectionKey> it = this.keysRegisteredKeySet.iterator();
            while (it.hasNext()) {
                AFSelectionKey key = it.next();
                if (!key.getAFCore().fd.valid() || !key.isValid()) {
                    key.cancelNoRemove();
                    it.remove();
                    existingPollFd = null;
                    continue;
                }
                key.setOpsReady(0);
            }
            if (existingPollFd != null && existingPollFd.keys != null && existingPollFd.keys.length - 1 == this.keysRegistered.size()) {
                boolean needsUpdate = false;
                int i = 1;
                for (AFSelectionKey key : this.keysRegisteredKeySet) {
                    if (existingPollFd.keys[i] != key || !key.isValid()) {
                        needsUpdate = true;
                        break;
                    }
                    existingPollFd.ops[i] = key.interestOps();
                    ++i;
                }
                if (!needsUpdate) {
                    return existingPollFd;
                }
            }
            int keysToPoll = this.keysRegistered.size();
            for (AFSelectionKey key : this.keysRegisteredKeySet) {
                if (key.isValid()) continue;
                --keysToPoll;
            }
            int size = keysToPoll + 1;
            FileDescriptor[] fds = new FileDescriptor[size];
            int[] ops = new int[size];
            AFSelectionKey[] keys = new AFSelectionKey[size];
            fds[0] = this.selectorPipe.sourceFD();
            ops[0] = 1;
            int i = 1;
            for (AFSelectionKey key : this.keysRegisteredKeySet) {
                if (!key.isValid()) continue;
                keys[i] = key;
                fds[i] = key.getAFCore().fd;
                ops[i] = key.interestOps();
                ++i;
            }
            return new PollFd(keys, fds, ops);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected void implCloseSelector() throws IOException {
        Set<SelectionKey> keys;
        this.wakeup();
        AFSelector aFSelector = this;
        synchronized (aFSelector) {
            keys = this.keys();
            this.keysRegistered.clear();
        }
        for (SelectionKey key : keys) {
            ((AFSelectionKey)key).cancelNoRemove();
        }
        this.selectorPipe.close();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Selector wakeup() {
        if (this.isOpen()) {
            try {
                ByteBuffer byteBuffer = this.pipeMsgWakeUp;
                synchronized (byteBuffer) {
                    block8: {
                        this.pipeMsgWakeUp.clear();
                        try {
                            this.selectorPipe.sink().write(this.pipeMsgWakeUp);
                        }
                        catch (SocketException e) {
                            if (!this.selectorPipe.sinkFD().valid()) break block8;
                            throw e;
                        }
                    }
                }
            }
            catch (IOException e) {
                StackTraceUtil.printStackTrace(e);
            }
        }
        return this;
    }

    synchronized void remove(AFSelectionKey key) {
        this.selectedKeysSet.remove(key);
        this.deregister(key);
        this.pollFd = null;
    }

    private void deregister(AFSelectionKey key) {
        try {
            NativeUnixSocket.deregisterSelectionKey((AbstractSelectableChannel)key.channel(), key);
        }
        catch (ClassCastException classCastException) {
            // empty catch block
        }
    }

    static final class PollFd {
        final FileDescriptor[] fds;
        final int[] ops;
        final int[] rops;
        final AFSelectionKey[] keys;

        PollFd(FileDescriptor pipeSourceFd) {
            this(pipeSourceFd, 1);
        }

        PollFd(FileDescriptor pipeSourceFd, int op) {
            this.fds = new FileDescriptor[]{pipeSourceFd};
            this.ops = new int[]{op};
            this.rops = new int[1];
            this.keys = null;
        }

        PollFd(AFSelectionKey[] keys, FileDescriptor[] fds, int[] ops) {
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

