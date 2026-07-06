/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1268
 *  net.minecraft.class_1743
 *  net.minecraft.class_1799
 *  net.minecraft.class_1829
 *  net.minecraft.class_1922
 *  net.minecraft.class_2211
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_2382
 *  net.minecraft.class_243
 *  net.minecraft.class_2868
 *  net.minecraft.class_3532
 */
package sweetie.leonware.client.features.modules.player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1743;
import net.minecraft.class_1799;
import net.minecraft.class_1829;
import net.minecraft.class_1922;
import net.minecraft.class_2211;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_2868;
import net.minecraft.class_3532;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.other.NetworkUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.rotation.rotations.InstantRotation;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.client.features.modules.movement.MoveFixModule;

@ModuleRegister(name="Auto Bamboo", category=Category.PLAYER)
public class AutoBambooModule
extends Module {
    private static final AutoBambooModule instance = new AutoBambooModule();
    private final SliderSetting searchRange = new SliderSetting("Search Range").value(Float.valueOf(6.0f)).range(1.0f, 10.0f).step(0.5f);
    private final SliderSetting attackRange = new SliderSetting("Attack Range").value(Float.valueOf(2.0f)).range(1.0f, 5.0f).step(0.1f);
    private final SliderSetting breakDelay = new SliderSetting("Break Delay").value(Float.valueOf(300.0f)).range(100.0f, 1000.0f).step(50.0f);
    private final TimerUtil breakTimer = new TimerUtil();
    private final TimerUtil weaponTimer = new TimerUtil();
    private final TimerUtil resetTimer = new TimerUtil();
    private final TimerUtil stuckTimer = new TimerUtil();
    private class_243 lastPos = null;
    private int stuckTicks = 0;
    private int skipIndex = 0;

    public AutoBambooModule() {
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
        EventListener tickEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (AutoBambooModule.mc.field_1724 == null || AutoBambooModule.mc.field_1687 == null) {
                return;
            }
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
            float r = ((Float)this.searchRange.getValue()).floatValue();
            float hitRange = ((Float)this.attackRange.getValue()).floatValue();
            class_2338 playerPos = AutoBambooModule.mc.field_1724.method_24515();
            List<class_2338> targets = this.findBambooTargets(playerPos, r);
            if (targets.isEmpty()) {
                this.stopMoving();
                return;
            }
            class_243 eye = AutoBambooModule.mc.field_1724.method_33571();
            targets.sort(Comparator.comparingDouble(pos -> AutoBambooModule.mc.field_1724.method_19538().method_1022(class_243.method_24953((class_2382)pos))));
            class_243 curPos = AutoBambooModule.mc.field_1724.method_19538();
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
            int idx = Math.min(this.skipIndex, targets.size() - 1);
            class_2338 nearest = targets.get(idx);
            class_243 approachPos = this.findApproachPos(nearest, curPos);
            double distToBlock = eye.method_1022(class_243.method_24953((class_2382)nearest));
            if (distToBlock > (double)hitRange) {
                this.walkTowards(approachPos);
            } else {
                this.stopMoving();
                if (this.breakTimer.finished((long)((Float)this.breakDelay.getValue()).floatValue())) {
                    this.breakBamboo(nearest);
                    this.breakTimer.reset();
                    this.skipIndex = 0;
                    this.stuckTicks = 0;
                }
            }
        }));
        this.addEvents(tickEvent);
    }

    private List<class_2338> findBambooTargets(class_2338 playerPos, float r) {
        ArrayList<class_2338> targets = new ArrayList<class_2338>();
        int x = (int)(-r);
        while ((float)x <= r) {
            int z = (int)(-r);
            while ((float)z <= r) {
                for (int y = -3; y <= 5; ++y) {
                    double dist;
                    class_2338 twoBelow;
                    class_2338 below;
                    class_2338 pos = playerPos.method_10069(x, y, z);
                    if (!(AutoBambooModule.mc.field_1687.method_8320(pos).method_26204() instanceof class_2211) || !(AutoBambooModule.mc.field_1687.method_8320(below = pos.method_10074()).method_26204() instanceof class_2211) || AutoBambooModule.mc.field_1687.method_8320(twoBelow = below.method_10074()).method_26204() instanceof class_2211 || !((dist = AutoBambooModule.mc.field_1724.method_19538().method_1022(class_243.method_24954((class_2382)pos))) <= (double)r + 1.5)) continue;
                    targets.add(pos);
                }
                ++z;
            }
            ++x;
        }
        return targets;
    }

    private class_243 findApproachPos(class_2338 target, class_243 playerPos) {
        int[][] offsets = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {2, 0}, {-2, 0}, {0, 2}, {0, -2}};
        class_243 best = null;
        double bestDist = Double.MAX_VALUE;
        for (int[] off : offsets) {
            class_243 pos;
            double dist;
            class_2338 candidate = target.method_10069(off[0], 0, off[1]);
            if (!this.isFreeCell(candidate) || !((dist = playerPos.method_1022(pos = new class_243((double)candidate.method_10263() + 0.5, playerPos.field_1351, (double)candidate.method_10260() + 0.5))) < bestDist)) continue;
            bestDist = dist;
            best = pos;
        }
        if (best == null) {
            return new class_243((double)target.method_10263() + 0.5, playerPos.field_1351, (double)target.method_10260() + 0.5);
        }
        return best;
    }

    private boolean isFreeCell(class_2338 pos) {
        boolean feetFree = !AutoBambooModule.mc.field_1687.method_8320(pos).method_26212((class_1922)AutoBambooModule.mc.field_1687, pos) && !(AutoBambooModule.mc.field_1687.method_8320(pos).method_26204() instanceof class_2211);
        boolean headFree = !AutoBambooModule.mc.field_1687.method_8320(pos.method_10084()).method_26212((class_1922)AutoBambooModule.mc.field_1687, pos.method_10084()) && !(AutoBambooModule.mc.field_1687.method_8320(pos.method_10084()).method_26204() instanceof class_2211);
        return feetFree && headFree;
    }

    private void walkTowards(class_243 approachPos) {
        class_243 playerPos = AutoBambooModule.mc.field_1724.method_19538();
        double dx = approachPos.field_1352 - playerPos.field_1352;
        double dz = approachPos.field_1350 - playerPos.field_1350;
        float targetYaw = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0f;
        if (AutoBambooModule.mc.field_1724.field_5976) {
            long t = System.currentTimeMillis() / 500L;
            targetYaw += t % 2L == 0L ? 30.0f : -30.0f;
        }
        float currentYaw = AutoBambooModule.mc.field_1724.method_36454();
        float diff = class_3532.method_15393((float)(targetYaw - currentYaw));
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
            class_1799 stack = AutoBambooModule.mc.field_1724.method_31548().method_5438(i);
            if (stack.method_7960() || !(stack.method_7909() instanceof class_1829) && !(stack.method_7909() instanceof class_1743)) continue;
            if (AutoBambooModule.mc.field_1724.method_31548().field_7545 != i) {
                AutoBambooModule.mc.field_1724.method_31548().field_7545 = i;
                NetworkUtil.sendPacket(new class_2868(i));
            }
            return;
        }
    }

    private void breakBamboo(class_2338 target) {
        class_243 eyePos = AutoBambooModule.mc.field_1724.method_33571();
        class_243 blockCenter = class_243.method_24953((class_2382)target);
        double dx = blockCenter.field_1352 - eyePos.field_1352;
        double dy = blockCenter.field_1351 - eyePos.field_1351;
        double dz = blockCenter.field_1350 - eyePos.field_1350;
        double hDist = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(dy, hDist)));
        RotationStrategy strategy = new RotationStrategy(new InstantRotation(), MoveFixModule.enabled(), MoveFixModule.isFree()).ticksUntilReset(2);
        RotationManager.getInstance().addRotation(new Rotation(yaw, pitch), strategy, TaskPriority.NORMAL, this);
        AutoBambooModule.mc.field_1761.method_2910(target, class_2350.field_11033);
        AutoBambooModule.mc.field_1724.method_6104(class_1268.field_5808);
    }

    @Generated
    public static AutoBambooModule getInstance() {
        return instance;
    }
}

