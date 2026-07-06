// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Chams", category = Category.RENDER)
public class ChamsModule extends Module
{
    private static final ChamsModule instance;
    public final BooleanSetting onlyPlayers;
    public final SliderSetting hiddenR;
    public final SliderSetting hiddenG;
    public final SliderSetting hiddenB;
    public final SliderSetting hiddenA;
    public final SliderSetting visibleR;
    public final SliderSetting visibleG;
    public final SliderSetting visibleB;
    public final SliderSetting visibleA;
    
    public ChamsModule() {
        this.onlyPlayers = new BooleanSetting("Only Players").value(true);
        this.hiddenR = new SliderSetting("Hidden R").value(255.0f).range(0.0f, 255.0f).step(1.0f);
        this.hiddenG = new SliderSetting("Hidden G").value(0.0f).range(0.0f, 255.0f).step(1.0f);
        this.hiddenB = new SliderSetting("Hidden B").value(0.0f).range(0.0f, 255.0f).step(1.0f);
        this.hiddenA = new SliderSetting("Hidden A").value(150.0f).range(0.0f, 255.0f).step(1.0f);
        this.visibleR = new SliderSetting("Visible R").value(0.0f).range(0.0f, 255.0f).step(1.0f);
        this.visibleG = new SliderSetting("Visible G").value(200.0f).range(0.0f, 255.0f).step(1.0f);
        this.visibleB = new SliderSetting("Visible B").value(255.0f).range(0.0f, 255.0f).step(1.0f);
        this.visibleA = new SliderSetting("Visible A").value(255.0f).range(0.0f, 255.0f).step(1.0f);
        this.addSettings(this.onlyPlayers, this.hiddenR, this.hiddenG, this.hiddenB, this.hiddenA, this.visibleR, this.visibleG, this.visibleB, this.visibleA);
    }
    
    @Override
    public void onEvent() {
    }
    
    public boolean isOnlyPlayers() {
        return this.onlyPlayers.getValue();
    }
    
    public int getHiddenArgb() {
        return this.hiddenA.getValue().intValue() << 24 | this.hiddenR.getValue().intValue() << 16 | this.hiddenG.getValue().intValue() << 8 | this.hiddenB.getValue().intValue();
    }
    
    public int getVisibleArgb() {
        return this.visibleA.getValue().intValue() << 24 | this.visibleR.getValue().intValue() << 16 | this.visibleG.getValue().intValue() << 8 | this.visibleB.getValue().intValue();
    }
    
    @Generated
    public static ChamsModule getInstance() {
        return ChamsModule.instance;
    }
    
    static {
        instance = new ChamsModule();
    }
}
