package sweetie.leonware.api.utils.combat;

import java.util.Random;
import org.newsclub.net.unix.AFVSOCKSocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/combat/ClickScheduler18.class */
public class ClickScheduler18 {
    private long nextClickTimeNs = 0;
    private final double[] recentIntervals = new double[5];
    private int intervalIndex = 0;
    private final Random random = new Random();

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/combat/ClickScheduler18$ClickPattern.class */
    public enum ClickPattern {
        STATIC,
        RANDOM,
        JITTER,
        INTERPOLATION,
        GAUSSIAN,
        LEGIT
    }

    public boolean canClick() {
        return this.nextClickTimeNs == 0 || System.nanoTime() >= this.nextClickTimeNs;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    public void advance(int minCps, int maxCps, ClickPattern pattern) throws MatchException {
        if (maxCps <= 0) {
            maxCps = 1;
        }
        if (minCps <= 0) {
            minCps = 1;
        }
        if (minCps > maxCps) {
            minCps = maxCps;
        }
        long intervalMs = computeInterval(minCps, maxCps, pattern);
        long intervalNs = intervalMs * 1000000;
        long now = System.nanoTime();
        if (this.nextClickTimeNs == 0 || now > this.nextClickTimeNs + (intervalNs * 3)) {
            this.nextClickTimeNs = now + intervalNs;
        } else {
            this.nextClickTimeNs += intervalNs;
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private long computeInterval(int minCps, int maxCps, ClickPattern pattern) throws MatchException {
        long minInterval = Math.max(1L, (1000 / ((long) maxCps)) - 2);
        long maxInterval = Math.max(minInterval, (1000 / ((long) minCps)) - 1);
        long range = maxInterval - minInterval;
        switch (pattern.ordinal()) {
            case 0:
                return minInterval;
            case 1:
                return minInterval + (range == 0 ? 0L : (long) (this.random.nextDouble() * range));
            case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                if (range == 0) {
                    return minInterval;
                }
                long base = minInterval + ((long) (this.random.nextDouble() * range));
                double jitter = 0.92d + (this.random.nextDouble() * 0.16d);
                return Math.max(minInterval, Math.min(maxInterval, (long) (base * jitter)));
            case 3:
                if (range == 0) {
                    return minInterval;
                }
                long base2 = minInterval + ((long) (this.random.nextDouble() * range));
                this.recentIntervals[this.intervalIndex % this.recentIntervals.length] = base2;
                this.intervalIndex++;
                double sum = 0.0d;
                int count = Math.min(this.intervalIndex, this.recentIntervals.length);
                for (int i = 0; i < count; i++) {
                    sum += this.recentIntervals[i];
                }
                double avg = sum / ((double) count);
                double t = 0.5d + (this.random.nextDouble() * 0.3d);
                return Math.max(minInterval, Math.min(maxInterval, (long) ((avg * (1.0d - t)) + (base2 * t))));
            case 4:
                if (range == 0) {
                    return minInterval;
                }
                double mid = (minInterval + maxInterval) / 2.0d;
                double sigma = range / 5.0d;
                int tries = 0;
                do {
                    long val = (long) ((this.random.nextGaussian() * sigma) + mid);
                    tries++;
                    if (val < minInterval || val > maxInterval) {
                    }
                    return Math.max(minInterval, Math.min(maxInterval, val));
                } while (tries < 20);
                return Math.max(minInterval, Math.min(maxInterval, val));
            case 5:
                if (range == 0) {
                    return minInterval;
                }
                double base3 = minInterval + (this.random.nextDouble() * range);
                double drift = Math.sin(System.nanoTime() / 8.0E8d) * range * 0.15d;
                double micro = (this.random.nextDouble() - 0.5d) * range * 0.08d;
                long val2 = (long) (base3 + drift + micro);
                if (this.random.nextFloat() < 0.06f) {
                    val2 += (long) (range * (0.15d + (this.random.nextDouble() * 0.2d)));
                }
                this.recentIntervals[this.intervalIndex % this.recentIntervals.length] = val2;
                this.intervalIndex++;
                double sum2 = 0.0d;
                int count2 = Math.min(this.intervalIndex, this.recentIntervals.length);
                for (int i2 = 0; i2 < count2; i2++) {
                    sum2 += this.recentIntervals[i2];
                }
                double avg2 = sum2 / ((double) count2);
                double blend = 0.35d + (this.random.nextDouble() * 0.3d);
                return Math.max(minInterval, Math.min(maxInterval, (long) ((avg2 * blend) + (val2 * (1.0d - blend)))));
            default:
                throw new MatchException((String) null, (Throwable) null);
        }
    }

    public void reset() {
        this.nextClickTimeNs = 0L;
        this.intervalIndex = 0;
    }
}
