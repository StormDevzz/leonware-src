package sweetie.leonware.client.features.modules.render.santa;

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
import sweetie.leonware.client.features.modules.render.SantaHatModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/santa/SantaHatFeatureRenderer.class */
@Environment(EnvType.CLIENT)
public class SantaHatFeatureRenderer extends class_3887<class_10055, class_591> {
    private final SantaHatModel hatModel;
    private static final class_2960 DEFAULT_TEXTURE = class_2960.method_60655("leonware", "textures/santa/santa.png");
    private static final class_2960 UKRAINE_TEXTURE = class_2960.method_60655("leonware", "textures/santa/ukraine.png");

    public SantaHatFeatureRenderer(class_3883<class_10055, class_591> context) {
        super(context);
        this.hatModel = new SantaHatModel();
    }

    /* JADX INFO: renamed from: render, reason: merged with bridge method [inline-methods] */
    public void method_4199(class_4587 matrices, class_4597 vertexConsumers, int light, class_10055 entityState, float limbAngle, float limbDistance) {
        if (shouldRender(entityState)) {
            matrices.method_22903();
            class_591 model = method_17165();
            model.field_3398.method_22703(matrices);
            matrices.method_22905(1.0f, -1.0f, 1.0f);
            class_2960 texture = getTexture();
            this.hatModel.render(matrices, vertexConsumers, light, texture);
            matrices.method_22909();
        }
    }

    private boolean shouldRender(class_10055 state) {
        SantaHatModule mod = SantaHatModule.getInstance();
        if (!mod.isEnabled()) {
            return false;
        }
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null || state.field_53529 == null) {
            return false;
        }
        String playerName = state.field_53529;
        boolean isSelf = playerName.equals(mc.method_1548().method_1676());
        boolean isFriend = FriendManager.getInstance().contains(playerName);
        return !mod.onlySelf.getValue().booleanValue() || isSelf || (isFriend && mod.friends.getValue().booleanValue());
    }

    private class_2960 getTexture() {
        SantaHatModule mod = SantaHatModule.getInstance();
        if (mod.mode.is("Ukraine")) {
            return UKRAINE_TEXTURE;
        }
        return DEFAULT_TEXTURE;
    }
}
