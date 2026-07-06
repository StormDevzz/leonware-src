package sweetie.leonware.api.utils.rotation.manager;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import lombok.Generated;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.api.utils.rotation.RotationUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/rotation/manager/Rotation.class */
public class Rotation {
    private float yaw;
    private float pitch;
    public static final Rotation DEFAULT = new Rotation(0.0f, 0.0f);

    @Generated
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    @Generated
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Generated
    public float getYaw() {
        return this.yaw;
    }

    @Generated
    public float getPitch() {
        return this.pitch;
    }

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Rotation adjustSensitivity() {
        double gcd = MouseUtil.getGCD();
        Rotation previousRotation = RotationManager.getInstance().getServerRotation();
        float adjustedYaw = adjustAxis(this.yaw, previousRotation.yaw, gcd);
        float adjustedPitch = adjustAxis(this.pitch, previousRotation.pitch, gcd);
        return new Rotation(adjustedYaw, class_3532.method_15363(adjustedPitch, -90.0f, 90.0f));
    }

    private float adjustAxis(float axisValue, float previousValue, double gcd) {
        float delta = axisValue - previousValue;
        return previousValue + (Math.round(((double) delta) / gcd) * ((float) gcd));
    }

    public class_243 getVector() {
        float f = this.pitch * 0.017453292f;
        float g = (-this.yaw) * 0.017453292f;
        float h = class_3532.method_15362(g);
        float i = class_3532.method_15374(g);
        float j = class_3532.method_15362(f);
        float k = class_3532.method_15374(f);
        return new class_243(i * j, -k, h * j);
    }

    public Rotation rotationDeltaTo(Rotation targetRotation) {
        return RotationUtil.calculateDelta(this, targetRotation);
    }

    public String toString() {
        return "Rotation(yaw=" + this.yaw + ", pitch=" + this.pitch + ")";
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/rotation/manager/Rotation$VecRotation.class */
    public static final class VecRotation extends Record {
        private final Rotation rotation;
        private final class_243 vec;

        public VecRotation(Rotation rotation, class_243 vec) {
            this.rotation = rotation;
            this.vec = vec;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, VecRotation.class), VecRotation.class, "rotation;vec", "FIELD:Lsweetie/leonware/api/utils/rotation/manager/Rotation$VecRotation;->rotation:Lsweetie/leonware/api/utils/rotation/manager/Rotation;", "FIELD:Lsweetie/leonware/api/utils/rotation/manager/Rotation$VecRotation;->vec:Lnet/minecraft/class_243;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, VecRotation.class, Object.class), VecRotation.class, "rotation;vec", "FIELD:Lsweetie/leonware/api/utils/rotation/manager/Rotation$VecRotation;->rotation:Lsweetie/leonware/api/utils/rotation/manager/Rotation;", "FIELD:Lsweetie/leonware/api/utils/rotation/manager/Rotation$VecRotation;->vec:Lnet/minecraft/class_243;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public Rotation rotation() {
            return this.rotation;
        }

        public class_243 vec() {
            return this.vec;
        }

        @Override // java.lang.Record
        public String toString() {
            return "VecRotation(rotation=" + String.valueOf(this.rotation) + ", vec=" + String.valueOf(this.vec) + ")";
        }
    }
}
