/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.client.features.modules.movement.spider.modes;

import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.client.features.modules.movement.spider.SpiderMode;

public class SpiderMatrix
extends SpiderMode {
    private final SliderSetting delay = new SliderSetting("Delay").value(Float.valueOf(2.0f)).range(1.0f, 15.0f).step(1.0f);

    @Override
    public String getName() {
        return "Matrix";
    }

    public SpiderMatrix(Supplier<Boolean> condition) {
        this.delay.setVisible((Supplier)condition);
        this.addSettings(this.delay);
    }

    @Override
    public void onUpdate() {
        if (!this.hozColl()) {
            return;
        }
        SpiderMatrix.mc.field_1724.method_24830(SpiderMatrix.mc.field_1724.field_6012 % ((Float)this.delay.getValue()).intValue() == 0);
        SpiderMatrix.mc.field_1724.field_6036 -= 2.0E-232;
        if (SpiderMatrix.mc.field_1724.method_24828()) {
            SpiderMatrix.mc.field_1724.method_18800(SpiderMatrix.mc.field_1724.method_18798().method_10216(), 0.42, SpiderMatrix.mc.field_1724.method_18798().method_10215());
        }
    }
}

