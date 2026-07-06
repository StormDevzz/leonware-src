/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_332
 *  net.minecraft.class_4587
 */
package sweetie.leonware.client.ui.clickgui.module.settings;

import java.awt.Color;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.ScissorUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.client.ui.clickgui.module.ExpandableComponent;

public class ModeComponent
extends ExpandableComponent.ExpandableSettingComponent {
    private final ModeSetting setting;
    private final List<Bound> bounds = new ArrayList<Bound>();
    private final Map<String, AnimationUtil> modeAnimations = new HashMap<String, AnimationUtil>();

    public ModeComponent(ModeSetting setting) {
        super(setting);
        this.setting = setting;
        this.updateHeight(this.getDefaultHeight());
        for (String mode : setting.getModes()) {
            this.modeAnimations.put(mode, new AnimationUtil());
        }
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        class_4587 matrixStack = context.method_51448();
        this.updateOpen();
        float fontSize = this.getDefaultHeight() * this.scaled(0.45f);
        float scd = this.scaled(this.getDefaultHeight());
        float zavoz = this.offset();
        float anim = this.getValue();
        String valueText = (String)this.setting.getValue();
        String name = this.setting.getName();
        float valueWidth = Fonts.PS_MEDIUM.getWidth(valueText, fontSize);
        float nameWidth = Fonts.PS_BOLD.getWidth(name, fontSize);
        float nameX = (this.getWidth() / 2.0f - nameWidth / 2.0f) * anim + zavoz * (1.0f - anim);
        int fullAlpha = (int)(this.getAlpha() * 255.0f);
        RenderUtil.BLUR_RECT.draw(matrixStack, this.getX(), this.getY(), this.getWidth(), this.getHeight(), this.getWidth() * 0.04f, UIColors.backgroundBlur(fullAlpha));
        Fonts.PS_BOLD.drawWrap(matrixStack, name, this.getX() + nameX, this.getY() + scd / 2.0f - fontSize / 2.0f, this.getWidth() - zavoz * 2.0f - valueWidth * (1.0f - anim), fontSize, UIColors.textColor(fullAlpha), this.scaled(16.0f), Duration.ofMillis(3000L), Duration.ofMillis(500L));
        ScissorUtil.start(matrixStack, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        Fonts.PS_MEDIUM.drawText(matrixStack, valueText, this.getX() + this.getWidth() - zavoz - valueWidth * (1.0f - anim), this.getY() + scd / 2.0f - fontSize / 2.0f, fontSize, UIColors.primary((int)((1.0f - anim) * this.getAlpha() * 255.0f)));
        if ((double)anim > 0.0) {
            float defX;
            float bY = -this.scaled(2.0f) * (1.0f - anim);
            this.bounds.clear();
            float currentX = defX = this.getX() + zavoz;
            float currentY = this.getY() + scd + bY;
            float tileSize = fontSize * 0.9f;
            float tileHeight = tileSize * 1.8f;
            float tilePadding = this.gap();
            fullAlpha = (int)(this.getAlpha() * anim * 255.0f);
            RenderUtil.OTHER.scaleStart(matrixStack, this.getX() + this.getWidth() / 2.0f, this.getY() + this.getDefaultHeight() + this.getHeight() / 2.0f - bY, 0.95f + 0.05f * anim);
            for (String mode : this.setting.getModes()) {
                AnimationUtil modeAnim = this.modeAnimations.get(mode);
                modeAnim.update();
                modeAnim.run(this.setting.is(mode) ? 1.0 : 0.0, 500L, Easing.EXPO_OUT);
                float textWidth = Fonts.PS_MEDIUM.getWidth(mode, tileSize);
                float tileWidth = textWidth + tileSize;
                if (currentX + tileWidth + tilePadding > this.getX() + this.getWidth()) {
                    currentX = defX;
                    currentY += tileHeight + tilePadding;
                }
                this.bounds.add(new Bound(currentX, currentY, tileWidth, tileHeight, mode));
                Color rectColor = ColorUtil.setAlpha(ColorUtil.interpolate(UIColors.primary(), UIColors.widgetBlur(), modeAnim.getValue()), fullAlpha);
                RenderUtil.BLUR_RECT.draw(matrixStack, currentX, currentY, tileWidth, tileHeight, tileHeight * 0.2f, rectColor);
                Fonts.PS_MEDIUM.drawCenteredText(matrixStack, mode, currentX + tileWidth / 2.0f, currentY + tileHeight / 2.0f - tileSize / 2.0f, tileSize, UIColors.textColor(fullAlpha));
                currentX += tileWidth + tilePadding;
            }
            RenderUtil.OTHER.scaleStop(matrixStack);
            float total = (currentY - this.getY() + tileHeight) * anim;
            float impotentMan = Math.max(total, scd + tileHeight * anim);
            float jopa = this.gap() * (anim * 2.0f);
            this.setHeight(impotentMan + jopa);
        } else {
            this.updateHeight(this.getDefaultHeight());
        }
        ScissorUtil.stop(matrixStack);
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
        for (Bound bound : this.bounds) {
            if (!MouseUtil.isHovered(mouseX, mouseY, (double)bound.x, (double)bound.y, (double)bound.width, (double)bound.height)) continue;
            this.setting.setValue(bound.value);
        }
    }

    private float getDefaultHeight() {
        return 15.0f;
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

    private record Bound(float x, float y, float width, float height, String value) {
    }
}

