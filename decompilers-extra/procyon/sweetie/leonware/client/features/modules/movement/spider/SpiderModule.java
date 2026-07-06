// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement.spider;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.move.MotionEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.system.backend.Choice;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.client.features.modules.movement.spider.modes.SpiderMatrix;
import sweetie.leonware.client.features.modules.movement.spider.modes.SpiderFunTime;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Spider", category = Category.MOVEMENT)
public class SpiderModule extends Module
{
    private static final SpiderModule instance;
    private final SpiderFunTime spiderFunTime;
    private final SpiderMatrix spiderMatrix;
    private final SpiderMode[] modes;
    private SpiderMode currentMode;
    private final ModeSetting mode;
    
    public SpiderModule() {
        this.spiderFunTime = new SpiderFunTime();
        this.spiderMatrix = new SpiderMatrix(() -> this.getMode().is("Matrix"));
        this.modes = new SpiderMode[] { this.spiderFunTime, this.spiderMatrix };
        this.currentMode = this.spiderFunTime;
        this.mode = new ModeSetting("Mode").value(this.spiderFunTime.getName()).values(Choice.getValues((Choice[])this.modes)).onAction(() -> this.currentMode = (SpiderMode)Choice.getChoiceByName((String)this.getMode().getValue(), (Choice[])this.modes));
        this.addSettings(this.mode);
        for (final SpiderMode spiderMode : this.modes) {
            this.addSettings(spiderMode.getSettings());
        }
    }
    
    @Override
    public void onEvent() {
        final EventListener motionEvent = MotionEvent.getInstance().subscribe(new Listener<MotionEvent.MotionEventData>(event -> this.currentMode.onMotion(event)));
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> this.currentMode.onUpdate()));
        this.addEvents(motionEvent, updateEvent);
    }
    
    @Generated
    public static SpiderModule getInstance() {
        return SpiderModule.instance;
    }
    
    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }
    
    static {
        instance = new SpiderModule();
    }
}
