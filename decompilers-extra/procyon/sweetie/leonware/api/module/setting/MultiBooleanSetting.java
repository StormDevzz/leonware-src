// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.module.setting;

import java.util.function.Supplier;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class MultiBooleanSetting extends Setting<List<BooleanSetting>>
{
    public MultiBooleanSetting(final String name) {
        super(name);
        this.value = (T)new ArrayList();
    }
    
    @Override
    public MultiBooleanSetting value(final List<BooleanSetting> value) {
        this.setValue(value);
        return this;
    }
    
    public MultiBooleanSetting value(final BooleanSetting... value) {
        this.setValue(new ArrayList<BooleanSetting>(Arrays.asList(value)));
        return this;
    }
    
    @Override
    public void setValue(final List<BooleanSetting> value) {
        if (this.sameValue(value)) {
            return;
        }
        super.setValue(value);
        this.runAction();
    }
    
    @Override
    public MultiBooleanSetting onAction(final Runnable action) {
        return (MultiBooleanSetting)super.onAction(action);
    }
    
    public MultiBooleanSetting addValues(final BooleanSetting... value) {
        ((List)this.value).addAll(Arrays.asList(value));
        return this;
    }
    
    public boolean isEnabled(final String name) {
        return this.getBooleanSettingByName(name).map((Function<? super BooleanSetting, ? extends Boolean>)Setting::getValue).orElse(false);
    }
    
    public boolean toggle(final String name) {
        return this.getBooleanSettingByName(name).map(booleanSetting -> {
            booleanSetting.value(!booleanSetting.getValue());
            return true;
        }).orElse(false);
    }
    
    public List<String> getAllNames() {
        return (List)((List)this.value).stream().map(Setting::getName).collect(Collectors.toList());
    }
    
    public List<String> getList() {
        return (List)((List)this.value).stream().filter(Setting::getValue).map(Setting::getName).collect(Collectors.toList());
    }
    
    private Optional<BooleanSetting> getBooleanSettingByName(final String name) {
        return ((List)this.value).stream().filter(setting -> setting.getName().equalsIgnoreCase(name)).findFirst();
    }
    
    @Override
    public MultiBooleanSetting setVisible(final Supplier<Boolean> condition) {
        return (MultiBooleanSetting)super.setVisible(condition);
    }
}
