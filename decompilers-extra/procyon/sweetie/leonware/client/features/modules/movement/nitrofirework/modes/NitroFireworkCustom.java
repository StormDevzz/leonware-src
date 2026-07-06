// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement.nitrofirework.modes;

import sweetie.leonware.api.system.backend.Pair;
import sweetie.leonware.api.module.setting.Setting;
import java.util.ArrayList;
import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.SliderSetting;
import java.util.List;
import sweetie.leonware.client.features.modules.movement.nitrofirework.NitroFireworkMode;

public class NitroFireworkCustom extends NitroFireworkMode
{
    private final float[] angles;
    private final List<SliderSetting> speedSettings;
    
    @Override
    public String getName() {
        return "Custom";
    }
    
    public NitroFireworkCustom(final Supplier<Boolean> condition) {
        this.angles = new float[] { 0.0f, 5.0f, 15.0f, 20.0f, 25.0f, 30.0f, 35.0f, 40.0f, 45.0f };
        this.speedSettings = new ArrayList<SliderSetting>();
        final float[] angles = this.angles;
        for (int length = angles.length, i = 0; i < length; ++i) {
            final float angle = angles[i];
            final SliderSetting setting = new SliderSetting((int)angle + "° speed").value(1.5f).range(1.5f, 3.0f).step(0.1f).setVisible(condition);
            this.addSettings(setting);
            this.speedSettings.add(setting);
        }
    }
    
    @Override
    public Pair<Float, Float> velocityValues() {
        int closestIndex = 0;
        float minDiff = Float.MAX_VALUE;
        final float playerYaw = (NitroFireworkCustom.mc.field_1724.method_36454() % 360.0f + 360.0f) % 360.0f;
        for (int i = 0; i < this.angles.length; ++i) {
            final float angleVal = (this.angles[i] + 360.0f) % 360.0f;
            final float diff = Math.abs(((playerYaw - angleVal + 180.0f) % 360.0f + 360.0f) % 360.0f - 180.0f);
            if (diff < minDiff) {
                minDiff = diff;
                closestIndex = i;
            }
        }
        final float speed = this.speedSettings.get(closestIndex).getValue();
        return new Pair<Float, Float>(speed, 1.5f);
    }
}
