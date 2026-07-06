package sweetie.leonware.api.module.setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import lombok.Generated;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/module/setting/ModeSetting.class */
public class ModeSetting extends Setting<String> {
    private final List<String> modes;

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/module/setting/ModeSetting$NamedChoice.class */
    public interface NamedChoice {
        String getName();
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    /* JADX INFO: renamed from: setVisible, reason: avoid collision after fix types in other method */
    public /* bridge */ /* synthetic */ Setting<String> setVisible2(Supplier supplier) {
        return setVisible((Supplier<Boolean>) supplier);
    }

    @Generated
    public List<String> getModes() {
        return this.modes;
    }

    public ModeSetting(String name) {
        super(name);
        this.modes = new ArrayList();
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [T, java.lang.Object] */
    public ModeSetting values(String... values) {
        this.modes.addAll(Arrays.asList(values));
        if (this.value == 0 && !this.modes.isEmpty()) {
            this.value = this.modes.getFirst();
        }
        return this;
    }

    /* JADX WARN: Type inference failed for: r1v6, types: [T, java.lang.Object] */
    public ModeSetting values(Enum<?>... enums) {
        this.modes.addAll(Arrays.stream(enums).map(e -> {
            if (e instanceof NamedChoice) {
                return ((NamedChoice) e).getName();
            }
            return e.name();
        }).toList());
        if (this.value == 0 && !this.modes.isEmpty()) {
            this.value = this.modes.getFirst();
        }
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ModeSetting value(Enum<?> e) {
        return value(e != 0 ? e instanceof NamedChoice ? ((NamedChoice) e).getName() : e.name() : null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean is(Enum<?> e) {
        if (e == 0) {
            return false;
        }
        String enumStr = e instanceof NamedChoice ? ((NamedChoice) e).getName() : e.name();
        return ((String) this.value).equals(enumStr);
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    public ModeSetting value(String value) {
        setValue(value);
        return this;
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    public void setValue(String value) {
        if (sameValue(value)) {
            return;
        }
        super.setValue(value);
        runAction();
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    public Setting<String> setVisible(Supplier<Boolean> condition) {
        return (ModeSetting) super.setVisible(condition);
    }

    @Override // sweetie.leonware.api.module.setting.Setting
    /* JADX INFO: renamed from: onAction */
    public Setting<String> onAction2(Runnable action) {
        return (ModeSetting) super.onAction2(action);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean is(String value) {
        return ((String) this.value).equals(value);
    }
}
