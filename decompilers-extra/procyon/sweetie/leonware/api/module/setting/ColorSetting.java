// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.module.setting;

import java.util.function.Supplier;
import java.awt.Color;

public class ColorSetting extends Setting<Color>
{
    private boolean rgbMode;
    private boolean uiMode;
    
    public ColorSetting(final String name) {
        super(name);
        this.rgbMode = false;
        this.uiMode = false;
    }
    
    @Override
    public ColorSetting value(final Color value) {
        this.setValue(value);
        return this;
    }
    
    @Override
    public void setValue(final Color value) {
        if (this.sameValue(value)) {
            return;
        }
        super.setValue(value);
        this.runAction();
    }
    
    public boolean isRgbMode() {
        return this.rgbMode;
    }
    
    public boolean isUiMode() {
        return this.uiMode;
    }
    
    public void setRgbMode(final boolean value) {
        this.rgbMode = value;
    }
    
    public void setUiMode(final boolean value) {
        this.uiMode = value;
    }
    
    @Override
    public ColorSetting setVisible(final Supplier<Boolean> condition) {
        return (ColorSetting)super.setVisible(condition);
    }
    
    @Override
    public ColorSetting onAction(final Runnable action) {
        return (ColorSetting)super.onAction(action);
    }
}
