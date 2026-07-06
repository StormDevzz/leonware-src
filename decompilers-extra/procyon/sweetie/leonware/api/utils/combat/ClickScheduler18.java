// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.combat;

import java.util.Random;

public class ClickScheduler18
{
    private long nextClickTimeNs;
    private final double[] recentIntervals;
    private int intervalIndex;
    private final Random random;
    
    public ClickScheduler18() {
        this.nextClickTimeNs = 0L;
        this.recentIntervals = new double[5];
        this.intervalIndex = 0;
        this.random = new Random();
    }
    
    public boolean canClick() {
        return this.nextClickTimeNs == 0L || System.nanoTime() >= this.nextClickTimeNs;
    }
    
    public void advance(int minCps, int maxCps, final ClickPattern pattern) {
        if (maxCps <= 0) {
            maxCps = 1;
        }
        if (minCps <= 0) {
            minCps = 1;
        }
        if (minCps > maxCps) {
            minCps = maxCps;
        }
        final long intervalMs = this.computeInterval(minCps, maxCps, pattern);
        final long intervalNs = intervalMs * 1000000L;
        final long now = System.nanoTime();
        if (this.nextClickTimeNs == 0L || now > this.nextClickTimeNs + intervalNs * 3L) {
            this.nextClickTimeNs = now + intervalNs;
        }
        else {
            this.nextClickTimeNs += intervalNs;
        }
    }
    
    private long computeInterval(final int minCps, final int maxCps, final ClickPattern pattern) {
        long minInterval = 1000L / maxCps - 2L;
        long maxInterval = 1000L / minCps - 1L;
        minInterval = Math.max(1L, minInterval);
        maxInterval = Math.max(minInterval, maxInterval);
        final long range = maxInterval - minInterval;
        long n = 0L;
        switch (pattern.ordinal()) {
            default: {
                throw new MatchException(null, null);
            }
            case 0: {
                n = minInterval;
                break;
            }
            case 1: {
                n = minInterval + ((range == 0L) ? 0L : ((long)(this.random.nextDouble() * range)));
                break;
            }
            case 2: {
                if (range == 0L) {
                    n = minInterval;
                    break;
                }
                final long base = minInterval + (long)(this.random.nextDouble() * range);
                final double jitter = 0.92 + this.random.nextDouble() * 0.16;
                n = Math.max(minInterval, Math.min(maxInterval, (long)(base * jitter)));
                break;
            }
            case 3: {
                if (range == 0L) {
                    n = minInterval;
                    break;
                }
                final long base = minInterval + (long)(this.random.nextDouble() * range);
                this.recentIntervals[this.intervalIndex % this.recentIntervals.length] = (double)base;
                ++this.intervalIndex;
                double sum = 0.0;
                final int count = Math.min(this.intervalIndex, this.recentIntervals.length);
                for (int i = 0; i < count; ++i) {
                    sum += this.recentIntervals[i];
                }
                final double avg = sum / count;
                final double t = 0.5 + this.random.nextDouble() * 0.3;
                n = Math.max(minInterval, Math.min(maxInterval, (long)(avg * (1.0 - t) + base * t)));
                break;
            }
            case 4: {
                if (range == 0L) {
                    n = minInterval;
                    break;
                }
                final double mid = (minInterval + maxInterval) / 2.0;
                final double sigma = range / 5.0;
                int tries = 0;
                long val;
                do {
                    val = (long)(this.random.nextGaussian() * sigma + mid);
                    ++tries;
                } while ((val < minInterval || val > maxInterval) && tries < 20);
                n = Math.max(minInterval, Math.min(maxInterval, val));
                break;
            }
            case 5: {
                if (range == 0L) {
                    n = minInterval;
                    break;
                }
                final double base2 = minInterval + this.random.nextDouble() * range;
                final double drift = Math.sin(System.nanoTime() / 8.0E8) * range * 0.15;
                final double micro = (this.random.nextDouble() - 0.5) * range * 0.08;
                long val2 = (long)(base2 + drift + micro);
                if (this.random.nextFloat() < 0.06f) {
                    val2 += (long)(range * (0.15 + this.random.nextDouble() * 0.2));
                }
                this.recentIntervals[this.intervalIndex % this.recentIntervals.length] = (double)val2;
                ++this.intervalIndex;
                double sum2 = 0.0;
                final int count2 = Math.min(this.intervalIndex, this.recentIntervals.length);
                for (int j = 0; j < count2; ++j) {
                    sum2 += this.recentIntervals[j];
                }
                final double avg2 = sum2 / count2;
                final double blend = 0.35 + this.random.nextDouble() * 0.3;
                n = Math.max(minInterval, Math.min(maxInterval, (long)(avg2 * blend + val2 * (1.0 - blend))));
                break;
            }
        }
        return n;
    }
    
    public void reset() {
        this.nextClickTimeNs = 0L;
        this.intervalIndex = 0;
    }
    
    public enum ClickPattern
    {
        STATIC, 
        RANDOM, 
        JITTER, 
        INTERPOLATION, 
        GAUSSIAN, 
        LEGIT;
    }
}
