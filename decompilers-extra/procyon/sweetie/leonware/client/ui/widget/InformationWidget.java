// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.widget;

import sweetie.leonware.api.utils.render.fonts.Icons;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import net.minecraft.class_4587;

public abstract class InformationWidget extends Widget
{
    public InformationWidget(final float x, final float y) {
        super(x, y);
    }
    
    @Override
    public void render(final class_4587 matrixStack) {
        final float x = this.getDraggable().getX();
        final float y = this.getDraggable().getY();
        final String valueText = " " + this.getValue();
        final float fontSize = this.scaled(7.5f);
        final float nameWidth = this.getSemiBoldFont().getWidth(this.getName(), fontSize);
        final float valueWidth = this.getSemiBoldFont().getWidth(valueText, fontSize);
        final float backgroundWidth = nameWidth + valueWidth + this.getGap() * 2.0f;
        final float backgroundHeight = fontSize + this.getGap() * 2.0f;
        final float round = backgroundHeight * 0.3f;
        final float textX = x + this.getGap();
        final float textY = y + this.getGap();
        RenderUtil.BLUR_RECT.draw(matrixStack, x, y, backgroundWidth, backgroundHeight, round, UIColors.widgetBlur());
        this.getSemiBoldFont().drawGradientText(matrixStack, this.getName(), textX, textY, fontSize, UIColors.primary(), UIColors.secondary(), nameWidth / 4.0f);
        this.getSemiBoldFont().drawText(matrixStack, valueText, textX + nameWidth, textY, fontSize, UIColors.textColor());
        this.getDraggable().setWidth(backgroundWidth);
        this.getDraggable().setHeight(backgroundHeight);
    }
    
    public abstract String getValue();
    
    public abstract Icons getIcon();
}
