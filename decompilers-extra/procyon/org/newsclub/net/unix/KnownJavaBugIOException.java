// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.IOException;

public class KnownJavaBugIOException extends IOException
{
    private static final long serialVersionUID = 1L;
    
    public KnownJavaBugIOException() {
    }
    
    public KnownJavaBugIOException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public KnownJavaBugIOException(final String message) {
        super(message);
    }
    
    public KnownJavaBugIOException(final Throwable cause) {
        super(cause);
    }
}
