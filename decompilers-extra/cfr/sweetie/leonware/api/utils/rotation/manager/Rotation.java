/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 */
package sweetie.leonware.api.utils.rotation.manager;

import lombok.Generated;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;

public class Rotation {
    private float yaw;
    private float pitch;
    public static final Rotation DEFAULT = new Rotation(0.0f, 0.0f);

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Rotation adjustSensitivity() {
        double gcd = MouseUtil.getGCD();
        Rotation previousRotation = RotationManager.getInstance().getServerRotation();
        float adjustedYaw = this.adjustAxis(this.yaw, previousRotation.yaw, gcd);
        float adjustedPitch = this.adjustAxis(this.pitch, previousRotation.pitch, gcd);
        return new Rotation(adjustedYaw, class_3532.method_15363((float)adjustedPitch, (float)-90.0f, (float)90.0f));
    }

    private float adjustAxis(float axisValue, float previousValue, double gcd) {
        float delta = axisValue - previousValue;
        return previousValue + (float)Math.round((double)delta / gcd) * (float)gcd;
    }

    public class_243 getVector() {
        float f = this.pitch * ((float)Math.PI / 180);
        float g = -this.yaw * ((float)Math.PI / 180);
        float h = class_3532.method_15362((float)g);
        float i = class_3532.method_15374((float)g);
        float j = class_3532.method_15362((float)f);
        float k = class_3532.method_15374((float)f);
        return new class_243((double)(i * j), (double)(-k), (double)(h * j));
    }

    public Rotation rotationDeltaTo(Rotation targetRotation) {
        return RotationUtil.calculateDelta(this, targetRotation);
    }

    public String toString() {
        return "Rotation(yaw=" + this.yaw + ", pitch=" + this.pitch + ")";
    }

    @Generated
    public float getYaw() {
        return this.yaw;
    }

    @Generated
    public float getPitch() {
        return this.pitch;
    }

    @Generated
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    @Generated
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public record VecRotation(Rotation rotation, class_243 vec) {
        @Override
        public String toString() {
            return "VecRotation(rotation=" + String.valueOf(this.rotation) + ", vec=" + String.valueOf(this.vec) + ")";
        }
    }
}

