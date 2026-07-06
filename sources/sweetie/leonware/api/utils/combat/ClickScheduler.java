package sweetie.leonware.api.utils.combat;

import sweetie.leonware.api.system.interfaces.QuickImports;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/combat/ClickScheduler.class */
public class ClickScheduler implements QuickImports {
    private long delay = 0;
    private long lastClickTime = System.currentTimeMillis();

    public boolean isCooldownComplete() {
        float currentCooldown = mc.field_1724 != null ? mc.field_1724.method_7261(0.5f) : 1.0f;
        long currentTime = System.currentTimeMillis();
        long timeSinceLastClick = currentTime - this.lastClickTime;
        boolean wasComplete = timeSinceLastClick >= this.delay && currentCooldown > 0.9f;
        return wasComplete;
    }

    public boolean hasTicksElapsedSinceLastClick(int ticks) {
        return lastClickPassed() >= ((long) ticks) * 50;
    }

    public long lastClickPassed() {
        return System.currentTimeMillis() - this.lastClickTime;
    }

    public void recalculate(long delay) {
        this.lastClickTime = System.currentTimeMillis();
        this.delay = delay;
    }

    public boolean isOneTickBeforeAttack() {
        return willClickAt(3);
    }

    public boolean willClickAt(int tick) {
        long timeSinceLastClick = lastClickPassed();
        long time = ((long) tick) * 50;
        return timeSinceLastClick >= this.delay - time && timeSinceLastClick < this.delay + time;
    }

    public long toNext() {
        return this.delay - lastClickPassed();
    }

    public float cooldownProgress(int baseTime) {
        return (mc.field_1724.field_6273 + baseTime) / mc.field_1724.method_7279();
    }
}
