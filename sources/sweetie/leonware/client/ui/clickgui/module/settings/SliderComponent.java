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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/clickgui/module/settings/SliderComponent.class */
public class SliderComponent extends SettingComponent {
    private final SliderSetting setting;
    private boolean dragging;
    private float currentWidth;
    private float previewValue;
    private boolean inputActive;
    private String inputText;
    private long lastClickTime;
    private static final long DOUBLE_CLICK_MS = 350;
    private final AnimationUtil dragAnimation;
    private final AnimationUtil inputAnimation;

    public SliderComponent(SliderSetting setting) {
        super(setting);
        this.inputText = "";
        this.lastClickTime = 0L;
        this.dragAnimation = new AnimationUtil();
        this.inputAnimation = new AnimationUtil();
        this.setting = setting;
        this.previewValue = setting.getValue().floatValue();
        updateHeight(getDefaultHeight());
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        this.inputAnimation.update();
        this.inputAnimation.run(this.inputActive ? 1.0d : 0.0d, 300L, Easing.EXPO_OUT);
        float inputAnim = (float) this.inputAnimation.getValue();
        float totalHeight = getDefaultHeight() + (inputAnim * inputFieldHeight());
        updateHeight(totalHeight);
        this.dragAnimation.update();
        this.dragAnimation.run(this.dragging ? 1.0d : 0.0d, 500L, Easing.EXPO_OUT);
        class_4587 matrixStack = context.method_51448();
        float bigPenis = this.dragging ? this.previewValue : this.setting.getValue().floatValue();
        float fontSize = fontSize();
        String valueText = MathUtil.round(bigPenis, this.setting.getStep());
        if (valueText.contains(".")) {
            valueText = String.valueOf(bigPenis);
        }
        float valueWidth = Fonts.PS_MEDIUM.getWidth(valueText, fontSize);
        float piska = scaled(0.5f);
        int fullAlpha = (int) (getAlpha() * 255.0f);
        float progress = ((bigPenis - this.setting.getMin()) / (this.setting.getMax() - this.setting.getMin())) * sliderWidth();
        this.currentWidth = MathUtil.interpolate(this.currentWidth, progress, 0.2f);
        Fonts.PS_MEDIUM.drawText(matrixStack, this.setting.getName(), getX() + piska, getY() + piska, fontSize, UIColors.textColor(fullAlpha));
        Fonts.PS_MEDIUM.drawText(matrixStack, valueText, ((getX() - piska) + getWidth()) - valueWidth, getY() + piska, fontSize, UIColors.textColor(fullAlpha));
        float sliderRound = sliderHeight() * 0.3f;
        float knobX = class_3532.method_15363((sliderX() + this.currentWidth) - (knobSize() / 2.0f), sliderX(), (sliderX() + sliderWidth()) - knobSize());
        Color knobColor = ColorUtil.setAlpha(ColorUtil.interpolate(UIColors.knob(), UIColors.inactiveKnob(), this.dragAnimation.getValue()), fullAlpha);
        float hui = (knobSize() - sliderHeight()) / 2.0f;
        Color color1 = UIColors.gradient(0, fullAlpha);
        Color color2 = UIColors.gradient(90, fullAlpha);
        RenderUtil.BLUR_RECT.draw(matrixStack, sliderX(), sliderY(), sliderWidth(), sliderHeight(), sliderRound, UIColors.backgroundBlur(fullAlpha));
        RenderUtil.BLUR_RECT.draw(matrixStack, sliderX(), sliderY(), this.currentWidth, sliderHeight(), new Vector4f(sliderRound), color2, color1, color2, color1);
        RenderUtil.BLUR_RECT.draw(matrixStack, knobX, sliderY() - hui, knobSize(), knobSize(), knobSize() * 0.4f, knobColor);
        if (inputAnim > 0.0f) {
            float ifY = sliderY() + (sliderHeight() / 2.0f) + (knobSize() / 2.0f) + scaled(1.0f);
            float ifH = inputFieldHeight() * inputAnim;
            float ifRound = ifH * 0.25f;
            int ifAlpha = (int) (getAlpha() * inputAnim * 255.0f);
            RenderUtil.BLUR_RECT.draw(matrixStack, getX(), ifY, getWidth(), ifH, ifRound, UIColors.widgetBlur(ifAlpha));
            String cursor = (!this.inputActive || System.currentTimeMillis() % 900 <= 450) ? "" : "|";
            String displayText = this.inputText.isEmpty() ? "" : this.inputText + cursor;
            if (displayText.isEmpty() && this.inputActive) {
                displayText = cursor;
            }
            float ifFontSize = fontSize() * 1.1f;
            Color ifTextColor = UIColors.textColor(ifAlpha);
            Fonts.PS_MEDIUM.drawCenteredText(matrixStack, displayText, getX() + (getWidth() / 2.0f), (ifY + (ifH / 2.0f)) - (ifFontSize / 2.0f), ifFontSize, ifTextColor);
        }
        setHeight(getDefaultHeight() + (inputAnim * inputFieldHeight()));
        if (this.dragging) {
            float newValue = (mouseX - getX()) / sliderWidth();
            this.previewValue = MathUtil.round(Math.max(this.setting.getMin(), Math.min(this.setting.getMax(), Math.round((this.setting.getMin() + (newValue * (this.setting.getMax() - this.setting.getMin()))) / this.setting.getStep()) * this.setting.getStep())), this.setting.getStep());
        }
        if (this.dragging && !MouseUtil.isHovered(mouseX, mouseY, getX(), getY(), getWidth(), getHeight())) {
            this.setting.setValue(Float.valueOf(this.previewValue));
            this.dragging = false;
        }
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseClicked(double mouseX, double mouseY, int button) {
        float fontSize = fontSize();
        float bigPenis = this.setting.getValue().floatValue();
        String valueText = String.valueOf(bigPenis);
        float valueWidth = Fonts.PS_MEDIUM.getWidth(valueText, fontSize);
        float piska = scaled(0.5f);
        float valueX = ((getX() - piska) + getWidth()) - valueWidth;
        boolean onValue = MouseUtil.isHovered(mouseX, mouseY, valueX, getY(), valueWidth + (piska * 2.0f), fontSize + (piska * 2.0f));
        if (onValue && button == 1) {
            long now = System.currentTimeMillis();
            if (!this.inputActive) {
                this.inputActive = true;
                this.inputText = "";
                this.lastClickTime = now;
                return;
            } else {
                this.inputActive = false;
                this.inputText = "";
                return;
            }
        }
        if (button == 0 && MouseUtil.isHovered(mouseX, mouseY, getX(), getY(), getWidth(), getDefaultHeight()) && !onValue) {
            this.dragging = true;
            this.inputActive = false;
        }
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (this.dragging) {
            this.setting.setValue(Float.valueOf(this.previewValue));
        }
        this.dragging = false;
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.inputActive) {
            switch (keyCode) {
                case 256:
                    this.inputActive = false;
                    this.inputText = "";
                    break;
                case 257:
                case 335:
                    applyInput();
                    break;
                case 259:
                    if (!this.inputText.isEmpty()) {
                        this.inputText = this.inputText.substring(0, this.inputText.length() - 1);
                    }
                    break;
            }
        }
    }

    public boolean charTyped(char chr, int modifiers) {
        if (!this.inputActive) {
            return false;
        }
        if ((chr >= '0' && chr <= '9') || chr == '.' || chr == '-') {
            if (chr == '.' && this.inputText.contains(".")) {
                return true;
            }
            if (chr == '-' && !this.inputText.isEmpty()) {
                return true;
            }
            this.inputText += chr;
            return true;
        }
        return false;
    }

    private void applyInput() {
        try {
            float val = Float.parseFloat(this.inputText);
            this.setting.setValue(Float.valueOf(MathUtil.round(class_3532.method_15363(Math.round(val / this.setting.getStep()) * this.setting.getStep(), this.setting.getMin(), this.setting.getMax()), this.setting.getStep())));
        } catch (NumberFormatException e) {
        }
        this.inputActive = false;
        this.inputText = "";
    }

    private float fontSize() {
        return scaled(15.0f) * 0.45f;
    }

    private float sliderWidth() {
        return getWidth();
    }

    private float sliderHeight() {
        return scaled(3.5f);
    }

    private float knobSize() {
        return sliderHeight() * 1.5f;
    }

    private float sliderY() {
        return getY() + fontSize() + (knobSize() / 2.0f);
    }

    private float sliderX() {
        return getX();
    }

    private float getDefaultHeight() {
        return fontSize() + gap() + knobSize();
    }

    private float inputFieldHeight() {
        return scaled(14.0f);
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    }
}
