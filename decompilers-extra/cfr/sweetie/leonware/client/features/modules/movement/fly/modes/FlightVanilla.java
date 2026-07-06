/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.client.features.modules.movement.fly.modes;

import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.client.features.modules.movement.fly.FlightMode;

public class FlightVanilla
extends FlightMode {
    private final SliderSetting speedH = new SliderSetting("Speed H").value(Float.valueOf(1.0f)).range(0.1f, 5.0f).step(0.1f);
    private final SliderSetting speedV = new SliderSetting("Speed V").value(Float.valueOf(1.0f)).range(0.1f, 5.0f).step(0.1f);
    private final BooleanSetting onlyWhenAllowed = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u043a\u043e\u0433\u0434\u0430 \u043c\u043e\u0436\u0435\u043c").value(false);

    @Override
    public String getName() {
        return "Vanilla";
    }

    public FlightVanilla(Supplier<Boolean> condition) {
        this.addSettings(this.speedH, this.speedV, this.onlyWhenAllowed);
        for (Setting<?> setting : this.getSettings()) {
            setting.setVisible(condition);
        }
    }

    @Override
    public void onUpdate() {
        if (!(!((Boolean)this.onlyWhenAllowed.getValue()).booleanValue() || FlightVanilla.mc.field_1724.method_31549().field_7478 && FlightVanilla.mc.field_1724.method_31549().field_7479)) {
            return;
        }
        MoveUtil.setSpeed(((Float)this.speedH.getValue()).floatValue());
        float y = ((Float)this.speedV.getValue()).floatValue();
        FlightVanilla.mc.field_1724.method_18798().field_1351 = FlightVanilla.mc.field_1690.field_1903.method_1434() ? (double)y : (FlightVanilla.mc.field_1690.field_1832.method_1434() ? (double)(-y) : 0.0);
    }

    public boolean isFlying() {
        if (!((Boolean)this.onlyWhenAllowed.getValue()).booleanValue()) {
            return true;
        }
        return FlightVanilla.mc.field_1724 != null && FlightVanilla.mc.field_1724.method_31549().field_7479;
    }
}

