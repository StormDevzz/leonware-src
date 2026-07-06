/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1259$class_1260
 *  net.minecraft.class_337
 *  net.minecraft.class_345
 *  net.minecraft.class_4587
 */
package sweetie.leonware.client.ui.widget.overlay;

import java.awt.Color;
import java.util.Map;
import net.minecraft.class_1259;
import net.minecraft.class_337;
import net.minecraft.class_345;
import net.minecraft.class_4587;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.client.ui.widget.Widget;

public class BossBarWidget
extends Widget {
    private float widgetWidth = 0.0f;
    private float widgetHeight = 0.0f;

    public BossBarWidget() {
        super(3.0f, 50.0f);
    }

    @Override
    public String getName() {
        return "BossBar";
    }

    @Override
    public void render(class_4587 matrixStack) {
        float x = this.getDraggable().getX();
        float y = this.getDraggable().getY();
        float gap = this.getGap();
        if (BossBarWidget.mc.field_1724 == null || BossBarWidget.mc.field_1687 == null || BossBarWidget.mc.field_1705 == null) {
            this.getDraggable().setWidth(0.0f);
            this.getDraggable().setHeight(0.0f);
            return;
        }
        class_337 bossBarHud = BossBarWidget.mc.field_1705.method_1740();
        if (bossBarHud == null) {
            this.getDraggable().setWidth(0.0f);
            this.getDraggable().setHeight(0.0f);
            return;
        }
        Map bossBars = bossBarHud.field_2060;
        if (bossBars.isEmpty()) {
            this.getDraggable().setWidth(0.0f);
            this.getDraggable().setHeight(0.0f);
            return;
        }
        float currentY = y;
        float maxWidth = 0.0f;
        float totalHeight = 0.0f;
        for (class_345 bossBar : bossBars.values()) {
            float[] barDimensions = this.renderBossBar(matrixStack, x, currentY, bossBar);
            maxWidth = Math.max(maxWidth, barDimensions[2]);
            totalHeight += barDimensions[3] + gap;
            currentY += barDimensions[3] + gap;
        }
        if (totalHeight > 0.0f) {
            totalHeight -= gap;
        }
        this.widgetWidth = maxWidth;
        this.widgetHeight = totalHeight;
        this.getDraggable().setWidth(this.widgetWidth);
        this.getDraggable().setHeight(this.widgetHeight);
    }

    private float[] renderBossBar(class_4587 matrixStack, float x, float y, class_345 bossBar) {
        float fontSize = this.scaled(6.0f);
        float barHeight = this.scaled(6.0f);
        float barWidth = this.scaled(120.0f);
        float gap = this.getGap() * 0.8f;
        String name = bossBar.method_5414().getString();
        float progress = bossBar.method_5412();
        Color barColor = this.getBossBarColor(bossBar.method_5420());
        Color backgroundColor = new Color(12, 12, 18, 220);
        float textWidth = this.getMediumFont().getWidth(name, fontSize);
        float contentWidth = Math.max(barWidth + gap * 2.0f, textWidth + gap * 3.0f);
        float backgroundHeight = fontSize + barHeight + gap * 2.0f;
        float round = backgroundHeight * 0.3f;
        RenderUtil.BLUR_RECT.draw(matrixStack, x, y, contentWidth, backgroundHeight, round, backgroundColor);
        float textX = x + (contentWidth - textWidth) / 2.0f;
        float textY = y + gap;
        this.getMediumFont().drawText(matrixStack, name, textX, textY, fontSize, Color.WHITE);
        float barX = x + (contentWidth - barWidth) / 2.0f;
        float barY = y + fontSize + gap * 1.5f;
        float barRound = barHeight * 0.2f;
        RenderUtil.BLUR_RECT.draw(matrixStack, barX, barY, barWidth, barHeight, barRound, new Color(20, 20, 25, 180));
        float progressWidth = barWidth * progress;
        if (progressWidth > 0.0f) {
            RenderUtil.BLUR_RECT.draw(matrixStack, barX, barY, progressWidth, barHeight, barRound, barColor);
        }
        return new float[]{x, y, contentWidth, backgroundHeight};
    }

    private Color getBossBarColor(class_1259.class_1260 color) {
        switch (color) {
            case field_5788: {
                return new Color(255, 182, 193);
            }
            case field_5780: {
                return new Color(100, 149, 237);
            }
            case field_5784: {
                return new Color(255, 69, 69);
            }
            case field_5785: {
                return new Color(50, 205, 50);
            }
            case field_5782: {
                return new Color(255, 215, 0);
            }
            case field_5783: {
                return new Color(147, 112, 219);
            }
            case field_5786: {
                return new Color(255, 255, 255);
            }
        }
        return new Color(128, 128, 128);
    }
}

