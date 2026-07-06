// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.screens;

import net.minecraft.class_4587;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.api.utils.render.RenderUtil;
import java.awt.Color;
import net.minecraft.class_332;
import net.minecraft.class_2561;
import net.minecraft.class_4185;

public class LeonButton extends class_4185
{
    public LeonButton(final int x, final int y, final int width, final int height, final String label, final Runnable action) {
        super(x, y, width, height, (class_2561)class_2561.method_43473(), btn -> action.run(), LeonButton.field_40754);
        this.method_25355((class_2561)class_2561.method_43470(label));
    }
    
    public void method_48579(final class_332 ctx, final int mx, final int my, final float delta) {
        final boolean hovered = this.method_49606();
        final Color bg = hovered ? new Color(50, 50, 70, 230) : new Color(30, 30, 45, 210);
        final class_4587 ms = ctx.method_51448();
        RenderUtil.RECT.draw(ms, (float)this.method_46426(), (float)this.method_46427(), (float)this.method_25368(), (float)this.method_25364(), 4.0f, bg);
        final String text = this.method_25369().getString();
        final float font = 6.5f;
        final float tw = Fonts.PS_MEDIUM.getWidth(text, font);
        Fonts.PS_MEDIUM.drawText(ms, text, this.method_46426() + (this.method_25368() - tw) / 2.0f, this.method_46427() + (this.method_25364() - font) / 2.0f, font, hovered ? UIColors.textColor() : new Color(200, 200, 200));
    }
}
