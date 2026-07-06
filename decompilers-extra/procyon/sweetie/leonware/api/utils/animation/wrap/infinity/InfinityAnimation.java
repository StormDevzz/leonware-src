// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.animation.wrap.infinity;

import lombok.Generated;
import sweetie.leonware.api.utils.animation.wrap.WrapAnimation;
import sweetie.leonware.api.utils.animation.Easing;

public class InfinityAnimation
{
    private float output;
    private float endpoint;
    private Easing easing;
    private WrapAnimation animation;
    
    public InfinityAnimation() {
        this.easing = Easing.LINEAR;
        this.animation = new WrapAnimation().setSize(0.0f).setSpeed(0).setForward(false).setEasing(this.easing);
    }
    
    public float animate(final float destination, int ms) {
        ms = Math.max(1, ms);
        this.output = this.endpoint - this.animation.get();
        this.endpoint = destination;
        if (this.output != this.endpoint - destination) {
            this.animation = new WrapAnimation().setSize(this.endpoint - this.output).setSpeed(ms).setForward(false).setEasing(this.easing);
        }
        return this.output;
    }
    
    public boolean finished() {
        return this.output == this.endpoint || this.animation.finished() || this.animation.finished(false);
    }
    
    public float get() {
        return this.output = this.endpoint - this.animation.get();
    }
    
    public InfinityAnimation easing(final Easing easing) {
        this.easing = easing;
        return this;
    }
    
    @Generated
    public WrapAnimation getAnimation() {
        return this.animation;
    }
}
