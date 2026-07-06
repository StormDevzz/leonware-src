package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1802;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BindSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.utils.other.SlownessManager;
import sweetie.leonware.api.utils.player.InventoryUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/ClickPearlModule.class */
@ModuleRegister(name = "Click Pearl", category = Category.PLAYER)
public class ClickPearlModule extends Module {
    private static final ClickPearlModule instance = new ClickPearlModule();
    private final BindSetting throwKey = new BindSetting("Throw key").value((Integer) (-999));
    private final BooleanSetting legit = new BooleanSetting("Legit").value((Boolean) false);
    private final InventoryUtil.ItemUsage itemUsage = new InventoryUtil.ItemUsage(class_1802.field_8634, this);

    @Generated
    public static ClickPearlModule getInstance() {
        return instance;
    }

    public ClickPearlModule() {
        addSettings(this.throwKey, this.legit);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        this.itemUsage.onDisable();
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener(event -> {
            handle(!SlownessManager.isEnabled());
        }));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event2 -> {
            handle(SlownessManager.isEnabled());
        }));
        addEvents(tickEvent, updateEvent);
    }

    public boolean isThrowingPearl() {
        return this.itemUsage.isForceUse();
    }

    private void handle(boolean tick) {
        if (tick) {
            return;
        }
        this.itemUsage.handleUse(this.throwKey.getValue().intValue(), this.legit.getValue().booleanValue());
    }
}
