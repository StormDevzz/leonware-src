// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.player;

import lombok.Generated;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.rotation.manager.RotationPlan;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import net.minecraft.class_3675;
import net.minecraft.class_304;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class MoveUtil implements QuickImports
{
    public static class_304[] getMovementKeys() {
        return new class_304[] { MoveUtil.mc.field_1690.field_1867, MoveUtil.mc.field_1690.field_1894, MoveUtil.mc.field_1690.field_1881, MoveUtil.mc.field_1690.field_1913, MoveUtil.mc.field_1690.field_1849, MoveUtil.mc.field_1690.field_1903 };
    }
    
    public static void updateMovementKeys() {
        for (final class_304 movementKey : getMovementKeys()) {
            movementKey.method_23481(class_3675.method_15987(MoveUtil.mc.method_22683().method_4490(), movementKey.method_1429().method_1444()));
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
    
    public static double direction(float rotationYaw, final float moveForward, final float moveStrafing) {
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
    
    public static double[] forward(final double speed) {
        final RotationManager rotationManager = RotationManager.getInstance();
        final Rotation rotation = rotationManager.getRotation();
        final RotationPlan currentRotationPlan = rotationManager.getCurrentRotationPlan();
        float forward = MoveUtil.mc.field_1724.field_3913.field_3905;
        float strafe = MoveUtil.mc.field_1724.field_3913.field_3907;
        float yaw = (currentRotationPlan == null) ? MoveUtil.mc.field_1724.method_36454() : rotation.getYaw();
        if (forward != 0.0f) {
            if (strafe > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            }
            else if (strafe < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            strafe = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double cosStrafe = Math.sin(Math.toRadians(yaw + 90.0f));
        final double sinStrafe = Math.cos(Math.toRadians(yaw + 90.0f));
        final double x = forward * speed * sinStrafe + strafe * speed * cosStrafe;
        final double z = forward * speed * cosStrafe - strafe * speed * sinStrafe;
        return new double[] { x, z };
    }
    
    public static void setSpeed(final double speed) {
        final double[] forward = forward(speed);
        MoveUtil.mc.field_1724.method_18800(forward[0], MoveUtil.mc.field_1724.method_18798().field_1351, forward[1]);
    }
    
    public static double getSpeed() {
        return Math.sqrt(Math.pow(MoveUtil.mc.field_1724.method_18798().field_1352, 2.0) + Math.pow(MoveUtil.mc.field_1724.method_18798().field_1350, 2.0));
    }
    
    public static void strafe() {
        strafe(getSpeed());
    }
    
    public static void strafe(final double speed) {
        if (isMoving()) {
            final double yaw = getDirection();
            final double motionX = -class_3532.method_15374((float)yaw) * speed;
            final double motionZ = class_3532.method_15362((float)yaw) * speed;
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
        }
        else if (MoveUtil.mc.field_1724.field_3913.field_3905 > 0.0f) {
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
