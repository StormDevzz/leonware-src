package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/MultiTaskModule.class */
@ModuleRegister(name = "Multi Task", category = Category.PLAYER)
public class MultiTaskModule extends Module {
    private static final MultiTaskModule instance = new MultiTaskModule();
    private final MultiBooleanSetting tasks = new MultiBooleanSetting("Tasks").value(new BooleanSetting("Attack").value((Boolean) true), new BooleanSetting("Block break").value((Boolean) true), new BooleanSetting("Interact").value((Boolean) false));

    @Generated
    public static MultiTaskModule getInstance() {
        return instance;
    }

    public MultiTaskModule() {
        addSettings(this.tasks);
    }

    public boolean allowAttack() {
        return isEnabled() && this.tasks.isEnabled("Attack");
    }

    public boolean allowBreak() {
        return isEnabled() && this.tasks.isEnabled("Block break");
    }

    public boolean allowInteract() {
        return isEnabled() && this.tasks.isEnabled("Interact");
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}
