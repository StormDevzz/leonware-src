// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.color;

import lombok.Generated;
import sweetie.leonware.api.utils.math.MathUtil;
import net.minecraft.class_3532;
import java.awt.Color;

public final class ColorUtil
{
    public static float[] normalize(final Color color) {
        return new float[] { color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f };
    }
    
    public static float[] normalize(final int color) {
        final int[] components = unpack(color);
        return new float[] { components[0] / 255.0f, components[1] / 255.0f, components[2] / 255.0f, components[3] / 255.0f };
    }
    
    public static int[] unpack(final int color) {
        return new int[] { color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF };
    }
    
    public static Color setAlpha(final Color color, final int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), class_3532.method_15340(alpha, 0, 255));
    }
    
    public static Color gradient(final int speed, final int index, final Color... colors) {
        int angle = (int)((System.currentTimeMillis() / speed + index) % 360L);
        angle = ((angle > 180) ? (360 - angle) : angle) + 180;
        int colorIndex = (int)(angle / 360.0f * colors.length);
        if (colorIndex == colors.length) {
            --colorIndex;
        }
        final Color color1 = colors[colorIndex];
        final Color color2 = colors[(colorIndex == colors.length - 1) ? 0 : (colorIndex + 1)];
        return interpolate(color1, color2, angle / 360.0f * colors.length - colorIndex);
    }
    
    public static Color interpolate(final Color to, final Color from, final double amount) {
        return interpolate(to, from, (float)amount);
    }
    
    public static Color interpolate(final Color to, final Color from, final float amount) {
        final float clampedAmount = 1.0f - class_3532.method_15363(amount, 0.0f, 1.0f);
        final int red1 = to.getRed();
        final int green1 = to.getGreen();
        final int blue1 = to.getBlue();
        final int alpha1 = to.getAlpha();
        final int red2 = from.getRed();
        final int green2 = from.getGreen();
        final int blue2 = from.getBlue();
        final int alpha2 = from.getAlpha();
        final int interpolatedRed = MathUtil.interpolate(red1, red2, clampedAmount);
        final int interpolatedGreen = MathUtil.interpolate(green1, green2, clampedAmount);
        final int interpolatedBlue = MathUtil.interpolate(blue1, blue2, clampedAmount);
        final int interpolatedAlpha = MathUtil.interpolate(alpha1, alpha2, clampedAmount);
        return new Color(interpolatedRed, interpolatedGreen, interpolatedBlue, interpolatedAlpha);
    }
    
    public static Color gradient(float progress, final Color color1, final Color color2) {
        if (progress < 0.0f || progress > 1.0f) {
            progress %= 1.0f;
            if (progress < 0.0f) {
                ++progress;
            }
        }
        final int r = (int)(color1.getRed() + (color2.getRed() - color1.getRed()) * progress);
        final int g = (int)(color1.getGreen() + (color2.getGreen() - color1.getGreen()) * progress);
        final int b = (int)(color1.getBlue() + (color2.getBlue() - color1.getBlue()) * progress);
        return new Color(r, g, b);
    }
    
    public static int gradient(final int color1, final int color2, final float position, final float totalWidth, final float time, final float offset) {
        final float gradientLength = 18.0f / offset;
        final float wavePosition = (time + position / (totalWidth * gradientLength)) % 1.0f;
        final float factor = (float)Math.sin(wavePosition * 3.141592653589793 * 2.0) * 0.5f + 0.5f;
        final int a1 = color1 >> 24 & 0xFF;
        final int r1 = color1 >> 16 & 0xFF;
        final int g1 = color1 >> 8 & 0xFF;
        final int b1 = color1 & 0xFF;
        final int a2 = color2 >> 24 & 0xFF;
        final int r2 = color2 >> 16 & 0xFF;
        final int g2 = color2 >> 8 & 0xFF;
        final int b2 = color2 & 0xFF;
        final int a3 = (int)(a1 + (a2 - a1) * factor);
        final int r3 = (int)(r1 + (r2 - r1) * factor);
        final int g3 = (int)(g1 + (g2 - g1) * factor);
        final int b3 = (int)(b1 + (b2 - b1) * factor);
        return a3 << 24 | r3 << 16 | g3 << 8 | b3;
    }
    
    public static Color flashingColor(final Color flashColor, final Color defaultColor) {
        return flashingColor(flashColor, defaultColor, 1.0f);
    }
    
    public static Color flashingColor(final Color flashColor, final Color defaultColor, final float alpha) {
        final double time = System.currentTimeMillis() % 1000L / 1000.0;
        final float flashFactor = (float)(Math.sin(time * 3.141592653589793 * 2.0) * 0.5 + 0.5);
        return flashingColor(flashColor, defaultColor, alpha, flashFactor);
    }
    
    public static Color flashingColor(final Color flashColor, final Color defaultColor, final float alpha, final float flashFactor) {
        final float[] def = normalize(defaultColor);
        final float[] flash = normalize(flashColor);
        return new Color((int)((flash[0] * flashFactor + def[0] * (1.0f - flashFactor)) * 255.0f), (int)((flash[1] * flashFactor + def[1] * (1.0f - flashFactor)) * 255.0f), (int)((flash[2] * flashFactor + def[2] * (1.0f - flashFactor)) * 255.0f), (int)(alpha * 255.0f));
    }
    
    @Generated
    private ColorUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
