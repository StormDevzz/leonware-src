// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.theme.basic;

import java.awt.Color;

public class GoldTheme extends ADefaultTheme
{
    public GoldTheme() {
        super("Gold");
    }
    
    @Override
    public Color setPrimary() {
        return new Color(255, 210, 60, 255);
    }
    
    @Override
    public Color setSecondary() {
        return new Color(200, 130, 20, 255);
    }
    
    @Override
    public Color setBlur() {
        return new Color(22, 16, 5, 255);
    }
    
    @Override
    public Color setWidgetBlur() {
        return new Color(58, 42, 10, 255);
    }
    
    @Override
    public Color setBackgroundBlur() {
        return new Color(72, 55, 12, 255);
    }
    
    @Override
    public Color setText() {
        return new Color(255, 240, 195, 255);
    }
    
    @Override
    public Color setInactiveText() {
        return new Color(205, 185, 130, 255);
    }
    
    @Override
    public Color setKnob() {
        return new Color(255, 220, 80, 255);
    }
    
    @Override
    public Color setInactiveKnob() {
        return new Color(255, 255, 255, 255);
    }
}
