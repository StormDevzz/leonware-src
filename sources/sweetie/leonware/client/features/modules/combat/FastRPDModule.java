package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_1268;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/FastRPDModule.class */
@ModuleRegister(name = "Fast RPD", category = Category.COMBAT)
public class FastRPDModule extends Module {
    private static final FastRPDModule instance = new FastRPDModule();
    private final BooleanSetting antiKick = new BooleanSetting("Anti Kick").value((Boolean) false);
    private final SliderSetting clickDelay = new SliderSetting("Задержка (мс)").value(Float.valueOf(1.0f)).range(1.0f, 100.0f).step(1.0f).setVisible(() -> {
        return this.antiKick.getValue();
    });
    private long lastClickTime = 0;

    @Generated
    public static FastRPDModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v6, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public FastRPDModule() {
        addSettings(this.antiKick, this.clickDelay);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1761 == null) {
                return;
            }
            long currentTime = System.currentTimeMillis();
            long delay = this.antiKick.getValue().booleanValue() ? this.clickDelay.getValue().longValue() : 1L;
            if (currentTime - this.lastClickTime >= delay) {
                mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
                this.lastClickTime = currentTime;
            }
        }));
        addEvents(updateEvent);
    }
}
