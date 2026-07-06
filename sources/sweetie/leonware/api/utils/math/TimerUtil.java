package sweetie.leonware.api.utils.math;

import lombok.Generated;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/math/TimerUtil.class */
public class TimerUtil {
    private long millis;

    @Generated
    public void setMillis(long millis) {
        this.millis = millis;
    }

    @Generated
    public long getMillis() {
        return this.millis;
    }

    public TimerUtil() {
        reset();
    }

    public boolean finished(float delay) {
        return System.currentTimeMillis() - this.millis >= ((long) delay);
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
}
