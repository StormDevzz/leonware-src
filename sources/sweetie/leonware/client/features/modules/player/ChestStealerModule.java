package sweetie.leonware.client.features.modules.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import lombok.Generated;
import net.minecraft.class_1703;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1722;
import net.minecraft.class_1733;
import net.minecraft.class_1799;
import net.minecraft.class_476;
import net.minecraft.class_488;
import net.minecraft.class_495;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/ChestStealerModule.class */
@ModuleRegister(name = "Chest Stealer", category = Category.PLAYER)
public class ChestStealerModule extends Module {
    private static final ChestStealerModule instance = new ChestStealerModule();
    private final SliderSetting delay = new SliderSetting("Задержка").value(Float.valueOf(50.0f)).range(0.0f, 1000.0f).step(10.0f);
    private final BooleanSetting randomize = new BooleanSetting("Рандомизация").value((Boolean) false);
    private final Random random = new Random();
    private long lastClickTime = 0;

    @Generated
    public static ChestStealerModule getInstance() {
        return instance;
    }

    public ChestStealerModule() {
        addSettings(this.delay, this.randomize);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        this.lastClickTime = 0L;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            class_1707 class_1707Var;
            int chestSize;
            if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1761 == null || mc.field_1755 == null) {
                return;
            }
            class_476 class_476Var = mc.field_1755;
            if (class_476Var instanceof class_476) {
                class_476 screen = class_476Var;
                class_1707 h = screen.method_17577();
                class_1707Var = h;
                chestSize = h.method_17388() * 9;
            } else {
                class_495 class_495Var = mc.field_1755;
                if (class_495Var instanceof class_495) {
                    class_495 screen2 = class_495Var;
                    class_1707Var = (class_1733) screen2.method_17577();
                    chestSize = 27;
                } else {
                    class_488 class_488Var = mc.field_1755;
                    if (class_488Var instanceof class_488) {
                        class_488 screen3 = class_488Var;
                        class_1707Var = (class_1722) screen3.method_17577();
                        chestSize = 5;
                    } else {
                        return;
                    }
                }
            }
            long currentTime = System.currentTimeMillis();
            if (currentTime - this.lastClickTime < this.delay.getValue().longValue()) {
                return;
            }
            List<Integer> indexes = new ArrayList<>(chestSize);
            for (int i = 0; i < chestSize; i++) {
                class_1799 stack = class_1707Var.method_7611(i).method_7677();
                if (!stack.method_7960()) {
                    indexes.add(Integer.valueOf(i));
                }
            }
            if (indexes.isEmpty()) {
                return;
            }
            if (this.randomize.getValue().booleanValue()) {
                Collections.shuffle(indexes, this.random);
            }
            int slot = indexes.get(0).intValue();
            mc.field_1761.method_2906(((class_1703) class_1707Var).field_7763, slot, 0, class_1713.field_7794, mc.field_1724);
            this.lastClickTime = currentTime;
        }));
        addEvents(updateEvent);
    }
}
