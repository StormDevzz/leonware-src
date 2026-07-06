/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.event.events.player.move;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class CollisionEvent
extends Event<CollisionEvent> {
    private static final CollisionEvent instance = new CollisionEvent();

    @Generated
    public static CollisionEvent getInstance() {
        return instance;
    }
}

