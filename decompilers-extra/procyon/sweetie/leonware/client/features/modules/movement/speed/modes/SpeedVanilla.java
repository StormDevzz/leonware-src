// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement.speed.modes;

import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.api.module.setting.Setting;
import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.client.features.modules.movement.speed.SpeedMode;

public class SpeedVanilla extends SpeedMode
{
    private final SliderSetting speed;
    
    @Override
    public String getName() {
        return "Vanilla";
    }
    
    public SpeedVanilla(final Supplier<Boolean> condition) {
        (this.speed = new SliderSetting("Speed").value(1.0f).range(0.1f, 5.0f).step(0.1f)).setVisible(condition);
        this.addSettings(this.speed);
    }
    
    @Override
    public void onTravel() {
        MoveUtil.setSpeed(this.speed.getValue());
    }
}
