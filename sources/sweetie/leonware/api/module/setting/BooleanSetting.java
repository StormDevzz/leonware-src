package sweetie.leonware.api.module.setting;

import java.util.function.Supplier;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/module/setting/BooleanSetting.class */
public class BooleanSetting extends Setting<Boolean> {
    @Override // sweetie.leonware.api.module.setting.Setting
    /* JADX INFO: renamed from: setVisible, reason: avoid collision after fix types in other method */
    public /* bridge */ /* synthetic */ Setting<Boolean> setVisible2(Supplier supplier) {
        return setVisible((Supplier<Boolean>) supplier);
    }

    public BooleanSetting(String name) {
        super(name);
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    public BooleanSetting value(Boolean value) {
        setValue(value);
        return this;
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    public void setValue(Boolean value) {
        if (sameValue(value)) {
            return;
        }
        super.setValue(value);
        runAction();
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    public Setting<Boolean> setVisible(Supplier<Boolean> condition) {
        return (BooleanSetting) super.setVisible(condition);
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    /* JADX INFO: renamed from: onAction, reason: merged with bridge method [inline-methods] */
    public Setting<Boolean> onAction2(Runnable action) {
        return (BooleanSetting) super.onAction2(action);
    }

    public void toggle() {
        setValue(Boolean.valueOf(!getValue().booleanValue()));
    }
}
