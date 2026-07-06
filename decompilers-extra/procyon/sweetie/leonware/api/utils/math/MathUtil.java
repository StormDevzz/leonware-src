// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.math;

import lombok.Generated;
import java.math.RoundingMode;
import java.math.BigDecimal;
import net.minecraft.class_1297;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class MathUtil implements QuickImports
{
    private static final RandomUtil randomUtil;
    
    public static double getEntityBPS(final class_1297 entity) {
        return Math.hypot(entity.field_6014 - entity.method_23317(), entity.field_5969 - entity.method_23321()) * 20.0;
    }
    
    public static float interpolate(final double oldValue, final double newValue) {
        return (float)(oldValue + (newValue - oldValue) * MathUtil.mc.method_61966().method_60637(false));
    }
    
    public static double interpolate(final double start, final double end, final double delta) {
        return start + (end - start) * delta;
    }
    
    public static float interpolate(final float start, final float end, final float delta) {
        return start + (end - start) * delta;
    }
    
    public static int interpolate(final int oldValue, final int newValue, final float interpolationValue) {
        return (int)(oldValue + (newValue - oldValue) * interpolationValue);
    }
    
    public static double round(final double value, final double step) {
        final double v = Math.round(value / step) * step;
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static float round(final float value, final float step) {
        final double v = Math.round(value / step) * (double)step;
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
    
    public static int randomInRange(final int min, final int max) {
        return MathUtil.randomUtil.randomInRange(min, max);
    }
    
    public static float randomInRange(final float min, final float max) {
        return MathUtil.randomUtil.randomInRange(min, max);
    }
    
    public static double randomInRange(final double min, final double max) {
        return MathUtil.randomUtil.randomInRange(min, max);
    }
    
    @Generated
    private MathUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    static {
        randomUtil = new RandomUtil();
    }
}
