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
import org.joml.Matrix4f;
import org.joml.Vector4f;
import sweetie.leonware.api.system.files.FileUtil;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.color.ColorUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/render/display/GradientRectRender.class */
public class GradientRectRender implements QuickImports {
    private static final class_10156 shaderKey = new class_10156(FileUtil.getShader("rect/gradient_rect"), class_290.field_1576, class_10149.field_53930);

    public void draw(class_4587 matrixStack, float x, float y, float width, float height, float radius, Color topLeft, Color topRight, Color bottomLeft, Color bottomRight) {
        draw(matrixStack, x, y, width, height, new Vector4f(radius), topLeft, topRight, bottomLeft, bottomRight);
    }

    public void draw(class_4587 matrixStack, float x, float y, float width, float height, Vector4f radius, Color topLeft, Color topRight, Color bottomLeft, Color bottomRight) {
        Matrix4f matrix4f = matrixStack.method_23760().method_23761();
        float horizontalPadding = ((-1.0f) / 2.0f) + (1.0f * 2.0f);
        float verticalPadding = (1.0f / 2.0f) + 1.0f;
        float adjustedX = x - (horizontalPadding / 2.0f);
        float adjustedY = y - (verticalPadding / 2.0f);
        float adjustedWidth = width + horizontalPadding;
        float adjustedHeight = height + verticalPadding;
        float[] color1 = ColorUtil.normalize(topLeft);
        float[] color2 = ColorUtil.normalize(bottomLeft);
        float[] color3 = ColorUtil.normalize(bottomRight);
        float[] color4 = ColorUtil.normalize(topRight);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        class_5944 shader = RenderSystem.setShader(shaderKey);
        shader.method_34582("uSize").method_1255(width, height);
        shader.method_34582("uRadius").method_35657(radius.x, radius.z, radius.w, radius.y);
        shader.method_34582("uSmoothness").method_1251(1.0f);
        shader.method_34582("uTopLeftColor").method_35657(color1[0], color1[1], color1[2], color1[3]);
        shader.method_34582("uBottomLeftColor").method_35657(color2[0], color2[1], color2[2], color2[3]);
        shader.method_34582("uBottomRightColor").method_35657(color3[0], color3[1], color3[2], color3[3]);
        shader.method_34582("uTopRightColor").method_35657(color4[0], color4[1], color4[2], color4[3]);
        class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
        builder.method_22918(matrix4f, adjustedX, adjustedY, 0.0f).method_39415(topLeft.getRGB());
        builder.method_22918(matrix4f, adjustedX, adjustedY + adjustedHeight, 0.0f).method_39415(bottomLeft.getRGB());
        builder.method_22918(matrix4f, adjustedX + adjustedWidth, adjustedY + adjustedHeight, 0.0f).method_39415(bottomRight.getRGB());
        builder.method_22918(matrix4f, adjustedX + adjustedWidth, adjustedY, 0.0f).method_39415(topRight.getRGB());
        class_286.method_43433(builder.method_60800());
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
}
