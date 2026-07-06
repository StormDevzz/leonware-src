// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.rotation.manager;

import lombok.Generated;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.math.MouseUtil;

public class Rotation
{
    private float yaw;
    private float pitch;
    public static final Rotation DEFAULT;
    
    public Rotation(final float yaw, final float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }
    
    public Rotation adjustSensitivity() {
        final double gcd = MouseUtil.getGCD();
        final Rotation previousRotation = RotationManager.getInstance().getServerRotation();
        final float adjustedYaw = this.adjustAxis(this.yaw, previousRotation.yaw, gcd);
        final float adjustedPitch = this.adjustAxis(this.pitch, previousRotation.pitch, gcd);
        return new Rotation(adjustedYaw, class_3532.method_15363(adjustedPitch, -90.0f, 90.0f));
    }
    
    private float adjustAxis(final float axisValue, final float previousValue, final double gcd) {
        final float delta = axisValue - previousValue;
        return previousValue + Math.round(delta / gcd) * (float)gcd;
    }
    
    public class_243 getVector() {
        final float f = this.pitch * 0.017453292f;
        final float g = -this.yaw * 0.017453292f;
        final float h = class_3532.method_15362(g);
        final float i = class_3532.method_15374(g);
        final float j = class_3532.method_15362(f);
        final float k = class_3532.method_15374(f);
        return new class_243((double)(i * j), (double)(-k), (double)(h * j));
    }
    
    public Rotation rotationDeltaTo(final Rotation targetRotation) {
        return RotationUtil.calculateDelta(this, targetRotation);
    }
    
    @Override
    public String toString() {
        return "Rotation(yaw=" + this.yaw + ", pitch=" + this.pitch;
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
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    @Generated
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }
    
    static {
        DEFAULT = new Rotation(0.0f, 0.0f);
    }
    
    record VecRotation(Rotation rotation, class_243 vec) {
        @Override
        public String toString() {
            return "VecRotation(rotation=" + String.valueOf(this.rotation) + ", vec=" + String.valueOf(this.vec);
        }
    }
}
