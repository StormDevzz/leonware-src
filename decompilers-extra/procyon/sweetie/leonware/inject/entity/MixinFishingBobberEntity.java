// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.entity;

import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import sweetie.leonware.client.features.modules.player.NoPushEntityModule;
import sweetie.leonware.api.system.backend.SharedClass;
import net.minecraft.class_1297;
import net.minecraft.class_1536;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_1536.class })
public abstract class MixinFishingBobberEntity
{
    @WrapWithCondition(method = { "handleStatus" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/FishingBobberEntity;pullHookedEntity(Lnet/minecraft/entity/Entity;)V") })
    private boolean noPushByFishingRodHook(final class_1536 instance, final class_1297 entity) {
        return entity != SharedClass.player() || !NoPushEntityModule.getInstance().cancelPush(NoPushEntityModule.PushingSource.FISHING_ROD);
    }
}
