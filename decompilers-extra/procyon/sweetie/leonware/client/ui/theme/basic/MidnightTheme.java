// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.theme.basic;

import java.awt.Color;

public class MidnightTheme extends ADefaultTheme
{
    public MidnightTheme() {
        super("Midnight");
    }
    
    @Override
    public Color setPrimary() {
        return new Color(90, 105, 255, 255);
    }
    
    @Override
    public Color setSecondary() {
        return new Color(50, 50, 180, 255);
    }
    
    @Override
    public Color setBlur() {
        return new Color(5, 5, 18, 255);
    }
    
    @Override
    public Color setWidgetBlur() {
        return new Color(14, 14, 55, 255);
    }
    
    @Override
    public Color setBackgroundBlur() {
        return new Color(18, 18, 68, 255);
    }
    
    @Override
    public Color setText() {
        return new Color(200, 205, 255, 255);
    }
    
    @Override
    public Color setInactiveText() {
        return new Color(130, 135, 200, 255);
    }
    
    @Override
    public Color setKnob() {
        return new Color(110, 125, 255, 255);
    }
    
    @Override
    public Color setInactiveKnob() {
        return new Color(255, 255, 255, 255);
    }
}
