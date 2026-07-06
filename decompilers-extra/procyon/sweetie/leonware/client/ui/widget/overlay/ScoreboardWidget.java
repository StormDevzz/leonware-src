// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.widget.overlay;

import net.minecraft.class_4597;
import net.minecraft.class_327;
import net.minecraft.class_268;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.class_266;
import net.minecraft.class_269;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import java.util.Objects;
import net.minecraft.class_5348;
import net.minecraft.class_8646;
import net.minecraft.class_4587;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.class_2561;
import net.minecraft.class_9011;
import java.util.List;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.client.ui.widget.Widget;

public class ScoreboardWidget extends Widget
{
    public final ModeSetting renderMode;
    private final List<class_9011> entriesBuffer;
    private final List<class_2561> formattedNamesBuffer;
    private static final Comparator<class_9011> ENTRY_COMPARATOR;
    
    public ScoreboardWidget() {
        super(3.0f, 50.0f);
        this.renderMode = new ModeSetting("Scoreboard").values("Blur", "Default", "Gradient", "Texture").value("Blur");
        this.entriesBuffer = new ArrayList<class_9011>();
        this.formattedNamesBuffer = new ArrayList<class_2561>();
    }
    
    @Override
    public String getName() {
        return "Scoreboard";
    }
    
    @Override
    public void render(final class_4587 matrixStack) {
        if (ScoreboardWidget.mc.field_1687 == null) {
            return;
        }
        final class_269 scoreboard = ScoreboardWidget.mc.field_1687.method_8428();
        final class_266 objective = scoreboard.method_1189(class_8646.field_45157);
        if (objective == null) {
            return;
        }
        final class_2561 title = objective.method_1114();
        final Collection<class_9011> entries = scoreboard.method_1184(objective);
        this.entriesBuffer.clear();
        for (final class_9011 score : entries) {
            if (score.comp_2127() != null && !score.comp_2127().startsWith("#")) {
                this.entriesBuffer.add(score);
            }
        }
        this.entriesBuffer.sort(ScoreboardWidget.ENTRY_COMPARATOR);
        while (this.entriesBuffer.size() > 15) {
            this.entriesBuffer.remove(this.entriesBuffer.size() - 1);
        }
        if (this.entriesBuffer.isEmpty() && title.getString().isEmpty()) {
            return;
        }
        final float gap = this.getGap();
        final float internalGap = gap * 0.8f;
        float maxWidth;
        final float titleWidth = maxWidth = (float)ScoreboardWidget.mc.field_1772.method_27525((class_5348)title);
        this.formattedNamesBuffer.clear();
        for (int i = 0, n = this.entriesBuffer.size(); i < n; ++i) {
            final class_9011 entry = this.entriesBuffer.get(i);
            final class_2561 fullLine = this.getFormattedName(scoreboard, entry.comp_2127());
            this.formattedNamesBuffer.add(fullLine);
            final float width = (float)ScoreboardWidget.mc.field_1772.method_27525((class_5348)fullLine);
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        Objects.requireNonNull(ScoreboardWidget.mc.field_1772);
        final float fontHeight = 9.0f;
        final float lineSpacing = 0.0f;
        final float titleSpacing = 0.0f;
        final float totalWidth = maxWidth + internalGap * 2.0f;
        final float totalHeight = fontHeight + titleSpacing + this.entriesBuffer.size() * (fontHeight + lineSpacing) + internalGap * 2.0f;
        this.getDraggable().setWidth(totalWidth);
        this.getDraggable().setHeight(totalHeight);
        this.clampToScreen();
        final float x = this.getDraggable().getX();
        final float y = this.getDraggable().getY();
        final String s;
        final String mode = s = this.renderMode.getValue();
        switch (s) {
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
                break;
            }
        }
        float currentY = y + internalGap;
        final float titleX = x + (totalWidth - titleWidth) / 2.0f;
        this.drawText(matrixStack, title, titleX, currentY);
        currentY += fontHeight + titleSpacing;
        for (int j = 0, n2 = this.formattedNamesBuffer.size(); j < n2; ++j) {
            final class_2561 lineText = this.formattedNamesBuffer.get(j);
            this.drawText(matrixStack, lineText, x + internalGap, currentY);
            currentY += fontHeight + lineSpacing;
        }
    }
    
    private void clampToScreen() {
        final float margin = 3.0f;
        final float screenWidth = (float)ScoreboardWidget.mc.method_22683().method_4486();
        final float screenHeight = (float)ScoreboardWidget.mc.method_22683().method_4502();
        float x = this.getDraggable().getX();
        float y = this.getDraggable().getY();
        final float width = this.getDraggable().getWidth();
        final float height = this.getDraggable().getHeight();
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
    
    private class_2561 getFormattedName(final class_269 scoreboard, final String owner) {
        final class_268 team = scoreboard.method_1164(owner);
        if (team != null) {
            return (class_2561)team.method_1198((class_2561)class_2561.method_43470(owner));
        }
        return (class_2561)class_2561.method_43470(owner);
    }
    
    private void drawText(final class_4587 matrixStack, final class_2561 text, final float x, final float y) {
        ScoreboardWidget.mc.field_1772.method_27522(text, (float)(int)x, (float)(int)y, -1, true, matrixStack.method_23760().method_23761(), (class_4597)ScoreboardWidget.mc.method_22940().method_23000(), class_327.class_6415.field_33993, 0, 15728880);
    }
    
    static {
        ENTRY_COMPARATOR = ((a, b) -> Integer.compare(b.comp_2128(), a.comp_2128()));
    }
}
