// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_1657;
import net.minecraft.class_1268;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Fast RPD", category = Category.COMBAT)
public class FastRPDModule extends Module
{
    private static final FastRPDModule instance;
    private final BooleanSetting antiKick;
    private final SliderSetting clickDelay;
    private long lastClickTime;
    
    public FastRPDModule() {
        this.antiKick = new BooleanSetting("Anti Kick").value(false);
        this.clickDelay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430 (\u043c\u0441)").value(1.0f).range(1.0f, 100.0f).step(1.0f).setVisible(() -> this.antiKick.getValue());
        this.lastClickTime = 0L;
        this.addSettings(this.antiKick, this.clickDelay);
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (FastRPDModule.mc.field_1724 == null || FastRPDModule.mc.field_1687 == null || FastRPDModule.mc.field_1761 == null) {
                return;
            }
            else {
                final long currentTime = System.currentTimeMillis();
                final long delay = this.antiKick.getValue() ? this.clickDelay.getValue().longValue() : 1L;
                if (currentTime - this.lastClickTime >= delay) {
                    FastRPDModule.mc.field_1761.method_2919((class_1657)FastRPDModule.mc.field_1724, class_1268.field_5808);
                    this.lastClickTime = currentTime;
                }
                return;
            }
        }));
        this.addEvents(updateEvent);
    }
    
    @Generated
    public static FastRPDModule getInstance() {
        return FastRPDModule.instance;
    }
    
    static {
        instance = new FastRPDModule();
    }
}
