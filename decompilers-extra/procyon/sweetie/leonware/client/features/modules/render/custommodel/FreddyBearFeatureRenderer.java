// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.custommodel;

import net.minecraft.class_10017;
import sweetie.leonware.api.system.configs.FriendManager;
import net.minecraft.class_310;
import sweetie.leonware.client.features.modules.render.CustomModelModule;
import net.minecraft.class_4588;
import net.minecraft.class_4608;
import net.minecraft.class_1921;
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
public class FreddyBearFeatureRenderer extends class_3887<class_10055, class_591>
{
    private static final class_2960 FREDDY_TEXTURE;
    private final FreddyBearModel freddyModel;
    
    public FreddyBearFeatureRenderer(final class_3883<class_10055, class_591> context, final FreddyBearModel model) {
        super((class_3883)context);
        this.freddyModel = model;
    }
    
    public void render(final class_4587 matrices, final class_4597 vertexConsumers, final int light, final class_10055 state, final float limbAngle, final float limbDistance) {
        if (!this.shouldRender(state)) {
            return;
        }
        this.freddyModel.copyPoseFromBase((class_591)this.method_17165());
        matrices.method_22903();
        matrices.method_22905(0.75f, 0.65f, 0.75f);
        matrices.method_46416(0.0f, 0.85f, 0.0f);
        final class_4588 buffer = vertexConsumers.getBuffer(class_1921.method_23578(FreddyBearFeatureRenderer.FREDDY_TEXTURE));
        this.freddyModel.method_62100(matrices, buffer, light, class_4608.field_21444, -1);
        matrices.method_22909();
    }
    
    private boolean shouldRender(final class_10055 state) {
        final CustomModelModule mod = CustomModelModule.getInstance();
        if (!mod.isEnabled() || !mod.model.is("Freddy Bear")) {
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
        FREDDY_TEXTURE = class_2960.method_60655("leonware", "textures/models/leon/freddy.png");
    }
}
