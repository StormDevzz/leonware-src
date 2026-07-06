// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.clickgui.module;

import lombok.Generated;
import java.awt.Color;
import net.minecraft.class_4587;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.api.utils.render.ScissorUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import org.joml.Vector4f;
import sweetie.leonware.api.system.backend.KeyStorage;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.api.utils.animation.Easing;
import net.minecraft.class_332;
import java.util.Iterator;
import sweetie.leonware.client.ui.clickgui.module.settings.BindComponent;
import sweetie.leonware.api.module.setting.BindSetting;
import sweetie.leonware.client.ui.clickgui.module.settings.ButtonComponent;
import sweetie.leonware.api.module.setting.RunSetting;
import sweetie.leonware.client.ui.clickgui.module.settings.ColorComponent;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.client.ui.clickgui.module.settings.SliderComponent;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.client.ui.clickgui.module.settings.ModeComponent;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.client.ui.clickgui.module.settings.MultiBooleanComponent;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.client.ui.clickgui.module.settings.BooleanComponent;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.Setting;
import java.util.ArrayList;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.module.Module;
import java.util.List;

public class ModuleComponent extends ExpandableComponent
{
    private final List<SettingComponent> settings;
    private final Module module;
    private float round;
    private boolean last;
    private int index;
    private boolean bind;
    private boolean searchVisible;
    private final TimerUtil shakeTimer;
    private final AnimationUtil enableAnimation;
    private final AnimationUtil bindAnimation;
    private final AnimationUtil hoverAnimation;
    
    public ModuleComponent(final Module module) {
        this.settings = new ArrayList<SettingComponent>();
        this.searchVisible = true;
        this.shakeTimer = new TimerUtil();
        this.enableAnimation = new AnimationUtil();
        this.bindAnimation = new AnimationUtil();
        this.hoverAnimation = new AnimationUtil();
        this.module = module;
        for (final Setting<?> setting : module.getSettings()) {
            if (setting instanceof final BooleanSetting bool) {
                this.settings.add(new BooleanComponent(bool));
            }
            if (setting instanceof final MultiBooleanSetting multi) {
                this.settings.add(new MultiBooleanComponent(multi));
            }
            if (setting instanceof final ModeSetting mode) {
                this.settings.add(new ModeComponent(mode));
            }
            if (setting instanceof final SliderSetting slider) {
                this.settings.add(new SliderComponent(slider));
            }
            if (setting instanceof final ColorSetting color) {
                this.settings.add(new ColorComponent(color));
            }
            if (setting instanceof final RunSetting DoniKuni) {
                this.settings.add(new ButtonComponent(DoniKuni));
            }
            if (setting instanceof final BindSetting sex) {
                this.settings.add(new BindComponent(sex));
            }
        }
        this.enableAnimation.setValue(module.isEnabled() ? 1.0 : 0.0);
    }
    
    public void setSearchVisible(final boolean visible) {
        this.searchVisible = visible;
    }
    
    public boolean isSearchVisible() {
        return this.searchVisible;
    }
    
    public boolean isBinding() {
        return this.bind;
    }
    
    @Override
    public void render(final class_332 context, final int mouseX, final int mouseY, final float delta) {
        if (!this.searchVisible) {
            return;
        }
        final class_4587 matrixStack = context.method_51448();
        this.updateOpen();
        this.hoverAnimation.update();
        this.enableAnimation.update();
        this.bindAnimation.update();
        this.bindAnimation.run(this.bind ? 1.0 : 0.0, 400L, Easing.EXPO_OUT);
        this.hoverAnimation.run(MouseUtil.isHovered((float)mouseX, (float)mouseY, this.getX(), this.getY(), this.getWidth(), this.getDefaultHeight()) ? 1.0 : 0.0, 300L, Easing.QUINT_OUT);
        this.enableAnimation.run(this.module.isEnabled() ? 1.0 : 0.0, 200L, Easing.EXPO_OUT);
        final int fullAlpha = (int)(this.getAlpha() * 255.0f);
        final Color rectColor1 = ColorUtil.setAlpha(ColorUtil.interpolate(UIColors.gradient(this.index), UIColors.blur(), this.enableAnimation.getValue()), fullAlpha);
        final Color rectColor2 = ColorUtil.setAlpha(ColorUtil.interpolate(UIColors.gradient(this.index + 45), UIColors.blur(), this.enableAnimation.getValue()), fullAlpha);
        final String bindText = (this.bind ? "Binding: " : "Bind: ") + KeyStorage.getBind(this.module.getBind());
        final String defaultText = this.module.getName();
        final float fontSize = this.getDefaultHeight() * 0.47f + this.scaled((float)this.hoverAnimation.getValue());
        final float openAnim = this.getAnim();
        final float finalRound = this.isLast() ? (this.getRound() * 2.0f * (1.0f - openAnim)) : 0.0f;
        final Vector4f round = new Vector4f(0.0f, 0.0f, finalRound, finalRound);
        final float nameAnim = 1.0f - (float)this.bindAnimation.getValue();
        final float bindAnim = (float)this.bindAnimation.getValue();
        final int bindAlpha1 = (int)(nameAnim * this.getAlpha() * 255.0f);
        final int bindAlpha2 = (int)(bindAnim * this.getAlpha() * 255.0f);
        final Color textColor1 = ColorUtil.interpolate(UIColors.textColor(bindAlpha1), UIColors.inactiveTextColor(bindAlpha1), this.enableAnimation.getValue());
        final Color textColor2 = ColorUtil.interpolate(UIColors.textColor(bindAlpha2), UIColors.inactiveTextColor(bindAlpha2), this.enableAnimation.getValue());
        final boolean huesos = bindAnim > 0.0f;
        if (openAnim > 0.0) {
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
    public void keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        if (this.bind) {
            final boolean deleteButton = keyCode == 256 || keyCode == 261;
            this.module.setBind(deleteButton ? -999 : keyCode);
            this.bind = false;
        }
        if (this.isNotOver()) {
            return;
        }
        for (final SettingComponent setting : this.settings) {
            if (setting.getAlpha() < 0.9) {
                continue;
            }
            setting.keyPressed(keyCode, scanCode, modifiers);
        }
    }
    
    public boolean charTyped(final char chr, final int modifiers) {
        if (this.isNotOver()) {
            return false;
        }
        for (final SettingComponent setting : this.settings) {
            if (setting.getAlpha() < 0.9) {
                continue;
            }
            if (!(setting instanceof SliderComponent)) {
                continue;
            }
            final SliderComponent slider = (SliderComponent)setting;
            if (slider.charTyped(chr, modifiers)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        final boolean hoveredToDefault = MouseUtil.isHovered(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getDefaultHeight());
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
                    if (!this.isOpen()) {
                        for (final SettingComponent setting : this.settings) {
                            if (setting instanceof final ExpandableSettingComponent e) {
                                e.setOpen(false);
                            }
                        }
                        break;
                    }
                    break;
                }
                case 2: {
                    this.bind = !this.bind;
                    break;
                }
            }
            return;
        }
        if (this.isNotOver()) {
            return;
        }
        for (final SettingComponent setting : this.settings) {
            if (setting.getAlpha() < 0.9) {
                continue;
            }
            setting.mouseClicked(mouseX, mouseY, button);
        }
    }
    
    @Override
    public void mouseReleased(final double mouseX, final double mouseY, final int button) {
        if (this.isNotOver()) {
            return;
        }
        for (final SettingComponent setting : this.settings) {
            if (setting.getAlpha() < 0.9) {
                continue;
            }
            setting.mouseReleased(mouseX, mouseY, button);
        }
    }
    
    @Override
    public void mouseScrolled(final double mouseX, final double mouseY, final double horizontalAmount, final double verticalAmount) {
    }
    
    private void moduleSetting(final class_332 context, final int mouseX, final int mouseY, final float delta) {
        final class_4587 matrixStack = context.method_51448();
        final float openAnim = this.getAnim();
        final float reverseAnim = 1.0f - openAnim;
        final float animPad = this.gap() * reverseAnim;
        final float finalRound = this.isLast() ? (this.getRound() * 2.0f * openAnim) : 0.0f;
        final Vector4f round = new Vector4f(0.0f, 0.0f, finalRound, finalRound);
        RenderUtil.BLUR_RECT.draw(matrixStack, this.getX(), this.getY() + this.getDefaultHeight(), this.getWidth(), this.getHeight() - this.getDefaultHeight(), round, UIColors.widgetBlur((int)(this.getAlpha() * openAnim * 255.0f)));
        final float govnarik = this.offset() * (1.0f + 0.7f * reverseAnim);
        final float doni = this.gap() - animPad;
        float componentY = this.getY() + this.getDefaultHeight() * openAnim + doni;
        for (final SettingComponent setting : this.settings) {
            setting.getVisibleAnimation().update();
            setting.getVisibleAnimation().run(setting.getSetting().isVisible() ? 1.0 : 0.0, 120L, Easing.SINE_OUT);
            final float visibleAnim = (float)setting.getVisibleAnimation().getValue();
            if (setting.getVisibleAnimation().getValue() > 0.0) {
                setting.setX(this.getX() + govnarik);
                setting.setY(componentY);
                setting.setWidth(this.getWidth() - govnarik * 2.0f);
                setting.setAlpha(visibleAnim * openAnim * this.getAlpha());
                setting.render(context, mouseX, mouseY, delta);
                componentY += (setting.getHeight() + this.gap()) * visibleAnim * openAnim;
            }
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
    public void setRound(final float round) {
        this.round = round;
    }
    
    @Generated
    public void setLast(final boolean last) {
        this.last = last;
    }
    
    @Generated
    public void setIndex(final int index) {
        this.index = index;
    }
}
