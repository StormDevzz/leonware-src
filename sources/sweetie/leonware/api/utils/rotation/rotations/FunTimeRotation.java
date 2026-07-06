package sweetie.leonware.api.utils.rotation.rotations;

import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/rotation/rotations/FunTimeRotation.class */
public class FunTimeRotation extends RotationMode {
    public static boolean attack;
    public static int attackedTicks = 0;
    public static int attackCount = 0;
    public static int idAttack = 0;
    public static long lastAttack = 0;
    public static float jitterUniquePower = 0.0f;
    public static boolean firstAttack = true;

    public FunTimeRotation() {
        super("FunTime");
    }

    public static void updateAttackState(boolean attack2) {
        attack = attack2;
        if (attack2) {
            attackCount++;
            if (firstAttack && attackCount >= 2) {
                firstAttack = false;
                attackCount = 0;
            }
            jitterUniquePower = MathUtil.randomInRange(5.0f, 15.0f);
            lastAttack = System.currentTimeMillis();
            idAttack = MathUtil.randomInRange(0, 1);
            attackedTicks = MathUtil.randomInRange(12, 16) * 3;
        }
    }

    @Override // sweetie.leonware.api.utils.rotation.manager.RotationMode
    public Rotation process(Rotation currentRotation, Rotation targetRotation, class_243 vec3d, class_1297 entity) {
        if (attackedTicks > 0) {
            attackedTicks = Math.max(attackedTicks - 1, 0);
        }
        boolean intensiveAttack = System.currentTimeMillis() - lastAttack < 500;
        boolean lateAttack = System.currentTimeMillis() - lastAttack > 800;
        float prevPitch = targetRotation.getPitch();
        if (!MoveUtil.isMoving() && !attack && attackedTicks > 27) {
            float prevYaw = targetRotation.getYaw();
            float jitterPower = 45.0f + jitterUniquePower;
            float jitter = firstAttack ? 0.0f : idAttack == 0 ? -jitterPower : jitterPower;
            targetRotation = new Rotation(prevYaw + jitter, prevPitch);
        }
        float swing = (float) (Math.sin(mc.field_1724.field_6012 * 0.9f) * ((double) MathUtil.randomInRange(16.8f, 17.1f)));
        float yawSpeed = 23.0f;
        if (lateAttack) {
            firstAttack = true;
            targetRotation = new Rotation(mc.field_1724.method_36454(), prevPitch);
            yawSpeed = 23.0f * 0.2f;
        }
        Rotation delta = RotationUtil.calculateDelta(currentRotation, targetRotation);
        float yawDelta = delta.getYaw();
        float pitchDelta = delta.getPitch();
        boolean glitchDelta = !attack && intensiveAttack && MoveUtil.isMoving();
        float finalDeltaYaw = glitchDelta ? swing / 3.0f : yawDelta + swing;
        float finalDeltaPitch = glitchDelta ? 0.0f : pitchDelta;
        float finalYaw = currentRotation.getYaw() + class_3532.method_15363(finalDeltaYaw, -yawSpeed, yawSpeed);
        float finalPitch = currentRotation.getPitch() + class_3532.method_15363(finalDeltaPitch, -5.0f, 5.0f);
        return new Rotation(finalYaw, finalPitch);
    }
}
