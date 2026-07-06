// 
// Decompiled by Procyon v0.6.0
// 

package eu.donyka.discord.connection.unix;

import java.io.IOException;

public interface IUnixBackend
{
    void openPipe(final String p0) throws IOException;
    
    void closePipe() throws IOException;
    
    void write(final byte[] p0) throws IOException;
    
    int getAvailable() throws IOException;
    
    int read(final byte[] p0) throws IOException;
    
    boolean isConnected();
}
