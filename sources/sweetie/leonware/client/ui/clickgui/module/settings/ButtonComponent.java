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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/clickgui/module/settings/ButtonComponent.class */
public class ButtonComponent extends SettingComponent {
    private final RunSetting setting;
    private final AnimationUtil hoverAnimation;

    public ButtonComponent(RunSetting setting) {
        super(setting);
        this.hoverAnimation = new AnimationUtil();
        this.setting = setting;
        updateHeight(getDefaultHeight());
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        updateHeight(getDefaultHeight());
        class_4587 matrixStack = context.method_51448();
        int fullAlpha = (int) (getAlpha() * 255.0f);
        this.hoverAnimation.update();
        this.hoverAnimation.run(hovered((double) mouseX, (double) mouseY) ? 1.0d : 0.0d, 500L, Easing.EXPO_OUT);
        Color buttonColor1 = ColorUtil.interpolate(UIColors.gradient(0, fullAlpha), UIColors.backgroundBlur(fullAlpha), this.hoverAnimation.getValue());
        Color buttonColor2 = ColorUtil.interpolate(UIColors.gradient(90, fullAlpha), UIColors.backgroundBlur(fullAlpha), this.hoverAnimation.getValue());
        float fontSize = (getHeight() * 0.45f) + scaled((float) this.hoverAnimation.getValue());
        float round = getWidth() * 0.04f;
        RenderUtil.BLUR_RECT.draw(matrixStack, getX(), getY(), getWidth(), getHeight(), new Vector4f(round), buttonColor1, buttonColor2, buttonColor1, buttonColor2);
        Fonts.PS_MEDIUM.drawCenteredText(matrixStack, this.setting.getName(), getX() + (getWidth() / 2.0f), (getY() + (getHeight() / 2.0f)) - (fontSize / 2.0f), fontSize, ColorUtil.setAlpha(UIColors.textColor(), fullAlpha));
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (hovered(mouseX, mouseY) && this.setting.getValue() != null) {
            this.setting.getValue().run();
        }
    }

    private boolean hovered(double mouseX, double mouseY) {
        return MouseUtil.isHovered(mouseX, mouseY, getX(), getY(), getWidth(), scaled(getDefaultHeight()));
    }

    private float getDefaultHeight() {
        return 15.0f;
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
