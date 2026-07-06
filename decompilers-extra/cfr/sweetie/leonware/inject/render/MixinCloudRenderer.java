/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.class_243
 *  net.minecraft.class_4063
 *  net.minecraft.class_4587
 *  net.minecraft.class_9955
 *  org.joml.Matrix4f
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
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

@Mixin(value={class_9955.class})
public class MixinCloudRenderer {
    @Inject(method={"renderClouds"}, at={@At(value="HEAD")})
    private void onRenderClouds(int color, class_4063 cloudRenderMode, float cloudHeight, Matrix4f positionMatrix, Matrix4f projectionMatrix, class_243 cameraPos, float ticks, CallbackInfo ci) {
        int newColor = AmbienceModule.getInstance().applyCloudsColor(color);
        class_4587 stack = new class_4587();
        RenderSystem.setShaderColor((float)((float)(newColor >> 16 & 0xFF) / 255.0f), (float)((float)(newColor >> 8 & 0xFF) / 255.0f), (float)((float)(newColor & 0xFF) / 255.0f), (float)1.0f);
    }
}

