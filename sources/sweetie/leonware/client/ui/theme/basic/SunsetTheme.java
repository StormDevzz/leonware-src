package sweetie.leonware.client.ui.theme.basic;

import java.awt.Color;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/theme/basic/SunsetTheme.class */
public class SunsetTheme extends ADefaultTheme {
    public SunsetTheme() {
        super("Sunset");
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setPrimary() {
        return new Color(255, 120, 80, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setSecondary() {
        return new Color(220, 60, 120, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setBlur() {
        return new Color(30, 12, 18, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setWidgetBlur() {
        return new Color(72, 25, 38, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setBackgroundBlur() {
        return new Color(88, 32, 50, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setText() {
        return new Color(255, 220, 210, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setInactiveText() {
        return new Color(210, 155, 140, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setKnob() {
        return new Color(255, 145, 100, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setInactiveKnob() {
        return new Color(255, 255, 255, 255);
    }
}
