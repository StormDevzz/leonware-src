/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.event.events.client;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class KeyEvent
extends Event<KeyEventData> {
    private static final KeyEvent instance = new KeyEvent();

    @Generated
    public static KeyEvent getInstance() {
        return instance;
    }

    public record KeyEventData(int key, int action, boolean mouse) {
    }
}

