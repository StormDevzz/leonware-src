package sweetie.leonware.client.ui.clickgui.module;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import org.joml.Vector4f;
import org.newsclub.net.unix.AFVSOCKSocketAddress;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.setting.BindSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.RunSetting;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.backend.KeyStorage;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.ScissorUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.client.ui.clickgui.module.ExpandableComponent;
import sweetie.leonware.client.ui.clickgui.module.settings.BindComponent;
import sweetie.leonware.client.ui.clickgui.module.settings.BooleanComponent;
import sweetie.leonware.client.ui.clickgui.module.settings.ButtonComponent;
import sweetie.leonware.client.ui.clickgui.module.settings.ColorComponent;
import sweetie.leonware.client.ui.clickgui.module.settings.ModeComponent;
import sweetie.leonware.client.ui.clickgui.module.settings.MultiBooleanComponent;
import sweetie.leonware.client.ui.clickgui.module.settings.SliderComponent;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/clickgui/module/ModuleComponent.class */
public class ModuleComponent extends ExpandableComponent {
    private final Module module;
    private float round;
    private boolean last;
    private int index;
    private boolean bind;
    private final List<SettingComponent> settings = new ArrayList();
    private boolean searchVisible = true;
    private final TimerUtil shakeTimer = new TimerUtil();
    private final AnimationUtil enableAnimation = new AnimationUtil();
    private final AnimationUtil bindAnimation = new AnimationUtil();
    private final AnimationUtil hoverAnimation = new AnimationUtil();

    @Generated
    public List<SettingComponent> getSettings() {
        return this.settings;
    }

    @Generated
    public Module getModule() {
        return this.module;
    }

    @Generated
    public float getRound() {
        return this.round;
    }

    @Generated
    public void setRound(float round) {
        this.round = round;
    }

    @Generated
    public boolean isLast() {
        return this.last;
    }

    @Generated
    public void setLast(boolean last) {
        this.last = last;
    }

    @Generated
    public int getIndex() {
        return this.index;
    }

    @Generated
    public void setIndex(int index) {
        this.index = index;
    }

    @Generated
    public boolean isBind() {
        return this.bind;
    }

    @Generated
    public TimerUtil getShakeTimer() {
        return this.shakeTimer;
    }

    @Generated
    public AnimationUtil getEnableAnimation() {
        return this.enableAnimation;
    }

    @Generated
    public AnimationUtil getBindAnimation() {
        return this.bindAnimation;
    }

    @Generated
    public AnimationUtil getHoverAnimation() {
        return this.hoverAnimation;
    }

    public ModuleComponent(Module module) {
        this.module = module;
        for (Setting<?> setting : module.getSettings()) {
            if (setting instanceof BooleanSetting) {
                BooleanSetting bool = (BooleanSetting) setting;
                this.settings.add(new BooleanComponent(bool));
            }
            if (setting instanceof MultiBooleanSetting) {
                MultiBooleanSetting multi = (MultiBooleanSetting) setting;
                this.settings.add(new MultiBooleanComponent(multi));
            }
            if (setting instanceof ModeSetting) {
                ModeSetting mode = (ModeSetting) setting;
                this.settings.add(new ModeComponent(mode));
            }
            if (setting instanceof SliderSetting) {
                SliderSetting slider = (SliderSetting) setting;
                this.settings.add(new SliderComponent(slider));
            }
            if (setting instanceof ColorSetting) {
                ColorSetting color = (ColorSetting) setting;
                this.settings.add(new ColorComponent(color));
            }
            if (setting instanceof RunSetting) {
                RunSetting DoniKuni = (RunSetting) setting;
                this.settings.add(new ButtonComponent(DoniKuni));
            }
            if (setting instanceof BindSetting) {
                BindSetting sex = (BindSetting) setting;
                this.settings.add(new BindComponent(sex));
            }
        }
        this.enableAnimation.setValue(module.isEnabled() ? 1.0d : 0.0d);
    }

    public void setSearchVisible(boolean visible) {
        this.searchVisible = visible;
    }

    public boolean isSearchVisible() {
        return this.searchVisible;
    }

    public boolean isBinding() {
        return this.bind;
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        if (this.searchVisible) {
            class_4587 matrixStack = context.method_51448();
            updateOpen();
            this.hoverAnimation.update();
            this.enableAnimation.update();
            this.bindAnimation.update();
            this.bindAnimation.run(this.bind ? 1.0d : 0.0d, 400L, Easing.EXPO_OUT);
            this.hoverAnimation.run(MouseUtil.isHovered((float) mouseX, (float) mouseY, getX(), getY(), getWidth(), getDefaultHeight()) ? 1.0d : 0.0d, 300L, Easing.QUINT_OUT);
            this.enableAnimation.run(this.module.isEnabled() ? 1.0d : 0.0d, 200L, Easing.EXPO_OUT);
            int fullAlpha = (int) (getAlpha() * 255.0f);
            Color rectColor1 = ColorUtil.setAlpha(ColorUtil.interpolate(UIColors.gradient(this.index), UIColors.blur(), this.enableAnimation.getValue()), fullAlpha);
            Color rectColor2 = ColorUtil.setAlpha(ColorUtil.interpolate(UIColors.gradient(this.index + 45), UIColors.blur(), this.enableAnimation.getValue()), fullAlpha);
            String bindText = (this.bind ? "Binding: " : "Bind: ") + KeyStorage.getBind(this.module.getBind());
            String defaultText = this.module.getName();
            float fontSize = (getDefaultHeight() * 0.47f) + scaled((float) this.hoverAnimation.getValue());
            float openAnim = getAnim();
            float finalRound = isLast() ? getRound() * 2.0f * (1.0f - openAnim) : 0.0f;
            Vector4f round = new Vector4f(0.0f, 0.0f, finalRound, finalRound);
            float nameAnim = 1.0f - ((float) this.bindAnimation.getValue());
            float bindAnim = (float) this.bindAnimation.getValue();
            int bindAlpha1 = (int) (nameAnim * getAlpha() * 255.0f);
            int bindAlpha2 = (int) (bindAnim * getAlpha() * 255.0f);
            Color textColor1 = ColorUtil.interpolate(UIColors.textColor(bindAlpha1), UIColors.inactiveTextColor(bindAlpha1), this.enableAnimation.getValue());
            Color textColor2 = ColorUtil.interpolate(UIColors.textColor(bindAlpha2), UIColors.inactiveTextColor(bindAlpha2), this.enableAnimation.getValue());
            boolean huesos = bindAnim > 0.0f;
            if (openAnim > 0.0d) {
                moduleSetting(context, mouseX, mouseY, delta);
            }
            RenderUtil.BLUR_RECT.draw(matrixStack, getX(), getY(), getWidth(), getDefaultHeight(), round, rectColor1, rectColor1, rectColor2, rectColor2);
            if (huesos) {
                ScissorUtil.start(matrixStack, getX(), getY(), getWidth(), getDefaultHeight());
            }
            if (nameAnim > 0.0f) {
                Fonts.PS_BOLD.drawCenteredText(matrixStack, defaultText, getX() + (((getWidth() / 2.0f) - (offset() * openAnim)) * nameAnim), (getY() + (getDefaultHeight() / 2.0f)) - (fontSize / 2.0f), fontSize, textColor1);
            }
            if (bindAnim > 0.0f) {
                Fonts.PS_BOLD.drawCenteredText(matrixStack, bindText, getX() + (getWidth() / 2.0f) + (getWidth() * nameAnim), (getY() + (getDefaultHeight() / 2.0f)) - (fontSize / 2.0f), fontSize, textColor2);
            }
            if (huesos) {
                ScissorUtil.stop(matrixStack);
            }
        }
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.bind) {
            boolean deleteButton = keyCode == 256 || keyCode == 261;
            this.module.setBind(deleteButton ? -999 : keyCode);
            this.bind = false;
        }
        if (isNotOver()) {
            return;
        }
        for (SettingComponent setting : this.settings) {
            if (setting.getAlpha() >= 0.9d) {
                setting.keyPressed(keyCode, scanCode, modifiers);
            }
        }
    }

    public boolean charTyped(char chr, int modifiers) {
        if (isNotOver()) {
            return false;
        }
        for (SettingComponent setting : this.settings) {
            if (setting.getAlpha() >= 0.9d && (setting instanceof SliderComponent)) {
                SliderComponent slider = (SliderComponent) setting;
                if (slider.charTyped(chr, modifiers)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseClicked(double mouseX, double mouseY, int button) {
        boolean hoveredToDefault = MouseUtil.isHovered(mouseX, mouseY, getX(), getY(), getWidth(), getDefaultHeight());
        if (this.bind && button != 1 && button != 2 && button != 0) {
            this.module.setBind((-100) + button);
            this.bind = false;
            return;
        }
        if (hoveredToDefault) {
            switch (button) {
                case 0:
                    this.module.toggle();
                    break;
                case 1:
                    if (!this.settings.isEmpty()) {
                        toggleOpen();
                    }
                    if (!isOpen()) {
                        for (SettingComponent setting : this.settings) {
                            if (setting instanceof ExpandableComponent.ExpandableSettingComponent) {
                                ExpandableComponent.ExpandableSettingComponent e = (ExpandableComponent.ExpandableSettingComponent) setting;
                                e.setOpen(false);
                            }
                        }
                    }
                    break;
                case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                    this.bind = !this.bind;
                    break;
            }
            return;
        }
        if (isNotOver()) {
            return;
        }
        for (SettingComponent setting2 : this.settings) {
            if (setting2.getAlpha() >= 0.9d) {
                setting2.mouseClicked(mouseX, mouseY, button);
            }
        }
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (isNotOver()) {
            return;
        }
        for (SettingComponent setting : this.settings) {
            if (setting.getAlpha() >= 0.9d) {
                setting.mouseReleased(mouseX, mouseY, button);
            }
        }
    }

    @Override // sweetie.leonware.api.system.interfaces.UIApi
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    }

    private void moduleSetting(class_332 context, int mouseX, int mouseY, float delta) {
        class_4587 matrixStack = context.method_51448();
        float openAnim = getAnim();
        float reverseAnim = 1.0f - openAnim;
        float animPad = gap() * reverseAnim;
        float finalRound = isLast() ? getRound() * 2.0f * openAnim : 0.0f;
        Vector4f round = new Vector4f(0.0f, 0.0f, finalRound, finalRound);
        RenderUtil.BLUR_RECT.draw(matrixStack, getX(), getY() + getDefaultHeight(), getWidth(), getHeight() - getDefaultHeight(), round, UIColors.widgetBlur((int) (getAlpha() * openAnim * 255.0f)));
        float govnarik = offset() * (1.0f + (0.7f * reverseAnim));
        float doni = gap() - animPad;
        float componentY = getY() + (getDefaultHeight() * openAnim) + doni;
        for (SettingComponent setting : this.settings) {
            setting.getVisibleAnimation().update();
            setting.getVisibleAnimation().run(setting.getSetting().isVisible() ? 1.0d : 0.0d, 120L, Easing.SINE_OUT);
            float visibleAnim = (float) setting.getVisibleAnimation().getValue();
            if (setting.getVisibleAnimation().getValue() > 0.0d) {
                setting.setX(getX() + govnarik);
                setting.setY(componentY);
                setting.setWidth(getWidth() - (govnarik * 2.0f));
                setting.setAlpha(visibleAnim * openAnim * getAlpha());
                setting.render(context, mouseX, mouseY, delta);
                componentY += (setting.getHeight() + gap()) * visibleAnim * openAnim;
            }
        }
    }

    public float getDefaultHeight() {
        return scaled(17.0f);
    }
}
