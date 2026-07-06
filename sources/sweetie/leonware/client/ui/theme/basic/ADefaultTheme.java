package sweetie.leonware.client.ui.theme.basic;

import java.awt.Color;
import sweetie.leonware.client.ui.theme.Theme;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/theme/basic/ADefaultTheme.class */
public abstract class ADefaultTheme extends Theme {
    public abstract Color setPrimary();

    public abstract Color setSecondary();

    public abstract Color setBlur();

    public abstract Color setWidgetBlur();

    public abstract Color setBackgroundBlur();

    public abstract Color setText();

    public abstract Color setInactiveText();

    public abstract Color setKnob();

    public abstract Color setInactiveKnob();

    public ADefaultTheme(String name) {
        super(name);
    }

    public ADefaultTheme update() {
        for (Theme.ElementColor elementColor : getElementColors()) {
            switch (elementColor.getName()) {
                case "Primary":
                    elementColor.setColor(setPrimary());
                    break;
                case "Secondary":
                    elementColor.setColor(setSecondary());
                    break;
                case "Blur":
                    elementColor.setColor(setBlur());
                    break;
                case "Widget blur":
                    elementColor.setColor(setWidgetBlur());
                    break;
                case "Background blur":
                    elementColor.setColor(setBackgroundBlur());
                    break;
                case "Text":
                    elementColor.setColor(setText());
                    break;
                case "Inactive text":
                    elementColor.setColor(setInactiveText());
                    break;
                case "Knob":
                    elementColor.setColor(setKnob());
                    break;
                case "Inactive knob":
                    elementColor.setColor(setInactiveKnob());
                    break;
            }
        }
        return this;
    }
}
