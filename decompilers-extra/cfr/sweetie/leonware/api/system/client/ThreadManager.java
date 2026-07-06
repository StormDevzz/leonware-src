/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.system.client;

import lombok.Generated;

public final class ThreadManager {
    public static Thread run(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    @Generated
    private ThreadManager() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

