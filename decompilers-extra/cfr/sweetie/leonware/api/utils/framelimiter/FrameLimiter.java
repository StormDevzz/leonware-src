/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_310
 */
package sweetie.leonware.api.utils.framelimiter;

import net.minecraft.class_310;
import sweetie.leonware.api.utils.framelimiter.IFrameCall;

public class FrameLimiter {
    private long lastHookTime = System.nanoTime();
    private int accumulatedCalls;
    private final boolean useMCFrameRate;
    private int currentFps = 0;
    private long hookIntervalNS = 0L;

    public FrameLimiter(boolean useMCFrameRate) {
        this.useMCFrameRate = useMCFrameRate;
        this.accumulatedCalls = 0;
    }

    public void execute(int fps, IFrameCall ... calls) {
        if (this.currentFps != fps) {
            this.hookIntervalNS = 1000000000L / (long)fps;
            this.currentFps = fps;
        }
        long nanoTime = System.nanoTime();
        long elapsed = nanoTime - this.lastHookTime;
        this.accumulatedCalls += (int)(elapsed / this.hookIntervalNS);
        this.lastHookTime += (long)this.accumulatedCalls * this.hookIntervalNS;
        this.accumulatedCalls = Math.min(this.accumulatedCalls, this.useMCFrameRate ? Math.min(this.currentFps, class_310.method_1551().method_47599()) : this.currentFps);
        while (this.accumulatedCalls > 0) {
            for (IFrameCall call : calls) {
                call.execute();
            }
            --this.accumulatedCalls;
        }
    }
}

