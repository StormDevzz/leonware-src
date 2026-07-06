/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_332
 *  net.minecraft.class_4587
 *  org.joml.Vector4f
 */
package sweetie.leonware.client.ui.clickgui.module;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import org.joml.Vector4f;
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
import sweetie.leonware.client.ui.clickgui.module.SettingComponent;
import sweetie.leonware.client.ui.clickgui.module.settings.BindComponent;
import sweetie.leonware.client.ui.clickgui.module.settings.BooleanComponent;
import sweetie.leonware.client.ui.clickgui.module.settings.ButtonComponent;
import sweetie.leonware.client.ui.clickgui.module.settings.ColorComponent;
import sweetie.leonware.client.ui.clickgui.module.settings.ModeComponent;
import sweetie.leonware.client.ui.clickgui.module.settings.MultiBooleanComponent;
import sweetie.leonware.client.ui.clickgui.module.settings.SliderComponent;

public class ModuleComponent
extends ExpandableComponent {
    private final List<SettingComponent> settings = new ArrayList<SettingComponent>();
    private final Module module;
    private float round;
    private boolean last;
    private int index;
    private boolean bind;
    private boolean searchVisible = true;
    private final TimerUtil shakeTimer = new TimerUtil();
    private final AnimationUtil enableAnimation = new AnimationUtil();
    private final AnimationUtil bindAnimation = new AnimationUtil();
    private final AnimationUtil hoverAnimation = new AnimationUtil();

    public ModuleComponent(Module module) {
        this.module = module;
        for (Setting<?> setting : module.getSettings()) {
            if (setting instanceof BooleanSetting) {
                BooleanSetting bool = (BooleanSetting)setting;
                this.settings.add(new BooleanComponent(bool));
            }
            if (setting instanceof MultiBooleanSetting) {
                MultiBooleanSetting multi = (MultiBooleanSetting)setting;
                this.settings.add(new MultiBooleanComponent(multi));
            }
            if (setting instanceof ModeSetting) {
                ModeSetting mode = (ModeSetting)setting;
                this.settings.add(new ModeComponent(mode));
            }
            if (setting instanceof SliderSetting) {
                SliderSetting slider = (SliderSetting)setting;
                this.settings.add(new SliderComponent(slider));
            }
            if (setting instanceof ColorSetting) {
                ColorSetting color = (ColorSetting)setting;
                this.settings.add(new ColorComponent(color));
            }
            if (setting instanceof RunSetting) {
                RunSetting DoniKuni = (RunSetting)setting;
                this.settings.add(new ButtonComponent(DoniKuni));
            }
            if (!(setting instanceof BindSetting)) continue;
            BindSetting sex = (BindSetting)setting;
            this.settings.add(new BindComponent(sex));
        }
        this.enableAnimation.setValue(module.isEnabled() ? 1.0 : 0.0);
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

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        boolean huesos;
        if (!this.searchVisible) {
            return;
        }
        class_4587 matrixStack = context.method_51448();
        this.updateOpen();
        this.hoverAnimation.update();
        this.enableAnimation.update();
        this.bindAnimation.update();
        this.bindAnimation.run(this.bind ? 1.0 : 0.0, 400L, Easing.EXPO_OUT);
        this.hoverAnimation.run(MouseUtil.isHovered(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getDefaultHeight()) ? 1.0 : 0.0, 300L, Easing.QUINT_OUT);
        this.enableAnimation.run(this.module.isEnabled() ? 1.0 : 0.0, 200L, Easing.EXPO_OUT);
        int fullAlpha = (int)(this.getAlpha() * 255.0f);
        Color rectColor1 = ColorUtil.setAlpha(ColorUtil.interpolate(UIColors.gradient(this.index), UIColors.blur(), this.enableAnimation.getValue()), fullAlpha);
        Color rectColor2 = ColorUtil.setAlpha(ColorUtil.interpolate(UIColors.gradient(this.index + 45), UIColors.blur(), this.enableAnimation.getValue()), fullAlpha);
        String bindText = (this.bind ? "Binding: " : "Bind: ") + KeyStorage.getBind(this.module.getBind());
        String defaultText = this.module.getName();
        float fontSize = this.getDefaultHeight() * 0.47f + this.scaled((float)this.hoverAnimation.getValue());
        float openAnim = this.getAnim();
        float finalRound = this.isLast() ? this.getRound() * 2.0f * (1.0f - openAnim) : 0.0f;
        Vector4f round = new Vector4f(0.0f, 0.0f, finalRound, finalRound);
        float nameAnim = 1.0f - (float)this.bindAnimation.getValue();
        float bindAnim = (float)this.bindAnimation.getValue();
        int bindAlpha1 = (int)(nameAnim * this.getAlpha() * 255.0f);
        int bindAlpha2 = (int)(bindAnim * this.getAlpha() * 255.0f);
        Color textColor1 = ColorUtil.interpolate(UIColors.textColor(bindAlpha1), UIColors.inactiveTextColor(bindAlpha1), this.enableAnimation.getValue());
        Color textColor2 = ColorUtil.interpolate(UIColors.textColor(bindAlpha2), UIColors.inactiveTextColor(bindAlpha2), this.enableAnimation.getValue());
        boolean bl = huesos = bindAnim > 0.0f;
        if ((double)openAnim > 0.0) {
            this.moduleSetting(context, mouseX, mouseY, delta);
        }
        RenderUtil.BLUR_RECT.draw(matrixStack, this.getX(), this.getY(), this.getWidth(), this.getDefaultHeight(), round, rectColor1, rectColor1, rectColor2, rectColor2);
        if (huesos) {
            ScissorUtil.start(matrixStack, this.getX(), this.getY(), this.getWidth(), this.getDefaultHeight());
        }
        if (nameAnim > 0.0f) {
            Fonts.PS_BOLD.drawCenteredText(matrixStack, defaultText, this.getX() + (this.getWidth() / 2.0f - this.offset() * openAnim) * nameAnim, this.getY() + this.getDefaultHeight() / 2.0f - fontSize / 2.0f, fontSize, textColor1);
        }
        if (bindAnim > 0.0f) {
            Fonts.PS_BOLD.drawCenteredText(matrixStack, bindText, this.getX() + this.getWidth() / 2.0f + this.getWidth() * nameAnim, this.getY() + this.getDefaultHeight() / 2.0f - fontSize / 2.0f, fontSize, textColor2);
        }
        if (huesos) {
            ScissorUtil.stop(matrixStack);
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.bind) {
            boolean deleteButton = keyCode == 256 || keyCode == 261;
            this.module.setBind(deleteButton ? -999 : keyCode);
            this.bind = false;
        }
        if (this.isNotOver()) {
            return;
        }
        for (SettingComponent setting : this.settings) {
            if ((double)setting.getAlpha() < 0.9) continue;
            setting.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    public boolean charTyped(char chr, int modifiers) {
        if (this.isNotOver()) {
            return false;
        }
        for (SettingComponent setting : this.settings) {
            SliderComponent slider;
            if ((double)setting.getAlpha() < 0.9 || !(setting instanceof SliderComponent) || !(slider = (SliderComponent)setting).charTyped(chr, modifiers)) continue;
            return true;
        }
        return false;
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        boolean hoveredToDefault = MouseUtil.isHovered(mouseX, mouseY, (double)this.getX(), (double)this.getY(), (double)this.getWidth(), (double)this.getDefaultHeight());
        if (this.bind && button != 1 && button != 2 && button != 0) {
            this.module.setBind(-100 + button);
            this.bind = false;
            return;
        }
        if (hoveredToDefault) {
            switch (button) {
                case 0: {
                    this.module.toggle();
                    break;
                }
                case 1: {
                    if (!this.settings.isEmpty()) {
                        this.toggleOpen();
                    }
                    if (this.isOpen()) break;
                    for (SettingComponent setting : this.settings) {
                        if (!(setting instanceof ExpandableComponent.ExpandableSettingComponent)) continue;
                        ExpandableComponent.ExpandableSettingComponent e = (ExpandableComponent.ExpandableSettingComponent)setting;
                        e.setOpen(false);
                    }
                    break;
                }
                case 2: {
                    this.bind = !this.bind;
                }
            }
            return;
        }
        if (this.isNotOver()) {
            return;
        }
        for (SettingComponent setting : this.settings) {
            if ((double)setting.getAlpha() < 0.9) continue;
            setting.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (this.isNotOver()) {
            return;
        }
        for (SettingComponent setting : this.settings) {
            if ((double)setting.getAlpha() < 0.9) continue;
            setting.mouseReleased(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    }

    private void moduleSetting(class_332 context, int mouseX, int mouseY, float delta) {
        class_4587 matrixStack = context.method_51448();
        float openAnim = this.getAnim();
        float reverseAnim = 1.0f - openAnim;
        float animPad = this.gap() * reverseAnim;
        float finalRound = this.isLast() ? this.getRound() * 2.0f * openAnim : 0.0f;
        Vector4f round = new Vector4f(0.0f, 0.0f, finalRound, finalRound);
        RenderUtil.BLUR_RECT.draw(matrixStack, this.getX(), this.getY() + this.getDefaultHeight(), this.getWidth(), this.getHeight() - this.getDefaultHeight(), round, UIColors.widgetBlur((int)(this.getAlpha() * openAnim * 255.0f)));
        float govnarik = this.offset() * (1.0f + 0.7f * reverseAnim);
        float doni = this.gap() - animPad;
        float componentY = this.getY() + this.getDefaultHeight() * openAnim + doni;
        for (SettingComponent setting : this.settings) {
            setting.getVisibleAnimation().update();
            setting.getVisibleAnimation().run(setting.getSetting().isVisible() ? 1.0 : 0.0, 120L, Easing.SINE_OUT);
            float visibleAnim = (float)setting.getVisibleAnimation().getValue();
            if (!(setting.getVisibleAnimation().getValue() > 0.0)) continue;
            setting.setX(this.getX() + govnarik);
            setting.setY(componentY);
            setting.setWidth(this.getWidth() - govnarik * 2.0f);
            setting.setAlpha(visibleAnim * openAnim * this.getAlpha());
            setting.render(context, mouseX, mouseY, delta);
            componentY += (setting.getHeight() + this.gap()) * visibleAnim * openAnim;
        }
    }

    public float getDefaultHeight() {
        return this.scaled(17.0f);
    }

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
    public boolean isLast() {
        return this.last;
    }

    @Generated
    public int getIndex() {
        return this.index;
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

    @Generated
    public void setRound(float round) {
        this.round = round;
    }

    @Generated
    public void setLast(boolean last) {
        this.last = last;
    }

    @Generated
    public void setIndex(int index) {
        this.index = index;
    }
}

