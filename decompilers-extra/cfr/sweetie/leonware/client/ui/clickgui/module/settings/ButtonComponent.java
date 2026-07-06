/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_332
 *  net.minecraft.class_4587
 *  org.joml.Vector4f
 */
package sweetie.leonware.client.ui.clickgui.module.settings;

import java.awt.Color;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import org.joml.Vector4f;
import sweetie.leonware.api.module.setting.RunSetting;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.client.ui.clickgui.module.SettingComponent;

public class ButtonComponent
extends SettingComponent {
    private final RunSetting setting;
    private final AnimationUtil hoverAnimation = new AnimationUtil();

    public ButtonComponent(RunSetting setting) {
        super(setting);
        this.setting = setting;
        this.updateHeight(this.getDefaultHeight());
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        this.updateHeight(this.getDefaultHeight());
        class_4587 matrixStack = context.method_51448();
        int fullAlpha = (int)(this.getAlpha() * 255.0f);
        this.hoverAnimation.update();
        this.hoverAnimation.run(this.hovered(mouseX, mouseY) ? 1.0 : 0.0, 500L, Easing.EXPO_OUT);
        Color buttonColor1 = ColorUtil.interpolate(UIColors.gradient(0, fullAlpha), UIColors.backgroundBlur(fullAlpha), this.hoverAnimation.getValue());
        Color buttonColor2 = ColorUtil.interpolate(UIColors.gradient(90, fullAlpha), UIColors.backgroundBlur(fullAlpha), this.hoverAnimation.getValue());
        float fontSize = this.getHeight() * 0.45f + this.scaled((float)this.hoverAnimation.getValue());
        float round = this.getWidth() * 0.04f;
        RenderUtil.BLUR_RECT.draw(matrixStack, this.getX(), this.getY(), this.getWidth(), this.getHeight(), new Vector4f(round), buttonColor1, buttonColor2, buttonColor1, buttonColor2);
        Fonts.PS_MEDIUM.drawCenteredText(matrixStack, this.setting.getName(), this.getX() + this.getWidth() / 2.0f, this.getY() + this.getHeight() / 2.0f - fontSize / 2.0f, fontSize, ColorUtil.setAlpha(UIColors.textColor(), fullAlpha));
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (this.hovered(mouseX, mouseY) && this.setting.getValue() != null) {
            ((Runnable)this.setting.getValue()).run();
        }
    }

    private boolean hovered(double mouseX, double mouseY) {
        return MouseUtil.isHovered(mouseX, mouseY, (double)this.getX(), (double)this.getY(), (double)this.getWidth(), (double)this.scaled(this.getDefaultHeight()));
    }

    private float getDefaultHeight() {
        return 15.0f;
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    }
}

