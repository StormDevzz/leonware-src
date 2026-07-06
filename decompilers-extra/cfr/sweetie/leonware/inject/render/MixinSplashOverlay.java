/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1921
 *  net.minecraft.class_332
 *  net.minecraft.class_425
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
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

@Mixin(value={class_425.class})
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
        return alpha << 24 | rgb & 0xFFFFFF;
    }

    @Redirect(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/DrawContext;fill(Lnet/minecraft/client/render/RenderLayer;IIIII)V"))
    private void redirectFill(class_332 instance, class_1921 layer, int x1, int y1, int x2, int y2, int color) {
        if (LeonWare.isUnhooked) {
            instance.method_51739(layer, x1, y1, x2, y2, color);
            return;
        }
        int alpha = color >> 24 & 0xFF;
        int rainbowColor = this.leonware$getRainbowColor(alpha);
        instance.method_51739(layer, x1, y1, x2, y2, rainbowColor);
    }
}

