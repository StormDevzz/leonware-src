// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.rotation.rotations;

import net.minecraft.class_3966;
import net.minecraft.class_3532;
import net.minecraft.class_2246;
import sweetie.leonware.api.utils.player.PlayerUtil;
import sweetie.leonware.api.utils.rotation.RaytracingUtil;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;

public class UniversalRotation extends RotationMode
{
    private final float dermoYaw;
    private final float dermoPitch;
    private final boolean spookySkeletons;
    private final boolean huly;
    private int jopa;
    private float lastYawDelta;
    private float lastPitchDelta;
    private int lastPitchChangeDirection;
    private int ticksSinceSwitchedDirection;
    
    public UniversalRotation(final float maxYawSpeed, final float maxPitchSpeed, final boolean spookySkeletons, final boolean huly) {
        super("Vulcan");
        this.jopa = 0;
        this.lastYawDelta = 0.0f;
        this.lastPitchDelta = 0.0f;
        this.lastPitchChangeDirection = 0;
        this.ticksSinceSwitchedDirection = 0;
        this.dermoYaw = maxYawSpeed;
        this.dermoPitch = maxPitchSpeed;
        this.spookySkeletons = spookySkeletons;
        this.huly = huly;
    }
    
    @Override
    public Rotation process(final Rotation currentRotation, final Rotation targetRotation, final class_243 vec3d, final class_1297 entity) {
        final Rotation angleDelta = RotationUtil.calculateDelta(currentRotation, targetRotation);
        float yawDelta = angleDelta.getYaw();
        float pitchDelta = angleDelta.getPitch();
        float maxYawSpeed = this.dermoYaw / 3.0f;
        float maxPitchSpeed = this.dermoPitch / 3.0f;
        if ((pitchDelta < 0.0f && this.lastPitchDelta > 0.0f) || (pitchDelta > 0.0f && this.lastPitchDelta < 0.0f)) {
            this.ticksSinceSwitchedDirection = 0;
        }
        else {
            ++this.ticksSinceSwitchedDirection;
        }
        final boolean invalid = this.ticksSinceSwitchedDirection == 0 && Math.abs(pitchDelta) > 5.0f;
        if (invalid) {
            --pitchDelta;
            pitchDelta *= 0.3f;
            maxPitchSpeed *= 0.4f;
        }
        if (Math.abs(pitchDelta) < 0.05f) {
            pitchDelta -= (float)(Math.random() * 0.05000000074505806 - 0.22499999403953552);
        }
        if (Math.abs(yawDelta - this.lastYawDelta) < 0.08f) {
            yawDelta -= (float)(Math.random() * 0.15000000596046448 - 0.125);
        }
        if (Math.abs(pitchDelta) < 0.01f) {
            pitchDelta -= (float)(Math.random() * 0.009999999776482582 - 0.004999999888241291);
        }
        if (Math.abs(yawDelta) > 180.25f) {
            maxYawSpeed *= 0.8f;
        }
        if (Math.abs(yawDelta) > 15.0f && Math.abs(pitchDelta) < 0.1f) {
            maxYawSpeed *= 0.7f;
        }
        if (Math.abs(yawDelta) < 0.05f && Math.abs(pitchDelta) < 0.05f) {
            maxYawSpeed *= 1.1f;
            maxPitchSpeed *= 1.1f;
        }
        if (yawDelta > 1.25f && this.lastYawDelta > 1.25f) {
            yawDelta -= this.lastYawDelta;
            maxYawSpeed *= 3.0f;
        }
        if (Math.abs(yawDelta) > 2.75f && Math.abs(pitchDelta) == 0.0f) {
            maxYawSpeed *= 0.8f;
            maxPitchSpeed *= 1.1f;
        }
        if (Math.abs(yawDelta) > 0.5f && Math.abs(pitchDelta) < 0.05f) {
            maxYawSpeed *= 0.7f;
            maxPitchSpeed *= 1.05f;
        }
        if (Math.abs(yawDelta) > 1.825f && Math.abs(pitchDelta) == 0.0f) {
            maxYawSpeed *= 0.6f;
            maxPitchSpeed *= 0.9f;
        }
        if (Math.abs(yawDelta) > 20.0f && Math.abs(pitchDelta) < 0.1f) {
            maxYawSpeed *= 0.5f;
            maxPitchSpeed *= 1.1f;
        }
        if (Math.abs(yawDelta) > 0.25f && Math.abs(pitchDelta) > 0.25f && Math.abs(pitchDelta) < 20.0f && Math.abs(yawDelta) < 20.0f) {
            maxYawSpeed *= 0.95f;
            maxPitchSpeed *= 0.85f;
        }
        if (Math.abs(yawDelta) > 0.1f && Math.abs(pitchDelta) > 0.1f && Math.abs(yawDelta) < 20.0f && Math.abs(pitchDelta) < 20.0f) {
            maxYawSpeed *= 0.9f;
            maxPitchSpeed *= 0.8f;
        }
        if (Math.abs(yawDelta) > 0.05f && Math.abs(pitchDelta) == 0.0f) {
            maxYawSpeed *= 0.8f;
            maxPitchSpeed *= 0.95f;
        }
        if (Math.abs(yawDelta) > 0.05f && Math.abs(pitchDelta) < 0.05f) {
            maxYawSpeed *= 0.85f;
            maxPitchSpeed *= 1.1f;
        }
        if (Math.abs(yawDelta) > 0.75f && Math.abs(pitchDelta) > 0.75f) {
            maxYawSpeed *= 0.8f;
            maxPitchSpeed *= 0.75f;
        }
        if (Math.abs(yawDelta) > 0.03f && Math.abs(pitchDelta) > 0.03f) {
            maxYawSpeed *= 0.9f;
            maxPitchSpeed *= 0.8f;
        }
        final int currentPitchChangeDirection = (pitchDelta > 0.0f) ? 1 : -1;
        if (this.lastPitchChangeDirection != 0 && currentPitchChangeDirection != this.lastPitchChangeDirection) {
            maxPitchSpeed *= 0.2f;
        }
        this.lastPitchChangeDirection = currentPitchChangeDirection;
        final AuraModule auraModule = AuraModule.getInstance();
        final class_3966 raytraceResult = RaytracingUtil.raytraceEntity(auraModule.getAttackDistance(), currentRotation, false);
        final boolean check = raytraceResult != null && raytraceResult.method_17782() == auraModule.target;
        final boolean checked = entity != null && PlayerUtil.hasCollisionWith(entity, 1.0f) && PlayerUtil.getBlock(0.0f, 2.0f, 0.0f) != class_2246.field_10124 && PlayerUtil.getBlock(0.0f, -1.0f, 0.0f) != class_2246.field_10124 && PlayerUtil.getBlock(0.0f, 2.0f, 0.0f) != class_2246.field_10382 && PlayerUtil.getBlock(0.0f, -1.0f, 0.0f) != class_2246.field_10382;
        if (this.spookySkeletons) {
            final int maxJopa = 20;
            final float deldeldel = this.huly ? 13.0f : 30.0f;
            if (checked) {
                pitchDelta /= deldeldel;
                yawDelta /= deldeldel;
                this.jopa = maxJopa;
            }
            if (!this.huly && !checked && check) {
                maxPitchSpeed *= 1.3f;
                maxYawSpeed *= 1.1f;
            }
            else if (!this.huly && !checked) {
                maxPitchSpeed *= 1.1f;
                maxYawSpeed *= 1.25f;
            }
            if (this.jopa-- > 0) {
                final float superJopa = Math.max(1.0f, this.jopa / (float)maxJopa * 15.0f);
                yawDelta /= superJopa;
                pitchDelta /= superJopa;
            }
        }
        this.lastYawDelta = yawDelta;
        this.lastPitchDelta = pitchDelta;
        return new Rotation(currentRotation.getYaw() + class_3532.method_15363(yawDelta, -maxYawSpeed, maxYawSpeed), currentRotation.getPitch() + class_3532.method_15363(pitchDelta, -maxPitchSpeed, maxPitchSpeed));
    }
}
