package sweetie.leonware.api.utils.combat;

import net.minecraft.class_2596;
import net.minecraft.class_2848;
import sweetie.leonware.api.event.events.player.move.SprintEvent;
import sweetie.leonware.api.system.interfaces.QuickImports;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/combat/SprintManager.class */
public class SprintManager implements QuickImports {
    public SprintType sprintType;

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/combat/SprintManager$SprintType.class */
    public enum SprintType {
        LEGIT,
        PACKET,
        NONE
    }

    public SprintManager(SprintType sprintType) {
        this.sprintType = sprintType;
    }

    public void legitSprint(SprintEvent.SprintEventData event, boolean rule, boolean forceKeep) {
        if (this.sprintType == SprintType.LEGIT) {
            if (forceKeep) {
                event.setSprint(true);
            } else if (rule) {
                event.setSprint(false);
            }
        }
    }

    public void packetSprint(boolean enable) {
        if (this.sprintType == SprintType.PACKET && mc.field_1724.field_3919 && !mc.field_1724.method_5799()) {
            sprintPacketState(enable);
        }
    }

    private void sprintPacketState(boolean enable) {
        sendPacket((class_2596<?>) new class_2848(mc.field_1724, enable ? class_2848.class_2849.field_12981 : class_2848.class_2849.field_12985));
        mc.field_1724.method_5728(enable);
        mc.field_1690.field_1867.method_23481(enable);
    }
}
