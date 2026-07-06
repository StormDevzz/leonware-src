// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.render.display;

import net.minecraft.class_10149;
import sweetie.leonware.api.system.files.FileUtil;
import net.minecraft.class_287;
import net.minecraft.class_5944;
import org.joml.Matrix4f;
import net.minecraft.class_286;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_289;
import com.mojang.blaze3d.systems.RenderSystem;
import sweetie.leonware.api.utils.color.ColorUtil;
import org.joml.Vector4f;
import java.awt.Color;
import net.minecraft.class_4587;
import net.minecraft.class_10156;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class GlowRectRender implements QuickImports
{
    private static final class_10156 shaderKey;
    
    public void draw(final class_4587 matrices, final float x, final float y, final float width, final float height, final float radius, final Color color, final float glowSize, final float glowIntensity) {
        this.draw(matrices, x, y, width, height, new Vector4f(radius, radius, radius, radius), color, color, color, color, glowSize, glowIntensity, 1.0f);
    }
    
    public void draw(final class_4587 matrices, final float x, final float y, final float width, final float height, final Vector4f radius, final Color topLeft, final Color topRight, final Color bottomLeft, final Color bottomRight, final float glowSize, final float glowIntensity) {
        this.draw(matrices, x, y, width, height, radius, topLeft, topRight, bottomLeft, bottomRight, glowSize, glowIntensity, 1.0f);
    }
    
    public void draw(final class_4587 matrices, final float x, final float y, final float width, final float height, final Vector4f radius, final Color topLeft, final Color topRight, final Color bottomLeft, final Color bottomRight, final float glowSize, final float glowIntensity, final float alpha) {
        final float[] c1 = ColorUtil.normalize(topLeft);
        final float[] c2 = ColorUtil.normalize(bottomLeft);
        final float[] c3 = ColorUtil.normalize(bottomRight);
        final float[] c4 = ColorUtil.normalize(topRight);
        final float combinedAlpha = (c1[3] + c2[3] + c3[3] + c4[3]) / 4.0f * alpha;
        final float px = x - glowSize;
        final float py = y - glowSize;
        final float pw = width + glowSize * 2.0f;
        final float ph = height + glowSize * 2.0f;
        final Matrix4f mat = matrices.method_23760().method_23761();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        final class_5944 shader = RenderSystem.setShader(GlowRectRender.shaderKey);
        shader.method_34582("uSize").method_1255(width, height);
        shader.method_34582("uRadius").method_35657(radius.x, radius.z, radius.w, radius.y);
        shader.method_34582("uAlpha").method_1251(combinedAlpha);
        shader.method_34582("uSmoothness").method_1251(1.2f);
        shader.method_34582("uGlowSize").method_1251(glowSize);
        shader.method_34582("uGlowIntensity").method_1251(glowIntensity);
        shader.method_34582("uTopLeftColor").method_35657(c1[0], c1[1], c1[2], c1[3]);
        shader.method_34582("uBottomLeftColor").method_35657(c2[0], c2[1], c2[2], c2[3]);
        shader.method_34582("uBottomRightColor").method_35657(c3[0], c3[1], c3[2], c3[3]);
        shader.method_34582("uTopRightColor").method_35657(c4[0], c4[1], c4[2], c4[3]);
        final class_287 buf = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
        buf.method_22918(mat, px, py, 0.0f).method_22913(0.0f, 0.0f).method_39415(topLeft.getRGB());
        buf.method_22918(mat, px, py + ph, 0.0f).method_22913(0.0f, 1.0f).method_39415(bottomLeft.getRGB());
        buf.method_22918(mat, px + pw, py + ph, 0.0f).method_22913(1.0f, 1.0f).method_39415(bottomRight.getRGB());
        buf.method_22918(mat, px + pw, py, 0.0f).method_22913(1.0f, 0.0f).method_39415(topRight.getRGB());
        class_286.method_43433(buf.method_60800());
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
    
    static {
        shaderKey = new class_10156(FileUtil.getShader("rect/glow_rect"), class_290.field_1575, class_10149.field_53930);
    }
}
