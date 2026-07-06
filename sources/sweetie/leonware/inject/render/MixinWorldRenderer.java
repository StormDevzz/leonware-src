package sweetie.leonware.inject.render;

import java.awt.Color;
import net.minecraft.class_765;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sweetie.leonware.client.features.modules.render.CustomWorldModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/render/MixinWorldRenderer.class */
@Mixin({class_765.class})
public class MixinWorldRenderer {
    @ModifyVariable(method = {"update"}, at = @At("STORE"), ordinal = 0)
    private Vector3f modifySkyLightColor(Vector3f vector3f) {
        CustomWorldModule mod = CustomWorldModule.getInstance();
        if (!mod.isEnabled() || mod.skyIntensity.getValue().floatValue() <= 0.01f) {
            return vector3f;
        }
        Color c = mod.skyColor.getValue();
        float t = mod.skyIntensity.getValue().floatValue();
        vector3f.x = (vector3f.x * (1.0f - t)) + ((c.getRed() / 255.0f) * t);
        vector3f.y = (vector3f.y * (1.0f - t)) + ((c.getGreen() / 255.0f) * t);
        vector3f.z = (vector3f.z * (1.0f - t)) + ((c.getBlue() / 255.0f) * t);
        return vector3f;
    }

    @ModifyVariable(method = {"update"}, at = @At("STORE"), ordinal = 5)
    private float modifyBlockFactor(float blockFactor) {
        CustomWorldModule mod = CustomWorldModule.getInstance();
        if (!mod.isEnabled() || mod.blockIntensity.getValue().floatValue() <= 0.01f) {
            return blockFactor;
        }
        Color c = mod.blockColor.getValue();
        float brightness = ((c.getRed() + c.getGreen()) + c.getBlue()) / 765.0f;
        float t = mod.blockIntensity.getValue().floatValue();
        return (blockFactor * (1.0f - t)) + (blockFactor * brightness * 2.5f * t);
    }

    @ModifyVariable(method = {"update"}, at = @At("STORE"), ordinal = 6)
    private float modifyAmbientLight(float ambientLight) {
        CustomWorldModule mod = CustomWorldModule.getInstance();
        if (!mod.isEnabled() || mod.ambientIntensity.getValue().floatValue() <= 0.01f) {
            return ambientLight;
        }
        Color c = mod.ambientColor.getValue();
        float brightness = ((c.getRed() + c.getGreen()) + c.getBlue()) / 765.0f;
        float t = mod.ambientIntensity.getValue().floatValue();
        return (ambientLight * (1.0f - t)) + (brightness * 0.8f * t);
    }
}
