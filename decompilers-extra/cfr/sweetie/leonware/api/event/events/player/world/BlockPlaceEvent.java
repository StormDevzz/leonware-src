/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1309
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 *  net.minecraft.class_2680
 */
package sweetie.leonware.api.event.events.player.world;

import lombok.Generated;
import net.minecraft.class_1309;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import sweetie.leonware.api.event.events.Event;

public class BlockPlaceEvent
extends Event<BlockPlaceEventData> {
    private static final BlockPlaceEvent instance = new BlockPlaceEvent();

    @Generated
    public static BlockPlaceEvent getInstance() {
        return instance;
    }

    public record BlockPlaceEventData(class_2248 block, class_2680 state, class_2338 pos, class_1309 placer) {
    }
}

