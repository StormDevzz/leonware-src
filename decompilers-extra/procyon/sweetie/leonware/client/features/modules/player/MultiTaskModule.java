// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Multi Task", category = Category.PLAYER)
public class MultiTaskModule extends Module
{
    private static final MultiTaskModule instance;
    private final MultiBooleanSetting tasks;
    
    public MultiTaskModule() {
        this.tasks = new MultiBooleanSetting("Tasks").value(new BooleanSetting("Attack").value(true), new BooleanSetting("Block break").value(true), new BooleanSetting("Interact").value(false));
        this.addSettings(this.tasks);
    }
    
    public boolean allowAttack() {
        return this.isEnabled() && this.tasks.isEnabled("Attack");
    }
    
    public boolean allowBreak() {
        return this.isEnabled() && this.tasks.isEnabled("Block break");
    }
    
    public boolean allowInteract() {
        return this.isEnabled() && this.tasks.isEnabled("Interact");
    }
    
    @Override
    public void onEvent() {
    }
    
    @Generated
    public static MultiTaskModule getInstance() {
        return MultiTaskModule.instance;
    }
    
    static {
        instance = new MultiTaskModule();
    }
}
