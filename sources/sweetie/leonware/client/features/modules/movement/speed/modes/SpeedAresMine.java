package sweetie.leonware.client.features.modules.movement.speed.modes;

import java.util.ArrayDeque;
import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.class_1309;
import net.minecraft.class_1531;
import net.minecraft.class_243;
import net.minecraft.class_746;
import sweetie.leonware.api.interfaces.IBacktrackable;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.client.features.modules.combat.BacktrackModule;
import sweetie.leonware.client.features.modules.movement.speed.SpeedMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/speed/modes/SpeedAresMine.class */
public class SpeedAresMine extends SpeedMode {
    private final SliderSetting speed = new SliderSetting("Скорость").value(Float.valueOf(7.0f)).range(2.0f, 9.0f).step(0.1f);
    private final SliderSetting boxExpand = new SliderSetting("Расширение бокса").value(Float.valueOf(0.5f)).range(0.3f, 0.6f).step(0.05f);
    private final BooleanSetting onlyPlayers = new BooleanSetting("Только игроки").value((Boolean) false);
    private final BooleanSetting armorStands = new BooleanSetting("Армор стенды").value((Boolean) false);
    private final BooleanSetting predict = new BooleanSetting("Предсказание движения").value((Boolean) true);
    private final BooleanSetting smooth = new BooleanSetting("Сглаживание").value((Boolean) true);
    private int speedBoostCooldown = 0;
    private final SliderSetting backtrackBoostTicks = new SliderSetting("BT Boost cooldown").value(Float.valueOf(3.0f)).range(1.0f, 20.0f).step(1.0f);

    @Generated
    public SliderSetting getSpeed() {
        return this.speed;
    }

    public SpeedAresMine(Supplier<Boolean> condition) {
        this.speed.setVisible(condition);
        this.boxExpand.setVisible(condition);
        this.onlyPlayers.setVisible(condition);
        this.armorStands.setVisible(condition);
        this.predict.setVisible(condition);
        this.smooth.setVisible(condition);
        this.backtrackBoostTicks.setVisible(() -> {
            return Boolean.valueOf(((Boolean) condition.get()).booleanValue() && BacktrackModule.getInstance().isApplyToSpeed());
        });
        addSettings(this.speed, this.boxExpand, this.onlyPlayers, this.armorStands, this.predict, this.smooth, this.backtrackBoostTicks);
    }

    @Override // sweetie.leonware.api.system.backend.Choice, sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
    public String getName() {
        return "AresMine";
    }

    @Override // sweetie.leonware.client.features.modules.movement.speed.SpeedMode
    public void onTravel() {
        if (MoveUtil.isMoving()) {
            double slowMul = mc.field_1724.method_6115() ? 0.85d : 1.0d;
            int collisions = 0;
            for (class_746 class_746Var : mc.field_1687.method_18112()) {
                if (class_746Var != mc.field_1724 && (!this.onlyPlayers.getValue().booleanValue() || (class_746Var instanceof class_1309))) {
                    if (!(class_746Var instanceof class_1531) || this.armorStands.getValue().booleanValue()) {
                        if (mc.field_1724.method_5829().method_1014(this.boxExpand.getValue().floatValue()).method_994(class_746Var.method_5829())) {
                            collisions++;
                        }
                    }
                }
            }
            if (collisions > 0) {
                double[] motion = MoveUtil.forward(((double) this.speed.getValue().floatValue()) * 0.01d * ((double) collisions) * slowMul);
                mc.field_1724.method_5762(motion[0], 0.0d, motion[1]);
            }
            applyBacktrackBoost();
        }
    }

    private void applyBacktrackBoost() {
        BacktrackModule.Position backPos;
        if (BacktrackModule.getInstance().isApplyToSpeed()) {
            if (this.speedBoostCooldown > 0) {
                this.speedBoostCooldown--;
                return;
            }
            for (IBacktrackable iBacktrackable : mc.field_1687.method_18456()) {
                if (iBacktrackable != mc.field_1724 && (iBacktrackable instanceof IBacktrackable)) {
                    IBacktrackable bt = iBacktrackable;
                    ArrayDeque<BacktrackModule.Position> tracks = bt.leonware$getBackTracks();
                    if (!tracks.isEmpty() && (backPos = tracks.peekFirst()) != null) {
                        class_243 btPos = backPos.pos();
                        if (mc.field_1724.method_5829().method_1014(this.boxExpand.getValue().floatValue()).method_994(iBacktrackable.method_5829().method_989(btPos.field_1352 - iBacktrackable.method_23317(), btPos.field_1351 - iBacktrackable.method_23318(), btPos.field_1350 - iBacktrackable.method_23321()))) {
                            double[] motion = MoveUtil.forward(((double) this.speed.getValue().floatValue()) * 0.01d * slowMul());
                            mc.field_1724.method_5762(motion[0], 0.0d, motion[1]);
                            this.speedBoostCooldown = this.backtrackBoostTicks.getValue().intValue();
                            return;
                        }
                    }
                }
            }
        }
    }

    private double slowMul() {
        return mc.field_1724.method_6115() ? 0.85d : 1.0d;
    }
}
