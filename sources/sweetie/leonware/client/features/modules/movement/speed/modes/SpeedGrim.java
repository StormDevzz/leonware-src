package sweetie.leonware.client.features.modules.movement.speed.modes;

import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.class_1309;
import net.minecraft.class_1531;
import net.minecraft.class_746;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.client.TimerManager;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.api.utils.player.PlayerUtil;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.client.features.modules.movement.speed.SpeedMode;
import sweetie.leonware.client.features.modules.movement.speed.SpeedModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim.class */
public class SpeedGrim extends SpeedMode {
    private boolean boosting;
    public BypassType bypassType = BypassType.COLLIDE;
    private final TimerUtil timerUtil = new TimerUtil();
    private final ModeSetting grimType = new ModeSetting("Grim mode").value((Enum<?>) BypassType.COLLIDE).values(BypassType.values()).onAction2(() -> {
        BypassType bypassType;
        switch (getGrimType().getValue()) {
            case "Timer":
                bypassType = BypassType.TIMER;
                break;
            case "Collide new":
                bypassType = BypassType.COLLIDE_NEW;
                break;
            default:
                bypassType = BypassType.COLLIDE;
                break;
        }
        this.bypassType = bypassType;
    });
    private final SliderSetting collideNewSpeed = new SliderSetting("Collide Speed").value(Float.valueOf(0.08f)).range(0.03f, 0.15f).step(0.01f);
    private final BooleanSetting onlyInAir = new BooleanSetting("Only in Air").value((Boolean) false);

    @Override // sweetie.leonware.api.system.backend.Choice, sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
    public String getName() {
        return "Grim";
    }

    @Generated
    public ModeSetting getGrimType() {
        return this.grimType;
    }

    @Generated
    public SliderSetting getCollideNewSpeed() {
        return this.collideNewSpeed;
    }

    @Generated
    public BooleanSetting getOnlyInAir() {
        return this.onlyInAir;
    }

    /* JADX WARN: Type inference failed for: r1v5, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    public SpeedGrim(Supplier<Boolean> condition) {
        this.collideNewSpeed.setVisible(() -> {
            return Boolean.valueOf(((Boolean) condition.get()).booleanValue() && this.bypassType == BypassType.COLLIDE_NEW);
        });
        this.onlyInAir.setVisible(() -> {
            return Boolean.valueOf(((Boolean) condition.get()).booleanValue() && this.bypassType == BypassType.COLLIDE_NEW);
        });
        this.grimType.setVisible(condition);
        addSettings(this.grimType, this.collideNewSpeed, this.onlyInAir);
    }

    @Override // sweetie.leonware.client.features.modules.movement.speed.SpeedMode
    public void onTravel() {
        class_746 class_746Var;
        switch (this.bypassType) {
            case COLLIDE:
            case COLLIDE_NEW:
                boolean newMode = this.bypassType == BypassType.COLLIDE_NEW;
                if (!newMode || !this.onlyInAir.getValue().booleanValue() || !mc.field_1724.method_24828()) {
                    int collisions = 0;
                    for (class_746 class_746Var2 : mc.field_1687.method_18112()) {
                        if ((class_746Var2 instanceof class_1309) && (class_746Var = (class_1309) class_746Var2) != mc.field_1724 && !(class_746Var instanceof class_1531)) {
                            if (PlayerUtil.hasCollisionWith(class_746Var, newMode ? 0.0f : 1.0f)) {
                                collisions++;
                            }
                        }
                    }
                    if (collisions > 0) {
                        double[] forward = MoveUtil.forward(this.collideNewSpeed.getValue().floatValue() * collisions);
                        mc.field_1724.method_5762(forward[0], 0.0d, forward[1]);
                    }
                }
                break;
            default:
                if (this.timerUtil.finished(1100L)) {
                    this.boosting = true;
                }
                if (this.timerUtil.finished(7000L)) {
                    this.boosting = false;
                    this.timerUtil.reset();
                }
                TimerManager.getInstance().addTimer(this.boosting ? mc.field_1724.field_6012 % 2 == 0 ? 1.5f : 1.2f : 0.05f, TaskPriority.HIGH, SpeedModule.getInstance(), 1);
                break;
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/speed/modes/SpeedGrim$BypassType.class */
    public enum BypassType implements ModeSetting.NamedChoice {
        COLLIDE("Collide"),
        COLLIDE_NEW("Collide new"),
        TIMER("Timer");

        private final String name;

        BypassType(String name) {
            this.name = name;
        }

        @Override // sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
        public String getName() {
            return this.name;
        }
    }
}
