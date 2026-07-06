/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_1799
 *  net.minecraft.class_1921
 *  net.minecraft.class_2371
 *  net.minecraft.class_2960
 *  net.minecraft.class_332
 *  net.minecraft.class_3532
 *  net.minecraft.class_408
 *  net.minecraft.class_4587
 *  net.minecraft.class_490
 *  org.joml.Matrix4fc
 *  org.joml.Quaternionf
 *  org.joml.Vector3f
 */
package sweetie.leonware.client.ui.widget.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.lang.invoke.LambdaMetafactory;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1921;
import net.minecraft.class_2371;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_408;
import net.minecraft.class_4587;
import net.minecraft.class_490;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.ScissorUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.client.ui.widget.Widget;

public class TargetInfoWidget
extends Widget {
    public final ModeSetting mode = new ModeSetting("Target Hud").values("\u041e\u0431\u044b\u0447\u043d\u044b\u0439", "\u041e\u0431\u044b\u0447\u043d\u044b\u0439+", "\u041d\u043e\u0432\u044b\u0439", "Sk3d").value("\u041e\u0431\u044b\u0447\u043d\u044b\u0439");
    private final AnimationUtil showAnimation = new AnimationUtil();
    private float healthAnimation = 0.0f;
    private float healthBarWidth = 0.0f;
    private float damageAnimation = 0.0f;
    private class_1309 target;
    private final List<class_1799> armorBuffer = new ArrayList<class_1799>(6);
    private final Vector3f reusedVec3 = new Vector3f();
    private final Quaternionf reusedQuatZ = new Quaternionf();
    private final Quaternionf reusedQuatX = new Quaternionf();
    private final Color[] hpGradientCache = new Color[2];
    private class_332 sharedDrawContext;

    public TargetInfoWidget() {
        super(30.0f, 30.0f);
    }

    @Override
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
        Color second;
        Color first;
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

    @Override
    public void render(class_4587 matrixStack) {
        this.update();
        class_1309 pretendTarget = this.getTarget();
        if (pretendTarget != null) {
            this.target = pretendTarget;
        }
        if (this.showAnimation.getValue() < 0.01 || this.target == null) {
            return;
        }
        float anim = (float)this.showAnimation.getValue();
        int fullAlpha = (int)(anim * 255.0f);
        float x = this.getDraggable().getX();
        float y = this.getDraggable().getY();
        float currentHealth = class_3532.method_15363((float)(this.target.method_6032() + this.target.method_6067()), (float)0.0f, (float)this.target.method_6063());
        float healthPct = currentHealth / this.target.method_6063();
        if (this.mode.is("\u041d\u043e\u0432\u044b\u0439")) {
            this.renderModern(matrixStack, x, y, anim, fullAlpha, currentHealth, healthPct);
        } else if (this.mode.is("Sk3d")) {
            this.renderSk3d(matrixStack, x, y, anim, fullAlpha, currentHealth, healthPct);
        } else {
            this.renderDefault(matrixStack, x, y, anim, fullAlpha, currentHealth, healthPct, this.mode.is("\u041e\u0431\u044b\u0447\u043d\u044b\u0439+"));
        }
    }

    /*
     * Unable to fully structure code
     */
    private void renderSk3d(class_4587 matrixStack, float x, float y, float anim, int fullAlpha, float currentHealth, float healthPct) {
        width = this.scaled(172.5f);
        height = this.scaled(50.3f);
        targetBarW = healthPct * this.scaled(79.06f);
        targetBarW = class_3532.method_15363((float)targetBarW, (float)0.0f, (float)this.scaled(79.06f));
        this.healthBarWidth = MathUtil.interpolate(this.healthBarWidth, targetBarW, 0.15f);
        health = String.format("%.1f", new Object[]{Float.valueOf(currentHealth)});
        distance = String.format("%.1f", new Object[]{Float.valueOf(TargetInfoWidget.mc.field_1724.method_5739((class_1297)this.target))});
        bgAlpha = Math.min(255, (int)(195.0f * anim));
        RenderUtil.BLUR_RECT.draw(matrixStack, x + this.scaled(1.44f), y + this.scaled(25.88f), this.scaled(125.06f), this.scaled(36.66f), 0.0f, new Color(14, 14, 14, bgAlpha));
        RenderUtil.RECT.draw(matrixStack, x + this.scaled(36.66f), y + this.scaled(53.19f), this.scaled(87.69f), this.scaled(7.91f), 0.0f, new Color(7, 7, 7, (int)(66.0f * anim)));
        c1 = UIColors.gradient(0, fullAlpha);
        c2 = UIColors.gradient(90, fullAlpha);
        RenderUtil.GRADIENT_RECT.draw(matrixStack, x + this.scaled(37.38f), y + this.scaled(54.63f), this.scaled(7.19f) + this.healthBarWidth, this.scaled(5.75f), 0.0f, c1, c2, c1, c2);
        nameMaxWidth = this.scaled(87.69f);
        Fonts.PS_MEDIUM.drawWrap(matrixStack, this.target.method_5477().getString(), x + this.scaled(37.38f), y + this.scaled(29.33f), nameMaxWidth, this.scaled(10.06f), new Color(255, 255, 255, fullAlpha), this.scaled(9.0f), Duration.ofMillis(2500L), Duration.ofMillis(1700L));
        Fonts.PS_MEDIUM.drawText(matrixStack, health + " HP - " + distance + "m", x + this.scaled(37.38f), y + this.scaled(42.55f), this.scaled(7.91f), new Color(255, 255, 255, fullAlpha));
        var18_17 = this.target;
        if (!(var18_17 instanceof class_1657)) ** GOTO lbl-1000
        player = (class_1657)var18_17;
        if (anim > 0.1f) {
            hurtPercent = class_3532.method_15363((float)((float)player.field_6235 / 10.0f), (float)0.0f, (float)1.0f);
            ctx = this.getOrCreateDrawContext();
            skin = TargetInfoWidget.mc.method_1582().method_52862(player.method_7334()).comp_1626();
            RenderSystem.setShaderColor((float)1.0f, (float)(1.0f - hurtPercent), (float)(1.0f - hurtPercent), (float)anim);
            ctx.method_25302((Function<class_2960, class_1921>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, method_62277(net.minecraft.class_2960 ), (Lnet/minecraft/class_2960;)Lnet/minecraft/class_1921;)(), skin, (int)(x + this.scaled(3.59f)), (int)(y + this.scaled(29.0f)), 8.0f, 8.0f, (int)this.scaled(31.63f), (int)this.scaled(31.63f), 8, 8, 64, 64);
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        } else if (!(this.target instanceof class_1657)) {
            Fonts.PS_MEDIUM.drawCenteredText(matrixStack, "?", x + this.scaled(18.69f), y + this.scaled(37.38f), this.scaled(14.38f), new Color(255, 255, 255, fullAlpha));
        }
        this.getDraggable().setWidth(width);
        this.getDraggable().setHeight(height);
    }

    private void renderDefault(class_4587 matrixStack, float x, float y, float anim, int fullAlpha, float currentHealth, float healthPct, boolean isPlus) {
        float width;
        float barWidth;
        this.healthAnimation = class_3532.method_15363((float)MathUtil.interpolate(this.healthAnimation, healthPct, 0.3f), (float)0.0f, (float)1.0f);
        float headSize = this.scaled(25.0f);
        float margin = this.getGap() * 2.0f;
        String targetName = this.target.method_5477().getString();
        Object healthText = isPlus ? String.format("%.1f", Float.valueOf(currentHealth)) : String.format("%.1f", Float.valueOf(currentHealth)) + "HP";
        float bigFontSize = headSize * 0.35f;
        float smallFontSize = headSize * 0.4f * 0.7f;
        float healthTextWidth = this.getMediumFont().getWidth((String)healthText, isPlus ? bigFontSize : smallFontSize);
        float nameWidth = this.getMediumFont().getWidth(targetName, bigFontSize);
        if (isPlus) {
            float gap = this.scaled(2.0f);
            float minBarWidth = this.scaled(40.0f);
            float neededBarWidth = nameWidth + healthTextWidth + gap;
            barWidth = Math.max(minBarWidth, neededBarWidth);
            width = headSize + margin * 2.5f + barWidth;
        } else {
            width = headSize * 3.7f + margin * 2.0f;
            barWidth = width - margin * 2.5f - headSize - healthTextWidth;
        }
        float height = headSize + this.getGap() * 2.0f;
        float backgroundRound = this.getGap() * 3.0f * 0.7f;
        RenderUtil.BLUR_RECT.draw(matrixStack, x, y, width, height, backgroundRound, UIColors.widgetBlur(fullAlpha));
        ScissorUtil.start(matrixStack, x, y, width, height);
        float barHeight = this.scaled(5.0f);
        float barRound = barHeight * 0.3f;
        float barY = y + height - barHeight - margin;
        float barX = x + headSize + margin;
        float nameY = y + margin + Math.abs(y + margin - (barY - margin / 2.0f)) / 2.0f - bigFontSize / 2.0f;
        Color textColor = UIColors.textColor(fullAlpha);
        Color[] hpColors = this.getHealthGradient(healthPct, fullAlpha);
        Color c1 = UIColors.gradient(0, fullAlpha);
        Color c2 = UIColors.gradient(90, fullAlpha);
        Color barBg = UIColors.backgroundBlur(fullAlpha);
        if (isPlus) {
            this.getMediumFont().drawText(matrixStack, targetName, barX, nameY, bigFontSize, textColor);
            this.getMediumFont().drawGradientText(matrixStack, (String)healthText, barX + barWidth - healthTextWidth, nameY, bigFontSize, hpColors[0], hpColors[1], 10.0f);
            RenderUtil.RECT.draw(matrixStack, barX, barY, barWidth, barHeight, barRound, barBg);
            RenderUtil.GRADIENT_RECT.draw(matrixStack, barX, barY, barWidth * this.healthAnimation, barHeight, barRound, c1, c2, c1, c2);
        } else {
            this.getMediumFont().drawWrap(matrixStack, targetName, barX, nameY, width - headSize - margin, bigFontSize, textColor, this.scaled(9.0f), Duration.ofMillis(2500L), Duration.ofMillis(1700L));
            this.getMediumFont().drawGradientText(matrixStack, (String)healthText, x + width - healthTextWidth - margin, barY - Math.abs(smallFontSize - barHeight) / 2.0f, smallFontSize, hpColors[0], hpColors[1], 10.0f);
            RenderUtil.RECT.draw(matrixStack, barX, barY, barWidth, barHeight, barRound, barBg);
            RenderUtil.GRADIENT_RECT.draw(matrixStack, barX, barY, barWidth * this.healthAnimation, barHeight, barRound, c1, c2, c1, c2);
        }
        class_1309 class_13092 = this.target;
        if (class_13092 instanceof class_1657) {
            class_1657 player = (class_1657)class_13092;
            RenderUtil.TEXTURE_RECT.drawHead(matrixStack, player, x + this.getGap(), y + this.getGap(), headSize, headSize, this.getGap() / 2.0f, 0.0f, ColorUtil.setAlpha(Color.WHITE, fullAlpha));
        } else {
            this.getSemiBoldFont().drawCenteredText(matrixStack, "?", x + this.getGap() + headSize / 2.0f, y + this.getGap() + headSize / 2.0f - headSize * 0.8f / 2.0f, headSize * 0.8f, UIColors.textColor(fullAlpha));
        }
        ScissorUtil.stop(matrixStack);
        this.getDraggable().setWidth(width);
        this.getDraggable().setHeight(height);
    }

    private void renderModern(class_4587 matrixStack, float x, float y, float anim, int fullAlpha, float currentHealth, float healthPct) {
        float width = 135.0f;
        float height = 44.0f;
        this.healthAnimation = MathUtil.interpolate(this.healthAnimation, healthPct, 0.2f);
        this.damageAnimation = MathUtil.interpolate(this.damageAnimation, healthPct, 0.05f);
        RenderUtil.BLUR_RECT.draw(matrixStack, x, y, width, height, 5.0f, UIColors.widgetBlur(fullAlpha));
        RenderUtil.RECT.draw(matrixStack, x, y, width, height, 5.0f, new Color(20, 20, 20, (int)(180.0f * anim)));
        ScissorUtil.start(matrixStack, x, y, width, height);
        if (fullAlpha > 10) {
            this.drawTargetModel(matrixStack, x + 20.0f, y + 36.0f, 17, this.target);
        }
        float textX = x + 42.0f;
        float textY = y + 5.0f;
        this.getSemiBoldFont().drawText(matrixStack, this.target.method_5477().getString(), textX, textY, 8.5f, UIColors.textColor(fullAlpha));
        this.getMediumFont().drawText(matrixStack, "\u0414\u0438\u0441\u0442: " + String.format("%.1f", Float.valueOf(TargetInfoWidget.mc.field_1724.method_5739((class_1297)this.target))), textX, textY + 9.0f, 6.0f, new Color(180, 180, 180, fullAlpha));
        String hpString = String.format("%.1f", Float.valueOf(currentHealth));
        float hpWidth = this.getSemiBoldFont().getWidth(hpString, 8.0f);
        Color[] hpColors = this.getHealthGradient(healthPct, fullAlpha);
        this.getSemiBoldFont().drawGradientText(matrixStack, hpString, x + width - hpWidth - 5.0f, textY + 1.0f, 8.0f, hpColors[0], hpColors[1], 10.0f);
        float barX = textX;
        float barY = y + 21.0f;
        float barWidth = width - 42.0f - 5.0f;
        RenderUtil.RECT.draw(matrixStack, barX, barY, barWidth, 6.5f, 2.5f, new Color(40, 40, 40, fullAlpha));
        if (this.damageAnimation > this.healthAnimation) {
            RenderUtil.RECT.draw(matrixStack, barX, barY, barWidth * this.damageAnimation, 6.5f, 2.5f, new Color(255, 255, 255, (int)(150.0f * anim)));
        }
        Color c1 = UIColors.gradient(0, fullAlpha);
        Color c2 = UIColors.gradient(90, fullAlpha);
        RenderUtil.GRADIENT_RECT.draw(matrixStack, barX, barY, barWidth * this.healthAnimation, 6.5f, 2.5f, c1, c2, c1, c2);
        class_1309 class_13092 = this.target;
        if (class_13092 instanceof class_1657) {
            class_1657 player = (class_1657)class_13092;
            if (anim > 0.1f) {
                this.renderArmor(matrixStack, player, barX, barY + 9.0f, fullAlpha);
            }
        }
        ScissorUtil.stop(matrixStack);
        this.getDraggable().setWidth(width);
        this.getDraggable().setHeight(height);
    }

    private void renderArmor(class_4587 matrices, class_1657 player, float x, float y, int alpha) {
        this.armorBuffer.clear();
        class_2371 armor = player.method_31548().field_7548;
        for (int i = armor.size() - 1; i >= 0; --i) {
            this.armorBuffer.add((class_1799)armor.get(i));
        }
        this.armorBuffer.add(player.method_6047());
        this.armorBuffer.add(player.method_6079());
        class_332 context = this.getOrCreateDrawContext();
        float itemX = x;
        int n = this.armorBuffer.size();
        for (int i = 0; i < n; ++i) {
            class_1799 stack = this.armorBuffer.get(i);
            if (stack.method_7960()) continue;
            matrices.method_22903();
            matrices.method_46416(itemX, y, 0.0f);
            matrices.method_22905(0.65f, 0.65f, 1.0f);
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)((float)alpha / 255.0f));
            context.method_51448().method_22903();
            context.method_51448().method_23760().method_23761().set((Matrix4fc)matrices.method_23760().method_23761());
            context.method_51427(stack, 0, 0);
            context.method_51431(TargetInfoWidget.mc.field_1772, stack, 0, 0);
            context.method_51448().method_22909();
            context.method_51452();
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            matrices.method_22909();
            itemX += 11.0f;
        }
    }

    private void drawTargetModel(class_4587 matrices, float x, float y, int size, class_1309 entity) {
        this.reusedQuatZ.identity().rotateZ((float)Math.PI).rotateX(-0.5235988f);
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
        class_490.method_48472((class_332)this.getOrCreateDrawContext(), (float)x, (float)y, (float)size, (Vector3f)this.reusedVec3, (Quaternionf)this.reusedQuatZ, null, (class_1309)entity);
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
        this.showAnimation.run(this.getTarget() != null ? 1.0 : 0.0, this.getDuration(), this.getEasing());
    }

    private class_1309 getTarget() {
        AuraModule aura = AuraModule.getInstance();
        if (aura.isEnabled() && aura.target != null && aura.target.method_5805() && !aura.target.method_31481()) {
            return aura.target;
        }
        if (TargetInfoWidget.mc.field_1755 instanceof class_408) {
            return TargetInfoWidget.mc.field_1724;
        }
        return null;
    }
}

