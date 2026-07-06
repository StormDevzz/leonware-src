package sweetie.leonware.client.features.modules.movement.nitrofirework;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.system.backend.Choice;
import sweetie.leonware.client.features.modules.movement.nitrofirework.modes.NitroFireworkCustom;
import sweetie.leonware.client.features.modules.movement.nitrofirework.modes.NitroFireworkLG;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/nitrofirework/NitroFireworkModule.class */
@ModuleRegister(name = "Nitro Firework", category = Category.MOVEMENT)
public class NitroFireworkModule extends Module {
    private static final NitroFireworkModule instance = new NitroFireworkModule();
    private final NitroFireworkCustom nitroFireworkCustom = new NitroFireworkCustom(() -> {
        return Boolean.valueOf(getMode().is("Custom"));
    });
    private final NitroFireworkLG nitroFireworkLG = new NitroFireworkLG(() -> {
        return Boolean.valueOf(getMode().is("Grim"));
    });
    private final NitroFireworkMode[] modes = {this.nitroFireworkCustom, this.nitroFireworkLG};
    public NitroFireworkMode currentMode = this.nitroFireworkCustom;
    private final ModeSetting mode = new ModeSetting("Mode").value("Custom").values(Choice.getValues(this.modes)).onAction2(() -> {
        this.currentMode = (NitroFireworkMode) Choice.getChoiceByName(getMode().getValue(), (Choice[]) this.modes);
    });

    @Generated
    public static NitroFireworkModule getInstance() {
        return instance;
    }

    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }

    /* JADX WARN: Type inference failed for: r1v9, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    public NitroFireworkModule() {
        addSettings(this.mode);
        getSettings().addAll(this.nitroFireworkCustom.getSettings());
        getSettings().addAll(this.nitroFireworkLG.getSettings());
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}
