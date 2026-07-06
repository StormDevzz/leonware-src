// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.targetesp;

import lombok.Generated;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import net.minecraft.class_1309;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.system.interfaces.QuickImports;

public abstract class TargetEspMode implements QuickImports
{
    public static final AnimationUtil showAnimation;
    public static final AnimationUtil sizeAnimation;
    public static class_1309 currentTarget;
    public float prevShowAnimation;
    public float prevSizeAnimation;
    private static double targetX;
    private static double targetY;
    private static double targetZ;
    private static double lastTargetX;
    private static double lastTargetY;
    private static double lastTargetZ;
    
    public TargetEspMode() {
        this.prevShowAnimation = 0.0f;
        this.prevSizeAnimation = 0.0f;
    }
    
    public AuraModule aura() {
        return AuraModule.getInstance();
    }
    
    public void updateTarget() {
        if (this.aura().target != null) {
            TargetEspMode.currentTarget = this.aura().target;
        }
    }
    
    public void updateAnimation(final long duration, final String mode, final float size, final float in, final float out) {
        this.prevShowAnimation = (float)TargetEspMode.showAnimation.getValue();
        this.prevSizeAnimation = (float)TargetEspMode.sizeAnimation.getValue();
        TargetEspMode.sizeAnimation.update();
        final double dyingSize = switch (mode) {
            case "In" -> in;
            case "Out" -> out;
            default -> size;
        };
        TargetEspMode.sizeAnimation.run(this.reason() ? size : dyingSize, duration, Easing.SINE_OUT);
        TargetEspMode.showAnimation.update();
        TargetEspMode.showAnimation.run(this.reason() ? 1.0 : 0.0, duration, Easing.SINE_OUT);
    }
    
    public boolean reason() {
        return this.aura().isEnabled() && this.aura().target != null;
    }
    
    public boolean canDraw() {
        return TargetEspMode.mc.field_1724 != null && TargetEspMode.mc.field_1687 != null && TargetEspMode.showAnimation.getValue() > 0.0;
    }
    
    public static void updatePositions() {
        final float animationValue = (float)TargetEspMode.showAnimation.getValue();
        final float animationTarget = (float)TargetEspMode.showAnimation.getToValue();
        final boolean useLastPosition = TargetEspModule.getInstance().lastPosition.getValue();
        final boolean preventUpdate = useLastPosition && animationTarget == 0.0 && animationValue <= 0.9f;
        if (TargetEspMode.currentTarget != null && !preventUpdate) {
            TargetEspMode.lastTargetX = MathUtil.interpolate((float)TargetEspMode.currentTarget.field_6014, (float)TargetEspMode.currentTarget.method_23317());
            TargetEspMode.lastTargetY = MathUtil.interpolate((float)TargetEspMode.currentTarget.field_6036, (float)TargetEspMode.currentTarget.method_23318());
            TargetEspMode.lastTargetZ = MathUtil.interpolate((float)TargetEspMode.currentTarget.field_5969, (float)TargetEspMode.currentTarget.method_23321());
        }
        TargetEspMode.targetX = TargetEspMode.lastTargetX;
        TargetEspMode.targetY = TargetEspMode.lastTargetY;
        TargetEspMode.targetZ = TargetEspMode.lastTargetZ;
    }
    
    public abstract void onUpdate();
    
    public abstract void onRender3D(final Render3DEvent.Render3DEventData p0);
    
    @Generated
    public static double getTargetX() {
        return TargetEspMode.targetX;
    }
    
    @Generated
    public static double getTargetY() {
        return TargetEspMode.targetY;
    }
    
    @Generated
    public static double getTargetZ() {
        return TargetEspMode.targetZ;
    }
    
    static {
        showAnimation = new AnimationUtil();
        sizeAnimation = new AnimationUtil();
        TargetEspMode.currentTarget = null;
        TargetEspMode.targetX = -1.0;
        TargetEspMode.targetY = -1.0;
        TargetEspMode.targetZ = -1.0;
        TargetEspMode.lastTargetX = -1.0;
        TargetEspMode.lastTargetY = -1.0;
        TargetEspMode.lastTargetZ = -1.0;
    }
}
