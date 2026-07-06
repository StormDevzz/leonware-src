// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.screens;

import net.minecraft.class_4587;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import java.awt.Color;
import sweetie.leonware.api.utils.render.RenderUtil;
import net.minecraft.class_332;
import sweetie.leonware.LeonWare;
import sweetie.leonware.api.system.configs.ConfigManager;
import net.minecraft.class_364;
import net.minecraft.class_2561;
import net.minecraft.class_437;

public class CloseConfirmScreen extends class_437
{
    private final class_437 parent;
    
    public CloseConfirmScreen(final class_437 parent) {
        super((class_2561)class_2561.method_43473());
        this.parent = parent;
    }
    
    public boolean method_25422() {
        return false;
    }
    
    protected void method_25426() {
        final float dialogW = 260.0f;
        final float dialogH = 110.0f;
        final float dialogX = (this.field_22789 - dialogW) / 2.0f;
        final float dialogY = (this.field_22790 - dialogH) / 2.0f;
        final float btnW = 74.0f;
        final float btnH = 22.0f;
        final float btnY = dialogY + dialogH - btnH - 12.0f;
        final float gap = 8.0f;
        final float totalBtnsW = btnW * 3.0f + gap * 2.0f;
        final float startX = dialogX + (dialogW - totalBtnsW) / 2.0f;
        this.method_37063((class_364)new LeonButton((int)startX, (int)btnY, (int)btnW, (int)btnH, "\u041d\u0435\u0442", () -> this.field_22787.method_1507(this.parent)));
        this.method_37063((class_364)new LeonButton((int)(startX + btnW + gap), (int)btnY, (int)btnW, (int)btnH, "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u0438 \u0432\u044b\u0439\u0442\u0438", () -> {
            ConfigManager.getInstance().save("autoConfig");
            LeonWare.getInstance().onClose();
            this.field_22787.method_1592();
            return;
        }));
        this.method_37063((class_364)new LeonButton((int)(startX + (btnW + gap) * 2.0f), (int)btnY, (int)btnW, (int)btnH, "\u0412\u044b\u0439\u0442\u0438", () -> {
            LeonWare.getInstance().onClose();
            this.field_22787.method_1592();
        }));
    }
    
    public void method_25420(final class_332 context, final int mouseX, final int mouseY, final float delta) {
    }
    
    public void method_25394(final class_332 ctx, final int mx, final int my, final float delta) {
        this.method_25420(ctx, mx, my, delta);
        final float dialogW = 260.0f;
        final float dialogH = 110.0f;
        final float dialogX = (this.field_22789 - dialogW) / 2.0f;
        final float dialogY = (this.field_22790 - dialogH) / 2.0f;
        final float radius = 8.0f;
        final class_4587 ms = ctx.method_51448();
        RenderUtil.BLUR_RECT.draw(ms, dialogX, dialogY, dialogW, dialogH, radius, new Color(18, 18, 26, 240));
        final float font = 8.0f;
        final String title = "\u0412\u044b\u0439\u0442\u0438 \u0438\u0437 Minecraft?";
        final float titleW = Fonts.PS_BOLD.getWidth(title, font + 1.0f);
        Fonts.PS_BOLD.drawText(ms, title, dialogX + (dialogW - titleW) / 2.0f, dialogY + 18.0f, font + 1.0f, UIColors.textColor());
        final String sub = "\u041d\u0435\u0441\u043e\u0445\u0440\u0430\u043d\u0451\u043d\u043d\u044b\u0435 \u0438\u0437\u043c\u0435\u043d\u0435\u043d\u0438\u044f \u043a\u043e\u043d\u0444\u0438\u0433\u0430 \u0431\u0443\u0434\u0443\u0442 \u043f\u043e\u0442\u0435\u0440\u044f\u043d\u044b";
        final float subW = Fonts.PS_MEDIUM.getWidth(sub, font - 1.0f);
        Fonts.PS_MEDIUM.drawText(ms, sub, dialogX + (dialogW - subW) / 2.0f, dialogY + 35.0f, font - 1.0f, new Color(180, 180, 180));
        super.method_25394(ctx, mx, my, delta);
    }
}
