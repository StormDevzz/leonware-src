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

public class RectRender {
    private static final class_10156 shaderKey = new class_10156(FileUtil.getShader("rect/rect"), class_290.field_1576, class_10149.field_53930);

    public void draw(class_4587 matrixStack, float x, float y, float width, float height, float radius, Color color) {
        this.draw(matrixStack, x, y, width, height, new Vector4f(radius, radius, radius, radius), color);
    }

    public void draw(class_4587 matrixStack, float x, float y, float width, float height, Vector4f radius, Color color) {
        Matrix4f matrix4f = matrixStack.method_23760().method_23761();
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
        class_5944 shader = RenderSystem.setShader((class_10156)shaderKey);
        shader.method_34582("uSize").method_1255(width, height);
        shader.method_34582("uRadius").method_35657(radius.x, radius.z, radius.w, radius.y);
        shader.method_34582("uSmoothness").method_1251(smoothness);
        float z = 0.0f;
        int colorInt = color.getRGB();
        class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
        builder.method_22918(matrix4f, adjustedX, adjustedY, z).method_39415(colorInt);
        builder.method_22918(matrix4f, adjustedX, adjustedY + adjustedHeight, z).method_39415(colorInt);
        builder.method_22918(matrix4f, adjustedX + adjustedWidth, adjustedY + adjustedHeight, z).method_39415(colorInt);
        builder.method_22918(matrix4f, adjustedX + adjustedWidth, adjustedY, z).method_39415(colorInt);
        class_286.method_43433((class_9801)builder.method_60800());
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
}

