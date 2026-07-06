/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.event.events.other;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class WindowResizeEvent
extends Event<WindowResizeEvent> {
    private static final WindowResizeEvent instance = new WindowResizeEvent();

    @Generated
    public static WindowResizeEvent getInstance() {
        return instance;
    }
}

