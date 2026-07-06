/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.api.module.setting;

import java.awt.Color;
import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.Setting;

public class ColorSetting
extends Setting<Color> {
    private boolean rgbMode = false;
    private boolean uiMode = false;

    public ColorSetting(String name) {
        super(name);
    }

    public ColorSetting value(Color value) {
        this.setValue(value);
        return this;
    }

    @Override
    public void setValue(Color value) {
        if (this.sameValue(value)) {
            return;
        }
        super.setValue(value);
        this.runAction();
    }

    public boolean isRgbMode() {
        return this.rgbMode;
    }

    public boolean isUiMode() {
        return this.uiMode;
    }

    public void setRgbMode(boolean value) {
        this.rgbMode = value;
    }

    public void setUiMode(boolean value) {
        this.uiMode = value;
    }

    public ColorSetting setVisible(Supplier<Boolean> condition) {
        return (ColorSetting)super.setVisible(condition);
    }

    public ColorSetting onAction(Runnable action) {
        return (ColorSetting)super.onAction(action);
    }
}

