// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.rotation.rotations;

import net.minecraft.class_3532;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.player.MoveUtil;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;

public class FunTimeRotation extends RotationMode
{
    public static boolean attack;
    public static int attackedTicks;
    public static int attackCount;
    public static int idAttack;
    public static long lastAttack;
    public static float jitterUniquePower;
    public static boolean firstAttack;
    
    public FunTimeRotation() {
        super("FunTime");
    }
    
    public static void updateAttackState(final boolean attack) {
        FunTimeRotation.attack = attack;
        if (attack) {
            ++FunTimeRotation.attackCount;
            if (FunTimeRotation.firstAttack && FunTimeRotation.attackCount >= 2) {
                FunTimeRotation.firstAttack = false;
                FunTimeRotation.attackCount = 0;
            }
            FunTimeRotation.jitterUniquePower = MathUtil.randomInRange(5.0f, 15.0f);
            FunTimeRotation.lastAttack = System.currentTimeMillis();
            FunTimeRotation.idAttack = MathUtil.randomInRange(0, 1);
            FunTimeRotation.attackedTicks = MathUtil.randomInRange(12, 16) * 3;
        }
    }
    
    @Override
    public Rotation process(final Rotation currentRotation, Rotation targetRotation, final class_243 vec3d, final class_1297 entity) {
        if (FunTimeRotation.attackedTicks > 0) {
            FunTimeRotation.attackedTicks = Math.max(FunTimeRotation.attackedTicks - 1, 0);
        }
        final boolean intensiveAttack = System.currentTimeMillis() - FunTimeRotation.lastAttack < 500L;
        final boolean lateAttack = System.currentTimeMillis() - FunTimeRotation.lastAttack > 800L;
        final float prevPitch = targetRotation.getPitch();
        if (!MoveUtil.isMoving() && !FunTimeRotation.attack && FunTimeRotation.attackedTicks > 27) {
            final float prevYaw = targetRotation.getYaw();
            final float jitterPower = 45.0f + FunTimeRotation.jitterUniquePower;
            final float jitter = FunTimeRotation.firstAttack ? 0.0f : ((FunTimeRotation.idAttack == 0) ? (-jitterPower) : jitterPower);
            targetRotation = new Rotation(prevYaw + jitter, prevPitch);
        }
        final float swing = (float)(Math.sin(FunTimeRotation.mc.field_1724.field_6012 * 0.9f) * MathUtil.randomInRange(16.8f, 17.1f));
        float yawSpeed = 23.0f;
        final float pitchSpeed = 5.0f;
        if (lateAttack) {
            FunTimeRotation.firstAttack = true;
            targetRotation = new Rotation(FunTimeRotation.mc.field_1724.method_36454(), prevPitch);
            yawSpeed *= 0.2f;
        }
        final Rotation delta = RotationUtil.calculateDelta(currentRotation, targetRotation);
        final float yawDelta = delta.getYaw();
        final float pitchDelta = delta.getPitch();
        final boolean glitchDelta = !FunTimeRotation.attack && intensiveAttack && MoveUtil.isMoving();
        final float finalDeltaYaw = glitchDelta ? (swing / 3.0f) : (yawDelta + swing);
        final float finalDeltaPitch = glitchDelta ? 0.0f : pitchDelta;
        final float finalYaw = currentRotation.getYaw() + class_3532.method_15363(finalDeltaYaw, -yawSpeed, yawSpeed);
        final float finalPitch = currentRotation.getPitch() + class_3532.method_15363(finalDeltaPitch, -pitchSpeed, pitchSpeed);
        return new Rotation(finalYaw, finalPitch);
    }
    
    static {
        FunTimeRotation.attackedTicks = 0;
        FunTimeRotation.attackCount = 0;
        FunTimeRotation.idAttack = 0;
        FunTimeRotation.lastAttack = 0L;
        FunTimeRotation.jitterUniquePower = 0.0f;
        FunTimeRotation.firstAttack = true;
    }
}
