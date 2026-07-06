// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.clickgui.module.settings;

import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.api.utils.render.fonts.Font;
import net.minecraft.class_4587;
import sweetie.leonware.api.utils.render.ScissorUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.system.backend.KeyStorage;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.api.utils.animation.Easing;
import net.minecraft.class_332;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.module.setting.BindSetting;
import sweetie.leonware.client.ui.clickgui.module.SettingComponent;

public class BindComponent extends SettingComponent
{
    private final BindSetting setting;
    private final AnimationUtil animation;
    private boolean bind;
    
    public BindComponent(final BindSetting setting) {
        super(setting);
        this.animation = new AnimationUtil();
        this.setting = setting;
        this.updateHeight(this.getDefaultHeight());
    }
    
    @Override
    public void render(final class_332 context, final int mouseX, final int mouseY, final float delta) {
        this.updateHeight(this.getDefaultHeight());
        final class_4587 matrixStack = context.method_51448();
        this.animation.update();
        this.animation.run(this.bind ? 1.0 : 0.0, 500L, Easing.EXPO_OUT);
        final float fontSize = this.getHeight() * 0.45f;
        final float halfY = this.getY() + this.getHeight() / 2.0f;
        final int fullAlpha = (int)(this.getAlpha() * 255.0f);
        final Font mediumFont = Fonts.PS_MEDIUM;
        mediumFont.drawText(matrixStack, this.setting.getName(), this.getX(), halfY - fontSize / 2.0f, fontSize, UIColors.textColor(fullAlpha));
        final float anim = (float)this.animation.getValue();
        final float reverseAnim = (float)(1.0 - this.animation.getValue());
        final float valueSize = fontSize * 0.9f;
        final String noneText = "\u041d\u0435\u043c\u0430";
        final String valueText = (this.setting.getValue() == -999) ? noneText : KeyStorage.getBind(this.setting.getValue());
        final String bindingText = "...";
        final float valueWidth = mediumFont.getWidth(valueText, valueSize);
        final float bindingWidth = mediumFont.getWidth(bindingText, valueSize);
        final float totalWidth = valueWidth * reverseAnim + bindingWidth * anim;
        final float valueY = halfY - valueSize / 2.0f;
        final float bindX = this.getX() + this.getWidth() - totalWidth - this.offset() * 2.0f;
        final float bindY = valueY - this.offset();
        final float bindWidth = totalWidth + this.offset() * 2.0f;
        final float bindHeight = valueSize + this.offset() * 2.0f;
        final float bindRound = bindHeight * 0.1f;
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
    public void keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        if (this.bind) {
            this.setting.setValue((keyCode == 261) ? -999 : keyCode);
            this.bind = false;
        }
    }
    
    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (this.bind && button != 1 && button != 0) {
            this.setting.setValue(-100 + button);
            this.bind = false;
            return;
        }
        if (MouseUtil.isHovered(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight())) {
            this.bind = !this.bind;
        }
    }
    
    private int getAlphaFrom(final float anim) {
        return (int)(anim * this.getAlpha() * 255.0f);
    }
    
    private float getDefaultHeight() {
        return 15.0f;
    }
    
    @Override
    public void mouseReleased(final double mouseX, final double mouseY, final int button) {
    }
    
    @Override
    public void mouseScrolled(final double mouseX, final double mouseY, final double horizontalAmount, final double verticalAmount) {
    }
}
