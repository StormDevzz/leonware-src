// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.client.features.modules.render.RemovalsModule;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_337;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_337.class })
public class MixinBossBarHud
{
    @Inject(method = { "render" }, at = { @At("HEAD") }, cancellable = true)
    private void onRender(final CallbackInfo ci) {
        if (class_310.method_1551().field_1724 == null) {
            return;
        }
        if (RemovalsModule.getInstance().isBossBar()) {
            ci.cancel();
        }
    }
}
