/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.utils.neuro;

import lombok.Generated;

public class FeaturesData {
    private final int onGround;
    private final int miniHitbox;
    private final float yawDelta;
    private final float pitchDelta;
    private final float targetYaw;
    private final float targetPitch;
    private final float sinceAttack;
    private final float distance;
    private final float playerYaw;
    private final float playerPitch;
    private final float playerPosX;
    private final float playerPosY;
    private final float playerPosZ;
    private final float playerVelX;
    private final float playerVelY;
    private final float playerVelZ;
    private final float targetPosX;
    private final float targetPosY;
    private final float targetPosZ;
    private final float targetVelX;
    private final float targetVelY;
    private final float targetVelZ;
    private final float deltaX;
    private final float deltaY;
    private final float deltaZ;

    public FeaturesData(float yawDelta, float pitchDelta, float targetYaw, float targetPitch, float sinceAttack, float distance, int onGround, int miniHitbox, float playerYaw, float playerPitch, float playerPosX, float playerPosY, float playerPosZ, float playerVelX, float playerVelY, float playerVelZ, float targetPosX, float targetPosY, float targetPosZ, float targetVelX, float targetVelY, float targetVelZ, float deltaX, float deltaY, float deltaZ) {
        this.yawDelta = yawDelta;
        this.pitchDelta = pitchDelta;
        this.targetYaw = targetYaw;
        this.targetPitch = targetPitch;
        this.sinceAttack = sinceAttack;
        this.distance = distance;
        this.onGround = onGround;
        this.miniHitbox = miniHitbox;
        this.playerYaw = playerYaw;
        this.playerPitch = playerPitch;
        this.playerPosX = playerPosX;
        this.playerPosY = playerPosY;
        this.playerPosZ = playerPosZ;
        this.playerVelX = playerVelX;
        this.playerVelY = playerVelY;
        this.playerVelZ = playerVelZ;
        this.targetPosX = targetPosX;
        this.targetPosY = targetPosY;
        this.targetPosZ = targetPosZ;
        this.targetVelX = targetVelX;
        this.targetVelY = targetVelY;
        this.targetVelZ = targetVelZ;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
    }

    @Generated
    public int onGround() {
        return this.onGround;
    }

    @Generated
    public int miniHitbox() {
        return this.miniHitbox;
    }

    @Generated
    public float yawDelta() {
        return this.yawDelta;
    }

    @Generated
    public float pitchDelta() {
        return this.pitchDelta;
    }

    @Generated
    public float targetYaw() {
        return this.targetYaw;
    }

    @Generated
    public float targetPitch() {
        return this.targetPitch;
    }

    @Generated
    public float sinceAttack() {
        return this.sinceAttack;
    }

    @Generated
    public float distance() {
        return this.distance;
    }

    @Generated
    public float playerYaw() {
        return this.playerYaw;
    }

    @Generated
    public float playerPitch() {
        return this.playerPitch;
    }

    @Generated
    public float playerPosX() {
        return this.playerPosX;
    }

    @Generated
    public float playerPosY() {
        return this.playerPosY;
    }

    @Generated
    public float playerPosZ() {
        return this.playerPosZ;
    }

    @Generated
    public float playerVelX() {
        return this.playerVelX;
    }

    @Generated
    public float playerVelY() {
        return this.playerVelY;
    }

    @Generated
    public float playerVelZ() {
        return this.playerVelZ;
    }

    @Generated
    public float targetPosX() {
        return this.targetPosX;
    }

    @Generated
    public float targetPosY() {
        return this.targetPosY;
    }

    @Generated
    public float targetPosZ() {
        return this.targetPosZ;
    }

    @Generated
    public float targetVelX() {
        return this.targetVelX;
    }

    @Generated
    public float targetVelY() {
        return this.targetVelY;
    }

    @Generated
    public float targetVelZ() {
        return this.targetVelZ;
    }

    @Generated
    public float deltaX() {
        return this.deltaX;
    }

    @Generated
    public float deltaY() {
        return this.deltaY;
    }

    @Generated
    public float deltaZ() {
        return this.deltaZ;
    }
}

