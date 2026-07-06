// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_4587;
import sweetie.leonware.client.features.modules.render.AmbienceModule;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_243;
import org.joml.Matrix4f;
import net.minecraft.class_4063;
import net.minecraft.class_9955;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_9955.class })
public class MixinCloudRenderer
{
    @Inject(method = { "renderClouds" }, at = { @At("HEAD") })
    private void onRenderClouds(final int color, final class_4063 cloudRenderMode, final float cloudHeight, final Matrix4f positionMatrix, final Matrix4f projectionMatrix, final class_243 cameraPos, final float ticks, final CallbackInfo ci) {
        final int newColor = AmbienceModule.getInstance().applyCloudsColor(color);
        final class_4587 stack = new class_4587();
        RenderSystem.setShaderColor((newColor >> 16 & 0xFF) / 255.0f, (newColor >> 8 & 0xFF) / 255.0f, (newColor & 0xFF) / 255.0f, 1.0f);
    }
}
