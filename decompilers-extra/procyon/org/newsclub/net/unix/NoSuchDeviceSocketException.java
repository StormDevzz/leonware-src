// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

public class NoSuchDeviceSocketException extends InvalidSocketException
{
    private static final long serialVersionUID = 1L;
    
    public NoSuchDeviceSocketException() {
    }
    
    public NoSuchDeviceSocketException(final String msg) {
        super(msg);
    }
}
