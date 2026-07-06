// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.client.features.modules.render.nametags.NameTagsModule;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_4597;
import net.minecraft.class_4587;
import net.minecraft.class_2561;
import net.minecraft.class_897;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.class_10017;
import net.minecraft.class_1297;

@Mixin({ class_897.class })
public abstract class MixinEntityRenderer<T extends class_1297, S extends class_10017>
{
    @Inject(method = { "renderLabelIfPresent" }, at = { @At("HEAD") }, cancellable = true)
    private void renderLabelIfPresent(final S state, final class_2561 text, final class_4587 matrices, final class_4597 vertexConsumers, final int light, final CallbackInfo ci) {
        if (NameTagsModule.getInstance().isEnabled()) {
            ci.cancel();
        }
    }
}
