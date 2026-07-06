/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.utils.neuro;

import java.io.Serializable;
import lombok.Generated;

public class Neuro2Pattern
implements Serializable {
    private static final long serialVersionUID = 1L;
    private final float yawOffset;
    private final float pitchOffset;
    private final double distance;
    private final double targetSpeed;
    private final long timestamp;
    private final boolean onGround;

    public Neuro2Pattern(float yawOffset, float pitchOffset, double distance, double targetSpeed, boolean onGround) {
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

