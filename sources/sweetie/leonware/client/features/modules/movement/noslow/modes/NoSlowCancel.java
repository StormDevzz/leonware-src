package sweetie.leonware.client.features.modules.movement.noslow.modes;

import sweetie.leonware.client.features.modules.movement.noslow.NoSlowMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowCancel.class */
public class NoSlowCancel extends NoSlowMode {
    @Override // sweetie.leonware.api.system.backend.Choice, sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
    public String getName() {
        return "Cancel";
    }

    @Override // sweetie.leonware.client.features.modules.movement.noslow.NoSlowMode
    public void onUpdate() {
    }

    @Override // sweetie.leonware.client.features.modules.movement.noslow.NoSlowMode
    public void onTick() {
    }

    @Override // sweetie.leonware.client.features.modules.movement.noslow.NoSlowMode
    public boolean slowingCancel() {
        return true;
    }
}
