// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.clickgui.module.settings;

import sweetie.leonware.api.utils.math.MouseUtil;
import java.awt.Color;
import net.minecraft.class_4587;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.render.ScissorUtil;
import java.time.Duration;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import net.minecraft.class_332;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import java.util.Map;
import java.util.List;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.client.ui.clickgui.module.ExpandableComponent;

public class ModeComponent extends ExpandableComponent.ExpandableSettingComponent
{
    private final ModeSetting setting;
    private final List<Bound> bounds;
    private final Map<String, AnimationUtil> modeAnimations;
    
    public ModeComponent(final ModeSetting setting) {
        super(setting);
        this.bounds = new ArrayList<Bound>();
        this.modeAnimations = new HashMap<String, AnimationUtil>();
        this.setting = setting;
        this.updateHeight(this.getDefaultHeight());
        for (final String mode : setting.getModes()) {
            this.modeAnimations.put(mode, new AnimationUtil());
        }
    }
    
    @Override
    public void render(final class_332 context, final int mouseX, final int mouseY, final float delta) {
        final class_4587 matrixStack = context.method_51448();
        this.updateOpen();
        final float fontSize = this.getDefaultHeight() * this.scaled(0.45f);
        final float scd = this.scaled(this.getDefaultHeight());
        final float zavoz = this.offset();
        final float anim = this.getValue();
        final String valueText = this.setting.getValue();
        final String name = this.setting.getName();
        final float valueWidth = Fonts.PS_MEDIUM.getWidth(valueText, fontSize);
        final float nameWidth = Fonts.PS_BOLD.getWidth(name, fontSize);
        final float nameX = (this.getWidth() / 2.0f - nameWidth / 2.0f) * anim + zavoz * (1.0f - anim);
        int fullAlpha = (int)(this.getAlpha() * 255.0f);
        RenderUtil.BLUR_RECT.draw(matrixStack, this.getX(), this.getY(), this.getWidth(), this.getHeight(), this.getWidth() * 0.04f, UIColors.backgroundBlur(fullAlpha));
        Fonts.PS_BOLD.drawWrap(matrixStack, name, this.getX() + nameX, this.getY() + scd / 2.0f - fontSize / 2.0f, this.getWidth() - zavoz * 2.0f - valueWidth * (1.0f - anim), fontSize, UIColors.textColor(fullAlpha), this.scaled(16.0f), Duration.ofMillis(3000L), Duration.ofMillis(500L));
        ScissorUtil.start(matrixStack, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        Fonts.PS_MEDIUM.drawText(matrixStack, valueText, this.getX() + this.getWidth() - zavoz - valueWidth * (1.0f - anim), this.getY() + scd / 2.0f - fontSize / 2.0f, fontSize, UIColors.primary((int)((1.0f - anim) * this.getAlpha() * 255.0f)));
        if (anim > 0.0) {
            final float bY = -this.scaled(2.0f) * (1.0f - anim);
            this.bounds.clear();
            float currentX;
            final float defX = currentX = this.getX() + zavoz;
            float currentY = this.getY() + scd + bY;
            final float tileSize = fontSize * 0.9f;
            final float tileHeight = tileSize * 1.8f;
            final float tilePadding = this.gap();
            fullAlpha = (int)(this.getAlpha() * anim * 255.0f);
            RenderUtil.OTHER.scaleStart(matrixStack, this.getX() + this.getWidth() / 2.0f, this.getY() + this.getDefaultHeight() + this.getHeight() / 2.0f - bY, 0.95f + 0.05f * anim);
            for (final String mode : this.setting.getModes()) {
                final AnimationUtil modeAnim = this.modeAnimations.get(mode);
                modeAnim.update();
                modeAnim.run(this.setting.is(mode) ? 1.0 : 0.0, 500L, Easing.EXPO_OUT);
                final float textWidth = Fonts.PS_MEDIUM.getWidth(mode, tileSize);
                final float tileWidth = textWidth + tileSize;
                if (currentX + tileWidth + tilePadding > this.getX() + this.getWidth()) {
                    currentX = defX;
                    currentY += tileHeight + tilePadding;
                }
                this.bounds.add(new Bound(currentX, currentY, tileWidth, tileHeight, mode));
                final Color rectColor = ColorUtil.setAlpha(ColorUtil.interpolate(UIColors.primary(), UIColors.widgetBlur(), modeAnim.getValue()), fullAlpha);
                RenderUtil.BLUR_RECT.draw(matrixStack, currentX, currentY, tileWidth, tileHeight, tileHeight * 0.2f, rectColor);
                Fonts.PS_MEDIUM.drawCenteredText(matrixStack, mode, currentX + tileWidth / 2.0f, currentY + tileHeight / 2.0f - tileSize / 2.0f, tileSize, UIColors.textColor(fullAlpha));
                currentX += tileWidth + tilePadding;
            }
            RenderUtil.OTHER.scaleStop(matrixStack);
            final float total = (currentY - this.getY() + tileHeight) * anim;
            final float impotentMan = Math.max(total, scd + tileHeight * anim);
            final float jopa = this.gap() * (anim * 2.0f);
            this.setHeight(impotentMan + jopa);
        }
        else {
            this.updateHeight(this.getDefaultHeight());
        }
        ScissorUtil.stop(matrixStack);
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
        for (final Bound bound : this.bounds) {
            if (MouseUtil.isHovered(mouseX, mouseY, bound.x, bound.y, bound.width, bound.height)) {
                this.setting.setValue(bound.value);
            }
        }
    }
    
    private float getDefaultHeight() {
        return 15.0f;
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
    
    record Bound(float x, float y, float width, float height, String value) {}
}
