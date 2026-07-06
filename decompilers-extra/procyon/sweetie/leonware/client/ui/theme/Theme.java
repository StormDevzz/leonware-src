// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.theme;

import sweetie.leonware.client.ui.clickgui.module.settings.ColorComponent;
import lombok.Generated;
import java.util.Iterator;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Theme
{
    private final String name;
    private final List<ElementColor> elementColors;
    
    public Theme(final String name) {
        this.elementColors = new ArrayList<ElementColor>();
        this.name = name;
        this.elementColors.add(new ElementColor("Primary", new Color(190, 141, 255)));
        this.elementColors.add(new ElementColor("Secondary", new Color(168, 108, 255)));
        this.elementColors.add(new ElementColor("Blur", new Color(37, 33, 46)));
        this.elementColors.add(new ElementColor("Widget blur", new Color(23, 23, 32, 255)));
        this.elementColors.add(new ElementColor("Background blur", new Color(35, 29, 47, 255)));
        this.elementColors.add(new ElementColor("Text", new Color(231, 217, 255, 255)));
        this.elementColors.add(new ElementColor("Inactive text", new Color(152, 152, 152)));
        this.elementColors.add(new ElementColor("Knob", new Color(181, 151, 252)));
        this.elementColors.add(new ElementColor("Inactive knob", new Color(255, 255, 255)));
        this.elementColors.add(new ElementColor("Positive", new Color(130, 255, 130)));
        this.elementColors.add(new ElementColor("Middle", new Color(255, 200, 95)));
        this.elementColors.add(new ElementColor("Negative", new Color(255, 80, 80)));
    }
    
    public Color getPrimaryColor() {
        return this.getElementColor("Primary");
    }
    
    public Color getSecondaryColor() {
        return this.getElementColor("Secondary");
    }
    
    public Color getBlurColor() {
        return this.getElementColor("Blur");
    }
    
    public Color getWidgetBlurColor() {
        return this.getElementColor("Widget blur");
    }
    
    public Color getBackgroundBlurColor() {
        return this.getElementColor("Background blur");
    }
    
    public Color getTextColor() {
        return this.getElementColor("Text");
    }
    
    public Color getInactiveTextColor() {
        return this.getElementColor("Inactive text");
    }
    
    public Color getKnobColor() {
        return this.getElementColor("Knob");
    }
    
    public Color getInactiveKnobColor() {
        return this.getElementColor("Inactive knob");
    }
    
    public Color getPositiveColor() {
        return this.getElementColor("Positive");
    }
    
    public Color getMiddleColor() {
        return this.getElementColor("Middle");
    }
    
    public Color getNegativeColor() {
        return this.getElementColor("Negative");
    }
    
    public Color getElementColor(final String elementName) {
        for (final ElementColor element : this.elementColors) {
            if (element.getName().equalsIgnoreCase(elementName)) {
                return element.getColor();
            }
        }
        return new Color(-1);
    }
    
    @Generated
    public String getName() {
        return this.name;
    }
    
    @Generated
    public List<ElementColor> getElementColors() {
        return this.elementColors;
    }
    
    public static class ElementColor
    {
        private final String name;
        private Color color;
        private final ColorComponent colorComponent;
        
        public ElementColor(final String name, final Color color) {
            this.name = name;
            this.color = color;
            this.colorComponent = new ColorComponent(this);
        }
        
        @Generated
        public String getName() {
            return this.name;
        }
        
        @Generated
        public Color getColor() {
            return this.color;
        }
        
        @Generated
        public ColorComponent getColorComponent() {
            return this.colorComponent;
        }
        
        @Generated
        public void setColor(final Color color) {
            this.color = color;
        }
    }
}
