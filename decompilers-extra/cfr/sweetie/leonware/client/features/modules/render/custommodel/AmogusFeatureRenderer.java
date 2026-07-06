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
import sweetie.leonware.client.features.modules.render.custommodel.AmogusModel;

@Environment(value=EnvType.CLIENT)
public class AmogusFeatureRenderer
extends class_3887<class_10055, class_591> {
    private static final class_2960 AMOGUS_TEXTURE = class_2960.method_60655((String)"leonware", (String)"textures/models/leon/amogus.png");
    private final AmogusModel amogusModel;

    public AmogusFeatureRenderer(class_3883<class_10055, class_591> context, AmogusModel model) {
        super(context);
        this.amogusModel = model;
    }

    public void render(class_4587 matrices, class_4597 vertexConsumers, int light, class_10055 state, float limbAngle, float limbDistance) {
        if (!this.shouldRender(state)) {
            return;
        }
        this.amogusModel.copyPoseFromBase((class_591)this.method_17165());
        matrices.method_22903();
        if (state.field_53457) {
            matrices.method_22905(0.5f, 0.5f, 0.5f);
            matrices.method_46416(0.0f, 1.5f, 0.0f);
        } else {
            matrices.method_22904(0.0, -0.8, 0.0);
            matrices.method_22905(1.8f, 1.6f, 1.6f);
        }
        class_4588 buffer = vertexConsumers.getBuffer(class_1921.method_23578((class_2960)AMOGUS_TEXTURE));
        this.amogusModel.method_62100(matrices, buffer, light, class_4608.field_21444, -1);
        matrices.method_22909();
    }

    private boolean shouldRender(class_10055 state) {
        CustomModelModule mod = CustomModelModule.getInstance();
        if (!mod.isEnabled() || !mod.model.is("Amogus")) {
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

