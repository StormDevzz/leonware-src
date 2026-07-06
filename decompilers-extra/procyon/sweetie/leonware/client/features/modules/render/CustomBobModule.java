// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_7833;
import net.minecraft.class_3532;
import net.minecraft.class_742;
import net.minecraft.class_4587;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Custom Bob", category = Category.RENDER)
public class CustomBobModule extends Module
{
    private static final CustomBobModule instance;
    public final SliderSetting txStrength;
    public final SliderSetting tyStrength;
    public final SliderSetting rxStrength;
    public final SliderSetting rzStrength;
    
    public CustomBobModule() {
        this.txStrength = new SliderSetting("X \u041f\u043e\u0437\u0438\u0446\u0438\u044f").value(0.45f).range(0.0f, 5.0f).step(0.05f);
        this.tyStrength = new SliderSetting("Y \u041f\u043e\u0437\u0438\u0446\u0438\u044f").value(0.3f).range(0.0f, 5.0f).step(0.05f);
        this.rxStrength = new SliderSetting("X \u0420\u043e\u0442\u0430\u0446\u0438\u044f").value(1.35f).range(0.0f, 5.0f).step(0.05f);
        this.rzStrength = new SliderSetting("Z \u0420\u043e\u0442\u0430\u0446\u0438\u044f").value(1.5f).range(0.0f, 5.0f).step(0.05f);
        this.addSettings(this.txStrength, this.tyStrength, this.rxStrength, this.rzStrength);
    }
    
    public void applyCustomBob(final class_4587 matrices) {
        final class_1297 method_1560 = CustomBobModule.mc.method_1560();
        if (method_1560 instanceof final class_742 player) {
            final float tickDelta = CustomBobModule.mc.method_61966().method_60637(true);
            final float f = player.field_53039 - player.field_53038;
            final float g = -(player.field_53039 + f * tickDelta);
            final float h = class_3532.method_16439(tickDelta, player.field_7505, player.field_7483);
            final float sinG = class_3532.method_15374(g * 3.1415927f);
            final float cosG = class_3532.method_15362(g * 3.1415927f);
            matrices.method_46416(sinG * h * 0.5f * this.txStrength.getValue(), -Math.abs(cosG * h) * this.tyStrength.getValue(), 0.0f);
            matrices.method_22907(class_7833.field_40718.rotationDegrees(sinG * h * 3.0f * this.rzStrength.getValue()));
            matrices.method_22907(class_7833.field_40714.rotationDegrees(Math.abs(class_3532.method_15362(g * 3.1415927f - 0.2f) * h) * 5.0f * this.rxStrength.getValue()));
        }
    }
    
    @Override
    public void onEvent() {
    }
    
    @Generated
    public static CustomBobModule getInstance() {
        return CustomBobModule.instance;
    }
    
    static {
        instance = new CustomBobModule();
    }
}
