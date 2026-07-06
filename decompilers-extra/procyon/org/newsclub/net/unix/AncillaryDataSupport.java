// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.util.Iterator;
import java.util.Collection;
import java.net.SocketException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import com.kohlschutter.annotations.compiletime.SuppressFBWarnings;
import java.util.List;
import java.util.Map;
import java.io.FileDescriptor;
import java.nio.ByteBuffer;
import java.io.Closeable;

final class AncillaryDataSupport implements Closeable
{
    private static final ByteBuffer EMPTY_BUFFER;
    private static final FileDescriptor[] NO_FILE_DESCRIPTORS;
    private static final int MIN_ANCBUF_LEN;
    private final Map<FileDescriptor, Integer> openReceivedFileDescriptors;
    private final List<FileDescriptor[]> receivedFileDescriptors;
    private ByteBuffer ancillaryReceiveBuffer;
    @SuppressFBWarnings({ "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD" })
    int[] pendingFileDescriptors;
    private int[] tipcErrorInfo;
    private int[] tipcDestName;
    
    AncillaryDataSupport() {
        this.openReceivedFileDescriptors = Collections.synchronizedMap(new HashMap<FileDescriptor, Integer>());
        this.receivedFileDescriptors = Collections.synchronizedList(new ArrayList<FileDescriptor[]>());
        this.ancillaryReceiveBuffer = AncillaryDataSupport.EMPTY_BUFFER;
        this.pendingFileDescriptors = null;
        this.tipcErrorInfo = null;
        this.tipcDestName = null;
    }
    
    void setTipcErrorInfo(final int errorCode, final int dataLength) {
        if (errorCode == 0 && dataLength == 0) {
            this.tipcErrorInfo = null;
        }
        else {
            this.tipcErrorInfo = new int[] { errorCode, dataLength };
        }
    }
    
    int[] getTIPCErrorInfo() {
        final int[] info = this.tipcErrorInfo;
        this.tipcErrorInfo = null;
        return info;
    }
    
    void setTipcDestName(final int a, final int b, final int c) {
        if (a == 0 && b == 0 && c == 0) {
            this.tipcDestName = null;
        }
        else {
            this.tipcDestName = new int[] { a, b, c };
        }
    }
    
    int[] getTIPCDestName() {
        final int[] addr = this.tipcDestName;
        this.tipcDestName = null;
        return addr;
    }
    
    int getAncillaryReceiveBufferSize() {
        return this.ancillaryReceiveBuffer.capacity();
    }
    
    void setAncillaryReceiveBufferSize(final int size) {
        if (size == this.ancillaryReceiveBuffer.capacity()) {
            return;
        }
        if (size <= 0) {
            this.ancillaryReceiveBuffer = AncillaryDataSupport.EMPTY_BUFFER;
        }
        else {
            this.setAncillaryReceiveBufferSize0(Math.max(256, Math.min(AncillaryDataSupport.MIN_ANCBUF_LEN, size)));
        }
    }
    
    void setAncillaryReceiveBufferSize0(final int size) {
        this.ancillaryReceiveBuffer = ByteBuffer.allocateDirect(size);
    }
    
    public void ensureAncillaryReceiveBufferSize(final int minSize) {
        if (minSize <= 0) {
            return;
        }
        if (this.ancillaryReceiveBuffer.capacity() < minSize) {
            this.setAncillaryReceiveBufferSize(minSize);
        }
    }
    
    void receiveFileDescriptors(final int[] fds) throws IOException {
        if (fds == null || fds.length == 0) {
            return;
        }
        final int fdsLength = fds.length;
        final FileDescriptor[] descriptors = new FileDescriptor[fdsLength];
        for (int i = 0; i < fdsLength; ++i) {
            final FileDescriptor fdesc = new FileDescriptor();
            NativeUnixSocket.initFD(fdesc, fds[i]);
            descriptors[i] = fdesc;
            this.openReceivedFileDescriptors.put(fdesc, fds[i]);
            final Closeable cleanup = new Closeable() {
                @Override
                public void close() throws IOException {
                    AncillaryDataSupport.this.openReceivedFileDescriptors.remove(fdesc);
                }
            };
            try {
                NativeUnixSocket.attachCloseable(fdesc, cleanup);
            }
            catch (final SocketException ex) {}
        }
        this.receivedFileDescriptors.add(descriptors);
    }
    
    void clearReceivedFileDescriptors() {
        this.receivedFileDescriptors.clear();
    }
    
    FileDescriptor[] getReceivedFileDescriptors() {
        if (this.receivedFileDescriptors.isEmpty()) {
            return AncillaryDataSupport.NO_FILE_DESCRIPTORS;
        }
        final List<FileDescriptor[]> copy = new ArrayList<FileDescriptor[]>(this.receivedFileDescriptors);
        if (copy.isEmpty()) {
            return AncillaryDataSupport.NO_FILE_DESCRIPTORS;
        }
        this.receivedFileDescriptors.removeAll(copy);
        int count = 0;
        for (final FileDescriptor[] fds : copy) {
            count += fds.length;
        }
        if (count == 0) {
            return AncillaryDataSupport.NO_FILE_DESCRIPTORS;
        }
        final FileDescriptor[] oneArray = new FileDescriptor[count];
        int offset = 0;
        for (final FileDescriptor[] fds2 : copy) {
            System.arraycopy(fds2, 0, oneArray, offset, fds2.length);
            offset += fds2.length;
        }
        return oneArray;
    }
    
    void setOutboundFileDescriptors(final int[] fds) {
        this.pendingFileDescriptors = (int[])((fds == null || fds.length == 0) ? null : fds);
    }
    
    boolean hasOutboundFileDescriptors() {
        return this.pendingFileDescriptors != null;
    }
    
    void setOutboundFileDescriptors(final FileDescriptor... fdescs) throws IOException {
        int[] fds;
        if (fdescs == null || fdescs.length == 0) {
            fds = null;
        }
        else {
            final int numFdescs = fdescs.length;
            fds = new int[numFdescs];
            for (int i = 0; i < numFdescs; ++i) {
                final FileDescriptor fdesc = fdescs[i];
                fds[i] = NativeUnixSocket.getFD(fdesc);
            }
        }
        this.setOutboundFileDescriptors(fds);
    }
    
    @Override
    public void close() {
        synchronized (this.openReceivedFileDescriptors) {
            for (final FileDescriptor desc : this.openReceivedFileDescriptors.keySet()) {
                if (desc.valid()) {
                    try {
                        NativeUnixSocket.close(desc);
                    }
                    catch (final Exception ex) {}
                }
            }
        }
    }
    
    static {
        EMPTY_BUFFER = ByteBuffer.allocate(0);
        NO_FILE_DESCRIPTORS = new FileDescriptor[0];
        MIN_ANCBUF_LEN = (NativeUnixSocket.isLoaded() ? NativeUnixSocket.ancillaryBufMinLen() : 0);
    }
}
