// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.module.setting;

import java.util.function.Supplier;

public class RunSetting extends Setting<Runnable>
{
    public RunSetting(final String name) {
        super(name);
    }
    
    @Override
    public RunSetting value(final Runnable value) {
        this.value = (T)value;
        return this;
    }
    
    @Override
    public RunSetting setVisible(final Supplier<Boolean> condition) {
        return (RunSetting)super.setVisible(condition);
    }
}
