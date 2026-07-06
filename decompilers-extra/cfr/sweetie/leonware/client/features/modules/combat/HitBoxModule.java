/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="HitBox", category=Category.COMBAT)
public class HitBoxModule
extends Module {
    private static final HitBoxModule instance = new HitBoxModule();
    private final ModeSetting mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").value("\u0421\u0442\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438\u0439").values("\u0421\u0442\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438\u0439", "\u0414\u0438\u043d\u0430\u043c\u0438\u0447\u0435\u0441\u043a\u0438\u0439");
    public final SliderSetting staticSize = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440").value(Float.valueOf(0.2f)).range(0.0f, 1.0f).step(0.05f);
    public final SliderSetting minSize = new SliderSetting("\u041c\u0438\u043d\u0438\u043c\u0430\u043b\u044c\u043d\u043e").value(Float.valueOf(0.1f)).range(0.0f, 0.5f).step(0.05f);
    public final SliderSetting maxSize = new SliderSetting("\u041c\u0430\u043a\u0441\u0438\u043c\u0430\u043b\u044c\u043d\u043e").value(Float.valueOf(0.5f)).range(0.0f, 1.0f).step(0.05f);

    public HitBoxModule() {
        this.addSettings(this.mode, this.staticSize, this.minSize, this.maxSize);
    }

    public static float getExpand() {
        if (!HitBoxModule.getInstance().isEnabled()) {
            return 0.0f;
        }
        String currentMode = (String)HitBoxModule.getInstance().mode.getValue();
        if (currentMode.equals("\u0421\u0442\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438\u0439")) {
            return ((Float)HitBoxModule.getInstance().staticSize.getValue()).floatValue();
        }
        float min = ((Float)HitBoxModule.getInstance().minSize.getValue()).floatValue();
        float max = ((Float)HitBoxModule.getInstance().maxSize.getValue()).floatValue();
        long time = System.currentTimeMillis();
        float cycle = (float)(time % 1000L) / 1000.0f;
        float sine = (float)Math.sin((double)cycle * Math.PI * 2.0);
        float normalized = (sine + 1.0f) / 2.0f;
        return min + (max - min) * normalized;
    }

    @Override
    public void onEvent() {
    }

    @Generated
    public static HitBoxModule getInstance() {
        return instance;
    }

    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }
}

