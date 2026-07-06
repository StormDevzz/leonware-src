package sweetie.leonware.client.features.modules.other;

import java.util.function.Supplier;
import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.math.TimerUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/other/TapeMouseModule.class */
@ModuleRegister(name = "Tape Mouse", category = Category.OTHER)
public class TapeMouseModule extends Module {
    private static final TapeMouseModule instance = new TapeMouseModule();
    private final MultiBooleanSetting actions = new MultiBooleanSetting("Actions").value(new BooleanSetting("Attack").value((Boolean) true), new BooleanSetting("Use").value((Boolean) false));
    private final Supplier<Boolean> isAttack = () -> {
        return Boolean.valueOf(this.actions.isEnabled("Attack"));
    };
    private final Supplier<Boolean> isUse = () -> {
        return Boolean.valueOf(this.actions.isEnabled("Use"));
    };
    private final SliderSetting attackDelay = new SliderSetting("Attack delay").value(Float.valueOf(10.0f)).range(1.0f, 20.0f).step(1.0f).setVisible(this.isAttack);
    private final SliderSetting useDelay = new SliderSetting("Use delay").value(Float.valueOf(10.0f)).range(1.0f, 20.0f).step(1.0f).setVisible(this.isUse);
    private final TimerUtil attackTimer = new TimerUtil();
    private final TimerUtil useTimer = new TimerUtil();

    @Generated
    public static TapeMouseModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v10, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v15, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public TapeMouseModule() {
        addSettings(this.actions, this.attackDelay, this.useDelay);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener(event -> {
            if (this.isAttack.get().booleanValue()) {
                handleAction(this.attackDelay.getValue().floatValue(), this.attackTimer, () -> {
                    mc.method_1536();
                });
            }
            if (this.isUse.get().booleanValue()) {
                handleAction(this.useDelay.getValue().floatValue(), this.useTimer, () -> {
                    mc.method_1583();
                });
            }
        }));
        addEvents(tickEvent);
    }

    private void handleAction(float delay, TimerUtil timer, Runnable run) {
        if (timer.finished(delay * 50.0f)) {
            run.run();
            timer.reset();
        }
    }
}
