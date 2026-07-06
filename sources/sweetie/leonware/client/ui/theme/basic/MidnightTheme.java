package sweetie.leonware.client.ui.theme.basic;

import java.awt.Color;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/theme/basic/MidnightTheme.class */
public class MidnightTheme extends ADefaultTheme {
    public MidnightTheme() {
        super("Midnight");
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setPrimary() {
        return new Color(90, 105, 255, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setSecondary() {
        return new Color(50, 50, 180, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setBlur() {
        return new Color(5, 5, 18, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setWidgetBlur() {
        return new Color(14, 14, 55, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setBackgroundBlur() {
        return new Color(18, 18, 68, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setText() {
        return new Color(200, 205, 255, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setInactiveText() {
        return new Color(130, 135, 200, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setKnob() {
        return new Color(110, 125, 255, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setInactiveKnob() {
        return new Color(255, 255, 255, 255);
    }
}
