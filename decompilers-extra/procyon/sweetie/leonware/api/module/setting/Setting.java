// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.module.setting;

import lombok.Generated;
import java.util.function.Supplier;

public abstract class Setting<T>
{
    protected String name;
    protected T value;
    protected Supplier<Boolean> visibilityCondition;
    protected Runnable action;
    
    public Setting(final String name) {
        this.visibilityCondition = (() -> true);
        this.name = name;
    }
    
    public Setting<T> setVisible(final Supplier<Boolean> condition) {
        this.visibilityCondition = condition;
        return this;
    }
    
    public void runAction() {
        if (this.action != null) {
            this.action.run();
        }
    }
    
    protected boolean sameValue(final T newValue) {
        return this.value != null && newValue != null && this.value == newValue;
    }
    
    public Setting<T> onAction(final Runnable action) {
        this.action = action;
        return this;
    }
    
    public boolean isVisible() {
        return this.visibilityCondition.get();
    }
    
    public abstract Setting<T> value(final T p0);
    
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
    public void setName(final String name) {
        this.name = name;
    }
    
    @Generated
    public void setValue(final T value) {
        this.value = value;
    }
    
    @Generated
    public void setVisibilityCondition(final Supplier<Boolean> visibilityCondition) {
        this.visibilityCondition = visibilityCondition;
    }
    
    @Generated
    public void setAction(final Runnable action) {
        this.action = action;
    }
}
