// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.theme.basic;

import java.awt.Color;

public class NeonTheme extends ADefaultTheme
{
    public NeonTheme() {
        super("Neon");
    }
    
    @Override
    public Color setPrimary() {
        return new Color(185, 48, 255, 255);
    }
    
    @Override
    public Color setSecondary() {
        return new Color(0, 255, 128, 255);
    }
    
    @Override
    public Color setBlur() {
        return new Color(10, 5, 20, 255);
    }
    
    @Override
    public Color setWidgetBlur() {
        return new Color(30, 12, 55, 255);
    }
    
    @Override
    public Color setBackgroundBlur() {
        return new Color(38, 16, 70, 255);
    }
    
    @Override
    public Color setText() {
        return new Color(230, 200, 255, 255);
    }
    
    @Override
    public Color setInactiveText() {
        return new Color(165, 130, 200, 255);
    }
    
    @Override
    public Color setKnob() {
        return new Color(200, 60, 255, 255);
    }
    
    @Override
    public Color setInactiveKnob() {
        return new Color(255, 255, 255, 255);
    }
}
