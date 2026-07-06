package sweetie.leonware.client.ui.widget.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_2371;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_408;
import net.minecraft.class_4587;
import net.minecraft.class_490;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.ScissorUtil;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.client.ui.widget.Widget;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/overlay/TargetInfoWidget.class */
public class TargetInfoWidget extends Widget {
    public final ModeSetting mode;
    private final AnimationUtil showAnimation;
    private float healthAnimation;
    private float healthBarWidth;
    private float damageAnimation;
    private class_1309 target;
    private final List<class_1799> armorBuffer;
    private final Vector3f reusedVec3;
    private final Quaternionf reusedQuatZ;
    private final Quaternionf reusedQuatX;
    private final Color[] hpGradientCache;
    private class_332 sharedDrawContext;

    public TargetInfoWidget() {
        super(30.0f, 30.0f);
        this.mode = new ModeSetting("Target Hud").values("Обычный", "Обычный+", "Новый", "Sk3d").value("Обычный");
        this.showAnimation = new AnimationUtil();
        this.healthAnimation = 0.0f;
        this.healthBarWidth = 0.0f;
        this.damageAnimation = 0.0f;
        this.armorBuffer = new ArrayList(6);
        this.reusedVec3 = new Vector3f();
        this.reusedQuatZ = new Quaternionf();
        this.reusedQuatX = new Quaternionf();
        this.hpGradientCache = new Color[2];
    }

    @Override // sweetie.leonware.client.ui.widget.Widget
    public String getName() {
        return "Target info";
    }

    private class_332 getOrCreateDrawContext() {
        if (this.sharedDrawContext == null) {
            this.sharedDrawContext = new class_332(mc, mc.method_22940().method_23000());
        }
        return this.sharedDrawContext;
    }

    private Color[] getHealthGradient(float healthPct, int alpha) {
        Color first;
        Color second;
        if (healthPct > 0.6f) {
            first = new Color(0, 255, 100, alpha);
            second = new Color(0, 200, 50, alpha);
        } else if (healthPct > 0.25f) {
            first = new Color(255, 255, 0, alpha);
            second = new Color(200, 200, 0, alpha);
        } else {
            first = new Color(255, 0, 50, alpha);
            second = new Color(180, 0, 20, alpha);
        }
        this.hpGradientCache[0] = first;
        this.hpGradientCache[1] = second;
        return this.hpGradientCache;
    }

    @Override // sweetie.leonware.api.system.interfaces.IRenderer
    public void render(class_4587 matrixStack) {
        update();
        class_1309 pretendTarget = getTarget();
        if (pretendTarget != null) {
            this.target = pretendTarget;
        }
        if (this.showAnimation.getValue() < 0.01d || this.target == null) {
            return;
        }
        float anim = (float) this.showAnimation.getValue();
        int fullAlpha = (int) (anim * 255.0f);
        float x = getDraggable().getX();
        float y = getDraggable().getY();
        float currentHealth = class_3532.method_15363(this.target.method_6032() + this.target.method_6067(), 0.0f, this.target.method_6063());
        float healthPct = currentHealth / this.target.method_6063();
        if (this.mode.is("Новый")) {
            renderModern(matrixStack, x, y, anim, fullAlpha, currentHealth, healthPct);
        } else if (this.mode.is("Sk3d")) {
            renderSk3d(matrixStack, x, y, anim, fullAlpha, currentHealth, healthPct);
        } else {
            renderDefault(matrixStack, x, y, anim, fullAlpha, currentHealth, healthPct, this.mode.is("Обычный+"));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0266  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void renderSk3d(net.minecraft.class_4587 r16, float r17, float r18, float r19, int r20, float r21, float r22) {
        /*
            Method dump skipped, instruction units count: 696
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: sweetie.leonware.client.ui.widget.overlay.TargetInfoWidget.renderSk3d(net.minecraft.class_4587, float, float, float, int, float, float):void");
    }

    private void renderDefault(class_4587 matrixStack, float x, float y, float anim, int fullAlpha, float currentHealth, float healthPct, boolean isPlus) {
        float width;
        float barWidth;
        this.healthAnimation = class_3532.method_15363(MathUtil.interpolate(this.healthAnimation, healthPct, 0.3f), 0.0f, 1.0f);
        float headSize = scaled(25.0f);
        float margin = getGap() * 2.0f;
        String targetName = this.target.method_5477().getString();
        String healthText = isPlus ? String.format("%.1f", Float.valueOf(currentHealth)) : String.format("%.1f", Float.valueOf(currentHealth)) + "HP";
        float bigFontSize = headSize * 0.35f;
        float smallFontSize = headSize * 0.4f * 0.7f;
        float healthTextWidth = getMediumFont().getWidth(healthText, isPlus ? bigFontSize : smallFontSize);
        float nameWidth = getMediumFont().getWidth(targetName, bigFontSize);
        if (isPlus) {
            float gap = scaled(2.0f);
            float minBarWidth = scaled(40.0f);
            float neededBarWidth = nameWidth + healthTextWidth + gap;
            barWidth = Math.max(minBarWidth, neededBarWidth);
            width = headSize + (margin * 2.5f) + barWidth;
        } else {
            width = (headSize * 3.7f) + (margin * 2.0f);
            barWidth = ((width - (margin * 2.5f)) - headSize) - healthTextWidth;
        }
        float height = headSize + (getGap() * 2.0f);
        float backgroundRound = getGap() * 3.0f * 0.7f;
        RenderUtil.BLUR_RECT.draw(matrixStack, x, y, width, height, backgroundRound, UIColors.widgetBlur(fullAlpha));
        ScissorUtil.start(matrixStack, x, y, width, height);
        float barHeight = scaled(5.0f);
        float barRound = barHeight * 0.3f;
        float barY = ((y + height) - barHeight) - margin;
        float barX = x + headSize + margin;
        float nameY = ((y + margin) + (Math.abs((y + margin) - (barY - (margin / 2.0f))) / 2.0f)) - (bigFontSize / 2.0f);
        Color textColor = UIColors.textColor(fullAlpha);
        Color[] hpColors = getHealthGradient(healthPct, fullAlpha);
        Color c1 = UIColors.gradient(0, fullAlpha);
        Color c2 = UIColors.gradient(90, fullAlpha);
        Color barBg = UIColors.backgroundBlur(fullAlpha);
        if (isPlus) {
            getMediumFont().drawText(matrixStack, targetName, barX, nameY, bigFontSize, textColor);
            getMediumFont().drawGradientText(matrixStack, healthText, (barX + barWidth) - healthTextWidth, nameY, bigFontSize, hpColors[0], hpColors[1], 10.0f);
            RenderUtil.RECT.draw(matrixStack, barX, barY, barWidth, barHeight, barRound, barBg);
            RenderUtil.GRADIENT_RECT.draw(matrixStack, barX, barY, barWidth * this.healthAnimation, barHeight, barRound, c1, c2, c1, c2);
        } else {
            getMediumFont().drawWrap(matrixStack, targetName, barX, nameY, (width - headSize) - margin, bigFontSize, textColor, scaled(9.0f), Duration.ofMillis(2500L), Duration.ofMillis(1700L));
            getMediumFont().drawGradientText(matrixStack, healthText, ((x + width) - healthTextWidth) - margin, barY - (Math.abs(smallFontSize - barHeight) / 2.0f), smallFontSize, hpColors[0], hpColors[1], 10.0f);
            RenderUtil.RECT.draw(matrixStack, barX, barY, barWidth, barHeight, barRound, barBg);
            RenderUtil.GRADIENT_RECT.draw(matrixStack, barX, barY, barWidth * this.healthAnimation, barHeight, barRound, c1, c2, c1, c2);
        }
        class_1657 class_1657Var = this.target;
        if (class_1657Var instanceof class_1657) {
            class_1657 player = class_1657Var;
            RenderUtil.TEXTURE_RECT.drawHead(matrixStack, player, x + getGap(), y + getGap(), headSize, headSize, getGap() / 2.0f, 0.0f, ColorUtil.setAlpha(Color.WHITE, fullAlpha));
        } else {
            getSemiBoldFont().drawCenteredText(matrixStack, "?", x + getGap() + (headSize / 2.0f), ((y + getGap()) + (headSize / 2.0f)) - ((headSize * 0.8f) / 2.0f), headSize * 0.8f, UIColors.textColor(fullAlpha));
        }
        ScissorUtil.stop(matrixStack);
        getDraggable().setWidth(width);
        getDraggable().setHeight(height);
    }

    private void renderModern(class_4587 matrixStack, float x, float y, float anim, int fullAlpha, float currentHealth, float healthPct) {
        this.healthAnimation = MathUtil.interpolate(this.healthAnimation, healthPct, 0.2f);
        this.damageAnimation = MathUtil.interpolate(this.damageAnimation, healthPct, 0.05f);
        RenderUtil.BLUR_RECT.draw(matrixStack, x, y, 135.0f, 44.0f, 5.0f, UIColors.widgetBlur(fullAlpha));
        RenderUtil.RECT.draw(matrixStack, x, y, 135.0f, 44.0f, 5.0f, new Color(20, 20, 20, (int) (180.0f * anim)));
        ScissorUtil.start(matrixStack, x, y, 135.0f, 44.0f);
        if (fullAlpha > 10) {
            drawTargetModel(matrixStack, x + 20.0f, y + 36.0f, 17, this.target);
        }
        float textX = x + 42.0f;
        float textY = y + 5.0f;
        getSemiBoldFont().drawText(matrixStack, this.target.method_5477().getString(), textX, textY, 8.5f, UIColors.textColor(fullAlpha));
        getMediumFont().drawText(matrixStack, "Дист: " + String.format("%.1f", Float.valueOf(mc.field_1724.method_5739(this.target))), textX, textY + 9.0f, 6.0f, new Color(180, 180, 180, fullAlpha));
        String hpString = String.format("%.1f", Float.valueOf(currentHealth));
        float hpWidth = getSemiBoldFont().getWidth(hpString, 8.0f);
        Color[] hpColors = getHealthGradient(healthPct, fullAlpha);
        getSemiBoldFont().drawGradientText(matrixStack, hpString, ((x + 135.0f) - hpWidth) - 5.0f, textY + 1.0f, 8.0f, hpColors[0], hpColors[1], 10.0f);
        float barY = y + 21.0f;
        float barWidth = (135.0f - 42.0f) - 5.0f;
        RenderUtil.RECT.draw(matrixStack, textX, barY, barWidth, 6.5f, 2.5f, new Color(40, 40, 40, fullAlpha));
        if (this.damageAnimation > this.healthAnimation) {
            RenderUtil.RECT.draw(matrixStack, textX, barY, barWidth * this.damageAnimation, 6.5f, 2.5f, new Color(255, 255, 255, (int) (150.0f * anim)));
        }
        Color c1 = UIColors.gradient(0, fullAlpha);
        Color c2 = UIColors.gradient(90, fullAlpha);
        RenderUtil.GRADIENT_RECT.draw(matrixStack, textX, barY, barWidth * this.healthAnimation, 6.5f, 2.5f, c1, c2, c1, c2);
        class_1309 class_1309Var = this.target;
        if (class_1309Var instanceof class_1657) {
            class_1657 player = (class_1657) class_1309Var;
            if (anim > 0.1f) {
                renderArmor(matrixStack, player, textX, barY + 9.0f, fullAlpha);
            }
        }
        ScissorUtil.stop(matrixStack);
        getDraggable().setWidth(135.0f);
        getDraggable().setHeight(44.0f);
    }

    private void renderArmor(class_4587 matrices, class_1657 player, float x, float y, int alpha) {
        this.armorBuffer.clear();
        class_2371 class_2371Var = player.method_31548().field_7548;
        for (int i = class_2371Var.size() - 1; i >= 0; i--) {
            this.armorBuffer.add((class_1799) class_2371Var.get(i));
        }
        this.armorBuffer.add(player.method_6047());
        this.armorBuffer.add(player.method_6079());
        class_332 context = getOrCreateDrawContext();
        float itemX = x;
        int n = this.armorBuffer.size();
        for (int i2 = 0; i2 < n; i2++) {
            class_1799 stack = this.armorBuffer.get(i2);
            if (!stack.method_7960()) {
                matrices.method_22903();
                matrices.method_46416(itemX, y, 0.0f);
                matrices.method_22905(0.65f, 0.65f, 1.0f);
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha / 255.0f);
                context.method_51448().method_22903();
                context.method_51448().method_23760().method_23761().set(matrices.method_23760().method_23761());
                context.method_51427(stack, 0, 0);
                context.method_51431(mc.field_1772, stack, 0, 0);
                context.method_51448().method_22909();
                context.method_51452();
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                matrices.method_22909();
                itemX += 11.0f;
            }
        }
    }

    private void drawTargetModel(class_4587 matrices, float x, float y, int size, class_1309 entity) {
        this.reusedQuatZ.identity().rotateZ(3.1415927f).rotateX(-0.5235988f);
        float bodyYaw = entity.field_6283;
        float prevBodyYaw = entity.field_6220;
        float headYaw = entity.field_6241;
        float prevHeadYaw = entity.field_6259;
        float yaw = entity.method_36454();
        float prevYaw = entity.field_5982;
        float pitch = entity.method_36455();
        float prevPitch = entity.field_6004;
        entity.field_6283 = 180.0f;
        entity.field_6220 = 180.0f;
        entity.field_6241 = 180.0f;
        entity.field_6259 = 180.0f;
        entity.method_36456(180.0f);
        entity.field_5982 = 180.0f;
        entity.method_36457(0.0f);
        entity.field_6004 = 0.0f;
        this.reusedVec3.set(0.0f, 0.0f, 0.0f);
        class_490.method_48472(getOrCreateDrawContext(), x, y, size, this.reusedVec3, this.reusedQuatZ, (Quaternionf) null, entity);
        entity.field_6283 = bodyYaw;
        entity.field_6220 = prevBodyYaw;
        entity.field_6241 = headYaw;
        entity.field_6259 = prevHeadYaw;
        entity.method_36456(yaw);
        entity.field_5982 = prevYaw;
        entity.method_36457(pitch);
        entity.field_6004 = prevPitch;
    }

    private void update() {
        this.showAnimation.update();
        this.showAnimation.run(getTarget() != null ? 1.0d : 0.0d, getDuration(), getEasing());
    }

    private class_1309 getTarget() {
        AuraModule aura = AuraModule.getInstance();
        if (aura.isEnabled() && aura.target != null && aura.target.method_5805() && !aura.target.method_31481()) {
            return aura.target;
        }
        if (mc.field_1755 instanceof class_408) {
            return mc.field_1724;
        }
        return null;
    }
}
