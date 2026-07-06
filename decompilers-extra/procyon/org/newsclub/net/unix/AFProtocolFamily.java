// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.IOException;
import java.net.ProtocolFamily;

public interface AFProtocolFamily extends ProtocolFamily
{
    AFDatagramChannel<?> openDatagramChannel() throws IOException;
    
    AFServerSocketChannel<?> openServerSocketChannel() throws IOException;
    
    AFSocketChannel<?> openSocketChannel() throws IOException;
}
