/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 */
package sweetie.leonware.api.utils.rotation.rotations;

import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;

public class FunTimeRotation
extends RotationMode {
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

    public static void updateAttackState(boolean attack) {
        FunTimeRotation.attack = attack;
        if (attack) {
            if (firstAttack && ++attackCount >= 2) {
                firstAttack = false;
                attackCount = 0;
            }
            jitterUniquePower = MathUtil.randomInRange(5.0f, 15.0f);
            lastAttack = System.currentTimeMillis();
            idAttack = MathUtil.randomInRange(0, 1);
            attackedTicks = MathUtil.randomInRange(12, 16) * 3;
        }
    }

    @Override
    public Rotation process(Rotation currentRotation, Rotation targetRotation, class_243 vec3d, class_1297 entity) {
        if (attackedTicks > 0) {
            attackedTicks = Math.max(attackedTicks - 1, 0);
        }
        boolean intensiveAttack = System.currentTimeMillis() - lastAttack < 500L;
        boolean lateAttack = System.currentTimeMillis() - lastAttack > 800L;
        float prevPitch = targetRotation.getPitch();
        if (!MoveUtil.isMoving() && !attack && attackedTicks > 27) {
            float prevYaw = targetRotation.getYaw();
            float jitterPower = 45.0f + jitterUniquePower;
            float jitter = firstAttack ? 0.0f : (idAttack == 0 ? -jitterPower : jitterPower);
            targetRotation = new Rotation(prevYaw + jitter, prevPitch);
        }
        float swing = (float)(Math.sin((float)FunTimeRotation.mc.field_1724.field_6012 * 0.9f) * (double)MathUtil.randomInRange(16.8f, 17.1f));
        float yawSpeed = 23.0f;
        float pitchSpeed = 5.0f;
        if (lateAttack) {
            firstAttack = true;
            targetRotation = new Rotation(FunTimeRotation.mc.field_1724.method_36454(), prevPitch);
            yawSpeed *= 0.2f;
        }
        Rotation delta = RotationUtil.calculateDelta(currentRotation, targetRotation);
        float yawDelta = delta.getYaw();
        float pitchDelta = delta.getPitch();
        boolean glitchDelta = !attack && intensiveAttack && MoveUtil.isMoving();
        float finalDeltaYaw = glitchDelta ? swing / 3.0f : yawDelta + swing;
        float finalDeltaPitch = glitchDelta ? 0.0f : pitchDelta;
        float finalYaw = currentRotation.getYaw() + class_3532.method_15363((float)finalDeltaYaw, (float)(-yawSpeed), (float)yawSpeed);
        float finalPitch = currentRotation.getPitch() + class_3532.method_15363((float)finalDeltaPitch, (float)(-pitchSpeed), (float)pitchSpeed);
        return new Rotation(finalYaw, finalPitch);
    }

    static {
        attackedTicks = 0;
        attackCount = 0;
        idAttack = 0;
        lastAttack = 0L;
        jitterUniquePower = 0.0f;
        firstAttack = true;
    }
}

