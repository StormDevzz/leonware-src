package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1294;
import net.minecraft.class_243;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/LevitationControlModule.class */
@ModuleRegister(name = "Levitation Control", category = Category.PLAYER)
public class LevitationControlModule extends Module {
    private static final LevitationControlModule instance = new LevitationControlModule();
    private final SliderSetting up = new SliderSetting("Up Speed").value(Float.valueOf(0.5f)).range(0.0f, 2.0f).step(0.1f);
    private final SliderSetting down = new SliderSetting("Down Speed").value(Float.valueOf(0.5f)).range(0.0f, 2.0f).step(0.1f);
    private final BooleanSetting stuck = new BooleanSetting("Freeze").value((Boolean) true);

    @Generated
    public static LevitationControlModule getInstance() {
        return instance;
    }

    public LevitationControlModule() {
        addSettings(this.up, this.down, this.stuck);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            double verticalVelocity;
            if (mc.field_1724 == null || mc.field_1687 == null || !mc.field_1724.method_6059(class_1294.field_5902)) {
                return;
            }
            if (mc.field_1690.field_1903.method_1434()) {
                verticalVelocity = this.up.getValue().floatValue();
            } else if (mc.field_1690.field_1832.method_1434()) {
                verticalVelocity = -this.down.getValue().floatValue();
            } else if (this.stuck.getValue().booleanValue()) {
                verticalVelocity = 0.0d;
            } else {
                verticalVelocity = mc.field_1724.method_18798().field_1351;
            }
            if (Math.abs(mc.field_1724.method_18798().field_1351 - verticalVelocity) > 0.001d) {
                class_243 currentVel = mc.field_1724.method_18798();
                mc.field_1724.method_18800(currentVel.field_1352, verticalVelocity, currentVel.field_1350);
            }
        }));
        addEvents(updateEvent);
    }
}
