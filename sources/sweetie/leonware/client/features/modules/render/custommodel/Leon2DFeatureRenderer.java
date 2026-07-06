package sweetie.leonware.client.features.modules.render.custommodel;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10055;
import net.minecraft.class_1921;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3883;
import net.minecraft.class_3887;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597;
import net.minecraft.class_4608;
import net.minecraft.class_591;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.client.features.modules.render.CustomModelModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/custommodel/Leon2DFeatureRenderer.class */
@Environment(EnvType.CLIENT)
public class Leon2DFeatureRenderer extends class_3887<class_10055, class_591> {
    private static final class_2960 LEON_TEXTURE = class_2960.method_60655("leonware", "textures/models/leon/leon2d.png");

    public Leon2DFeatureRenderer(class_3883<class_10055, class_591> context, Leon2DModel model) {
        super(context);
    }

    /* JADX INFO: renamed from: render, reason: merged with bridge method [inline-methods] */
    public void method_4199(class_4587 matrices, class_4597 vertexConsumers, int light, class_10055 state, float limbAngle, float limbDistance) {
        if (shouldRender(state)) {
            CustomModelModule mod = CustomModelModule.getInstance();
            matrices.method_22903();
            matrices.method_22905(-1.0f, -1.0f, 1.0f);
            matrices.method_46416(0.0f, -2.0f, 0.0f);
            if (state.field_53457) {
                matrices.method_22905(0.5f, 0.5f, 0.5f);
            }
            matrices.method_46416(mod.leon2dX.getValue().floatValue(), mod.leon2dY.getValue().floatValue(), mod.leon2dZ.getValue().floatValue());
            matrices.method_22907(new Quaternionf().rotationY((float) Math.toRadians(mod.leon2dRotateY.getValue().floatValue())));
            float scale = mod.leon2dScale.getValue().floatValue();
            float height = scale * 2.0f;
            class_4588 buffer = vertexConsumers.getBuffer(class_1921.method_23578(LEON_TEXTURE));
            Matrix4f matrix = matrices.method_23760().method_23761();
            buffer.method_22918(matrix, -scale, height, 0.0f).method_1336(255, 255, 255, 255).method_22913(0.0f, 0.0f).method_22922(class_4608.field_21444).method_60803(light).method_60831(matrices.method_23760(), 0.0f, 0.0f, 1.0f);
            buffer.method_22918(matrix, -scale, 0.0f, 0.0f).method_1336(255, 255, 255, 255).method_22913(0.0f, 1.0f).method_22922(class_4608.field_21444).method_60803(light).method_60831(matrices.method_23760(), 0.0f, 0.0f, 1.0f);
            buffer.method_22918(matrix, scale, 0.0f, 0.0f).method_1336(255, 255, 255, 255).method_22913(1.0f, 1.0f).method_22922(class_4608.field_21444).method_60803(light).method_60831(matrices.method_23760(), 0.0f, 0.0f, 1.0f);
            buffer.method_22918(matrix, scale, height, 0.0f).method_1336(255, 255, 255, 255).method_22913(1.0f, 0.0f).method_22922(class_4608.field_21444).method_60803(light).method_60831(matrices.method_23760(), 0.0f, 0.0f, 1.0f);
            matrices.method_22909();
        }
    }

    private boolean shouldRender(class_10055 state) {
        CustomModelModule mod = CustomModelModule.getInstance();
        if (!mod.isEnabled() || !mod.model.is("Leon 2D")) {
            return false;
        }
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null || state.field_53529 == null) {
            return false;
        }
        boolean isSelf = state.field_53529.equals(mc.method_1548().method_1676());
        if (isSelf) {
            return true;
        }
        return mod.friends.getValue().booleanValue() && FriendManager.getInstance().contains(state.field_53529);
    }
}
