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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/render/display/RectRender.class */
public class RectRender {
    private static final class_10156 shaderKey = new class_10156(FileUtil.getShader("rect/rect"), class_290.field_1576, class_10149.field_53930);

    public void draw(class_4587 matrixStack, float x, float y, float width, float height, float radius, Color color) {
        draw(matrixStack, x, y, width, height, new Vector4f(radius, radius, radius, radius), color);
    }

    public void draw(class_4587 matrixStack, float x, float y, float width, float height, Vector4f radius, Color color) {
        Matrix4f matrix4f = matrixStack.method_23760().method_23761();
        float horizontalPadding = ((-0.8f) / 2.0f) + (0.8f * 2.0f);
        float verticalPadding = (0.8f / 2.0f) + 0.8f;
        float adjustedX = x - (horizontalPadding / 2.0f);
        float adjustedY = y - (verticalPadding / 2.0f);
        float adjustedWidth = width + horizontalPadding;
        float adjustedHeight = height + verticalPadding;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        class_5944 shader = RenderSystem.setShader(shaderKey);
        shader.method_34582("uSize").method_1255(width, height);
        shader.method_34582("uRadius").method_35657(radius.x, radius.z, radius.w, radius.y);
        shader.method_34582("uSmoothness").method_1251(0.8f);
        int colorInt = color.getRGB();
        class_287 builder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
        builder.method_22918(matrix4f, adjustedX, adjustedY, 0.0f).method_39415(colorInt);
        builder.method_22918(matrix4f, adjustedX, adjustedY + adjustedHeight, 0.0f).method_39415(colorInt);
        builder.method_22918(matrix4f, adjustedX + adjustedWidth, adjustedY + adjustedHeight, 0.0f).method_39415(colorInt);
        builder.method_22918(matrix4f, adjustedX + adjustedWidth, adjustedY, 0.0f).method_39415(colorInt);
        class_286.method_43433(builder.method_60800());
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
}
