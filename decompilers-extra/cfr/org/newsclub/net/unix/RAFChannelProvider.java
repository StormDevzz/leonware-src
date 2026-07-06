/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import org.newsclub.net.unix.FileDescriptorAccess;
import org.newsclub.net.unix.NativeUnixSocket;

final class RAFChannelProvider
extends RandomAccessFile
implements FileDescriptorAccess {
    private final File tempPath;
    private final FileDescriptor fdObj;
    private final FileDescriptor rafFdOrig = new FileDescriptor();
    private final AtomicBoolean closed = new AtomicBoolean(false);

    private RAFChannelProvider(FileDescriptor fdObj) throws IOException {
        this(fdObj, File.createTempFile("jux", ".sock"));
    }

    private RAFChannelProvider(FileDescriptor fdObj, File tempPath) throws IOException {
        super(tempPath, "rw");
        this.tempPath = tempPath;
        if (!tempPath.delete() && tempPath.exists()) {
            if (!tempPath.delete()) {
                // empty if block
            }
            if ((tempPath = new File(tempPath.getParentFile(), "jux-" + UUID.randomUUID().toString() + ".sock")).exists()) {
                throw new IOException("Could not create a temporary path: " + tempPath);
            }
        }
        tempPath.deleteOnExit();
        NativeUnixSocket.ensureSupported();
        this.fdObj = fdObj;
        FileDescriptor rafFdObj = this.getFD();
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
            if (!this.tempPath.delete()) {
                // empty if block
            }
        }
    }

    public static FileChannel getFileChannel(FileDescriptor fd) throws IOException {
        return RAFChannelProvider.getFileChannel0(fd);
    }

    private static FileChannel getFileChannel0(FileDescriptor fd) throws IOException {
        return new RAFChannelProvider(fd).getChannel();
    }
}

