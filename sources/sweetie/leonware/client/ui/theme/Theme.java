package sweetie.leonware.client.ui.theme;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import sweetie.leonware.client.ui.clickgui.module.settings.ColorComponent;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/theme/Theme.class */
public class Theme {
    private final String name;
    private final List<ElementColor> elementColors = new ArrayList();

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public List<ElementColor> getElementColors() {
        return this.elementColors;
    }

    public Theme(String name) {
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
        return getElementColor("Primary");
    }

    public Color getSecondaryColor() {
        return getElementColor("Secondary");
    }

    public Color getBlurColor() {
        return getElementColor("Blur");
    }

    public Color getWidgetBlurColor() {
        return getElementColor("Widget blur");
    }

    public Color getBackgroundBlurColor() {
        return getElementColor("Background blur");
    }

    public Color getTextColor() {
        return getElementColor("Text");
    }

    public Color getInactiveTextColor() {
        return getElementColor("Inactive text");
    }

    public Color getKnobColor() {
        return getElementColor("Knob");
    }

    public Color getInactiveKnobColor() {
        return getElementColor("Inactive knob");
    }

    public Color getPositiveColor() {
        return getElementColor("Positive");
    }

    public Color getMiddleColor() {
        return getElementColor("Middle");
    }

    public Color getNegativeColor() {
        return getElementColor("Negative");
    }

    public Color getElementColor(String elementName) {
        for (ElementColor element : this.elementColors) {
            if (element.getName().equalsIgnoreCase(elementName)) {
                return element.getColor();
            }
        }
        return new Color(-1);
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/theme/Theme$ElementColor.class */
    public static class ElementColor {
        private final String name;
        private Color color;
        private final ColorComponent colorComponent = new ColorComponent(this);

        @Generated
        public String getName() {
            return this.name;
        }

        @Generated
        public Color getColor() {
            return this.color;
        }

        @Generated
        public void setColor(Color color) {
            this.color = color;
        }

        @Generated
        public ColorComponent getColorComponent() {
            return this.colorComponent;
        }

        public ElementColor(String name, Color color) {
            this.name = name;
            this.color = color;
        }
    }
}
