// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.other;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.class_746;
import java.util.function.Supplier;
import net.minecraft.class_310;
import sweetie.leonware.client.features.commands.CommandSkin;
import net.minecraft.class_8685;
import net.minecraft.class_640;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_640.class })
public class MixinPlayerListEntry
{
    @ModifyReturnValue(method = { "getSkinTextures" }, at = { @At("RETURN") })
    private class_8685 skinTexturesHook(class_8685 original) {
        final Supplier<class_8685> customSkin = CommandSkin.getCustomSkinTextures();
        final class_746 player = class_310.method_1551().field_1724;
        if (player != null && customSkin != null) {
            final class_640 playerListEntry = player.method_3123();
            if (playerListEntry != null && playerListEntry.equals(this)) {
                original = customSkin.get();
            }
        }
        return original;
    }
    
    @ModifyExpressionValue(method = { "texturesSupplier" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;uuidEquals(Ljava/util/UUID;)Z") })
    private static boolean texturesSupplierHook(final boolean original) {
        return original || CommandSkin.getCustomSkinTextures() != null;
    }
}
