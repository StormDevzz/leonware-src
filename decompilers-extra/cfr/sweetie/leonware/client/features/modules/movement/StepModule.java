/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1324
 *  net.minecraft.class_2828$class_2829
 *  net.minecraft.class_5134
 */
package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_1324;
import net.minecraft.class_2828;
import net.minecraft.class_5134;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.move.MotionEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.other.NetworkUtil;
import sweetie.leonware.api.utils.world.HoleUtility;

@ModuleRegister(name="Step", category=Category.MOVEMENT)
public class StepModule
extends Module {
    private static final StepModule instance = new StepModule();
    private final BooleanSetting strict = new BooleanSetting("Strict").value(false);
    private final SliderSetting height = new SliderSetting("Height").value(Float.valueOf(2.0f)).range(1.0f, 2.5f).step(0.1f).setVisible(() -> (Boolean)this.strict.getValue() == false);
    private final BooleanSetting pauseIfShift = new BooleanSetting("Pause If Sneak").value(false);
    private final SliderSetting stepDelay = new SliderSetting("Step Delay").value(Float.valueOf(200.0f)).range(0.0f, 1000.0f).step(10.0f);
    private final BooleanSetting holeDisable = new BooleanSetting("Hole Disable").value(false);
    private final ModeSetting mode = new ModeSetting("Mode").value("NCP").values("NCP", "Vanilla");
    private final TimerUtil stepTimer = new TimerUtil();
    private boolean alreadyInHole = false;

    public StepModule() {
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
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            float targetHeight;
            if (StepModule.mc.field_1724 == null || StepModule.mc.field_1687 == null) {
                return;
            }
            if (((Boolean)this.holeDisable.getValue()).booleanValue()) {
                boolean inHole = HoleUtility.isHole(StepModule.mc.field_1724.method_24515());
                if (inHole && !this.alreadyInHole) {
                    this.disable();
                    return;
                }
                this.alreadyInHole = inHole;
            }
            if (((Boolean)this.pauseIfShift.getValue()).booleanValue() && StepModule.mc.field_1690.field_1832.method_1434()) {
                this.setStepHeight(0.6f);
                return;
            }
            if (StepModule.mc.field_1724.method_31549().field_7479 || StepModule.mc.field_1724.method_3144() || StepModule.mc.field_1724.method_5799()) {
                this.setStepHeight(0.6f);
                return;
            }
            float f = targetHeight = (Boolean)this.strict.getValue() != false ? 1.0f : ((Float)this.height.getValue()).floatValue();
            if (StepModule.mc.field_1724.method_24828() && this.stepTimer.finished(((Float)this.stepDelay.getValue()).longValue())) {
                this.setStepHeight(targetHeight);
            } else {
                this.setStepHeight(0.6f);
            }
        }));
        EventListener motionEvent = MotionEvent.getInstance().subscribe(new Listener<MotionEvent.MotionEventData>(event -> {
            float maxH;
            if (StepModule.mc.field_1724 == null || !this.mode.is("NCP")) {
                return;
            }
            double stepY = StepModule.mc.field_1724.method_23318() - StepModule.mc.field_1724.field_6036;
            float f = maxH = (Boolean)this.strict.getValue() != false ? 1.0f : ((Float)this.height.getValue()).floatValue();
            if (stepY <= 0.75 || stepY > (double)maxH) {
                return;
            }
            double[] offsets = this.getOffset(stepY);
            if (offsets == null || offsets.length <= 1) {
                return;
            }
            for (double offset : offsets) {
                NetworkUtil.sendSilentPacket(new class_2828.class_2829(StepModule.mc.field_1724.field_6014, StepModule.mc.field_1724.field_6036 + offset, StepModule.mc.field_1724.field_5969, false, StepModule.mc.field_1724.field_5976));
            }
            if (((Boolean)this.strict.getValue()).booleanValue()) {
                NetworkUtil.sendSilentPacket(new class_2828.class_2829(StepModule.mc.field_1724.field_6014, StepModule.mc.field_1724.field_6036 + stepY, StepModule.mc.field_1724.field_5969, false, StepModule.mc.field_1724.field_5976));
            }
            this.stepTimer.reset();
        }));
        this.addEvents(updateEvent, motionEvent);
    }

    private double[] getOffset(double h) {
        double[] dArray;
        switch ((int)(h * 10000.0)) {
            case 7500: 
            case 10000: {
                double[] dArray2 = new double[2];
                dArray2[0] = 0.42;
                dArray = dArray2;
                dArray2[1] = 0.753;
                break;
            }
            case 8125: 
            case 8750: {
                double[] dArray3 = new double[2];
                dArray3[0] = 0.39;
                dArray = dArray3;
                dArray3[1] = 0.7;
                break;
            }
            case 15000: {
                double[] dArray4 = new double[6];
                dArray4[0] = 0.42;
                dArray4[1] = 0.75;
                dArray4[2] = 1.0;
                dArray4[3] = 1.16;
                dArray4[4] = 1.23;
                dArray = dArray4;
                dArray4[5] = 1.2;
                break;
            }
            case 20000: {
                double[] dArray5 = new double[8];
                dArray5[0] = 0.42;
                dArray5[1] = 0.78;
                dArray5[2] = 0.63;
                dArray5[3] = 0.51;
                dArray5[4] = 0.9;
                dArray5[5] = 1.21;
                dArray5[6] = 1.45;
                dArray = dArray5;
                dArray5[7] = 1.43;
                break;
            }
            case 25000: {
                double[] dArray6 = new double[10];
                dArray6[0] = 0.425;
                dArray6[1] = 0.821;
                dArray6[2] = 0.699;
                dArray6[3] = 0.599;
                dArray6[4] = 1.022;
                dArray6[5] = 1.372;
                dArray6[6] = 1.652;
                dArray6[7] = 1.869;
                dArray6[8] = 2.019;
                dArray = dArray6;
                dArray6[9] = 1.907;
                break;
            }
            default: {
                dArray = null;
            }
        }
        return dArray;
    }

    private void setStepHeight(float v) {
        if (StepModule.mc.field_1724 == null) {
            return;
        }
        class_1324 attr = StepModule.mc.field_1724.method_5996(class_5134.field_47761);
        if (attr != null) {
            attr.method_6192((double)v);
        }
    }

    @Generated
    public static StepModule getInstance() {
        return instance;
    }
}

