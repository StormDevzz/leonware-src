/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

public final class StackTraceUtil {
    private StackTraceUtil() {
        throw new IllegalStateException("No instances");
    }

    public static void printStackTrace(Throwable t) {
        t.printStackTrace();
    }

    public static void printStackTraceSevere(Throwable t) {
        t.printStackTrace();
    }
}

