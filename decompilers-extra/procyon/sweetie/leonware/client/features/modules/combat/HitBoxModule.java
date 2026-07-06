// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "HitBox", category = Category.COMBAT)
public class HitBoxModule extends Module
{
    private static final HitBoxModule instance;
    private final ModeSetting mode;
    public final SliderSetting staticSize;
    public final SliderSetting minSize;
    public final SliderSetting maxSize;
    
    public HitBoxModule() {
        this.mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").value("\u0421\u0442\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438\u0439").values("\u0421\u0442\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438\u0439", "\u0414\u0438\u043d\u0430\u043c\u0438\u0447\u0435\u0441\u043a\u0438\u0439");
        this.staticSize = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440").value(0.2f).range(0.0f, 1.0f).step(0.05f);
        this.minSize = new SliderSetting("\u041c\u0438\u043d\u0438\u043c\u0430\u043b\u044c\u043d\u043e").value(0.1f).range(0.0f, 0.5f).step(0.05f);
        this.maxSize = new SliderSetting("\u041c\u0430\u043a\u0441\u0438\u043c\u0430\u043b\u044c\u043d\u043e").value(0.5f).range(0.0f, 1.0f).step(0.05f);
        this.addSettings(this.mode, this.staticSize, this.minSize, this.maxSize);
    }
    
    public static float getExpand() {
        if (!getInstance().isEnabled()) {
            return 0.0f;
        }
        final String currentMode = getInstance().mode.getValue();
        if (currentMode.equals("\u0421\u0442\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438\u0439")) {
            return getInstance().staticSize.getValue();
        }
        final float min = getInstance().minSize.getValue();
        final float max = getInstance().maxSize.getValue();
        final long time = System.currentTimeMillis();
        final float cycle = time % 1000L / 1000.0f;
        final float sine = (float)Math.sin(cycle * 3.141592653589793 * 2.0);
        final float normalized = (sine + 1.0f) / 2.0f;
        return min + (max - min) * normalized;
    }
    
    @Override
    public void onEvent() {
    }
    
    @Generated
    public static HitBoxModule getInstance() {
        return HitBoxModule.instance;
    }
    
    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }
    
    static {
        instance = new HitBoxModule();
    }
}
