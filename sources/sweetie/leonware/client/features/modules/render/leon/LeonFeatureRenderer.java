package sweetie.leonware.client.features.modules.render.leon;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10055;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3883;
import net.minecraft.class_3887;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_591;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.client.features.modules.render.LeonModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/leon/LeonFeatureRenderer.class */
@Environment(EnvType.CLIENT)
public class LeonFeatureRenderer extends class_3887<class_10055, class_591> {
    private final LeonHeadModel model;
    private static final class_2960 TEXTURE = class_2960.method_60655("leonware", "textures/leon/leon52.png");

    public LeonFeatureRenderer(class_3883<class_10055, class_591> context) {
        super(context);
        this.model = new LeonHeadModel();
    }

    /* JADX INFO: renamed from: render, reason: merged with bridge method [inline-methods] */
    public void method_4199(class_4587 matrices, class_4597 vertexConsumers, int light, class_10055 state, float limbAngle, float limbDistance) {
        if (shouldRender(state)) {
            matrices.method_22903();
            method_17165().field_3398.method_22703(matrices);
            matrices.method_22905(1.0f, -1.0f, 1.0f);
            this.model.render(matrices, vertexConsumers, light, TEXTURE);
            matrices.method_22909();
        }
    }

    private boolean shouldRender(class_10055 state) {
        LeonModule mod = LeonModule.getInstance();
        if (!mod.isEnabled()) {
            return false;
        }
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null || state.field_53529 == null) {
            return false;
        }
        boolean isSelf = state.field_53529.equals(mc.method_1548().method_1676());
        boolean isFriend = FriendManager.getInstance().contains(state.field_53529);
        return !mod.onlySelf.getValue().booleanValue() || isSelf || (isFriend && mod.friends.getValue().booleanValue());
    }
}
