/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_332
 *  net.minecraft.class_4587
 */
package sweetie.leonware.client.ui.clickgui.module.settings;

import java.awt.Color;
import java.time.Duration;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.client.ui.clickgui.module.SettingComponent;

public class BooleanComponent
extends SettingComponent {
    private final BooleanSetting setting;
    private final AnimationUtil toggleAnimation = new AnimationUtil();
    private final boolean inMenu;
    private Color color;

    public BooleanComponent(BooleanSetting setting) {
        this(setting, false);
    }

    public BooleanComponent(BooleanSetting setting, boolean inMenu) {
        super(setting);
        this.setting = setting;
        this.updateHeight(15.0f);
        this.toggleAnimation.setValue((Boolean)setting.getValue() != false ? 1.0 : 0.0);
        this.inMenu = inMenu;
        this.color = inMenu ? UIColors.widgetBlur() : UIColors.backgroundBlur();
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        this.updateHeight(15.0f);
        this.color = this.inMenu ? UIColors.widgetBlur() : UIColors.backgroundBlur();
        this.toggleAnimation.update();
        this.toggleAnimation.run((Boolean)this.setting.getValue() != false ? 1.0 : 0.0, 100L, Easing.SINE_OUT);
        class_4587 matrixStack = context.method_51448();
        float fontSize = this.getHeight() * 0.45f;
        int fullAlpha = (int)(this.getAlpha() * 255.0f);
        float anim = (float)this.toggleAnimation.getValue();
        float checkHeight = this.getHeight() * 0.67f;
        float checkWidth = checkHeight * 1.9f;
        float checkX = this.getX() + this.getWidth() - checkWidth;
        float checkY = this.getY() + this.getHeight() / 2.0f - checkHeight / 2.0f;
        float checkRound = checkHeight * 0.4f;
        float baseKnob = checkHeight * 0.8f;
        float knobGap = (checkHeight - baseKnob) * 0.8f;
        float knobSize = baseKnob - knobGap;
        float knobPenis = knobGap * 1.3f;
        float knobY = this.getY() + this.getHeight() / 2.0f - knobSize / 2.0f;
        float knobX = checkX + knobPenis + (checkWidth - knobSize - knobPenis * 2.0f) * anim;
        float knobRound = knobSize * 0.4f;
        Fonts.PS_MEDIUM.drawWrap(matrixStack, this.setting.getName(), this.getX(), this.getY() + this.getHeight() / 2.0f - fontSize / 2.0f, this.getWidth() - checkWidth - knobGap, fontSize, UIColors.textColor(fullAlpha), this.scaled(16.0f), Duration.ofMillis(3000L), Duration.ofMillis(500L));
        Color knobColor = ColorUtil.interpolate(UIColors.knob(fullAlpha), UIColors.inactiveKnob(fullAlpha), anim);
        RenderUtil.BLUR_RECT.draw(matrixStack, checkX, checkY, checkWidth, checkHeight, checkRound, ColorUtil.setAlpha(this.color, fullAlpha));
        RenderUtil.BLUR_RECT.draw(matrixStack, knobX, knobY, knobSize, knobSize, knobRound, knobColor);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (MouseUtil.isHovered(mouseX, mouseY, (double)this.getX(), (double)this.getY(), (double)this.getWidth(), (double)this.getHeight())) {
            this.setting.toggle();
        }
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

