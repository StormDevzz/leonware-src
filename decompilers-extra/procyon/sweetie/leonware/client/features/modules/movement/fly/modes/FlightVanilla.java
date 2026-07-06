// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement.fly.modes;

import sweetie.leonware.api.utils.player.MoveUtil;
import java.util.Iterator;
import sweetie.leonware.api.module.setting.Setting;
import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.client.features.modules.movement.fly.FlightMode;

public class FlightVanilla extends FlightMode
{
    private final SliderSetting speedH;
    private final SliderSetting speedV;
    private final BooleanSetting onlyWhenAllowed;
    
    @Override
    public String getName() {
        return "Vanilla";
    }
    
    public FlightVanilla(final Supplier<Boolean> condition) {
        this.speedH = new SliderSetting("Speed H").value(1.0f).range(0.1f, 5.0f).step(0.1f);
        this.speedV = new SliderSetting("Speed V").value(1.0f).range(0.1f, 5.0f).step(0.1f);
        this.onlyWhenAllowed = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u043a\u043e\u0433\u0434\u0430 \u043c\u043e\u0436\u0435\u043c").value(false);
        this.addSettings(this.speedH, this.speedV, this.onlyWhenAllowed);
        for (final Setting<?> setting : this.getSettings()) {
            setting.setVisible(condition);
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.onlyWhenAllowed.getValue() && (!FlightVanilla.mc.field_1724.method_31549().field_7478 || !FlightVanilla.mc.field_1724.method_31549().field_7479)) {
            return;
        }
        MoveUtil.setSpeed(this.speedH.getValue());
        final float y = this.speedV.getValue();
        if (FlightVanilla.mc.field_1690.field_1903.method_1434()) {
            FlightVanilla.mc.field_1724.method_18798().field_1351 = y;
        }
        else if (FlightVanilla.mc.field_1690.field_1832.method_1434()) {
            FlightVanilla.mc.field_1724.method_18798().field_1351 = -y;
        }
        else {
            FlightVanilla.mc.field_1724.method_18798().field_1351 = 0.0;
        }
    }
    
    public boolean isFlying() {
        return !this.onlyWhenAllowed.getValue() || (FlightVanilla.mc.field_1724 != null && FlightVanilla.mc.field_1724.method_31549().field_7479);
    }
}
