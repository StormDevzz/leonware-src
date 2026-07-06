// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.santa;

import net.minecraft.class_10017;
import sweetie.leonware.api.system.configs.FriendManager;
import net.minecraft.class_310;
import sweetie.leonware.client.features.modules.render.SantaHatModule;
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
public class SantaHatFeatureRenderer extends class_3887<class_10055, class_591>
{
    private final SantaHatModel hatModel;
    private static final class_2960 DEFAULT_TEXTURE;
    private static final class_2960 UKRAINE_TEXTURE;
    
    public SantaHatFeatureRenderer(final class_3883<class_10055, class_591> context) {
        super((class_3883)context);
        this.hatModel = new SantaHatModel();
    }
    
    public void render(final class_4587 matrices, final class_4597 vertexConsumers, final int light, final class_10055 entityState, final float limbAngle, final float limbDistance) {
        if (this.shouldRender(entityState)) {
            matrices.method_22903();
            final class_591 model = (class_591)this.method_17165();
            model.field_3398.method_22703(matrices);
            matrices.method_22905(1.0f, -1.0f, 1.0f);
            final class_2960 texture = this.getTexture();
            this.hatModel.render(matrices, vertexConsumers, light, texture);
            matrices.method_22909();
        }
    }
    
    private boolean shouldRender(final class_10055 state) {
        final SantaHatModule mod = SantaHatModule.getInstance();
        if (!mod.isEnabled()) {
            return false;
        }
        final class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null || state.field_53529 == null) {
            return false;
        }
        final String playerName = state.field_53529;
        final boolean isSelf = playerName.equals(mc.method_1548().method_1676());
        final boolean isFriend = FriendManager.getInstance().contains(playerName);
        return !mod.onlySelf.getValue() || isSelf || (isFriend && mod.friends.getValue());
    }
    
    private class_2960 getTexture() {
        final SantaHatModule mod = SantaHatModule.getInstance();
        if (mod.mode.is("Ukraine")) {
            return SantaHatFeatureRenderer.UKRAINE_TEXTURE;
        }
        return SantaHatFeatureRenderer.DEFAULT_TEXTURE;
    }
    
    static {
        DEFAULT_TEXTURE = class_2960.method_60655("leonware", "textures/santa/santa.png");
        UKRAINE_TEXTURE = class_2960.method_60655("leonware", "textures/santa/ukraine.png");
    }
}
