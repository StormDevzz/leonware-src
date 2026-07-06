// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.client;

import lombok.Generated;

public final class ThreadManager
{
    public static Thread run(final Runnable runnable) {
        final Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }
    
    @Generated
    private ThreadManager() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
