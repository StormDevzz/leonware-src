package sweetie.leonware.api.system.client;

import lombok.Generated;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/client/ThreadManager.class */
public final class ThreadManager {
    @Generated
    private ThreadManager() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Thread run(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }
}
