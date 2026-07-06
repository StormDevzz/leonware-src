// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.theme.basic;

import java.awt.Color;

public class SunsetTheme extends ADefaultTheme
{
    public SunsetTheme() {
        super("Sunset");
    }
    
    @Override
    public Color setPrimary() {
        return new Color(255, 120, 80, 255);
    }
    
    @Override
    public Color setSecondary() {
        return new Color(220, 60, 120, 255);
    }
    
    @Override
    public Color setBlur() {
        return new Color(30, 12, 18, 255);
    }
    
    @Override
    public Color setWidgetBlur() {
        return new Color(72, 25, 38, 255);
    }
    
    @Override
    public Color setBackgroundBlur() {
        return new Color(88, 32, 50, 255);
    }
    
    @Override
    public Color setText() {
        return new Color(255, 220, 210, 255);
    }
    
    @Override
    public Color setInactiveText() {
        return new Color(210, 155, 140, 255);
    }
    
    @Override
    public Color setKnob() {
        return new Color(255, 145, 100, 255);
    }
    
    @Override
    public Color setInactiveKnob() {
        return new Color(255, 255, 255, 255);
    }
}
