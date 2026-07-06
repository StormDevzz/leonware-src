/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.utils.render;

import lombok.Generated;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.DisconnectEvent;

public class ShadersTicker {
    private static final ShadersTicker instance = new ShadersTicker();
    private long lastUpdateMs = -1L;
    private long passedTime = 0L;

    public void reset() {
        this.passedTime = 0L;
        this.lastUpdateMs = -1L;
    }

    public void update(float speed) {
        long now = System.currentTimeMillis();
        if (this.lastUpdateMs == -1L) {
            this.lastUpdateMs = now;
            return;
        }
        long delta = now - this.lastUpdateMs;
        if (delta > 0L) {
            this.passedTime += (long)((float)delta * speed);
        }
        this.lastUpdateMs = now;
    }

    public long getPassedTime() {
        return this.passedTime;
    }

    @Generated
    public static ShadersTicker getInstance() {
        return instance;
    }

    static {
        DisconnectEvent.getInstance().subscribe(new Listener<DisconnectEvent.DisconnectData>(event -> instance.reset()));
    }
}

