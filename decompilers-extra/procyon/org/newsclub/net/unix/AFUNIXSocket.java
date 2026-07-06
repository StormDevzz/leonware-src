// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.nio.channels.SocketChannel;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.DataInputStream;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.IOException;
import java.io.FileDescriptor;
import java.net.SocketException;

public final class AFUNIXSocket extends AFSocket<AFUNIXSocketAddress> implements AFUNIXSocketExtensions
{
    private static final Constructor<AFUNIXSocketAddress> CONSTRUCTOR_STRICT;
    
    private AFUNIXSocket(final AFSocketImpl<AFUNIXSocketAddress> impl, final AFSocketFactory<AFUNIXSocketAddress> factory) throws SocketException {
        super(impl, factory);
    }
    
    AFUNIXSocket(final FileDescriptor fd, final AFSocketFactory<AFUNIXSocketAddress> factory) throws SocketException {
        this(new AFUNIXSocketImpl.Lenient(fd), factory);
    }
    
    @Override
    protected AFUNIXSocketChannel newChannel() {
        return new AFUNIXSocketChannel(this);
    }
    
    public static AFUNIXSocket newInstance() throws IOException {
        return (AFUNIXSocket)AFSocket.newInstance(AFUNIXSocket::new, null);
    }
    
    static AFUNIXSocket newLenientInstance() throws IOException {
        return newInstance();
    }
    
    static AFUNIXSocket newInstance(final FileDescriptor fdObj, final int localPort, final int remotePort) throws IOException {
        return (AFUNIXSocket)AFSocket.newInstance(AFUNIXSocket::new, null, fdObj, localPort, remotePort);
    }
    
    static AFUNIXSocket newInstance(final AFUNIXSocketFactory factory) throws SocketException {
        return (AFUNIXSocket)AFSocket.newInstance(AFUNIXSocket::new, (AFSocketFactory<AFSocketAddress>)factory);
    }
    
    public static AFUNIXSocket newStrictInstance() throws IOException {
        return (AFUNIXSocket)AFSocket.newInstance(AFUNIXSocket.CONSTRUCTOR_STRICT, null);
    }
    
    public static AFUNIXSocket connectTo(final AFUNIXSocketAddress addr) throws IOException {
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
    public void setOutboundFileDescriptors(final FileDescriptor... fdescs) throws IOException {
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
    
    public static void main(final String[] args) {
        System.out.print(AFUNIXSocket.class.getName() + ".isSupported(): ");
        System.out.flush();
        System.out.println(isSupported());
        for (final AFSocketCapability cap : AFSocketCapability.values()) {
            System.out.print(cap + ": ");
            System.out.flush();
            System.out.println(AFSocket.supports(cap));
        }
        System.out.println();
        if (AFSocket.supports(AFSocketCapability.CAPABILITY_UNIX_DOMAIN)) {
            System.out.println("Starting mini selftest...");
            miniSelftest();
        }
        else {
            System.out.println("Skipping mini selftest; AFSocketCapability.CAPABILITY_UNIX_DOMAIN is missing");
        }
    }
    
    private static void miniSelftest() {
        final AtomicBoolean success = new AtomicBoolean(true);
        try {
            final AFUNIXSocketAddress addr = AFUNIXSocketAddress.ofNewTempFile();
            System.out.println("Using temporary address: " + addr);
            try (final AFUNIXServerSocket server = addr.newBoundServerSocket()) {
                final Thread t = new Thread(() -> {
                    try {
                        try (final AFUNIXSocket client = server.accept()) {
                            System.out.println("Server accepted client connection");
                            final SocketChannel chann = client.getChannel();
                            try {
                                final ByteBuffer bb = ByteBuffer.allocate(64).order(ByteOrder.BIG_ENDIAN);
                                for (int numRead = 0; bb.position() != 4 && numRead != -1; numRead = chann.read(bb)) {}
                                if (bb.position() != 4) {
                                    new IOException("Unexpected number of bytes read: " + bb.position());
                                    throw;
                                }
                                else {
                                    bb.flip();
                                    bb.getInt();
                                    final int n;
                                    final int v2;
                                    if ((v2 = n) != -1412567278) {
                                        new IOException("Received unexpected data from client: 0x" + Integer.toHexString(v2));
                                        throw;
                                    }
                                    else {
                                        bb.clear();
                                        bb.putLong(18838586746761L);
                                        bb.flip();
                                        chann.write(bb);
                                        if (chann != null) {
                                            chann.close();
                                        }
                                    }
                                }
                            }
                            catch (final Throwable t6) {
                                if (chann != null) {
                                    try {
                                        chann.close();
                                    }
                                    catch (final Throwable exception5) {
                                        t6.addSuppressed(exception5);
                                    }
                                }
                                throw t6;
                            }
                        }
                        finally {
                            server.close();
                        }
                    }
                    catch (final Exception e2) {
                        success.set(false);
                        e2.printStackTrace();
                    }
                    return;
                });
                t.start();
                try (final AFUNIXSocket socket = addr.newConnectedSocket();
                     final DataInputStream in = new DataInputStream(socket.getInputStream());
                     final DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
                    out.writeInt(-1412567278);
                    out.flush();
                    final long v = in.readLong();
                    if (v != 18838586746761L) {
                        throw new IOException("Received unexpected data from server: 0x" + Long.toHexString(v));
                    }
                }
                System.out.println("Data exchange succeeded");
            }
        }
        catch (final Exception e) {
            success.set(false);
            e.printStackTrace();
        }
        finally {
            System.out.println("mini selftest " + (success.get() ? "passed" : "failed"));
        }
    }
    
    static {
        CONSTRUCTOR_STRICT = new Constructor<AFUNIXSocketAddress>() {
            @Override
            public AFSocket<AFUNIXSocketAddress> newInstance(final FileDescriptor fdObj, final AFSocketFactory<AFUNIXSocketAddress> factory) throws SocketException {
                return new AFUNIXSocket(new AFUNIXSocketImpl(fdObj), factory, null);
            }
        };
    }
}
