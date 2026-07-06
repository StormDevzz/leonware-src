// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.module.setting;

import lombok.Generated;
import java.util.function.Supplier;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class ModeSetting extends Setting<String>
{
    private final List<String> modes;
    
    public ModeSetting(final String name) {
        super(name);
        this.modes = new ArrayList<String>();
    }
    
    public ModeSetting values(final String... values) {
        this.modes.addAll(Arrays.asList(values));
        if (this.value == null && !this.modes.isEmpty()) {
            this.value = (T)this.modes.getFirst();
        }
        return this;
    }
    
    public ModeSetting values(final Enum<?>... enums) {
        this.modes.addAll((Collection<? extends String>)Arrays.stream(enums).map(e -> {
            if (e instanceof final NamedChoice namedChoice) {
                return namedChoice.getName();
            }
            else {
                return e.name();
            }
        }).toList());
        if (this.value == null && !this.modes.isEmpty()) {
            this.value = (T)this.modes.getFirst();
        }
        return this;
    }
    
    public ModeSetting value(final Enum<?> e) {
        return this.value((e != null) ? ((e instanceof NamedChoice) ? ((NamedChoice)e).getName() : e.name()) : null);
    }
    
    public boolean is(final Enum<?> e) {
        if (e == null) {
            return false;
        }
        final String enumStr = (e instanceof NamedChoice) ? ((NamedChoice)e).getName() : e.name();
        return ((String)this.value).equals(enumStr);
    }
    
    @Override
    public ModeSetting value(final String value) {
        this.setValue(value);
        return this;
    }
    
    @Override
    public void setValue(final String value) {
        if (this.sameValue(value)) {
            return;
        }
        super.setValue(value);
        this.runAction();
    }
    
    @Override
    public ModeSetting setVisible(final Supplier<Boolean> condition) {
        return (ModeSetting)super.setVisible(condition);
    }
    
    @Override
    public ModeSetting onAction(final Runnable action) {
        return (ModeSetting)super.onAction(action);
    }
    
    public boolean is(final String value) {
        return ((String)this.value).equals(value);
    }
    
    @Generated
    public List<String> getModes() {
        return this.modes;
    }
    
    public interface NamedChoice
    {
        String getName();
    }
}
