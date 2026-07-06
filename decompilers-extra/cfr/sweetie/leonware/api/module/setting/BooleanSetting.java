/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.api.module.setting;

import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.Setting;

public class BooleanSetting
extends Setting<Boolean> {
    public BooleanSetting(String name) {
        super(name);
    }

    public BooleanSetting value(Boolean value) {
        this.setValue(value);
        return this;
    }

    @Override
    public void setValue(Boolean value) {
        if (this.sameValue(value)) {
            return;
        }
        super.setValue(value);
        this.runAction();
    }

    public BooleanSetting setVisible(Supplier<Boolean> condition) {
        return (BooleanSetting)super.setVisible(condition);
    }

    public BooleanSetting onAction(Runnable action) {
        return (BooleanSetting)super.onAction(action);
    }

    public void toggle() {
        this.setValue((Boolean)this.getValue() == false);
    }
}

