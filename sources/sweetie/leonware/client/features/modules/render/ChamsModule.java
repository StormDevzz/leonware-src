package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/ChamsModule.class */
@ModuleRegister(name = "Chams", category = Category.RENDER)
public class ChamsModule extends Module {
    private static final ChamsModule instance = new ChamsModule();
    public final BooleanSetting onlyPlayers = new BooleanSetting("Only Players").value((Boolean) true);
    public final SliderSetting hiddenR = new SliderSetting("Hidden R").value(Float.valueOf(255.0f)).range(0.0f, 255.0f).step(1.0f);
    public final SliderSetting hiddenG = new SliderSetting("Hidden G").value(Float.valueOf(0.0f)).range(0.0f, 255.0f).step(1.0f);
    public final SliderSetting hiddenB = new SliderSetting("Hidden B").value(Float.valueOf(0.0f)).range(0.0f, 255.0f).step(1.0f);
    public final SliderSetting hiddenA = new SliderSetting("Hidden A").value(Float.valueOf(150.0f)).range(0.0f, 255.0f).step(1.0f);
    public final SliderSetting visibleR = new SliderSetting("Visible R").value(Float.valueOf(0.0f)).range(0.0f, 255.0f).step(1.0f);
    public final SliderSetting visibleG = new SliderSetting("Visible G").value(Float.valueOf(200.0f)).range(0.0f, 255.0f).step(1.0f);
    public final SliderSetting visibleB = new SliderSetting("Visible B").value(Float.valueOf(255.0f)).range(0.0f, 255.0f).step(1.0f);
    public final SliderSetting visibleA = new SliderSetting("Visible A").value(Float.valueOf(255.0f)).range(0.0f, 255.0f).step(1.0f);

    @Generated
    public static ChamsModule getInstance() {
        return instance;
    }

    public ChamsModule() {
        addSettings(this.onlyPlayers, this.hiddenR, this.hiddenG, this.hiddenB, this.hiddenA, this.visibleR, this.visibleG, this.visibleB, this.visibleA);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }

    public boolean isOnlyPlayers() {
        return this.onlyPlayers.getValue().booleanValue();
    }

    public int getHiddenArgb() {
        return (this.hiddenA.getValue().intValue() << 24) | (this.hiddenR.getValue().intValue() << 16) | (this.hiddenG.getValue().intValue() << 8) | this.hiddenB.getValue().intValue();
    }

    public int getVisibleArgb() {
        return (this.visibleA.getValue().intValue() << 24) | (this.visibleR.getValue().intValue() << 16) | (this.visibleG.getValue().intValue() << 8) | this.visibleB.getValue().intValue();
    }
}
