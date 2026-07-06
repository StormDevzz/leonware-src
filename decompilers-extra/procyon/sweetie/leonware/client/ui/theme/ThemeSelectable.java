// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.theme;

import lombok.Generated;
import sweetie.leonware.api.utils.math.MouseUtil;
import java.awt.Color;
import net.minecraft.class_4587;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.UIColors;
import net.minecraft.class_332;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.client.ui.UIComponent;

public class ThemeSelectable extends UIComponent
{
    private final Theme theme;
    private final AnimationUtil hoverAnimation;
    private final AnimationUtil enableAnimation;
    
    public ThemeSelectable(final Theme theme) {
        this.hoverAnimation = new AnimationUtil();
        this.enableAnimation = new AnimationUtil();
        this.theme = theme;
    }
    
    @Override
    public void render(final class_332 context, final int mouseX, final int mouseY, final float delta) {
        final class_4587 matrixStack = context.method_51448();
        final boolean hovered = this.hovered((float)mouseX, (float)mouseY);
        this.enableAnimation.update();
        this.enableAnimation.run(UIColors.currentTheme().getName().equals(this.theme.getName()) ? 1.0 : 0.7, 200L, Easing.SINE_OUT);
        this.hoverAnimation.update();
        this.hoverAnimation.run(hovered ? 1.0 : 0.0, 500L, Easing.EXPO_OUT);
        final int fullAlpha = (int)(this.getAlpha() * this.enableAnimation.getValue() * 255.0);
        final Color primary = ColorUtil.setAlpha(this.theme.getPrimaryColor(), fullAlpha);
        final Color secondary = ColorUtil.setAlpha(this.theme.getSecondaryColor(), fullAlpha);
        final float round = this.getHeight() * 0.2f;
        final float fontSize = (float)(this.getHeight() * 0.4f + this.hoverAnimation.getValue());
        RenderUtil.GRADIENT_RECT.draw(matrixStack, this.getX(), this.getY(), this.getWidth(), this.getHeight(), round, primary, secondary, primary, secondary);
        Fonts.PS_BOLD.drawCenteredText(matrixStack, this.theme.getName(), this.getX() + this.getWidth() / 2.0f, this.getY() + this.getHeight() / 2.0f - fontSize / 2.0f, fontSize, ColorUtil.setAlpha(this.theme.getTextColor(), fullAlpha));
    }
    
    private boolean hovered(final float mouseX, final float mouseY) {
        return MouseUtil.isHovered(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
    
    @Override
    public void keyPressed(final int keyCode, final int scanCode, final int modifiers) {
    }
    
    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
    }
    
    @Override
    public void mouseReleased(final double mouseX, final double mouseY, final int button) {
    }
    
    @Override
    public void mouseScrolled(final double mouseX, final double mouseY, final double horizontalAmount, final double verticalAmount) {
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
