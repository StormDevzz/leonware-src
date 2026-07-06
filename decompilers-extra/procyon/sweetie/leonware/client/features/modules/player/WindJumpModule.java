// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_1268;
import sweetie.leonware.api.utils.player.InventoryUtil;
import net.minecraft.class_1802;
import sweetie.leonware.api.event.events.client.KeyEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.BindSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Wind Jump", category = Category.PLAYER)
public class WindJumpModule extends Module
{
    private static final WindJumpModule instance;
    private final BindSetting windChargeBind;
    
    public WindJumpModule() {
        this.windChargeBind = new BindSetting("Wind Charge Key").value(-999);
        this.addSettings(this.windChargeBind);
    }
    
    @Override
    public void onEvent() {
        final EventListener keyEvent = KeyEvent.getInstance().subscribe(new Listener<KeyEvent.KeyEventData>(event -> {
            if (WindJumpModule.mc.field_1724 == null || WindJumpModule.mc.field_1687 == null || WindJumpModule.mc.field_1755 != null) {
                return;
            }
            else {
                if (event.key() == this.windChargeBind.getValue() && event.action() == 1) {
                    final int slot = InventoryUtil.findItem(class_1802.field_49098, true);
                    if (slot != -1) {
                        final int oldSlot = WindJumpModule.mc.field_1724.method_31548().field_7545;
                        InventoryUtil.swapToSlot(slot);
                        InventoryUtil.useItem(class_1268.field_5808);
                        InventoryUtil.swapToSlot(oldSlot);
                    }
                }
                return;
            }
        }));
        this.addEvents(keyEvent);
    }
    
    @Generated
    public static WindJumpModule getInstance() {
        return WindJumpModule.instance;
    }
    
    static {
        instance = new WindJumpModule();
    }
}
