package sweetie.leonware.client.ui.theme.basic;

import java.awt.Color;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/theme/basic/ForestTheme.class */
public class ForestTheme extends ADefaultTheme {
    public ForestTheme() {
        super("Forest");
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setPrimary() {
        return new Color(72, 199, 116, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setSecondary() {
        return new Color(30, 130, 76, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setBlur() {
        return new Color(10, 24, 15, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setWidgetBlur() {
        return new Color(22, 55, 33, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setBackgroundBlur() {
        return new Color(28, 72, 44, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setText() {
        return new Color(210, 255, 225, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setInactiveText() {
        return new Color(140, 200, 160, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setKnob() {
        return new Color(80, 220, 130, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setInactiveKnob() {
        return new Color(255, 255, 255, 255);
    }
}
