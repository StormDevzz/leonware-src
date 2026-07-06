/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.event.events.player.move;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class JumpEvent
extends Event<JumpEvent> {
    private static final JumpEvent instance = new JumpEvent();

    @Generated
    public static JumpEvent getInstance() {
        return instance;
    }
}

