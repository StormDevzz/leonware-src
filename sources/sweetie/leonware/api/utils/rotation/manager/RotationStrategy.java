package sweetie.leonware.api.utils.rotation.manager;

import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.utils.rotation.rotations.InstantRotation;
import sweetie.leonware.api.utils.rotation.rotations.SmoothRotation;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/rotation/manager/RotationStrategy.class */
public class RotationStrategy {
    private final RotationMode rotationMode;
    private final boolean moveCorrection;
    private final boolean freeCorrection;
    private final float resetThreshold = 2.0f;
    private int ticksUntilReset;
    private boolean clientLook;
    public static final RotationStrategy SMOOTH_FREE = new RotationStrategy(new SmoothRotation(), true, true);
    public static final RotationStrategy SMOOTH_FOCUS = new RotationStrategy(new SmoothRotation(), true);
    public static final RotationStrategy TARGET = new RotationStrategy(new InstantRotation(), true);

    @Generated
    public RotationStrategy ticksUntilReset(int ticksUntilReset) {
        this.ticksUntilReset = ticksUntilReset;
        return this;
    }

    @Generated
    public RotationStrategy clientLook(boolean clientLook) {
        this.clientLook = clientLook;
        return this;
    }

    public RotationStrategy(RotationMode rotationMode, boolean moveCorrection) {
        this.resetThreshold = 2.0f;
        this.ticksUntilReset = 5;
        this.rotationMode = rotationMode;
        this.moveCorrection = moveCorrection;
        this.freeCorrection = false;
    }

    public RotationStrategy(RotationMode rotationMode, boolean moveCorrection, boolean freeCorrection) {
        this.resetThreshold = 2.0f;
        this.ticksUntilReset = 5;
        this.rotationMode = rotationMode;
        this.moveCorrection = moveCorrection;
        this.freeCorrection = freeCorrection;
    }

    public RotationStrategy(boolean moveCorrection) {
        this(new SmoothRotation(), moveCorrection);
    }

    public RotationStrategy() {
        this(new SmoothRotation(), true, true);
    }

    public RotationPlan createRotationPlan(Rotation rotation, class_243 vec, class_1297 entity, Module provider) {
        return new RotationPlan(rotation, vec, entity, this.rotationMode, this.ticksUntilReset, 2.0f, this.moveCorrection, this.freeCorrection, provider).clientLook(this.clientLook);
    }

    public RotationPlan createRotationPlan(Rotation rotation, Module provider) {
        return new RotationPlan(rotation, null, null, this.rotationMode, this.ticksUntilReset, 2.0f, this.moveCorrection, this.freeCorrection, provider).clientLook(this.clientLook);
    }

    public RotationPlan createRotationPlan(Rotation rotation, class_243 vec, class_1297 entity, boolean moveCorrection, boolean freeCorrection, Module provider) {
        return new RotationPlan(rotation, vec, entity, this.rotationMode, this.ticksUntilReset, 2.0f, moveCorrection, freeCorrection, provider).clientLook(this.clientLook);
    }
}
