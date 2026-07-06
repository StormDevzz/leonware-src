package sweetie.leonware.client.features.modules.movement.speed.modes;

import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.client.features.modules.movement.speed.SpeedMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/speed/modes/SpeedVanilla.class */
public class SpeedVanilla extends SpeedMode {
    private final SliderSetting speed = new SliderSetting("Speed").value(Float.valueOf(1.0f)).range(0.1f, 5.0f).step(0.1f);

    @Override // sweetie.leonware.api.system.backend.Choice, sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
    public String getName() {
        return "Vanilla";
    }

    public SpeedVanilla(Supplier<Boolean> condition) {
        this.speed.setVisible(condition);
        addSettings(this.speed);
    }

    @Override // sweetie.leonware.client.features.modules.movement.speed.SpeedMode
    public void onTravel() {
        MoveUtil.setSpeed(this.speed.getValue().floatValue());
    }
}
