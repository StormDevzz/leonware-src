// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.clickgui.module.settings;

import sweetie.leonware.api.utils.math.MouseUtil;
import java.awt.Color;
import net.minecraft.class_4587;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import org.joml.Vector4f;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.animation.Easing;
import net.minecraft.class_332;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.module.setting.RunSetting;
import sweetie.leonware.client.ui.clickgui.module.SettingComponent;

public class ButtonComponent extends SettingComponent
{
    private final RunSetting setting;
    private final AnimationUtil hoverAnimation;
    
    public ButtonComponent(final RunSetting setting) {
        super(setting);
        this.hoverAnimation = new AnimationUtil();
        this.setting = setting;
        this.updateHeight(this.getDefaultHeight());
    }
    
    @Override
    public void render(final class_332 context, final int mouseX, final int mouseY, final float delta) {
        this.updateHeight(this.getDefaultHeight());
        final class_4587 matrixStack = context.method_51448();
        final int fullAlpha = (int)(this.getAlpha() * 255.0f);
        this.hoverAnimation.update();
        this.hoverAnimation.run(this.hovered(mouseX, mouseY) ? 1.0 : 0.0, 500L, Easing.EXPO_OUT);
        final Color buttonColor1 = ColorUtil.interpolate(UIColors.gradient(0, fullAlpha), UIColors.backgroundBlur(fullAlpha), this.hoverAnimation.getValue());
        final Color buttonColor2 = ColorUtil.interpolate(UIColors.gradient(90, fullAlpha), UIColors.backgroundBlur(fullAlpha), this.hoverAnimation.getValue());
        final float fontSize = this.getHeight() * 0.45f + this.scaled((float)this.hoverAnimation.getValue());
        final float round = this.getWidth() * 0.04f;
        RenderUtil.BLUR_RECT.draw(matrixStack, this.getX(), this.getY(), this.getWidth(), this.getHeight(), new Vector4f(round), buttonColor1, buttonColor2, buttonColor1, buttonColor2);
        Fonts.PS_MEDIUM.drawCenteredText(matrixStack, this.setting.getName(), this.getX() + this.getWidth() / 2.0f, this.getY() + this.getHeight() / 2.0f - fontSize / 2.0f, fontSize, ColorUtil.setAlpha(UIColors.textColor(), fullAlpha));
    }
    
    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (this.hovered(mouseX, mouseY) && this.setting.getValue() != null) {
            this.setting.getValue().run();
        }
    }
    
    private boolean hovered(final double mouseX, final double mouseY) {
        return MouseUtil.isHovered(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.scaled(this.getDefaultHeight()));
    }
    
    private float getDefaultHeight() {
        return 15.0f;
    }
    
    @Override
    public void keyPressed(final int keyCode, final int scanCode, final int modifiers) {
    }
    
    @Override
    public void mouseReleased(final double mouseX, final double mouseY, final int button) {
    }
    
    @Override
    public void mouseScrolled(final double mouseX, final double mouseY, final double horizontalAmount, final double verticalAmount) {
    }
}
