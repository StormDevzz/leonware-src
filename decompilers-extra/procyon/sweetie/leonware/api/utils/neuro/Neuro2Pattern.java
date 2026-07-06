// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.neuro;

import lombok.Generated;
import java.io.Serializable;

public class Neuro2Pattern implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final float yawOffset;
    private final float pitchOffset;
    private final double distance;
    private final double targetSpeed;
    private final long timestamp;
    private final boolean onGround;
    
    public Neuro2Pattern(final float yawOffset, final float pitchOffset, final double distance, final double targetSpeed, final boolean onGround) {
        this.yawOffset = yawOffset;
        this.pitchOffset = pitchOffset;
        this.distance = distance;
        this.targetSpeed = targetSpeed;
        this.timestamp = System.currentTimeMillis();
        this.onGround = onGround;
    }
    
    @Generated
    public float getYawOffset() {
        return this.yawOffset;
    }
    
    @Generated
    public float getPitchOffset() {
        return this.pitchOffset;
    }
    
    @Generated
    public double getDistance() {
        return this.distance;
    }
    
    @Generated
    public double getTargetSpeed() {
        return this.targetSpeed;
    }
    
    @Generated
    public long getTimestamp() {
        return this.timestamp;
    }
    
    @Generated
    public boolean isOnGround() {
        return this.onGround;
    }
}
