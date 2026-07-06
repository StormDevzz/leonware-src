/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 */
package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_1297;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.utils.neuro.DataCollector;

@ModuleRegister(name="Recorder", category=Category.OTHER)
public class RecorderModule
extends Module {
    private static final RecorderModule instance = new RecorderModule();
    private final DataCollector dataCollector = new DataCollector();
    private class_1297 target;

    @Override
    public void onDisable() {
        this.target = null;
        this.dataCollector.stopCollecting();
    }

    @Override
    public void onEnable() {
        this.dataCollector.startCollecting();
    }

    @Override
    public void onEvent() {
        EventListener attackEvent = AttackEvent.getInstance().subscribe(new Listener<AttackEvent.AttackEventData>(event -> {
            if (this.target != event.entity()) {
                this.target = event.entity();
            }
            if (this.target != null) {
                this.dataCollector.onAttack((AttackEvent.AttackEventData)event);
            }
        }));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (this.target != null) {
                this.dataCollector.onUpdate();
            }
        }));
        this.addEvents(attackEvent, updateEvent);
    }

    @Generated
    public static RecorderModule getInstance() {
        return instance;
    }
}

