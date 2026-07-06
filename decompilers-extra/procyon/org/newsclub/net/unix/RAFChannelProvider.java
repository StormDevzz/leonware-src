// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.nio.channels.FileChannel;
import java.util.UUID;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.FileDescriptor;
import java.io.File;
import java.io.RandomAccessFile;

final class RAFChannelProvider extends RandomAccessFile implements FileDescriptorAccess
{
    private final File tempPath;
    private final FileDescriptor fdObj;
    private final FileDescriptor rafFdOrig;
    private final AtomicBoolean closed;
    
    private RAFChannelProvider(final FileDescriptor fdObj) throws IOException {
        this(fdObj, File.createTempFile("jux", ".sock"));
    }
    
    private RAFChannelProvider(final FileDescriptor fdObj, File tempPath) throws IOException {
        super(tempPath, "rw");
        this.rafFdOrig = new FileDescriptor();
        this.closed = new AtomicBoolean(false);
        this.tempPath = tempPath;
        if (!tempPath.delete() && tempPath.exists()) {
            if (!tempPath.delete()) {}
            tempPath = new File(tempPath.getParentFile(), "jux-" + UUID.randomUUID().toString() + ".sock");
            if (tempPath.exists()) {
                throw new IOException("Could not create a temporary path: " + tempPath);
            }
        }
        tempPath.deleteOnExit();
        NativeUnixSocket.ensureSupported();
        this.fdObj = fdObj;
        final FileDescriptor rafFdObj = this.getFD();
        NativeUnixSocket.copyFileDescriptor(rafFdObj, this.rafFdOrig);
        NativeUnixSocket.copyFileDescriptor(fdObj, rafFdObj);
    }
    
    @Override
    public FileDescriptor getFileDescriptor() {
        return this.fdObj;
    }
    
    @Override
    public synchronized void close() throws IOException {
        if (!this.closed.getAndSet(true)) {
            NativeUnixSocket.copyFileDescriptor(this.rafFdOrig, this.getFD());
            if (!this.tempPath.delete()) {}
        }
    }
    
    public static FileChannel getFileChannel(final FileDescriptor fd) throws IOException {
        return getFileChannel0(fd);
    }
    
    private static FileChannel getFileChannel0(final FileDescriptor fd) throws IOException {
        return new RAFChannelProvider(fd).getChannel();
    }
}
