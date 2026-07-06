// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.framelimiter;

import net.minecraft.class_310;

public class FrameLimiter
{
    private long lastHookTime;
    private int accumulatedCalls;
    private final boolean useMCFrameRate;
    private int currentFps;
    private long hookIntervalNS;
    
    public FrameLimiter(final boolean useMCFrameRate) {
        this.currentFps = 0;
        this.hookIntervalNS = 0L;
        this.lastHookTime = System.nanoTime();
        this.useMCFrameRate = useMCFrameRate;
        this.accumulatedCalls = 0;
    }
    
    public void execute(final int fps, final IFrameCall... calls) {
        if (this.currentFps != fps) {
            this.hookIntervalNS = 1000000000L / fps;
            this.currentFps = fps;
        }
        final long nanoTime = System.nanoTime();
        final long elapsed = nanoTime - this.lastHookTime;
        this.accumulatedCalls += (int)(elapsed / this.hookIntervalNS);
        this.lastHookTime += this.accumulatedCalls * this.hookIntervalNS;
        this.accumulatedCalls = Math.min(this.accumulatedCalls, this.useMCFrameRate ? Math.min(this.currentFps, class_310.method_1551().method_47599()) : this.currentFps);
        while (this.accumulatedCalls > 0) {
            for (final IFrameCall call : calls) {
                call.execute();
            }
            --this.accumulatedCalls;
        }
    }
}
