// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.module.setting;

import lombok.Generated;
import java.util.function.Supplier;
import java.math.RoundingMode;
import java.math.BigDecimal;

public class SliderSetting extends Setting<Float>
{
    private float min;
    private float max;
    private float step;
    
    public SliderSetting(final String name) {
        super(name);
        this.min = Float.MIN_VALUE;
        this.max = Float.MAX_VALUE;
        this.step = 1.0f;
    }
    
    @Override
    public SliderSetting value(final Float value) {
        this.setValue(value);
        return this;
    }
    
    @Override
    public void setValue(final Float value) {
        final float clampedValue = Math.max(this.min, Math.min(this.max, value));
        final int scale = Math.max(0, (int)Math.ceil(Math.log10(1.0 / this.step)));
        BigDecimal bd = new BigDecimal(Float.toString(clampedValue));
        bd = bd.divide(new BigDecimal(Float.toString(this.step)), 0, RoundingMode.HALF_UP).multiply(new BigDecimal(Float.toString(this.step))).setScale(scale, RoundingMode.HALF_UP);
        final float steppedValue = bd.floatValue();
        if (this.sameValue(steppedValue)) {
            return;
        }
        super.setValue(steppedValue);
        this.runAction();
    }
    
    @Override
    public SliderSetting setVisible(final Supplier<Boolean> condition) {
        return (SliderSetting)super.setVisible(condition);
    }
    
    @Override
    public SliderSetting onAction(final Runnable action) {
        return (SliderSetting)super.onAction(action);
    }
    
    public SliderSetting range(final float min, final float max) {
        this.min = min;
        this.max = max;
        return this;
    }
    
    public SliderSetting step(final float step) {
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
