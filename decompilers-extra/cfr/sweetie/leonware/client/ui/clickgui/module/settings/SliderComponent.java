/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_332
 *  net.minecraft.class_3532
 *  net.minecraft.class_4587
 *  org.joml.Vector4f
 */
package sweetie.leonware.client.ui.clickgui.module.settings;

import java.awt.Color;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import org.joml.Vector4f;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.client.ui.clickgui.module.SettingComponent;

public class SliderComponent
extends SettingComponent {
    private final SliderSetting setting;
    private boolean dragging;
    private float currentWidth;
    private float previewValue;
    private boolean inputActive;
    private String inputText = "";
    private long lastClickTime = 0L;
    private static final long DOUBLE_CLICK_MS = 350L;
    private final AnimationUtil dragAnimation = new AnimationUtil();
    private final AnimationUtil inputAnimation = new AnimationUtil();

    public SliderComponent(SliderSetting setting) {
        super(setting);
        this.setting = setting;
        this.previewValue = ((Float)setting.getValue()).floatValue();
        this.updateHeight(this.getDefaultHeight());
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        this.inputAnimation.update();
        this.inputAnimation.run(this.inputActive ? 1.0 : 0.0, 300L, Easing.EXPO_OUT);
        float inputAnim = (float)this.inputAnimation.getValue();
        float totalHeight = this.getDefaultHeight() + inputAnim * this.inputFieldHeight();
        this.updateHeight(totalHeight);
        this.dragAnimation.update();
        this.dragAnimation.run(this.dragging ? 1.0 : 0.0, 500L, Easing.EXPO_OUT);
        class_4587 matrixStack = context.method_51448();
        float bigPenis = this.dragging ? this.previewValue : ((Float)this.setting.getValue()).floatValue();
        float fontSize = this.fontSize();
        Object valueText = "" + MathUtil.round(bigPenis, this.setting.getStep());
        if (((String)valueText).contains(".")) {
            valueText = String.valueOf(bigPenis);
        }
        float valueWidth = Fonts.PS_MEDIUM.getWidth((String)valueText, fontSize);
        float piska = this.scaled(0.5f);
        int fullAlpha = (int)(this.getAlpha() * 255.0f);
        float progress = (bigPenis - this.setting.getMin()) / (this.setting.getMax() - this.setting.getMin()) * this.sliderWidth();
        this.currentWidth = MathUtil.interpolate(this.currentWidth, progress, 0.2f);
        Fonts.PS_MEDIUM.drawText(matrixStack, this.setting.getName(), this.getX() + piska, this.getY() + piska, fontSize, UIColors.textColor(fullAlpha));
        Fonts.PS_MEDIUM.drawText(matrixStack, (String)valueText, this.getX() - piska + this.getWidth() - valueWidth, this.getY() + piska, fontSize, UIColors.textColor(fullAlpha));
        float sliderRound = this.sliderHeight() * 0.3f;
        float knobX = class_3532.method_15363((float)(this.sliderX() + this.currentWidth - this.knobSize() / 2.0f), (float)this.sliderX(), (float)(this.sliderX() + this.sliderWidth() - this.knobSize()));
        Color knobColor = ColorUtil.setAlpha(ColorUtil.interpolate(UIColors.knob(), UIColors.inactiveKnob(), this.dragAnimation.getValue()), fullAlpha);
        float hui = (this.knobSize() - this.sliderHeight()) / 2.0f;
        Color color1 = UIColors.gradient(0, fullAlpha);
        Color color2 = UIColors.gradient(90, fullAlpha);
        RenderUtil.BLUR_RECT.draw(matrixStack, this.sliderX(), this.sliderY(), this.sliderWidth(), this.sliderHeight(), sliderRound, UIColors.backgroundBlur(fullAlpha));
        RenderUtil.BLUR_RECT.draw(matrixStack, this.sliderX(), this.sliderY(), this.currentWidth, this.sliderHeight(), new Vector4f(sliderRound), color2, color1, color2, color1);
        RenderUtil.BLUR_RECT.draw(matrixStack, knobX, this.sliderY() - hui, this.knobSize(), this.knobSize(), this.knobSize() * 0.4f, knobColor);
        if (inputAnim > 0.0f) {
            String displayText;
            float ifY = this.sliderY() + this.sliderHeight() / 2.0f + this.knobSize() / 2.0f + this.scaled(1.0f);
            float ifH = this.inputFieldHeight() * inputAnim;
            float ifRound = ifH * 0.25f;
            int ifAlpha = (int)(this.getAlpha() * inputAnim * 255.0f);
            RenderUtil.BLUR_RECT.draw(matrixStack, this.getX(), ifY, this.getWidth(), ifH, ifRound, UIColors.widgetBlur(ifAlpha));
            String cursor = this.inputActive && System.currentTimeMillis() % 900L > 450L ? "|" : "";
            String string = displayText = this.inputText.isEmpty() ? "" : this.inputText + cursor;
            if (displayText.isEmpty() && this.inputActive) {
                displayText = cursor;
            }
            float ifFontSize = this.fontSize() * 1.1f;
            Color ifTextColor = UIColors.textColor(ifAlpha);
            Fonts.PS_MEDIUM.drawCenteredText(matrixStack, displayText, this.getX() + this.getWidth() / 2.0f, ifY + ifH / 2.0f - ifFontSize / 2.0f, ifFontSize, ifTextColor);
        }
        this.setHeight(this.getDefaultHeight() + inputAnim * this.inputFieldHeight());
        if (this.dragging) {
            float newValue = ((float)mouseX - this.getX()) / this.sliderWidth();
            newValue = this.setting.getMin() + newValue * (this.setting.getMax() - this.setting.getMin());
            newValue = (float)Math.round(newValue / this.setting.getStep()) * this.setting.getStep();
            this.previewValue = MathUtil.round(Math.max(this.setting.getMin(), Math.min(this.setting.getMax(), newValue)), this.setting.getStep());
        }
        if (this.dragging && !MouseUtil.isHovered(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight())) {
            this.setting.setValue(Float.valueOf(this.previewValue));
            this.dragging = false;
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        float fontSize = this.fontSize();
        float bigPenis = ((Float)this.setting.getValue()).floatValue();
        String valueText = String.valueOf(bigPenis);
        float valueWidth = Fonts.PS_MEDIUM.getWidth(valueText, fontSize);
        float piska = this.scaled(0.5f);
        float valueX = this.getX() - piska + this.getWidth() - valueWidth;
        boolean onValue = MouseUtil.isHovered(mouseX, mouseY, (double)valueX, (double)this.getY(), (double)(valueWidth + piska * 2.0f), (double)(fontSize + piska * 2.0f));
        if (onValue && button == 1) {
            long now = System.currentTimeMillis();
            if (!this.inputActive) {
                this.inputActive = true;
                this.inputText = "";
                this.lastClickTime = now;
            } else {
                this.inputActive = false;
                this.inputText = "";
            }
            return;
        }
        if (button == 0 && MouseUtil.isHovered(mouseX, mouseY, (double)this.getX(), (double)this.getY(), (double)this.getWidth(), (double)this.getDefaultHeight()) && !onValue) {
            this.dragging = true;
            this.inputActive = false;
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (this.dragging) {
            this.setting.setValue(Float.valueOf(this.previewValue));
        }
        this.dragging = false;
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
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
                if (this.inputText.isEmpty()) break;
                this.inputText = this.inputText.substring(0, this.inputText.length() - 1);
            }
        }
    }

    public boolean charTyped(char chr, int modifiers) {
        if (!this.inputActive) {
            return false;
        }
        if (chr >= '0' && chr <= '9' || chr == '.' || chr == '-') {
            if (chr == '.' && this.inputText.contains(".")) {
                return true;
            }
            if (chr == '-' && !this.inputText.isEmpty()) {
                return true;
            }
            this.inputText = this.inputText + chr;
            return true;
        }
        return false;
    }

    private void applyInput() {
        try {
            float val = Float.parseFloat(this.inputText);
            val = (float)Math.round(val / this.setting.getStep()) * this.setting.getStep();
            val = class_3532.method_15363((float)val, (float)this.setting.getMin(), (float)this.setting.getMax());
            this.setting.setValue(Float.valueOf(MathUtil.round(val, this.setting.getStep())));
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
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
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    }
}

