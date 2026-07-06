/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_2246
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 *  net.minecraft.class_3966
 */
package sweetie.leonware.api.utils.rotation.rotations;

import net.minecraft.class_1297;
import net.minecraft.class_2246;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_3966;
import sweetie.leonware.api.utils.player.PlayerUtil;
import sweetie.leonware.api.utils.rotation.RaytracingUtil;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;
import sweetie.leonware.client.features.modules.combat.AuraModule;

public class UniversalRotation
extends RotationMode {
    private final float dermoYaw;
    private final float dermoPitch;
    private final boolean spookySkeletons;
    private final boolean huly;
    private int jopa = 0;
    private float lastYawDelta = 0.0f;
    private float lastPitchDelta = 0.0f;
    private int lastPitchChangeDirection = 0;
    private int ticksSinceSwitchedDirection = 0;

    public UniversalRotation(float maxYawSpeed, float maxPitchSpeed, boolean spookySkeletons, boolean huly) {
        super("Vulcan");
        this.dermoYaw = maxYawSpeed;
        this.dermoPitch = maxPitchSpeed;
        this.spookySkeletons = spookySkeletons;
        this.huly = huly;
    }

    @Override
    public Rotation process(Rotation currentRotation, Rotation targetRotation, class_243 vec3d, class_1297 entity) {
        boolean checked;
        int currentPitchChangeDirection;
        boolean invalid;
        Rotation angleDelta = RotationUtil.calculateDelta(currentRotation, targetRotation);
        float yawDelta = angleDelta.getYaw();
        float pitchDelta = angleDelta.getPitch();
        float maxYawSpeed = this.dermoYaw / 3.0f;
        float maxPitchSpeed = this.dermoPitch / 3.0f;
        this.ticksSinceSwitchedDirection = pitchDelta < 0.0f && this.lastPitchDelta > 0.0f || pitchDelta > 0.0f && this.lastPitchDelta < 0.0f ? 0 : ++this.ticksSinceSwitchedDirection;
        boolean bl = invalid = this.ticksSinceSwitchedDirection == 0 && Math.abs(pitchDelta) > 5.0f;
        if (invalid) {
            pitchDelta -= 1.0f;
            pitchDelta *= 0.3f;
            maxPitchSpeed *= 0.4f;
        }
        if (Math.abs(pitchDelta) < 0.05f) {
            pitchDelta -= (float)(Math.random() * (double)0.05f - (double)0.225f);
        }
        if (Math.abs(yawDelta - this.lastYawDelta) < 0.08f) {
            yawDelta -= (float)(Math.random() * (double)0.15f - 0.125);
        }
        if (Math.abs(pitchDelta) < 0.01f) {
            pitchDelta -= (float)(Math.random() * (double)0.01f - (double)0.005f);
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
        int n = currentPitchChangeDirection = pitchDelta > 0.0f ? 1 : -1;
        if (this.lastPitchChangeDirection != 0 && currentPitchChangeDirection != this.lastPitchChangeDirection) {
            maxPitchSpeed *= 0.2f;
        }
        this.lastPitchChangeDirection = currentPitchChangeDirection;
        AuraModule auraModule = AuraModule.getInstance();
        class_3966 raytraceResult = RaytracingUtil.raytraceEntity(auraModule.getAttackDistance(), currentRotation, false);
        boolean check = raytraceResult != null && raytraceResult.method_17782() == auraModule.target;
        boolean bl2 = checked = entity != null && PlayerUtil.hasCollisionWith(entity, 1.0f) && PlayerUtil.getBlock(0.0f, 2.0f, 0.0f) != class_2246.field_10124 && PlayerUtil.getBlock(0.0f, -1.0f, 0.0f) != class_2246.field_10124 && PlayerUtil.getBlock(0.0f, 2.0f, 0.0f) != class_2246.field_10382 && PlayerUtil.getBlock(0.0f, -1.0f, 0.0f) != class_2246.field_10382;
        if (this.spookySkeletons) {
            float deldeldel;
            int maxJopa = 20;
            float f = deldeldel = !this.huly ? 30.0f : 13.0f;
            if (checked) {
                pitchDelta /= deldeldel;
                yawDelta /= deldeldel;
                this.jopa = maxJopa;
            }
            if (!this.huly && !checked && check) {
                maxPitchSpeed *= 1.3f;
                maxYawSpeed *= 1.1f;
            } else if (!this.huly && !checked) {
                maxPitchSpeed *= 1.1f;
                maxYawSpeed *= 1.25f;
            }
            if (this.jopa-- > 0) {
                float superJopa = Math.max(1.0f, (float)this.jopa / (float)maxJopa * 15.0f);
                yawDelta /= superJopa;
                pitchDelta /= superJopa;
            }
        }
        this.lastYawDelta = yawDelta;
        this.lastPitchDelta = pitchDelta;
        return new Rotation(currentRotation.getYaw() + class_3532.method_15363((float)yawDelta, (float)(-maxYawSpeed), (float)maxYawSpeed), currentRotation.getPitch() + class_3532.method_15363((float)pitchDelta, (float)(-maxPitchSpeed), (float)maxPitchSpeed));
    }
}

