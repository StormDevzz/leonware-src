package sweetie.leonware.inject.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_243;
import net.minecraft.class_4063;
import net.minecraft.class_4587;
import net.minecraft.class_9955;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.client.features.modules.render.AmbienceModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/render/MixinCloudRenderer.class */
@Mixin({class_9955.class})
public class MixinCloudRenderer {
    @Inject(method = {"renderClouds"}, at = {@At("HEAD")})
    private void onRenderClouds(int color, class_4063 cloudRenderMode, float cloudHeight, Matrix4f positionMatrix, Matrix4f projectionMatrix, class_243 cameraPos, float ticks, CallbackInfo ci) {
        int newColor = AmbienceModule.getInstance().applyCloudsColor(color);
        new class_4587();
        RenderSystem.setShaderColor(((newColor >> 16) & 255) / 255.0f, ((newColor >> 8) & 255) / 255.0f, (newColor & 255) / 255.0f, 1.0f);
    }
}
