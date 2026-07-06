package sweetie.leonware.api.utils.color;

import java.awt.Color;
import lombok.Generated;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.math.MathUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/color/ColorUtil.class */
public final class ColorUtil {
    @Generated
    private ColorUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static float[] normalize(Color color) {
        return new float[]{color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f};
    }

    public static float[] normalize(int color) {
        int[] components = unpack(color);
        return new float[]{components[0] / 255.0f, components[1] / 255.0f, components[2] / 255.0f, components[3] / 255.0f};
    }

    public static int[] unpack(int color) {
        return new int[]{(color >> 16) & 255, (color >> 8) & 255, color & 255, (color >> 24) & 255};
    }

    public static Color setAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), class_3532.method_15340(alpha, 0, 255));
    }

    public static Color gradient(int speed, int index, Color... colors) {
        int angle = (int) (((System.currentTimeMillis() / ((long) speed)) + ((long) index)) % 360);
        int angle2 = (angle > 180 ? 360 - angle : angle) + 180;
        int colorIndex = (int) ((angle2 / 360.0f) * colors.length);
        if (colorIndex == colors.length) {
            colorIndex--;
        }
        Color color1 = colors[colorIndex];
        Color color2 = colors[colorIndex == colors.length - 1 ? 0 : colorIndex + 1];
        return interpolate(color1, color2, ((angle2 / 360.0f) * colors.length) - colorIndex);
    }

    public static Color interpolate(Color to, Color from, double amount) {
        return interpolate(to, from, (float) amount);
    }

    public static Color interpolate(Color to, Color from, float amount) {
        float clampedAmount = 1.0f - class_3532.method_15363(amount, 0.0f, 1.0f);
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
        if (progress < 0.0f || progress > 1.0f) {
            progress %= 1.0f;
            if (progress < 0.0f) {
                progress += 1.0f;
            }
        }
        int r = (int) (color1.getRed() + ((color2.getRed() - color1.getRed()) * progress));
        int g = (int) (color1.getGreen() + ((color2.getGreen() - color1.getGreen()) * progress));
        int b = (int) (color1.getBlue() + ((color2.getBlue() - color1.getBlue()) * progress));
        return new Color(r, g, b);
    }

    public static int gradient(int color1, int color2, float position, float totalWidth, float time, float offset) {
        float gradientLength = 18.0f / offset;
        float wavePosition = (time + (position / (totalWidth * gradientLength))) % 1.0f;
        float factor = (((float) Math.sin(((double) wavePosition) * 3.141592653589793d * 2.0d)) * 0.5f) + 0.5f;
        int a1 = (color1 >> 24) & 255;
        int r1 = (color1 >> 16) & 255;
        int g1 = (color1 >> 8) & 255;
        int b1 = color1 & 255;
        int a2 = (color2 >> 24) & 255;
        int r2 = (color2 >> 16) & 255;
        int g2 = (color2 >> 8) & 255;
        int b2 = color2 & 255;
        int a = (int) (a1 + ((a2 - a1) * factor));
        int r = (int) (r1 + ((r2 - r1) * factor));
        int g = (int) (g1 + ((g2 - g1) * factor));
        int b = (int) (b1 + ((b2 - b1) * factor));
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static Color flashingColor(Color flashColor, Color defaultColor) {
        return flashingColor(flashColor, defaultColor, 1.0f);
    }

    public static Color flashingColor(Color flashColor, Color defaultColor, float alpha) {
        double time = (System.currentTimeMillis() % 1000) / 1000.0d;
        float flashFactor = (float) ((Math.sin(time * 3.141592653589793d * 2.0d) * 0.5d) + 0.5d);
        return flashingColor(flashColor, defaultColor, alpha, flashFactor);
    }

    public static Color flashingColor(Color flashColor, Color defaultColor, float alpha, float flashFactor) {
        float[] def = normalize(defaultColor);
        float[] flash = normalize(flashColor);
        return new Color((int) (((flash[0] * flashFactor) + (def[0] * (1.0f - flashFactor))) * 255.0f), (int) (((flash[1] * flashFactor) + (def[1] * (1.0f - flashFactor))) * 255.0f), (int) (((flash[2] * flashFactor) + (def[2] * (1.0f - flashFactor))) * 255.0f), (int) (alpha * 255.0f));
    }
}
