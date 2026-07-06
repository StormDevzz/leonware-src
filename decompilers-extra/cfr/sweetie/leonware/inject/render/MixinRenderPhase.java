/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.class_156
 *  net.minecraft.class_310
 *  net.minecraft.class_4668
 *  net.minecraft.class_4668$class_4684
 *  org.joml.Matrix4f
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package sweetie.leonware.inject.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_156;
import net.minecraft.class_310;
import net.minecraft.class_4668;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.client.features.modules.render.EnchantColorModule;

@Mixin(value={class_4668.class})
public class MixinRenderPhase {
    @Mutable
    @Shadow
    @Final
    protected static class_4668.class_4684 field_21381;
    @Mutable
    @Shadow
    @Final
    protected static class_4668.class_4684 field_21382;

    @Inject(method={"<clinit>"}, at={@At(value="TAIL")})
    private static void modifyInitTexturing(CallbackInfo ci) {
        field_21381 = new class_4668.class_4684("glint_texturing", () -> {
            MixinRenderPhase.setupCustomGlint(8.0f);
            MixinRenderPhase.applyGlintColor();
        }, () -> {
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            RenderSystem.resetTextureMatrix();
        });
        field_21382 = new class_4668.class_4684("entity_glint_texturing", () -> {
            MixinRenderPhase.setupCustomGlint(0.16f);
            MixinRenderPhase.applyGlintColor();
        }, () -> {
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            RenderSystem.resetTextureMatrix();
        });
    }

    @Unique
    private static void applyGlintColor() {
        EnchantColorModule mod = EnchantColorModule.getInstance();
        if (mod.isEnabled()) {
            float[] clr = EnchantColorModule.getEnchantColor();
            RenderSystem.setShaderColor((float)clr[0], (float)clr[1], (float)clr[2], (float)1.0f);
            RenderSystem.setShaderGlintAlpha((float)clr[3]);
        } else {
            RenderSystem.setShaderColor((float)0.64705884f, (float)0.0f, (float)1.0f, (float)1.0f);
        }
    }

    @Unique
    private static void setupCustomGlint(float scale) {
        EnchantColorModule mod = EnchantColorModule.getInstance();
        class_310 mc = class_310.method_1551();
        if (mod.isEnabled()) {
            scale /= ((Float)mod.enchantSize.getValue()).floatValue();
        }
        double speedMod = mod.isEnabled() ? (double)((Float)mod.enchantSpeed.getValue()).floatValue() : 1.0;
        long time = (long)((double)class_156.method_658() * ((Double)mc.field_1690.method_48580().method_41753() * speedMod) * 8.0);
        float f = (float)(time % 110000L) / 110000.0f;
        float g = (float)(time % 30000L) / 30000.0f;
        Matrix4f matrix = new Matrix4f().translation(-f, g, 0.0f);
        matrix.rotateZ(0.17453292f).scale(scale);
        RenderSystem.setTextureMatrix((Matrix4f)matrix);
    }
}

