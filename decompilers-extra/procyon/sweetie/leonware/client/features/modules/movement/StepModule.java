// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_1324;
import net.minecraft.class_5134;
import sweetie.leonware.api.event.EventListener;
import net.minecraft.class_2596;
import sweetie.leonware.api.utils.other.NetworkUtil;
import net.minecraft.class_2828;
import sweetie.leonware.api.event.events.player.move.MotionEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.utils.world.HoleUtility;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Step", category = Category.MOVEMENT)
public class StepModule extends Module
{
    private static final StepModule instance;
    private final BooleanSetting strict;
    private final SliderSetting height;
    private final BooleanSetting pauseIfShift;
    private final SliderSetting stepDelay;
    private final BooleanSetting holeDisable;
    private final ModeSetting mode;
    private final TimerUtil stepTimer;
    private boolean alreadyInHole;
    
    public StepModule() {
        this.strict = new BooleanSetting("Strict").value(false);
        this.height = new SliderSetting("Height").value(2.0f).range(1.0f, 2.5f).step(0.1f).setVisible(() -> !this.strict.getValue());
        this.pauseIfShift = new BooleanSetting("Pause If Sneak").value(false);
        this.stepDelay = new SliderSetting("Step Delay").value(200.0f).range(0.0f, 1000.0f).step(10.0f);
        this.holeDisable = new BooleanSetting("Hole Disable").value(false);
        this.mode = new ModeSetting("Mode").value("NCP").values("NCP", "Vanilla");
        this.stepTimer = new TimerUtil();
        this.alreadyInHole = false;
        this.addSettings(this.strict, this.height, this.pauseIfShift, this.stepDelay, this.holeDisable, this.mode);
    }
    
    @Override
    public void onEnable() {
        if (StepModule.mc.field_1724 != null) {
            this.alreadyInHole = HoleUtility.isHole(StepModule.mc.field_1724.method_24515());
        }
    }
    
    @Override
    public void onDisable() {
        this.setStepHeight(0.6f);
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (StepModule.mc.field_1724 == null || StepModule.mc.field_1687 == null) {
                return;
            }
            else {
                if (this.holeDisable.getValue()) {
                    final boolean inHole = HoleUtility.isHole(StepModule.mc.field_1724.method_24515());
                    if (inHole && !this.alreadyInHole) {
                        this.disable();
                        return;
                    }
                    else {
                        this.alreadyInHole = inHole;
                    }
                }
                if (this.pauseIfShift.getValue() && StepModule.mc.field_1690.field_1832.method_1434()) {
                    this.setStepHeight(0.6f);
                    return;
                }
                else if (StepModule.mc.field_1724.method_31549().field_7479 || StepModule.mc.field_1724.method_3144() || StepModule.mc.field_1724.method_5799()) {
                    this.setStepHeight(0.6f);
                    return;
                }
                else {
                    final float targetHeight = this.strict.getValue() ? 1.0f : this.height.getValue();
                    if (StepModule.mc.field_1724.method_24828() && this.stepTimer.finished(this.stepDelay.getValue().longValue())) {
                        this.setStepHeight(targetHeight);
                    }
                    else {
                        this.setStepHeight(0.6f);
                    }
                    return;
                }
            }
        }));
        final EventListener motionEvent = MotionEvent.getInstance().subscribe(new Listener<MotionEvent.MotionEventData>(event -> {
            if (StepModule.mc.field_1724 == null || !this.mode.is("NCP")) {
                return;
            }
            else {
                final double stepY = StepModule.mc.field_1724.method_23318() - StepModule.mc.field_1724.field_6036;
                final float maxH = this.strict.getValue() ? 1.0f : this.height.getValue();
                if (stepY <= 0.75 || stepY > maxH) {
                    return;
                }
                else {
                    final double[] offsets = this.getOffset(stepY);
                    if (offsets == null || offsets.length <= 1) {
                        return;
                    }
                    else {
                        final double[] array;
                        int i = 0;
                        for (int length = array.length; i < length; ++i) {
                            final double offset = array[i];
                            NetworkUtil.sendSilentPacket((class_2596<?>)new class_2828.class_2829(StepModule.mc.field_1724.field_6014, StepModule.mc.field_1724.field_6036 + offset, StepModule.mc.field_1724.field_5969, false, StepModule.mc.field_1724.field_5976));
                        }
                        if (this.strict.getValue()) {
                            NetworkUtil.sendSilentPacket((class_2596<?>)new class_2828.class_2829(StepModule.mc.field_1724.field_6014, StepModule.mc.field_1724.field_6036 + stepY, StepModule.mc.field_1724.field_5969, false, StepModule.mc.field_1724.field_5976));
                        }
                        this.stepTimer.reset();
                        return;
                    }
                }
            }
        }));
        this.addEvents(updateEvent, motionEvent);
    }
    
    private double[] getOffset(final double h) {
        return switch ((int)(h * 10000.0)) {
            case 7500,  10000 -> new double[] { 0.42, 0.753 };
            case 8125,  8750 -> new double[] { 0.39, 0.7 };
            case 15000 -> new double[] { 0.42, 0.75, 1.0, 1.16, 1.23, 1.2 };
            case 20000 -> new double[] { 0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43 };
            case 25000 -> new double[] { 0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907 };
            default -> null;
        };
    }
    
    private void setStepHeight(final float v) {
        if (StepModule.mc.field_1724 == null) {
            return;
        }
        final class_1324 attr = StepModule.mc.field_1724.method_5996(class_5134.field_47761);
        if (attr != null) {
            attr.method_6192((double)v);
        }
    }
    
    @Generated
    public static StepModule getInstance() {
        return StepModule.instance;
    }
    
    static {
        instance = new StepModule();
    }
}
