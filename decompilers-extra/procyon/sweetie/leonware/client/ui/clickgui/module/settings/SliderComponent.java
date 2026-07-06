// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.clickgui.module.settings;

import java.awt.Color;
import net.minecraft.class_4587;
import sweetie.leonware.api.utils.math.MouseUtil;
import org.joml.Vector4f;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.color.ColorUtil;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.animation.Easing;
import net.minecraft.class_332;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.client.ui.clickgui.module.SettingComponent;

public class SliderComponent extends SettingComponent
{
    private final SliderSetting setting;
    private boolean dragging;
    private float currentWidth;
    private float previewValue;
    private boolean inputActive;
    private String inputText;
    private long lastClickTime;
    private static final long DOUBLE_CLICK_MS = 350L;
    private final AnimationUtil dragAnimation;
    private final AnimationUtil inputAnimation;
    
    public SliderComponent(final SliderSetting setting) {
        super(setting);
        this.inputText = "";
        this.lastClickTime = 0L;
        this.dragAnimation = new AnimationUtil();
        this.inputAnimation = new AnimationUtil();
        this.setting = setting;
        this.previewValue = setting.getValue();
        this.updateHeight(this.getDefaultHeight());
    }
    
    @Override
    public void render(final class_332 context, final int mouseX, final int mouseY, final float delta) {
        this.inputAnimation.update();
        this.inputAnimation.run(this.inputActive ? 1.0 : 0.0, 300L, Easing.EXPO_OUT);
        final float inputAnim = (float)this.inputAnimation.getValue();
        final float totalHeight = this.getDefaultHeight() + inputAnim * this.inputFieldHeight();
        this.updateHeight(totalHeight);
        this.dragAnimation.update();
        this.dragAnimation.run(this.dragging ? 1.0 : 0.0, 500L, Easing.EXPO_OUT);
        final class_4587 matrixStack = context.method_51448();
        final float bigPenis = this.dragging ? this.previewValue : this.setting.getValue();
        final float fontSize = this.fontSize();
        String valueText = "" + MathUtil.round(bigPenis, this.setting.getStep());
        if (valueText.contains(".")) {
            valueText = String.valueOf(bigPenis);
        }
        final float valueWidth = Fonts.PS_MEDIUM.getWidth(valueText, fontSize);
        final float piska = this.scaled(0.5f);
        final int fullAlpha = (int)(this.getAlpha() * 255.0f);
        final float progress = (bigPenis - this.setting.getMin()) / (this.setting.getMax() - this.setting.getMin()) * this.sliderWidth();
        this.currentWidth = MathUtil.interpolate(this.currentWidth, progress, 0.2f);
        Fonts.PS_MEDIUM.drawText(matrixStack, this.setting.getName(), this.getX() + piska, this.getY() + piska, fontSize, UIColors.textColor(fullAlpha));
        Fonts.PS_MEDIUM.drawText(matrixStack, valueText, this.getX() - piska + this.getWidth() - valueWidth, this.getY() + piska, fontSize, UIColors.textColor(fullAlpha));
        final float sliderRound = this.sliderHeight() * 0.3f;
        final float knobX = class_3532.method_15363(this.sliderX() + this.currentWidth - this.knobSize() / 2.0f, this.sliderX(), this.sliderX() + this.sliderWidth() - this.knobSize());
        final Color knobColor = ColorUtil.setAlpha(ColorUtil.interpolate(UIColors.knob(), UIColors.inactiveKnob(), this.dragAnimation.getValue()), fullAlpha);
        final float hui = (this.knobSize() - this.sliderHeight()) / 2.0f;
        final Color color1 = UIColors.gradient(0, fullAlpha);
        final Color color2 = UIColors.gradient(90, fullAlpha);
        RenderUtil.BLUR_RECT.draw(matrixStack, this.sliderX(), this.sliderY(), this.sliderWidth(), this.sliderHeight(), sliderRound, UIColors.backgroundBlur(fullAlpha));
        RenderUtil.BLUR_RECT.draw(matrixStack, this.sliderX(), this.sliderY(), this.currentWidth, this.sliderHeight(), new Vector4f(sliderRound), color2, color1, color2, color1);
        RenderUtil.BLUR_RECT.draw(matrixStack, knobX, this.sliderY() - hui, this.knobSize(), this.knobSize(), this.knobSize() * 0.4f, knobColor);
        if (inputAnim > 0.0f) {
            final float ifY = this.sliderY() + this.sliderHeight() / 2.0f + this.knobSize() / 2.0f + this.scaled(1.0f);
            final float ifH = this.inputFieldHeight() * inputAnim;
            final float ifRound = ifH * 0.25f;
            final int ifAlpha = (int)(this.getAlpha() * inputAnim * 255.0f);
            RenderUtil.BLUR_RECT.draw(matrixStack, this.getX(), ifY, this.getWidth(), ifH, ifRound, UIColors.widgetBlur(ifAlpha));
            final String cursor = (this.inputActive && System.currentTimeMillis() % 900L > 450L) ? "|" : "";
            String displayText = this.inputText.isEmpty() ? "" : (this.inputText + cursor);
            if (displayText.isEmpty() && this.inputActive) {
                displayText = cursor;
            }
            final float ifFontSize = this.fontSize() * 1.1f;
            final Color ifTextColor = UIColors.textColor(ifAlpha);
            Fonts.PS_MEDIUM.drawCenteredText(matrixStack, displayText, this.getX() + this.getWidth() / 2.0f, ifY + ifH / 2.0f - ifFontSize / 2.0f, ifFontSize, ifTextColor);
        }
        this.setHeight(this.getDefaultHeight() + inputAnim * this.inputFieldHeight());
        if (this.dragging) {
            float newValue = (mouseX - this.getX()) / this.sliderWidth();
            newValue = this.setting.getMin() + newValue * (this.setting.getMax() - this.setting.getMin());
            newValue = Math.round(newValue / this.setting.getStep()) * this.setting.getStep();
            this.previewValue = MathUtil.round(Math.max(this.setting.getMin(), Math.min(this.setting.getMax(), newValue)), this.setting.getStep());
        }
        if (this.dragging && !MouseUtil.isHovered((float)mouseX, (float)mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight())) {
            this.setting.setValue(this.previewValue);
            this.dragging = false;
        }
    }
    
    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        final float fontSize = this.fontSize();
        final float bigPenis = this.setting.getValue();
        final String valueText = String.valueOf(bigPenis);
        final float valueWidth = Fonts.PS_MEDIUM.getWidth(valueText, fontSize);
        final float piska = this.scaled(0.5f);
        final float valueX = this.getX() - piska + this.getWidth() - valueWidth;
        final boolean onValue = MouseUtil.isHovered(mouseX, mouseY, valueX, this.getY(), valueWidth + piska * 2.0f, fontSize + piska * 2.0f);
        if (onValue && button == 1) {
            final long now = System.currentTimeMillis();
            if (!this.inputActive) {
                this.inputActive = true;
                this.inputText = "";
                this.lastClickTime = now;
            }
            else {
                this.inputActive = false;
                this.inputText = "";
            }
            return;
        }
        if (button == 0 && MouseUtil.isHovered(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getDefaultHeight()) && !onValue) {
            this.dragging = true;
            this.inputActive = false;
        }
    }
    
    @Override
    public void mouseReleased(final double mouseX, final double mouseY, final int button) {
        if (this.dragging) {
            this.setting.setValue(this.previewValue);
        }
        this.dragging = false;
    }
    
    @Override
    public void keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        if (!this.inputActive) {
            return;
        }
        switch (keyCode) {
            case 256: {
                this.inputActive = false;
                this.inputText = "";
                break;
            }
            case 257:
            case 335: {
                this.applyInput();
                break;
            }
            case 259: {
                if (!this.inputText.isEmpty()) {
                    this.inputText = this.inputText.substring(0, this.inputText.length() - 1);
                    break;
                }
                break;
            }
        }
    }
    
    public boolean charTyped(final char chr, final int modifiers) {
        if (!this.inputActive) {
            return false;
        }
        if ((chr < '0' || chr > '9') && chr != '.' && chr != '-') {
            return false;
        }
        if (chr == '.' && this.inputText.contains(".")) {
            return true;
        }
        if (chr == '-' && !this.inputText.isEmpty()) {
            return true;
        }
        this.inputText += chr;
        return true;
    }
    
    private void applyInput() {
        try {
            float val = Float.parseFloat(this.inputText);
            val = Math.round(val / this.setting.getStep()) * this.setting.getStep();
            val = class_3532.method_15363(val, this.setting.getMin(), this.setting.getMax());
            this.setting.setValue(MathUtil.round(val, this.setting.getStep()));
        }
        catch (final NumberFormatException ex) {}
        this.inputActive = false;
        this.inputText = "";
    }
    
    private float fontSize() {
        return this.scaled(15.0f) * 0.45f;
    }
    
    private float sliderWidth() {
        return this.getWidth();
    }
    
    private float sliderHeight() {
        return this.scaled(3.5f);
    }
    
    private float knobSize() {
        return this.sliderHeight() * 1.5f;
    }
    
    private float sliderY() {
        return this.getY() + this.fontSize() + this.knobSize() / 2.0f;
    }
    
    private float sliderX() {
        return this.getX();
    }
    
    private float getDefaultHeight() {
        return this.fontSize() + this.gap() + this.knobSize();
    }
    
    private float inputFieldHeight() {
        return this.scaled(14.0f);
    }
    
    @Override
    public void mouseScrolled(final double mouseX, final double mouseY, final double horizontalAmount, final double verticalAmount) {
    }
}
