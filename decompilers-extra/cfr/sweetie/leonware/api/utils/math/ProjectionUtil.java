/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 *  net.minecraft.class_4184
 *  net.minecraft.class_742
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Quaternionf
 *  org.joml.Quaternionfc
 *  org.joml.Vector2f
 *  org.joml.Vector3f
 *  org.joml.Vector3fc
 */
package sweetie.leonware.api.utils.math;

import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_4184;
import net.minecraft.class_742;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class ProjectionUtil
implements QuickImports {
    public static Vector2f project(@NotNull class_243 vec3d) {
        return ProjectionUtil.project(vec3d.method_10216(), vec3d.method_10214(), vec3d.method_10215());
    }

    public static Vector2f project(double x, double y, double z) {
        class_1297 class_12972;
        class_4184 camera = ProjectionUtil.mc.method_1561().field_4686;
        class_243 cameraPos = camera.method_19326();
        Quaternionf cameraRotation = mc.method_1561().method_24197();
        cameraRotation = cameraRotation.conjugate(new Quaternionf());
        Vector3f result3f = new Vector3f((float)(x - cameraPos.method_10216()), (float)(y - cameraPos.method_10214()), (float)(z - cameraPos.method_10215()));
        result3f.rotate((Quaternionfc)cameraRotation);
        if (((Boolean)ProjectionUtil.mc.field_1690.method_42448().method_41753()).booleanValue() && (class_12972 = mc.method_1560()) instanceof class_742) {
            class_742 playerEntity = (class_742)class_12972;
            ProjectionUtil.calculateViewBobbing(playerEntity, result3f);
        }
        double fov = ProjectionUtil.mc.field_1773.method_3196(camera, mc.method_61966().method_60637(true), true);
        return ProjectionUtil.calculateScreenPosition(result3f, fov);
    }

    private static void calculateViewBobbing(class_742 player, Vector3f result3f) {
        float tickDelta = mc.method_61966().method_60637(true);
        float f = player.field_53039 - player.field_53038;
        float g = -(player.field_53039 + f * tickDelta);
        float h = class_3532.method_16439((float)tickDelta, (float)player.field_7505, (float)player.field_7483);
        float rotateX = Math.abs(class_3532.method_15362((float)(g * (float)Math.PI - 0.2f)) * h) * 5.0f;
        Quaternionf xRot = new Quaternionf().setAngleAxis(rotateX *= (float)Math.PI / 180, 1.0f, 0.0f, 0.0f);
        xRot.conjugate();
        result3f.rotate((Quaternionfc)xRot);
        float rotateZ = class_3532.method_15374((float)(g * (float)Math.PI)) * h * 3.0f;
        Quaternionf zRot = new Quaternionf().setAngleAxis(rotateZ *= (float)Math.PI / 180, 0.0f, 0.0f, 1.0f);
        zRot.conjugate();
        result3f.rotate((Quaternionfc)zRot);
        Vector3f bobTranslation = new Vector3f(class_3532.method_15374((float)(g * (float)Math.PI)) * h * 0.5f, 0.0f, 0.0f);
        bobTranslation.y = -bobTranslation.y;
        result3f.add((Vector3fc)bobTranslation);
    }

    private static Vector2f calculateScreenPosition(Vector3f result3f, double fov) {
        float width = (float)mc.method_22683().method_4486() / 2.0f;
        float height = (float)mc.method_22683().method_4502() / 2.0f;
        float x = result3f.x;
        float y = result3f.y;
        float z = result3f.z;
        float scaleFactor = height / (z * (float)Math.tan(Math.toRadians(fov / 2.0)));
        if (z < 0.0f) {
            float screenX = -x * scaleFactor + width;
            float screenY = y * scaleFactor + height;
            return new Vector2f(screenX, screenY);
        }
        return new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE);
    }

    @Generated
    private ProjectionUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

