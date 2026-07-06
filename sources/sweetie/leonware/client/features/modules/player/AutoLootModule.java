package sweetie.leonware.client.features.modules.player;

import java.util.Set;
import lombok.Generated;
import net.minecraft.class_1713;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_476;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/AutoLootModule.class */
@ModuleRegister(name = "Auto Loot", category = Category.PLAYER)
public class AutoLootModule extends Module {
    private final BooleanSetting antiFlag = new BooleanSetting("AntiFlag").value((Boolean) true);
    private final SliderSetting delay = new SliderSetting("Задержка").value(Float.valueOf(1.0f)).range(0.0f, 100.0f).step(1.0f);
    private long lastClickTime = 0;
    private static final AutoLootModule instance = new AutoLootModule();
    private static final Set<class_1792> TARGET_ITEMS = Set.of((Object[]) new class_1792[]{class_1802.field_8805, class_1802.field_8058, class_1802.field_8348, class_1802.field_8285, class_1802.field_8802, class_1802.field_8601, class_1802.field_8144, class_1802.field_8279, class_1802.field_46250, class_1802.field_8137, class_1802.field_8425, class_1802.field_8834});

    @Generated
    public static AutoLootModule getInstance() {
        return instance;
    }

    public AutoLootModule() {
        addSettings(this.antiFlag, this.delay);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            class_476 class_476Var = mc.field_1755;
            if (class_476Var instanceof class_476) {
                class_476 screen = class_476Var;
                int chestSize = screen.method_17577().method_17388() * 9;
                for (int i = 0; i < chestSize; i++) {
                    class_1799 stack = screen.method_17577().method_7611(i).method_7677();
                    if (!stack.method_7960() && isTargetItem(stack)) {
                        if (this.antiFlag.getValue().booleanValue()) {
                            long currentTime = System.currentTimeMillis();
                            if (currentTime - this.lastClickTime < this.delay.getValue().longValue()) {
                                return;
                            }
                        }
                        mc.field_1761.method_2906(screen.method_17577().field_7763, i, 0, class_1713.field_7794, mc.field_1724);
                        this.lastClickTime = System.currentTimeMillis();
                        if (this.antiFlag.getValue().booleanValue()) {
                            return;
                        }
                    }
                }
            }
        }));
        addEvents(updateEvent);
    }

    private boolean isTargetItem(class_1799 stack) {
        return TARGET_ITEMS.contains(stack.method_7909());
    }
}
