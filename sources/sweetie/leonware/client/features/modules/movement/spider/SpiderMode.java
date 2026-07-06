package sweetie.leonware.client.features.modules.movement.spider;

import sweetie.leonware.api.event.events.player.move.MotionEvent;
import sweetie.leonware.api.system.backend.Choice;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/spider/SpiderMode.class */
public abstract class SpiderMode extends Choice {
    public void onUpdate() {
    }

    public void onMotion(MotionEvent.MotionEventData event) {
    }

    public boolean hozColl() {
        return mc.field_1724.field_5976;
    }
}
