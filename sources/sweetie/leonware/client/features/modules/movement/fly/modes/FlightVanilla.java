package sweetie.leonware.client.features.modules.movement.fly.modes;

import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.client.features.modules.movement.fly.FlightMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/fly/modes/FlightVanilla.class */
public class FlightVanilla extends FlightMode {
    private final SliderSetting speedH = new SliderSetting("Speed H").value(Float.valueOf(1.0f)).range(0.1f, 5.0f).step(0.1f);
    private final SliderSetting speedV = new SliderSetting("Speed V").value(Float.valueOf(1.0f)).range(0.1f, 5.0f).step(0.1f);
    private final BooleanSetting onlyWhenAllowed = new BooleanSetting("Только когда можем").value((Boolean) false);

    @Override // sweetie.leonware.api.system.backend.Choice, sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
    public String getName() {
        return "Vanilla";
    }

    public FlightVanilla(Supplier<Boolean> condition) {
        addSettings(this.speedH, this.speedV, this.onlyWhenAllowed);
        for (Setting<?> setting : getSettings()) {
            setting.setVisible(condition);
        }
    }

    @Override // sweetie.leonware.client.features.modules.movement.fly.FlightMode
    public void onUpdate() {
        if (this.onlyWhenAllowed.getValue().booleanValue() && (!mc.field_1724.method_31549().field_7478 || !mc.field_1724.method_31549().field_7479)) {
            return;
        }
        MoveUtil.setSpeed(this.speedH.getValue().floatValue());
        float y = this.speedV.getValue().floatValue();
        if (mc.field_1690.field_1903.method_1434()) {
            mc.field_1724.method_18798().field_1351 = y;
        } else if (mc.field_1690.field_1832.method_1434()) {
            mc.field_1724.method_18798().field_1351 = -y;
        } else {
            mc.field_1724.method_18798().field_1351 = 0.0d;
        }
    }

    public boolean isFlying() {
        if (this.onlyWhenAllowed.getValue().booleanValue()) {
            return mc.field_1724 != null && mc.field_1724.method_31549().field_7479;
        }
        return true;
    }
}
