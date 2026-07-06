package sweetie.leonware.api.module.setting;

import java.util.function.Supplier;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/module/setting/RunSetting.class */
public class RunSetting extends Setting<Runnable> {
    @Override // sweetie.leonware.api.module.setting.Setting
    /* JADX INFO: renamed from: setVisible, reason: avoid collision after fix types in other method */
    public /* bridge */ /* synthetic */ Setting<Runnable> setVisible2(Supplier supplier) {
        return setVisible((Supplier<Boolean>) supplier);
    }

    public RunSetting(String name) {
        super(name);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // sweetie.leonware.api.module.setting.Setting
    public RunSetting value(Runnable value) {
        this.value = value;
        return this;
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    public Setting<Runnable> setVisible(Supplier<Boolean> condition) {
        return (RunSetting) super.setVisible(condition);
    }
}
