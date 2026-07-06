package sweetie.leonware.client.ui.screens;

import java.awt.Color;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_4587;
import sweetie.leonware.LeonWare;
import sweetie.leonware.api.system.configs.ConfigManager;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/screens/CloseConfirmScreen.class */
public class CloseConfirmScreen extends class_437 {
    private final class_437 parent;

    public CloseConfirmScreen(class_437 parent) {
        super(class_2561.method_43473());
        this.parent = parent;
    }

    public boolean method_25422() {
        return false;
    }

    protected void method_25426() {
        float dialogX = (this.field_22789 - 260.0f) / 2.0f;
        float dialogY = (this.field_22790 - 110.0f) / 2.0f;
        float btnY = ((dialogY + 110.0f) - 22.0f) - 12.0f;
        float totalBtnsW = (74.0f * 3.0f) + (8.0f * 2.0f);
        float startX = dialogX + ((260.0f - totalBtnsW) / 2.0f);
        method_37063(new LeonButton((int) startX, (int) btnY, (int) 74.0f, (int) 22.0f, "Нет", () -> {
            this.field_22787.method_1507(this.parent);
        }));
        method_37063(new LeonButton((int) (startX + 74.0f + 8.0f), (int) btnY, (int) 74.0f, (int) 22.0f, "Сохранить и выйти", () -> {
            ConfigManager.getInstance().save("autoConfig");
            LeonWare.getInstance().onClose();
            this.field_22787.method_1592();
        }));
        method_37063(new LeonButton((int) (startX + ((74.0f + 8.0f) * 2.0f)), (int) btnY, (int) 74.0f, (int) 22.0f, "Выйти", () -> {
            LeonWare.getInstance().onClose();
            this.field_22787.method_1592();
        }));
    }

    public void method_25420(class_332 context, int mouseX, int mouseY, float delta) {
    }

    public void method_25394(class_332 ctx, int mx, int my, float delta) {
        method_25420(ctx, mx, my, delta);
        float dialogX = (this.field_22789 - 260.0f) / 2.0f;
        float dialogY = (this.field_22790 - 110.0f) / 2.0f;
        class_4587 ms = ctx.method_51448();
        RenderUtil.BLUR_RECT.draw(ms, dialogX, dialogY, 260.0f, 110.0f, 8.0f, new Color(18, 18, 26, 240));
        float titleW = Fonts.PS_BOLD.getWidth("Выйти из Minecraft?", 8.0f + 1.0f);
        Fonts.PS_BOLD.drawText(ms, "Выйти из Minecraft?", dialogX + ((260.0f - titleW) / 2.0f), dialogY + 18.0f, 8.0f + 1.0f, UIColors.textColor());
        float subW = Fonts.PS_MEDIUM.getWidth("Несохранённые изменения конфига будут потеряны", 8.0f - 1.0f);
        Fonts.PS_MEDIUM.drawText(ms, "Несохранённые изменения конфига будут потеряны", dialogX + ((260.0f - subW) / 2.0f), dialogY + 35.0f, 8.0f - 1.0f, new Color(180, 180, 180));
        super.method_25394(ctx, mx, my, delta);
    }
}
