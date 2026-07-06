package sweetie.leonware.api.module.setting;

import java.awt.Color;
import java.util.function.Supplier;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/module/setting/ColorSetting.class */
public class ColorSetting extends Setting<Color> {
    private boolean rgbMode;
    private boolean uiMode;

    @Override // sweetie.leonware.api.module.setting.Setting
    /* JADX INFO: renamed from: setVisible, reason: avoid collision after fix types in other method */
    public /* bridge */ /* synthetic */ Setting<Color> setVisible2(Supplier supplier) {
        return setVisible((Supplier<Boolean>) supplier);
    }

    public ColorSetting(String name) {
        super(name);
        this.rgbMode = false;
        this.uiMode = false;
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    public ColorSetting value(Color value) {
        setValue(value);
        return this;
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    public void setValue(Color value) {
        if (sameValue(value)) {
            return;
        }
        super.setValue(value);
        runAction();
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

    @Override // sweetie.leonware.api.module.setting.Setting
    public Setting<Color> setVisible(Supplier<Boolean> condition) {
        return (ColorSetting) super.setVisible(condition);
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    /* JADX INFO: renamed from: onAction */
    public Setting<Color> onAction2(Runnable action) {
        return (ColorSetting) super.onAction2(action);
    }
}
