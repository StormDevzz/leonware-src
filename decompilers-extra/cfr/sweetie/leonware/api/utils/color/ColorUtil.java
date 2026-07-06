/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_3532
 */
package sweetie.leonware.api.utils.color;

import java.awt.Color;
import lombok.Generated;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.math.MathUtil;

public final class ColorUtil {
    public static float[] normalize(Color color) {
        return new float[]{(float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f};
    }

    public static float[] normalize(int color) {
        int[] components = ColorUtil.unpack(color);
        return new float[]{(float)components[0] / 255.0f, (float)components[1] / 255.0f, (float)components[2] / 255.0f, (float)components[3] / 255.0f};
    }

    public static int[] unpack(int color) {
        return new int[]{color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF};
    }

    public static Color setAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), class_3532.method_15340((int)alpha, (int)0, (int)255));
    }

    public static Color gradient(int speed, int index, Color ... colors) {
        int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
        angle = (angle > 180 ? 360 - angle : angle) + 180;
        int colorIndex = (int)((float)angle / 360.0f * (float)colors.length);
        if (colorIndex == colors.length) {
            --colorIndex;
        }
        Color color1 = colors[colorIndex];
        Color color2 = colors[colorIndex == colors.length - 1 ? 0 : colorIndex + 1];
        return ColorUtil.interpolate(color1, color2, (float)angle / 360.0f * (float)colors.length - (float)colorIndex);
    }

    public static Color interpolate(Color to, Color from, double amount) {
        return ColorUtil.interpolate(to, from, (float)amount);
    }

    public static Color interpolate(Color to, Color from, float amount) {
        float clampedAmount = 1.0f - class_3532.method_15363((float)amount, (float)0.0f, (float)1.0f);
        int red1 = to.getRed();
        int green1 = to.getGreen();
        int blue1 = to.getBlue();
        int alpha1 = to.getAlpha();
        int red2 = from.getRed();
        int green2 = from.getGreen();
        int blue2 = from.getBlue();
        int alpha2 = from.getAlpha();
        int interpolatedRed = MathUtil.interpolate(red1, red2, clampedAmount);
        int interpolatedGreen = MathUtil.interpolate(green1, green2, clampedAmount);
        int interpolatedBlue = MathUtil.interpolate(blue1, blue2, clampedAmount);
        int interpolatedAlpha = MathUtil.interpolate(alpha1, alpha2, clampedAmount);
        return new Color(interpolatedRed, interpolatedGreen, interpolatedBlue, interpolatedAlpha);
    }

    public static Color gradient(float progress, Color color1, Color color2) {
        if ((progress < 0.0f || progress > 1.0f) && (progress %= 1.0f) < 0.0f) {
            progress += 1.0f;
        }
        int r = (int)((float)color1.getRed() + (float)(color2.getRed() - color1.getRed()) * progress);
        int g = (int)((float)color1.getGreen() + (float)(color2.getGreen() - color1.getGreen()) * progress);
        int b = (int)((float)color1.getBlue() + (float)(color2.getBlue() - color1.getBlue()) * progress);
        return new Color(r, g, b);
    }

    public static int gradient(int color1, int color2, float position, float totalWidth, float time, float offset) {
        float gradientLength = 18.0f / offset;
        float wavePosition = (time + position / (totalWidth * gradientLength)) % 1.0f;
        float factor = (float)Math.sin((double)wavePosition * Math.PI * 2.0) * 0.5f + 0.5f;
        int a1 = color1 >> 24 & 0xFF;
        int r1 = color1 >> 16 & 0xFF;
        int g1 = color1 >> 8 & 0xFF;
        int b1 = color1 & 0xFF;
        int a2 = color2 >> 24 & 0xFF;
        int r2 = color2 >> 16 & 0xFF;
        int g2 = color2 >> 8 & 0xFF;
        int b2 = color2 & 0xFF;
        int a = (int)((float)a1 + (float)(a2 - a1) * factor);
        int r = (int)((float)r1 + (float)(r2 - r1) * factor);
        int g = (int)((float)g1 + (float)(g2 - g1) * factor);
        int b = (int)((float)b1 + (float)(b2 - b1) * factor);
        return a << 24 | r << 16 | g << 8 | b;
    }

    public static Color flashingColor(Color flashColor, Color defaultColor) {
        return ColorUtil.flashingColor(flashColor, defaultColor, 1.0f);
    }

    public static Color flashingColor(Color flashColor, Color defaultColor, float alpha) {
        double time = (double)(System.currentTimeMillis() % 1000L) / 1000.0;
        float flashFactor = (float)(Math.sin(time * Math.PI * 2.0) * 0.5 + 0.5);
        return ColorUtil.flashingColor(flashColor, defaultColor, alpha, flashFactor);
    }

    public static Color flashingColor(Color flashColor, Color defaultColor, float alpha, float flashFactor) {
        float[] def = ColorUtil.normalize(defaultColor);
        float[] flash = ColorUtil.normalize(flashColor);
        return new Color((int)((flash[0] * flashFactor + def[0] * (1.0f - flashFactor)) * 255.0f), (int)((flash[1] * flashFactor + def[1] * (1.0f - flashFactor)) * 255.0f), (int)((flash[2] * flashFactor + def[2] * (1.0f - flashFactor)) * 255.0f), (int)(alpha * 255.0f));
    }

    @Generated
    private ColorUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

