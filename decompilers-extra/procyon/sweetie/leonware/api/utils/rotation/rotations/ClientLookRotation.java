// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.rotation.rotations;

import sweetie.leonware.api.utils.math.MouseUtil;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;

public class ClientLookRotation extends RotationMode
{
    private final RotationMode inner;
    private final float strength;
    
    public ClientLookRotation(final RotationMode inner, final float strength) {
        super("ClientLook");
        this.inner = inner;
        this.strength = class_3532.method_15363(strength, 0.05f, 1.0f);
    }
    
    public ClientLookRotation(final RotationMode inner) {
        this(inner, 0.35f);
    }
    
    @Override
    public Rotation process(final Rotation currentRotation, final Rotation targetRotation, final class_243 vec3d, final class_1297 entity) {
        final float playerYaw = ClientLookRotation.mc.field_1724.method_36454();
        final float playerPitch = ClientLookRotation.mc.field_1724.method_36455();
        final Rotation playerRot = new Rotation(playerYaw, playerPitch);
        final Rotation innerResult = this.inner.process(playerRot, targetRotation, vec3d, entity);
        final float yawDiff = class_3532.method_15393(innerResult.getYaw() - playerYaw);
        final float pitchDiff = innerResult.getPitch() - playerPitch;
        final float moveYaw = yawDiff * this.strength;
        final float movePitch = pitchDiff * this.strength;
        float newYaw = playerYaw + moveYaw;
        float newPitch = class_3532.method_15363(playerPitch + movePitch, -90.0f, 90.0f);
        final float gcd = MouseUtil.getGCD();
        if (gcd > 0.0f) {
            newYaw = playerYaw + Math.round(moveYaw / gcd) * gcd;
            newPitch = playerPitch + Math.round(movePitch / gcd) * gcd;
            newPitch = class_3532.method_15363(newPitch, -90.0f, 90.0f);
        }
        return new Rotation(newYaw, newPitch);
    }
}
