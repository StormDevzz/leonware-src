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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/clickgui/module/settings/MultiBooleanComponent.class */
public class MultiBooleanComponent extends ExpandableComponent.ExpandableSettingComponent {
    private final MultiBooleanSetting setting;
    private final AnimationUtil settingsAnimation;
    private final List<BooleanComponent> booleans;

    public MultiBooleanComponent(MultiBooleanSetting setting) {
        super(setting);
        this.settingsAnimation = new AnimationUtil();
        this.booleans = new ArrayList();
        this.setting = setting;
        updateHeight(getDefaultHeight());
        for (BooleanSetting value : setting.getValue()) {
            this.booleans.add(new BooleanComponent(value, true));
        }
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        class_4587 matrixStack = context.method_51448();
        updateOpen();
        this.settingsAnimation.update();
        if (isOpen()) {
            getAnim().run(1.0d, 300L, Easing.EXPO_OUT);
            this.settingsAnimation.run(((double) getValue()) >= 0.9d ? 1.0d : 0.0d, 300L, Easing.EXPO_OUT);
        } else {
            this.settingsAnimation.run(0.0d, 200L, Easing.EXPO_OUT);
            if (this.settingsAnimation.getValue() <= 0.1d) {
                getAnim().run(0.0d, 300L, Easing.EXPO_OUT);
            }
        }
        float openAnim = getValue();
        float settingsAnim = (float) this.settingsAnimation.getValue();
        float fontSize = getDefaultHeight() * scaled(0.45f);
        float scd = scaled(getDefaultHeight());
        int fullAlpha = (int) (getAlpha() * 255.0f);
        float dermoWidth = Fonts.PS_MEDIUM.getWidth("...", fontSize);
        RenderUtil.BLUR_RECT.draw(matrixStack, getX(), getY(), getWidth(), getHeight(), getWidth() * 0.04f, UIColors.backgroundBlur(fullAlpha));
        Fonts.PS_MEDIUM.drawWrap(matrixStack, this.setting.getName(), getX() + offset(), (getY() + (scd / 2.0f)) - (fontSize / 2.0f), (getWidth() - (offset() * 3.0f)) - dermoWidth, fontSize, UIColors.textColor(fullAlpha), scaled(16.0f), Duration.ofMillis(3000L), Duration.ofMillis(500L));
        Fonts.PS_MEDIUM.drawText(matrixStack, "...", ((getX() + getWidth()) - (offset() * 2.0f)) - dermoWidth, ((getY() + (scd / 2.0f)) - (fontSize / 2.0f)) - scaled(2.0f), fontSize, UIColors.inactiveTextColor(fullAlpha));
        if (openAnim > 0.0d) {
            float bY = (-scaled(2.0f)) * (1.0f - settingsAnim);
            for (BooleanComponent component : this.booleans) {
                AnimationUtil anim = component.getVisibleAnimation();
                anim.update();
                anim.run(component.getSetting().isVisible() ? 1.0d : 0.0d, 120L, Easing.SINE_OUT);
                component.setX(getX() + offset());
                component.setY(getY() + scd + bY);
                component.setWidth(getWidth() - (offset() * 2.0f));
                bY += (float) (((double) component.getHeight()) * anim.getValue());
            }
            setHeight(scaled(getDefaultHeight()) + ((bY + gap()) * openAnim));
            if (settingsAnim > 0.0d) {
                RenderUtil.OTHER.scaleStart(matrixStack, getX() + (getWidth() / 2.0f), ((getY() + getDefaultHeight()) + (getHeight() / 2.0f)) - bY, 0.95f + (0.05f * settingsAnim));
                ScissorUtil.start(matrixStack, getX(), getY(), getWidth(), getHeight());
                for (BooleanComponent component2 : this.booleans) {
                    component2.setAlpha((float) (component2.getVisibleAnimation().getValue() * ((double) getAlpha()) * ((double) settingsAnim)));
                    component2.render(context, mouseX, mouseY, delta);
                }
                ScissorUtil.stop(matrixStack);
                RenderUtil.OTHER.scaleStop(matrixStack);
                return;
            }
            return;
        }
        updateHeight(getDefaultHeight());
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (MouseUtil.isHovered(mouseX, mouseY, getX(), getY(), getWidth(), scaled(getDefaultHeight()))) {
            toggleOpen();
            return;
        }
        if (isNotOver()) {
            return;
        }
        for (BooleanComponent aBoolean : this.booleans) {
            if (aBoolean.getVisibleAnimation().getValue() >= 0.8d) {
                aBoolean.mouseClicked(mouseX, mouseY, button);
            }
        }
    }

    private float getDefaultHeight() {
        return 16.0f;
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
