/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_332
 *  net.minecraft.class_4587
 */
package sweetie.leonware.client.ui.clickgui.module.settings;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.ScissorUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.client.ui.clickgui.module.ExpandableComponent;
import sweetie.leonware.client.ui.clickgui.module.settings.BooleanComponent;

public class MultiBooleanComponent
extends ExpandableComponent.ExpandableSettingComponent {
    private final MultiBooleanSetting setting;
    private final AnimationUtil settingsAnimation = new AnimationUtil();
    private final List<BooleanComponent> booleans = new ArrayList<BooleanComponent>();

    public MultiBooleanComponent(MultiBooleanSetting setting) {
        super(setting);
        this.setting = setting;
        this.updateHeight(this.getDefaultHeight());
        for (BooleanSetting value : (List)setting.getValue()) {
            this.booleans.add(new BooleanComponent(value, true));
        }
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        class_4587 matrixStack = context.method_51448();
        this.updateOpen();
        this.settingsAnimation.update();
        if (this.isOpen()) {
            this.getAnim().run(1.0, 300L, Easing.EXPO_OUT);
            this.settingsAnimation.run((double)this.getValue() >= 0.9 ? 1.0 : 0.0, 300L, Easing.EXPO_OUT);
        } else {
            this.settingsAnimation.run(0.0, 200L, Easing.EXPO_OUT);
            if (this.settingsAnimation.getValue() <= 0.1) {
                this.getAnim().run(0.0, 300L, Easing.EXPO_OUT);
            }
        }
        float openAnim = this.getValue();
        float settingsAnim = (float)this.settingsAnimation.getValue();
        float fontSize = this.getDefaultHeight() * this.scaled(0.45f);
        float scd = this.scaled(this.getDefaultHeight());
        int fullAlpha = (int)(this.getAlpha() * 255.0f);
        String dermo = "...";
        float dermoWidth = Fonts.PS_MEDIUM.getWidth(dermo, fontSize);
        RenderUtil.BLUR_RECT.draw(matrixStack, this.getX(), this.getY(), this.getWidth(), this.getHeight(), this.getWidth() * 0.04f, UIColors.backgroundBlur(fullAlpha));
        Fonts.PS_MEDIUM.drawWrap(matrixStack, this.setting.getName(), this.getX() + this.offset(), this.getY() + scd / 2.0f - fontSize / 2.0f, this.getWidth() - this.offset() * 3.0f - dermoWidth, fontSize, UIColors.textColor(fullAlpha), this.scaled(16.0f), Duration.ofMillis(3000L), Duration.ofMillis(500L));
        Fonts.PS_MEDIUM.drawText(matrixStack, dermo, this.getX() + this.getWidth() - this.offset() * 2.0f - dermoWidth, this.getY() + scd / 2.0f - fontSize / 2.0f - this.scaled(2.0f), fontSize, UIColors.inactiveTextColor(fullAlpha));
        if ((double)openAnim > 0.0) {
            float bY = -this.scaled(2.0f) * (1.0f - settingsAnim);
            for (BooleanComponent component : this.booleans) {
                AnimationUtil anim = component.getVisibleAnimation();
                anim.update();
                anim.run(component.getSetting().isVisible() ? 1.0 : 0.0, 120L, Easing.SINE_OUT);
                component.setX(this.getX() + this.offset());
                component.setY(this.getY() + scd + bY);
                component.setWidth(this.getWidth() - this.offset() * 2.0f);
                bY += (float)((double)component.getHeight() * anim.getValue());
            }
            this.setHeight(this.scaled(this.getDefaultHeight()) + (bY + this.gap()) * openAnim);
            if ((double)settingsAnim > 0.0) {
                RenderUtil.OTHER.scaleStart(matrixStack, this.getX() + this.getWidth() / 2.0f, this.getY() + this.getDefaultHeight() + this.getHeight() / 2.0f - bY, 0.95f + 0.05f * settingsAnim);
                ScissorUtil.start(matrixStack, this.getX(), this.getY(), this.getWidth(), this.getHeight());
                for (BooleanComponent component : this.booleans) {
                    component.setAlpha((float)(component.getVisibleAnimation().getValue() * (double)this.getAlpha() * (double)settingsAnim));
                    component.render(context, mouseX, mouseY, delta);
                }
                ScissorUtil.stop(matrixStack);
                RenderUtil.OTHER.scaleStop(matrixStack);
            }
        } else {
            this.updateHeight(this.getDefaultHeight());
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (MouseUtil.isHovered(mouseX, mouseY, (double)this.getX(), (double)this.getY(), (double)this.getWidth(), (double)this.scaled(this.getDefaultHeight()))) {
            this.toggleOpen();
            return;
        }
        if (this.isNotOver()) {
            return;
        }
        for (BooleanComponent aBoolean : this.booleans) {
            if (aBoolean.getVisibleAnimation().getValue() < 0.8) continue;
            aBoolean.mouseClicked(mouseX, mouseY, button);
        }
    }

    private float getDefaultHeight() {
        return 16.0f;
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    }
}

