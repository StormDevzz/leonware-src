// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.math;

import lombok.Generated;

public class TimerUtil
{
    private long millis;
    
    public TimerUtil() {
        this.reset();
    }
    
    public boolean finished(final float delay) {
        return System.currentTimeMillis() - this.millis >= (long)delay;
    }
    
    public boolean finished(final long delay) {
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
    public void setMillis(final long millis) {
        this.millis = millis;
    }
}
