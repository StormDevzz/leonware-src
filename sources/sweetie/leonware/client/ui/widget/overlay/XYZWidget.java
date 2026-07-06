package sweetie.leonware.client.ui.widget.overlay;

import sweetie.leonware.api.utils.render.fonts.Icons;
import sweetie.leonware.client.ui.widget.InformationWidget;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/overlay/XYZWidget.class */
public class XYZWidget extends InformationWidget {
    @Override // sweetie.leonware.client.ui.widget.Widget
    public String getName() {
        return "XYZ";
    }

    public XYZWidget() {
        super(30.0f, 120.0f);
    }

    @Override // sweetie.leonware.client.ui.widget.InformationWidget
    public String getValue() {
        String x = String.format("%.1f", Double.valueOf(mc.field_1724.method_23317()));
        String y = String.format("%.1f", Double.valueOf(mc.field_1724.method_23318()));
        String z = String.format("%.1f", Double.valueOf(mc.field_1724.method_23321()));
        return x + ", " + y + ", " + z;
    }

    @Override // sweetie.leonware.client.ui.widget.InformationWidget
    public Icons getIcon() {
        return null;
    }
}
