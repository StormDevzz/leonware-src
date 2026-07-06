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
import org.newsclub.net.unix.AFSocket;
import org.newsclub.net.unix.AFUNIXSocketImpl;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFUNIXSocket.class */
public final class AFUNIXSocket extends AFSocket<AFUNIXSocketAddress> implements AFUNIXSocketExtensions {
    private static final AFSocket.Constructor<AFUNIXSocketAddress> CONSTRUCTOR_STRICT = new AFSocket.Constructor<AFUNIXSocketAddress>() { // from class: org.newsclub.net.unix.AFUNIXSocket.1
        @Override // org.newsclub.net.unix.AFSocket.Constructor
        public AFSocket<AFUNIXSocketAddress> newInstance(FileDescriptor fdObj, AFSocketFactory<AFUNIXSocketAddress> factory) throws SocketException {
            return new AFUNIXSocket(new AFUNIXSocketImpl(fdObj), factory);
        }
    };

    private AFUNIXSocket(AFSocketImpl<AFUNIXSocketAddress> impl, AFSocketFactory<AFUNIXSocketAddress> factory) throws SocketException {
        super(impl, factory);
    }

    AFUNIXSocket(FileDescriptor fd, AFSocketFactory<AFUNIXSocketAddress> factory) throws SocketException {
        this(new AFUNIXSocketImpl.Lenient(fd), factory);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.newsclub.net.unix.AFSocket
    public AFUNIXSocketChannel newChannel() {
        return new AFUNIXSocketChannel(this);
    }

    public static AFUNIXSocket newInstance() throws IOException {
        return (AFUNIXSocket) AFSocket.newInstance(AFUNIXSocket::new, (AFUNIXSocketFactory) null);
    }

    static AFUNIXSocket newLenientInstance() throws IOException {
        return newInstance();
    }

    static AFUNIXSocket newInstance(FileDescriptor fdObj, int localPort, int remotePort) throws IOException {
        return (AFUNIXSocket) AFSocket.newInstance(AFUNIXSocket::new, (AFUNIXSocketFactory) null, fdObj, localPort, remotePort);
    }

    static AFUNIXSocket newInstance(AFUNIXSocketFactory factory) throws SocketException {
        return (AFUNIXSocket) AFSocket.newInstance(AFUNIXSocket::new, factory);
    }

    public static AFUNIXSocket newStrictInstance() throws IOException {
        return (AFUNIXSocket) AFSocket.newInstance(CONSTRUCTOR_STRICT, (AFUNIXSocketFactory) null);
    }

    public static AFUNIXSocket connectTo(AFUNIXSocketAddress addr) throws IOException {
        return (AFUNIXSocket) AFSocket.connectTo(AFUNIXSocket::new, addr);
    }

    @Override // org.newsclub.net.unix.AFSocket, java.net.Socket
    public AFUNIXSocketChannel getChannel() {
        return (AFUNIXSocketChannel) super.getChannel();
    }

    @Override // org.newsclub.net.unix.AFUNIXSocketExtensions
    public AFUNIXSocketCredentials getPeerCredentials() throws IOException {
        if (isClosed() || !isConnected()) {
            throw new SocketException("Not connected");
        }
        return ((AFUNIXSocketImpl) getAFImpl()).getPeerCredentials();
    }

    @Override // org.newsclub.net.unix.AFUNIXSocketExtensions
    public FileDescriptor[] getReceivedFileDescriptors() throws IOException {
        return ((AFUNIXSocketImpl) getAFImpl()).getReceivedFileDescriptors();
    }

    @Override // org.newsclub.net.unix.AFUNIXSocketExtensions
    public void clearReceivedFileDescriptors() {
        ((AFUNIXSocketImpl) getAFImpl()).clearReceivedFileDescriptors();
    }

    @Override // org.newsclub.net.unix.AFUNIXSocketExtensions
    public void setOutboundFileDescriptors(FileDescriptor... fdescs) throws IOException {
        if (fdescs != null && fdescs.length > 0 && !isConnected()) {
            throw new SocketException("Not connected");
        }
        ((AFUNIXSocketImpl) getAFImpl()).setOutboundFileDescriptors(fdescs);
    }

    @Override // org.newsclub.net.unix.AFUNIXSocketExtensions
    public boolean hasOutboundFileDescriptors() {
        return ((AFUNIXSocketImpl) getAFImpl()).hasOutboundFileDescriptors();
    }

    public static boolean isSupported() {
        return AFSocket.isSupported() && AFSocket.supports(AFSocketCapability.CAPABILITY_UNIX_DOMAIN);
    }

    public static void main(String[] args) {
        System.out.print(AFUNIXSocket.class.getName() + ".isSupported(): ");
        System.out.flush();
        System.out.println(isSupported());
        for (AFSocketCapability cap : AFSocketCapability.values()) {
            System.out.print(cap + ": ");
            System.out.flush();
            System.out.println(AFSocket.supports(cap));
        }
        System.out.println();
        if (AFSocket.supports(AFSocketCapability.CAPABILITY_UNIX_DOMAIN)) {
            System.out.println("Starting mini selftest...");
            miniSelftest();
        } else {
            System.out.println("Skipping mini selftest; AFSocketCapability.CAPABILITY_UNIX_DOMAIN is missing");
        }
    }

    private static void miniSelftest() {
        AtomicBoolean success = new AtomicBoolean(true);
        try {
            try {
                AFUNIXSocketAddress addr = AFUNIXSocketAddress.ofNewTempFile();
                System.out.println("Using temporary address: " + addr);
                AFUNIXServerSocket server = addr.newBoundServerSocket();
                try {
                    Thread t = new Thread(() -> {
                        try {
                            try {
                                AFUNIXSocket client = server.accept();
                                try {
                                    System.out.println("Server accepted client connection");
                                    SocketChannel chann = client.getChannel();
                                    try {
                                        ByteBuffer bb = ByteBuffer.allocate(64).order(ByteOrder.BIG_ENDIAN);
                                        for (int numRead = 0; bb.position() != 4 && numRead != -1; numRead = chann.read(bb)) {
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
                                        chann.write(bb);
                                        if (chann != null) {
                                            chann.close();
                                        }
                                        if (client != null) {
                                            client.close();
                                        }
                                        server.close();
                                    } catch (Throwable th) {
                                        if (chann != null) {
                                            try {
                                                chann.close();
                                            } catch (Throwable th2) {
                                                th.addSuppressed(th2);
                                            }
                                        }
                                        throw th;
                                    }
                                } catch (Throwable th3) {
                                    if (client != null) {
                                        try {
                                            client.close();
                                        } catch (Throwable th4) {
                                            th3.addSuppressed(th4);
                                        }
                                    }
                                    throw th3;
                                }
                            } catch (Throwable th5) {
                                server.close();
                                throw th5;
                            }
                        } catch (Exception e) {
                            success.set(false);
                            e.printStackTrace();
                        }
                    });
                    t.start();
                    AFUNIXSocket socket = addr.newConnectedSocket();
                    try {
                        DataInputStream in = new DataInputStream(socket.getInputStream());
                        try {
                            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                            try {
                                out.writeInt(-1412567278);
                                out.flush();
                                long v = in.readLong();
                                if (v != 18838586746761L) {
                                    throw new IOException("Received unexpected data from server: 0x" + Long.toHexString(v));
                                }
                                out.close();
                                in.close();
                                if (socket != null) {
                                    socket.close();
                                }
                                System.out.println("Data exchange succeeded");
                                if (server != null) {
                                    server.close();
                                }
                                System.out.println("mini selftest " + (success.get() ? "passed" : "failed"));
                            } catch (Throwable th) {
                                try {
                                    out.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                                throw th;
                            }
                        } catch (Throwable th3) {
                            try {
                                in.close();
                            } catch (Throwable th4) {
                                th3.addSuppressed(th4);
                            }
                            throw th3;
                        }
                    } catch (Throwable th5) {
                        if (socket != null) {
                            try {
                                socket.close();
                            } catch (Throwable th6) {
                                th5.addSuppressed(th6);
                            }
                        }
                        throw th5;
                    }
                } catch (Throwable th7) {
                    if (server != null) {
                        try {
                            server.close();
                        } catch (Throwable th8) {
                            th7.addSuppressed(th8);
                        }
                    }
                    throw th7;
                }
            } catch (Exception e) {
                success.set(false);
                e.printStackTrace();
                System.out.println("mini selftest " + (success.get() ? "passed" : "failed"));
            }
        } catch (Throwable th9) {
            System.out.println("mini selftest " + (success.get() ? "passed" : "failed"));
            throw th9;
        }
    }
}
