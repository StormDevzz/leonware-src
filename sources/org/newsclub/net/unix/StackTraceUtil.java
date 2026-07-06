package org.newsclub.net.unix;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/StackTraceUtil.class */
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
