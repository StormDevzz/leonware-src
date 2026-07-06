// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;
import java.util.Arrays;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Removals", category = Category.RENDER)
public class RemovalsModule extends Module
{
    private static final RemovalsModule instance;
    private final String[] elements;
    private final MultiBooleanSetting remove;
    
    public RemovalsModule() {
        this.elements = new String[] { "Fire overlay", "Hurt camera", "Inwall overlay", "Water overlay", "Scoreboard", "Glow effect", "Bad effects", "Boss bar", "Gui background" };
        this.remove = new MultiBooleanSetting("Remove").value((BooleanSetting[])Arrays.stream(this.elements).map(name -> new BooleanSetting(name).value(false)).toArray(BooleanSetting[]::new));
        this.addSettings(this.remove);
    }
    
    public boolean isFireOverlay() {
        return this.isEnabled() && this.remove.isEnabled("Fire overlay");
    }
    
    public boolean isHurtCamera() {
        return this.isEnabled() && this.remove.isEnabled("Hurt camera");
    }
    
    public boolean isInwallOverlay() {
        return this.isEnabled() && this.remove.isEnabled("Inwall overlay");
    }
    
    public boolean isWaterOverlay() {
        return this.isEnabled() && this.remove.isEnabled("Water overlay");
    }
    
    public boolean isScoreboard() {
        return this.isEnabled() && this.remove.isEnabled("Scoreboard");
    }
    
    public boolean isGlowEffect() {
        return this.isEnabled() && this.remove.isEnabled("Glow effect");
    }
    
    public boolean isBadEffects() {
        return this.isEnabled() && this.remove.isEnabled("Bad effects");
    }
    
    public boolean isBossBar() {
        return this.isEnabled() && this.remove.isEnabled("Boss bar");
    }
    
    public boolean isGuiBackground() {
        return this.isEnabled() && this.remove.isEnabled("Gui background");
    }
    
    @Override
    public void onEvent() {
    }
    
    @Generated
    public static RemovalsModule getInstance() {
        return RemovalsModule.instance;
    }
    
    static {
        instance = new RemovalsModule();
    }
}
