package sweetie.leonware.inject.render;

import net.minecraft.class_10017;
import net.minecraft.class_1297;
import net.minecraft.class_2561;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_897;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.client.features.modules.render.nametags.NameTagsModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/render/MixinEntityRenderer.class */
@Mixin({class_897.class})
public abstract class MixinEntityRenderer<T extends class_1297, S extends class_10017> {
    @Inject(method = {"renderLabelIfPresent"}, at = {@At("HEAD")}, cancellable = true)
    private void renderLabelIfPresent(S state, class_2561 text, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
        if (NameTagsModule.getInstance().isEnabled()) {
            ci.cancel();
        }
    }
}
