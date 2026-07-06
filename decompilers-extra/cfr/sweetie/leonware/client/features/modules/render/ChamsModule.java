/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="Chams", category=Category.RENDER)
public class ChamsModule
extends Module {
    private static final ChamsModule instance = new ChamsModule();
    public final BooleanSetting onlyPlayers = new BooleanSetting("Only Players").value(true);
    public final SliderSetting hiddenR = new SliderSetting("Hidden R").value(Float.valueOf(255.0f)).range(0.0f, 255.0f).step(1.0f);
    public final SliderSetting hiddenG = new SliderSetting("Hidden G").value(Float.valueOf(0.0f)).range(0.0f, 255.0f).step(1.0f);
    public final SliderSetting hiddenB = new SliderSetting("Hidden B").value(Float.valueOf(0.0f)).range(0.0f, 255.0f).step(1.0f);
    public final SliderSetting hiddenA = new SliderSetting("Hidden A").value(Float.valueOf(150.0f)).range(0.0f, 255.0f).step(1.0f);
    public final SliderSetting visibleR = new SliderSetting("Visible R").value(Float.valueOf(0.0f)).range(0.0f, 255.0f).step(1.0f);
    public final SliderSetting visibleG = new SliderSetting("Visible G").value(Float.valueOf(200.0f)).range(0.0f, 255.0f).step(1.0f);
    public final SliderSetting visibleB = new SliderSetting("Visible B").value(Float.valueOf(255.0f)).range(0.0f, 255.0f).step(1.0f);
    public final SliderSetting visibleA = new SliderSetting("Visible A").value(Float.valueOf(255.0f)).range(0.0f, 255.0f).step(1.0f);

    public ChamsModule() {
        this.addSettings(this.onlyPlayers, this.hiddenR, this.hiddenG, this.hiddenB, this.hiddenA, this.visibleR, this.visibleG, this.visibleB, this.visibleA);
    }

    @Override
    public void onEvent() {
    }

    public boolean isOnlyPlayers() {
        return (Boolean)this.onlyPlayers.getValue();
    }

    public int getHiddenArgb() {
        return ((Float)this.hiddenA.getValue()).intValue() << 24 | ((Float)this.hiddenR.getValue()).intValue() << 16 | ((Float)this.hiddenG.getValue()).intValue() << 8 | ((Float)this.hiddenB.getValue()).intValue();
    }

    public int getVisibleArgb() {
        return ((Float)this.visibleA.getValue()).intValue() << 24 | ((Float)this.visibleR.getValue()).intValue() << 16 | ((Float)this.visibleG.getValue()).intValue() << 8 | ((Float)this.visibleB.getValue()).intValue();
    }

    @Generated
    public static ChamsModule getInstance() {
        return instance;
    }
}

