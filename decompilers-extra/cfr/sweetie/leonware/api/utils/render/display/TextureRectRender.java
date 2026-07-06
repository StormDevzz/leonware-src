/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.class_10149
 *  net.minecraft.class_10156
 *  net.minecraft.class_1657
 *  net.minecraft.class_286
 *  net.minecraft.class_287
 *  net.minecraft.class_289
 *  net.minecraft.class_290
 *  net.minecraft.class_293$class_5596
 *  net.minecraft.class_2960
 *  net.minecraft.class_4587
 *  net.minecraft.class_5944
 *  net.minecraft.class_742
 *  net.minecraft.class_9801
 *  org.joml.Matrix4f
 *  org.joml.Vector4f
 */
package sweetie.leonware.api.utils.render.display;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.class_10149;
import net.minecraft.class_10156;
import net.minecraft.class_1657;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import net.minecraft.class_5944;
import net.minecraft.class_742;
import net.minecraft.class_9801;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import sweetie.leonware.api.system.files.FileUtil;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.render.RenderUtil;

public class TextureRectRender
implements QuickImports {
    private static final class_10156 shaderKey = new class_10156(FileUtil.getShader("rect/texture_rect"), class_290.field_1575, class_10149.field_53930);

    public void drawHead(class_4587 matrixStack, class_1657 player, float x, float y, float width, float height, float gap, float radius, Color color) {
        class_2960 skin = ((class_742)player).method_52814().comp_1626();
        float u = 0.125f;
        float u2 = 0.625f;
        float superGap = gap * 2.0f;
        int texture = mc.method_1531().method_4619(skin).method_4624();
        RenderUtil.TEXTURE_RECT.draw(matrixStack, x + gap, y + gap, width - superGap, height - superGap, radius, color, u, u, u, u, texture);
        RenderUtil.TEXTURE_RECT.draw(matrixStack, x, y, width, height, radius, color, u2, u, u, u, texture);
    }

    public void draw(class_4587 matrixStack, float x, float y, float width, float height, float radius, Color color, float u, float v, float texWidth, float texHeight, int texture) {
        this.draw(matrixStack, x, y, width, height, new Vector4f(radius, radius, radius, radius), color, u, v, texWidth, texHeight, texture);
    }

    public void draw(class_4587 matrixStack, float x, float y, float width, float height, Vector4f radius, Color color, float u, float v, float texWidth, float texHeight, int texture) {
        Matrix4f matrix4f = matrixStack.method_23760().method_23761();
        float z = 0.0f;
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
        RenderSystem.setShaderTexture((int)0, (int)texture);
        class_5944 shader = RenderSystem.setShader((class_10156)shaderKey);
        shader.method_34582("uSize").method_1255(width, height);
        shader.method_34582("uRadius").method_35657(radius.x, radius.z, radius.w, radius.y);
        shader.method_34582("uSmoothness").method_1251(smoothness);
        class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
        builder.method_22918(matrix4f, adjustedX, adjustedY, z).method_22913(u, v).method_39415(color.getRGB());
        builder.method_22918(matrix4f, adjustedX, adjustedY + adjustedHeight, z).method_22913(u, v + texHeight).method_39415(color.getRGB());
        builder.method_22918(matrix4f, adjustedX + adjustedWidth, adjustedY + adjustedHeight, z).method_22913(u + texWidth, v + texHeight).method_39415(color.getRGB());
        builder.method_22918(matrix4f, adjustedX + adjustedWidth, adjustedY, z).method_22913(u + texWidth, v).method_39415(color.getRGB());
        class_286.method_43433((class_9801)builder.method_60800());
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public void drawTexture(class_4587 matrixStack, class_2960 identifier, float x, float y, float width, float height, Color color) {
        int textureId = mc.method_1531().method_4619(identifier).method_4624();
        this.draw(matrixStack, x, y, width, height, 0.0f, color, 0.0f, 0.0f, 1.0f, 1.0f, textureId);
    }

    public void drawTexture(class_4587 matrixStack, class_2960 identifier, float x, float y, float width, float height, float radius, Color color) {
        int textureId = mc.method_1531().method_4619(identifier).method_4624();
        this.draw(matrixStack, x, y, width, height, radius, color, 0.0f, 0.0f, 1.0f, 1.0f, textureId);
    }
}

