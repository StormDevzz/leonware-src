// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.color;

import lombok.Generated;
import java.awt.Color;
import sweetie.leonware.client.ui.theme.ThemeEditor;
import sweetie.leonware.client.ui.theme.Theme;

public final class UIColors
{
    public static Theme currentTheme() {
        return ThemeEditor.getInstance().getCurrentTheme();
    }
    
    private static Color getColor(final Color color, final int alpha) {
        final int finalAlpha = (int)(color.getAlpha() / 255.0f * alpha);
        return ColorUtil.setAlpha(color, finalAlpha);
    }
    
    public static Color gradient(final int index) {
        return gradient(index, 255);
    }
    
    public static Color gradient(final int index, final int alpha) {
        return getColor(ColorUtil.gradient(15, index, primary(alpha), secondary(alpha)), alpha);
    }
    
    public static Color blur() {
        return blur(255);
    }
    
    public static Color blur(final int alpha) {
        return getColor(currentTheme().getBlurColor(), alpha);
    }
    
    public static Color widgetBlur() {
        return widgetBlur(255);
    }
    
    public static Color widgetBlur(final int alpha) {
        return getColor(currentTheme().getWidgetBlurColor(), alpha);
    }
    
    public static Color backgroundBlur() {
        return backgroundBlur(255);
    }
    
    public static Color backgroundBlur(final int alpha) {
        return getColor(currentTheme().getBackgroundBlurColor(), alpha);
    }
    
    public static Color primary() {
        return primary(255);
    }
    
    public static Color primary(final int alpha) {
        return getColor(currentTheme().getPrimaryColor(), alpha);
    }
    
    public static Color secondary() {
        return secondary(255);
    }
    
    public static Color secondary(final int alpha) {
        return getColor(currentTheme().getSecondaryColor(), alpha);
    }
    
    public static Color knob() {
        return knob(255);
    }
    
    public static Color knob(final int alpha) {
        return getColor(currentTheme().getKnobColor(), alpha);
    }
    
    public static Color inactiveKnob() {
        return inactiveKnob(255);
    }
    
    public static Color inactiveKnob(final int alpha) {
        return getColor(currentTheme().getInactiveKnobColor(), alpha);
    }
    
    public static Color textColor() {
        return textColor(255);
    }
    
    public static Color textColor(final int alpha) {
        return getColor(currentTheme().getTextColor(), alpha);
    }
    
    public static Color inactiveTextColor() {
        return inactiveTextColor(255);
    }
    
    public static Color inactiveTextColor(final int alpha) {
        return getColor(currentTheme().getInactiveTextColor(), alpha);
    }
    
    public static Color positiveColor() {
        return positiveColor(255);
    }
    
    public static Color positiveColor(final int alpha) {
        return getColor(currentTheme().getPositiveColor(), alpha);
    }
    
    public static Color middleColor() {
        return middleColor(255);
    }
    
    public static Color middleColor(final int alpha) {
        return getColor(currentTheme().getMiddleColor(), alpha);
    }
    
    public static Color negativeColor() {
        return negativeColor(255);
    }
    
    public static Color negativeColor(final int alpha) {
        return getColor(currentTheme().getNegativeColor(), alpha);
    }
    
    @Generated
    private UIColors() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
