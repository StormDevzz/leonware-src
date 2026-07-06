package sweetie.leonware.api.utils.rotation;

import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_238;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.rotation.manager.Rotation;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/rotation/RotationUtil.class */
public final class RotationUtil implements QuickImports {
    @Generated
    private RotationUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean inFov(class_243 vec3d, float fov) {
        Rotation rotation = rotationAt(vec3d);
        float deltaYaw = class_3532.method_15393(rotation.getYaw() - mc.field_1724.method_36454());
        float deltaPitch = class_3532.method_15393(rotation.getPitch() - mc.field_1724.method_36455());
        return Math.abs(deltaYaw) <= fov && Math.abs(deltaPitch) <= fov;
    }

    public static Rotation rotationAt(class_243 position) {
        if (position == null) {
            return Rotation.DEFAULT;
        }
        class_243 playerPos = mc.field_1724.method_19538().method_1031(0.0d, mc.field_1724.method_18381(mc.field_1724.method_18376()), 0.0d);
        float diffX = (float) (position.field_1352 - playerPos.field_1352);
        float diffY = (float) (position.field_1351 - playerPos.field_1351);
        float diffZ = (float) (position.field_1350 - playerPos.field_1350);
        float dist = (float) Math.sqrt((diffX * diffX) + (diffZ * diffZ));
        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0d);
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, dist)));
        return new Rotation(yaw, pitch);
    }

    public static Rotation fromVec2f(class_241 vector2f) {
        return new Rotation(vector2f.field_1342, vector2f.field_1343);
    }

    public static Rotation fromVec3d(class_243 vector) {
        return new Rotation(class_3532.method_15393(((float) Math.toDegrees(Math.atan2(vector.field_1350, vector.field_1352))) - 90.0f), class_3532.method_15393((float) Math.toDegrees(-Math.atan2(vector.field_1351, Math.hypot(vector.field_1352, vector.field_1350)))));
    }

    public static Rotation calculateDelta(Rotation start, Rotation end) {
        float deltaYaw = class_3532.method_15393(end.getYaw() - start.getYaw());
        float deltaPitch = class_3532.method_15393(end.getPitch() - start.getPitch());
        return new Rotation(deltaYaw, deltaPitch);
    }

    public static class_243 getSpot(class_1297 entity) {
        class_243 eye = mc.field_1724.method_33571();
        class_238 box = entity.method_5829();
        return new class_243(class_3532.method_15350(eye.field_1352, box.field_1323, box.field_1320), class_3532.method_15350(eye.field_1351, box.field_1322, box.field_1325), class_3532.method_15350(eye.field_1350, box.field_1321, box.field_1324));
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x00d9  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00eb  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static net.minecraft.class_243 rayCastBox(net.minecraft.class_1297 r9, net.minecraft.class_243 r10) {
        /*
            Method dump skipped, instruction units count: 345
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: sweetie.leonware.api.utils.rotation.RotationUtil.rayCastBox(net.minecraft.class_1297, net.minecraft.class_243):net.minecraft.class_243");
    }
}
