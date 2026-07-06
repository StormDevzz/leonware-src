// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.utils.other.SlownessManager;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.setting.Setting;
import net.minecraft.class_1802;
import sweetie.leonware.api.utils.player.InventoryUtil;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.BindSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Click Pearl", category = Category.PLAYER)
public class ClickPearlModule extends Module
{
    private static final ClickPearlModule instance;
    private final BindSetting throwKey;
    private final BooleanSetting legit;
    private final InventoryUtil.ItemUsage itemUsage;
    
    public ClickPearlModule() {
        this.throwKey = new BindSetting("Throw key").value(-999);
        this.legit = new BooleanSetting("Legit").value(false);
        this.itemUsage = new InventoryUtil.ItemUsage(class_1802.field_8634, this);
        this.addSettings(this.throwKey, this.legit);
    }
    
    @Override
    public void onDisable() {
        this.itemUsage.onDisable();
    }
    
    @Override
    public void onEvent() {
        final EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> this.handle(!SlownessManager.isEnabled())));
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> this.handle(SlownessManager.isEnabled())));
        this.addEvents(tickEvent, updateEvent);
    }
    
    public boolean isThrowingPearl() {
        return this.itemUsage.isForceUse();
    }
    
    private void handle(final boolean tick) {
        if (tick) {
            return;
        }
        this.itemUsage.handleUse(this.throwKey.getValue(), this.legit.getValue());
    }
    
    @Generated
    public static ClickPearlModule getInstance() {
        return ClickPearlModule.instance;
    }
    
    static {
        instance = new ClickPearlModule();
    }
}
