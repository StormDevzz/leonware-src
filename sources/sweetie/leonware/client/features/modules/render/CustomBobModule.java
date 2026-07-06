package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_742;
import net.minecraft.class_7833;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/CustomBobModule.class */
@ModuleRegister(name = "Custom Bob", category = Category.RENDER)
public class CustomBobModule extends Module {
    private static final CustomBobModule instance = new CustomBobModule();
    public final SliderSetting txStrength = new SliderSetting("X Позиция").value(Float.valueOf(0.45f)).range(0.0f, 5.0f).step(0.05f);
    public final SliderSetting tyStrength = new SliderSetting("Y Позиция").value(Float.valueOf(0.3f)).range(0.0f, 5.0f).step(0.05f);
    public final SliderSetting rxStrength = new SliderSetting("X Ротация").value(Float.valueOf(1.35f)).range(0.0f, 5.0f).step(0.05f);
    public final SliderSetting rzStrength = new SliderSetting("Z Ротация").value(Float.valueOf(1.5f)).range(0.0f, 5.0f).step(0.05f);

    @Generated
    public static CustomBobModule getInstance() {
        return instance;
    }

    public CustomBobModule() {
        addSettings(this.txStrength, this.tyStrength, this.rxStrength, this.rzStrength);
    }

    public void applyCustomBob(class_4587 matrices) {
        class_742 class_742VarMethod_1560 = mc.method_1560();
        if (class_742VarMethod_1560 instanceof class_742) {
            class_742 player = class_742VarMethod_1560;
            float tickDelta = mc.method_61966().method_60637(true);
            float f = player.field_53039 - player.field_53038;
            float g = -(player.field_53039 + (f * tickDelta));
            float h = class_3532.method_16439(tickDelta, player.field_7505, player.field_7483);
            float sinG = class_3532.method_15374(g * 3.1415927f);
            float cosG = class_3532.method_15362(g * 3.1415927f);
            matrices.method_46416(sinG * h * 0.5f * this.txStrength.getValue().floatValue(), (-Math.abs(cosG * h)) * this.tyStrength.getValue().floatValue(), 0.0f);
            matrices.method_22907(class_7833.field_40718.rotationDegrees(sinG * h * 3.0f * this.rzStrength.getValue().floatValue()));
            matrices.method_22907(class_7833.field_40714.rotationDegrees(Math.abs(class_3532.method_15362((g * 3.1415927f) - 0.2f) * h) * 5.0f * this.rxStrength.getValue().floatValue()));
        }
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}
