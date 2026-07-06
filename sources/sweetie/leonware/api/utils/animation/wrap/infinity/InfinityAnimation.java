package sweetie.leonware.api.utils.animation.wrap.infinity;

import lombok.Generated;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.animation.wrap.WrapAnimation;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/animation/wrap/infinity/InfinityAnimation.class */
public class InfinityAnimation {
    private float output;
    private float endpoint;
    private Easing easing = Easing.LINEAR;
    private WrapAnimation animation = new WrapAnimation().setSize(0.0f).setSpeed(0).setForward(false).setEasing(this.easing);

    @Generated
    public WrapAnimation getAnimation() {
        return this.animation;
    }

    public float animate(float destination, int ms) {
        int ms2 = Math.max(1, ms);
        this.output = this.endpoint - this.animation.get();
        this.endpoint = destination;
        if (this.output != this.endpoint - destination) {
            this.animation = new WrapAnimation().setSize(this.endpoint - this.output).setSpeed(ms2).setForward(false).setEasing(this.easing);
        }
        return this.output;
    }

    public boolean finished() {
        return this.output == this.endpoint || this.animation.finished() || this.animation.finished(false);
    }

    public float get() {
        this.output = this.endpoint - this.animation.get();
        return this.output;
    }

    public InfinityAnimation easing(Easing easing) {
        this.easing = easing;
        return this;
    }
}
