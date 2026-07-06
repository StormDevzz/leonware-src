/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.event.events.client;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class DisconnectEvent
extends Event<DisconnectData> {
    private static final DisconnectEvent instance = new DisconnectEvent();

    @Generated
    public static DisconnectEvent getInstance() {
        return instance;
    }

    public static class DisconnectData {
    }
}

