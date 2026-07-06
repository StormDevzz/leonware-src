package org.newsclub.net.unix;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/RAFChannelProvider.class */
final class RAFChannelProvider extends RandomAccessFile implements FileDescriptorAccess {
    private final File tempPath;
    private final FileDescriptor fdObj;
    private final FileDescriptor rafFdOrig;
    private final AtomicBoolean closed;

    private RAFChannelProvider(FileDescriptor fdObj) throws IOException {
        this(fdObj, File.createTempFile("jux", ".sock"));
    }

    private RAFChannelProvider(FileDescriptor fdObj, File tempPath) throws IOException {
        super(tempPath, "rw");
        this.rafFdOrig = new FileDescriptor();
        this.closed = new AtomicBoolean(false);
        this.tempPath = tempPath;
        if (!tempPath.delete() && tempPath.exists()) {
            if (!tempPath.delete()) {
            }
            tempPath = new File(tempPath.getParentFile(), "jux-" + UUID.randomUUID().toString() + ".sock");
            if (tempPath.exists()) {
                throw new IOException("Could not create a temporary path: " + tempPath);
            }
        }
        tempPath.deleteOnExit();
        NativeUnixSocket.ensureSupported();
        this.fdObj = fdObj;
        FileDescriptor rafFdObj = getFD();
        NativeUnixSocket.copyFileDescriptor(rafFdObj, this.rafFdOrig);
        NativeUnixSocket.copyFileDescriptor(fdObj, rafFdObj);
    }

    @Override // org.newsclub.net.unix.FileDescriptorAccess
    public FileDescriptor getFileDescriptor() {
        return this.fdObj;
    }

    @Override // java.io.RandomAccessFile, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        if (!this.closed.getAndSet(true)) {
            NativeUnixSocket.copyFileDescriptor(this.rafFdOrig, getFD());
            if (!this.tempPath.delete()) {
            }
        }
    }

    public static FileChannel getFileChannel(FileDescriptor fd) throws IOException {
        return getFileChannel0(fd);
    }

    private static FileChannel getFileChannel0(FileDescriptor fd) throws IOException {
        return new RAFChannelProvider(fd).getChannel();
    }
}
