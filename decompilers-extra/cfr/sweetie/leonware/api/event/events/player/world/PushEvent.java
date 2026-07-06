/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.event.events.player.world;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class PushEvent
extends Event<PushEventData> {
    private static final PushEvent instance = new PushEvent();

    @Generated
    public static PushEvent getInstance() {
        return instance;
    }

    public record PushEventData(PushingSource source) {

        public static enum PushingSource {
            BLOCK,
            WATER,
            ENTITY;

        }
    }
}

