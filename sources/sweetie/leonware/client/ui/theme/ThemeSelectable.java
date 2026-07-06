package sweetie.leonware.client.ui.theme;

import java.awt.Color;
import lombok.Generated;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.client.ui.UIComponent;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/theme/ThemeSelectable.class */
public class ThemeSelectable extends UIComponent {
    private final Theme theme;
    private final AnimationUtil hoverAnimation = new AnimationUtil();
    private final AnimationUtil enableAnimation = new AnimationUtil();

    @Generated
    public Theme getTheme() {
        return this.theme;
    }

    @Generated
    public AnimationUtil getHoverAnimation() {
        return this.hoverAnimation;
    }

    @Generated
    public AnimationUtil getEnableAnimation() {
        return this.enableAnimation;
    }

    public ThemeSelectable(Theme theme) {
        this.theme = theme;
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        class_4587 matrixStack = context.method_51448();
        boolean hovered = hovered(mouseX, mouseY);
        this.enableAnimation.update();
        this.enableAnimation.run(UIColors.currentTheme().getName().equals(this.theme.getName()) ? 1.0d : 0.7d, 200L, Easing.SINE_OUT);
        this.hoverAnimation.update();
        this.hoverAnimation.run(hovered ? 1.0d : 0.0d, 500L, Easing.EXPO_OUT);
        int fullAlpha = (int) (((double) getAlpha()) * this.enableAnimation.getValue() * 255.0d);
        Color primary = ColorUtil.setAlpha(this.theme.getPrimaryColor(), fullAlpha);
        Color secondary = ColorUtil.setAlpha(this.theme.getSecondaryColor(), fullAlpha);
        float round = getHeight() * 0.2f;
        float fontSize = (float) (((double) (getHeight() * 0.4f)) + this.hoverAnimation.getValue());
        RenderUtil.GRADIENT_RECT.draw(matrixStack, getX(), getY(), getWidth(), getHeight(), round, primary, secondary, primary, secondary);
        Fonts.PS_BOLD.drawCenteredText(matrixStack, this.theme.getName(), getX() + (getWidth() / 2.0f), (getY() + (getHeight() / 2.0f)) - (fontSize / 2.0f), fontSize, ColorUtil.setAlpha(this.theme.getTextColor(), fullAlpha));
    }

    private boolean hovered(float mouseX, float mouseY) {
        return MouseUtil.isHovered(mouseX, mouseY, getX(), getY(), getWidth(), getHeight());
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseClicked(double mouseX, double mouseY, int button) {
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseReleased(double mouseX, double mouseY, int button) {
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    }
}
