package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_2510;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/FastClimbModule.class */
@ModuleRegister(name = "Fast Climb", category = Category.MOVEMENT)
public class FastClimbModule extends Module {
    private static final FastClimbModule instance = new FastClimbModule();
    private final MultiBooleanSetting mode = new MultiBooleanSetting("Режимы").value(new BooleanSetting("Ступеньки").value((Boolean) true), new BooleanSetting("Лестницы").value((Boolean) true));
    private final SliderSetting ladderSpeed = new SliderSetting("Скорость лестниц").value(Float.valueOf(0.28f)).range(0.15f, 0.6f).step(0.01f);

    @Generated
    public static FastClimbModule getInstance() {
        return instance;
    }

    public FastClimbModule() {
        addSettings(this.mode, this.ladderSpeed);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            if (this.mode.isEnabled("Ступеньки")) {
                handleStairs();
            }
            if (this.mode.isEnabled("Лестницы")) {
                handleLadder();
            }
        }));
        addEvents(updateEvent);
    }

    private void handleStairs() {
        if (mc.field_1724.field_3913.method_3128().field_1342 > 0.01f && mc.field_1724.method_24828() && (mc.field_1687.method_8320(mc.field_1724.method_24515().method_10074()).method_26204() instanceof class_2510)) {
            mc.field_1724.method_6043();
        }
    }

    private void handleLadder() {
        if (mc.field_1724.method_6101() && mc.field_1724.field_3913.method_3128().field_1342 > 0.01f) {
            mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, this.ladderSpeed.getValue().doubleValue(), mc.field_1724.method_18798().field_1350);
        }
    }
}
