// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.theme.basic;

import java.awt.Color;

public class RoseGoldTheme extends ADefaultTheme
{
    public RoseGoldTheme() {
        super("Rose Gold");
    }
    
    @Override
    public Color setPrimary() {
        return new Color(255, 168, 155, 255);
    }
    
    @Override
    public Color setSecondary() {
        return new Color(200, 120, 100, 255);
    }
    
    @Override
    public Color setBlur() {
        return new Color(28, 14, 12, 255);
    }
    
    @Override
    public Color setWidgetBlur() {
        return new Color(72, 36, 30, 255);
    }
    
    @Override
    public Color setBackgroundBlur() {
        return new Color(88, 45, 38, 255);
    }
    
    @Override
    public Color setText() {
        return new Color(255, 225, 218, 255);
    }
    
    @Override
    public Color setInactiveText() {
        return new Color(210, 165, 155, 255);
    }
    
    @Override
    public Color setKnob() {
        return new Color(255, 180, 168, 255);
    }
    
    @Override
    public Color setInactiveKnob() {
        return new Color(255, 255, 255, 255);
    }
}
