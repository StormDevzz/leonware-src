/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  lombok.Generated
 *  net.minecraft.class_10142
 *  net.minecraft.class_10156
 *  net.minecraft.class_286
 *  net.minecraft.class_287
 *  net.minecraft.class_289
 *  net.minecraft.class_290
 *  net.minecraft.class_293$class_5596
 *  net.minecraft.class_332
 *  net.minecraft.class_3532
 *  net.minecraft.class_4587
 *  net.minecraft.class_9801
 *  org.joml.Matrix4f
 *  org.joml.Vector4f
 */
package sweetie.leonware.api.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import lombok.Generated;
import net.minecraft.class_10142;
import net.minecraft.class_10156;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_9801;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;

public final class Render2DEngine
implements QuickImports {
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
        Render2DEngine.drawRoundedGradient(matrices, x, y, w, h, radius, c1, c2, c2, c1);
    }

    public static void drawHealthBar(class_4587 matrices, float x, float y, float w, float h, float radius, float healthPercentage) {
        Color color = (healthPercentage = class_3532.method_15363((float)healthPercentage, (float)0.0f, (float)1.0f)) >= 0.5f ? Render2DEngine.interpolateColor(UIColors.middleColor(), UIColors.positiveColor(), (healthPercentage - 0.5f) * 2.0f) : Render2DEngine.interpolateColor(UIColors.negativeColor(), UIColors.middleColor(), healthPercentage * 2.0f);
        Render2DEngine.drawRoundedRect(matrices, x, y, w, h, radius, new Color(20, 20, 20, 180));
        Render2DEngine.drawRoundedRect(matrices, x, y, w * healthPercentage, h, radius, color);
    }

    public static Color applyOpacity(Color color, float opacity) {
        opacity = class_3532.method_15363((float)opacity, (float)0.0f, (float)1.0f);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)((float)color.getAlpha() * opacity));
    }

    public static Color interpolateColor(Color c1, Color c2, float t) {
        t = class_3532.method_15363((float)t, (float)0.0f, (float)1.0f);
        int r = class_3532.method_48781((float)t, (int)c1.getRed(), (int)c2.getRed());
        int g = class_3532.method_48781((float)t, (int)c1.getGreen(), (int)c2.getGreen());
        int b = class_3532.method_48781((float)t, (int)c1.getBlue(), (int)c2.getBlue());
        int a = class_3532.method_48781((float)t, (int)c1.getAlpha(), (int)c2.getAlpha());
        return new Color(r, g, b, a);
    }

    public static void drawCircle(class_4587 matrices, float x, float y, float radius, float lineWidth, int color) {
        class_289 tessellator = class_289.method_1348();
        class_287 buffer = tessellator.method_60827(class_293.class_5596.field_29345, class_290.field_1576);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShader((class_10156)class_10142.field_53876);
        RenderSystem.lineWidth((float)lineWidth);
        Matrix4f matrix = matrices.method_23760().method_23761();
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        for (int i = 0; i <= 360; ++i) {
            double angle = (double)i * Math.PI / 180.0;
            buffer.method_22918(matrix, x + (float)(Math.cos(angle) * (double)radius), y + (float)(Math.sin(angle) * (double)radius), 0.0f).method_22915(red, green, blue, alpha);
        }
        class_286.method_43433((class_9801)buffer.method_60800());
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    @Generated
    private Render2DEngine() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

