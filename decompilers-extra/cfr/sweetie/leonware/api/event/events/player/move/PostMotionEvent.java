/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.event.events.player.move;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class PostMotionEvent
extends Event<PostMotionEvent> {
    private static final PostMotionEvent instance = new PostMotionEvent();

    @Generated
    public static PostMotionEvent getInstance() {
        return instance;
    }
}

