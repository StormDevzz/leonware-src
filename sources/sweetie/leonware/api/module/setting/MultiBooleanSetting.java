package sweetie.leonware.api.module.setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/module/setting/MultiBooleanSetting.class */
public class MultiBooleanSetting extends Setting<List<BooleanSetting>> {
    @Override // sweetie.leonware.api.module.setting.Setting
    /* JADX INFO: renamed from: setVisible, reason: avoid collision after fix types in other method */
    public /* bridge */ /* synthetic */ Setting<List<BooleanSetting>> setVisible2(Supplier supplier) {
        return setVisible((Supplier<Boolean>) supplier);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [T, java.util.ArrayList] */
    public MultiBooleanSetting(String name) {
        super(name);
        this.value = new ArrayList();
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    public MultiBooleanSetting value(List<BooleanSetting> value) {
        setValue(value);
        return this;
    }

    public MultiBooleanSetting value(BooleanSetting... value) {
        setValue((List<BooleanSetting>) new ArrayList(Arrays.asList(value)));
        return this;
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    public void setValue(List<BooleanSetting> value) {
        if (sameValue(value)) {
            return;
        }
        super.setValue(value);
        runAction();
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    /* JADX INFO: renamed from: onAction */
    public Setting<List<BooleanSetting>> onAction2(Runnable action) {
        return (MultiBooleanSetting) super.onAction2(action);
    }

    public MultiBooleanSetting addValues(BooleanSetting... value) {
        ((List) this.value).addAll(Arrays.asList(value));
        return this;
    }

    public boolean isEnabled(String name) {
        return ((Boolean) getBooleanSettingByName(name).map((v0) -> {
            return v0.getValue();
        }).orElse(false)).booleanValue();
    }

    public boolean toggle(String name) {
        return ((Boolean) getBooleanSettingByName(name).map(booleanSetting -> {
            booleanSetting.value(Boolean.valueOf(!booleanSetting.getValue().booleanValue()));
            return true;
        }).orElse(false)).booleanValue();
    }

    public List<String> getAllNames() {
        return (List) ((List) this.value).stream().map((v0) -> {
            return v0.getName();
        }).collect(Collectors.toList());
    }

    public List<String> getList() {
        return (List) ((List) this.value).stream().filter((v0) -> {
            return v0.getValue();
        }).map((v0) -> {
            return v0.getName();
        }).collect(Collectors.toList());
    }

    private Optional<BooleanSetting> getBooleanSettingByName(String name) {
        return ((List) this.value).stream().filter(setting -> {
            return setting.getName().equalsIgnoreCase(name);
        }).findFirst();
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    public Setting<List<BooleanSetting>> setVisible(Supplier<Boolean> condition) {
        return (MultiBooleanSetting) super.setVisible(condition);
    }
}
