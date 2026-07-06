/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.class_10149
 *  net.minecraft.class_10156
 *  net.minecraft.class_286
 *  net.minecraft.class_287
 *  net.minecraft.class_289
 *  net.minecraft.class_290
 *  net.minecraft.class_293$class_5596
 *  net.minecraft.class_4587
 *  net.minecraft.class_5944
 *  net.minecraft.class_9801
 *  org.joml.Matrix4f
 *  org.joml.Vector4f
 */
package sweetie.leonware.api.utils.render.display;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.class_10149;
import net.minecraft.class_10156;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_4587;
import net.minecraft.class_5944;
import net.minecraft.class_9801;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import sweetie.leonware.api.system.files.FileUtil;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.color.ColorUtil;

public class GlowRectRender
implements QuickImports {
    private static final class_10156 shaderKey = new class_10156(FileUtil.getShader("rect/glow_rect"), class_290.field_1575, class_10149.field_53930);

    public void draw(class_4587 matrices, float x, float y, float width, float height, float radius, Color color, float glowSize, float glowIntensity) {
        this.draw(matrices, x, y, width, height, new Vector4f(radius, radius, radius, radius), color, color, color, color, glowSize, glowIntensity, 1.0f);
    }

    public void draw(class_4587 matrices, float x, float y, float width, float height, Vector4f radius, Color topLeft, Color topRight, Color bottomLeft, Color bottomRight, float glowSize, float glowIntensity) {
        this.draw(matrices, x, y, width, height, radius, topLeft, topRight, bottomLeft, bottomRight, glowSize, glowIntensity, 1.0f);
    }

    public void draw(class_4587 matrices, float x, float y, float width, float height, Vector4f radius, Color topLeft, Color topRight, Color bottomLeft, Color bottomRight, float glowSize, float glowIntensity, float alpha) {
        float[] c1 = ColorUtil.normalize(topLeft);
        float[] c2 = ColorUtil.normalize(bottomLeft);
        float[] c3 = ColorUtil.normalize(bottomRight);
        float[] c4 = ColorUtil.normalize(topRight);
        float combinedAlpha = (c1[3] + c2[3] + c3[3] + c4[3]) / 4.0f * alpha;
        float px = x - glowSize;
        float py = y - glowSize;
        float pw = width + glowSize * 2.0f;
        float ph = height + glowSize * 2.0f;
        Matrix4f mat = matrices.method_23760().method_23761();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        class_5944 shader = RenderSystem.setShader((class_10156)shaderKey);
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
        class_287 buf = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
        buf.method_22918(mat, px, py, 0.0f).method_22913(0.0f, 0.0f).method_39415(topLeft.getRGB());
        buf.method_22918(mat, px, py + ph, 0.0f).method_22913(0.0f, 1.0f).method_39415(bottomLeft.getRGB());
        buf.method_22918(mat, px + pw, py + ph, 0.0f).method_22913(1.0f, 1.0f).method_39415(bottomRight.getRGB());
        buf.method_22918(mat, px + pw, py, 0.0f).method_22913(1.0f, 0.0f).method_39415(topRight.getRGB());
        class_286.method_43433((class_9801)buf.method_60800());
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
}

