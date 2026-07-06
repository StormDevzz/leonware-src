/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.client.features.modules.movement.spider;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.move.MotionEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.system.backend.Choice;
import sweetie.leonware.client.features.modules.movement.spider.SpiderMode;
import sweetie.leonware.client.features.modules.movement.spider.modes.SpiderFunTime;
import sweetie.leonware.client.features.modules.movement.spider.modes.SpiderMatrix;

@ModuleRegister(name="Spider", category=Category.MOVEMENT)
public class SpiderModule
extends Module {
    private static final SpiderModule instance = new SpiderModule();
    private final SpiderFunTime spiderFunTime = new SpiderFunTime();
    private final SpiderMatrix spiderMatrix = new SpiderMatrix(() -> this.getMode().is("Matrix"));
    private final SpiderMode[] modes = new SpiderMode[]{this.spiderFunTime, this.spiderMatrix};
    private SpiderMode currentMode = this.spiderFunTime;
    private final ModeSetting mode = new ModeSetting("Mode").value(this.spiderFunTime.getName()).values(Choice.getValues(this.modes)).onAction(() -> {
        this.currentMode = (SpiderMode)Choice.getChoiceByName((String)this.getMode().getValue(), this.modes);
    });

    public SpiderModule() {
        this.addSettings(this.mode);
        for (SpiderMode spiderMode : this.modes) {
            this.addSettings(spiderMode.getSettings());
        }
    }

    @Override
    public void onEvent() {
        EventListener motionEvent = MotionEvent.getInstance().subscribe(new Listener<MotionEvent.MotionEventData>(event -> this.currentMode.onMotion((MotionEvent.MotionEventData)event)));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> this.currentMode.onUpdate()));
        this.addEvents(motionEvent, updateEvent);
    }

    @Generated
    public static SpiderModule getInstance() {
        return instance;
    }

    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }
}

