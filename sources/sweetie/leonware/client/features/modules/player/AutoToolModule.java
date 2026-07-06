package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1661;
import net.minecraft.class_2248;
import net.minecraft.class_3965;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/AutoToolModule.class */
@ModuleRegister(name = "Auto Tool", category = Category.PLAYER)
public class AutoToolModule extends Module {
    private static final AutoToolModule instance = new AutoToolModule();
    private int lastSlot = -1;

    @Generated
    public static AutoToolModule getInstance() {
        return instance;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            int bestToolSlot;
            if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1761 == null || mc.field_1724.method_7337()) {
                this.lastSlot = -1;
                return;
            }
            boolean breaking = mc.field_1761.method_2923();
            if (breaking && this.lastSlot == -1) {
                this.lastSlot = mc.field_1724.method_31548().field_7545;
            }
            class_1661 class_1661VarMethod_31548 = mc.field_1724.method_31548();
            if (breaking) {
                bestToolSlot = getBestToolSlot();
            } else {
                bestToolSlot = this.lastSlot != -1 ? this.lastSlot : mc.field_1724.method_31548().field_7545;
            }
            class_1661VarMethod_31548.field_7545 = bestToolSlot;
            if (!breaking) {
                this.lastSlot = -1;
            }
        }));
        addEvents(updateEvent);
    }

    private int getBestToolSlot() {
        class_3965 class_3965Var = mc.field_1765;
        if (!(class_3965Var instanceof class_3965)) {
            return mc.field_1724.method_31548().field_7545;
        }
        class_3965 hit = class_3965Var;
        class_2248 block = mc.field_1687.method_8320(hit.method_17777()).method_26204();
        int bestSlot = mc.field_1724.method_31548().field_7545;
        float bestSpeed = 1.0f;
        for (int i = 0; i < 9; i++) {
            float speed = mc.field_1724.method_31548().method_5438(i).method_7924(block.method_9564());
            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = i;
            }
        }
        return bestSlot;
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        this.lastSlot = -1;
    }
}
