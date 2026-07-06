// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.clickgui.module.settings;

import sweetie.leonware.api.utils.math.MouseUtil;
import net.minecraft.class_4587;
import sweetie.leonware.api.utils.render.ScissorUtil;
import java.time.Duration;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.api.utils.animation.Easing;
import net.minecraft.class_332;
import java.util.Iterator;
import sweetie.leonware.api.module.setting.BooleanSetting;
import java.util.ArrayList;
import sweetie.leonware.api.module.setting.Setting;
import java.util.List;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.client.ui.clickgui.module.ExpandableComponent;

public class MultiBooleanComponent extends ExpandableComponent.ExpandableSettingComponent
{
    private final MultiBooleanSetting setting;
    private final AnimationUtil settingsAnimation;
    private final List<BooleanComponent> booleans;
    
    public MultiBooleanComponent(final MultiBooleanSetting setting) {
        super(setting);
        this.settingsAnimation = new AnimationUtil();
        this.booleans = new ArrayList<BooleanComponent>();
        this.setting = setting;
        this.updateHeight(this.getDefaultHeight());
        for (final BooleanSetting value : ((Setting<List>)setting).getValue()) {
            this.booleans.add(new BooleanComponent(value, true));
        }
    }
    
    @Override
    public void render(final class_332 context, final int mouseX, final int mouseY, final float delta) {
        final class_4587 matrixStack = context.method_51448();
        this.updateOpen();
        this.settingsAnimation.update();
        if (this.isOpen()) {
            this.getAnim().run(1.0, 300L, Easing.EXPO_OUT);
            this.settingsAnimation.run((this.getValue() >= 0.9) ? 1.0 : 0.0, 300L, Easing.EXPO_OUT);
        }
        else {
            this.settingsAnimation.run(0.0, 200L, Easing.EXPO_OUT);
            if (this.settingsAnimation.getValue() <= 0.1) {
                this.getAnim().run(0.0, 300L, Easing.EXPO_OUT);
            }
        }
        final float openAnim = this.getValue();
        final float settingsAnim = (float)this.settingsAnimation.getValue();
        final float fontSize = this.getDefaultHeight() * this.scaled(0.45f);
        final float scd = this.scaled(this.getDefaultHeight());
        final int fullAlpha = (int)(this.getAlpha() * 255.0f);
        final String dermo = "...";
        final float dermoWidth = Fonts.PS_MEDIUM.getWidth(dermo, fontSize);
        RenderUtil.BLUR_RECT.draw(matrixStack, this.getX(), this.getY(), this.getWidth(), this.getHeight(), this.getWidth() * 0.04f, UIColors.backgroundBlur(fullAlpha));
        Fonts.PS_MEDIUM.drawWrap(matrixStack, this.setting.getName(), this.getX() + this.offset(), this.getY() + scd / 2.0f - fontSize / 2.0f, this.getWidth() - this.offset() * 3.0f - dermoWidth, fontSize, UIColors.textColor(fullAlpha), this.scaled(16.0f), Duration.ofMillis(3000L), Duration.ofMillis(500L));
        Fonts.PS_MEDIUM.drawText(matrixStack, dermo, this.getX() + this.getWidth() - this.offset() * 2.0f - dermoWidth, this.getY() + scd / 2.0f - fontSize / 2.0f - this.scaled(2.0f), fontSize, UIColors.inactiveTextColor(fullAlpha));
        if (openAnim > 0.0) {
            float bY = -this.scaled(2.0f) * (1.0f - settingsAnim);
            for (final BooleanComponent component : this.booleans) {
                final AnimationUtil anim = component.getVisibleAnimation();
                anim.update();
                anim.run(component.getSetting().isVisible() ? 1.0 : 0.0, 120L, Easing.SINE_OUT);
                component.setX(this.getX() + this.offset());
                component.setY(this.getY() + scd + bY);
                component.setWidth(this.getWidth() - this.offset() * 2.0f);
                bY += (float)(component.getHeight() * anim.getValue());
            }
            this.setHeight(this.scaled(this.getDefaultHeight()) + (bY + this.gap()) * openAnim);
            if (settingsAnim > 0.0) {
                RenderUtil.OTHER.scaleStart(matrixStack, this.getX() + this.getWidth() / 2.0f, this.getY() + this.getDefaultHeight() + this.getHeight() / 2.0f - bY, 0.95f + 0.05f * settingsAnim);
                ScissorUtil.start(matrixStack, this.getX(), this.getY(), this.getWidth(), this.getHeight());
                for (final BooleanComponent component : this.booleans) {
                    component.setAlpha((float)(component.getVisibleAnimation().getValue() * this.getAlpha() * settingsAnim));
                    component.render(context, mouseX, mouseY, delta);
                }
                ScissorUtil.stop(matrixStack);
                RenderUtil.OTHER.scaleStop(matrixStack);
            }
        }
        else {
            this.updateHeight(this.getDefaultHeight());
        }
    }
    
    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (MouseUtil.isHovered(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.scaled(this.getDefaultHeight()))) {
            this.toggleOpen();
            return;
        }
        if (this.isNotOver()) {
            return;
        }
        for (final BooleanComponent aBoolean : this.booleans) {
            if (aBoolean.getVisibleAnimation().getValue() < 0.8) {
                continue;
            }
            aBoolean.mouseClicked(mouseX, mouseY, button);
        }
    }
    
    private float getDefaultHeight() {
        return 16.0f;
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
