package sweetie.leonware.api.utils.rotation.rotations;

import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/rotation/rotations/ClientLookRotation.class */
public class ClientLookRotation extends RotationMode {
    private final RotationMode inner;
    private final float strength;

    public ClientLookRotation(RotationMode inner, float strength) {
        super("ClientLook");
        this.inner = inner;
        this.strength = class_3532.method_15363(strength, 0.05f, 1.0f);
    }

    public ClientLookRotation(RotationMode inner) {
        this(inner, 0.35f);
    }

    @Override // sweetie.leonware.api.utils.rotation.manager.RotationMode
    public Rotation process(Rotation currentRotation, Rotation targetRotation, class_243 vec3d, class_1297 entity) {
        float playerYaw = mc.field_1724.method_36454();
        float playerPitch = mc.field_1724.method_36455();
        Rotation playerRot = new Rotation(playerYaw, playerPitch);
        Rotation innerResult = this.inner.process(playerRot, targetRotation, vec3d, entity);
        float yawDiff = class_3532.method_15393(innerResult.getYaw() - playerYaw);
        float pitchDiff = innerResult.getPitch() - playerPitch;
        float moveYaw = yawDiff * this.strength;
        float movePitch = pitchDiff * this.strength;
        float newYaw = playerYaw + moveYaw;
        float newPitch = class_3532.method_15363(playerPitch + movePitch, -90.0f, 90.0f);
        float gcd = MouseUtil.getGCD();
        if (gcd > 0.0f) {
            newYaw = playerYaw + (Math.round(moveYaw / gcd) * gcd);
            float newPitch2 = playerPitch + (Math.round(movePitch / gcd) * gcd);
            newPitch = class_3532.method_15363(newPitch2, -90.0f, 90.0f);
        }
        return new Rotation(newYaw, newPitch);
    }
}
