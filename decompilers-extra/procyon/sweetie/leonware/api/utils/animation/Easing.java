// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.animation;

import lombok.Generated;
import org.apache.commons.lang3.StringUtils;
import java.util.function.Function;

public enum Easing
{
    LINEAR(x -> x), 
    SIGMOID(x -> 1.0 / (1.0 + Math.exp(-x))), 
    QUAD_IN(x -> x * x), 
    QUAD_OUT(x -> x * (2.0 - x)), 
    QUAD_BOTH(x -> (x < 0.5) ? (2.0 * x * x) : (-1.0 + (4.0 - 2.0 * x) * x)), 
    CUBIC_IN(x -> x * x * x), 
    CUBIC_OUT(x -> {
        --x;
        final Double n8;
        return Double.valueOf(n8 * x * x + 1.0);
    }), 
    CUBIC_BOTH(x -> (x < 0.5) ? (4.0 * x * x * x) : ((x - 1.0) * (2.0 * x - 2.0) * (2.0 * x - 2.0) + 1.0)), 
    QUART_IN(x -> x * x * x * x), 
    QUART_OUT(x -> {
        --x;
        final double n12;
        final Double n13;
        return Double.valueOf(n12 - n13 * x * x * x);
    }), 
    QUART_BOTH(x -> {
        double d;
        if (x < 0.5) {
            d = 8.0 * x * x * x * x;
        }
        else {
            --x;
            final double n15;
            final double n16;
            final Double n17;
            d = n15 - n16 * n17 * x * x * x;
        }
        return Double.valueOf(d);
    }), 
    QUINT_IN(x -> x * x * x * x * x), 
    QUINT_OUT(x -> {
        --x;
        final double n20;
        final Double n21;
        return Double.valueOf(n20 + n21 * x * x * x * x);
    }), 
    QUINT_BOTH(x -> {
        double d2;
        if (x < 0.5) {
            d2 = 16.0 * x * x * x * x * x;
        }
        else {
            --x;
            final double n23;
            final double n24;
            final Double n25;
            d2 = n23 + n24 * n25 * x * x * x * x;
        }
        return Double.valueOf(d2);
    }), 
    SINE_IN(x -> 1.0 - Math.cos(x * 3.141592653589793 / 2.0)), 
    SINE_OUT(x -> Math.sin(x * 3.141592653589793 / 2.0)), 
    SINE_BOTH(x -> (1.0 - Math.cos(3.141592653589793 * x)) / 2.0), 
    EXPO_IN(x -> (x == 0.0) ? 0.0 : Math.pow(2.0, 10.0 * x - 10.0)), 
    EXPO_OUT(x -> (x == 1.0) ? 1.0 : (1.0 - Math.pow(2.0, -10.0 * x))), 
    EXPO_BOTH(x -> (x == 0.0) ? 0.0 : ((x == 1.0) ? 1.0 : ((x < 0.5) ? (Math.pow(2.0, 20.0 * x - 10.0) / 2.0) : ((2.0 - Math.pow(2.0, -20.0 * x + 10.0)) / 2.0)))), 
    CIRC_IN(x -> 1.0 - Math.sqrt(1.0 - x * x)), 
    CIRC_OUT(x -> {
        --x;
        final double n34;
        final Double n35;
        return Double.valueOf(Math.sqrt(n34 - n35 * x));
    }), 
    CIRC_BOTH(x -> (x < 0.5) ? ((1.0 - Math.sqrt(1.0 - 4.0 * x * x)) / 2.0) : ((Math.sqrt(1.0 - 4.0 * (x - 1.0) * x) + 1.0) / 2.0)), 
    BACK_IN(x -> 2.70158 * x * x * x - 1.70158 * x * x), 
    BACK_OUT(x -> 1.0 + 2.70158 * Math.pow(x - 1.0, 3.0) + 1.70158 * Math.pow(x - 1.0, 2.0)), 
    BACK_BOTH(x -> {
        final double c1 = 2.5949095;
        final double c2 = c1 + 1.0;
        return Double.valueOf((x < 0.5) ? (Math.pow(2.0 * x, 2.0) * (c2 * 2.0 * x - c1) / 2.0) : ((Math.pow(2.0 * x - 2.0, 2.0) * (c2 * (2.0 * x - 2.0) + c1) + 2.0) / 2.0));
    }), 
    ELASTIC_IN(x -> (x == 0.0) ? 0.0 : ((x == 1.0) ? 1.0 : (-Math.pow(2.0, 10.0 * x - 10.0) * Math.sin((x * 10.0 - 10.75) * 2.0943951023931953)))), 
    ELASTIC_OUT(x -> (x == 0.0) ? 0.0 : ((x == 1.0) ? 1.0 : (Math.pow(2.0, -10.0 * x) * Math.sin((x * 10.0 - 0.75) * 2.0943951023931953) + 1.0))), 
    ELASTIC_BOTH(x -> (x == 0.0) ? 0.0 : ((x == 1.0) ? 1.0 : ((x < 0.5) ? (-(Math.pow(2.0, 20.0 * x - 10.0) * Math.sin((20.0 * x - 11.125) * 0.6981317007977318))) : ((Math.pow(2.0, -20.0 * x + 10.0) * Math.sin((20.0 * x - 11.125) * 0.6981317007977318) + 2.0) / 2.0)))), 
    SHRINK(x -> {
        final float easeAmount = 1.3f;
        final float shrink = easeAmount + 1.0f;
        return Double.valueOf(Math.max(0.0, 1.0 + shrink * Math.pow(x - 1.0, 3.0) + easeAmount * Math.pow(x - 1.0, 2.0)));
    });
    
    private final Function<Double, Double> function;
    
    private Easing(final Function<Double, Double> function) {
        this.function = function;
    }
    
    public double apply(final double x) {
        return this.getFunction().apply(x);
    }
    
    public float apply(final float x) {
        return this.getFunction().apply((double)x).floatValue();
    }
    
    @Override
    public String toString() {
        return StringUtils.capitalize(super.toString().toLowerCase().replace("_", " "));
    }
    
    @Generated
    public Function<Double, Double> getFunction() {
        return this.function;
    }
}
