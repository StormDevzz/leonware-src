/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.api.module.setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.Setting;

public class MultiBooleanSetting
extends Setting<List<BooleanSetting>> {
    public MultiBooleanSetting(String name) {
        super(name);
        this.value = new ArrayList();
    }

    public MultiBooleanSetting value(List<BooleanSetting> value) {
        this.setValue(value);
        return this;
    }

    public MultiBooleanSetting value(BooleanSetting ... value) {
        this.setValue((List<BooleanSetting>)new ArrayList<BooleanSetting>(Arrays.asList(value)));
        return this;
    }

    @Override
    public void setValue(List<BooleanSetting> value) {
        if (this.sameValue(value)) {
            return;
        }
        super.setValue(value);
        this.runAction();
    }

    public MultiBooleanSetting onAction(Runnable action) {
        return (MultiBooleanSetting)super.onAction(action);
    }

    public MultiBooleanSetting addValues(BooleanSetting ... value) {
        ((List)this.value).addAll(Arrays.asList(value));
        return this;
    }

    public boolean isEnabled(String name) {
        return this.getBooleanSettingByName(name).map(Setting::getValue).orElse(false);
    }

    public boolean toggle(String name) {
        return this.getBooleanSettingByName(name).map(booleanSetting -> {
            booleanSetting.value((Boolean)booleanSetting.getValue() == false);
            return true;
        }).orElse(false);
    }

    public List<String> getAllNames() {
        return ((List)this.value).stream().map(Setting::getName).collect(Collectors.toList());
    }

    public List<String> getList() {
        return ((List)this.value).stream().filter(Setting::getValue).map(Setting::getName).collect(Collectors.toList());
    }

    private Optional<BooleanSetting> getBooleanSettingByName(String name) {
        return ((List)this.value).stream().filter(setting -> setting.getName().equalsIgnoreCase(name)).findFirst();
    }

    public MultiBooleanSetting setVisible(Supplier<Boolean> condition) {
        return (MultiBooleanSetting)super.setVisible(condition);
    }
}

