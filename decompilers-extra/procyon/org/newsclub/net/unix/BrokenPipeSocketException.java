// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.net.SocketException;

public final class BrokenPipeSocketException extends SocketException
{
    private static final long serialVersionUID = 1L;
    
    public BrokenPipeSocketException() {
    }
    
    public BrokenPipeSocketException(final String msg) {
        super(msg);
    }
}
