// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.render;

import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.DisconnectEvent;
import lombok.Generated;

public class ShadersTicker
{
    private static final ShadersTicker instance;
    private long lastUpdateMs;
    private long passedTime;
    
    public ShadersTicker() {
        this.lastUpdateMs = -1L;
        this.passedTime = 0L;
    }
    
    public void reset() {
        this.passedTime = 0L;
        this.lastUpdateMs = -1L;
    }
    
    public void update(final float speed) {
        final long now = System.currentTimeMillis();
        if (this.lastUpdateMs == -1L) {
            this.lastUpdateMs = now;
            return;
        }
        final long delta = now - this.lastUpdateMs;
        if (delta > 0L) {
            this.passedTime += (long)(delta * speed);
        }
        this.lastUpdateMs = now;
    }
    
    public long getPassedTime() {
        return this.passedTime;
    }
    
    @Generated
    public static ShadersTicker getInstance() {
        return ShadersTicker.instance;
    }
    
    static {
        instance = new ShadersTicker();
        DisconnectEvent.getInstance().subscribe(new Listener<DisconnectEvent.DisconnectData>(event -> ShadersTicker.instance.reset()));
    }
}
