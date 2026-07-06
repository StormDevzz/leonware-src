/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jdt.annotation.NonNull
 */
package org.newsclub.net.unix;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.jdt.annotation.NonNull;
import org.newsclub.net.unix.AFSocket;
import org.newsclub.net.unix.AFSocketCapability;
import org.newsclub.net.unix.AFSocketFactory;
import org.newsclub.net.unix.AFSocketImpl;
import org.newsclub.net.unix.AFUNIXServerSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import org.newsclub.net.unix.AFUNIXSocketChannel;
import org.newsclub.net.unix.AFUNIXSocketCredentials;
import org.newsclub.net.unix.AFUNIXSocketExtensions;
import org.newsclub.net.unix.AFUNIXSocketFactory;
import org.newsclub.net.unix.AFUNIXSocketImpl;

public final class AFUNIXSocket
extends AFSocket<AFUNIXSocketAddress>
implements AFUNIXSocketExtensions {
    private static final AFSocket.Constructor<AFUNIXSocketAddress> CONSTRUCTOR_STRICT = new AFSocket.Constructor<AFUNIXSocketAddress>(){

        @Override
        public @NonNull AFSocket<AFUNIXSocketAddress> newInstance(FileDescriptor fdObj, AFSocketFactory<AFUNIXSocketAddress> factory) throws SocketException {
            return new AFUNIXSocket(new AFUNIXSocketImpl(fdObj), factory);
        }
    };

    private AFUNIXSocket(AFSocketImpl<AFUNIXSocketAddress> impl, AFSocketFactory<AFUNIXSocketAddress> factory) throws SocketException {
        super(impl, factory);
    }

    AFUNIXSocket(FileDescriptor fd, AFSocketFactory<AFUNIXSocketAddress> factory) throws SocketException {
        this(new AFUNIXSocketImpl.Lenient(fd), factory);
    }

    protected AFUNIXSocketChannel newChannel() {
        return new AFUNIXSocketChannel(this);
    }

    public static AFUNIXSocket newInstance() throws IOException {
        return (AFUNIXSocket)AFSocket.newInstance(AFUNIXSocket::new, null);
    }

    static AFUNIXSocket newLenientInstance() throws IOException {
        return AFUNIXSocket.newInstance();
    }

    static AFUNIXSocket newInstance(FileDescriptor fdObj, int localPort, int remotePort) throws IOException {
        return (AFUNIXSocket)AFSocket.newInstance(AFUNIXSocket::new, null, fdObj, localPort, remotePort);
    }

    static AFUNIXSocket newInstance(AFUNIXSocketFactory factory) throws SocketException {
        return (AFUNIXSocket)AFSocket.newInstance(AFUNIXSocket::new, factory);
    }

    public static AFUNIXSocket newStrictInstance() throws IOException {
        return (AFUNIXSocket)AFSocket.newInstance(CONSTRUCTOR_STRICT, null);
    }

    public static AFUNIXSocket connectTo(AFUNIXSocketAddress addr) throws IOException {
        return (AFUNIXSocket)AFSocket.connectTo(AFUNIXSocket::new, addr);
    }

    @Override
    public AFUNIXSocketChannel getChannel() {
        return (AFUNIXSocketChannel)super.getChannel();
    }

    @Override
    public AFUNIXSocketCredentials getPeerCredentials() throws IOException {
        if (this.isClosed() || !this.isConnected()) {
            throw new SocketException("Not connected");
        }
        return ((AFUNIXSocketImpl)this.getAFImpl()).getPeerCredentials();
    }

    @Override
    public FileDescriptor[] getReceivedFileDescriptors() throws IOException {
        return ((AFUNIXSocketImpl)this.getAFImpl()).getReceivedFileDescriptors();
    }

    @Override
    public void clearReceivedFileDescriptors() {
        ((AFUNIXSocketImpl)this.getAFImpl()).clearReceivedFileDescriptors();
    }

    @Override
    public void setOutboundFileDescriptors(FileDescriptor ... fdescs) throws IOException {
        if (fdescs != null && fdescs.length > 0 && !this.isConnected()) {
            throw new SocketException("Not connected");
        }
        ((AFUNIXSocketImpl)this.getAFImpl()).setOutboundFileDescriptors(fdescs);
    }

    @Override
    public boolean hasOutboundFileDescriptors() {
        return ((AFUNIXSocketImpl)this.getAFImpl()).hasOutboundFileDescriptors();
    }

    public static boolean isSupported() {
        return AFSocket.isSupported() && AFSocket.supports(AFSocketCapability.CAPABILITY_UNIX_DOMAIN);
    }

    public static void main(String[] args) {
        System.out.print(AFUNIXSocket.class.getName() + ".isSupported(): ");
        System.out.flush();
        System.out.println(AFUNIXSocket.isSupported());
        for (AFSocketCapability cap : AFSocketCapability.values()) {
            System.out.print((Object)((Object)cap) + ": ");
            System.out.flush();
            System.out.println(AFSocket.supports(cap));
        }
        System.out.println();
        if (AFSocket.supports(AFSocketCapability.CAPABILITY_UNIX_DOMAIN)) {
            System.out.println("Starting mini selftest...");
            AFUNIXSocket.miniSelftest();
        } else {
            System.out.println("Skipping mini selftest; AFSocketCapability.CAPABILITY_UNIX_DOMAIN is missing");
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void miniSelftest() {
        AtomicBoolean success = new AtomicBoolean(true);
        try {
            AFUNIXSocketAddress addr = AFUNIXSocketAddress.ofNewTempFile();
            System.out.println("Using temporary address: " + addr);
            try (AFUNIXServerSocket server = addr.newBoundServerSocket();){
                Thread t = new Thread(() -> {
                    try {
                        try (AFUNIXSocket client = server.accept();){
                            System.out.println("Server accepted client connection");
                            try (AFUNIXSocketChannel chann = client.getChannel();){
                                ByteBuffer bb = ByteBuffer.allocate(64).order(ByteOrder.BIG_ENDIAN);
                                int numRead = 0;
                                while (bb.position() != 4 && numRead != -1) {
                                    numRead = ((SocketChannel)chann).read(bb);
                                }
                                if (bb.position() != 4) {
                                    throw new IOException("Unexpected number of bytes read: " + bb.position());
                                }
                                bb.flip();
                                int v = bb.getInt();
                                if (v != -1412567278) {
                                    throw new IOException("Received unexpected data from client: 0x" + Integer.toHexString(v));
                                }
                                bb.clear();
                                bb.putLong(18838586746761L);
                                bb.flip();
                                ((SocketChannel)chann).write(bb);
                            }
                        }
                        finally {
                            server.close();
                        }
                    }
                    catch (Exception e) {
                        success.set(false);
                        e.printStackTrace();
                    }
                });
                t.start();
                try (AFUNIXSocket socket = addr.newConnectedSocket();
                     DataInputStream in = new DataInputStream(socket.getInputStream());
                     DataOutputStream out = new DataOutputStream(socket.getOutputStream());){
                    out.writeInt(-1412567278);
                    out.flush();
                    long v = in.readLong();
                    if (v != 18838586746761L) {
                        throw new IOException("Received unexpected data from server: 0x" + Long.toHexString(v));
                    }
                }
                System.out.println("Data exchange succeeded");
            }
            System.out.println("mini selftest " + (success.get() ? "passed" : "failed"));
        }
        catch (Exception e) {
            try {
                success.set(false);
                e.printStackTrace();
                System.out.println("mini selftest " + (success.get() ? "passed" : "failed"));
                return;
            }
            catch (Throwable throwable) {
                System.out.println("mini selftest " + (success.get() ? "passed" : "failed"));
                throw throwable;
            }
        }
    }
}

