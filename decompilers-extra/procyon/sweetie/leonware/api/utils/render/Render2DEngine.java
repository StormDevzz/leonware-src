// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.render;

import lombok.Generated;
import org.joml.Matrix4f;
import net.minecraft.class_287;
import net.minecraft.class_286;
import net.minecraft.class_10142;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_289;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.color.UIColors;
import org.joml.Vector4f;
import java.awt.Color;
import net.minecraft.class_4587;
import net.minecraft.class_332;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class Render2DEngine implements QuickImports
{
    public static void onRender(final class_332 context) {
    }
    
    public static void drawRoundedRect(final class_4587 matrices, final float x, final float y, final float w, final float h, final float radius, final Color color) {
        RenderUtil.RECT.draw(matrices, x, y, w, h, radius, color);
    }
    
    public static void drawRoundedGradient(final class_4587 matrices, final float x, final float y, final float w, final float h, final float radius, final Color topLeft, final Color topRight, final Color bottomLeft, final Color bottomRight) {
        RenderUtil.GRADIENT_RECT.draw(matrices, x, y, w, h, new Vector4f(radius), topLeft, topRight, bottomLeft, bottomRight);
    }
    
    public static void drawThemeGradient(final class_4587 matrices, final float x, final float y, final float w, final float h, final float radius) {
        final Color c1 = UIColors.primary();
        final Color c2 = UIColors.secondary();
        drawRoundedGradient(matrices, x, y, w, h, radius, c1, c2, c2, c1);
    }
    
    public static void drawHealthBar(final class_4587 matrices, final float x, final float y, final float w, final float h, final float radius, float healthPercentage) {
        healthPercentage = class_3532.method_15363(healthPercentage, 0.0f, 1.0f);
        Color color;
        if (healthPercentage >= 0.5f) {
            color = interpolateColor(UIColors.middleColor(), UIColors.positiveColor(), (healthPercentage - 0.5f) * 2.0f);
        }
        else {
            color = interpolateColor(UIColors.negativeColor(), UIColors.middleColor(), healthPercentage * 2.0f);
        }
        drawRoundedRect(matrices, x, y, w, h, radius, new Color(20, 20, 20, 180));
        drawRoundedRect(matrices, x, y, w * healthPercentage, h, radius, color);
    }
    
    public static Color applyOpacity(final Color color, float opacity) {
        opacity = class_3532.method_15363(opacity, 0.0f, 1.0f);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(color.getAlpha() * opacity));
    }
    
    public static Color interpolateColor(final Color c1, final Color c2, float t) {
        t = class_3532.method_15363(t, 0.0f, 1.0f);
        final int r = class_3532.method_48781(t, c1.getRed(), c2.getRed());
        final int g = class_3532.method_48781(t, c1.getGreen(), c2.getGreen());
        final int b = class_3532.method_48781(t, c1.getBlue(), c2.getBlue());
        final int a = class_3532.method_48781(t, c1.getAlpha(), c2.getAlpha());
        return new Color(r, g, b, a);
    }
    
    public static void drawCircle(final class_4587 matrices, final float x, final float y, final float radius, final float lineWidth, final int color) {
        final class_289 tessellator = class_289.method_1348();
        final class_287 buffer = tessellator.method_60827(class_293.class_5596.field_29345, class_290.field_1576);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShader(class_10142.field_53876);
        RenderSystem.lineWidth(lineWidth);
        final Matrix4f matrix = matrices.method_23760().method_23761();
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        for (int i = 0; i <= 360; ++i) {
            final double angle = i * 3.141592653589793 / 180.0;
            buffer.method_22918(matrix, x + (float)(Math.cos(angle) * radius), y + (float)(Math.sin(angle) * radius), 0.0f).method_22915(red, green, blue, alpha);
        }
        class_286.method_43433(buffer.method_60800());
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
    
    @Generated
    private Render2DEngine() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
