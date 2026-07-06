package eu.donyka.discord.connection.unix;

import java.io.IOException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:eu/donyka/discord/connection/unix/IUnixBackend.class */
public interface IUnixBackend {
    void openPipe(String str) throws IOException;

    void closePipe() throws IOException;

    void write(byte[] bArr) throws IOException;

    int getAvailable() throws IOException;

    int read(byte[] bArr) throws IOException;

    boolean isConnected();
}
