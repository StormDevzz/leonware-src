package sweetie.leonware.api.utils.math;

import java.util.Random;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/math/RandomUtil.class */
public class RandomUtil extends Random {
    private long seed;

    public RandomUtil(long seed) {
        this.seed = seed;
    }

    public RandomUtil() {
        this(System.nanoTime());
    }

    public int randomInRange(int min, int max) {
        return (min == max || min > max) ? min : nextInt((max - min) + 1) + min;
    }

    public float randomInRange(float min, float max) {
        return min + (nextFloat() * (max - min));
    }

    public double randomInRange(double min, double max) {
        return min + (nextDouble() * (max - min));
    }

    @Override // java.util.Random
    protected int next(int bits) {
        this.seed ^= this.seed << 13;
        this.seed ^= this.seed >>> 17;
        this.seed ^= this.seed << 5;
        return (int) (this.seed & ((1 << bits) - 1));
    }
}
