/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1657
 *  net.minecraft.class_1707
 *  net.minecraft.class_1713
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_437
 *  net.minecraft.class_476
 */
package sweetie.leonware.client.features.modules.player;

import java.util.Set;
import lombok.Generated;
import net.minecraft.class_1657;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_437;
import net.minecraft.class_476;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="Auto Loot", category=Category.PLAYER)
public class AutoLootModule
extends Module {
    private static final AutoLootModule instance = new AutoLootModule();
    private final BooleanSetting antiFlag = new BooleanSetting("AntiFlag").value(true);
    private final SliderSetting delay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430").value(Float.valueOf(1.0f)).range(0.0f, 100.0f).step(1.0f);
    private long lastClickTime = 0L;
    private static final Set<class_1792> TARGET_ITEMS = Set.of(class_1802.field_8805, class_1802.field_8058, class_1802.field_8348, class_1802.field_8285, class_1802.field_8802, class_1802.field_8601, class_1802.field_8144, class_1802.field_8279, class_1802.field_46250, class_1802.field_8137, class_1802.field_8425, class_1802.field_8834);

    public AutoLootModule() {
        this.addSettings(this.antiFlag, this.delay);
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (AutoLootModule.mc.field_1724 == null || AutoLootModule.mc.field_1687 == null) {
                return;
            }
            class_437 patt0$temp = AutoLootModule.mc.field_1755;
            if (patt0$temp instanceof class_476) {
                class_476 screen = (class_476)patt0$temp;
                int chestSize = ((class_1707)screen.method_17577()).method_17388() * 9;
                for (int i = 0; i < chestSize; ++i) {
                    long currentTime;
                    class_1799 stack = ((class_1707)screen.method_17577()).method_7611(i).method_7677();
                    if (stack.method_7960() || !this.isTargetItem(stack)) continue;
                    if (((Boolean)this.antiFlag.getValue()).booleanValue() && (currentTime = System.currentTimeMillis()) - this.lastClickTime < ((Float)this.delay.getValue()).longValue()) {
                        return;
                    }
                    AutoLootModule.mc.field_1761.method_2906(((class_1707)screen.method_17577()).field_7763, i, 0, class_1713.field_7794, (class_1657)AutoLootModule.mc.field_1724);
                    this.lastClickTime = System.currentTimeMillis();
                    if (!((Boolean)this.antiFlag.getValue()).booleanValue()) continue;
                    return;
                }
            }
        }));
        this.addEvents(updateEvent);
    }

    private boolean isTargetItem(class_1799 stack) {
        return TARGET_ITEMS.contains(stack.method_7909());
    }

    @Generated
    public static AutoLootModule getInstance() {
        return instance;
    }
}

