// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.player.world;

import net.minecraft.class_1309;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_2248;
import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class BlockPlaceEvent extends Event<BlockPlaceEventData>
{
    private static final BlockPlaceEvent instance;
    
    @Generated
    public static BlockPlaceEvent getInstance() {
        return BlockPlaceEvent.instance;
    }
    
    static {
        instance = new BlockPlaceEvent();
    }
    
    record BlockPlaceEventData(class_2248 block, class_2680 state, class_2338 pos, class_1309 placer) {}
}
