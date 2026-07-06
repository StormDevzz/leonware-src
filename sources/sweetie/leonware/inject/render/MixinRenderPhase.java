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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/render/MixinRenderPhase.class */
@Mixin({class_4668.class})
public class MixinRenderPhase {

    @Mutable
    @Shadow
    @Final
    protected static class_4668.class_4684 field_21381;

    @Mutable
    @Shadow
    @Final
    protected static class_4668.class_4684 field_21382;

    @Inject(method = {"<clinit>"}, at = {@At("TAIL")})
    private static void modifyInitTexturing(CallbackInfo ci) {
        field_21381 = new class_4668.class_4684("glint_texturing", () -> {
            setupCustomGlint(8.0f);
            applyGlintColor();
        }, () -> {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.resetTextureMatrix();
        });
        field_21382 = new class_4668.class_4684("entity_glint_texturing", () -> {
            setupCustomGlint(0.16f);
            applyGlintColor();
        }, () -> {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.resetTextureMatrix();
        });
    }

    @Unique
    private static void applyGlintColor() {
        EnchantColorModule mod = EnchantColorModule.getInstance();
        if (mod.isEnabled()) {
            float[] clr = EnchantColorModule.getEnchantColor();
            RenderSystem.setShaderColor(clr[0], clr[1], clr[2], 1.0f);
            RenderSystem.setShaderGlintAlpha(clr[3]);
            return;
        }
        RenderSystem.setShaderColor(0.64705884f, 0.0f, 1.0f, 1.0f);
    }

    @Unique
    private static void setupCustomGlint(float scale) {
        EnchantColorModule mod = EnchantColorModule.getInstance();
        class_310 mc = class_310.method_1551();
        if (mod.isEnabled()) {
            scale /= mod.enchantSize.getValue().floatValue();
        }
        double speedMod = mod.isEnabled() ? mod.enchantSpeed.getValue().floatValue() : 1.0d;
        long time = (long) (class_156.method_658() * ((Double) mc.field_1690.method_48580().method_41753()).doubleValue() * speedMod * 8.0d);
        float f = (time % 110000) / 110000.0f;
        float g = (time % 30000) / 30000.0f;
        Matrix4f matrix = new Matrix4f().translation(-f, g, 0.0f);
        matrix.rotateZ(0.17453292f).scale(scale);
        RenderSystem.setTextureMatrix(matrix);
    }
}
