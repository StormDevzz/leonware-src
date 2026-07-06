/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.v2.WrapWithCondition
 *  net.minecraft.class_1297
 *  net.minecraft.class_1536
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package sweetie.leonware.inject.entity;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.class_1297;
import net.minecraft.class_1536;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sweetie.leonware.api.system.backend.SharedClass;
import sweetie.leonware.client.features.modules.player.NoPushEntityModule;

@Mixin(value={class_1536.class})
public abstract class MixinFishingBobberEntity {
    @WrapWithCondition(method={"handleStatus"}, at={@At(value="INVOKE", target="Lnet/minecraft/entity/projectile/FishingBobberEntity;pullHookedEntity(Lnet/minecraft/entity/Entity;)V")})
    private boolean noPushByFishingRodHook(class_1536 instance, class_1297 entity) {
        return entity != SharedClass.player() || !NoPushEntityModule.getInstance().cancelPush(NoPushEntityModule.PushingSource.FISHING_ROD);
    }
}

