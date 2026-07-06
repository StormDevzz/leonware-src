package sweetie.leonware.client.ui.screens;

import java.awt.Color;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_4185;
import net.minecraft.class_4587;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/screens/LeonButton.class */
public class LeonButton extends class_4185 {
    public LeonButton(int x, int y, int width, int height, String label, Runnable action) {
        super(x, y, width, height, class_2561.method_43473(), btn -> {
            action.run();
        }, field_40754);
        method_25355(class_2561.method_43470(label));
    }

    public void method_48579(class_332 ctx, int mx, int my, float delta) {
        Color color;
        boolean hovered = method_49606();
        if (hovered) {
            color = new Color(50, 50, 70, 230);
        } else {
            color = new Color(30, 30, 45, 210);
        }
        Color bg = color;
        class_4587 ms = ctx.method_51448();
        RenderUtil.RECT.draw(ms, method_46426(), method_46427(), method_25368(), method_25364(), 4.0f, bg);
        String text = method_25369().getString();
        float tw = Fonts.PS_MEDIUM.getWidth(text, 6.5f);
        Fonts.PS_MEDIUM.drawText(ms, text, method_46426() + ((method_25368() - tw) / 2.0f), method_46427() + ((method_25364() - 6.5f) / 2.0f), 6.5f, hovered ? UIColors.textColor() : new Color(200, 200, 200));
    }
}
