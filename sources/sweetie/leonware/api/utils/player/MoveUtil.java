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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/player/MoveUtil.class */
public final class MoveUtil implements QuickImports {
    @Generated
    private MoveUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static class_304[] getMovementKeys() {
        return new class_304[]{mc.field_1690.field_1867, mc.field_1690.field_1894, mc.field_1690.field_1881, mc.field_1690.field_1913, mc.field_1690.field_1849, mc.field_1690.field_1903};
    }

    public static void updateMovementKeys() {
        for (class_304 movementKey : getMovementKeys()) {
            movementKey.method_23481(class_3675.method_15987(mc.method_22683().method_4490(), movementKey.method_1429().method_1444()));
        }
    }

    public static boolean w() {
        return mc.field_1690.field_1894.method_1434();
    }

    public static boolean s() {
        return mc.field_1690.field_1881.method_1434();
    }

    public static boolean a() {
        return mc.field_1690.field_1913.method_1434();
    }

    public static boolean d() {
        return mc.field_1690.field_1849.method_1434();
    }

    public static boolean isMoving() {
        return (mc.field_1724.field_6250 == 0.0f && mc.field_1724.field_6212 == 0.0f) ? false : true;
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
        RotationManager rotationManager = RotationManager.getInstance();
        Rotation rotation = rotationManager.getRotation();
        RotationPlan currentRotationPlan = rotationManager.getCurrentRotationPlan();
        float forward = mc.field_1724.field_3913.field_3905;
        float strafe = mc.field_1724.field_3913.field_3907;
        float yaw = currentRotationPlan == null ? mc.field_1724.method_36454() : rotation.getYaw();
        if (forward != 0.0f) {
            if (strafe > 0.0f) {
                yaw += forward > 0.0f ? -45 : 45;
            } else if (strafe < 0.0f) {
                yaw += forward > 0.0f ? 45 : -45;
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
        double x = (((double) forward) * speed * sinStrafe) + (((double) strafe) * speed * cosStrafe);
        double z = ((((double) forward) * speed) * cosStrafe) - ((((double) strafe) * speed) * sinStrafe);
        return new double[]{x, z};
    }

    public static void setSpeed(double speed) {
        double[] forward = forward(speed);
        mc.field_1724.method_18800(forward[0], mc.field_1724.method_18798().field_1351, forward[1]);
    }

    public static double getSpeed() {
        return Math.sqrt(Math.pow(mc.field_1724.method_18798().field_1352, 2.0d) + Math.pow(mc.field_1724.method_18798().field_1350, 2.0d));
    }

    public static void strafe() {
        strafe(getSpeed());
    }

    public static void strafe(double speed) {
        if (isMoving()) {
            double yaw = getDirection();
            double motionX = ((double) (-class_3532.method_15374((float) yaw))) * speed;
            double motionZ = ((double) class_3532.method_15362((float) yaw)) * speed;
            mc.field_1724.method_18799(new class_243(motionX, mc.field_1724.method_18798().field_1351, motionZ));
        }
    }

    public static double getDirection() {
        double rotationYaw = mc.field_1724.method_36454();
        if (mc.field_1724.field_3913.field_3905 < 0.0f) {
            rotationYaw += 180.0d;
        }
        double forward = 1.0d;
        if (mc.field_1724.field_3913.field_3905 < 0.0f) {
            forward = -0.5d;
        } else if (mc.field_1724.field_3913.field_3905 > 0.0f) {
            forward = 0.5d;
        }
        if (mc.field_1724.field_3913.field_3907 > 0.0f) {
            rotationYaw -= 90.0d * forward;
        }
        if (mc.field_1724.field_3913.field_3907 < 0.0f) {
            rotationYaw += 90.0d * forward;
        }
        return Math.toRadians(rotationYaw);
    }
}
