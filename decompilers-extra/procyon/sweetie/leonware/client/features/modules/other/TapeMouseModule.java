// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.module.setting.SliderSetting;
import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Tape Mouse", category = Category.OTHER)
public class TapeMouseModule extends Module
{
    private static final TapeMouseModule instance;
    private final MultiBooleanSetting actions;
    private final Supplier<Boolean> isAttack;
    private final Supplier<Boolean> isUse;
    private final SliderSetting attackDelay;
    private final SliderSetting useDelay;
    private final TimerUtil attackTimer;
    private final TimerUtil useTimer;
    
    public TapeMouseModule() {
        this.actions = new MultiBooleanSetting("Actions").value(new BooleanSetting("Attack").value(true), new BooleanSetting("Use").value(false));
        this.isAttack = (() -> this.actions.isEnabled("Attack"));
        this.isUse = (() -> this.actions.isEnabled("Use"));
        this.attackDelay = new SliderSetting("Attack delay").value(10.0f).range(1.0f, 20.0f).step(1.0f).setVisible(this.isAttack);
        this.useDelay = new SliderSetting("Use delay").value(10.0f).range(1.0f, 20.0f).step(1.0f).setVisible(this.isUse);
        this.attackTimer = new TimerUtil();
        this.useTimer = new TimerUtil();
        this.addSettings(this.actions, this.attackDelay, this.useDelay);
    }
    
    @Override
    public void onEvent() {
        final EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            if (this.isAttack.get()) {
                this.handleAction(this.attackDelay.getValue(), this.attackTimer, () -> TapeMouseModule.mc.method_1536());
            }
            if (this.isUse.get()) {
                this.handleAction(this.useDelay.getValue(), this.useTimer, () -> TapeMouseModule.mc.method_1583());
            }
            return;
        }));
        this.addEvents(tickEvent);
    }
    
    private void handleAction(final float delay, final TimerUtil timer, final Runnable run) {
        if (timer.finished(delay * 50.0f)) {
            run.run();
            timer.reset();
        }
    }
    
    @Generated
    public static TapeMouseModule getInstance() {
        return TapeMouseModule.instance;
    }
    
    static {
        instance = new TapeMouseModule();
    }
}
