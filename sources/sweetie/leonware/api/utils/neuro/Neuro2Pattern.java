package sweetie.leonware.api.utils.neuro;

import java.io.Serializable;
import lombok.Generated;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/neuro/Neuro2Pattern.class */
public class Neuro2Pattern implements Serializable {
    private static final long serialVersionUID = 1;
    private final float yawOffset;
    private final float pitchOffset;
    private final double distance;
    private final double targetSpeed;
    private final long timestamp = System.currentTimeMillis();
    private final boolean onGround;

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

    public Neuro2Pattern(float yawOffset, float pitchOffset, double distance, double targetSpeed, boolean onGround) {
        this.yawOffset = yawOffset;
        this.pitchOffset = pitchOffset;
        this.distance = distance;
        this.targetSpeed = targetSpeed;
        this.onGround = onGround;
    }
}
