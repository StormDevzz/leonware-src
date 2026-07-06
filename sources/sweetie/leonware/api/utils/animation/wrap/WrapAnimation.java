package sweetie.leonware.api.utils.animation.wrap;

import lombok.Generated;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.math.TimerUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/animation/wrap/WrapAnimation.class */
public class WrapAnimation {
    private int speed;
    private boolean forward;
    private Easing easing;
    private final TimerUtil timer = new TimerUtil();
    private double size = 1.0d;

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
    public boolean isForward() {
        return this.forward;
    }

    @Generated
    public Easing getEasing() {
        return this.easing;
    }

    public boolean finished(boolean forward) {
        return this.timer.finished((long) this.speed) && (!forward ? this.forward : !this.forward);
    }

    public boolean finished() {
        return this.timer.finished((long) this.speed) && this.forward;
    }

    public WrapAnimation setForward(boolean forward) {
        if (this.forward != forward) {
            this.forward = forward;
            this.timer.setMillis((long) (System.currentTimeMillis() - (this.size - Math.min(this.size, this.timer.getElapsedTime()))));
        }
        return this;
    }

    public WrapAnimation finish() {
        this.timer.setMillis(System.currentTimeMillis() - ((long) this.speed));
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
                return (float) this.size;
            }
            return (float) ((this.timer.getElapsedTime() / ((double) this.speed)) * this.size);
        }
        if (this.timer.finished(this.speed)) {
            return 0.0f;
        }
        return (float) ((1.0d - (this.timer.getElapsedTime() / ((double) this.speed))) * this.size);
    }

    public float get() {
        if (this.forward) {
            if (this.timer.finished(this.speed)) {
                return (float) this.size;
            }
            return (float) (this.easing.apply(this.timer.getElapsedTime() / ((double) this.speed)) * this.size);
        }
        if (this.timer.finished(this.speed)) {
            return 0.0f;
        }
        return (float) ((1.0d - this.easing.apply(this.timer.getElapsedTime() / ((double) this.speed))) * this.size);
    }

    public float reversed() {
        return 1.0f - get();
    }

    public void reset() {
        this.timer.reset();
    }

    public void kuni() {
        if (finished()) {
            setForward(false);
        } else if (finished(false)) {
            setForward(true);
        }
    }
}
