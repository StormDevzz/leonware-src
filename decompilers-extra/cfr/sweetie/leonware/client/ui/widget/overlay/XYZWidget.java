/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.client.ui.widget.overlay;

import sweetie.leonware.api.utils.render.fonts.Icons;
import sweetie.leonware.client.ui.widget.InformationWidget;

public class XYZWidget
extends InformationWidget {
    @Override
    public String getName() {
        return "XYZ";
    }

    public XYZWidget() {
        super(30.0f, 120.0f);
    }

    @Override
    public String getValue() {
        String x = String.format("%.1f", XYZWidget.mc.field_1724.method_23317());
        String y = String.format("%.1f", XYZWidget.mc.field_1724.method_23318());
        String z = String.format("%.1f", XYZWidget.mc.field_1724.method_23321());
        return x + ", " + y + ", " + z;
    }

    @Override
    public Icons getIcon() {
        return null;
    }
}

