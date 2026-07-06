// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import org.joml.Matrix4f;
import net.minecraft.class_156;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Unique;
import sweetie.leonware.client.features.modules.render.EnchantColorModule;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mutable;
import net.minecraft.class_4668;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_4668.class })
public class MixinRenderPhase
{
    @Mutable
    @Shadow
    @Final
    protected static class_4668.class_4684 field_21381;
    @Mutable
    @Shadow
    @Final
    protected static class_4668.class_4684 field_21382;
    
    @Inject(method = { "<clinit>" }, at = { @At("TAIL") })
    private static void modifyInitTexturing(final CallbackInfo ci) {
        MixinRenderPhase.field_21381 = new class_4668.class_4684("glint_texturing", () -> {
            setupCustomGlint(8.0f);
            applyGlintColor();
            return;
        }, () -> {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.resetTextureMatrix();
            return;
        });
        MixinRenderPhase.field_21382 = new class_4668.class_4684("entity_glint_texturing", () -> {
            setupCustomGlint(0.16f);
            applyGlintColor();
        }, () -> {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.resetTextureMatrix();
        });
    }
    
    @Unique
    private static void applyGlintColor() {
        final EnchantColorModule mod = EnchantColorModule.getInstance();
        if (mod.isEnabled()) {
            final float[] clr = EnchantColorModule.getEnchantColor();
            RenderSystem.setShaderColor(clr[0], clr[1], clr[2], 1.0f);
            RenderSystem.setShaderGlintAlpha(clr[3]);
        }
        else {
            RenderSystem.setShaderColor(0.64705884f, 0.0f, 1.0f, 1.0f);
        }
    }
    
    @Unique
    private static void setupCustomGlint(float scale) {
        final EnchantColorModule mod = EnchantColorModule.getInstance();
        final class_310 mc = class_310.method_1551();
        if (mod.isEnabled()) {
            scale /= mod.enchantSize.getValue();
        }
        final double speedMod = (double)(mod.isEnabled() ? mod.enchantSpeed.getValue() : 1.0);
        final long time = (long)(class_156.method_658() * ((double)mc.field_1690.method_48580().method_41753() * speedMod) * 8.0);
        final float f = time % 110000L / 110000.0f;
        final float g = time % 30000L / 30000.0f;
        final Matrix4f matrix = new Matrix4f().translation(-f, g, 0.0f);
        matrix.rotateZ(0.17453292f).scale(scale);
        RenderSystem.setTextureMatrix(matrix);
    }
}
