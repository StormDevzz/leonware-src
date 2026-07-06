// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;
import java.util.function.Supplier;
import java.util.Objects;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Baby Model", category = Category.RENDER)
public class BabyModelModule extends Module
{
    public static boolean currentShouldScale;
    private static final BabyModelModule instance;
    public final BooleanSetting onlySelf;
    public final BooleanSetting friends;
    public final SliderSetting allScale;
    public final SliderSetting headScale;
    public final BooleanSetting cameraOffset;
    
    public BabyModelModule() {
        this.onlySelf = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u043d\u0430 \u0441\u0435\u0431\u044f").value(true);
        final BooleanSetting value = new BooleanSetting("\u0418 \u043d\u0430 \u0434\u0440\u0443\u0437\u0435\u0439").value(false);
        final BooleanSetting onlySelf = this.onlySelf;
        Objects.requireNonNull(onlySelf);
        this.friends = value.setVisible((Supplier<Boolean>)onlySelf::getValue);
        this.allScale = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440 \u0442\u0435\u043b\u0430").value(0.5f).range(0.1f, 2.0f).step(0.1f);
        this.headScale = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440 \u0433\u043e\u043b\u043e\u0432\u044b").value(1.0f).range(0.1f, 3.0f).step(0.1f);
        this.cameraOffset = new BooleanSetting("\u0421\u043c\u0435\u0449\u0435\u043d\u0438\u0435 \u043a\u0430\u043c\u0435\u0440\u044b").value(true);
        this.addSettings(this.onlySelf, this.friends, this.allScale, this.headScale, this.cameraOffset);
    }
    
    @Override
    public void onEvent() {
    }
    
    @Generated
    public static BabyModelModule getInstance() {
        return BabyModelModule.instance;
    }
    
    static {
        BabyModelModule.currentShouldScale = false;
        instance = new BabyModelModule();
    }
}
