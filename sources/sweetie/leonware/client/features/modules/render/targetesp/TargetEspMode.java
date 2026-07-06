package sweetie.leonware.client.features.modules.render.targetesp;

import lombok.Generated;
import net.minecraft.class_1309;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.client.features.modules.combat.AuraModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/targetesp/TargetEspMode.class */
public abstract class TargetEspMode implements QuickImports {
    public float prevShowAnimation = 0.0f;
    public float prevSizeAnimation = 0.0f;
    public static final AnimationUtil showAnimation = new AnimationUtil();
    public static final AnimationUtil sizeAnimation = new AnimationUtil();
    public static class_1309 currentTarget = null;
    private static double targetX = -1.0d;
    private static double targetY = -1.0d;
    private static double targetZ = -1.0d;
    private static double lastTargetX = -1.0d;
    private static double lastTargetY = -1.0d;
    private static double lastTargetZ = -1.0d;

    public abstract void onUpdate();

    public abstract void onRender3D(Render3DEvent.Render3DEventData render3DEventData);

    public AuraModule aura() {
        return AuraModule.getInstance();
    }

    public void updateTarget() {
        if (aura().target != null) {
            currentTarget = aura().target;
        }
    }

    public void updateAnimation(long duration, String mode, float size, float in, float out) {
        double d;
        this.prevShowAnimation = (float) showAnimation.getValue();
        this.prevSizeAnimation = (float) sizeAnimation.getValue();
        sizeAnimation.update();
        switch (mode) {
            case "In":
                d = in;
                break;
            case "Out":
                d = out;
                break;
            default:
                d = size;
                break;
        }
        double dyingSize = d;
        sizeAnimation.run(reason() ? size : dyingSize, duration, Easing.SINE_OUT);
        showAnimation.update();
        showAnimation.run(reason() ? 1.0d : 0.0d, duration, Easing.SINE_OUT);
    }

    public boolean reason() {
        return aura().isEnabled() && aura().target != null;
    }

    public boolean canDraw() {
        return (mc.field_1724 == null || mc.field_1687 == null || showAnimation.getValue() <= 0.0d) ? false : true;
    }

    public static void updatePositions() {
        float animationValue = (float) showAnimation.getValue();
        float animationTarget = (float) showAnimation.getToValue();
        boolean useLastPosition = TargetEspModule.getInstance().lastPosition.getValue().booleanValue();
        boolean preventUpdate = useLastPosition && ((double) animationTarget) == 0.0d && animationValue <= 0.9f;
        if (currentTarget != null && !preventUpdate) {
            lastTargetX = MathUtil.interpolate((float) currentTarget.field_6014, (float) currentTarget.method_23317());
            lastTargetY = MathUtil.interpolate((float) currentTarget.field_6036, (float) currentTarget.method_23318());
            lastTargetZ = MathUtil.interpolate((float) currentTarget.field_5969, (float) currentTarget.method_23321());
        }
        targetX = lastTargetX;
        targetY = lastTargetY;
        targetZ = lastTargetZ;
    }

    @Generated
    public static double getTargetX() {
        return targetX;
    }

    @Generated
    public static double getTargetY() {
        return targetY;
    }

    @Generated
    public static double getTargetZ() {
        return targetZ;
    }
}
