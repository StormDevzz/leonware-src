package sweetie.leonware.api.utils.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.Generated;
import net.minecraft.class_1297;
import sweetie.leonware.api.system.interfaces.QuickImports;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/math/MathUtil.class */
public final class MathUtil implements QuickImports {
    private static final RandomUtil randomUtil = new RandomUtil();

    @Generated
    private MathUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static double getEntityBPS(class_1297 entity) {
        return Math.hypot(entity.field_6014 - entity.method_23317(), entity.field_5969 - entity.method_23321()) * 20.0d;
    }

    public static float interpolate(double oldValue, double newValue) {
        return (float) (oldValue + ((newValue - oldValue) * ((double) mc.method_61966().method_60637(false))));
    }

    public static double interpolate(double start, double end, double delta) {
        return start + ((end - start) * delta);
    }

    public static float interpolate(float start, float end, float delta) {
        return start + ((end - start) * delta);
    }

    public static int interpolate(int oldValue, int newValue, float interpolationValue) {
        return (int) (oldValue + ((newValue - oldValue) * interpolationValue));
    }

    public static double round(double value, double step) {
        double v = Math.round(value / step) * step;
        BigDecimal bd = new BigDecimal(v);
        return bd.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public static float round(float value, float step) {
        double v = ((double) Math.round(value / step)) * ((double) step);
        BigDecimal bd = new BigDecimal(v);
        return bd.setScale(2, RoundingMode.HALF_UP).floatValue();
    }

    public static int randomInRange(int min, int max) {
        return randomUtil.randomInRange(min, max);
    }

    public static float randomInRange(float min, float max) {
        return randomUtil.randomInRange(min, max);
    }

    public static double randomInRange(double min, double max) {
        return randomUtil.randomInRange(min, max);
    }
}
