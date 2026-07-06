// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import net.minecraft.class_1297;
import sweetie.leonware.api.utils.neuro.DataCollector;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Recorder", category = Category.OTHER)
public class RecorderModule extends Module
{
    private static final RecorderModule instance;
    private final DataCollector dataCollector;
    private class_1297 target;
    
    public RecorderModule() {
        this.dataCollector = new DataCollector();
    }
    
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
        final EventListener attackEvent = AttackEvent.getInstance().subscribe(new Listener<AttackEvent.AttackEventData>(event -> {
            if (this.target != event.entity()) {
                this.target = event.entity();
            }
            if (this.target != null) {
                this.dataCollector.onAttack(event);
            }
            return;
        }));
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (this.target != null) {
                this.dataCollector.onUpdate();
            }
            return;
        }));
        this.addEvents(attackEvent, updateEvent);
    }
    
    @Generated
    public static RecorderModule getInstance() {
        return RecorderModule.instance;
    }
    
    static {
        instance = new RecorderModule();
    }
}
