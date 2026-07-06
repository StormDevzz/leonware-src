// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;
import java.util.function.Supplier;
import java.util.Objects;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Custom Elytra", category = Category.RENDER)
public class CustomElytraModule extends Module
{
    private static final CustomElytraModule instance;
    public final SliderSetting elytraScale;
    public final SliderSetting elytraX;
    public final SliderSetting elytraY;
    public final SliderSetting elytraZ;
    public final BooleanSetting customModelElytra;
    public final SliderSetting customScale;
    public final SliderSetting customX;
    public final SliderSetting customY;
    public final SliderSetting customZ;
    
    public CustomElytraModule() {
        this.elytraScale = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440 \u044d\u043b\u0438\u0442\u0440\u044b").value(1.0f).range(0.1f, 3.0f).step(0.05f);
        this.elytraX = new SliderSetting("\u042d\u043b\u0438\u0442\u0440\u0430 X").value(0.0f).range(-2.0f, 2.0f).step(0.01f);
        this.elytraY = new SliderSetting("\u042d\u043b\u0438\u0442\u0440\u0430 Y").value(0.0f).range(-2.0f, 2.0f).step(0.01f);
        this.elytraZ = new SliderSetting("\u042d\u043b\u0438\u0442\u0440\u0430 Z").value(0.0f).range(-2.0f, 2.0f).step(0.01f);
        this.customModelElytra = new BooleanSetting("\u041d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0438 \u0434\u043b\u044f \u043a\u0430\u0441\u0442\u043e\u043c \u043c\u043e\u0434\u0435\u043b\u0438").value(true);
        final SliderSetting step = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440 \u044d\u043b\u0438\u0442\u0440\u044b").value(0.67f).range(0.1f, 3.0f).step(0.05f);
        final BooleanSetting customModelElytra = this.customModelElytra;
        Objects.requireNonNull(customModelElytra);
        this.customScale = step.setVisible((Supplier<Boolean>)customModelElytra::getValue);
        final SliderSetting step2 = new SliderSetting("\u042d\u043b\u0438\u0442\u0440\u0430 X").value(0.0f).range(-2.0f, 2.0f).step(0.01f);
        final BooleanSetting customModelElytra2 = this.customModelElytra;
        Objects.requireNonNull(customModelElytra2);
        this.customX = step2.setVisible((Supplier<Boolean>)customModelElytra2::getValue);
        final SliderSetting step3 = new SliderSetting("\u042d\u043b\u0438\u0442\u0440\u0430 Y").value(0.6f).range(-2.0f, 2.0f).step(0.01f);
        final BooleanSetting customModelElytra3 = this.customModelElytra;
        Objects.requireNonNull(customModelElytra3);
        this.customY = step3.setVisible((Supplier<Boolean>)customModelElytra3::getValue);
        final SliderSetting step4 = new SliderSetting("\u042d\u043b\u0438\u0442\u0440\u0430 Z").value(0.12f).range(-2.0f, 2.0f).step(0.01f);
        final BooleanSetting customModelElytra4 = this.customModelElytra;
        Objects.requireNonNull(customModelElytra4);
        this.customZ = step4.setVisible((Supplier<Boolean>)customModelElytra4::getValue);
        this.addSettings(this.elytraScale, this.elytraX, this.elytraY, this.elytraZ, this.customModelElytra, this.customScale, this.customX, this.customY, this.customZ);
    }
    
    @Override
    public void onEvent() {
    }
    
    @Generated
    public static CustomElytraModule getInstance() {
        return CustomElytraModule.instance;
    }
    
    static {
        instance = new CustomElytraModule();
    }
}
