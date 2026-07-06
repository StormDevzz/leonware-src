package sweetie.leonware.api.utils.animation.wrap.infinity;

import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.rotation.manager.Rotation;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/animation/wrap/infinity/RotationAnimation.class */
public class RotationAnimation {
    private final InfinityAnimation yaw = new InfinityAnimation();
    private final InfinityAnimation pitch = new InfinityAnimation();

    public Rotation animate(Rotation rotation, int yawSpeed, int pitchSpeed) {
        return new Rotation(this.yaw.animate(rotation.getYaw(), yawSpeed), this.pitch.animate(rotation.getPitch(), pitchSpeed));
    }

    public float getYaw() {
        return this.yaw.get();
    }

    public float getPitch() {
        return this.pitch.get();
    }

    public RotationAnimation easing(Easing easing) {
        this.yaw.easing(easing);
        this.yaw.easing(easing);
        return this;
    }
}
