package sweetie.leonware.api.module.setting;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Supplier;
import lombok.Generated;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/module/setting/SliderSetting.class */
public class SliderSetting extends Setting<Float> {
    private float min;
    private float max;
    private float step;

    @Override // sweetie.leonware.api.module.setting.Setting
    /* JADX INFO: renamed from: setVisible, reason: avoid collision after fix types in other method */
    public /* bridge */ /* synthetic */ Setting<Float> setVisible2(Supplier supplier) {
        return setVisible((Supplier<Boolean>) supplier);
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

    public SliderSetting(String name) {
        super(name);
        this.min = Float.MIN_VALUE;
        this.max = Float.MAX_VALUE;
        this.step = 1.0f;
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    public SliderSetting value(Float value) {
        setValue(value);
        return this;
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    public void setValue(Float value) {
        float clampedValue = Math.max(this.min, Math.min(this.max, value.floatValue()));
        int scale = Math.max(0, (int) Math.ceil(Math.log10(1.0d / ((double) this.step))));
        BigDecimal bd = new BigDecimal(Float.toString(clampedValue));
        float steppedValue = bd.divide(new BigDecimal(Float.toString(this.step)), 0, RoundingMode.HALF_UP).multiply(new BigDecimal(Float.toString(this.step))).setScale(scale, RoundingMode.HALF_UP).floatValue();
        if (sameValue(Float.valueOf(steppedValue))) {
            return;
        }
        super.setValue(Float.valueOf(steppedValue));
        runAction();
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    public Setting<Float> setVisible(Supplier<Boolean> condition) {
        return (SliderSetting) super.setVisible(condition);
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    /* JADX INFO: renamed from: onAction */
    public Setting<Float> onAction2(Runnable action) {
        return (SliderSetting) super.onAction2(action);
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
}
