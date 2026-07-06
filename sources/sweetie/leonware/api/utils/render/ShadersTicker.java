package sweetie.leonware.api.utils.render;

import lombok.Generated;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.DisconnectEvent;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/render/ShadersTicker.class */
public class ShadersTicker {
    private static final ShadersTicker instance = new ShadersTicker();
    private long lastUpdateMs = -1;
    private long passedTime = 0;

    @Generated
    public static ShadersTicker getInstance() {
        return instance;
    }

    static {
        DisconnectEvent.getInstance().subscribe(new Listener(event -> {
            instance.reset();
        }));
    }

    public void reset() {
        this.passedTime = 0L;
        this.lastUpdateMs = -1L;
    }

    public void update(float speed) {
        long now = System.currentTimeMillis();
        if (this.lastUpdateMs == -1) {
            this.lastUpdateMs = now;
            return;
        }
        long delta = now - this.lastUpdateMs;
        if (delta > 0) {
            this.passedTime += (long) (delta * speed);
        }
        this.lastUpdateMs = now;
    }

    public long getPassedTime() {
        return this.passedTime;
    }
}
