/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.client.ui.theme.basic;

import java.awt.Color;
import sweetie.leonware.client.ui.theme.basic.ADefaultTheme;

public class OceanTheme
extends ADefaultTheme {
    public OceanTheme() {
        super("Ocean");
    }

    @Override
    public Color setPrimary() {
        return new Color(64, 220, 230, 255);
    }

    @Override
    public Color setSecondary() {
        return new Color(32, 130, 200, 255);
    }

    @Override
    public Color setBlur() {
        return new Color(8, 18, 30, 255);
    }

    @Override
    public Color setWidgetBlur() {
        return new Color(18, 48, 78, 255);
    }

    @Override
    public Color setBackgroundBlur() {
        return new Color(22, 62, 98, 255);
    }

    @Override
    public Color setText() {
        return new Color(200, 240, 255, 255);
    }

    @Override
    public Color setInactiveText() {
        return new Color(130, 185, 215, 255);
    }

    @Override
    public Color setKnob() {
        return new Color(70, 230, 240, 255);
    }

    @Override
    public Color setInactiveKnob() {
        return new Color(255, 255, 255, 255);
    }
}

