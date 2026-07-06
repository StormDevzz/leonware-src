package sweetie.leonware.client.ui.theme.basic;

import java.awt.Color;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/theme/basic/GoldTheme.class */
public class GoldTheme extends ADefaultTheme {
    public GoldTheme() {
        super("Gold");
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setPrimary() {
        return new Color(255, 210, 60, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setSecondary() {
        return new Color(200, 130, 20, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setBlur() {
        return new Color(22, 16, 5, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setWidgetBlur() {
        return new Color(58, 42, 10, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setBackgroundBlur() {
        return new Color(72, 55, 12, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setText() {
        return new Color(255, 240, 195, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setInactiveText() {
        return new Color(205, 185, 130, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setKnob() {
        return new Color(255, 220, 80, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setInactiveKnob() {
        return new Color(255, 255, 255, 255);
    }
}
