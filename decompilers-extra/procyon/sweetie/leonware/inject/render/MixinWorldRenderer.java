// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import java.awt.Color;
import sweetie.leonware.client.features.modules.render.CustomWorldModule;
import org.joml.Vector3f;
import net.minecraft.class_765;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_765.class })
public class MixinWorldRenderer
{
    @ModifyVariable(method = { "update" }, at = @At("STORE"), ordinal = 0)
    private Vector3f modifySkyLightColor(final Vector3f vector3f) {
        final CustomWorldModule mod = CustomWorldModule.getInstance();
        if (!mod.isEnabled() || mod.skyIntensity.getValue() <= 0.01f) {
            return vector3f;
        }
        final Color c = mod.skyColor.getValue();
        final float t = mod.skyIntensity.getValue();
        vector3f.x = vector3f.x * (1.0f - t) + c.getRed() / 255.0f * t;
        vector3f.y = vector3f.y * (1.0f - t) + c.getGreen() / 255.0f * t;
        vector3f.z = vector3f.z * (1.0f - t) + c.getBlue() / 255.0f * t;
        return vector3f;
    }
    
    @ModifyVariable(method = { "update" }, at = @At("STORE"), ordinal = 5)
    private float modifyBlockFactor(final float blockFactor) {
        final CustomWorldModule mod = CustomWorldModule.getInstance();
        if (!mod.isEnabled() || mod.blockIntensity.getValue() <= 0.01f) {
            return blockFactor;
        }
        final Color c = mod.blockColor.getValue();
        final float brightness = (c.getRed() + c.getGreen() + c.getBlue()) / 765.0f;
        final float t = mod.blockIntensity.getValue();
        return blockFactor * (1.0f - t) + blockFactor * brightness * 2.5f * t;
    }
    
    @ModifyVariable(method = { "update" }, at = @At("STORE"), ordinal = 6)
    private float modifyAmbientLight(final float ambientLight) {
        final CustomWorldModule mod = CustomWorldModule.getInstance();
        if (!mod.isEnabled() || mod.ambientIntensity.getValue() <= 0.01f) {
            return ambientLight;
        }
        final Color c = mod.ambientColor.getValue();
        final float brightness = (c.getRed() + c.getGreen() + c.getBlue()) / 765.0f;
        final float t = mod.ambientIntensity.getValue();
        return ambientLight * (1.0f - t) + brightness * 0.8f * t;
    }
}
