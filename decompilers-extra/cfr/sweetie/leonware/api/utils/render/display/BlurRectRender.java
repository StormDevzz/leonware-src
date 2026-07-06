/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.class_10149
 *  net.minecraft.class_10156
 *  net.minecraft.class_276
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
import net.minecraft.class_276;
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
import sweetie.leonware.api.utils.render.KawaseBlurProgram;
import sweetie.leonware.client.features.modules.render.InterfaceModule;

public class BlurRectRender
implements QuickImports {
    private static final class_10156 shaderKey = new class_10156(FileUtil.getShader("rect/blurred_rect"), class_290.field_1575, class_10149.field_53930);

    public void draw(class_4587 matrixStack, float x, float y, float width, float height, float radius, Color color, float mix) {
        this.draw(matrixStack, x, y, width, height, new Vector4f(radius, radius, radius, radius), color, color, color, color, mix);
    }

    public void draw(class_4587 matrixStack, float x, float y, float width, float height, float radius, Color color) {
        this.draw(matrixStack, x, y, width, height, radius, color, InterfaceModule.getGlassy());
    }

    public void draw(class_4587 matrixStack, float x, float y, float width, float height, Vector4f radius, Color color) {
        this.draw(matrixStack, x, y, width, height, radius, color, color, color, color, InterfaceModule.getGlassy());
    }

    public void draw(class_4587 matrixStack, float x, float y, float width, float height, Vector4f radius, Color topLeft, Color topRight, Color bottomLeft, Color bottomRight) {
        this.draw(matrixStack, x, y, width, height, radius, topLeft, topRight, bottomLeft, bottomRight, InterfaceModule.getGlassy());
    }

    public void draw(class_4587 matrixStack, float x, float y, float width, float height, Vector4f radius, Color topLeft, Color topRight, Color bottomLeft, Color bottomRight, float mix) {
        float vBottom;
        float vTop;
        float uRight;
        float uLeft;
        float z = 0.0f;
        float[] color1 = ColorUtil.normalize(topLeft);
        float[] color2 = ColorUtil.normalize(bottomLeft);
        float[] color3 = ColorUtil.normalize(bottomRight);
        float[] color4 = ColorUtil.normalize(topRight);
        float a = (color1[3] + color2[3] + color3[3] + color4[3]) / 4.0f;
        Matrix4f matrix4f = matrixStack.method_23760().method_23761();
        class_276 fbo = (class_276)KawaseBlurProgram.fbos.getFirst();
        float smoothness = 0.8f;
        float horizontalPadding = -smoothness / 2.0f + smoothness * 2.0f;
        float verticalPadding = smoothness / 2.0f + smoothness;
        float adjustedX = x - horizontalPadding / 2.0f;
        float adjustedY = y - verticalPadding / 2.0f;
        float adjustedWidth = width + horizontalPadding;
        float adjustedHeight = height + verticalPadding;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShaderTexture((int)0, (int)fbo.method_30277());
        class_5944 shader = RenderSystem.setShader((class_10156)shaderKey);
        shader.method_34582("uSize").method_1255(width, height);
        shader.method_34582("uRadius").method_35657(radius.x, radius.z, radius.w, radius.y);
        shader.method_34582("uMix").method_1251(mix);
        shader.method_34582("uSmoothness").method_1251(smoothness);
        shader.method_34582("uAlpha").method_1251(a);
        shader.method_34582("uTopLeftColor").method_35657(color1[0], color1[1], color1[2], color1[3]);
        shader.method_34582("uBottomLeftColor").method_35657(color2[0], color2[1], color2[2], color2[3]);
        shader.method_34582("uBottomRightColor").method_35657(color3[0], color3[1], color3[2], color3[3]);
        shader.method_34582("uTopRightColor").method_35657(color4[0], color4[1], color4[2], color4[3]);
        if (mix != 1.0f) {
            int texW = fbo.field_1482;
            int texH = fbo.field_1481;
            double scale = (double)texW / (double)mc.method_22683().method_4486();
            float fx = (float)((double)x * scale);
            float fy = (float)((double)y * scale);
            float fw = (float)((double)width * scale);
            float fh = (float)((double)height * scale);
            uLeft = fx / (float)texW;
            uRight = (fx + fw) / (float)texW;
            vTop = 1.0f - fy / (float)texH;
            vBottom = 1.0f - (fy + fh) / (float)texH;
        } else {
            vBottom = 0.0f;
            vTop = 0.0f;
            uRight = 0.0f;
            uLeft = 0.0f;
        }
        class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
        builder.method_22918(matrix4f, adjustedX, adjustedY, z).method_22913(uLeft, vTop).method_39415(topLeft.getRGB());
        builder.method_22918(matrix4f, adjustedX, adjustedY + adjustedHeight, z).method_22913(uLeft, vBottom).method_39415(bottomLeft.getRGB());
        builder.method_22918(matrix4f, adjustedX + adjustedWidth, adjustedY + adjustedHeight, z).method_22913(uRight, vBottom).method_39415(bottomRight.getRGB());
        builder.method_22918(matrix4f, adjustedX + adjustedWidth, adjustedY, z).method_22913(uRight, vTop).method_39415(topRight.getRGB());
        class_286.method_43433((class_9801)builder.method_60800());
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
}

