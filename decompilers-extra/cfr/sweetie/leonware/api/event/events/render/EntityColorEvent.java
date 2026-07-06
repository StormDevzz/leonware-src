/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.event.events.render;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class EntityColorEvent
extends Event<EntityColorEventData> {
    private static final EntityColorEvent instance = new EntityColorEvent();

    @Generated
    public static EntityColorEvent getInstance() {
        return instance;
    }

    public static class EntityColorEventData {
        int color;

        @Generated
        public int color() {
            return this.color;
        }

        @Generated
        public EntityColorEventData color(int color) {
            this.color = color;
            return this;
        }

        @Generated
        public EntityColorEventData(int color) {
            this.color = color;
        }
    }
}

