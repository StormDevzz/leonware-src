package sweetie.leonware.client.ui.widget;

import net.minecraft.class_4587;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Icons;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/InformationWidget.class */
public abstract class InformationWidget extends Widget {
    public abstract String getValue();

    public abstract Icons getIcon();

    public InformationWidget(float x, float y) {
        super(x, y);
    }

    @Override // sweetie.leonware.api.system.interfaces.IRenderer
    public void render(class_4587 matrixStack) {
        float x = getDraggable().getX();
        float y = getDraggable().getY();
        String valueText = " " + getValue();
        float fontSize = scaled(7.5f);
        float nameWidth = getSemiBoldFont().getWidth(getName(), fontSize);
        float valueWidth = getSemiBoldFont().getWidth(valueText, fontSize);
        float backgroundWidth = nameWidth + valueWidth + (getGap() * 2.0f);
        float backgroundHeight = fontSize + (getGap() * 2.0f);
        float round = backgroundHeight * 0.3f;
        float textX = x + getGap();
        float textY = y + getGap();
        RenderUtil.BLUR_RECT.draw(matrixStack, x, y, backgroundWidth, backgroundHeight, round, UIColors.widgetBlur());
        getSemiBoldFont().drawGradientText(matrixStack, getName(), textX, textY, fontSize, UIColors.primary(), UIColors.secondary(), nameWidth / 4.0f);
        getSemiBoldFont().drawText(matrixStack, valueText, textX + nameWidth, textY, fontSize, UIColors.textColor());
        getDraggable().setWidth(backgroundWidth);
        getDraggable().setHeight(backgroundHeight);
    }
}
