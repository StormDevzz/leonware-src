/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  com.llamalad7.mixinextras.injector.ModifyReturnValue
 *  net.minecraft.class_310
 *  net.minecraft.class_640
 *  net.minecraft.class_746
 *  net.minecraft.class_8685
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
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

@Mixin(value={class_640.class})
public class MixinPlayerListEntry {
    @ModifyReturnValue(method={"getSkinTextures"}, at={@At(value="RETURN")})
    private class_8685 skinTexturesHook(class_8685 original) {
        class_640 playerListEntry;
        Supplier<class_8685> customSkin = CommandSkin.getCustomSkinTextures();
        class_746 player = class_310.method_1551().field_1724;
        if (player != null && customSkin != null && (playerListEntry = player.method_3123()) != null && playerListEntry.equals(this)) {
            original = customSkin.get();
        }
        return original;
    }

    @ModifyExpressionValue(method={"texturesSupplier"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/MinecraftClient;uuidEquals(Ljava/util/UUID;)Z")})
    private static boolean texturesSupplierHook(boolean original) {
        return original || CommandSkin.getCustomSkinTextures() != null;
    }
}

