// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.module.setting;

import java.util.function.Supplier;

public class BindSetting extends Setting<Integer>
{
    public BindSetting(final String name) {
        super(name);
        this.value = (T)Integer.valueOf(-999);
    }
    
    @Override
    public BindSetting value(final Integer value) {
        this.setValue(value);
        return this;
    }
    
    @Override
    public void setValue(final Integer value) {
        if (this.sameValue(value)) {
            return;
        }
        super.setValue(value);
        this.runAction();
    }
    
    @Override
    public BindSetting setVisible(final Supplier<Boolean> condition) {
        return (BindSetting)super.setVisible(condition);
    }
    
    @Override
    public BindSetting onAction(final Runnable action) {
        return (BindSetting)super.onAction(action);
    }
}
