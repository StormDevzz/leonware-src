/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_243
 *  net.minecraft.class_304
 *  net.minecraft.class_3532
 *  net.minecraft.class_3675
 */
package sweetie.leonware.api.utils.player;

import lombok.Generated;
import net.minecraft.class_243;
import net.minecraft.class_304;
import net.minecraft.class_3532;
import net.minecraft.class_3675;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationPlan;

public final class MoveUtil
implements QuickImports {
    public static class_304[] getMovementKeys() {
        return new class_304[]{MoveUtil.mc.field_1690.field_1867, MoveUtil.mc.field_1690.field_1894, MoveUtil.mc.field_1690.field_1881, MoveUtil.mc.field_1690.field_1913, MoveUtil.mc.field_1690.field_1849, MoveUtil.mc.field_1690.field_1903};
    }

    public static void updateMovementKeys() {
        for (class_304 movementKey : MoveUtil.getMovementKeys()) {
            movementKey.method_23481(class_3675.method_15987((long)mc.method_22683().method_4490(), (int)movementKey.method_1429().method_1444()));
        }
    }

    public static boolean w() {
        return MoveUtil.mc.field_1690.field_1894.method_1434();
    }

    public static boolean s() {
        return MoveUtil.mc.field_1690.field_1881.method_1434();
    }

    public static boolean a() {
        return MoveUtil.mc.field_1690.field_1913.method_1434();
    }

    public static boolean d() {
        return MoveUtil.mc.field_1690.field_1849.method_1434();
    }

    public static boolean isMoving() {
        return MoveUtil.mc.field_1724.field_6250 != 0.0f || MoveUtil.mc.field_1724.field_6212 != 0.0f;
    }

    public static double direction(float rotationYaw, float moveForward, float moveStrafing) {
        if (moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (moveForward < 0.0f) {
            forward = -0.5f;
        }
        if (moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }

    public static double[] forward(double speed) {
        float yaw;
        RotationManager rotationManager = RotationManager.getInstance();
        Rotation rotation = rotationManager.getRotation();
        RotationPlan currentRotationPlan = rotationManager.getCurrentRotationPlan();
        float forward = MoveUtil.mc.field_1724.field_3913.field_3905;
        float strafe = MoveUtil.mc.field_1724.field_3913.field_3907;
        float f = yaw = currentRotationPlan == null ? MoveUtil.mc.field_1724.method_36454() : rotation.getYaw();
        if (forward != 0.0f) {
            if (strafe > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (strafe < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            strafe = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double cosStrafe = Math.sin(Math.toRadians(yaw + 90.0f));
        double sinStrafe = Math.cos(Math.toRadians(yaw + 90.0f));
        double x = (double)forward * speed * sinStrafe + (double)strafe * speed * cosStrafe;
        double z = (double)forward * speed * cosStrafe - (double)strafe * speed * sinStrafe;
        return new double[]{x, z};
    }

    public static void setSpeed(double speed) {
        double[] forward = MoveUtil.forward(speed);
        MoveUtil.mc.field_1724.method_18800(forward[0], MoveUtil.mc.field_1724.method_18798().field_1351, forward[1]);
    }

    public static double getSpeed() {
        return Math.sqrt(Math.pow(MoveUtil.mc.field_1724.method_18798().field_1352, 2.0) + Math.pow(MoveUtil.mc.field_1724.method_18798().field_1350, 2.0));
    }

    public static void strafe() {
        MoveUtil.strafe(MoveUtil.getSpeed());
    }

    public static void strafe(double speed) {
        if (MoveUtil.isMoving()) {
            double yaw = MoveUtil.getDirection();
            double motionX = (double)(-class_3532.method_15374((float)((float)yaw))) * speed;
            double motionZ = (double)class_3532.method_15362((float)((float)yaw)) * speed;
            MoveUtil.mc.field_1724.method_18799(new class_243(motionX, MoveUtil.mc.field_1724.method_18798().field_1351, motionZ));
        }
    }

    public static double getDirection() {
        double rotationYaw = MoveUtil.mc.field_1724.method_36454();
        if (MoveUtil.mc.field_1724.field_3913.field_3905 < 0.0f) {
            rotationYaw += 180.0;
        }
        double forward = 1.0;
        if (MoveUtil.mc.field_1724.field_3913.field_3905 < 0.0f) {
            forward = -0.5;
        } else if (MoveUtil.mc.field_1724.field_3913.field_3905 > 0.0f) {
            forward = 0.5;
        }
        if (MoveUtil.mc.field_1724.field_3913.field_3907 > 0.0f) {
            rotationYaw -= 90.0 * forward;
        }
        if (MoveUtil.mc.field_1724.field_3913.field_3907 < 0.0f) {
            rotationYaw += 90.0 * forward;
        }
        return Math.toRadians(rotationYaw);
    }

    @Generated
    private MoveUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

