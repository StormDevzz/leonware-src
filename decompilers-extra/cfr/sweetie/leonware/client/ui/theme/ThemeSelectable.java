/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_332
 *  net.minecraft.class_4587
 */
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
import sweetie.leonware.client.ui.theme.Theme;

public class ThemeSelectable
extends UIComponent {
    private final Theme theme;
    private final AnimationUtil hoverAnimation = new AnimationUtil();
    private final AnimationUtil enableAnimation = new AnimationUtil();

    public ThemeSelectable(Theme theme) {
        this.theme = theme;
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        class_4587 matrixStack = context.method_51448();
        boolean hovered = this.hovered(mouseX, mouseY);
        this.enableAnimation.update();
        this.enableAnimation.run(UIColors.currentTheme().getName().equals(this.theme.getName()) ? 1.0 : 0.7, 200L, Easing.SINE_OUT);
        this.hoverAnimation.update();
        this.hoverAnimation.run(hovered ? 1.0 : 0.0, 500L, Easing.EXPO_OUT);
        int fullAlpha = (int)((double)this.getAlpha() * this.enableAnimation.getValue() * 255.0);
        Color primary = ColorUtil.setAlpha(this.theme.getPrimaryColor(), fullAlpha);
        Color secondary = ColorUtil.setAlpha(this.theme.getSecondaryColor(), fullAlpha);
        float round = this.getHeight() * 0.2f;
        float fontSize = (float)((double)(this.getHeight() * 0.4f) + this.hoverAnimation.getValue());
        RenderUtil.GRADIENT_RECT.draw(matrixStack, this.getX(), this.getY(), this.getWidth(), this.getHeight(), round, primary, secondary, primary, secondary);
        Fonts.PS_BOLD.drawCenteredText(matrixStack, this.theme.getName(), this.getX() + this.getWidth() / 2.0f, this.getY() + this.getHeight() / 2.0f - fontSize / 2.0f, fontSize, ColorUtil.setAlpha(this.theme.getTextColor(), fullAlpha));
    }

    private boolean hovered(float mouseX, float mouseY) {
        return MouseUtil.isHovered(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    }

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
}

