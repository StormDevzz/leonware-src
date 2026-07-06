/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.client.features.modules.movement.spider;

import sweetie.leonware.api.event.events.player.move.MotionEvent;
import sweetie.leonware.api.system.backend.Choice;

public abstract class SpiderMode
extends Choice {
    public void onUpdate() {
    }

    public void onMotion(MotionEvent.MotionEventData event) {
    }

    public boolean hozColl() {
        return SpiderMode.mc.field_1724.field_5976;
    }
}

