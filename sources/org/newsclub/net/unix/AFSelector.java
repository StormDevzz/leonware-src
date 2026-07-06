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
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSelector.class */
final class AFSelector extends AbstractSelector {
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

    AFSelector(AFSelectorProvider<?> provider) throws IOException {
        super(provider);
        this.pipeMsgWakeUp = ByteBuffer.allocate(1);
        this.pipeMsgReceiveBuffer = ByteBuffer.allocateDirect(256);
        this.keysRegistered = new ConcurrentHashMap();
        this.keysRegisteredKeySet = this.keysRegistered.keySet();
        this.keysRegisteredPublic = Collections.unmodifiableSet(this.keysRegisteredKeySet);
        this.selectCount = new AtomicInteger(0);
        Map<AFSelectionKey, Integer> map = this.keysRegistered;
        AtomicInteger atomicInteger = this.selectCount;
        Objects.requireNonNull(atomicInteger);
        this.selectedKeysSet = new MapValueSet<>(map, atomicInteger::get, 0);
        this.selectedKeysPublic = new UngrowableSet(this.selectedKeysSet);
        this.pollFd = null;
        this.selectorPipe = AFUNIXSelectorProvider.getInstance().openSelectablePipe();
        this.selectorPipePollFd = new PollFd(this.selectorPipe.sourceFD());
    }

    @Override // java.nio.channels.spi.AbstractSelector
    protected SelectionKey register(AbstractSelectableChannel ch, int ops, Object att) {
        AFSelectionKey key = new AFSelectionKey(this, ch, ops, att);
        synchronized (this) {
            this.pollFd = null;
            this.selectedKeysSet.markRemoved(key);
        }
        return key;
    }

    @Override // java.nio.channels.Selector
    public Set<SelectionKey> keys() {
        return this.keysRegisteredPublic;
    }

    @Override // java.nio.channels.Selector
    public Set<SelectionKey> selectedKeys() {
        return this.selectedKeysPublic;
    }

    @Override // java.nio.channels.Selector
    public int selectNow() throws IOException {
        return select0(0);
    }

    @Override // java.nio.channels.Selector
    public int select(long timeout) throws IOException {
        if (timeout > 2147483647L) {
            timeout = 2147483647L;
        } else if (timeout < 0) {
            throw new IllegalArgumentException("Timeout must not be negative");
        }
        return select0((int) timeout);
    }

    @Override // java.nio.channels.Selector
    public int select() throws IOException {
        try {
            return select0(-1);
        } catch (SocketTimeoutException e) {
            return 0;
        }
    }

    private int select0(int timeout) throws IOException {
        PollFd pfd;
        int size;
        AFSelectionKey[] keys;
        SelectableChannel ch;
        int selectId = updateSelectCount();
        synchronized (this) {
            if (!isOpen()) {
                throw new ClosedSelectorException();
            }
            pfd = initPollFd(this.pollFd);
            this.pollFd = pfd;
        }
        try {
            begin();
            int num = NativeUnixSocket.poll(pfd, timeout);
            end();
            synchronized (this) {
                PollFd pfd2 = this.pollFd;
                if (pfd2 != null && (keys = pfd2.keys) != null) {
                    for (AFSelectionKey key : keys) {
                        if (key != null && key.hasOpInvalid() && (ch = key.channel()) != null && ch.isOpen()) {
                            ch.close();
                        }
                    }
                }
                if (num > 0) {
                    consumeAllBytesAfterPoll();
                    setOpsReady(pfd2, selectId);
                }
                size = this.selectedKeysSet.size();
            }
            return size;
        } catch (Throwable th) {
            end();
            throw th;
        }
    }

    private synchronized void consumeAllBytesAfterPoll() throws IOException {
        int maxReceive;
        int bytesReceived;
        int read;
        if (this.pollFd == null || (this.pollFd.rops[0] & 1) == 0) {
            return;
        }
        int options = this.selectorPipe.getOptions();
        synchronized (this.pipeMsgReceiveBuffer) {
            this.pipeMsgReceiveBuffer.clear();
            maxReceive = this.pipeMsgReceiveBuffer.remaining();
            bytesReceived = NativeUnixSocket.receive(this.pollFd.fds[0], this.pipeMsgReceiveBuffer, 0, maxReceive, null, options, null, 1);
        }
        if (bytesReceived != maxReceive || maxReceive <= 0) {
            return;
        }
        do {
            int iPoll = NativeUnixSocket.poll(this.selectorPipePollFd, 0);
            read = iPoll;
            if (iPoll > 0) {
                synchronized (this.pipeMsgReceiveBuffer) {
                    this.pipeMsgReceiveBuffer.clear();
                    read = NativeUnixSocket.receive(this.selectorPipePollFd.fds[0], this.pipeMsgReceiveBuffer, 0, maxReceive, null, options, null, 1);
                }
            }
            if (read != maxReceive) {
                return;
            }
        } while (read > 0);
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
            for (int i = 1; i < pfd.rops.length; i++) {
                int rops = pfd.rops[i];
                AFSelectionKey key = pfd.keys[i];
                key.setOpsReady(rops);
                if (rops != 0 && this.keysRegistered.containsKey(key)) {
                    this.keysRegistered.put(key, Integer.valueOf(selectId));
                }
            }
        }
    }

    private PollFd initPollFd(PollFd existingPollFd) throws IOException {
        synchronized (this) {
            Iterator<AFSelectionKey> it = this.keysRegisteredKeySet.iterator();
            while (it.hasNext()) {
                AFSelectionKey key = it.next();
                if (!key.getAFCore().fd.valid() || !key.isValid()) {
                    key.cancelNoRemove();
                    it.remove();
                    existingPollFd = null;
                } else {
                    key.setOpsReady(0);
                }
            }
            if (existingPollFd != null && existingPollFd.keys != null && existingPollFd.keys.length - 1 == this.keysRegistered.size()) {
                boolean needsUpdate = false;
                int i = 1;
                for (AFSelectionKey key2 : this.keysRegisteredKeySet) {
                    if (existingPollFd.keys[i] != key2 || !key2.isValid()) {
                        needsUpdate = true;
                        break;
                    }
                    existingPollFd.ops[i] = key2.interestOps();
                    i++;
                }
                if (!needsUpdate) {
                    return existingPollFd;
                }
            }
            int keysToPoll = this.keysRegistered.size();
            Iterator<AFSelectionKey> it2 = this.keysRegisteredKeySet.iterator();
            while (it2.hasNext()) {
                if (!it2.next().isValid()) {
                    keysToPoll--;
                }
            }
            int size = keysToPoll + 1;
            FileDescriptor[] fds = new FileDescriptor[size];
            int[] ops = new int[size];
            AFSelectionKey[] keys = new AFSelectionKey[size];
            fds[0] = this.selectorPipe.sourceFD();
            ops[0] = 1;
            int i2 = 1;
            for (AFSelectionKey key3 : this.keysRegisteredKeySet) {
                if (key3.isValid()) {
                    keys[i2] = key3;
                    fds[i2] = key3.getAFCore().fd;
                    ops[i2] = key3.interestOps();
                    i2++;
                }
            }
            return new PollFd(keys, fds, ops);
        }
    }

    @Override // java.nio.channels.spi.AbstractSelector
    protected void implCloseSelector() throws IOException {
        Set<SelectionKey> keys;
        wakeup();
        synchronized (this) {
            keys = keys();
            this.keysRegistered.clear();
        }
        for (SelectionKey key : keys) {
            ((AFSelectionKey) key).cancelNoRemove();
        }
        this.selectorPipe.close();
    }

    @Override // java.nio.channels.Selector
    public Selector wakeup() {
        if (isOpen()) {
            try {
                synchronized (this.pipeMsgWakeUp) {
                    this.pipeMsgWakeUp.clear();
                    try {
                        this.selectorPipe.sink().write(this.pipeMsgWakeUp);
                    } catch (SocketException e) {
                        if (this.selectorPipe.sinkFD().valid()) {
                            throw e;
                        }
                    }
                }
            } catch (IOException e2) {
                StackTraceUtil.printStackTrace(e2);
            }
        }
        return this;
    }

    synchronized void remove(AFSelectionKey key) {
        this.selectedKeysSet.remove(key);
        deregister(key);
        this.pollFd = null;
    }

    private void deregister(AFSelectionKey key) {
        try {
            NativeUnixSocket.deregisterSelectionKey((AbstractSelectableChannel) key.channel(), key);
        } catch (ClassCastException e) {
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSelector$PollFd.class */
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
