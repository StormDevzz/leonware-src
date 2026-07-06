/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_332
 *  net.minecraft.class_4587
 */
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

public class BindComponent
extends SettingComponent {
    private final BindSetting setting;
    private final AnimationUtil animation = new AnimationUtil();
    private boolean bind;

    public BindComponent(BindSetting setting) {
        super(setting);
        this.setting = setting;
        this.updateHeight(this.getDefaultHeight());
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        this.updateHeight(this.getDefaultHeight());
        class_4587 matrixStack = context.method_51448();
        this.animation.update();
        this.animation.run(this.bind ? 1.0 : 0.0, 500L, Easing.EXPO_OUT);
        float fontSize = this.getHeight() * 0.45f;
        float halfY = this.getY() + this.getHeight() / 2.0f;
        int fullAlpha = (int)(this.getAlpha() * 255.0f);
        Font mediumFont = Fonts.PS_MEDIUM;
        mediumFont.drawText(matrixStack, this.setting.getName(), this.getX(), halfY - fontSize / 2.0f, fontSize, UIColors.textColor(fullAlpha));
        float anim = (float)this.animation.getValue();
        float reverseAnim = (float)(1.0 - this.animation.getValue());
        float valueSize = fontSize * 0.9f;
        String noneText = "\u041d\u0435\u043c\u0430";
        String valueText = (Integer)this.setting.getValue() == -999 ? noneText : KeyStorage.getBind((Integer)this.setting.getValue());
        String bindingText = "...";
        float valueWidth = mediumFont.getWidth(valueText, valueSize);
        float bindingWidth = mediumFont.getWidth(bindingText, valueSize);
        float totalWidth = valueWidth * reverseAnim + bindingWidth * anim;
        float valueY = halfY - valueSize / 2.0f;
        float bindX = this.getX() + this.getWidth() - totalWidth - this.offset() * 2.0f;
        float bindY = valueY - this.offset();
        float bindWidth = totalWidth + this.offset() * 2.0f;
        float bindHeight = valueSize + this.offset() * 2.0f;
        float bindRound = bindHeight * 0.1f;
        RenderUtil.BLUR_RECT.draw(matrixStack, bindX, bindY, bindWidth, bindHeight, bindRound, UIColors.backgroundBlur(fullAlpha));
        ScissorUtil.start(matrixStack, bindX, bindY, bindWidth, bindHeight);
        if (reverseAnim > 0.0f) {
            mediumFont.drawText(matrixStack, valueText, bindX + this.offset(), valueY, valueSize, UIColors.textColor(this.getAlphaFrom(reverseAnim)));
        }
        if (anim > 0.0f) {
            mediumFont.drawText(matrixStack, bindingText, bindX + this.offset(), valueY, valueSize, UIColors.textColor(this.getAlphaFrom(anim)));
        }
        ScissorUtil.stop(matrixStack);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.bind) {
            this.setting.setValue(keyCode == 261 ? -999 : keyCode);
            this.bind = false;
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (this.bind && button != 1 && button != 0) {
            this.setting.setValue(-100 + button);
            this.bind = false;
            return;
        }
        if (MouseUtil.isHovered(mouseX, mouseY, (double)this.getX(), (double)this.getY(), (double)this.getWidth(), (double)this.getHeight())) {
            this.bind = !this.bind;
        }
    }

    private int getAlphaFrom(float anim) {
        return (int)(anim * this.getAlpha() * 255.0f);
    }

    private float getDefaultHeight() {
        return 15.0f;
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    }
}

