package sweetie.leonware.api.utils.framelimiter;

import net.minecraft.class_310;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/framelimiter/FrameLimiter.class */
public class FrameLimiter {
    private final boolean useMCFrameRate;
    private int currentFps = 0;
    private long hookIntervalNS = 0;
    private long lastHookTime = System.nanoTime();
    private int accumulatedCalls = 0;

    public FrameLimiter(boolean useMCFrameRate) {
        this.useMCFrameRate = useMCFrameRate;
    }

    public void execute(int fps, IFrameCall... calls) {
        if (this.currentFps != fps) {
            this.hookIntervalNS = 1000000000 / ((long) fps);
            this.currentFps = fps;
        }
        long nanoTime = System.nanoTime();
        long elapsed = nanoTime - this.lastHookTime;
        this.accumulatedCalls += (int) (elapsed / this.hookIntervalNS);
        this.lastHookTime += ((long) this.accumulatedCalls) * this.hookIntervalNS;
        this.accumulatedCalls = Math.min(this.accumulatedCalls, this.useMCFrameRate ? Math.min(this.currentFps, class_310.method_1551().method_47599()) : this.currentFps);
        while (this.accumulatedCalls > 0) {
            for (IFrameCall call : calls) {
                call.execute();
            }
            this.accumulatedCalls--;
        }
    }
}
