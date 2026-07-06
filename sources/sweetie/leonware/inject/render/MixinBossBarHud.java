package sweetie.leonware.inject.render;

import net.minecraft.class_310;
import net.minecraft.class_337;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.client.features.modules.render.RemovalsModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/render/MixinBossBarHud.class */
@Mixin({class_337.class})
public class MixinBossBarHud {
    @Inject(method = {"render"}, at = {@At("HEAD")}, cancellable = true)
    private void onRender(CallbackInfo ci) {
        if (class_310.method_1551().field_1724 != null && RemovalsModule.getInstance().isBossBar()) {
            ci.cancel();
        }
    }
}
