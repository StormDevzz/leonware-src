package sweetie.leonware.inject.other;

import net.minecraft.class_1309;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.api.event.events.player.world.BlockPlaceEvent;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/other/MixinBlock.class */
@Mixin({class_2248.class})
public class MixinBlock {
    @Inject(method = {"onPlaced"}, at = {@At("HEAD")})
    public void blockPlaceHook(class_1937 world, class_2338 pos, class_2680 state, class_1309 placer, class_1799 itemStack, CallbackInfo callbackInfo) {
        BlockPlaceEvent.getInstance().call(new BlockPlaceEvent.BlockPlaceEventData((class_2248) this, state, pos, placer));
    }
}
