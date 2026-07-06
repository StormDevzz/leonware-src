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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/WindJumpModule.class */
@ModuleRegister(name = "Wind Jump", category = Category.PLAYER)
public class WindJumpModule extends Module {
    private static final WindJumpModule instance = new WindJumpModule();
    private final BindSetting windChargeBind = new BindSetting("Wind Charge Key").value((Integer) (-999));

    @Generated
    public static WindJumpModule getInstance() {
        return instance;
    }

    public WindJumpModule() {
        addSettings(this.windChargeBind);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener keyEvent = KeyEvent.getInstance().subscribe(new Listener(event -> {
            int slot;
            if (mc.field_1724 != null && mc.field_1687 != null && mc.field_1755 == null && event.key() == this.windChargeBind.getValue().intValue() && event.action() == 1 && (slot = InventoryUtil.findItem(class_1802.field_49098, true)) != -1) {
                int oldSlot = mc.field_1724.method_31548().field_7545;
                InventoryUtil.swapToSlot(slot);
                InventoryUtil.useItem(class_1268.field_5808);
                InventoryUtil.swapToSlot(oldSlot);
            }
        }));
        addEvents(keyEvent);
    }
}
