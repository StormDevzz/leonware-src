/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_266
 *  net.minecraft.class_268
 *  net.minecraft.class_269
 *  net.minecraft.class_327$class_6415
 *  net.minecraft.class_4587
 *  net.minecraft.class_4597
 *  net.minecraft.class_5348
 *  net.minecraft.class_8646
 *  net.minecraft.class_9011
 */
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
import net.minecraft.class_4597;
import net.minecraft.class_5348;
import net.minecraft.class_8646;
import net.minecraft.class_9011;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.client.ui.widget.Widget;

public class ScoreboardWidget
extends Widget {
    public final ModeSetting renderMode = new ModeSetting("Scoreboard").values("Blur", "Default", "Gradient", "Texture").value("Blur");
    private final List<class_9011> entriesBuffer = new ArrayList<class_9011>();
    private final List<class_2561> formattedNamesBuffer = new ArrayList<class_2561>();
    private static final Comparator<class_9011> ENTRY_COMPARATOR = (a, b) -> Integer.compare(b.comp_2128(), a.comp_2128());

    public ScoreboardWidget() {
        super(3.0f, 50.0f);
    }

    @Override
    public String getName() {
        return "Scoreboard";
    }

    @Override
    public void render(class_4587 matrixStack) {
        String mode;
        float titleWidth;
        if (ScoreboardWidget.mc.field_1687 == null) {
            return;
        }
        class_269 scoreboard = ScoreboardWidget.mc.field_1687.method_8428();
        class_266 objective = scoreboard.method_1189(class_8646.field_45157);
        if (objective == null) {
            return;
        }
        class_2561 title = objective.method_1114();
        Collection entries = scoreboard.method_1184(objective);
        this.entriesBuffer.clear();
        for (class_9011 score : entries) {
            if (score.comp_2127() == null || score.comp_2127().startsWith("#")) continue;
            this.entriesBuffer.add(score);
        }
        this.entriesBuffer.sort(ENTRY_COMPARATOR);
        while (this.entriesBuffer.size() > 15) {
            this.entriesBuffer.remove(this.entriesBuffer.size() - 1);
        }
        if (this.entriesBuffer.isEmpty() && title.getString().isEmpty()) {
            return;
        }
        float gap = this.getGap();
        float internalGap = gap * 0.8f;
        float maxWidth = titleWidth = (float)ScoreboardWidget.mc.field_1772.method_27525((class_5348)title);
        this.formattedNamesBuffer.clear();
        int n = this.entriesBuffer.size();
        for (int i = 0; i < n; ++i) {
            class_9011 entry = this.entriesBuffer.get(i);
            class_2561 fullLine = this.getFormattedName(scoreboard, entry.comp_2127());
            this.formattedNamesBuffer.add(fullLine);
            float width = ScoreboardWidget.mc.field_1772.method_27525((class_5348)fullLine);
            if (!(width > maxWidth)) continue;
            maxWidth = width;
        }
        Objects.requireNonNull(ScoreboardWidget.mc.field_1772);
        float fontHeight = 9.0f;
        float lineSpacing = 0.0f;
        float titleSpacing = 0.0f;
        float totalWidth = maxWidth + internalGap * 2.0f;
        float totalHeight = fontHeight + titleSpacing + (float)this.entriesBuffer.size() * (fontHeight + lineSpacing) + internalGap * 2.0f;
        this.getDraggable().setWidth(totalWidth);
        this.getDraggable().setHeight(totalHeight);
        this.clampToScreen();
        float x = this.getDraggable().getX();
        float y = this.getDraggable().getY();
        switch (mode = (String)this.renderMode.getValue()) {
            case "Blur": {
                RenderUtil.BLUR_RECT.draw(matrixStack, x, y, totalWidth, totalHeight, 4.0f, UIColors.widgetBlur());
                break;
            }
            case "Default": {
                RenderUtil.RECT.draw(matrixStack, x, y, totalWidth, totalHeight, 4.0f, UIColors.widgetBlur());
                break;
            }
            case "Gradient": {
                RenderUtil.GRADIENT_RECT.draw(matrixStack, x, y, totalWidth, totalHeight, 4.0f, UIColors.primary(), UIColors.primary(), UIColors.secondary(), UIColors.secondary());
                break;
            }
            case "Texture": {
                RenderUtil.TEXTURE_RECT.draw(matrixStack, x, y, totalWidth, totalHeight, 4.0f, UIColors.widgetBlur(), 0.0f, 0.0f, 1.0f, 1.0f, 0);
            }
        }
        float currentY = y + internalGap;
        float titleX = x + (totalWidth - titleWidth) / 2.0f;
        this.drawText(matrixStack, title, titleX, currentY);
        currentY += fontHeight + titleSpacing;
        int n2 = this.formattedNamesBuffer.size();
        for (int i = 0; i < n2; ++i) {
            class_2561 lineText = this.formattedNamesBuffer.get(i);
            this.drawText(matrixStack, lineText, x + internalGap, currentY);
            currentY += fontHeight + lineSpacing;
        }
    }

    private void clampToScreen() {
        float margin = 3.0f;
        float screenWidth = mc.method_22683().method_4486();
        float screenHeight = mc.method_22683().method_4502();
        float x = this.getDraggable().getX();
        float y = this.getDraggable().getY();
        float width = this.getDraggable().getWidth();
        float height = this.getDraggable().getHeight();
        if (x < margin) {
            x = margin;
        }
        if (y < margin) {
            y = margin;
        }
        if (x + width > screenWidth - margin) {
            x = screenWidth - width - margin;
        }
        if (y + height > screenHeight - margin) {
            y = screenHeight - height - margin;
        }
        this.getDraggable().setX(x);
        this.getDraggable().setY(y);
    }

    private class_2561 getFormattedName(class_269 scoreboard, String owner) {
        class_268 team = scoreboard.method_1164(owner);
        if (team != null) {
            return team.method_1198((class_2561)class_2561.method_43470((String)owner));
        }
        return class_2561.method_43470((String)owner);
    }

    private void drawText(class_4587 matrixStack, class_2561 text, float x, float y) {
        ScoreboardWidget.mc.field_1772.method_27522(text, (float)((int)x), (float)((int)y), -1, true, matrixStack.method_23760().method_23761(), (class_4597)mc.method_22940().method_23000(), class_327.class_6415.field_33993, 0, 0xF000F0);
    }
}

