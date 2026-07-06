package sweetie.leonware.api.utils.color;

import java.awt.Color;
import lombok.Generated;
import sweetie.leonware.client.ui.theme.Theme;
import sweetie.leonware.client.ui.theme.ThemeEditor;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/color/UIColors.class */
public final class UIColors {
    @Generated
    private UIColors() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Theme currentTheme() {
        return ThemeEditor.getInstance().getCurrentTheme();
    }

    private static Color getColor(Color color, int alpha) {
        int finalAlpha = (int) ((color.getAlpha() / 255.0f) * alpha);
        return ColorUtil.setAlpha(color, finalAlpha);
    }

    public static Color gradient(int index) {
        return gradient(index, 255);
    }

    public static Color gradient(int index, int alpha) {
        return getColor(ColorUtil.gradient(15, index, primary(alpha), secondary(alpha)), alpha);
    }

    public static Color blur() {
        return blur(255);
    }

    public static Color blur(int alpha) {
        return getColor(currentTheme().getBlurColor(), alpha);
    }

    public static Color widgetBlur() {
        return widgetBlur(255);
    }

    public static Color widgetBlur(int alpha) {
        return getColor(currentTheme().getWidgetBlurColor(), alpha);
    }

    public static Color backgroundBlur() {
        return backgroundBlur(255);
    }

    public static Color backgroundBlur(int alpha) {
        return getColor(currentTheme().getBackgroundBlurColor(), alpha);
    }

    public static Color primary() {
        return primary(255);
    }

    public static Color primary(int alpha) {
        return getColor(currentTheme().getPrimaryColor(), alpha);
    }

    public static Color secondary() {
        return secondary(255);
    }

    public static Color secondary(int alpha) {
        return getColor(currentTheme().getSecondaryColor(), alpha);
    }

    public static Color knob() {
        return knob(255);
    }

    public static Color knob(int alpha) {
        return getColor(currentTheme().getKnobColor(), alpha);
    }

    public static Color inactiveKnob() {
        return inactiveKnob(255);
    }

    public static Color inactiveKnob(int alpha) {
        return getColor(currentTheme().getInactiveKnobColor(), alpha);
    }

    public static Color textColor() {
        return textColor(255);
    }

    public static Color textColor(int alpha) {
        return getColor(currentTheme().getTextColor(), alpha);
    }

    public static Color inactiveTextColor() {
        return inactiveTextColor(255);
    }

    public static Color inactiveTextColor(int alpha) {
        return getColor(currentTheme().getInactiveTextColor(), alpha);
    }

    public static Color positiveColor() {
        return positiveColor(255);
    }

    public static Color positiveColor(int alpha) {
        return getColor(currentTheme().getPositiveColor(), alpha);
    }

    public static Color middleColor() {
        return middleColor(255);
    }

    public static Color middleColor(int alpha) {
        return getColor(currentTheme().getMiddleColor(), alpha);
    }

    public static Color negativeColor() {
        return negativeColor(255);
    }

    public static Color negativeColor(int alpha) {
        return getColor(currentTheme().getNegativeColor(), alpha);
    }
}
