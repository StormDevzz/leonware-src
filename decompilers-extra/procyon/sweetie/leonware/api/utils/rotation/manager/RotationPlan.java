// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.rotation.manager;

import lombok.Generated;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.module.Module;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class RotationPlan implements QuickImports
{
    private final Rotation rotation;
    private final class_243 vec3d;
    private final class_1297 entity;
    private final RotationMode rotationMode;
    private final int ticksUntilReset;
    private final float resetThreshold;
    private final boolean moveCorrection;
    private boolean freeMoveCorrection;
    private boolean clientLook;
    private final Module provider;
    
    public RotationPlan(final Rotation rotation, final class_243 vec3d, final class_1297 entity, final RotationMode rotationMode, final int ticksUntilReset, final float resetThreshold, final boolean moveCorrection, final boolean freeMoveCorrection, final Module provider) {
        this.rotation = rotation;
        this.vec3d = vec3d;
        this.entity = entity;
        this.rotationMode = rotationMode;
        this.ticksUntilReset = ticksUntilReset;
        this.resetThreshold = resetThreshold;
        this.moveCorrection = moveCorrection;
        this.freeMoveCorrection = freeMoveCorrection;
        this.provider = provider;
    }
    
    public Rotation nextRotation(final Rotation fromRotation, final boolean isResetting) {
        if (isResetting) {
            return this.rotationMode.process(fromRotation, RotationUtil.fromVec2f(RotationPlan.mc.field_1724.method_5802()));
        }
        return this.rotationMode.process(fromRotation, this.rotation, this.vec3d, this.entity);
    }
    
    @Generated
    public Rotation rotation() {
        return this.rotation;
    }
    
    @Generated
    public class_243 vec3d() {
        return this.vec3d;
    }
    
    @Generated
    public class_1297 entity() {
        return this.entity;
    }
    
    @Generated
    public RotationMode rotationMode() {
        return this.rotationMode;
    }
    
    @Generated
    public int ticksUntilReset() {
        return this.ticksUntilReset;
    }
    
    @Generated
    public float resetThreshold() {
        return this.resetThreshold;
    }
    
    @Generated
    public boolean moveCorrection() {
        return this.moveCorrection;
    }
    
    @Generated
    public boolean freeMoveCorrection() {
        return this.freeMoveCorrection;
    }
    
    @Generated
    public boolean clientLook() {
        return this.clientLook;
    }
    
    @Generated
    public Module provider() {
        return this.provider;
    }
    
    @Generated
    public RotationPlan freeMoveCorrection(final boolean freeMoveCorrection) {
        this.freeMoveCorrection = freeMoveCorrection;
        return this;
    }
    
    @Generated
    public RotationPlan clientLook(final boolean clientLook) {
        this.clientLook = clientLook;
        return this;
    }
}
