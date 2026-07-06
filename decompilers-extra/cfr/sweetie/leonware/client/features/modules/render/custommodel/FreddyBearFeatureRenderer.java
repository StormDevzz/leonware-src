/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_10055
 *  net.minecraft.class_1921
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_3883
 *  net.minecraft.class_3887
 *  net.minecraft.class_4587
 *  net.minecraft.class_4588
 *  net.minecraft.class_4597
 *  net.minecraft.class_4608
 *  net.minecraft.class_591
 */
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
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.client.features.modules.render.CustomModelModule;
import sweetie.leonware.client.features.modules.render.custommodel.FreddyBearModel;

@Environment(value=EnvType.CLIENT)
public class FreddyBearFeatureRenderer
extends class_3887<class_10055, class_591> {
    private static final class_2960 FREDDY_TEXTURE = class_2960.method_60655((String)"leonware", (String)"textures/models/leon/freddy.png");
    private final FreddyBearModel freddyModel;

    public FreddyBearFeatureRenderer(class_3883<class_10055, class_591> context, FreddyBearModel model) {
        super(context);
        this.freddyModel = model;
    }

    public void render(class_4587 matrices, class_4597 vertexConsumers, int light, class_10055 state, float limbAngle, float limbDistance) {
        if (!this.shouldRender(state)) {
            return;
        }
        this.freddyModel.copyPoseFromBase((class_591)this.method_17165());
        matrices.method_22903();
        matrices.method_22905(0.75f, 0.65f, 0.75f);
        matrices.method_46416(0.0f, 0.85f, 0.0f);
        class_4588 buffer = vertexConsumers.getBuffer(class_1921.method_23578((class_2960)FREDDY_TEXTURE));
        this.freddyModel.method_62100(matrices, buffer, light, class_4608.field_21444, -1);
        matrices.method_22909();
    }

    private boolean shouldRender(class_10055 state) {
        CustomModelModule mod = CustomModelModule.getInstance();
        if (!mod.isEnabled() || !mod.model.is("Freddy Bear")) {
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
        return (Boolean)mod.friends.getValue() != false && FriendManager.getInstance().contains(state.field_53529);
    }
}

