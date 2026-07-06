package sweetie.leonware.client.ui.widget.overlay;

import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.render.fonts.Icons;
import sweetie.leonware.client.ui.widget.InformationWidget;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/overlay/BPSWidget.class */
public class BPSWidget extends InformationWidget {
    @Override // sweetie.leonware.client.ui.widget.Widget
    public String getName() {
        return "BPS";
    }

    public BPSWidget() {
        super(80.0f, 120.0f);
    }

    @Override // sweetie.leonware.client.ui.widget.InformationWidget
    public String getValue() {
        return String.format("%.2f", Double.valueOf(MathUtil.getEntityBPS(mc.field_1724)));
    }

    @Override // sweetie.leonware.client.ui.widget.InformationWidget
    public Icons getIcon() {
        return null;
    }
}
