/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_10185
 *  net.minecraft.class_744
 */
package sweetie.leonware.api.utils.player;

import lombok.Generated;
import net.minecraft.class_10185;
import net.minecraft.class_744;
import sweetie.leonware.api.system.interfaces.IPlayerInput;

public final class DirectionalInput {
    private boolean forwards;
    private boolean backwards;
    private boolean left;
    private boolean right;
    public static final DirectionalInput NONE = new DirectionalInput(false, false, false, false);
    public static final DirectionalInput FORWARDS = new DirectionalInput(true, false, false, false);
    public static final DirectionalInput BACKWARDS = new DirectionalInput(false, true, false, false);
    public static final DirectionalInput LEFT = new DirectionalInput(false, false, true, false);
    public static final DirectionalInput RIGHT = new DirectionalInput(false, false, false, true);

    public DirectionalInput(boolean forwards, boolean backwards, boolean left, boolean right) {
        this.forwards = forwards;
        this.backwards = backwards;
        this.left = left;
        this.right = right;
    }

    public DirectionalInput(class_744 input) {
        this(((IPlayerInput)input).evelina$getUntransformed());
    }

    public DirectionalInput(class_10185 input) {
        this(input.comp_3159(), input.comp_3160(), input.comp_3161(), input.comp_3162());
    }

    public DirectionalInput(float movementForward, float movementSideways) {
        this(movementForward > 0.0f, movementForward < 0.0f, movementSideways > 0.0f, movementSideways < 0.0f);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof DirectionalInput)) {
            return false;
        }
        DirectionalInput that = (DirectionalInput)other;
        return this.forwards == that.forwards && this.backwards == that.backwards && this.left == that.left && this.right == that.right;
    }

    public int hashCode() {
        int result = Boolean.hashCode(this.forwards);
        result = 31 * result + Boolean.hashCode(this.backwards);
        result = 31 * result + Boolean.hashCode(this.left);
        result = 31 * result + Boolean.hashCode(this.right);
        return result;
    }

    public boolean isMoving() {
        return this.forwards || this.backwards || this.left || this.right;
    }

    @Generated
    public boolean isForwards() {
        return this.forwards;
    }

    @Generated
    public boolean isBackwards() {
        return this.backwards;
    }

    @Generated
    public boolean isLeft() {
        return this.left;
    }

    @Generated
    public boolean isRight() {
        return this.right;
    }

    @Generated
    public void setForwards(boolean forwards) {
        this.forwards = forwards;
    }

    @Generated
    public void setBackwards(boolean backwards) {
        this.backwards = backwards;
    }

    @Generated
    public void setLeft(boolean left) {
        this.left = left;
    }

    @Generated
    public void setRight(boolean right) {
        this.right = right;
    }
}

