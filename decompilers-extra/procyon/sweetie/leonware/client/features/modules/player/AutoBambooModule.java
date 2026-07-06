// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_2350;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.client.features.modules.movement.MoveFixModule;
import sweetie.leonware.api.utils.rotation.rotations.InstantRotation;
import net.minecraft.class_1799;
import net.minecraft.class_2596;
import sweetie.leonware.api.utils.other.NetworkUtil;
import net.minecraft.class_2868;
import net.minecraft.class_1743;
import net.minecraft.class_1829;
import net.minecraft.class_3532;
import net.minecraft.class_1922;
import net.minecraft.class_2211;
import java.util.ArrayList;
import java.util.List;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_2338;
import java.util.Comparator;
import net.minecraft.class_2382;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Auto Bamboo", category = Category.PLAYER)
public class AutoBambooModule extends Module
{
    private static final AutoBambooModule instance;
    private final SliderSetting searchRange;
    private final SliderSetting attackRange;
    private final SliderSetting breakDelay;
    private final TimerUtil breakTimer;
    private final TimerUtil weaponTimer;
    private final TimerUtil resetTimer;
    private final TimerUtil stuckTimer;
    private class_243 lastPos;
    private int stuckTicks;
    private int skipIndex;
    
    public AutoBambooModule() {
        this.searchRange = new SliderSetting("Search Range").value(6.0f).range(1.0f, 10.0f).step(0.5f);
        this.attackRange = new SliderSetting("Attack Range").value(2.0f).range(1.0f, 5.0f).step(0.1f);
        this.breakDelay = new SliderSetting("Break Delay").value(300.0f).range(100.0f, 1000.0f).step(50.0f);
        this.breakTimer = new TimerUtil();
        this.weaponTimer = new TimerUtil();
        this.resetTimer = new TimerUtil();
        this.stuckTimer = new TimerUtil();
        this.lastPos = null;
        this.stuckTicks = 0;
        this.skipIndex = 0;
        this.addSettings(this.searchRange, this.attackRange, this.breakDelay);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.reset();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.stopMoving();
        this.reset();
    }
    
    private void reset() {
        this.stuckTicks = 0;
        this.skipIndex = 0;
        this.lastPos = null;
        this.stuckTimer.reset();
        this.breakTimer.reset();
        this.weaponTimer.reset();
        this.resetTimer.reset();
    }
    
    @Override
    public void onEvent() {
        final EventListener tickEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (AutoBambooModule.mc.field_1724 == null || AutoBambooModule.mc.field_1687 == null) {
                return;
            }
            else {
                if (this.resetTimer.finished(10000L)) {
                    this.stuckTicks = 0;
                    this.skipIndex = 0;
                    this.lastPos = null;
                    this.resetTimer.reset();
                }
                if (this.weaponTimer.finished(1000L)) {
                    this.equipWeapon();
                    this.weaponTimer.reset();
                }
                final float r = this.searchRange.getValue();
                final float hitRange = this.attackRange.getValue();
                final class_2338 playerPos = AutoBambooModule.mc.field_1724.method_24515();
                final List<class_2338> targets = this.findBambooTargets(playerPos, r);
                if (targets.isEmpty()) {
                    this.stopMoving();
                    return;
                }
                else {
                    final class_243 eye = AutoBambooModule.mc.field_1724.method_33571();
                    targets.sort(Comparator.comparingDouble(pos -> AutoBambooModule.mc.field_1724.method_19538().method_1022(class_243.method_24953((class_2382)pos))));
                    final class_243 curPos = AutoBambooModule.mc.field_1724.method_19538();
                    if (this.lastPos != null && curPos.method_1022(this.lastPos) > 0.05) {
                        this.stuckTimer.reset();
                        this.stuckTicks = 0;
                    }
                    this.lastPos = curPos;
                    if (AutoBambooModule.mc.field_1724.field_5976) {
                        this.stuckTicks += 3;
                    }
                    if (this.stuckTimer.finished(1500L) || this.stuckTicks > 8) {
                        this.stuckTicks = 0;
                        this.stuckTimer.reset();
                        this.skipIndex = (this.skipIndex + 1) % Math.max(1, targets.size());
                    }
                    final int idx = Math.min(this.skipIndex, targets.size() - 1);
                    final class_2338 nearest = (class_2338)targets.get(idx);
                    final class_243 approachPos = this.findApproachPos(nearest, curPos);
                    final double distToBlock = eye.method_1022(class_243.method_24953((class_2382)nearest));
                    if (distToBlock > hitRange) {
                        this.walkTowards(approachPos);
                    }
                    else {
                        this.stopMoving();
                        if (this.breakTimer.finished((long)(float)this.breakDelay.getValue())) {
                            this.breakBamboo(nearest);
                            this.breakTimer.reset();
                            this.skipIndex = 0;
                            this.stuckTicks = 0;
                        }
                    }
                    return;
                }
            }
        }));
        this.addEvents(tickEvent);
    }
    
    private List<class_2338> findBambooTargets(final class_2338 playerPos, final float r) {
        final List<class_2338> targets = new ArrayList<class_2338>();
        for (int x = (int)(-r); x <= r; ++x) {
            for (int z = (int)(-r); z <= r; ++z) {
                for (int y = -3; y <= 5; ++y) {
                    final class_2338 pos = playerPos.method_10069(x, y, z);
                    if (AutoBambooModule.mc.field_1687.method_8320(pos).method_26204() instanceof class_2211) {
                        final class_2338 below = pos.method_10074();
                        if (AutoBambooModule.mc.field_1687.method_8320(below).method_26204() instanceof class_2211) {
                            final class_2338 twoBelow = below.method_10074();
                            if (!(AutoBambooModule.mc.field_1687.method_8320(twoBelow).method_26204() instanceof class_2211)) {
                                final double dist = AutoBambooModule.mc.field_1724.method_19538().method_1022(class_243.method_24954((class_2382)pos));
                                if (dist <= r + 1.5) {
                                    targets.add(pos);
                                }
                            }
                        }
                    }
                }
            }
        }
        return targets;
    }
    
    private class_243 findApproachPos(final class_2338 target, final class_243 playerPos) {
        final int[][] offsets = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 }, { 2, 0 }, { -2, 0 }, { 0, 2 }, { 0, -2 } };
        class_243 best = null;
        double bestDist = Double.MAX_VALUE;
        for (final int[] off : offsets) {
            final class_2338 candidate = target.method_10069(off[0], 0, off[1]);
            if (this.isFreeCell(candidate)) {
                final class_243 pos = new class_243(candidate.method_10263() + 0.5, playerPos.field_1351, candidate.method_10260() + 0.5);
                final double dist = playerPos.method_1022(pos);
                if (dist < bestDist) {
                    bestDist = dist;
                    best = pos;
                }
            }
        }
        if (best == null) {
            return new class_243(target.method_10263() + 0.5, playerPos.field_1351, target.method_10260() + 0.5);
        }
        return best;
    }
    
    private boolean isFreeCell(final class_2338 pos) {
        final boolean feetFree = !AutoBambooModule.mc.field_1687.method_8320(pos).method_26212((class_1922)AutoBambooModule.mc.field_1687, pos) && !(AutoBambooModule.mc.field_1687.method_8320(pos).method_26204() instanceof class_2211);
        final boolean headFree = !AutoBambooModule.mc.field_1687.method_8320(pos.method_10084()).method_26212((class_1922)AutoBambooModule.mc.field_1687, pos.method_10084()) && !(AutoBambooModule.mc.field_1687.method_8320(pos.method_10084()).method_26204() instanceof class_2211);
        return feetFree && headFree;
    }
    
    private void walkTowards(final class_243 approachPos) {
        final class_243 playerPos = AutoBambooModule.mc.field_1724.method_19538();
        final double dx = approachPos.field_1352 - playerPos.field_1352;
        final double dz = approachPos.field_1350 - playerPos.field_1350;
        float targetYaw = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0f;
        if (AutoBambooModule.mc.field_1724.field_5976) {
            final long t = System.currentTimeMillis() / 500L;
            targetYaw += ((t % 2L == 0L) ? 30.0f : -30.0f);
        }
        final float currentYaw = AutoBambooModule.mc.field_1724.method_36454();
        final float diff = class_3532.method_15393(targetYaw - currentYaw);
        AutoBambooModule.mc.field_1724.method_36456(currentYaw + diff * 0.5f);
        AutoBambooModule.mc.field_1690.field_1894.method_23481(true);
        AutoBambooModule.mc.field_1690.field_1832.method_23481(false);
    }
    
    private void stopMoving() {
        if (AutoBambooModule.mc.field_1724 == null) {
            return;
        }
        AutoBambooModule.mc.field_1690.field_1894.method_23481(false);
    }
    
    private void equipWeapon() {
        for (int i = 0; i <= 8; ++i) {
            final class_1799 stack = AutoBambooModule.mc.field_1724.method_31548().method_5438(i);
            if (!stack.method_7960()) {
                if (stack.method_7909() instanceof class_1829 || stack.method_7909() instanceof class_1743) {
                    if (AutoBambooModule.mc.field_1724.method_31548().field_7545 != i) {
                        AutoBambooModule.mc.field_1724.method_31548().field_7545 = i;
                        NetworkUtil.sendPacket((class_2596<?>)new class_2868(i));
                    }
                    return;
                }
            }
        }
    }
    
    private void breakBamboo(final class_2338 target) {
        final class_243 eyePos = AutoBambooModule.mc.field_1724.method_33571();
        final class_243 blockCenter = class_243.method_24953((class_2382)target);
        final double dx = blockCenter.field_1352 - eyePos.field_1352;
        final double dy = blockCenter.field_1351 - eyePos.field_1351;
        final double dz = blockCenter.field_1350 - eyePos.field_1350;
        final double hDist = Math.sqrt(dx * dx + dz * dz);
        final float yaw = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(dy, hDist)));
        final RotationStrategy strategy = new RotationStrategy(new InstantRotation(), MoveFixModule.enabled(), MoveFixModule.isFree()).ticksUntilReset(2);
        RotationManager.getInstance().addRotation(new Rotation(yaw, pitch), strategy, TaskPriority.NORMAL, this);
        AutoBambooModule.mc.field_1761.method_2910(target, class_2350.field_11033);
        AutoBambooModule.mc.field_1724.method_6104(class_1268.field_5808);
    }
    
    @Generated
    public static AutoBambooModule getInstance() {
        return AutoBambooModule.instance;
    }
    
    static {
        instance = new AutoBambooModule();
    }
}
