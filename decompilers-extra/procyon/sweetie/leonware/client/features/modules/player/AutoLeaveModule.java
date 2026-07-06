// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import java.util.Iterator;
import sweetie.leonware.api.system.configs.FriendManager;
import net.minecraft.class_742;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Auto Leave", category = Category.PLAYER)
public class AutoLeaveModule extends Module
{
    private static final AutoLeaveModule instance;
    private final SliderSetting distance;
    private final ModeSetting action;
    
    public AutoLeaveModule() {
        this.distance = new SliderSetting("Distance").value(50.0f).range(1.0f, 100.0f).step(1.0f);
        this.action = new ModeSetting("Action").value("Spawn").values("Hub", "Spawn", "Home");
        this.addSettings(this.distance, this.action);
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> this.handleUpdateEvent()));
        this.addEvents(updateEvent);
    }
    
    private void handleUpdateEvent() {
        for (final class_742 player : AutoLeaveModule.mc.field_1687.method_18456()) {
            if (AutoLeaveModule.mc.field_1724 == player) {
                continue;
            }
            if (FriendManager.getInstance().contains(player.method_5477().getString())) {
                continue;
            }
            if (player.method_19538().method_1022(AutoLeaveModule.mc.field_1724.method_19538()) <= this.distance.getValue()) {
                this.handleLeave();
                this.toggle();
                break;
            }
        }
    }
    
    private void handleLeave() {
        final String s = this.action.getValue();
        switch (s) {
            case "Hub": {
                AutoLeaveModule.mc.field_1724.field_3944.method_45730("hub");
                break;
            }
            case "Spawn": {
                AutoLeaveModule.mc.field_1724.field_3944.method_45730("spawn");
                break;
            }
            case "Home": {
                AutoLeaveModule.mc.field_1724.field_3944.method_45730("home home");
                break;
            }
        }
    }
    
    @Generated
    public static AutoLeaveModule getInstance() {
        return AutoLeaveModule.instance;
    }
    
    static {
        instance = new AutoLeaveModule();
    }
}
