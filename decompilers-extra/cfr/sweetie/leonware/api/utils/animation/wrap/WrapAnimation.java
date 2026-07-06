/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.utils.animation.wrap;

import lombok.Generated;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.math.TimerUtil;

public class WrapAnimation {
    private final TimerUtil timer = new TimerUtil();
    private int speed;
    private double size = 1.0;
    private boolean forward;
    private Easing easing;

    public boolean finished(boolean forward) {
        return this.timer.finished(this.speed) && (forward ? this.forward : !this.forward);
    }

    public boolean finished() {
        return this.timer.finished(this.speed) && this.forward;
    }

    public WrapAnimation setForward(boolean forward) {
        if (this.forward != forward) {
            this.forward = forward;
            this.timer.setMillis((long)((double)System.currentTimeMillis() - (this.size - Math.min(this.size, (double)this.timer.getElapsedTime()))));
        }
        return this;
    }

    public WrapAnimation finish() {
        this.timer.setMillis(System.currentTimeMillis() - (long)this.speed);
        return this;
    }

    public WrapAnimation setEasing(Easing easing) {
        this.easing = easing;
        return this;
    }

    public WrapAnimation setSpeed(int speed) {
        this.speed = speed;
        return this;
    }

    public WrapAnimation setSize(float size) {
        this.size = size;
        return this;
    }

    public float getLinear() {
        if (this.forward) {
            if (this.timer.finished(this.speed)) {
                return (float)this.size;
            }
            return (float)((double)this.timer.getElapsedTime() / (double)this.speed * this.size);
        }
        if (this.timer.finished(this.speed)) {
            return 0.0f;
        }
        return (float)((1.0 - (double)this.timer.getElapsedTime() / (double)this.speed) * this.size);
    }

    public float get() {
        if (this.forward) {
            if (this.timer.finished(this.speed)) {
                return (float)this.size;
            }
            return (float)(this.easing.apply((double)this.timer.getElapsedTime() / (double)this.speed) * this.size);
        }
        if (this.timer.finished(this.speed)) {
            return 0.0f;
        }
        return (float)((1.0 - this.easing.apply((double)this.timer.getElapsedTime() / (double)this.speed)) * this.size);
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
        } else if (this.finished(false)) {
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

