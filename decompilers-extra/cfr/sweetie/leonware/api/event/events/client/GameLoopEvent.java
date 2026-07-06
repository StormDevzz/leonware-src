/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.event.events.client;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class GameLoopEvent
extends Event<GameLoopEvent> {
    private static final GameLoopEvent instance = new GameLoopEvent();

    @Generated
    public static GameLoopEvent getInstance() {
        return instance;
    }
}

