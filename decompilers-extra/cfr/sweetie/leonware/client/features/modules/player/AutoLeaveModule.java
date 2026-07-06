/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_742
 */
package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_742;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.configs.FriendManager;

@ModuleRegister(name="Auto Leave", category=Category.PLAYER)
public class AutoLeaveModule
extends Module {
    private static final AutoLeaveModule instance = new AutoLeaveModule();
    private final SliderSetting distance = new SliderSetting("Distance").value(Float.valueOf(50.0f)).range(1.0f, 100.0f).step(1.0f);
    private final ModeSetting action = new ModeSetting("Action").value("Spawn").values("Hub", "Spawn", "Home");

    public AutoLeaveModule() {
        this.addSettings(this.distance, this.action);
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> this.handleUpdateEvent()));
        this.addEvents(updateEvent);
    }

    private void handleUpdateEvent() {
        for (class_742 player : AutoLeaveModule.mc.field_1687.method_18456()) {
            if (AutoLeaveModule.mc.field_1724 == player || FriendManager.getInstance().contains(player.method_5477().getString()) || !(player.method_19538().method_1022(AutoLeaveModule.mc.field_1724.method_19538()) <= (double)((Float)this.distance.getValue()).floatValue())) continue;
            this.handleLeave();
            this.toggle();
            break;
        }
    }

    private void handleLeave() {
        switch ((String)this.action.getValue()) {
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
            }
        }
    }

    @Generated
    public static AutoLeaveModule getInstance() {
        return instance;
    }
}

