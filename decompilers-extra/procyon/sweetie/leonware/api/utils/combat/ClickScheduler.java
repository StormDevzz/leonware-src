// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.combat;

import sweetie.leonware.api.system.interfaces.QuickImports;

public class ClickScheduler implements QuickImports
{
    private long delay;
    private long lastClickTime;
    
    public ClickScheduler() {
        this.delay = 0L;
        this.lastClickTime = System.currentTimeMillis();
    }
    
    public boolean isCooldownComplete() {
        final float currentCooldown = (ClickScheduler.mc.field_1724 != null) ? ClickScheduler.mc.field_1724.method_7261(0.5f) : 1.0f;
        final long currentTime = System.currentTimeMillis();
        final long timeSinceLastClick = currentTime - this.lastClickTime;
        final boolean wasComplete = timeSinceLastClick >= this.delay && currentCooldown > 0.9f;
        return wasComplete;
    }
    
    public boolean hasTicksElapsedSinceLastClick(final int ticks) {
        return this.lastClickPassed() >= ticks * 50L;
    }
    
    public long lastClickPassed() {
        return System.currentTimeMillis() - this.lastClickTime;
    }
    
    public void recalculate(final long delay) {
        this.lastClickTime = System.currentTimeMillis();
        this.delay = delay;
    }
    
    public boolean isOneTickBeforeAttack() {
        return this.willClickAt(3);
    }
    
    public boolean willClickAt(final int tick) {
        final long timeSinceLastClick = this.lastClickPassed();
        final long time = tick * 50L;
        return timeSinceLastClick >= this.delay - time && timeSinceLastClick < this.delay + time;
    }
    
    public long toNext() {
        return this.delay - this.lastClickPassed();
    }
    
    public float cooldownProgress(final int baseTime) {
        return (ClickScheduler.mc.field_1724.field_6273 + baseTime) / ClickScheduler.mc.field_1724.method_7279();
    }
}
