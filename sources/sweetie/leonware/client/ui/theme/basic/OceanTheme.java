package sweetie.leonware.client.ui.theme.basic;

import java.awt.Color;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/theme/basic/OceanTheme.class */
public class OceanTheme extends ADefaultTheme {
    public OceanTheme() {
        super("Ocean");
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setPrimary() {
        return new Color(64, 220, 230, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setSecondary() {
        return new Color(32, 130, 200, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setBlur() {
        return new Color(8, 18, 30, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setWidgetBlur() {
        return new Color(18, 48, 78, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setBackgroundBlur() {
        return new Color(22, 62, 98, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setText() {
        return new Color(200, 240, 255, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setInactiveText() {
        return new Color(130, 185, 215, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setKnob() {
        return new Color(70, 230, 240, 255);
    }

    @Override // sweetie.leonware.client.ui.theme.basic.ADefaultTheme
    public Color setInactiveKnob() {
        return new Color(255, 255, 255, 255);
    }
}
