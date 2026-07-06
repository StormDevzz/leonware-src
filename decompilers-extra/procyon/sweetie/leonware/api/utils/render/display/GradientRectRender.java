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

public class GradientRectRender implements QuickImports
{
    private static final class_10156 shaderKey;
    
    public void draw(final class_4587 matrixStack, final float x, final float y, final float width, final float height, final float radius, final Color topLeft, final Color topRight, final Color bottomLeft, final Color bottomRight) {
        this.draw(matrixStack, x, y, width, height, new Vector4f(radius), topLeft, topRight, bottomLeft, bottomRight);
    }
    
    public void draw(final class_4587 matrixStack, final float x, final float y, final float width, final float height, final Vector4f radius, final Color topLeft, final Color topRight, final Color bottomLeft, final Color bottomRight) {
        final Matrix4f matrix4f = matrixStack.method_23760().method_23761();
        final float z = 0.0f;
        final float smoothness = 1.0f;
        final float horizontalPadding = -smoothness / 2.0f + smoothness * 2.0f;
        final float verticalPadding = smoothness / 2.0f + smoothness;
        final float adjustedX = x - horizontalPadding / 2.0f;
        final float adjustedY = y - verticalPadding / 2.0f;
        final float adjustedWidth = width + horizontalPadding;
        final float adjustedHeight = height + verticalPadding;
        final float[] color1 = ColorUtil.normalize(topLeft);
        final float[] color2 = ColorUtil.normalize(bottomLeft);
        final float[] color3 = ColorUtil.normalize(bottomRight);
        final float[] color4 = ColorUtil.normalize(topRight);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        final class_5944 shader = RenderSystem.setShader(GradientRectRender.shaderKey);
        shader.method_34582("uSize").method_1255(width, height);
        shader.method_34582("uRadius").method_35657(radius.x, radius.z, radius.w, radius.y);
        shader.method_34582("uSmoothness").method_1251(smoothness);
        shader.method_34582("uTopLeftColor").method_35657(color1[0], color1[1], color1[2], color1[3]);
        shader.method_34582("uBottomLeftColor").method_35657(color2[0], color2[1], color2[2], color2[3]);
        shader.method_34582("uBottomRightColor").method_35657(color3[0], color3[1], color3[2], color3[3]);
        shader.method_34582("uTopRightColor").method_35657(color4[0], color4[1], color4[2], color4[3]);
        final class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
        builder.method_22918(matrix4f, adjustedX, adjustedY, z).method_39415(topLeft.getRGB());
        builder.method_22918(matrix4f, adjustedX, adjustedY + adjustedHeight, z).method_39415(bottomLeft.getRGB());
        builder.method_22918(matrix4f, adjustedX + adjustedWidth, adjustedY + adjustedHeight, z).method_39415(bottomRight.getRGB());
        builder.method_22918(matrix4f, adjustedX + adjustedWidth, adjustedY, z).method_39415(topRight.getRGB());
        class_286.method_43433(builder.method_60800());
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
    
    static {
        shaderKey = new class_10156(FileUtil.getShader("rect/gradient_rect"), class_290.field_1576, class_10149.field_53930);
    }
}
