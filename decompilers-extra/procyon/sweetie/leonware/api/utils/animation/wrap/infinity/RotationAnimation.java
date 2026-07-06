// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.animation.wrap.infinity;

import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.rotation.manager.Rotation;

public class RotationAnimation
{
    private final InfinityAnimation yaw;
    private final InfinityAnimation pitch;
    
    public RotationAnimation() {
        this.yaw = new InfinityAnimation();
        this.pitch = new InfinityAnimation();
    }
    
    public Rotation animate(final Rotation rotation, final int yawSpeed, final int pitchSpeed) {
        return new Rotation(this.yaw.animate(rotation.getYaw(), yawSpeed), this.pitch.animate(rotation.getPitch(), pitchSpeed));
    }
    
    public float getYaw() {
        return this.yaw.get();
    }
    
    public float getPitch() {
        return this.pitch.get();
    }
    
    public RotationAnimation easing(final Easing easing) {
        this.yaw.easing(easing);
        this.yaw.easing(easing);
        return this;
    }
}
