/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_2596
 *  net.minecraft.class_2848
 *  net.minecraft.class_2848$class_2849
 */
package sweetie.leonware.api.utils.combat;

import net.minecraft.class_1297;
import net.minecraft.class_2596;
import net.minecraft.class_2848;
import sweetie.leonware.api.event.events.player.move.SprintEvent;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class SprintManager
implements QuickImports {
    public SprintType sprintType;

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
        if (this.sprintType == SprintType.PACKET && SprintManager.mc.field_1724.field_3919 && !SprintManager.mc.field_1724.method_5799()) {
            this.sprintPacketState(enable);
        }
    }

    private void sprintPacketState(boolean enable) {
        this.sendPacket((class_2596<?>)new class_2848((class_1297)SprintManager.mc.field_1724, enable ? class_2848.class_2849.field_12981 : class_2848.class_2849.field_12985));
        SprintManager.mc.field_1724.method_5728(enable);
        SprintManager.mc.field_1690.field_1867.method_23481(enable);
    }

    public static enum SprintType {
        LEGIT,
        PACKET,
        NONE;

    }
}

