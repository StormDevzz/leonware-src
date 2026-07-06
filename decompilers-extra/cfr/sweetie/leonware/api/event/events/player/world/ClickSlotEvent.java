/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1713
 */
package sweetie.leonware.api.event.events.player.world;

import lombok.Generated;
import net.minecraft.class_1713;
import sweetie.leonware.api.event.events.Event;

public class ClickSlotEvent
extends Event<ClickSlotEventData> {
    private static final ClickSlotEvent instance = new ClickSlotEvent();

    @Generated
    public static ClickSlotEvent getInstance() {
        return instance;
    }

    public record ClickSlotEventData(class_1713 slotActionType, int slot, int button, int id) {
    }
}

