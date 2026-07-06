package sweetie.leonware.client.ui.widget.overlay;

import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.render.fonts.Icons;
import sweetie.leonware.client.ui.widget.InformationWidget;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/overlay/FPSWidget.class */
public class FPSWidget extends InformationWidget {
    private float animFps;

    @Override // sweetie.leonware.client.ui.widget.Widget
    public String getName() {
        return "FPS";
    }

    public FPSWidget() {
        super(50.0f, 100.0f);
    }

    @Override // sweetie.leonware.client.ui.widget.InformationWidget
    public String getValue() {
        this.animFps = MathUtil.interpolate((int) this.animFps, mc.method_47599(), 0.2f);
        return String.valueOf((int) this.animFps);
    }

    @Override // sweetie.leonware.client.ui.widget.InformationWidget
    public Icons getIcon() {
        return null;
    }
}
