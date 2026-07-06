package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.world.ClickSlotEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.backend.KeyStorage;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/other/MouseTweaksModule.class */
@ModuleRegister(name = "Mouse Tweaks", category = Category.OTHER)
public class MouseTweaksModule extends Module {
    private static final MouseTweaksModule instance = new MouseTweaksModule();
    public final SliderSetting delay = new SliderSetting("Delay").value(Float.valueOf(0.0f)).range(0.0f, 200.0f).step(5.0f);
    private boolean stop = false;

    @Generated
    public static MouseTweaksModule getInstance() {
        return instance;
    }

    public MouseTweaksModule() {
        addSettings(this.delay);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener clickSlotEvent = ClickSlotEvent.getInstance().subscribe(new Listener(event -> {
            if (KeyStorage.isPressed(340) || KeyStorage.isPressed(344)) {
                if ((KeyStorage.isPressed(341) || KeyStorage.isPressed(345)) && event.slotActionType() == class_1713.field_7795 && !this.stop) {
                    class_1792 copy = ((class_1735) mc.field_1724.field_7512.field_7761.get(event.slot())).method_7677().method_7909();
                    this.stop = true;
                    for (int i2 = 0; i2 < mc.field_1724.field_7512.field_7761.size(); i2++) {
                        if (((class_1735) mc.field_1724.field_7512.field_7761.get(i2)).method_7677().method_7909() == copy) {
                            mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, i2, 1, class_1713.field_7795, mc.field_1724);
                        }
                    }
                    this.stop = false;
                }
            }
        }));
        addEvents(clickSlotEvent);
    }
}
