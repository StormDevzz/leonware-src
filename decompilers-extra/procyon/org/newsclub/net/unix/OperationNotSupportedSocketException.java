// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

public class OperationNotSupportedSocketException extends InvalidSocketException
{
    private static final long serialVersionUID = 1L;
    
    public OperationNotSupportedSocketException() {
    }
    
    public OperationNotSupportedSocketException(final String msg) {
        super(msg);
    }
}
