package sweetie.leonware.client.features.modules.movement.nitrofirework.modes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.backend.Pair;
import sweetie.leonware.client.features.modules.movement.nitrofirework.NitroFireworkMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/nitrofirework/modes/NitroFireworkCustom.class */
public class NitroFireworkCustom extends NitroFireworkMode {
    private final float[] angles = {0.0f, 5.0f, 15.0f, 20.0f, 25.0f, 30.0f, 35.0f, 40.0f, 45.0f};
    private final List<SliderSetting> speedSettings = new ArrayList();

    @Override // sweetie.leonware.api.system.backend.Choice, sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
    public String getName() {
        return "Custom";
    }

    public NitroFireworkCustom(Supplier<Boolean> supplier) {
        for (float f : this.angles) {
            Setting<Float> visible = new SliderSetting(((int) f) + "° speed").value(Float.valueOf(1.5f)).range(1.5f, 3.0f).step(0.1f).setVisible(supplier);
            addSettings(visible);
            this.speedSettings.add((SliderSetting) visible);
        }
    }

    @Override // sweetie.leonware.client.features.modules.movement.nitrofirework.NitroFireworkMode
    public Pair<Float, Float> velocityValues() {
        int closestIndex = 0;
        float minDiff = Float.MAX_VALUE;
        float playerYaw = ((mc.field_1724.method_36454() % 360.0f) + 360.0f) % 360.0f;
        for (int i = 0; i < this.angles.length; i++) {
            float angleVal = (this.angles[i] + 360.0f) % 360.0f;
            float diff = Math.abs((((((playerYaw - angleVal) + 180.0f) % 360.0f) + 360.0f) % 360.0f) - 180.0f);
            if (diff < minDiff) {
                minDiff = diff;
                closestIndex = i;
            }
        }
        float speed = this.speedSettings.get(closestIndex).getValue().floatValue();
        return new Pair<>(Float.valueOf(speed), Float.valueOf(1.5f));
    }
}
