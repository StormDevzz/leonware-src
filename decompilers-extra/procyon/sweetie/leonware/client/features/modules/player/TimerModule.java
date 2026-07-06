// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.api.system.client.TimerManager;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Timer", category = Category.PLAYER)
public class TimerModule extends Module
{
    private static final TimerModule instance;
    private final SliderSetting multiplier;
    
    public TimerModule() {
        this.multiplier = new SliderSetting("Multiplier").value(2.0f).range(0.1f, 5.0f).step(0.1f);
        this.addSettings(this.multiplier);
    }
    
    @Override
    public void onEvent() {
        final EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> TimerManager.getInstance().addTimer(this.multiplier.getValue(), TaskPriority.NORMAL, this, 1)));
        this.addEvents(tickEvent);
    }
    
    @Generated
    public static TimerModule getInstance() {
        return TimerModule.instance;
    }
    
    static {
        instance = new TimerModule();
    }
}
