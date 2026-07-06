/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.module.setting;

import java.util.function.Supplier;
import lombok.Generated;

public abstract class Setting<T> {
    protected String name;
    protected T value;
    protected Supplier<Boolean> visibilityCondition = () -> true;
    protected Runnable action;

    public Setting(String name) {
        this.name = name;
    }

    public Setting<T> setVisible(Supplier<Boolean> condition) {
        this.visibilityCondition = condition;
        return this;
    }

    public void runAction() {
        if (this.action != null) {
            this.action.run();
        }
    }

    protected boolean sameValue(T newValue) {
        if (this.value == null || newValue == null) {
            return false;
        }
        return this.value == newValue;
    }

    public Setting<T> onAction(Runnable action) {
        this.action = action;
        return this;
    }

    public boolean isVisible() {
        return this.visibilityCondition.get();
    }

    public abstract Setting<T> value(T var1);

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public T getValue() {
        return this.value;
    }

    @Generated
    public Supplier<Boolean> getVisibilityCondition() {
        return this.visibilityCondition;
    }

    @Generated
    public Runnable getAction() {
        return this.action;
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }

    @Generated
    public void setValue(T value) {
        this.value = value;
    }

    @Generated
    public void setVisibilityCondition(Supplier<Boolean> visibilityCondition) {
        this.visibilityCondition = visibilityCondition;
    }

    @Generated
    public void setAction(Runnable action) {
        this.action = action;
    }
}

