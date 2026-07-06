// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.net.SocketException;

public final class ConnectionResetSocketException extends SocketException
{
    private static final long serialVersionUID = 1L;
    
    public ConnectionResetSocketException() {
    }
    
    public ConnectionResetSocketException(final String msg) {
        super(msg);
    }
}
