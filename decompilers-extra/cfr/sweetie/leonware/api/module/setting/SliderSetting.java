/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.module.setting;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Supplier;
import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;

public class SliderSetting
extends Setting<Float> {
    private float min = Float.MIN_VALUE;
    private float max = Float.MAX_VALUE;
    private float step = 1.0f;

    public SliderSetting(String name) {
        super(name);
    }

    public SliderSetting value(Float value) {
        this.setValue(value);
        return this;
    }

    @Override
    public void setValue(Float value) {
        float clampedValue = Math.max(this.min, Math.min(this.max, value.floatValue()));
        int scale = Math.max(0, (int)Math.ceil(Math.log10(1.0 / (double)this.step)));
        BigDecimal bd = new BigDecimal(Float.toString(clampedValue));
        float steppedValue = (bd = bd.divide(new BigDecimal(Float.toString(this.step)), 0, RoundingMode.HALF_UP).multiply(new BigDecimal(Float.toString(this.step))).setScale(scale, RoundingMode.HALF_UP)).floatValue();
        if (this.sameValue(Float.valueOf(steppedValue))) {
            return;
        }
        super.setValue(Float.valueOf(steppedValue));
        this.runAction();
    }

    public SliderSetting setVisible(Supplier<Boolean> condition) {
        return (SliderSetting)super.setVisible(condition);
    }

    public SliderSetting onAction(Runnable action) {
        return (SliderSetting)super.onAction(action);
    }

    public SliderSetting range(float min, float max) {
        this.min = min;
        this.max = max;
        return this;
    }

    public SliderSetting step(float step) {
        this.step = step;
        return this;
    }

    @Generated
    public float getMin() {
        return this.min;
    }

    @Generated
    public float getMax() {
        return this.max;
    }

    @Generated
    public float getStep() {
        return this.step;
    }
}

