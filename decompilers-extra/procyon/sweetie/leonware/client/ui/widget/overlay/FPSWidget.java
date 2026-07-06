// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.widget.overlay;

import sweetie.leonware.api.utils.render.fonts.Icons;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.client.ui.widget.InformationWidget;

public class FPSWidget extends InformationWidget
{
    private float animFps;
    
    @Override
    public String getName() {
        return "FPS";
    }
    
    public FPSWidget() {
        super(50.0f, 100.0f);
    }
    
    @Override
    public String getValue() {
        this.animFps = (float)MathUtil.interpolate((int)this.animFps, FPSWidget.mc.method_47599(), 0.2f);
        return String.valueOf((int)this.animFps);
    }
    
    @Override
    public Icons getIcon() {
        return null;
    }
}
