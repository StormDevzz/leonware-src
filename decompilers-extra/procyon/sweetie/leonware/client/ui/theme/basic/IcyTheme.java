// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.theme.basic;

import java.awt.Color;

public class IcyTheme extends ADefaultTheme
{
    public IcyTheme() {
        super("Icy");
    }
    
    @Override
    public Color setPrimary() {
        return new Color(180, 230, 255, 255);
    }
    
    @Override
    public Color setSecondary() {
        return new Color(100, 175, 240, 255);
    }
    
    @Override
    public Color setBlur() {
        return new Color(12, 20, 30, 255);
    }
    
    @Override
    public Color setWidgetBlur() {
        return new Color(30, 55, 80, 255);
    }
    
    @Override
    public Color setBackgroundBlur() {
        return new Color(38, 68, 100, 255);
    }
    
    @Override
    public Color setText() {
        return new Color(220, 240, 255, 255);
    }
    
    @Override
    public Color setInactiveText() {
        return new Color(155, 185, 215, 255);
    }
    
    @Override
    public Color setKnob() {
        return new Color(190, 235, 255, 255);
    }
    
    @Override
    public Color setInactiveKnob() {
        return new Color(255, 255, 255, 255);
    }
}
