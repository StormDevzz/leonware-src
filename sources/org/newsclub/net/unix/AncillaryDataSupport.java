package org.newsclub.net.unix;

import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AncillaryDataSupport.class */
final class AncillaryDataSupport implements Closeable {
    private static final ByteBuffer EMPTY_BUFFER = ByteBuffer.allocate(0);
    private static final FileDescriptor[] NO_FILE_DESCRIPTORS = new FileDescriptor[0];
    private static final int MIN_ANCBUF_LEN;
    private final Map<FileDescriptor, Integer> openReceivedFileDescriptors = Collections.synchronizedMap(new HashMap());
    private final List<FileDescriptor[]> receivedFileDescriptors = Collections.synchronizedList(new ArrayList());
    private ByteBuffer ancillaryReceiveBuffer = EMPTY_BUFFER;

    @SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    int[] pendingFileDescriptors = null;
    private int[] tipcErrorInfo = null;
    private int[] tipcDestName = null;

    AncillaryDataSupport() {
    }

    static {
        MIN_ANCBUF_LEN = NativeUnixSocket.isLoaded() ? NativeUnixSocket.ancillaryBufMinLen() : 0;
    }

    void setTipcErrorInfo(int errorCode, int dataLength) {
        if (errorCode == 0 && dataLength == 0) {
            this.tipcErrorInfo = null;
        } else {
            this.tipcErrorInfo = new int[]{errorCode, dataLength};
        }
    }

    int[] getTIPCErrorInfo() {
        int[] info = this.tipcErrorInfo;
        this.tipcErrorInfo = null;
        return info;
    }

    void setTipcDestName(int a, int b, int c) {
        if (a == 0 && b == 0 && c == 0) {
            this.tipcDestName = null;
        } else {
            this.tipcDestName = new int[]{a, b, c};
        }
    }

    int[] getTIPCDestName() {
        int[] addr = this.tipcDestName;
        this.tipcDestName = null;
        return addr;
    }

    int getAncillaryReceiveBufferSize() {
        return this.ancillaryReceiveBuffer.capacity();
    }

    void setAncillaryReceiveBufferSize(int size) {
        if (size == this.ancillaryReceiveBuffer.capacity()) {
            return;
        }
        if (size <= 0) {
            this.ancillaryReceiveBuffer = EMPTY_BUFFER;
        } else {
            setAncillaryReceiveBufferSize0(Math.max(256, Math.min(MIN_ANCBUF_LEN, size)));
        }
    }

    void setAncillaryReceiveBufferSize0(int size) {
        this.ancillaryReceiveBuffer = ByteBuffer.allocateDirect(size);
    }

    public void ensureAncillaryReceiveBufferSize(int minSize) {
        if (minSize > 0 && this.ancillaryReceiveBuffer.capacity() < minSize) {
            setAncillaryReceiveBufferSize(minSize);
        }
    }

    void receiveFileDescriptors(int[] fds) throws IOException {
        if (fds == null || fds.length == 0) {
            return;
        }
        int fdsLength = fds.length;
        FileDescriptor[] descriptors = new FileDescriptor[fdsLength];
        for (int i = 0; i < fdsLength; i++) {
            final FileDescriptor fdesc = new FileDescriptor();
            NativeUnixSocket.initFD(fdesc, fds[i]);
            descriptors[i] = fdesc;
            this.openReceivedFileDescriptors.put(fdesc, Integer.valueOf(fds[i]));
            Closeable cleanup = new Closeable(this) { // from class: org.newsclub.net.unix.AncillaryDataSupport.1
                final /* synthetic */ AncillaryDataSupport this$0;

                {
                    this.this$0 = this;
                }

                @Override // java.io.Closeable, java.lang.AutoCloseable
                public void close() throws IOException {
                    this.this$0.openReceivedFileDescriptors.remove(fdesc);
                }
            };
            try {
                NativeUnixSocket.attachCloseable(fdesc, cleanup);
            } catch (SocketException e) {
            }
        }
        this.receivedFileDescriptors.add(descriptors);
    }

    void clearReceivedFileDescriptors() {
        this.receivedFileDescriptors.clear();
    }

    FileDescriptor[] getReceivedFileDescriptors() {
        if (this.receivedFileDescriptors.isEmpty()) {
            return NO_FILE_DESCRIPTORS;
        }
        List<FileDescriptor[]> copy = new ArrayList<>(this.receivedFileDescriptors);
        if (copy.isEmpty()) {
            return NO_FILE_DESCRIPTORS;
        }
        this.receivedFileDescriptors.removeAll(copy);
        int count = 0;
        Iterator<FileDescriptor[]> it = copy.iterator();
        while (it.hasNext()) {
            count += it.next().length;
        }
        if (count == 0) {
            return NO_FILE_DESCRIPTORS;
        }
        FileDescriptor[] oneArray = new FileDescriptor[count];
        int offset = 0;
        for (FileDescriptor[] fds : copy) {
            System.arraycopy(fds, 0, oneArray, offset, fds.length);
            offset += fds.length;
        }
        return oneArray;
    }

    void setOutboundFileDescriptors(int[] fds) {
        this.pendingFileDescriptors = (fds == null || fds.length == 0) ? null : fds;
    }

    boolean hasOutboundFileDescriptors() {
        return this.pendingFileDescriptors != null;
    }

    void setOutboundFileDescriptors(FileDescriptor... fdescs) throws IOException {
        int[] fds;
        if (fdescs == null || fdescs.length == 0) {
            fds = null;
        } else {
            int numFdescs = fdescs.length;
            fds = new int[numFdescs];
            for (int i = 0; i < numFdescs; i++) {
                FileDescriptor fdesc = fdescs[i];
                fds[i] = NativeUnixSocket.getFD(fdesc);
            }
        }
        setOutboundFileDescriptors(fds);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        synchronized (this.openReceivedFileDescriptors) {
            for (FileDescriptor desc : this.openReceivedFileDescriptors.keySet()) {
                if (desc.valid()) {
                    try {
                        NativeUnixSocket.close(desc);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}
