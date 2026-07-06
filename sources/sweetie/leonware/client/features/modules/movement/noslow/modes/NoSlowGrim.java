package sweetie.leonware.client.features.modules.movement.noslow.modes;

import net.minecraft.class_1268;
import net.minecraft.class_2338;
import net.minecraft.class_2596;
import net.minecraft.class_2846;
import net.minecraft.class_2886;
import org.newsclub.net.unix.AFVSOCKSocketAddress;
import sweetie.leonware.client.features.modules.movement.noslow.NoSlowMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowGrim.class */
public class NoSlowGrim extends NoSlowMode {
    private int ticks = 0;
    private float ticksS = 0.0f;
    public BypassType bypassType = BypassType.TICK;

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowGrim$BypassType.class */
    public enum BypassType {
        TICK,
        TICKS,
        OLD,
        DROP
    }

    @Override // sweetie.leonware.api.system.backend.Choice, sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
    public String getName() {
        return "Grim";
    }

    @Override // sweetie.leonware.client.features.modules.movement.noslow.NoSlowMode
    public void onUpdate() {
        switch (this.bypassType.ordinal()) {
            case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                if (slowingCancel() && mc.field_1724.method_6115()) {
                    class_1268 hand = mc.field_1724.method_6058() == class_1268.field_5808 ? class_1268.field_5810 : class_1268.field_5808;
                    sendPacket((class_2596<?>) new class_2886(hand, 0, mc.field_1724.method_36454(), mc.field_1724.method_36455()));
                    break;
                }
                break;
            case 3:
                if (mc.field_1724.method_6058() != class_1268.field_5810 && mc.field_1724.method_6048() >= 4 && mc.field_1724.method_6048() == 5) {
                    sendPacket((class_2596<?>) new class_2846(class_2846.class_2847.field_12970, class_2338.field_10980, mc.field_1724.method_5735()));
                }
                break;
        }
    }

    @Override // sweetie.leonware.client.features.modules.movement.noslow.NoSlowMode
    public void onTick() {
        if (mc.field_1724.method_6115()) {
            this.ticks++;
            this.ticksS += 1.0f;
        } else {
            this.ticks = 0;
            this.ticksS = 0.0f;
        }
    }

    @Override // sweetie.leonware.client.features.modules.movement.noslow.NoSlowMode
    public boolean slowingCancel() {
        boolean tickRule = this.ticks >= 2;
        if (tickRule) {
            this.ticks = 0;
        }
        boolean cancelRule = false;
        switch (this.bypassType.ordinal()) {
            case 0:
                cancelRule = tickRule;
                break;
            case 1:
                if (mc.field_1724.method_6115() && !mc.field_1724.method_5765() && this.ticksS >= 1.3f) {
                    cancelRule = true;
                    this.ticksS = 0.26f;
                }
                break;
            case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                cancelRule = true;
                break;
            case 3:
                if (mc.field_1724.method_6058() == class_1268.field_5810) {
                    return false;
                }
                cancelRule = mc.field_1724.method_6048() > 6;
                break;
        }
        return cancelRule;
    }
}
