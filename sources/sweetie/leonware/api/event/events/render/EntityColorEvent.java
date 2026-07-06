package sweetie.leonware.api.event.events.render;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/render/EntityColorEvent.class */
public class EntityColorEvent extends Event<EntityColorEventData> {
    private static final EntityColorEvent instance = new EntityColorEvent();

    @Generated
    public static EntityColorEvent getInstance() {
        return instance;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/render/EntityColorEvent$EntityColorEventData.class */
    public static class EntityColorEventData {
        int color;

        @Generated
        public EntityColorEventData color(int color) {
            this.color = color;
            return this;
        }

        @Generated
        public EntityColorEventData(int color) {
            this.color = color;
        }

        @Generated
        public int color() {
            return this.color;
        }
    }
}
