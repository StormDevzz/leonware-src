// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.math;

import lombok.Generated;
import org.joml.Vector3fc;
import net.minecraft.class_3532;
import net.minecraft.class_1297;
import net.minecraft.class_4184;
import net.minecraft.class_742;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.jetbrains.annotations.NotNull;
import net.minecraft.class_243;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class ProjectionUtil implements QuickImports
{
    public static Vector2f project(@NotNull final class_243 vec3d) {
        return project(vec3d.method_10216(), vec3d.method_10214(), vec3d.method_10215());
    }
    
    public static Vector2f project(final double x, final double y, final double z) {
        final class_4184 camera = ProjectionUtil.mc.method_1561().field_4686;
        final class_243 cameraPos = camera.method_19326();
        Quaternionf cameraRotation = ProjectionUtil.mc.method_1561().method_24197();
        cameraRotation = cameraRotation.conjugate(new Quaternionf());
        final Vector3f result3f = new Vector3f((float)(x - cameraPos.method_10216()), (float)(y - cameraPos.method_10214()), (float)(z - cameraPos.method_10215()));
        result3f.rotate((Quaternionfc)cameraRotation);
        if (ProjectionUtil.mc.field_1690.method_42448().method_41753()) {
            final class_1297 method_1560 = ProjectionUtil.mc.method_1560();
            if (method_1560 instanceof final class_742 playerEntity) {
                calculateViewBobbing(playerEntity, result3f);
            }
        }
        final double fov = ProjectionUtil.mc.field_1773.method_3196(camera, ProjectionUtil.mc.method_61966().method_60637(true), true);
        return calculateScreenPosition(result3f, fov);
    }
    
    private static void calculateViewBobbing(final class_742 player, final Vector3f result3f) {
        final float tickDelta = ProjectionUtil.mc.method_61966().method_60637(true);
        final float f = player.field_53039 - player.field_53038;
        final float g = -(player.field_53039 + f * tickDelta);
        final float h = class_3532.method_16439(tickDelta, player.field_7505, player.field_7483);
        float rotateX = Math.abs(class_3532.method_15362(g * 3.1415927f - 0.2f) * h) * 5.0f;
        rotateX *= 0.017453292f;
        final Quaternionf xRot = new Quaternionf().setAngleAxis(rotateX, 1.0f, 0.0f, 0.0f);
        xRot.conjugate();
        result3f.rotate((Quaternionfc)xRot);
        float rotateZ = class_3532.method_15374(g * 3.1415927f) * h * 3.0f;
        rotateZ *= 0.017453292f;
        final Quaternionf zRot = new Quaternionf().setAngleAxis(rotateZ, 0.0f, 0.0f, 1.0f);
        zRot.conjugate();
        result3f.rotate((Quaternionfc)zRot);
        final Vector3f bobTranslation = new Vector3f(class_3532.method_15374(g * 3.1415927f) * h * 0.5f, 0.0f, 0.0f);
        bobTranslation.y = -bobTranslation.y;
        result3f.add((Vector3fc)bobTranslation);
    }
    
    private static Vector2f calculateScreenPosition(final Vector3f result3f, final double fov) {
        final float width = ProjectionUtil.mc.method_22683().method_4486() / 2.0f;
        final float height = ProjectionUtil.mc.method_22683().method_4502() / 2.0f;
        final float x = result3f.x;
        final float y = result3f.y;
        final float z = result3f.z;
        final float scaleFactor = height / (z * (float)Math.tan(Math.toRadians(fov / 2.0)));
        if (z < 0.0f) {
            final float screenX = -x * scaleFactor + width;
            final float screenY = y * scaleFactor + height;
            return new Vector2f(screenX, screenY);
        }
        return new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE);
    }
    
    @Generated
    private ProjectionUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
