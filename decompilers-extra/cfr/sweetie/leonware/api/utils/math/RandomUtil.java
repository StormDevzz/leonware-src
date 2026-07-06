/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.api.utils.math;

import java.util.Random;

public class RandomUtil
extends Random {
    private long seed;

    public RandomUtil(long seed) {
        this.seed = seed;
    }

    public RandomUtil() {
        this(System.nanoTime());
    }

    public int randomInRange(int min, int max) {
        if (min == max || min > max) {
            return min;
        }
        return this.nextInt(max - min + 1) + min;
    }

    public float randomInRange(float min, float max) {
        return min + this.nextFloat() * (max - min);
    }

    public double randomInRange(double min, double max) {
        return min + this.nextDouble() * (max - min);
    }

    @Override
    protected int next(int bits) {
        this.seed ^= this.seed << 13;
        this.seed ^= this.seed >>> 17;
        this.seed ^= this.seed << 5;
        return (int)(this.seed & (1L << bits) - 1L);
    }
}

