package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_1324;
import net.minecraft.class_2596;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/StepModule.class */
@ModuleRegister(name = "Step", category = Category.MOVEMENT)
public class StepModule extends Module {
    private static final StepModule instance = new StepModule();
    private final BooleanSetting strict = new BooleanSetting("Strict").value((Boolean) false);
    private final SliderSetting height = new SliderSetting("Height").value(Float.valueOf(2.0f)).range(1.0f, 2.5f).step(0.1f).setVisible(() -> {
        return Boolean.valueOf(!this.strict.getValue().booleanValue());
    });
    private final BooleanSetting pauseIfShift = new BooleanSetting("Pause If Sneak").value((Boolean) false);
    private final SliderSetting stepDelay = new SliderSetting("Step Delay").value(Float.valueOf(200.0f)).range(0.0f, 1000.0f).step(10.0f);
    private final BooleanSetting holeDisable = new BooleanSetting("Hole Disable").value((Boolean) false);
    private final ModeSetting mode = new ModeSetting("Mode").value("NCP").values("NCP", "Vanilla");
    private final TimerUtil stepTimer = new TimerUtil();
    private boolean alreadyInHole = false;

    @Generated
    public static StepModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v6, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public StepModule() {
        addSettings(this.strict, this.height, this.pauseIfShift, this.stepDelay, this.holeDisable, this.mode);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        if (mc.field_1724 != null) {
            this.alreadyInHole = HoleUtility.isHole(mc.field_1724.method_24515());
        }
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        setStepHeight(0.6f);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            if (this.holeDisable.getValue().booleanValue()) {
                boolean inHole = HoleUtility.isHole(mc.field_1724.method_24515());
                if (inHole && !this.alreadyInHole) {
                    disable();
                    return;
                }
                this.alreadyInHole = inHole;
            }
            if (this.pauseIfShift.getValue().booleanValue() && mc.field_1690.field_1832.method_1434()) {
                setStepHeight(0.6f);
                return;
            }
            if (mc.field_1724.method_31549().field_7479 || mc.field_1724.method_3144() || mc.field_1724.method_5799()) {
                setStepHeight(0.6f);
                return;
            }
            float targetHeight = this.strict.getValue().booleanValue() ? 1.0f : this.height.getValue().floatValue();
            if (mc.field_1724.method_24828() && this.stepTimer.finished(this.stepDelay.getValue().longValue())) {
                setStepHeight(targetHeight);
            } else {
                setStepHeight(0.6f);
            }
        }));
        EventListener motionEvent = MotionEvent.getInstance().subscribe(new Listener(event2 -> {
            double[] offsets;
            if (mc.field_1724 == null || !this.mode.is("NCP")) {
                return;
            }
            double stepY = mc.field_1724.method_23318() - mc.field_1724.field_6036;
            float maxH = this.strict.getValue().booleanValue() ? 1.0f : this.height.getValue().floatValue();
            if (stepY <= 0.75d || stepY > maxH || (offsets = getOffset(stepY)) == null || offsets.length <= 1) {
                return;
            }
            for (double offset : offsets) {
                NetworkUtil.sendSilentPacket((class_2596<?>) new class_2828.class_2829(mc.field_1724.field_6014, mc.field_1724.field_6036 + offset, mc.field_1724.field_5969, false, mc.field_1724.field_5976));
            }
            if (this.strict.getValue().booleanValue()) {
                NetworkUtil.sendSilentPacket((class_2596<?>) new class_2828.class_2829(mc.field_1724.field_6014, mc.field_1724.field_6036 + stepY, mc.field_1724.field_5969, false, mc.field_1724.field_5976));
            }
            this.stepTimer.reset();
        }));
        addEvents(updateEvent, motionEvent);
    }

    private double[] getOffset(double h) {
        switch ((int) (h * 10000.0d)) {
            case 7500:
            case 10000:
                return new double[]{0.42d, 0.753d};
            case 8125:
            case 8750:
                return new double[]{0.39d, 0.7d};
            case 15000:
                return new double[]{0.42d, 0.75d, 1.0d, 1.16d, 1.23d, 1.2d};
            case 20000:
                return new double[]{0.42d, 0.78d, 0.63d, 0.51d, 0.9d, 1.21d, 1.45d, 1.43d};
            case 25000:
                return new double[]{0.425d, 0.821d, 0.699d, 0.599d, 1.022d, 1.372d, 1.652d, 1.869d, 2.019d, 1.907d};
            default:
                return null;
        }
    }

    private void setStepHeight(float v) {
        class_1324 attr;
        if (mc.field_1724 != null && (attr = mc.field_1724.method_5996(class_5134.field_47761)) != null) {
            attr.method_6192(v);
        }
    }
}
