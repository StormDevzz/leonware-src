/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_4185
 *  net.minecraft.class_437
 */
package sweetie.leonware.api.event.events.other;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import sweetie.leonware.api.event.events.Event;

public class ScreenEvent
extends Event<ScreenEventData> {
    private static final ScreenEvent instance = new ScreenEvent();

    @Generated
    public static ScreenEvent getInstance() {
        return instance;
    }

    public static class ScreenEventData {
        private final class_437 screen;
        private final List<class_4185> buttons = new ArrayList<class_4185>();

        @Generated
        public class_437 screen() {
            return this.screen;
        }

        @Generated
        public List<class_4185> buttons() {
            return this.buttons;
        }

        @Generated
        public ScreenEventData(class_437 screen) {
            this.screen = screen;
        }
    }
}

