// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.net.SocketException;

public final class SocketClosedException extends SocketException
{
    private static final long serialVersionUID = 1L;
    
    public SocketClosedException() {
    }
    
    public SocketClosedException(final String msg) {
        super(msg);
    }
}
