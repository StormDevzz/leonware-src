// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.leon;

import net.minecraft.class_10017;
import sweetie.leonware.api.system.configs.FriendManager;
import net.minecraft.class_310;
import sweetie.leonware.client.features.modules.render.LeonModule;
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
public class LeonFeatureRenderer extends class_3887<class_10055, class_591>
{
    private final LeonHeadModel model;
    private static final class_2960 TEXTURE;
    
    public LeonFeatureRenderer(final class_3883<class_10055, class_591> context) {
        super((class_3883)context);
        this.model = new LeonHeadModel();
    }
    
    public void render(final class_4587 matrices, final class_4597 vertexConsumers, final int light, final class_10055 state, final float limbAngle, final float limbDistance) {
        if (!this.shouldRender(state)) {
            return;
        }
        matrices.method_22903();
        ((class_591)this.method_17165()).field_3398.method_22703(matrices);
        matrices.method_22905(1.0f, -1.0f, 1.0f);
        this.model.render(matrices, vertexConsumers, light, LeonFeatureRenderer.TEXTURE);
        matrices.method_22909();
    }
    
    private boolean shouldRender(final class_10055 state) {
        final LeonModule mod = LeonModule.getInstance();
        if (!mod.isEnabled()) {
            return false;
        }
        final class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null || state.field_53529 == null) {
            return false;
        }
        final boolean isSelf = state.field_53529.equals(mc.method_1548().method_1676());
        final boolean isFriend = FriendManager.getInstance().contains(state.field_53529);
        return !mod.onlySelf.getValue() || isSelf || (isFriend && mod.friends.getValue());
    }
    
    static {
        TEXTURE = class_2960.method_60655("leonware", "textures/leon/leon52.png");
    }
}
