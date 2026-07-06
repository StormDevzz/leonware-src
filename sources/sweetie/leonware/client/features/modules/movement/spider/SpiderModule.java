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
import sweetie.leonware.client.features.modules.movement.spider.modes.SpiderFunTime;
import sweetie.leonware.client.features.modules.movement.spider.modes.SpiderMatrix;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/spider/SpiderModule.class */
@ModuleRegister(name = "Spider", category = Category.MOVEMENT)
public class SpiderModule extends Module {
    private static final SpiderModule instance = new SpiderModule();
    private final SpiderFunTime spiderFunTime = new SpiderFunTime();
    private final SpiderMatrix spiderMatrix = new SpiderMatrix(() -> {
        return Boolean.valueOf(getMode().is("Matrix"));
    });
    private final SpiderMode[] modes = {this.spiderFunTime, this.spiderMatrix};
    private SpiderMode currentMode = this.spiderFunTime;
    private final ModeSetting mode = new ModeSetting("Mode").value(this.spiderFunTime.getName()).values(Choice.getValues(this.modes)).onAction2(() -> {
        this.currentMode = (SpiderMode) Choice.getChoiceByName(getMode().getValue(), (Choice[]) this.modes);
    });

    @Generated
    public static SpiderModule getInstance() {
        return instance;
    }

    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }

    /* JADX WARN: Type inference failed for: r1v9, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    public SpiderModule() {
        addSettings(this.mode);
        for (SpiderMode spiderMode : this.modes) {
            addSettings(spiderMode.getSettings());
        }
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener motionEvent = MotionEvent.getInstance().subscribe(new Listener(event -> {
            this.currentMode.onMotion(event);
        }));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event2 -> {
            this.currentMode.onUpdate();
        }));
        addEvents(motionEvent, updateEvent);
    }
}
