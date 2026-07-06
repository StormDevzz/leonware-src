// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

public final class StackTraceUtil
{
    private StackTraceUtil() {
        throw new IllegalStateException("No instances");
    }
    
    public static void printStackTrace(final Throwable t) {
        t.printStackTrace();
    }
    
    public static void printStackTraceSevere(final Throwable t) {
        t.printStackTrace();
    }
}
