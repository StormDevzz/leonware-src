/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
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

@ModuleRegister(name="Tape Mouse", category=Category.OTHER)
public class TapeMouseModule
extends Module {
    private static final TapeMouseModule instance = new TapeMouseModule();
    private final MultiBooleanSetting actions = new MultiBooleanSetting("Actions").value(new BooleanSetting("Attack").value(true), new BooleanSetting("Use").value(false));
    private final Supplier<Boolean> isAttack = () -> this.actions.isEnabled("Attack");
    private final Supplier<Boolean> isUse = () -> this.actions.isEnabled("Use");
    private final SliderSetting attackDelay = new SliderSetting("Attack delay").value(Float.valueOf(10.0f)).range(1.0f, 20.0f).step(1.0f).setVisible((Supplier)this.isAttack);
    private final SliderSetting useDelay = new SliderSetting("Use delay").value(Float.valueOf(10.0f)).range(1.0f, 20.0f).step(1.0f).setVisible((Supplier)this.isUse);
    private final TimerUtil attackTimer = new TimerUtil();
    private final TimerUtil useTimer = new TimerUtil();

    public TapeMouseModule() {
        this.addSettings(this.actions, this.attackDelay, this.useDelay);
    }

    @Override
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            if (this.isAttack.get().booleanValue()) {
                this.handleAction(((Float)this.attackDelay.getValue()).floatValue(), this.attackTimer, () -> mc.method_1536());
            }
            if (this.isUse.get().booleanValue()) {
                this.handleAction(((Float)this.useDelay.getValue()).floatValue(), this.useTimer, () -> mc.method_1583());
            }
        }));
        this.addEvents(tickEvent);
    }

    private void handleAction(float delay, TimerUtil timer, Runnable run) {
        if (timer.finished(delay * 50.0f)) {
            run.run();
            timer.reset();
        }
    }

    @Generated
    public static TapeMouseModule getInstance() {
        return instance;
    }
}

