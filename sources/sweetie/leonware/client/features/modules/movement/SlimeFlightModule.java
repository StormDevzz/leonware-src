package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_3965;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.move.MotionEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.utils.player.InventoryUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/SlimeFlightModule.class */
@ModuleRegister(name = "Slime Flight", category = Category.MOVEMENT)
public class SlimeFlightModule extends Module {
    private static final SlimeFlightModule instance = new SlimeFlightModule();
    private int slimeJumpCooldown = 0;
    private boolean startSetPitch = false;

    @Generated
    public static SlimeFlightModule getInstance() {
        return instance;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (this.startSetPitch && mc.field_1724 != null) {
                mc.field_1724.method_36457(54.0f);
                this.startSetPitch = false;
            }
        }));
        EventListener motionEvent = MotionEvent.getInstance().subscribe(new Listener(event2 -> {
            handleSlimeLogic();
        }));
        addEvents(updateEvent, motionEvent);
    }

    private void handleSlimeLogic() {
        int slimeSlot;
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return;
        }
        class_2338 playerPos = mc.field_1724.method_24515();
        boolean slimeNearby = isSlimeAdjacent(playerPos);
        if (!slimeNearby || !mc.field_1724.field_5976 || mc.field_1724.method_18798().field_1351 <= -1.0d) {
            return;
        }
        class_3965 class_3965Var = mc.field_1765;
        if (class_3965Var instanceof class_3965) {
            class_3965 blockHitResult = class_3965Var;
            if (mc.field_1687.method_8320(blockHitResult.method_17777()).method_26215() || (slimeSlot = InventoryUtil.findItem(class_1802.field_8828, true)) == -1) {
                return;
            }
            mc.field_1724.method_31548().field_7545 = slimeSlot;
            this.startSetPitch = true;
            if (mc.field_1761 != null) {
                mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, blockHitResult);
                mc.field_1724.method_6104(class_1268.field_5808);
            }
            if (this.slimeJumpCooldown >= 1) {
                mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, 0.63d, mc.field_1724.method_18798().field_1350);
                this.slimeJumpCooldown = 0;
            } else {
                this.slimeJumpCooldown++;
            }
        }
    }

    private boolean isSlimeAdjacent(class_2338 pos) {
        return mc.field_1687.method_8320(pos.method_10078()).method_27852(class_2246.field_10030) || mc.field_1687.method_8320(pos.method_10067()).method_27852(class_2246.field_10030) || mc.field_1687.method_8320(pos.method_10095()).method_27852(class_2246.field_10030) || mc.field_1687.method_8320(pos.method_10072()).method_27852(class_2246.field_10030);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        super.onDisable();
        this.slimeJumpCooldown = 0;
        this.startSetPitch = false;
    }
}
