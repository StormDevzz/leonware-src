package sweetie.leonware.client.ui.widget.overlay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_2561;
import net.minecraft.class_266;
import net.minecraft.class_268;
import net.minecraft.class_269;
import net.minecraft.class_327;
import net.minecraft.class_4587;
import net.minecraft.class_8646;
import net.minecraft.class_9011;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.client.ui.widget.Widget;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/overlay/ScoreboardWidget.class */
public class ScoreboardWidget extends Widget {
    public final ModeSetting renderMode;
    private final List<class_9011> entriesBuffer;
    private final List<class_2561> formattedNamesBuffer;
    private static final Comparator<class_9011> ENTRY_COMPARATOR = (a, b) -> {
        return Integer.compare(b.comp_2128(), a.comp_2128());
    };

    public ScoreboardWidget() {
        super(3.0f, 50.0f);
        this.renderMode = new ModeSetting("Scoreboard").values("Blur", "Default", "Gradient", "Texture").value("Blur");
        this.entriesBuffer = new ArrayList();
        this.formattedNamesBuffer = new ArrayList();
    }

    @Override // sweetie.leonware.client.ui.widget.Widget
    public String getName() {
        return "Scoreboard";
    }

    @Override // sweetie.leonware.api.system.interfaces.IRenderer
    public void render(class_4587 matrixStack) {
        class_269 scoreboard;
        class_266 objective;
        float totalWidth;
        float totalHeight;
        float x;
        float y;
        if (mc.field_1687 == null || (objective = (scoreboard = mc.field_1687.method_8428()).method_1189(class_8646.field_45157)) == null) {
            return;
        }
        class_2561 title = objective.method_1114();
        Collection<class_9011> entries = scoreboard.method_1184(objective);
        this.entriesBuffer.clear();
        for (class_9011 score : entries) {
            if (score.comp_2127() != null && !score.comp_2127().startsWith("#")) {
                this.entriesBuffer.add(score);
            }
        }
        this.entriesBuffer.sort(ENTRY_COMPARATOR);
        while (this.entriesBuffer.size() > 15) {
            this.entriesBuffer.remove(this.entriesBuffer.size() - 1);
        }
        if (this.entriesBuffer.isEmpty() && title.getString().isEmpty()) {
            return;
        }
        float gap = getGap();
        float internalGap = gap * 0.8f;
        float titleWidth = mc.field_1772.method_27525(title);
        float maxWidth = titleWidth;
        this.formattedNamesBuffer.clear();
        int n = this.entriesBuffer.size();
        for (int i = 0; i < n; i++) {
            class_9011 entry = this.entriesBuffer.get(i);
            class_2561 fullLine = getFormattedName(scoreboard, entry.comp_2127());
            this.formattedNamesBuffer.add(fullLine);
            float width = mc.field_1772.method_27525(fullLine);
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        Objects.requireNonNull(mc.field_1772);
        totalWidth = maxWidth + (internalGap * 2.0f);
        totalHeight = 9.0f + 0.0f + (this.entriesBuffer.size() * (9.0f + 0.0f)) + (internalGap * 2.0f);
        getDraggable().setWidth(totalWidth);
        getDraggable().setHeight(totalHeight);
        clampToScreen();
        x = getDraggable().getX();
        y = getDraggable().getY();
        String mode = this.renderMode.getValue();
        switch (mode) {
            case "Blur":
                RenderUtil.BLUR_RECT.draw(matrixStack, x, y, totalWidth, totalHeight, 4.0f, UIColors.widgetBlur());
                break;
            case "Default":
                RenderUtil.RECT.draw(matrixStack, x, y, totalWidth, totalHeight, 4.0f, UIColors.widgetBlur());
                break;
            case "Gradient":
                RenderUtil.GRADIENT_RECT.draw(matrixStack, x, y, totalWidth, totalHeight, 4.0f, UIColors.primary(), UIColors.primary(), UIColors.secondary(), UIColors.secondary());
                break;
            case "Texture":
                RenderUtil.TEXTURE_RECT.draw(matrixStack, x, y, totalWidth, totalHeight, 4.0f, UIColors.widgetBlur(), 0.0f, 0.0f, 1.0f, 1.0f, 0);
                break;
        }
        float currentY = y + internalGap;
        float titleX = x + ((totalWidth - titleWidth) / 2.0f);
        drawText(matrixStack, title, titleX, currentY);
        float currentY2 = currentY + 9.0f + 0.0f;
        int n2 = this.formattedNamesBuffer.size();
        for (int i2 = 0; i2 < n2; i2++) {
            class_2561 lineText = this.formattedNamesBuffer.get(i2);
            drawText(matrixStack, lineText, x + internalGap, currentY2);
            currentY2 += 9.0f + 0.0f;
        }
    }

    private void clampToScreen() {
        float screenWidth = mc.method_22683().method_4486();
        float screenHeight = mc.method_22683().method_4502();
        float x = getDraggable().getX();
        float y = getDraggable().getY();
        float width = getDraggable().getWidth();
        float height = getDraggable().getHeight();
        if (x < 3.0f) {
            x = 3.0f;
        }
        if (y < 3.0f) {
            y = 3.0f;
        }
        if (x + width > screenWidth - 3.0f) {
            x = (screenWidth - width) - 3.0f;
        }
        if (y + height > screenHeight - 3.0f) {
            y = (screenHeight - height) - 3.0f;
        }
        getDraggable().setX(x);
        getDraggable().setY(y);
    }

    private class_2561 getFormattedName(class_269 scoreboard, String owner) {
        class_268 team = scoreboard.method_1164(owner);
        if (team != null) {
            return team.method_1198(class_2561.method_43470(owner));
        }
        return class_2561.method_43470(owner);
    }

    private void drawText(class_4587 matrixStack, class_2561 text, float x, float y) {
        mc.field_1772.method_27522(text, (int) x, (int) y, -1, true, matrixStack.method_23760().method_23761(), mc.method_22940().method_23000(), class_327.class_6415.field_33993, 0, 15728880);
    }
}
