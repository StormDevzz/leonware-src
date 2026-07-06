/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.api.module.setting;

import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.Setting;

public class BindSetting
extends Setting<Integer> {
    public BindSetting(String name) {
        super(name);
        this.value = -999;
    }

    public BindSetting value(Integer value) {
        this.setValue(value);
        return this;
    }

    @Override
    public void setValue(Integer value) {
        if (this.sameValue(value)) {
            return;
        }
        super.setValue(value);
        this.runAction();
    }

    public BindSetting setVisible(Supplier<Boolean> condition) {
        return (BindSetting)super.setVisible(condition);
    }

    public BindSetting onAction(Runnable action) {
        return (BindSetting)super.onAction(action);
    }
}

