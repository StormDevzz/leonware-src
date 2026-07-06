// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.module.setting;

import java.util.function.Supplier;

public class BooleanSetting extends Setting<Boolean>
{
    public BooleanSetting(final String name) {
        super(name);
    }
    
    @Override
    public BooleanSetting value(final Boolean value) {
        this.setValue(value);
        return this;
    }
    
    @Override
    public void setValue(final Boolean value) {
        if (this.sameValue(value)) {
            return;
        }
        super.setValue(value);
        this.runAction();
    }
    
    @Override
    public BooleanSetting setVisible(final Supplier<Boolean> condition) {
        return (BooleanSetting)super.setVisible(condition);
    }
    
    @Override
    public BooleanSetting onAction(final Runnable action) {
        return (BooleanSetting)super.onAction(action);
    }
    
    public void toggle() {
        this.setValue(!this.getValue());
    }
}
