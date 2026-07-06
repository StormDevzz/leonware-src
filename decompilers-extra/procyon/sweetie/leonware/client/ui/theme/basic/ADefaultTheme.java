// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.theme.basic;

import java.util.Iterator;
import java.awt.Color;
import sweetie.leonware.client.ui.theme.Theme;

public abstract class ADefaultTheme extends Theme
{
    public ADefaultTheme(final String name) {
        super(name);
    }
    
    public abstract Color setPrimary();
    
    public abstract Color setSecondary();
    
    public abstract Color setBlur();
    
    public abstract Color setWidgetBlur();
    
    public abstract Color setBackgroundBlur();
    
    public abstract Color setText();
    
    public abstract Color setInactiveText();
    
    public abstract Color setKnob();
    
    public abstract Color setInactiveKnob();
    
    public ADefaultTheme update() {
        for (final ElementColor elementColor : this.getElementColors()) {
            final String name = elementColor.getName();
            switch (name) {
                case "Primary": {
                    elementColor.setColor(this.setPrimary());
                    continue;
                }
                case "Secondary": {
                    elementColor.setColor(this.setSecondary());
                    continue;
                }
                case "Blur": {
                    elementColor.setColor(this.setBlur());
                    continue;
                }
                case "Widget blur": {
                    elementColor.setColor(this.setWidgetBlur());
                    continue;
                }
                case "Background blur": {
                    elementColor.setColor(this.setBackgroundBlur());
                    continue;
                }
                case "Text": {
                    elementColor.setColor(this.setText());
                    continue;
                }
                case "Inactive text": {
                    elementColor.setColor(this.setInactiveText());
                    continue;
                }
                case "Knob": {
                    elementColor.setColor(this.setKnob());
                    continue;
                }
                case "Inactive knob": {
                    elementColor.setColor(this.setInactiveKnob());
                    continue;
                }
            }
        }
        return this;
    }
}
