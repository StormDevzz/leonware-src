package sweetie.leonware.client.features.modules.movement.noslow.modes;

import net.minecraft.class_2596;
import net.minecraft.class_2868;
import sweetie.leonware.client.features.modules.movement.noslow.NoSlowMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowSlotUpdate.class */
public class NoSlowSlotUpdate extends NoSlowMode {
    @Override // sweetie.leonware.api.system.backend.Choice, sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
    public String getName() {
        return "Slot update";
    }

    @Override // sweetie.leonware.client.features.modules.movement.noslow.NoSlowMode
    public void onUpdate() {
        sendPacket((class_2596<?>) new class_2868(mc.field_1724.method_31548().field_7545));
    }

    @Override // sweetie.leonware.client.features.modules.movement.noslow.NoSlowMode
    public void onTick() {
    }

    @Override // sweetie.leonware.client.features.modules.movement.noslow.NoSlowMode
    public boolean slowingCancel() {
        return true;
    }
}
