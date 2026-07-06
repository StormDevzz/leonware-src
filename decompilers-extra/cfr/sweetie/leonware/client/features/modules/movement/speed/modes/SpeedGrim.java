/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1531
 */
package sweetie.leonware.client.features.modules.movement.speed.modes;

import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1531;
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

public class SpeedGrim
extends SpeedMode {
    public BypassType bypassType = BypassType.COLLIDE;
    private boolean boosting;
    private final TimerUtil timerUtil = new TimerUtil();
    private final ModeSetting grimType = new ModeSetting("Grim mode").value(BypassType.COLLIDE).values(BypassType.values()).onAction(() -> {
        this.bypassType = switch ((String)this.getGrimType().getValue()) {
            case "Timer" -> BypassType.TIMER;
            case "Collide new" -> BypassType.COLLIDE_NEW;
            default -> BypassType.COLLIDE;
        };
    });
    private final SliderSetting collideNewSpeed = new SliderSetting("Collide Speed").value(Float.valueOf(0.08f)).range(0.03f, 0.15f).step(0.01f);
    private final BooleanSetting onlyInAir = new BooleanSetting("Only in Air").value(false);

    @Override
    public String getName() {
        return "Grim";
    }

    public SpeedGrim(Supplier<Boolean> condition) {
        this.collideNewSpeed.setVisible(() -> (Boolean)condition.get() != false && this.bypassType == BypassType.COLLIDE_NEW);
        this.onlyInAir.setVisible(() -> (Boolean)condition.get() != false && this.bypassType == BypassType.COLLIDE_NEW);
        this.grimType.setVisible((Supplier)condition);
        this.addSettings(this.grimType, this.collideNewSpeed, this.onlyInAir);
    }

    @Override
    public void onTravel() {
        switch (this.bypassType.ordinal()) {
            case 0: 
            case 1: {
                boolean newMode;
                boolean bl = newMode = this.bypassType == BypassType.COLLIDE_NEW;
                if (newMode && ((Boolean)this.onlyInAir.getValue()).booleanValue() && SpeedGrim.mc.field_1724.method_24828()) {
                    return;
                }
                int collisions = 0;
                for (class_1297 entity : SpeedGrim.mc.field_1687.method_18112()) {
                    class_1309 living;
                    if (!(entity instanceof class_1309) || (living = (class_1309)entity) == SpeedGrim.mc.field_1724 || living instanceof class_1531 || !PlayerUtil.hasCollisionWith((class_1297)living, newMode ? 0.0f : 1.0f)) continue;
                    ++collisions;
                }
                if (collisions <= 0) break;
                double[] forward = MoveUtil.forward(((Float)this.collideNewSpeed.getValue()).floatValue() * (float)collisions);
                SpeedGrim.mc.field_1724.method_5762(forward[0], 0.0, forward[1]);
                break;
            }
            default: {
                if (this.timerUtil.finished(1100L)) {
                    this.boosting = true;
                }
                if (this.timerUtil.finished(7000L)) {
                    this.boosting = false;
                    this.timerUtil.reset();
                }
                TimerManager.getInstance().addTimer(this.boosting ? (SpeedGrim.mc.field_1724.field_6012 % 2 == 0 ? 1.5f : 1.2f) : 0.05f, TaskPriority.HIGH, SpeedModule.getInstance(), 1);
            }
        }
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

    public static enum BypassType implements ModeSetting.NamedChoice
    {
        COLLIDE("Collide"),
        COLLIDE_NEW("Collide new"),
        TIMER("Timer");

        private final String name;

        private BypassType(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}

