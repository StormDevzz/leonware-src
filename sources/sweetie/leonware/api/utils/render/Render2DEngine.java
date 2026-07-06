package sweetie.leonware.api.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import lombok.Generated;
import net.minecraft.class_10142;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.color.UIColors;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/render/Render2DEngine.class */
public final class Render2DEngine implements QuickImports {
    @Generated
    private Render2DEngine() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void onRender(class_332 context) {
    }

    public static void drawRoundedRect(class_4587 matrices, float x, float y, float w, float h, float radius, Color color) {
        RenderUtil.RECT.draw(matrices, x, y, w, h, radius, color);
    }

    public static void drawRoundedGradient(class_4587 matrices, float x, float y, float w, float h, float radius, Color topLeft, Color topRight, Color bottomLeft, Color bottomRight) {
        RenderUtil.GRADIENT_RECT.draw(matrices, x, y, w, h, new Vector4f(radius), topLeft, topRight, bottomLeft, bottomRight);
    }

    public static void drawThemeGradient(class_4587 matrices, float x, float y, float w, float h, float radius) {
        Color c1 = UIColors.primary();
        Color c2 = UIColors.secondary();
        drawRoundedGradient(matrices, x, y, w, h, radius, c1, c2, c2, c1);
    }

    public static void drawHealthBar(class_4587 matrices, float x, float y, float w, float h, float radius, float healthPercentage) {
        Color color;
        float healthPercentage2 = class_3532.method_15363(healthPercentage, 0.0f, 1.0f);
        if (healthPercentage2 >= 0.5f) {
            color = interpolateColor(UIColors.middleColor(), UIColors.positiveColor(), (healthPercentage2 - 0.5f) * 2.0f);
        } else {
            color = interpolateColor(UIColors.negativeColor(), UIColors.middleColor(), healthPercentage2 * 2.0f);
        }
        drawRoundedRect(matrices, x, y, w, h, radius, new Color(20, 20, 20, 180));
        drawRoundedRect(matrices, x, y, w * healthPercentage2, h, radius, color);
    }

    public static Color applyOpacity(Color color, float opacity) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (color.getAlpha() * class_3532.method_15363(opacity, 0.0f, 1.0f)));
    }

    public static Color interpolateColor(Color c1, Color c2, float t) {
        float t2 = class_3532.method_15363(t, 0.0f, 1.0f);
        int r = class_3532.method_48781(t2, c1.getRed(), c2.getRed());
        int g = class_3532.method_48781(t2, c1.getGreen(), c2.getGreen());
        int b = class_3532.method_48781(t2, c1.getBlue(), c2.getBlue());
        int a = class_3532.method_48781(t2, c1.getAlpha(), c2.getAlpha());
        return new Color(r, g, b, a);
    }

    public static void drawCircle(class_4587 matrices, float x, float y, float radius, float lineWidth, int color) {
        class_289 tessellator = class_289.method_1348();
        class_287 buffer = tessellator.method_60827(class_293.class_5596.field_29345, class_290.field_1576);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShader(class_10142.field_53876);
        RenderSystem.lineWidth(lineWidth);
        Matrix4f matrix = matrices.method_23760().method_23761();
        float alpha = ((color >> 24) & 255) / 255.0f;
        float red = ((color >> 16) & 255) / 255.0f;
        float green = ((color >> 8) & 255) / 255.0f;
        float blue = (color & 255) / 255.0f;
        for (int i = 0; i <= 360; i++) {
            double angle = (((double) i) * 3.141592653589793d) / 180.0d;
            buffer.method_22918(matrix, x + ((float) (Math.cos(angle) * ((double) radius))), y + ((float) (Math.sin(angle) * ((double) radius))), 0.0f).method_22915(red, green, blue, alpha);
        }
        class_286.method_43433(buffer.method_60800());
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
}
