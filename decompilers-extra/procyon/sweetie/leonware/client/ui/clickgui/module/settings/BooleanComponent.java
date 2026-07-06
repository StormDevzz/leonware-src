// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.clickgui.module.settings;

import sweetie.leonware.api.utils.math.MouseUtil;
import net.minecraft.class_4587;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.color.ColorUtil;
import java.time.Duration;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.api.utils.animation.Easing;
import net.minecraft.class_332;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.module.setting.Setting;
import java.awt.Color;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.client.ui.clickgui.module.SettingComponent;

public class BooleanComponent extends SettingComponent
{
    private final BooleanSetting setting;
    private final AnimationUtil toggleAnimation;
    private final boolean inMenu;
    private Color color;
    
    public BooleanComponent(final BooleanSetting setting) {
        this(setting, false);
    }
    
    public BooleanComponent(final BooleanSetting setting, final boolean inMenu) {
        super(setting);
        this.toggleAnimation = new AnimationUtil();
        this.setting = setting;
        this.updateHeight(15.0f);
        this.toggleAnimation.setValue(((boolean)setting.getValue()) ? 1.0 : 0.0);
        this.inMenu = inMenu;
        this.color = (inMenu ? UIColors.widgetBlur() : UIColors.backgroundBlur());
    }
    
    @Override
    public void render(final class_332 context, final int mouseX, final int mouseY, final float delta) {
        this.updateHeight(15.0f);
        this.color = (this.inMenu ? UIColors.widgetBlur() : UIColors.backgroundBlur());
        this.toggleAnimation.update();
        this.toggleAnimation.run(this.setting.getValue() ? 1.0 : 0.0, 100L, Easing.SINE_OUT);
        final class_4587 matrixStack = context.method_51448();
        final float fontSize = this.getHeight() * 0.45f;
        final int fullAlpha = (int)(this.getAlpha() * 255.0f);
        final float anim = (float)this.toggleAnimation.getValue();
        final float checkHeight = this.getHeight() * 0.67f;
        final float checkWidth = checkHeight * 1.9f;
        final float checkX = this.getX() + this.getWidth() - checkWidth;
        final float checkY = this.getY() + this.getHeight() / 2.0f - checkHeight / 2.0f;
        final float checkRound = checkHeight * 0.4f;
        final float baseKnob = checkHeight * 0.8f;
        final float knobGap = (checkHeight - baseKnob) * 0.8f;
        final float knobSize = baseKnob - knobGap;
        final float knobPenis = knobGap * 1.3f;
        final float knobY = this.getY() + this.getHeight() / 2.0f - knobSize / 2.0f;
        final float knobX = checkX + knobPenis + (checkWidth - knobSize - knobPenis * 2.0f) * anim;
        final float knobRound = knobSize * 0.4f;
        Fonts.PS_MEDIUM.drawWrap(matrixStack, this.setting.getName(), this.getX(), this.getY() + this.getHeight() / 2.0f - fontSize / 2.0f, this.getWidth() - checkWidth - knobGap, fontSize, UIColors.textColor(fullAlpha), this.scaled(16.0f), Duration.ofMillis(3000L), Duration.ofMillis(500L));
        final Color knobColor = ColorUtil.interpolate(UIColors.knob(fullAlpha), UIColors.inactiveKnob(fullAlpha), anim);
        RenderUtil.BLUR_RECT.draw(matrixStack, checkX, checkY, checkWidth, checkHeight, checkRound, ColorUtil.setAlpha(this.color, fullAlpha));
        RenderUtil.BLUR_RECT.draw(matrixStack, knobX, knobY, knobSize, knobSize, knobRound, knobColor);
    }
    
    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (MouseUtil.isHovered(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight())) {
            this.setting.toggle();
        }
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
