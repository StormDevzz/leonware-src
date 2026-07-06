/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.client.ui.theme.basic;

import java.awt.Color;
import sweetie.leonware.client.ui.theme.Theme;

public abstract class ADefaultTheme
extends Theme {
    public ADefaultTheme(String name) {
        super(name);
    }

    public abstract Color setPrimary();

    public abstract Color setSecondary();

    public abstract Color setBlur();

    public abstract Color setWidgetBlur();

    public abstract Color setBackgroundBlur();

    public abstract Color setText();

    public abstract Color setInactiveText();

    public abstract Color setKnob();

    public abstract Color setInactiveKnob();

    public ADefaultTheme update() {
        for (Theme.ElementColor elementColor : this.getElementColors()) {
            switch (elementColor.getName()) {
                case "Primary": {
                    elementColor.setColor(this.setPrimary());
                    break;
                }
                case "Secondary": {
                    elementColor.setColor(this.setSecondary());
                    break;
                }
                case "Blur": {
                    elementColor.setColor(this.setBlur());
                    break;
                }
                case "Widget blur": {
                    elementColor.setColor(this.setWidgetBlur());
                    break;
                }
                case "Background blur": {
                    elementColor.setColor(this.setBackgroundBlur());
                    break;
                }
                case "Text": {
                    elementColor.setColor(this.setText());
                    break;
                }
                case "Inactive text": {
                    elementColor.setColor(this.setInactiveText());
                    break;
                }
                case "Knob": {
                    elementColor.setColor(this.setKnob());
                    break;
                }
                case "Inactive knob": {
                    elementColor.setColor(this.setInactiveKnob());
                }
            }
        }
        return this;
    }
}

