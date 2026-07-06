package sweetie.leonware.api.utils.animation;

import java.util.function.Function;
import lombok.Generated;
import org.apache.commons.lang3.StringUtils;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/animation/Easing.class */
public enum Easing {
    LINEAR(x -> {
        return x;
    }),
    SIGMOID(x2 -> {
        return Double.valueOf(1.0d / (1.0d + Math.exp(-x2.doubleValue())));
    }),
    QUAD_IN(x3 -> {
        return Double.valueOf(x3.doubleValue() * x3.doubleValue());
    }),
    QUAD_OUT(x4 -> {
        return Double.valueOf(x4.doubleValue() * (2.0d - x4.doubleValue()));
    }),
    QUAD_BOTH(x5 -> {
        return Double.valueOf(x5.doubleValue() < 0.5d ? 2.0d * x5.doubleValue() * x5.doubleValue() : (-1.0d) + ((4.0d - (2.0d * x5.doubleValue())) * x5.doubleValue()));
    }),
    CUBIC_IN(x6 -> {
        return Double.valueOf(x6.doubleValue() * x6.doubleValue() * x6.doubleValue());
    }),
    CUBIC_OUT(x7 -> {
        Double x7 = Double.valueOf(x7.doubleValue() - 1.0d);
        return Double.valueOf((x7.doubleValue() * x7.doubleValue() * x7.doubleValue()) + 1.0d);
    }),
    CUBIC_BOTH(x8 -> {
        return Double.valueOf(x8.doubleValue() < 0.5d ? 4.0d * x8.doubleValue() * x8.doubleValue() * x8.doubleValue() : ((x8.doubleValue() - 1.0d) * ((2.0d * x8.doubleValue()) - 2.0d) * ((2.0d * x8.doubleValue()) - 2.0d)) + 1.0d);
    }),
    QUART_IN(x9 -> {
        return Double.valueOf(x9.doubleValue() * x9.doubleValue() * x9.doubleValue() * x9.doubleValue());
    }),
    QUART_OUT(x10 -> {
        Double x10 = Double.valueOf(x10.doubleValue() - 1.0d);
        return Double.valueOf(1.0d - (((x10.doubleValue() * x10.doubleValue()) * x10.doubleValue()) * x10.doubleValue()));
    }),
    QUART_BOTH(x11 -> {
        double dDoubleValue;
        if (x11.doubleValue() < 0.5d) {
            dDoubleValue = 8.0d * x11.doubleValue() * x11.doubleValue() * x11.doubleValue() * x11.doubleValue();
        } else {
            Double x11 = Double.valueOf(x11.doubleValue() - 1.0d);
            dDoubleValue = 1.0d - ((((8.0d * x11.doubleValue()) * x11.doubleValue()) * x11.doubleValue()) * x11.doubleValue());
        }
        return Double.valueOf(dDoubleValue);
    }),
    QUINT_IN(x12 -> {
        return Double.valueOf(x12.doubleValue() * x12.doubleValue() * x12.doubleValue() * x12.doubleValue() * x12.doubleValue());
    }),
    QUINT_OUT(x13 -> {
        Double x13 = Double.valueOf(x13.doubleValue() - 1.0d);
        return Double.valueOf(1.0d + (x13.doubleValue() * x13.doubleValue() * x13.doubleValue() * x13.doubleValue() * x13.doubleValue()));
    }),
    QUINT_BOTH(x14 -> {
        double dDoubleValue;
        if (x14.doubleValue() < 0.5d) {
            dDoubleValue = 16.0d * x14.doubleValue() * x14.doubleValue() * x14.doubleValue() * x14.doubleValue() * x14.doubleValue();
        } else {
            Double x14 = Double.valueOf(x14.doubleValue() - 1.0d);
            dDoubleValue = 1.0d + (16.0d * x14.doubleValue() * x14.doubleValue() * x14.doubleValue() * x14.doubleValue() * x14.doubleValue());
        }
        return Double.valueOf(dDoubleValue);
    }),
    SINE_IN(x15 -> {
        return Double.valueOf(1.0d - Math.cos((x15.doubleValue() * 3.141592653589793d) / 2.0d));
    }),
    SINE_OUT(x16 -> {
        return Double.valueOf(Math.sin((x16.doubleValue() * 3.141592653589793d) / 2.0d));
    }),
    SINE_BOTH(x17 -> {
        return Double.valueOf((1.0d - Math.cos(3.141592653589793d * x17.doubleValue())) / 2.0d);
    }),
    EXPO_IN(x18 -> {
        return Double.valueOf(x18.doubleValue() == 0.0d ? 0.0d : Math.pow(2.0d, (10.0d * x18.doubleValue()) - 10.0d));
    }),
    EXPO_OUT(x19 -> {
        return Double.valueOf(x19.doubleValue() == 1.0d ? 1.0d : 1.0d - Math.pow(2.0d, (-10.0d) * x19.doubleValue()));
    }),
    EXPO_BOTH(x20 -> {
        return Double.valueOf(x20.doubleValue() == 0.0d ? 0.0d : x20.doubleValue() == 1.0d ? 1.0d : x20.doubleValue() < 0.5d ? Math.pow(2.0d, (20.0d * x20.doubleValue()) - 10.0d) / 2.0d : (2.0d - Math.pow(2.0d, ((-20.0d) * x20.doubleValue()) + 10.0d)) / 2.0d);
    }),
    CIRC_IN(x21 -> {
        return Double.valueOf(1.0d - Math.sqrt(1.0d - (x21.doubleValue() * x21.doubleValue())));
    }),
    CIRC_OUT(x22 -> {
        Double x22 = Double.valueOf(x22.doubleValue() - 1.0d);
        return Double.valueOf(Math.sqrt(1.0d - (x22.doubleValue() * x22.doubleValue())));
    }),
    CIRC_BOTH(x23 -> {
        return Double.valueOf(x23.doubleValue() < 0.5d ? (1.0d - Math.sqrt(1.0d - ((4.0d * x23.doubleValue()) * x23.doubleValue()))) / 2.0d : (Math.sqrt(1.0d - ((4.0d * (x23.doubleValue() - 1.0d)) * x23.doubleValue())) + 1.0d) / 2.0d);
    }),
    BACK_IN(x24 -> {
        return Double.valueOf((((2.70158d * x24.doubleValue()) * x24.doubleValue()) * x24.doubleValue()) - ((1.70158d * x24.doubleValue()) * x24.doubleValue()));
    }),
    BACK_OUT(x25 -> {
        return Double.valueOf(1.0d + (2.70158d * Math.pow(x25.doubleValue() - 1.0d, 3.0d)) + (1.70158d * Math.pow(x25.doubleValue() - 1.0d, 2.0d)));
    }),
    BACK_BOTH(x26 -> {
        double c3 = 2.5949095d + 1.0d;
        return Double.valueOf(x26.doubleValue() < 0.5d ? (Math.pow(2.0d * x26.doubleValue(), 2.0d) * (((c3 * 2.0d) * x26.doubleValue()) - 2.5949095d)) / 2.0d : ((Math.pow((2.0d * x26.doubleValue()) - 2.0d, 2.0d) * ((c3 * ((2.0d * x26.doubleValue()) - 2.0d)) + 2.5949095d)) + 2.0d) / 2.0d);
    }),
    ELASTIC_IN(x27 -> {
        return Double.valueOf(x27.doubleValue() == 0.0d ? 0.0d : x27.doubleValue() == 1.0d ? 1.0d : (-Math.pow(2.0d, (10.0d * x27.doubleValue()) - 10.0d)) * Math.sin(((x27.doubleValue() * 10.0d) - 10.75d) * 2.0943951023931953d));
    }),
    ELASTIC_OUT(x28 -> {
        return Double.valueOf(x28.doubleValue() == 0.0d ? 0.0d : x28.doubleValue() == 1.0d ? 1.0d : (Math.pow(2.0d, (-10.0d) * x28.doubleValue()) * Math.sin(((x28.doubleValue() * 10.0d) - 0.75d) * 2.0943951023931953d)) + 1.0d);
    }),
    ELASTIC_BOTH(x29 -> {
        return Double.valueOf(x29.doubleValue() == 0.0d ? 0.0d : x29.doubleValue() == 1.0d ? 1.0d : x29.doubleValue() < 0.5d ? -(Math.pow(2.0d, (20.0d * x29.doubleValue()) - 10.0d) * Math.sin(((20.0d * x29.doubleValue()) - 11.125d) * 0.6981317007977318d)) : ((Math.pow(2.0d, ((-20.0d) * x29.doubleValue()) + 10.0d) * Math.sin(((20.0d * x29.doubleValue()) - 11.125d) * 0.6981317007977318d)) + 2.0d) / 2.0d);
    }),
    SHRINK(x30 -> {
        float shrink = 1.3f + 1.0f;
        return Double.valueOf(Math.max(0.0d, 1.0d + (((double) shrink) * Math.pow(x30.doubleValue() - 1.0d, 3.0d)) + (((double) 1.3f) * Math.pow(x30.doubleValue() - 1.0d, 2.0d))));
    });

    private final Function<Double, Double> function;

    @Generated
    public Function<Double, Double> getFunction() {
        return this.function;
    }

    Easing(final Function function) {
        this.function = function;
    }

    public double apply(double x) {
        return getFunction().apply(Double.valueOf(x)).doubleValue();
    }

    public float apply(float x) {
        return getFunction().apply(Double.valueOf(x)).floatValue();
    }

    @Override // java.lang.Enum
    public String toString() {
        return StringUtils.capitalize(super.toString().toLowerCase().replace("_", " "));
    }
}
