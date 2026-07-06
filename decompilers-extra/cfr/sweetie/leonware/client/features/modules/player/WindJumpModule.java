/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1268
 *  net.minecraft.class_1802
 */
package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.KeyEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BindSetting;
import sweetie.leonware.api.utils.player.InventoryUtil;

@ModuleRegister(name="Wind Jump", category=Category.PLAYER)
public class WindJumpModule
extends Module {
    private static final WindJumpModule instance = new WindJumpModule();
    private final BindSetting windChargeBind = new BindSetting("Wind Charge Key").value(-999);

    public WindJumpModule() {
        this.addSettings(this.windChargeBind);
    }

    @Override
    public void onEvent() {
        EventListener keyEvent = KeyEvent.getInstance().subscribe(new Listener<KeyEvent.KeyEventData>(event -> {
            int slot;
            if (WindJumpModule.mc.field_1724 == null || WindJumpModule.mc.field_1687 == null || WindJumpModule.mc.field_1755 != null) {
                return;
            }
            if (event.key() == ((Integer)this.windChargeBind.getValue()).intValue() && event.action() == 1 && (slot = InventoryUtil.findItem(class_1802.field_49098, true)) != -1) {
                int oldSlot = WindJumpModule.mc.field_1724.method_31548().field_7545;
                InventoryUtil.swapToSlot(slot);
                InventoryUtil.useItem(class_1268.field_5808);
                InventoryUtil.swapToSlot(oldSlot);
            }
        }));
        this.addEvents(keyEvent);
    }

    @Generated
    public static WindJumpModule getInstance() {
        return instance;
    }
}

