/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;

@ModuleRegister(name="Multi Task", category=Category.PLAYER)
public class MultiTaskModule
extends Module {
    private static final MultiTaskModule instance = new MultiTaskModule();
    private final MultiBooleanSetting tasks = new MultiBooleanSetting("Tasks").value(new BooleanSetting("Attack").value(true), new BooleanSetting("Block break").value(true), new BooleanSetting("Interact").value(false));

    public MultiTaskModule() {
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
        return instance;
    }
}

