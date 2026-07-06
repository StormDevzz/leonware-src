/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.api.module.setting;

import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.Setting;

public class RunSetting
extends Setting<Runnable> {
    public RunSetting(String name) {
        super(name);
    }

    public RunSetting value(Runnable value) {
        this.value = value;
        return this;
    }

    public RunSetting setVisible(Supplier<Boolean> condition) {
        return (RunSetting)super.setVisible(condition);
    }
}

