// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.player;

import lombok.Generated;
import net.minecraft.class_10185;
import sweetie.leonware.api.system.interfaces.IPlayerInput;
import net.minecraft.class_744;

public final class DirectionalInput
{
    private boolean forwards;
    private boolean backwards;
    private boolean left;
    private boolean right;
    public static final DirectionalInput NONE;
    public static final DirectionalInput FORWARDS;
    public static final DirectionalInput BACKWARDS;
    public static final DirectionalInput LEFT;
    public static final DirectionalInput RIGHT;
    
    public DirectionalInput(final boolean forwards, final boolean backwards, final boolean left, final boolean right) {
        this.forwards = forwards;
        this.backwards = backwards;
        this.left = left;
        this.right = right;
    }
    
    public DirectionalInput(final class_744 input) {
        this(((IPlayerInput)input).evelina$getUntransformed());
    }
    
    public DirectionalInput(final class_10185 input) {
        this(input.comp_3159(), input.comp_3160(), input.comp_3161(), input.comp_3162());
    }
    
    public DirectionalInput(final float movementForward, final float movementSideways) {
        this(movementForward > 0.0f, movementForward < 0.0f, movementSideways > 0.0f, movementSideways < 0.0f);
    }
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof final DirectionalInput that) {
            return this.forwards == that.forwards && this.backwards == that.backwards && this.left == that.left && this.right == that.right;
        }
        return false;
    }
    
    @Override
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
    public void setForwards(final boolean forwards) {
        this.forwards = forwards;
    }
    
    @Generated
    public void setBackwards(final boolean backwards) {
        this.backwards = backwards;
    }
    
    @Generated
    public void setLeft(final boolean left) {
        this.left = left;
    }
    
    @Generated
    public void setRight(final boolean right) {
        this.right = right;
    }
    
    static {
        NONE = new DirectionalInput(false, false, false, false);
        FORWARDS = new DirectionalInput(true, false, false, false);
        BACKWARDS = new DirectionalInput(false, true, false, false);
        LEFT = new DirectionalInput(false, false, true, false);
        RIGHT = new DirectionalInput(false, false, false, true);
    }
}
