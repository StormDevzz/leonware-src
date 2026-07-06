/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.api.utils.combat;

import sweetie.leonware.api.system.interfaces.QuickImports;

public class ClickScheduler
implements QuickImports {
    private long delay = 0L;
    private long lastClickTime = System.currentTimeMillis();

    public boolean isCooldownComplete() {
        float currentCooldown = ClickScheduler.mc.field_1724 != null ? ClickScheduler.mc.field_1724.method_7261(0.5f) : 1.0f;
        long currentTime = System.currentTimeMillis();
        long timeSinceLastClick = currentTime - this.lastClickTime;
        boolean wasComplete = timeSinceLastClick >= this.delay && currentCooldown > 0.9f;
        return wasComplete;
    }

    public boolean hasTicksElapsedSinceLastClick(int ticks) {
        return this.lastClickPassed() >= (long)ticks * 50L;
    }

    public long lastClickPassed() {
        return System.currentTimeMillis() - this.lastClickTime;
    }

    public void recalculate(long delay) {
        this.lastClickTime = System.currentTimeMillis();
        this.delay = delay;
    }

    public boolean isOneTickBeforeAttack() {
        return this.willClickAt(3);
    }

    public boolean willClickAt(int tick) {
        long time;
        long timeSinceLastClick = this.lastClickPassed();
        return timeSinceLastClick >= this.delay - (time = (long)tick * 50L) && timeSinceLastClick < this.delay + time;
    }

    public long toNext() {
        return this.delay - this.lastClickPassed();
    }

    public float cooldownProgress(int baseTime) {
        return (float)(ClickScheduler.mc.field_1724.field_6273 + baseTime) / ClickScheduler.mc.field_1724.method_7279();
    }
}

