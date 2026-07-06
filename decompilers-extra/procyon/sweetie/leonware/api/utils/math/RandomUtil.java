// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.math;

import java.util.Random;

public class RandomUtil extends Random
{
    private long seed;
    
    public RandomUtil(final long seed) {
        this.seed = seed;
    }
    
    public RandomUtil() {
        this(System.nanoTime());
    }
    
    public int randomInRange(final int min, final int max) {
        if (min == max || min > max) {
            return min;
        }
        return this.nextInt(max - min + 1) + min;
    }
    
    public float randomInRange(final float min, final float max) {
        return min + this.nextFloat() * (max - min);
    }
    
    public double randomInRange(final double min, final double max) {
        return min + this.nextDouble() * (max - min);
    }
    
    @Override
    protected int next(final int bits) {
        this.seed ^= this.seed << 13;
        this.seed ^= this.seed >>> 17;
        this.seed ^= this.seed << 5;
        return (int)(this.seed & (1L << bits) - 1L);
    }
}
