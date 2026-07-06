// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement.speed.modes;

import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.api.module.setting.Setting;
import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.client.features.modules.movement.speed.SpeedMode;

public class SpeedNCP extends SpeedMode
{
    private final SliderSetting speedStrafe;
    
    public SpeedNCP(final Supplier<Boolean> condition) {
        (this.speedStrafe = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0441\u0442\u0440\u0435\u0439\u0444\u0430").value(0.576f).range(0.3f, 0.9f).step(0.001f)).setVisible(condition);
        this.addSettings(this.speedStrafe);
    }
    
    @Override
    public String getName() {
        return "NoCheatPlus";
    }
    
    @Override
    public void onTravel() {
        if (!MoveUtil.isMoving()) {
            return;
        }
        MoveUtil.setSpeed(this.speedStrafe.getValue());
    }
}
