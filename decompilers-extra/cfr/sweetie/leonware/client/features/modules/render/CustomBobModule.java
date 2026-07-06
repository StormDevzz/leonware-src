/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_3532
 *  net.minecraft.class_4587
 *  net.minecraft.class_742
 *  net.minecraft.class_7833
 */
package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_742;
import net.minecraft.class_7833;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="Custom Bob", category=Category.RENDER)
public class CustomBobModule
extends Module {
    private static final CustomBobModule instance = new CustomBobModule();
    public final SliderSetting txStrength = new SliderSetting("X \u041f\u043e\u0437\u0438\u0446\u0438\u044f").value(Float.valueOf(0.45f)).range(0.0f, 5.0f).step(0.05f);
    public final SliderSetting tyStrength = new SliderSetting("Y \u041f\u043e\u0437\u0438\u0446\u0438\u044f").value(Float.valueOf(0.3f)).range(0.0f, 5.0f).step(0.05f);
    public final SliderSetting rxStrength = new SliderSetting("X \u0420\u043e\u0442\u0430\u0446\u0438\u044f").value(Float.valueOf(1.35f)).range(0.0f, 5.0f).step(0.05f);
    public final SliderSetting rzStrength = new SliderSetting("Z \u0420\u043e\u0442\u0430\u0446\u0438\u044f").value(Float.valueOf(1.5f)).range(0.0f, 5.0f).step(0.05f);

    public CustomBobModule() {
        this.addSettings(this.txStrength, this.tyStrength, this.rxStrength, this.rzStrength);
    }

    public void applyCustomBob(class_4587 matrices) {
        class_1297 class_12972 = mc.method_1560();
        if (class_12972 instanceof class_742) {
            class_742 player = (class_742)class_12972;
            float tickDelta = mc.method_61966().method_60637(true);
            float f = player.field_53039 - player.field_53038;
            float g = -(player.field_53039 + f * tickDelta);
            float h = class_3532.method_16439((float)tickDelta, (float)player.field_7505, (float)player.field_7483);
            float sinG = class_3532.method_15374((float)(g * (float)Math.PI));
            float cosG = class_3532.method_15362((float)(g * (float)Math.PI));
            matrices.method_46416(sinG * h * 0.5f * ((Float)this.txStrength.getValue()).floatValue(), -Math.abs(cosG * h) * ((Float)this.tyStrength.getValue()).floatValue(), 0.0f);
            matrices.method_22907(class_7833.field_40718.rotationDegrees(sinG * h * 3.0f * ((Float)this.rzStrength.getValue()).floatValue()));
            matrices.method_22907(class_7833.field_40714.rotationDegrees(Math.abs(class_3532.method_15362((float)(g * (float)Math.PI - 0.2f)) * h) * 5.0f * ((Float)this.rxStrength.getValue()).floatValue()));
        }
    }

    @Override
    public void onEvent() {
    }

    @Generated
    public static CustomBobModule getInstance() {
        return instance;
    }
}

