/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1657
 *  net.minecraft.class_1707
 *  net.minecraft.class_1713
 *  net.minecraft.class_1722
 *  net.minecraft.class_1733
 *  net.minecraft.class_1799
 *  net.minecraft.class_437
 *  net.minecraft.class_476
 *  net.minecraft.class_488
 *  net.minecraft.class_495
 */
package sweetie.leonware.client.features.modules.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import lombok.Generated;
import net.minecraft.class_1657;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1722;
import net.minecraft.class_1733;
import net.minecraft.class_1799;
import net.minecraft.class_437;
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

@ModuleRegister(name="Chest Stealer", category=Category.PLAYER)
public class ChestStealerModule
extends Module {
    private static final ChestStealerModule instance = new ChestStealerModule();
    private final SliderSetting delay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430").value(Float.valueOf(50.0f)).range(0.0f, 1000.0f).step(10.0f);
    private final BooleanSetting randomize = new BooleanSetting("\u0420\u0430\u043d\u0434\u043e\u043c\u0438\u0437\u0430\u0446\u0438\u044f").value(false);
    private final Random random = new Random();
    private long lastClickTime = 0L;

    public ChestStealerModule() {
        this.addSettings(this.delay, this.randomize);
    }

    @Override
    public void onEnable() {
        this.lastClickTime = 0L;
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            int chestSize;
            class_1707 handler;
            if (ChestStealerModule.mc.field_1724 == null || ChestStealerModule.mc.field_1687 == null || ChestStealerModule.mc.field_1761 == null) {
                return;
            }
            if (ChestStealerModule.mc.field_1755 == null) {
                return;
            }
            class_437 patt0$temp = ChestStealerModule.mc.field_1755;
            if (patt0$temp instanceof class_476) {
                class_476 screen = (class_476)patt0$temp;
                handler = h = (class_1707)screen.method_17577();
                chestSize = h.method_17388() * 9;
            } else {
                class_437 patt1$temp = ChestStealerModule.mc.field_1755;
                if (patt1$temp instanceof class_495) {
                    class_495 screen = (class_495)patt1$temp;
                    handler = h = (class_1733)screen.method_17577();
                    chestSize = 27;
                } else {
                    class_437 patt2$temp = ChestStealerModule.mc.field_1755;
                    if (patt2$temp instanceof class_488) {
                        class_488 screen = (class_488)patt2$temp;
                        handler = h = (class_1722)screen.method_17577();
                        chestSize = 5;
                    } else {
                        return;
                    }
                }
            }
            long currentTime = System.currentTimeMillis();
            if (currentTime - this.lastClickTime < ((Float)this.delay.getValue()).longValue()) {
                return;
            }
            ArrayList<Integer> indexes = new ArrayList<Integer>(chestSize);
            for (int i = 0; i < chestSize; ++i) {
                class_1799 stack = handler.method_7611(i).method_7677();
                if (stack.method_7960()) continue;
                indexes.add(i);
            }
            if (indexes.isEmpty()) {
                return;
            }
            if (((Boolean)this.randomize.getValue()).booleanValue()) {
                Collections.shuffle(indexes, this.random);
            }
            int slot = (Integer)indexes.get(0);
            ChestStealerModule.mc.field_1761.method_2906(handler.field_7763, slot, 0, class_1713.field_7794, (class_1657)ChestStealerModule.mc.field_1724);
            this.lastClickTime = currentTime;
        }));
        this.addEvents(updateEvent);
    }

    @Generated
    public static ChestStealerModule getInstance() {
        return instance;
    }
}

