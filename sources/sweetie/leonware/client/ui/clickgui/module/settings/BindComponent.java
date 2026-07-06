package sweetie.leonware.client.ui.clickgui.module.settings;

import net.minecraft.class_332;
import net.minecraft.class_4587;
import sweetie.leonware.api.module.setting.BindSetting;
import sweetie.leonware.api.system.backend.KeyStorage;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.ScissorUtil;
import sweetie.leonware.api.utils.render.fonts.Font;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.client.ui.clickgui.module.SettingComponent;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/clickgui/module/settings/BindComponent.class */
public class BindComponent extends SettingComponent {
    private final BindSetting setting;
    private final AnimationUtil animation;
    private boolean bind;

    public BindComponent(BindSetting setting) {
        super(setting);
        this.animation = new AnimationUtil();
        this.setting = setting;
        updateHeight(getDefaultHeight());
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        updateHeight(getDefaultHeight());
        class_4587 matrixStack = context.method_51448();
        this.animation.update();
        this.animation.run(this.bind ? 1.0d : 0.0d, 500L, Easing.EXPO_OUT);
        float fontSize = getHeight() * 0.45f;
        float halfY = getY() + (getHeight() / 2.0f);
        int fullAlpha = (int) (getAlpha() * 255.0f);
        Font mediumFont = Fonts.PS_MEDIUM;
        mediumFont.drawText(matrixStack, this.setting.getName(), getX(), halfY - (fontSize / 2.0f), fontSize, UIColors.textColor(fullAlpha));
        float anim = (float) this.animation.getValue();
        float reverseAnim = (float) (1.0d - this.animation.getValue());
        float valueSize = fontSize * 0.9f;
        String valueText = this.setting.getValue().intValue() == -999 ? "Нема" : KeyStorage.getBind(this.setting.getValue().intValue());
        float valueWidth = mediumFont.getWidth(valueText, valueSize);
        float bindingWidth = mediumFont.getWidth("...", valueSize);
        float totalWidth = (valueWidth * reverseAnim) + (bindingWidth * anim);
        float valueY = halfY - (valueSize / 2.0f);
        float bindX = ((getX() + getWidth()) - totalWidth) - (offset() * 2.0f);
        float bindY = valueY - offset();
        float bindWidth = totalWidth + (offset() * 2.0f);
        float bindHeight = valueSize + (offset() * 2.0f);
        float bindRound = bindHeight * 0.1f;
        RenderUtil.BLUR_RECT.draw(matrixStack, bindX, bindY, bindWidth, bindHeight, bindRound, UIColors.backgroundBlur(fullAlpha));
        ScissorUtil.start(matrixStack, bindX, bindY, bindWidth, bindHeight);
        if (reverseAnim > 0.0f) {
            mediumFont.drawText(matrixStack, valueText, bindX + offset(), valueY, valueSize, UIColors.textColor(getAlphaFrom(reverseAnim)));
        }
        if (anim > 0.0f) {
            mediumFont.drawText(matrixStack, "...", bindX + offset(), valueY, valueSize, UIColors.textColor(getAlphaFrom(anim)));
        }
        ScissorUtil.stop(matrixStack);
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.bind) {
            this.setting.setValue(Integer.valueOf(keyCode == 261 ? -999 : keyCode));
            this.bind = false;
        }
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (this.bind && button != 1 && button != 0) {
            this.setting.setValue(Integer.valueOf((-100) + button));
            this.bind = false;
        } else if (MouseUtil.isHovered(mouseX, mouseY, getX(), getY(), getWidth(), getHeight())) {
            this.bind = !this.bind;
        }
    }

    private int getAlphaFrom(float anim) {
        return (int) (anim * getAlpha() * 255.0f);
    }

    private float getDefaultHeight() {
        return 15.0f;
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseReleased(double mouseX, double mouseY, int button) {
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    }
}
