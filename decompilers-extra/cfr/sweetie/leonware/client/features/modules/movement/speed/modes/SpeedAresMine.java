/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1531
 *  net.minecraft.class_1657
 *  net.minecraft.class_243
 */
package sweetie.leonware.client.features.modules.movement.speed.modes;

import java.util.ArrayDeque;
import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1531;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import sweetie.leonware.api.interfaces.IBacktrackable;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.client.features.modules.combat.BacktrackModule;
import sweetie.leonware.client.features.modules.movement.speed.SpeedMode;

public class SpeedAresMine
extends SpeedMode {
    private final SliderSetting speed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c").value(Float.valueOf(7.0f)).range(2.0f, 9.0f).step(0.1f);
    private final SliderSetting boxExpand = new SliderSetting("\u0420\u0430\u0441\u0448\u0438\u0440\u0435\u043d\u0438\u0435 \u0431\u043e\u043a\u0441\u0430").value(Float.valueOf(0.5f)).range(0.3f, 0.6f).step(0.05f);
    private final BooleanSetting onlyPlayers = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u0438\u0433\u0440\u043e\u043a\u0438").value(false);
    private final BooleanSetting armorStands = new BooleanSetting("\u0410\u0440\u043c\u043e\u0440 \u0441\u0442\u0435\u043d\u0434\u044b").value(false);
    private final BooleanSetting predict = new BooleanSetting("\u041f\u0440\u0435\u0434\u0441\u043a\u0430\u0437\u0430\u043d\u0438\u0435 \u0434\u0432\u0438\u0436\u0435\u043d\u0438\u044f").value(true);
    private final BooleanSetting smooth = new BooleanSetting("\u0421\u0433\u043b\u0430\u0436\u0438\u0432\u0430\u043d\u0438\u0435").value(true);
    private int speedBoostCooldown = 0;
    private final SliderSetting backtrackBoostTicks = new SliderSetting("BT Boost cooldown").value(Float.valueOf(3.0f)).range(1.0f, 20.0f).step(1.0f);

    public SpeedAresMine(Supplier<Boolean> condition) {
        this.speed.setVisible((Supplier)condition);
        this.boxExpand.setVisible((Supplier)condition);
        this.onlyPlayers.setVisible((Supplier)condition);
        this.armorStands.setVisible((Supplier)condition);
        this.predict.setVisible((Supplier)condition);
        this.smooth.setVisible((Supplier)condition);
        this.backtrackBoostTicks.setVisible(() -> (Boolean)condition.get() != false && BacktrackModule.getInstance().isApplyToSpeed());
        this.addSettings(this.speed, this.boxExpand, this.onlyPlayers, this.armorStands, this.predict, this.smooth, this.backtrackBoostTicks);
    }

    @Override
    public String getName() {
        return "AresMine";
    }

    @Override
    public void onTravel() {
        if (!MoveUtil.isMoving()) {
            return;
        }
        double slowMul = SpeedAresMine.mc.field_1724.method_6115() ? 0.85 : 1.0;
        int collisions = 0;
        for (class_1297 ent : SpeedAresMine.mc.field_1687.method_18112()) {
            if (ent == SpeedAresMine.mc.field_1724 || ((Boolean)this.onlyPlayers.getValue()).booleanValue() && !(ent instanceof class_1309) || ent instanceof class_1531 && !((Boolean)this.armorStands.getValue()).booleanValue() || !SpeedAresMine.mc.field_1724.method_5829().method_1014((double)((Float)this.boxExpand.getValue()).floatValue()).method_994(ent.method_5829())) continue;
            ++collisions;
        }
        if (collisions > 0) {
            double[] motion = MoveUtil.forward((double)((Float)this.speed.getValue()).floatValue() * 0.01 * (double)collisions * slowMul);
            SpeedAresMine.mc.field_1724.method_5762(motion[0], 0.0, motion[1]);
        }
        this.applyBacktrackBoost();
    }

    private void applyBacktrackBoost() {
        if (!BacktrackModule.getInstance().isApplyToSpeed()) {
            return;
        }
        if (this.speedBoostCooldown > 0) {
            --this.speedBoostCooldown;
            return;
        }
        for (class_1657 player : SpeedAresMine.mc.field_1687.method_18456()) {
            BacktrackModule.Position backPos;
            IBacktrackable bt;
            ArrayDeque<BacktrackModule.Position> tracks;
            if (player == SpeedAresMine.mc.field_1724 || !(player instanceof IBacktrackable) || (tracks = (bt = (IBacktrackable)player).leonware$getBackTracks()).isEmpty() || (backPos = tracks.peekFirst()) == null) continue;
            class_243 btPos = backPos.pos();
            if (!SpeedAresMine.mc.field_1724.method_5829().method_1014((double)((Float)this.boxExpand.getValue()).floatValue()).method_994(player.method_5829().method_989(btPos.field_1352 - player.method_23317(), btPos.field_1351 - player.method_23318(), btPos.field_1350 - player.method_23321()))) continue;
            double[] motion = MoveUtil.forward((double)((Float)this.speed.getValue()).floatValue() * 0.01 * this.slowMul());
            SpeedAresMine.mc.field_1724.method_5762(motion[0], 0.0, motion[1]);
            this.speedBoostCooldown = ((Float)this.backtrackBoostTicks.getValue()).intValue();
            break;
        }
    }

    private double slowMul() {
        return SpeedAresMine.mc.field_1724.method_6115() ? 0.85 : 1.0;
    }

    @Generated
    public SliderSetting getSpeed() {
        return this.speed;
    }
}

