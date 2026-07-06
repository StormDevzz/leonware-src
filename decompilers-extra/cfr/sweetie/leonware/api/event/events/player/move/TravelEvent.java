/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.event.events.player.move;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class TravelEvent
extends Event<TravelEvent> {
    private static final TravelEvent instance = new TravelEvent();

    @Generated
    public static TravelEvent getInstance() {
        return instance;
    }
}

