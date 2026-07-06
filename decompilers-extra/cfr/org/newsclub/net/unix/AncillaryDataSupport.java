/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.kohlschutter.annotations.compiletime.SuppressFBWarnings
 */
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
import java.util.List;
import java.util.Map;
import org.newsclub.net.unix.NativeUnixSocket;

final class AncillaryDataSupport
implements Closeable {
    private static final ByteBuffer EMPTY_BUFFER = ByteBuffer.allocate(0);
    private static final FileDescriptor[] NO_FILE_DESCRIPTORS = new FileDescriptor[0];
    private static final int MIN_ANCBUF_LEN = NativeUnixSocket.isLoaded() ? NativeUnixSocket.ancillaryBufMinLen() : 0;
    private final Map<FileDescriptor, Integer> openReceivedFileDescriptors = Collections.synchronizedMap(new HashMap());
    private final List<FileDescriptor[]> receivedFileDescriptors = Collections.synchronizedList(new ArrayList());
    private ByteBuffer ancillaryReceiveBuffer = EMPTY_BUFFER;
    @SuppressFBWarnings(value={"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    int[] pendingFileDescriptors = null;
    private int[] tipcErrorInfo = null;
    private int[] tipcDestName = null;

    AncillaryDataSupport() {
    }

    void setTipcErrorInfo(int errorCode, int dataLength) {
        this.tipcErrorInfo = (int[])(errorCode == 0 && dataLength == 0 ? null : new int[]{errorCode, dataLength});
    }

    int[] getTIPCErrorInfo() {
        int[] info = this.tipcErrorInfo;
        this.tipcErrorInfo = null;
        return info;
    }

    void setTipcDestName(int a, int b, int c) {
        this.tipcDestName = (int[])(a == 0 && b == 0 && c == 0 ? null : new int[]{a, b, c});
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
            this.setAncillaryReceiveBufferSize0(Math.max(256, Math.min(MIN_ANCBUF_LEN, size)));
        }
    }

    void setAncillaryReceiveBufferSize0(int size) {
        this.ancillaryReceiveBuffer = ByteBuffer.allocateDirect(size);
    }

    public void ensureAncillaryReceiveBufferSize(int minSize) {
        if (minSize <= 0) {
            return;
        }
        if (this.ancillaryReceiveBuffer.capacity() < minSize) {
            this.setAncillaryReceiveBufferSize(minSize);
        }
    }

    void receiveFileDescriptors(int[] fds) throws IOException {
        if (fds == null || fds.length == 0) {
            return;
        }
        int fdsLength = fds.length;
        FileDescriptor[] descriptors = new FileDescriptor[fdsLength];
        for (int i = 0; i < fdsLength; ++i) {
            FileDescriptor fdesc = new FileDescriptor();
            NativeUnixSocket.initFD(fdesc, fds[i]);
            descriptors[i] = fdesc;
            this.openReceivedFileDescriptors.put(fdesc, fds[i]);
            Closeable cleanup = new Closeable(){
                final /* synthetic */ AncillaryDataSupport this$0;
                {
                    this.this$0 = this$0;
                }

                @Override
                public void close() throws IOException {
                    this.this$0.openReceivedFileDescriptors.remove(fdesc);
                }
            };
            try {
                NativeUnixSocket.attachCloseable(fdesc, cleanup);
                continue;
            }
            catch (SocketException socketException) {
                // empty catch block
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
        ArrayList<FileDescriptor[]> copy = new ArrayList<FileDescriptor[]>(this.receivedFileDescriptors);
        if (copy.isEmpty()) {
            return NO_FILE_DESCRIPTORS;
        }
        this.receivedFileDescriptors.removeAll(copy);
        int count = 0;
        for (FileDescriptor[] fds : copy) {
            count += fds.length;
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
        this.pendingFileDescriptors = fds == null || fds.length == 0 ? null : fds;
    }

    boolean hasOutboundFileDescriptors() {
        return this.pendingFileDescriptors != null;
    }

    void setOutboundFileDescriptors(FileDescriptor ... fdescs) throws IOException {
        int[] fds;
        if (fdescs == null || fdescs.length == 0) {
            fds = null;
        } else {
            int numFdescs = fdescs.length;
            fds = new int[numFdescs];
            for (int i = 0; i < numFdescs; ++i) {
                FileDescriptor fdesc = fdescs[i];
                fds[i] = NativeUnixSocket.getFD(fdesc);
            }
        }
        this.setOutboundFileDescriptors(fds);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void close() {
        Map<FileDescriptor, Integer> map = this.openReceivedFileDescriptors;
        synchronized (map) {
            for (FileDescriptor desc : this.openReceivedFileDescriptors.keySet()) {
                if (!desc.valid()) continue;
                try {
                    NativeUnixSocket.close(desc);
                }
                catch (Exception exception) {}
            }
        }
    }
}

