// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.rotation.manager;

import sweetie.leonware.api.utils.rotation.rotations.InstantRotation;
import lombok.Generated;
import sweetie.leonware.api.module.Module;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.rotation.rotations.SmoothRotation;

public class RotationStrategy
{
    private final RotationMode rotationMode;
    private final boolean moveCorrection;
    private final boolean freeCorrection;
    private final float resetThreshold = 2.0f;
    private int ticksUntilReset;
    private boolean clientLook;
    public static final RotationStrategy SMOOTH_FREE;
    public static final RotationStrategy SMOOTH_FOCUS;
    public static final RotationStrategy TARGET;
    
    public RotationStrategy(final RotationMode rotationMode, final boolean moveCorrection) {
        this.ticksUntilReset = 5;
        this.rotationMode = rotationMode;
        this.moveCorrection = moveCorrection;
        this.freeCorrection = false;
    }
    
    public RotationStrategy(final RotationMode rotationMode, final boolean moveCorrection, final boolean freeCorrection) {
        this.ticksUntilReset = 5;
        this.rotationMode = rotationMode;
        this.moveCorrection = moveCorrection;
        this.freeCorrection = freeCorrection;
    }
    
    public RotationStrategy(final boolean moveCorrection) {
        this(new SmoothRotation(), moveCorrection);
    }
    
    public RotationStrategy() {
        this(new SmoothRotation(), true, true);
    }
    
    public RotationPlan createRotationPlan(final Rotation rotation, final class_243 vec, final class_1297 entity, final Module provider) {
        return new RotationPlan(rotation, vec, entity, this.rotationMode, this.ticksUntilReset, 2.0f, this.moveCorrection, this.freeCorrection, provider).clientLook(this.clientLook);
    }
    
    public RotationPlan createRotationPlan(final Rotation rotation, final Module provider) {
        return new RotationPlan(rotation, null, null, this.rotationMode, this.ticksUntilReset, 2.0f, this.moveCorrection, this.freeCorrection, provider).clientLook(this.clientLook);
    }
    
    public RotationPlan createRotationPlan(final Rotation rotation, final class_243 vec, final class_1297 entity, final boolean moveCorrection, final boolean freeCorrection, final Module provider) {
        return new RotationPlan(rotation, vec, entity, this.rotationMode, this.ticksUntilReset, 2.0f, moveCorrection, freeCorrection, provider).clientLook(this.clientLook);
    }
    
    @Generated
    public RotationStrategy ticksUntilReset(final int ticksUntilReset) {
        this.ticksUntilReset = ticksUntilReset;
        return this;
    }
    
    @Generated
    public RotationStrategy clientLook(final boolean clientLook) {
        this.clientLook = clientLook;
        return this;
    }
    
    static {
        SMOOTH_FREE = new RotationStrategy(new SmoothRotation(), true, true);
        SMOOTH_FOCUS = new RotationStrategy(new SmoothRotation(), true);
        TARGET = new RotationStrategy(new InstantRotation(), true);
    }
}
