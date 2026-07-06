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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/clickgui/module/settings/BooleanComponent.class */
public class BooleanComponent extends SettingComponent {
    private final BooleanSetting setting;
    private final AnimationUtil toggleAnimation;
    private final boolean inMenu;
    private Color color;

    public BooleanComponent(BooleanSetting setting) {
        this(setting, false);
    }

    public BooleanComponent(BooleanSetting setting, boolean inMenu) {
        super(setting);
        this.toggleAnimation = new AnimationUtil();
        this.setting = setting;
        updateHeight(15.0f);
        this.toggleAnimation.setValue(setting.getValue().booleanValue() ? 1.0d : 0.0d);
        this.inMenu = inMenu;
        this.color = inMenu ? UIColors.widgetBlur() : UIColors.backgroundBlur();
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        updateHeight(15.0f);
        this.color = this.inMenu ? UIColors.widgetBlur() : UIColors.backgroundBlur();
        this.toggleAnimation.update();
        this.toggleAnimation.run(this.setting.getValue().booleanValue() ? 1.0d : 0.0d, 100L, Easing.SINE_OUT);
        class_4587 matrixStack = context.method_51448();
        float fontSize = getHeight() * 0.45f;
        int fullAlpha = (int) (getAlpha() * 255.0f);
        float anim = (float) this.toggleAnimation.getValue();
        float checkHeight = getHeight() * 0.67f;
        float checkWidth = checkHeight * 1.9f;
        float checkX = (getX() + getWidth()) - checkWidth;
        float checkY = (getY() + (getHeight() / 2.0f)) - (checkHeight / 2.0f);
        float checkRound = checkHeight * 0.4f;
        float baseKnob = checkHeight * 0.8f;
        float knobGap = (checkHeight - baseKnob) * 0.8f;
        float knobSize = baseKnob - knobGap;
        float knobPenis = knobGap * 1.3f;
        float knobY = (getY() + (getHeight() / 2.0f)) - (knobSize / 2.0f);
        float knobX = checkX + knobPenis + (((checkWidth - knobSize) - (knobPenis * 2.0f)) * anim);
        float knobRound = knobSize * 0.4f;
        Fonts.PS_MEDIUM.drawWrap(matrixStack, this.setting.getName(), getX(), (getY() + (getHeight() / 2.0f)) - (fontSize / 2.0f), (getWidth() - checkWidth) - knobGap, fontSize, UIColors.textColor(fullAlpha), scaled(16.0f), Duration.ofMillis(3000L), Duration.ofMillis(500L));
        Color knobColor = ColorUtil.interpolate(UIColors.knob(fullAlpha), UIColors.inactiveKnob(fullAlpha), anim);
        RenderUtil.BLUR_RECT.draw(matrixStack, checkX, checkY, checkWidth, checkHeight, checkRound, ColorUtil.setAlpha(this.color, fullAlpha));
        RenderUtil.BLUR_RECT.draw(matrixStack, knobX, knobY, knobSize, knobSize, knobRound, knobColor);
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (MouseUtil.isHovered(mouseX, mouseY, getX(), getY(), getWidth(), getHeight())) {
            this.setting.toggle();
        }
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseReleased(double mouseX, double mouseY, int button) {
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    }
}
