/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.utils.color;

import java.awt.Color;
import lombok.Generated;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.client.ui.theme.Theme;
import sweetie.leonware.client.ui.theme.ThemeEditor;

public final class UIColors {
    public static Theme currentTheme() {
        return ThemeEditor.getInstance().getCurrentTheme();
    }

    private static Color getColor(Color color, int alpha) {
        int finalAlpha = (int)((float)color.getAlpha() / 255.0f * (float)alpha);
        return ColorUtil.setAlpha(color, finalAlpha);
    }

    public static Color gradient(int index) {
        return UIColors.gradient(index, 255);
    }

    public static Color gradient(int index, int alpha) {
        return UIColors.getColor(ColorUtil.gradient(15, index, UIColors.primary(alpha), UIColors.secondary(alpha)), alpha);
    }

    public static Color blur() {
        return UIColors.blur(255);
    }

    public static Color blur(int alpha) {
        return UIColors.getColor(UIColors.currentTheme().getBlurColor(), alpha);
    }

    public static Color widgetBlur() {
        return UIColors.widgetBlur(255);
    }

    public static Color widgetBlur(int alpha) {
        return UIColors.getColor(UIColors.currentTheme().getWidgetBlurColor(), alpha);
    }

    public static Color backgroundBlur() {
        return UIColors.backgroundBlur(255);
    }

    public static Color backgroundBlur(int alpha) {
        return UIColors.getColor(UIColors.currentTheme().getBackgroundBlurColor(), alpha);
    }

    public static Color primary() {
        return UIColors.primary(255);
    }

    public static Color primary(int alpha) {
        return UIColors.getColor(UIColors.currentTheme().getPrimaryColor(), alpha);
    }

    public static Color secondary() {
        return UIColors.secondary(255);
    }

    public static Color secondary(int alpha) {
        return UIColors.getColor(UIColors.currentTheme().getSecondaryColor(), alpha);
    }

    public static Color knob() {
        return UIColors.knob(255);
    }

    public static Color knob(int alpha) {
        return UIColors.getColor(UIColors.currentTheme().getKnobColor(), alpha);
    }

    public static Color inactiveKnob() {
        return UIColors.inactiveKnob(255);
    }

    public static Color inactiveKnob(int alpha) {
        return UIColors.getColor(UIColors.currentTheme().getInactiveKnobColor(), alpha);
    }

    public static Color textColor() {
        return UIColors.textColor(255);
    }

    public static Color textColor(int alpha) {
        return UIColors.getColor(UIColors.currentTheme().getTextColor(), alpha);
    }

    public static Color inactiveTextColor() {
        return UIColors.inactiveTextColor(255);
    }

    public static Color inactiveTextColor(int alpha) {
        return UIColors.getColor(UIColors.currentTheme().getInactiveTextColor(), alpha);
    }

    public static Color positiveColor() {
        return UIColors.positiveColor(255);
    }

    public static Color positiveColor(int alpha) {
        return UIColors.getColor(UIColors.currentTheme().getPositiveColor(), alpha);
    }

    public static Color middleColor() {
        return UIColors.middleColor(255);
    }

    public static Color middleColor(int alpha) {
        return UIColors.getColor(UIColors.currentTheme().getMiddleColor(), alpha);
    }

    public static Color negativeColor() {
        return UIColors.negativeColor(255);
    }

    public static Color negativeColor(int alpha) {
        return UIColors.getColor(UIColors.currentTheme().getNegativeColor(), alpha);
    }

    @Generated
    private UIColors() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

