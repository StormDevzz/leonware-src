package sweetie.leonware.client.features.modules.movement.spider.modes;

import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.client.features.modules.movement.spider.SpiderMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/spider/modes/SpiderMatrix.class */
public class SpiderMatrix extends SpiderMode {
    private final SliderSetting delay = new SliderSetting("Delay").value(Float.valueOf(2.0f)).range(1.0f, 15.0f).step(1.0f);

    @Override // sweetie.leonware.api.system.backend.Choice, sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
    public String getName() {
        return "Matrix";
    }

    public SpiderMatrix(Supplier<Boolean> condition) {
        this.delay.setVisible(condition);
        addSettings(this.delay);
    }

    @Override // sweetie.leonware.client.features.modules.movement.spider.SpiderMode
    public void onUpdate() {
        if (hozColl()) {
            mc.field_1724.method_24830(mc.field_1724.field_6012 % this.delay.getValue().intValue() == 0);
            mc.field_1724.field_6036 -= 2.0E-232d;
            if (mc.field_1724.method_24828()) {
                mc.field_1724.method_18800(mc.field_1724.method_18798().method_10216(), 0.42d, mc.field_1724.method_18798().method_10215());
            }
        }
    }
}
