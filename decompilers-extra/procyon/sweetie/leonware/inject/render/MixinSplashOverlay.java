// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sweetie.leonware.LeonWare;
import net.minecraft.class_1921;
import net.minecraft.class_332;
import java.awt.Color;
import org.spongepowered.asm.mixin.Unique;
import net.minecraft.class_425;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_425.class })
public class MixinSplashOverlay
{
    @Unique
    private float leonware$progress;
    
    public MixinSplashOverlay() {
        this.leonware$progress = 0.0f;
    }
    
    @Unique
    private int leonware$getRainbowColor(final int alpha) {
        this.leonware$progress += 0.005f;
        if (this.leonware$progress > 1.0f) {
            this.leonware$progress = 0.0f;
        }
        final int rgb = Color.HSBtoRGB(this.leonware$progress, 0.8f, 1.0f);
        return alpha << 24 | (rgb & 0xFFFFFF);
    }
    
    @Redirect(method = { "render" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(Lnet/minecraft/client/render/RenderLayer;IIIII)V"))
    private void redirectFill(final class_332 instance, final class_1921 layer, final int x1, final int y1, final int x2, final int y2, final int color) {
        if (LeonWare.isUnhooked) {
            instance.method_51739(layer, x1, y1, x2, y2, color);
            return;
        }
        final int alpha = color >> 24 & 0xFF;
        final int rainbowColor = this.leonware$getRainbowColor(alpha);
        instance.method_51739(layer, x1, y1, x2, y2, rainbowColor);
    }
}
