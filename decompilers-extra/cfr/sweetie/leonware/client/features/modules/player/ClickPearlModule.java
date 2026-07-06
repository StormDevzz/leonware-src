/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1802
 */
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

@ModuleRegister(name="Click Pearl", category=Category.PLAYER)
public class ClickPearlModule
extends Module {
    private static final ClickPearlModule instance = new ClickPearlModule();
    private final BindSetting throwKey = new BindSetting("Throw key").value(-999);
    private final BooleanSetting legit = new BooleanSetting("Legit").value(false);
    private final InventoryUtil.ItemUsage itemUsage = new InventoryUtil.ItemUsage(class_1802.field_8634, this);

    public ClickPearlModule() {
        this.addSettings(this.throwKey, this.legit);
    }

    @Override
    public void onDisable() {
        this.itemUsage.onDisable();
    }

    @Override
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> this.handle(!SlownessManager.isEnabled())));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> this.handle(SlownessManager.isEnabled())));
        this.addEvents(tickEvent, updateEvent);
    }

    public boolean isThrowingPearl() {
        return this.itemUsage.isForceUse();
    }

    private void handle(boolean tick) {
        if (tick) {
            return;
        }
        this.itemUsage.handleUse((Integer)this.throwKey.getValue(), (Boolean)this.legit.getValue());
    }

    @Generated
    public static ClickPearlModule getInstance() {
        return instance;
    }
}

