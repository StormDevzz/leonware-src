package sweetie.leonware.inject.render;

import java.awt.Color;
import net.minecraft.class_1921;
import net.minecraft.class_332;
import net.minecraft.class_425;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sweetie.leonware.LeonWare;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/render/MixinSplashOverlay.class */
@Mixin({class_425.class})
public class MixinSplashOverlay {

    @Unique
    private float leonware$progress = 0.0f;

    @Unique
    private int leonware$getRainbowColor(int alpha) {
        this.leonware$progress += 0.005f;
        if (this.leonware$progress > 1.0f) {
            this.leonware$progress = 0.0f;
        }
        int rgb = Color.HSBtoRGB(this.leonware$progress, 0.8f, 1.0f);
        return (alpha << 24) | (rgb & 16777215);
    }

    @Redirect(method = {"render"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(Lnet/minecraft/client/render/RenderLayer;IIIII)V"))
    private void redirectFill(class_332 instance, class_1921 layer, int x1, int y1, int x2, int y2, int color) {
        if (LeonWare.isUnhooked) {
            instance.method_51739(layer, x1, y1, x2, y2, color);
            return;
        }
        int alpha = (color >> 24) & 255;
        int rainbowColor = leonware$getRainbowColor(alpha);
        instance.method_51739(layer, x1, y1, x2, y2, rainbowColor);
    }
}
