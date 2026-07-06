package sweetie.leonware.api.event.events.other;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import sweetie.leonware.api.event.events.Event;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/other/ScreenEvent.class */
public class ScreenEvent extends Event<ScreenEventData> {
    private static final ScreenEvent instance = new ScreenEvent();

    @Generated
    public static ScreenEvent getInstance() {
        return instance;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/other/ScreenEvent$ScreenEventData.class */
    public static class ScreenEventData {
        private final class_437 screen;
        private final List<class_4185> buttons = new ArrayList();

        @Generated
        public ScreenEventData(class_437 screen) {
            this.screen = screen;
        }

        @Generated
        public class_437 screen() {
            return this.screen;
        }

        @Generated
        public List<class_4185> buttons() {
            return this.buttons;
        }
    }
}
