/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1309
 *  net.minecraft.class_1799
 *  net.minecraft.class_1937
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 *  net.minecraft.class_2680
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
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

@Mixin(value={class_2248.class})
public class MixinBlock {
    @Inject(method={"onPlaced"}, at={@At(value="HEAD")})
    public void blockPlaceHook(class_1937 world, class_2338 pos, class_2680 state, class_1309 placer, class_1799 itemStack, CallbackInfo callbackInfo) {
        BlockPlaceEvent.getInstance().call(new BlockPlaceEvent.BlockPlaceEventData((class_2248)this, state, pos, placer));
    }
}

