// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.animation.wrap;

import lombok.Generated;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.math.TimerUtil;

public class WrapAnimation
{
    private final TimerUtil timer;
    private int speed;
    private double size;
    private boolean forward;
    private Easing easing;
    
    public WrapAnimation() {
        this.timer = new TimerUtil();
        this.size = 1.0;
    }
    
    public boolean finished(final boolean forward) {
        return this.timer.finished(this.speed) && !(forward ? (!this.forward) : this.forward);
    }
    
    public boolean finished() {
        return this.timer.finished(this.speed) && this.forward;
    }
    
    public WrapAnimation setForward(final boolean forward) {
        if (this.forward != forward) {
            this.forward = forward;
            this.timer.setMillis((long)(System.currentTimeMillis() - (this.size - Math.min(this.size, (double)this.timer.getElapsedTime()))));
        }
        return this;
    }
    
    public WrapAnimation finish() {
        this.timer.setMillis(System.currentTimeMillis() - this.speed);
        return this;
    }
    
    public WrapAnimation setEasing(final Easing easing) {
        this.easing = easing;
        return this;
    }
    
    public WrapAnimation setSpeed(final int speed) {
        this.speed = speed;
        return this;
    }
    
    public WrapAnimation setSize(final float size) {
        this.size = size;
        return this;
    }
    
    public float getLinear() {
        if (this.forward) {
            if (this.timer.finished(this.speed)) {
                return (float)this.size;
            }
            return (float)(this.timer.getElapsedTime() / (double)this.speed * this.size);
        }
        else {
            if (this.timer.finished(this.speed)) {
                return 0.0f;
            }
            return (float)((1.0 - this.timer.getElapsedTime() / (double)this.speed) * this.size);
        }
    }
    
    public float get() {
        if (this.forward) {
            if (this.timer.finished(this.speed)) {
                return (float)this.size;
            }
            return (float)(this.easing.apply(this.timer.getElapsedTime() / (double)this.speed) * this.size);
        }
        else {
            if (this.timer.finished(this.speed)) {
                return 0.0f;
            }
            return (float)((1.0 - this.easing.apply(this.timer.getElapsedTime() / (double)this.speed)) * this.size);
        }
    }
    
    public float reversed() {
        return 1.0f - this.get();
    }
    
    public void reset() {
        this.timer.reset();
    }
    
    public void kuni() {
        if (this.finished()) {
            this.setForward(false);
        }
        else if (this.finished(false)) {
            this.setForward(true);
        }
    }
    
    @Generated
    public TimerUtil getTimer() {
        return this.timer;
    }
    
    @Generated
    public int getSpeed() {
        return this.speed;
    }
    
    @Generated
    public double getSize() {
        return this.size;
    }
    
    @Generated
    public Easing getEasing() {
        return this.easing;
    }
    
    @Generated
    public boolean isForward() {
        return this.forward;
    }
}
