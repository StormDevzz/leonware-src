/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.event.events.client;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class TickEvent
extends Event<TickEvent> {
    private static final TickEvent instance = new TickEvent();

    @Generated
    public static TickEvent getInstance() {
        return instance;
    }
}

