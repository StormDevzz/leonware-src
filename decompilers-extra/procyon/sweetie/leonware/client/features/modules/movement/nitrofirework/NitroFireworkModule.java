// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement.nitrofirework;

import lombok.Generated;
import java.util.Collection;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.system.backend.Choice;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.client.features.modules.movement.nitrofirework.modes.NitroFireworkLG;
import sweetie.leonware.client.features.modules.movement.nitrofirework.modes.NitroFireworkCustom;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Nitro Firework", category = Category.MOVEMENT)
public class NitroFireworkModule extends Module
{
    private static final NitroFireworkModule instance;
    private final NitroFireworkCustom nitroFireworkCustom;
    private final NitroFireworkLG nitroFireworkLG;
    private final NitroFireworkMode[] modes;
    public NitroFireworkMode currentMode;
    private final ModeSetting mode;
    
    public NitroFireworkModule() {
        this.nitroFireworkCustom = new NitroFireworkCustom(() -> this.getMode().is("Custom"));
        this.nitroFireworkLG = new NitroFireworkLG(() -> this.getMode().is("Grim"));
        this.modes = new NitroFireworkMode[] { this.nitroFireworkCustom, this.nitroFireworkLG };
        this.currentMode = this.nitroFireworkCustom;
        this.mode = new ModeSetting("Mode").value("Custom").values(Choice.getValues((Choice[])this.modes)).onAction(() -> this.currentMode = (NitroFireworkMode)Choice.getChoiceByName((String)this.getMode().getValue(), (Choice[])this.modes));
        this.addSettings(this.mode);
        this.getSettings().addAll(this.nitroFireworkCustom.getSettings());
        this.getSettings().addAll(this.nitroFireworkLG.getSettings());
    }
    
    @Override
    public void onEvent() {
    }
    
    @Generated
    public static NitroFireworkModule getInstance() {
        return NitroFireworkModule.instance;
    }
    
    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }
    
    static {
        instance = new NitroFireworkModule();
    }
}
