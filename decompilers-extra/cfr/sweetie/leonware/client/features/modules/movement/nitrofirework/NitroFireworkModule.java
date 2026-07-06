/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.client.features.modules.movement.nitrofirework;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.system.backend.Choice;
import sweetie.leonware.client.features.modules.movement.nitrofirework.NitroFireworkMode;
import sweetie.leonware.client.features.modules.movement.nitrofirework.modes.NitroFireworkCustom;
import sweetie.leonware.client.features.modules.movement.nitrofirework.modes.NitroFireworkLG;

@ModuleRegister(name="Nitro Firework", category=Category.MOVEMENT)
public class NitroFireworkModule
extends Module {
    private static final NitroFireworkModule instance = new NitroFireworkModule();
    private final NitroFireworkCustom nitroFireworkCustom = new NitroFireworkCustom(() -> this.getMode().is("Custom"));
    private final NitroFireworkLG nitroFireworkLG = new NitroFireworkLG(() -> this.getMode().is("Grim"));
    private final NitroFireworkMode[] modes = new NitroFireworkMode[]{this.nitroFireworkCustom, this.nitroFireworkLG};
    public NitroFireworkMode currentMode = this.nitroFireworkCustom;
    private final ModeSetting mode = new ModeSetting("Mode").value("Custom").values(Choice.getValues(this.modes)).onAction(() -> {
        this.currentMode = (NitroFireworkMode)Choice.getChoiceByName((String)this.getMode().getValue(), this.modes);
    });

    public NitroFireworkModule() {
        this.addSettings(this.mode);
        this.getSettings().addAll(this.nitroFireworkCustom.getSettings());
        this.getSettings().addAll(this.nitroFireworkLG.getSettings());
    }

    @Override
    public void onEvent() {
    }

    @Generated
    public static NitroFireworkModule getInstance() {
        return instance;
    }

    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }
}

