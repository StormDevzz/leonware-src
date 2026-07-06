// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.other;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.api.event.events.player.world.BlockPlaceEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_1799;
import net.minecraft.class_1309;
import net.minecraft.class_2680;
import net.minecraft.class_2338;
import net.minecraft.class_1937;
import net.minecraft.class_2248;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_2248.class })
public class MixinBlock
{
    @Inject(method = { "onPlaced" }, at = { @At("HEAD") })
    public void blockPlaceHook(final class_1937 world, final class_2338 pos, final class_2680 state, final class_1309 placer, final class_1799 itemStack, final CallbackInfo callbackInfo) {
        BlockPlaceEvent.getInstance().call(new BlockPlaceEvent.BlockPlaceEventData((class_2248)this, state, pos, placer));
    }
}
