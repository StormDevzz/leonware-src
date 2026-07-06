/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.module.setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;

public class ModeSetting
extends Setting<String> {
    private final List<String> modes = new ArrayList<String>();

    public ModeSetting(String name) {
        super(name);
    }

    public ModeSetting values(String ... values) {
        this.modes.addAll(Arrays.asList(values));
        if (this.value == null && !this.modes.isEmpty()) {
            this.value = this.modes.getFirst();
        }
        return this;
    }

    public ModeSetting values(Enum<?> ... enums) {
        this.modes.addAll(Arrays.stream(enums).map(e -> {
            if (e instanceof NamedChoice) {
                return ((NamedChoice)((Object)e)).getName();
            }
            return e.name();
        }).toList());
        if (this.value == null && !this.modes.isEmpty()) {
            this.value = this.modes.getFirst();
        }
        return this;
    }

    public ModeSetting value(Enum<?> e) {
        return this.value(e != null ? (e instanceof NamedChoice ? ((NamedChoice)((Object)e)).getName() : e.name()) : null);
    }

    public boolean is(Enum<?> e) {
        if (e == null) {
            return false;
        }
        String enumStr = e instanceof NamedChoice ? ((NamedChoice)((Object)e)).getName() : e.name();
        return ((String)this.value).equals(enumStr);
    }

    public ModeSetting value(String value) {
        this.setValue(value);
        return this;
    }

    @Override
    public void setValue(String value) {
        if (this.sameValue(value)) {
            return;
        }
        super.setValue(value);
        this.runAction();
    }

    public ModeSetting setVisible(Supplier<Boolean> condition) {
        return (ModeSetting)super.setVisible(condition);
    }

    public ModeSetting onAction(Runnable action) {
        return (ModeSetting)super.onAction(action);
    }

    public boolean is(String value) {
        return ((String)this.value).equals(value);
    }

    @Generated
    public List<String> getModes() {
        return this.modes;
    }

    public static interface NamedChoice {
        public String getName();
    }
}

