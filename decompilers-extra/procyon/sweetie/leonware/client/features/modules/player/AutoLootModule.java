// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import net.minecraft.class_1802;
import lombok.Generated;
import net.minecraft.class_1799;
import net.minecraft.class_437;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1707;
import net.minecraft.class_476;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import net.minecraft.class_1792;
import java.util.Set;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Auto Loot", category = Category.PLAYER)
public class AutoLootModule extends Module
{
    private static final AutoLootModule instance;
    private final BooleanSetting antiFlag;
    private final SliderSetting delay;
    private long lastClickTime;
    private static final Set<class_1792> TARGET_ITEMS;
    
    public AutoLootModule() {
        this.antiFlag = new BooleanSetting("AntiFlag").value(true);
        this.delay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430").value(1.0f).range(0.0f, 100.0f).step(1.0f);
        this.lastClickTime = 0L;
        this.addSettings(this.antiFlag, this.delay);
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (AutoLootModule.mc.field_1724 == null || AutoLootModule.mc.field_1687 == null) {
                return;
            }
            else {
                final class_437 patt0$temp = AutoLootModule.mc.field_1755;
                if (patt0$temp instanceof final class_476 screen) {
                    for (int chestSize = ((class_1707)screen.method_17577()).method_17388() * 9, i = 0; i < chestSize; ++i) {
                        final class_1799 stack = ((class_1707)screen.method_17577()).method_7611(i).method_7677();
                        if (!stack.method_7960() && this.isTargetItem(stack)) {
                            if (this.antiFlag.getValue()) {
                                final long currentTime = System.currentTimeMillis();
                                if (currentTime - this.lastClickTime < this.delay.getValue().longValue()) {
                                    return;
                                }
                            }
                            AutoLootModule.mc.field_1761.method_2906(((class_1707)screen.method_17577()).field_7763, i, 0, class_1713.field_7794, (class_1657)AutoLootModule.mc.field_1724);
                            this.lastClickTime = System.currentTimeMillis();
                            if (this.antiFlag.getValue()) {
                                return;
                            }
                        }
                    }
                }
                return;
            }
        }));
        this.addEvents(updateEvent);
    }
    
    private boolean isTargetItem(final class_1799 stack) {
        return AutoLootModule.TARGET_ITEMS.contains(stack.method_7909());
    }
    
    @Generated
    public static AutoLootModule getInstance() {
        return AutoLootModule.instance;
    }
    
    static {
        instance = new AutoLootModule();
        TARGET_ITEMS = Set.of(new class_1792[] { class_1802.field_8805, class_1802.field_8058, class_1802.field_8348, class_1802.field_8285, class_1802.field_8802, class_1802.field_8601, class_1802.field_8144, class_1802.field_8279, class_1802.field_46250, class_1802.field_8137, class_1802.field_8425, class_1802.field_8834 });
    }
}
