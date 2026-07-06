/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.event.events.other;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class FramebufferResizeEvent
extends Event<FramebufferResizeEvent> {
    private static final FramebufferResizeEvent instance = new FramebufferResizeEvent();

    @Generated
    public static FramebufferResizeEvent getInstance() {
        return instance;
    }
}

