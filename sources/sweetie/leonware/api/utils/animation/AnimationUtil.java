package sweetie.leonware.api.utils.animation;

import lombok.Generated;
import sweetie.leonware.api.utils.math.MathUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/animation/AnimationUtil.class */
public class AnimationUtil {
    private long start = 0;
    private long duration = 0;
    private double fromValue = 0.0d;
    private double toValue = 0.0d;
    private double value = 0.0d;
    private Easing easing = Easing.LINEAR;

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

    public AnimationUtil run(float valueTo, long duration, Easing easing) {
        return run(valueTo, duration, easing, false);
    }

    public AnimationUtil run(double valueTo, long duration, Easing easing) {
        return run(valueTo, duration, easing, false);
    }

    public AnimationUtil run(double valueTo, long duration, Easing easing, boolean safe) {
        if (!check(safe, valueTo)) {
            this.easing = easing;
            this.duration = duration;
            this.start = System.currentTimeMillis();
            this.fromValue = this.value;
            this.toValue = valueTo;
        }
        return this;
    }

    public boolean update() {
        boolean alive = isAlive();
        if (alive) {
            this.value = MathUtil.interpolate(this.fromValue, this.toValue, this.easing.apply(calculatePart()));
        } else {
            this.start = 0L;
            this.value = this.toValue;
        }
        return alive;
    }

    public boolean isAlive() {
        return !isFinished();
    }

    public boolean isFinished() {
        return calculatePart() >= 1.0d;
    }

    private double calculatePart() {
        return (System.currentTimeMillis() - this.start) / this.duration;
    }

    private boolean check(boolean safe, double valueTo) {
        return safe && isAlive() && (valueTo == this.fromValue || valueTo == this.toValue || valueTo == this.value);
    }
}
