/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.utils.math;

import lombok.Generated;

public class TimerUtil {
    private long millis;

    public TimerUtil() {
        this.reset();
    }

    public boolean finished(float delay) {
        return System.currentTimeMillis() - this.millis >= (long)delay;
    }

    public boolean finished(long delay) {
        return System.currentTimeMillis() - this.millis >= delay;
    }

    public void reset() {
        this.millis = System.currentTimeMillis();
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - this.millis;
    }

    @Generated
    public long getMillis() {
        return this.millis;
    }

    @Generated
    public void setMillis(long millis) {
        this.millis = millis;
    }
}

