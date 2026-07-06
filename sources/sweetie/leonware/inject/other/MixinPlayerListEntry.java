package sweetie.leonware.inject.other;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import java.util.function.Supplier;
import net.minecraft.class_310;
import net.minecraft.class_640;
import net.minecraft.class_746;
import net.minecraft.class_8685;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sweetie.leonware.client.features.commands.CommandSkin;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/other/MixinPlayerListEntry.class */
@Mixin({class_640.class})
public class MixinPlayerListEntry {
    @ModifyReturnValue(method = {"getSkinTextures"}, at = {@At("RETURN")})
    private class_8685 skinTexturesHook(class_8685 original) {
        class_640 playerListEntry;
        Supplier<class_8685> customSkin = CommandSkin.getCustomSkinTextures();
        class_746 player = class_310.method_1551().field_1724;
        if (player != null && customSkin != null && (playerListEntry = player.method_3123()) != null && playerListEntry.equals(this)) {
            original = customSkin.get();
        }
        return original;
    }

    @ModifyExpressionValue(method = {"texturesSupplier"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;uuidEquals(Ljava/util/UUID;)Z")})
    private static boolean texturesSupplierHook(boolean original) {
        return original || CommandSkin.getCustomSkinTextures() != null;
    }
}
