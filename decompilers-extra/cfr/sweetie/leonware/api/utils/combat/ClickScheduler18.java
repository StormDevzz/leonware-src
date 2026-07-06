/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.api.utils.combat;

import java.util.Random;

public class ClickScheduler18 {
    private long nextClickTimeNs = 0L;
    private final double[] recentIntervals = new double[5];
    private int intervalIndex = 0;
    private final Random random = new Random();

    public boolean canClick() {
        if (this.nextClickTimeNs == 0L) {
            return true;
        }
        return System.nanoTime() >= this.nextClickTimeNs;
    }

    public void advance(int minCps, int maxCps, ClickPattern pattern) {
        if (maxCps <= 0) {
            maxCps = 1;
        }
        if (minCps <= 0) {
            minCps = 1;
        }
        if (minCps > maxCps) {
            minCps = maxCps;
        }
        long intervalMs = this.computeInterval(minCps, maxCps, pattern);
        long intervalNs = intervalMs * 1000000L;
        long now = System.nanoTime();
        this.nextClickTimeNs = this.nextClickTimeNs == 0L || now > this.nextClickTimeNs + intervalNs * 3L ? now + intervalNs : (this.nextClickTimeNs += intervalNs);
    }

    private long computeInterval(int minCps, int maxCps, ClickPattern pattern) {
        long minInterval = 1000L / (long)maxCps - 2L;
        long maxInterval = 1000L / (long)minCps - 1L;
        minInterval = Math.max(1L, minInterval);
        maxInterval = Math.max(minInterval, maxInterval);
        long range = maxInterval - minInterval;
        return switch (pattern.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> minInterval;
            case 1 -> minInterval + (range == 0L ? 0L : (long)(this.random.nextDouble() * (double)range));
            case 2 -> {
                if (range == 0L) {
                    yield minInterval;
                }
                long base = minInterval + (long)(this.random.nextDouble() * (double)range);
                double jitter = 0.92 + this.random.nextDouble() * 0.16;
                yield Math.max(minInterval, Math.min(maxInterval, (long)((double)base * jitter)));
            }
            case 3 -> {
                if (range == 0L) {
                    yield minInterval;
                }
                long base = minInterval + (long)(this.random.nextDouble() * (double)range);
                this.recentIntervals[this.intervalIndex % this.recentIntervals.length] = base;
                ++this.intervalIndex;
                double sum = 0.0;
                int count = Math.min(this.intervalIndex, this.recentIntervals.length);
                for (int i = 0; i < count; ++i) {
                    sum += this.recentIntervals[i];
                }
                double avg = sum / (double)count;
                double t = 0.5 + this.random.nextDouble() * 0.3;
                yield Math.max(minInterval, Math.min(maxInterval, (long)(avg * (1.0 - t) + (double)base * t)));
            }
            case 4 -> {
                long val;
                if (range == 0L) {
                    yield minInterval;
                }
                double mid = (double)(minInterval + maxInterval) / 2.0;
                double sigma = (double)range / 5.0;
                int tries = 0;
                while (((val = (long)(this.random.nextGaussian() * sigma + mid)) < minInterval || val > maxInterval) && ++tries < 20) {
                }
                yield Math.max(minInterval, Math.min(maxInterval, val));
            }
            case 5 -> {
                if (range == 0L) {
                    yield minInterval;
                }
                double base = (double)minInterval + this.random.nextDouble() * (double)range;
                double drift = Math.sin((double)System.nanoTime() / 8.0E8) * (double)range * 0.15;
                double micro = (this.random.nextDouble() - 0.5) * (double)range * 0.08;
                long val2 = (long)(base + drift + micro);
                if (this.random.nextFloat() < 0.06f) {
                    val2 += (long)((double)range * (0.15 + this.random.nextDouble() * 0.2));
                }
                this.recentIntervals[this.intervalIndex % this.recentIntervals.length] = val2;
                ++this.intervalIndex;
                double sum2 = 0.0;
                int count2 = Math.min(this.intervalIndex, this.recentIntervals.length);
                for (int i = 0; i < count2; ++i) {
                    sum2 += this.recentIntervals[i];
                }
                double avg2 = sum2 / (double)count2;
                double blend = 0.35 + this.random.nextDouble() * 0.3;
                yield Math.max(minInterval, Math.min(maxInterval, (long)(avg2 * blend + (double)val2 * (1.0 - blend))));
            }
        };
    }

    public void reset() {
        this.nextClickTimeNs = 0L;
        this.intervalIndex = 0;
    }

    public static enum ClickPattern {
        STATIC,
        RANDOM,
        JITTER,
        INTERPOLATION,
        GAUSSIAN,
        LEGIT;

    }
}

