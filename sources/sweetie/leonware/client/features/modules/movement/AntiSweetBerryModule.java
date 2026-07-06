package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_2338;
import net.minecraft.class_3830;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.player.MoveUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/AntiSweetBerryModule.class */
@ModuleRegister(name = "Anti Sweet Berry", category = Category.MOVEMENT)
public class AntiSweetBerryModule extends Module {
    private static final AntiSweetBerryModule instance = new AntiSweetBerryModule();
    private final SliderSetting boostFactor = new SliderSetting("Скорость").value(Float.valueOf(0.2f)).range(0.0f, 1.0f).step(0.01f);

    @Generated
    public static AntiSweetBerryModule getInstance() {
        return instance;
    }

    public AntiSweetBerryModule() {
        addSettings(this.boostFactor);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null || !isInSweetBerryBush()) {
                return;
            }
            double boost = this.boostFactor.getValue().floatValue();
            double[] dir = MoveUtil.forward(boost);
            mc.field_1724.method_18800(dir[0], mc.field_1724.method_18798().field_1351, dir[1]);
        }));
        addEvents(updateEvent);
    }

    private boolean isInSweetBerryBush() {
        if (mc.field_1687 == null || mc.field_1724 == null) {
            return false;
        }
        class_2338 pos = mc.field_1724.method_24515();
        return (mc.field_1687.method_8320(pos).method_26204() instanceof class_3830) || (mc.field_1687.method_8320(pos.method_10074()).method_26204() instanceof class_3830);
    }
}
