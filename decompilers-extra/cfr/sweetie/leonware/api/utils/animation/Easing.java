/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.apache.commons.lang3.StringUtils
 */
package sweetie.leonware.api.utils.animation;

import java.util.function.Function;
import lombok.Generated;
import org.apache.commons.lang3.StringUtils;

public enum Easing {
    LINEAR(x -> x),
    SIGMOID(x -> 1.0 / (1.0 + Math.exp(-x.doubleValue()))),
    QUAD_IN(x -> x * x),
    QUAD_OUT(x -> x * (2.0 - x)),
    QUAD_BOTH(x -> x < 0.5 ? 2.0 * x * x : -1.0 + (4.0 - 2.0 * x) * x),
    CUBIC_IN(x -> x * x * x),
    CUBIC_OUT(x -> {
        x = x - 1.0;
        return x * x * x + 1.0;
    }),
    CUBIC_BOTH(x -> x < 0.5 ? 4.0 * x * x * x : (x - 1.0) * (2.0 * x - 2.0) * (2.0 * x - 2.0) + 1.0),
    QUART_IN(x -> x * x * x * x),
    QUART_OUT(x -> {
        x = x - 1.0;
        return 1.0 - x * x * x * x;
    }),
    QUART_BOTH(x -> {
        double d;
        if (x < 0.5) {
            d = 8.0 * x * x * x * x;
        } else {
            x = x - 1.0;
            d = 1.0 - 8.0 * x * x * x * x;
        }
        return d;
    }),
    QUINT_IN(x -> x * x * x * x * x),
    QUINT_OUT(x -> {
        x = x - 1.0;
        return 1.0 + x * x * x * x * x;
    }),
    QUINT_BOTH(x -> {
        double d;
        if (x < 0.5) {
            d = 16.0 * x * x * x * x * x;
        } else {
            x = x - 1.0;
            d = 1.0 + 16.0 * x * x * x * x * x;
        }
        return d;
    }),
    SINE_IN(x -> 1.0 - Math.cos(x * Math.PI / 2.0)),
    SINE_OUT(x -> Math.sin(x * Math.PI / 2.0)),
    SINE_BOTH(x -> (1.0 - Math.cos(Math.PI * x)) / 2.0),
    EXPO_IN(x -> x == 0.0 ? 0.0 : Math.pow(2.0, 10.0 * x - 10.0)),
    EXPO_OUT(x -> x == 1.0 ? 1.0 : 1.0 - Math.pow(2.0, -10.0 * x)),
    EXPO_BOTH(x -> x == 0.0 ? 0.0 : (x == 1.0 ? 1.0 : (x < 0.5 ? Math.pow(2.0, 20.0 * x - 10.0) / 2.0 : (2.0 - Math.pow(2.0, -20.0 * x + 10.0)) / 2.0))),
    CIRC_IN(x -> 1.0 - Math.sqrt(1.0 - x * x)),
    CIRC_OUT(x -> {
        x = x - 1.0;
        return Math.sqrt(1.0 - x * x);
    }),
    CIRC_BOTH(x -> x < 0.5 ? (1.0 - Math.sqrt(1.0 - 4.0 * x * x)) / 2.0 : (Math.sqrt(1.0 - 4.0 * (x - 1.0) * x) + 1.0) / 2.0),
    BACK_IN(x -> 2.70158 * x * x * x - 1.70158 * x * x),
    BACK_OUT(x -> 1.0 + 2.70158 * Math.pow(x - 1.0, 3.0) + 1.70158 * Math.pow(x - 1.0, 2.0)),
    BACK_BOTH(x -> {
        double c1 = 2.5949095;
        double c3 = c1 + 1.0;
        return x < 0.5 ? Math.pow(2.0 * x, 2.0) * (c3 * 2.0 * x - c1) / 2.0 : (Math.pow(2.0 * x - 2.0, 2.0) * (c3 * (2.0 * x - 2.0) + c1) + 2.0) / 2.0;
    }),
    ELASTIC_IN(x -> x == 0.0 ? 0.0 : (x == 1.0 ? 1.0 : -Math.pow(2.0, 10.0 * x - 10.0) * Math.sin((x * 10.0 - 10.75) * 2.0943951023931953))),
    ELASTIC_OUT(x -> x == 0.0 ? 0.0 : (x == 1.0 ? 1.0 : Math.pow(2.0, -10.0 * x) * Math.sin((x * 10.0 - 0.75) * 2.0943951023931953) + 1.0)),
    ELASTIC_BOTH(x -> x == 0.0 ? 0.0 : (x == 1.0 ? 1.0 : (x < 0.5 ? -(Math.pow(2.0, 20.0 * x - 10.0) * Math.sin((20.0 * x - 11.125) * 0.6981317007977318)) : (Math.pow(2.0, -20.0 * x + 10.0) * Math.sin((20.0 * x - 11.125) * 0.6981317007977318) + 2.0) / 2.0))),
    SHRINK(x -> {
        float easeAmount = 1.3f;
        float shrink = easeAmount + 1.0f;
        return Math.max(0.0, 1.0 + (double)shrink * Math.pow(x - 1.0, 3.0) + (double)easeAmount * Math.pow(x - 1.0, 2.0));
    });

    private final Function<Double, Double> function;

    private Easing(Function<Double, Double> function) {
        this.function = function;
    }

    public double apply(double x) {
        return this.getFunction().apply(x);
    }

    public float apply(float x) {
        return this.getFunction().apply(Double.valueOf(x)).floatValue();
    }

    public String toString() {
        return StringUtils.capitalize((String)super.toString().toLowerCase().replace("_", " "));
    }

    @Generated
    public Function<Double, Double> getFunction() {
        return this.function;
    }
}

