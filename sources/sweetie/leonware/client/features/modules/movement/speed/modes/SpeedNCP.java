package sweetie.leonware.client.features.modules.movement.speed.modes;

import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.client.features.modules.movement.speed.SpeedMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/speed/modes/SpeedNCP.class */
public class SpeedNCP extends SpeedMode {
    private final SliderSetting speedStrafe = new SliderSetting("Скорость стрейфа").value(Float.valueOf(0.576f)).range(0.3f, 0.9f).step(0.001f);

    public SpeedNCP(Supplier<Boolean> condition) {
        this.speedStrafe.setVisible(condition);
        addSettings(this.speedStrafe);
    }

    @Override // sweetie.leonware.api.system.backend.Choice, sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
    public String getName() {
        return "NoCheatPlus";
    }

    @Override // sweetie.leonware.client.features.modules.movement.speed.SpeedMode
    public void onTravel() {
        if (MoveUtil.isMoving()) {
            MoveUtil.setSpeed(this.speedStrafe.getValue().floatValue());
        }
    }
}
