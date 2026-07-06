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
import org.joml.Vector4f;
import java.awt.Color;
import net.minecraft.class_4587;
import net.minecraft.class_10156;

public class RectRender
{
    private static final class_10156 shaderKey;
    
    public void draw(final class_4587 matrixStack, final float x, final float y, final float width, final float height, final float radius, final Color color) {
        this.draw(matrixStack, x, y, width, height, new Vector4f(radius, radius, radius, radius), color);
    }
    
    public void draw(final class_4587 matrixStack, final float x, final float y, final float width, final float height, final Vector4f radius, final Color color) {
        final Matrix4f matrix4f = matrixStack.method_23760().method_23761();
        final float smoothness = 0.8f;
        final float horizontalPadding = -smoothness / 2.0f + smoothness * 2.0f;
        final float verticalPadding = smoothness / 2.0f + smoothness;
        final float adjustedX = x - horizontalPadding / 2.0f;
        final float adjustedY = y - verticalPadding / 2.0f;
        final float adjustedWidth = width + horizontalPadding;
        final float adjustedHeight = height + verticalPadding;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        final class_5944 shader = RenderSystem.setShader(RectRender.shaderKey);
        shader.method_34582("uSize").method_1255(width, height);
        shader.method_34582("uRadius").method_35657(radius.x, radius.z, radius.w, radius.y);
        shader.method_34582("uSmoothness").method_1251(smoothness);
        final float z = 0.0f;
        final int colorInt = color.getRGB();
        final class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
        builder.method_22918(matrix4f, adjustedX, adjustedY, z).method_39415(colorInt);
        builder.method_22918(matrix4f, adjustedX, adjustedY + adjustedHeight, z).method_39415(colorInt);
        builder.method_22918(matrix4f, adjustedX + adjustedWidth, adjustedY + adjustedHeight, z).method_39415(colorInt);
        builder.method_22918(matrix4f, adjustedX + adjustedWidth, adjustedY, z).method_39415(colorInt);
        class_286.method_43433(builder.method_60800());
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
    
    static {
        shaderKey = new class_10156(FileUtil.getShader("rect/rect"), class_290.field_1576, class_10149.field_53930);
    }
}
