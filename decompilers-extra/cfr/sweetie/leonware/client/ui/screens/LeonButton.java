/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_332
 *  net.minecraft.class_4185
 *  net.minecraft.class_4587
 */
package sweetie.leonware.client.ui.screens;

import java.awt.Color;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_4185;
import net.minecraft.class_4587;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;

public class LeonButton
extends class_4185 {
    public LeonButton(int x, int y, int width, int height, String label, Runnable action) {
        super(x, y, width, height, (class_2561)class_2561.method_43473(), btn -> action.run(), field_40754);
        this.method_25355((class_2561)class_2561.method_43470((String)label));
    }

    public void method_48579(class_332 ctx, int mx, int my, float delta) {
        boolean hovered = this.method_49606();
        Color bg = hovered ? new Color(50, 50, 70, 230) : new Color(30, 30, 45, 210);
        class_4587 ms = ctx.method_51448();
        RenderUtil.RECT.draw(ms, (float)this.method_46426(), (float)this.method_46427(), (float)this.method_25368(), (float)this.method_25364(), 4.0f, bg);
        String text = this.method_25369().getString();
        float font = 6.5f;
        float tw = Fonts.PS_MEDIUM.getWidth(text, font);
        Fonts.PS_MEDIUM.drawText(ms, text, (float)this.method_46426() + ((float)this.method_25368() - tw) / 2.0f, (float)this.method_46427() + ((float)this.method_25364() - font) / 2.0f, font, hovered ? UIColors.textColor() : new Color(200, 200, 200));
    }
}

