/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.utils.animation;

import lombok.Generated;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.math.MathUtil;

public class AnimationUtil {
    private long start = 0L;
    private long duration = 0L;
    private double fromValue = 0.0;
    private double toValue = 0.0;
    private double value = 0.0;
    private Easing easing = Easing.LINEAR;

    public AnimationUtil run(float valueTo, long duration, Easing easing) {
        return this.run(valueTo, duration, easing, false);
    }

    public AnimationUtil run(double valueTo, long duration, Easing easing) {
        return this.run(valueTo, duration, easing, false);
    }

    public AnimationUtil run(double valueTo, long duration, Easing easing, boolean safe) {
        if (!this.check(safe, valueTo)) {
            this.easing = easing;
            this.duration = duration;
            this.start = System.currentTimeMillis();
            this.fromValue = this.value;
            this.toValue = valueTo;
        }
        return this;
    }

    public boolean update() {
        boolean alive = this.isAlive();
        if (alive) {
            this.value = MathUtil.interpolate(this.fromValue, this.toValue, this.easing.apply(this.calculatePart()));
        } else {
            this.start = 0L;
            this.value = this.toValue;
        }
        return alive;
    }

    public boolean isAlive() {
        return !this.isFinished();
    }

    public boolean isFinished() {
        return this.calculatePart() >= 1.0;
    }

    private double calculatePart() {
        return (double)(System.currentTimeMillis() - this.start) / (double)this.duration;
    }

    private boolean check(boolean safe, double valueTo) {
        return safe && this.isAlive() && (valueTo == this.fromValue || valueTo == this.toValue || valueTo == this.value);
    }

    @Generated
    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Generated
    public double getFromValue() {
        return this.fromValue;
    }

    @Generated
    public double getToValue() {
        return this.toValue;
    }

    @Generated
    public void setValue(double value) {
        this.value = value;
    }

    @Generated
    public double getValue() {
        return this.value;
    }

    @Generated
    public void setEasing(Easing easing) {
        this.easing = easing;
    }
}

