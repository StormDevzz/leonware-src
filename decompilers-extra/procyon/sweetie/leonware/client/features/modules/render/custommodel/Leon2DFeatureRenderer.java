// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.custommodel;

import net.minecraft.class_10017;
import sweetie.leonware.api.system.configs.FriendManager;
import net.minecraft.class_310;
import org.joml.Matrix4f;
import net.minecraft.class_4588;
import net.minecraft.class_4608;
import net.minecraft.class_1921;
import org.joml.Quaternionf;
import sweetie.leonware.client.features.modules.render.CustomModelModule;
import net.minecraft.class_4597;
import net.minecraft.class_4587;
import net.minecraft.class_3883;
import net.minecraft.class_2960;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_591;
import net.minecraft.class_10055;
import net.minecraft.class_3887;

@Environment(EnvType.CLIENT)
public class Leon2DFeatureRenderer extends class_3887<class_10055, class_591>
{
    private static final class_2960 LEON_TEXTURE;
    
    public Leon2DFeatureRenderer(final class_3883<class_10055, class_591> context, final Leon2DModel model) {
        super((class_3883)context);
    }
    
    public void render(final class_4587 matrices, final class_4597 vertexConsumers, final int light, final class_10055 state, final float limbAngle, final float limbDistance) {
        if (!this.shouldRender(state)) {
            return;
        }
        final CustomModelModule mod = CustomModelModule.getInstance();
        matrices.method_22903();
        matrices.method_22905(-1.0f, -1.0f, 1.0f);
        matrices.method_46416(0.0f, -2.0f, 0.0f);
        if (state.field_53457) {
            matrices.method_22905(0.5f, 0.5f, 0.5f);
        }
        matrices.method_46416((float)mod.leon2dX.getValue(), (float)mod.leon2dY.getValue(), (float)mod.leon2dZ.getValue());
        matrices.method_22907(new Quaternionf().rotationY((float)Math.toRadians(mod.leon2dRotateY.getValue())));
        final float halfWidth;
        final float scale = halfWidth = mod.leon2dScale.getValue();
        final float height = scale * 2.0f;
        final class_4588 buffer = vertexConsumers.getBuffer(class_1921.method_23578(Leon2DFeatureRenderer.LEON_TEXTURE));
        final Matrix4f matrix = matrices.method_23760().method_23761();
        buffer.method_22918(matrix, -halfWidth, height, 0.0f).method_1336(255, 255, 255, 255).method_22913(0.0f, 0.0f).method_22922(class_4608.field_21444).method_60803(light).method_60831(matrices.method_23760(), 0.0f, 0.0f, 1.0f);
        buffer.method_22918(matrix, -halfWidth, 0.0f, 0.0f).method_1336(255, 255, 255, 255).method_22913(0.0f, 1.0f).method_22922(class_4608.field_21444).method_60803(light).method_60831(matrices.method_23760(), 0.0f, 0.0f, 1.0f);
        buffer.method_22918(matrix, halfWidth, 0.0f, 0.0f).method_1336(255, 255, 255, 255).method_22913(1.0f, 1.0f).method_22922(class_4608.field_21444).method_60803(light).method_60831(matrices.method_23760(), 0.0f, 0.0f, 1.0f);
        buffer.method_22918(matrix, halfWidth, height, 0.0f).method_1336(255, 255, 255, 255).method_22913(1.0f, 0.0f).method_22922(class_4608.field_21444).method_60803(light).method_60831(matrices.method_23760(), 0.0f, 0.0f, 1.0f);
        matrices.method_22909();
    }
    
    private boolean shouldRender(final class_10055 state) {
        final CustomModelModule mod = CustomModelModule.getInstance();
        if (!mod.isEnabled() || !mod.model.is("Leon 2D")) {
            return false;
        }
        final class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null || state.field_53529 == null) {
            return false;
        }
        final boolean isSelf = state.field_53529.equals(mc.method_1548().method_1676());
        return isSelf || (mod.friends.getValue() && FriendManager.getInstance().contains(state.field_53529));
    }
    
    static {
        LEON_TEXTURE = class_2960.method_60655("leonware", "textures/models/leon/leon2d.png");
    }
}
