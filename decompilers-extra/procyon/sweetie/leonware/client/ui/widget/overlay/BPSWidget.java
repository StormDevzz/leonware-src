// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.widget.overlay;

import sweetie.leonware.api.utils.render.fonts.Icons;
import net.minecraft.class_1297;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.client.ui.widget.InformationWidget;

public class BPSWidget extends InformationWidget
{
    @Override
    public String getName() {
        return "BPS";
    }
    
    public BPSWidget() {
        super(80.0f, 120.0f);
    }
    
    @Override
    public String getValue() {
        return String.format("%.2f", MathUtil.getEntityBPS((class_1297)BPSWidget.mc.field_1724));
    }
    
    @Override
    public Icons getIcon() {
        return null;
    }
}
