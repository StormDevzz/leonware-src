// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.net.SocketException;

public class InvalidSocketException extends SocketException
{
    private static final long serialVersionUID = 1L;
    
    public InvalidSocketException() {
    }
    
    public InvalidSocketException(final String msg) {
        super(msg);
    }
}
