/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1268
 *  net.minecraft.class_1657
 *  net.minecraft.class_1802
 *  net.minecraft.class_304
 *  net.minecraft.class_3675$class_306
 */
package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1657;
import net.minecraft.class_1802;
import net.minecraft.class_304;
import net.minecraft.class_3675;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="Auto GApple", category=Category.COMBAT)
public class AutoGAppleModule
extends Module {
    private static final AutoGAppleModule instance = new AutoGAppleModule();
    private final SliderSetting health = new SliderSetting("Health").value(Float.valueOf(18.0f)).range(4.0f, 20.0f).step(1.0f);
    private final BooleanSetting useEnchanted = new BooleanSetting("Use enchanted").value(true);
    private boolean active;

    public AutoGAppleModule() {
        this.addSettings(this.health, this.useEnchanted);
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            boolean validItem;
            boolean bl = validItem = AutoGAppleModule.mc.field_1724.method_6079().method_7909() == class_1802.field_8463 || (Boolean)this.useEnchanted.getValue() != false && AutoGAppleModule.mc.field_1724.method_6079().method_7909() == class_1802.field_8367;
            if (validItem && AutoGAppleModule.mc.field_1724.method_6032() <= ((Float)this.health.getValue()).floatValue()) {
                this.active = true;
                if (!AutoGAppleModule.mc.field_1724.method_6115()) {
                    AutoGAppleModule.mc.field_1761.method_2919((class_1657)AutoGAppleModule.mc.field_1724, class_1268.field_5810);
                    class_304.method_1416((class_3675.class_306)AutoGAppleModule.mc.field_1690.field_1904.method_1429(), (boolean)true);
                    AutoGAppleModule.mc.field_1724.method_6019(class_1268.field_5810);
                }
            } else if (this.active && AutoGAppleModule.mc.field_1724.method_6115()) {
                AutoGAppleModule.mc.field_1761.method_2897((class_1657)AutoGAppleModule.mc.field_1724);
                if (!AutoGAppleModule.mc.field_1729.method_1609() || AutoGAppleModule.mc.field_1755 != null) {
                    class_304.method_1416((class_3675.class_306)AutoGAppleModule.mc.field_1690.field_1904.method_1429(), (boolean)false);
                }
                this.active = false;
            }
        }));
        this.addEvents(updateEvent);
    }

    @Generated
    public static AutoGAppleModule getInstance() {
        return instance;
    }
}

