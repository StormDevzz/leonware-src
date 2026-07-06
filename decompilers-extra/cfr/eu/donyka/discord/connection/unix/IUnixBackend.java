/*
 * Decompiled with CFR 0.152.
 */
package eu.donyka.discord.connection.unix;

import java.io.IOException;

public interface IUnixBackend {
    public void openPipe(String var1) throws IOException;

    public void closePipe() throws IOException;

    public void write(byte[] var1) throws IOException;

    public int getAvailable() throws IOException;

    public int read(byte[] var1) throws IOException;

    public boolean isConnected();
}

