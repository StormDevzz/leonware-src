/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 */
package sweetie.leonware.api.event.events.player.world;

import lombok.Generated;
import net.minecraft.class_1297;
import sweetie.leonware.api.event.events.Event;

public class AttackEvent
extends Event<AttackEventData> {
    private static final AttackEvent instance = new AttackEvent();

    @Generated
    public static AttackEvent getInstance() {
        return instance;
    }

    public record AttackEventData(class_1297 entity) {
    }
}

