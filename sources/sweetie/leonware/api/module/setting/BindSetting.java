package sweetie.leonware.api.module.setting;

import java.util.function.Supplier;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/module/setting/BindSetting.class */
public class BindSetting extends Setting<Integer> {
    @Override // sweetie.leonware.api.module.setting.Setting
    /* JADX INFO: renamed from: setVisible, reason: avoid collision after fix types in other method */
    public /* bridge */ /* synthetic */ Setting<Integer> setVisible2(Supplier supplier) {
        return setVisible((Supplier<Boolean>) supplier);
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [T, java.lang.Integer] */
    public BindSetting(String name) {
        super(name);
        this.value = -999;
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    public BindSetting value(Integer value) {
        setValue(value);
        return this;
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    public void setValue(Integer value) {
        if (sameValue(value)) {
            return;
        }
        super.setValue(value);
        runAction();
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    public Setting<Integer> setVisible(Supplier<Boolean> condition) {
        return (BindSetting) super.setVisible(condition);
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    /* JADX INFO: renamed from: onAction */
    public Setting<Integer> onAction2(Runnable action) {
        return (BindSetting) super.onAction2(action);
    }
}
