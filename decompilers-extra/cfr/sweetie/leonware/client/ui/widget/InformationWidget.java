/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_4587
 */
package sweetie.leonware.client.ui.widget;

import net.minecraft.class_4587;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Icons;
import sweetie.leonware.client.ui.widget.Widget;

public abstract class InformationWidget
extends Widget {
    public InformationWidget(float x, float y) {
        super(x, y);
    }

    @Override
    public void render(class_4587 matrixStack) {
        float x = this.getDraggable().getX();
        float y = this.getDraggable().getY();
        String valueText = " " + this.getValue();
        float fontSize = this.scaled(7.5f);
        float nameWidth = this.getSemiBoldFont().getWidth(this.getName(), fontSize);
        float valueWidth = this.getSemiBoldFont().getWidth(valueText, fontSize);
        float backgroundWidth = nameWidth + valueWidth + this.getGap() * 2.0f;
        float backgroundHeight = fontSize + this.getGap() * 2.0f;
        float round = backgroundHeight * 0.3f;
        float textX = x + this.getGap();
        float textY = y + this.getGap();
        RenderUtil.BLUR_RECT.draw(matrixStack, x, y, backgroundWidth, backgroundHeight, round, UIColors.widgetBlur());
        this.getSemiBoldFont().drawGradientText(matrixStack, this.getName(), textX, textY, fontSize, UIColors.primary(), UIColors.secondary(), nameWidth / 4.0f);
        this.getSemiBoldFont().drawText(matrixStack, valueText, textX + nameWidth, textY, fontSize, UIColors.textColor());
        this.getDraggable().setWidth(backgroundWidth);
        this.getDraggable().setHeight(backgroundHeight);
    }

    public abstract String getValue();

    public abstract Icons getIcon();
}

