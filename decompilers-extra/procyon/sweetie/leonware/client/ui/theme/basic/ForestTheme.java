// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.theme.basic;

import java.awt.Color;

public class ForestTheme extends ADefaultTheme
{
    public ForestTheme() {
        super("Forest");
    }
    
    @Override
    public Color setPrimary() {
        return new Color(72, 199, 116, 255);
    }
    
    @Override
    public Color setSecondary() {
        return new Color(30, 130, 76, 255);
    }
    
    @Override
    public Color setBlur() {
        return new Color(10, 24, 15, 255);
    }
    
    @Override
    public Color setWidgetBlur() {
        return new Color(22, 55, 33, 255);
    }
    
    @Override
    public Color setBackgroundBlur() {
        return new Color(28, 72, 44, 255);
    }
    
    @Override
    public Color setText() {
        return new Color(210, 255, 225, 255);
    }
    
    @Override
    public Color setInactiveText() {
        return new Color(140, 200, 160, 255);
    }
    
    @Override
    public Color setKnob() {
        return new Color(80, 220, 130, 255);
    }
    
    @Override
    public Color setInactiveKnob() {
        return new Color(255, 255, 255, 255);
    }
}
