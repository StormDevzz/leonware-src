/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.client.features.modules.movement.speed.modes;

import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.client.features.modules.movement.speed.SpeedMode;

public class SpeedVanilla
extends SpeedMode {
    private final SliderSetting speed = new SliderSetting("Speed").value(Float.valueOf(1.0f)).range(0.1f, 5.0f).step(0.1f);

    @Override
    public String getName() {
        return "Vanilla";
    }

    public SpeedVanilla(Supplier<Boolean> condition) {
        this.speed.setVisible((Supplier)condition);
        this.addSettings(this.speed);
    }

    @Override
    public void onTravel() {
        MoveUtil.setSpeed(((Float)this.speed.getValue()).floatValue());
    }
}

