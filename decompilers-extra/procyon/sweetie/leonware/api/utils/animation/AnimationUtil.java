// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.animation;

import lombok.Generated;
import sweetie.leonware.api.utils.math.MathUtil;

public class AnimationUtil
{
    private long start;
    private long duration;
    private double fromValue;
    private double toValue;
    private double value;
    private Easing easing;
    
    public AnimationUtil() {
        this.start = 0L;
        this.duration = 0L;
        this.fromValue = 0.0;
        this.toValue = 0.0;
        this.value = 0.0;
        this.easing = Easing.LINEAR;
    }
    
    public AnimationUtil run(final float valueTo, final long duration, final Easing easing) {
        return this.run(valueTo, duration, easing, false);
    }
    
    public AnimationUtil run(final double valueTo, final long duration, final Easing easing) {
        return this.run(valueTo, duration, easing, false);
    }
    
    public AnimationUtil run(final double valueTo, final long duration, final Easing easing, final boolean safe) {
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
        final boolean alive = this.isAlive();
        if (alive) {
            this.value = MathUtil.interpolate(this.fromValue, this.toValue, this.easing.apply(this.calculatePart()));
        }
        else {
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
        return (System.currentTimeMillis() - this.start) / (double)this.duration;
    }
    
    private boolean check(final boolean safe, final double valueTo) {
        return safe && this.isAlive() && (valueTo == this.fromValue || valueTo == this.toValue || valueTo == this.value);
    }
    
    @Generated
    public void setDuration(final long duration) {
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
    public void setValue(final double value) {
        this.value = value;
    }
    
    @Generated
    public double getValue() {
        return this.value;
    }
    
    @Generated
    public void setEasing(final Easing easing) {
        this.easing = easing;
    }
}
