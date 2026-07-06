// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement.speed.modes;

import lombok.Generated;
import net.minecraft.class_243;
import java.util.ArrayDeque;
import sweetie.leonware.api.interfaces.IBacktrackable;
import net.minecraft.class_1657;
import java.util.Iterator;
import net.minecraft.class_1531;
import net.minecraft.class_1309;
import net.minecraft.class_1297;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.client.features.modules.combat.BacktrackModule;
import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.client.features.modules.movement.speed.SpeedMode;

public class SpeedAresMine extends SpeedMode
{
    private final SliderSetting speed;
    private final SliderSetting boxExpand;
    private final BooleanSetting onlyPlayers;
    private final BooleanSetting armorStands;
    private final BooleanSetting predict;
    private final BooleanSetting smooth;
    private int speedBoostCooldown;
    private final SliderSetting backtrackBoostTicks;
    
    public SpeedAresMine(final Supplier<Boolean> condition) {
        this.speed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c").value(7.0f).range(2.0f, 9.0f).step(0.1f);
        this.boxExpand = new SliderSetting("\u0420\u0430\u0441\u0448\u0438\u0440\u0435\u043d\u0438\u0435 \u0431\u043e\u043a\u0441\u0430").value(0.5f).range(0.3f, 0.6f).step(0.05f);
        this.onlyPlayers = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u0438\u0433\u0440\u043e\u043a\u0438").value(false);
        this.armorStands = new BooleanSetting("\u0410\u0440\u043c\u043e\u0440 \u0441\u0442\u0435\u043d\u0434\u044b").value(false);
        this.predict = new BooleanSetting("\u041f\u0440\u0435\u0434\u0441\u043a\u0430\u0437\u0430\u043d\u0438\u0435 \u0434\u0432\u0438\u0436\u0435\u043d\u0438\u044f").value(true);
        this.smooth = new BooleanSetting("\u0421\u0433\u043b\u0430\u0436\u0438\u0432\u0430\u043d\u0438\u0435").value(true);
        this.speedBoostCooldown = 0;
        this.backtrackBoostTicks = new SliderSetting("BT Boost cooldown").value(3.0f).range(1.0f, 20.0f).step(1.0f);
        this.speed.setVisible(condition);
        this.boxExpand.setVisible(condition);
        this.onlyPlayers.setVisible(condition);
        this.armorStands.setVisible(condition);
        this.predict.setVisible(condition);
        this.smooth.setVisible(condition);
        this.backtrackBoostTicks.setVisible(() -> condition.get() && BacktrackModule.getInstance().isApplyToSpeed());
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
        final double slowMul = SpeedAresMine.mc.field_1724.method_6115() ? 0.85 : 1.0;
        int collisions = 0;
        for (final class_1297 ent : SpeedAresMine.mc.field_1687.method_18112()) {
            if (ent == SpeedAresMine.mc.field_1724) {
                continue;
            }
            if (this.onlyPlayers.getValue() && !(ent instanceof class_1309)) {
                continue;
            }
            if (ent instanceof class_1531 && !this.armorStands.getValue()) {
                continue;
            }
            if (!SpeedAresMine.mc.field_1724.method_5829().method_1014((double)this.boxExpand.getValue()).method_994(ent.method_5829())) {
                continue;
            }
            ++collisions;
        }
        if (collisions > 0) {
            final double[] motion = MoveUtil.forward(this.speed.getValue() * 0.01 * collisions * slowMul);
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
        for (final class_1657 player : SpeedAresMine.mc.field_1687.method_18456()) {
            if (player == SpeedAresMine.mc.field_1724) {
                continue;
            }
            if (!(player instanceof IBacktrackable)) {
                continue;
            }
            final IBacktrackable bt = (IBacktrackable)player;
            final ArrayDeque<BacktrackModule.Position> tracks = bt.leonware$getBackTracks();
            if (tracks.isEmpty()) {
                continue;
            }
            final BacktrackModule.Position backPos = tracks.peekFirst();
            if (backPos == null) {
                continue;
            }
            final class_243 btPos = backPos.pos();
            if (SpeedAresMine.mc.field_1724.method_5829().method_1014((double)this.boxExpand.getValue()).method_994(player.method_5829().method_989(btPos.field_1352 - player.method_23317(), btPos.field_1351 - player.method_23318(), btPos.field_1350 - player.method_23321()))) {
                final double[] motion = MoveUtil.forward(this.speed.getValue() * 0.01 * this.slowMul());
                SpeedAresMine.mc.field_1724.method_5762(motion[0], 0.0, motion[1]);
                this.speedBoostCooldown = this.backtrackBoostTicks.getValue().intValue();
                break;
            }
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
