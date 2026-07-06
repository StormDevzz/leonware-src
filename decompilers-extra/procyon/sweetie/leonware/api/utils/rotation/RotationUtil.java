// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.rotation;

import lombok.Generated;
import net.minecraft.class_238;
import net.minecraft.class_1297;
import net.minecraft.class_241;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import net.minecraft.class_3532;
import net.minecraft.class_243;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class RotationUtil implements QuickImports
{
    public static boolean inFov(final class_243 vec3d, final float fov) {
        final Rotation rotation = rotationAt(vec3d);
        final float deltaYaw = class_3532.method_15393(rotation.getYaw() - RotationUtil.mc.field_1724.method_36454());
        final float deltaPitch = class_3532.method_15393(rotation.getPitch() - RotationUtil.mc.field_1724.method_36455());
        return Math.abs(deltaYaw) <= fov && Math.abs(deltaPitch) <= fov;
    }
    
    public static Rotation rotationAt(final class_243 position) {
        if (position == null) {
            return Rotation.DEFAULT;
        }
        final class_243 playerPos = RotationUtil.mc.field_1724.method_19538().method_1031(0.0, (double)RotationUtil.mc.field_1724.method_18381(RotationUtil.mc.field_1724.method_18376()), 0.0);
        final float diffX = (float)(position.field_1352 - playerPos.field_1352);
        final float diffY = (float)(position.field_1351 - playerPos.field_1351);
        final float diffZ = (float)(position.field_1350 - playerPos.field_1350);
        final float dist = (float)Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, dist)));
        return new Rotation(yaw, pitch);
    }
    
    public static Rotation fromVec2f(final class_241 vector2f) {
        return new Rotation(vector2f.field_1342, vector2f.field_1343);
    }
    
    public static Rotation fromVec3d(final class_243 vector) {
        return new Rotation(class_3532.method_15393((float)Math.toDegrees(Math.atan2(vector.field_1350, vector.field_1352)) - 90.0f), class_3532.method_15393((float)Math.toDegrees(-Math.atan2(vector.field_1351, Math.hypot(vector.field_1352, vector.field_1350)))));
    }
    
    public static Rotation calculateDelta(final Rotation start, final Rotation end) {
        final float deltaYaw = class_3532.method_15393(end.getYaw() - start.getYaw());
        final float deltaPitch = class_3532.method_15393(end.getPitch() - start.getPitch());
        return new Rotation(deltaYaw, deltaPitch);
    }
    
    public static class_243 getSpot(final class_1297 entity) {
        final class_243 eye = RotationUtil.mc.field_1724.method_33571();
        final class_238 box = entity.method_5829();
        return new class_243(class_3532.method_15350(eye.field_1352, box.field_1323, box.field_1320), class_3532.method_15350(eye.field_1351, box.field_1322, box.field_1325), class_3532.method_15350(eye.field_1350, box.field_1321, box.field_1324));
    }
    
    public static class_243 rayCastBox(final class_1297 entity, final class_243 end) {
        final class_238 box = entity.method_5829();
        final class_243 start = RotationUtil.mc.method_1560().method_5836(1.0f);
        final class_243 min = new class_243(box.field_1323, box.field_1322, box.field_1321);
        final class_243 max = new class_243(box.field_1320, box.field_1325, box.field_1324);
        double tMin = -1.7976931348623157E308;
        double tMax = Double.MAX_VALUE;
        final class_243 direction = end.method_1029();
        for (int axis = 0; axis < 3; ++axis) {
            double d = 0.0;
            double minVal = 0.0;
            double maxVal = 0.0;
            double startVal = 0.0;
            switch (axis) {
                case 0: {
                    d = direction.field_1352;
                    minVal = min.field_1352;
                    maxVal = max.field_1352;
                    startVal = start.field_1352;
                    break;
                }
                case 1: {
                    d = direction.field_1351;
                    minVal = min.field_1351;
                    maxVal = max.field_1351;
                    startVal = start.field_1351;
                    break;
                }
                case 2: {
                    d = direction.field_1350;
                    minVal = min.field_1350;
                    maxVal = max.field_1350;
                    startVal = start.field_1350;
                    break;
                }
                default: {
                    continue;
                }
            }
            if (Math.abs(d) < 1.0E-7) {
                if (startVal < minVal || startVal > maxVal) {
                    return end;
                }
            }
            else {
                double t1 = (minVal - startVal) / d;
                double t2 = (maxVal - startVal) / d;
                if (t1 > t2) {
                    final double temp = t1;
                    t1 = t2;
                    t2 = temp;
                }
                tMin = Math.max(tMin, t1);
                tMax = Math.min(tMax, t2);
                if (tMin > tMax) {
                    return end;
                }
            }
        }
        final double distance = start.method_1022(end);
        if (tMin > distance || tMin < 0.0) {
            return end;
        }
        return start.method_1019(direction.method_1021(tMin));
    }
    
    @Generated
    private RotationUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
