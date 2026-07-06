package sweetie.leonware.client.features.modules.player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1743;
import net.minecraft.class_1799;
import net.minecraft.class_1829;
import net.minecraft.class_2211;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2596;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/AutoBambooModule.class */
@ModuleRegister(name = "Auto Bamboo", category = Category.PLAYER)
public class AutoBambooModule extends Module {
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

    @Generated
    public static AutoBambooModule getInstance() {
        return instance;
    }

    public AutoBambooModule() {
        addSettings(this.searchRange, this.attackRange, this.breakDelay);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        super.onEnable();
        reset();
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        super.onDisable();
        stopMoving();
        reset();
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

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener tickEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            if (this.resetTimer.finished(10000L)) {
                this.stuckTicks = 0;
                this.skipIndex = 0;
                this.lastPos = null;
                this.resetTimer.reset();
            }
            if (this.weaponTimer.finished(1000L)) {
                equipWeapon();
                this.weaponTimer.reset();
            }
            float r = this.searchRange.getValue().floatValue();
            float hitRange = this.attackRange.getValue().floatValue();
            class_2338 playerPos = mc.field_1724.method_24515();
            List<class_2338> targets = findBambooTargets(playerPos, r);
            if (targets.isEmpty()) {
                stopMoving();
                return;
            }
            class_243 eye = mc.field_1724.method_33571();
            targets.sort(Comparator.comparingDouble(pos -> {
                return mc.field_1724.method_19538().method_1022(class_243.method_24953(pos));
            }));
            class_243 curPos = mc.field_1724.method_19538();
            if (this.lastPos != null && curPos.method_1022(this.lastPos) > 0.05d) {
                this.stuckTimer.reset();
                this.stuckTicks = 0;
            }
            this.lastPos = curPos;
            if (mc.field_1724.field_5976) {
                this.stuckTicks += 3;
            }
            if (this.stuckTimer.finished(1500L) || this.stuckTicks > 8) {
                this.stuckTicks = 0;
                this.stuckTimer.reset();
                this.skipIndex = (this.skipIndex + 1) % Math.max(1, targets.size());
            }
            int idx = Math.min(this.skipIndex, targets.size() - 1);
            class_2338 nearest = targets.get(idx);
            class_243 approachPos = findApproachPos(nearest, curPos);
            double distToBlock = eye.method_1022(class_243.method_24953(nearest));
            if (distToBlock > hitRange) {
                walkTowards(approachPos);
                return;
            }
            stopMoving();
            if (this.breakTimer.finished((long) this.breakDelay.getValue().floatValue())) {
                breakBamboo(nearest);
                this.breakTimer.reset();
                this.skipIndex = 0;
                this.stuckTicks = 0;
            }
        }));
        addEvents(tickEvent);
    }

    private List<class_2338> findBambooTargets(class_2338 playerPos, float r) {
        List<class_2338> targets = new ArrayList<>();
        for (int x = (int) (-r); x <= r; x++) {
            for (int z = (int) (-r); z <= r; z++) {
                for (int y = -3; y <= 5; y++) {
                    class_2338 pos = playerPos.method_10069(x, y, z);
                    if (mc.field_1687.method_8320(pos).method_26204() instanceof class_2211) {
                        class_2338 below = pos.method_10074();
                        if (mc.field_1687.method_8320(below).method_26204() instanceof class_2211) {
                            class_2338 twoBelow = below.method_10074();
                            if (!(mc.field_1687.method_8320(twoBelow).method_26204() instanceof class_2211)) {
                                double dist = mc.field_1724.method_19538().method_1022(class_243.method_24954(pos));
                                if (dist <= ((double) r) + 1.5d) {
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

    /* JADX WARN: Multi-variable type inference failed */
    private class_243 findApproachPos(class_2338 target, class_243 playerPos) {
        class_243 best = null;
        double bestDist = Double.MAX_VALUE;
        for (Object[] objArr : new int[]{new int[]{1, 0}, new int[]{-1, 0}, new int[]{0, 1}, new int[]{0, -1}, new int[]{1, 1}, new int[]{1, -1}, new int[]{-1, 1}, new int[]{-1, -1}, new int[]{2, 0}, new int[]{-2, 0}, new int[]{0, 2}, new int[]{0, -2}}) {
            class_2338 candidate = target.method_10069(objArr[0], 0, objArr[1]);
            if (isFreeCell(candidate)) {
                class_243 pos = new class_243(((double) candidate.method_10263()) + 0.5d, playerPos.field_1351, ((double) candidate.method_10260()) + 0.5d);
                double dist = playerPos.method_1022(pos);
                if (dist < bestDist) {
                    bestDist = dist;
                    best = pos;
                }
            }
        }
        return best == null ? new class_243(((double) target.method_10263()) + 0.5d, playerPos.field_1351, ((double) target.method_10260()) + 0.5d) : best;
    }

    private boolean isFreeCell(class_2338 pos) {
        boolean feetFree = (mc.field_1687.method_8320(pos).method_26212(mc.field_1687, pos) || (mc.field_1687.method_8320(pos).method_26204() instanceof class_2211)) ? false : true;
        boolean headFree = (mc.field_1687.method_8320(pos.method_10084()).method_26212(mc.field_1687, pos.method_10084()) || (mc.field_1687.method_8320(pos.method_10084()).method_26204() instanceof class_2211)) ? false : true;
        return feetFree && headFree;
    }

    private void walkTowards(class_243 approachPos) {
        class_243 playerPos = mc.field_1724.method_19538();
        double dx = approachPos.field_1352 - playerPos.field_1352;
        double dz = approachPos.field_1350 - playerPos.field_1350;
        float targetYaw = ((float) Math.toDegrees(Math.atan2(dz, dx))) - 90.0f;
        if (mc.field_1724.field_5976) {
            long t = System.currentTimeMillis() / 500;
            targetYaw += t % 2 == 0 ? 30.0f : -30.0f;
        }
        float currentYaw = mc.field_1724.method_36454();
        float diff = class_3532.method_15393(targetYaw - currentYaw);
        mc.field_1724.method_36456(currentYaw + (diff * 0.5f));
        mc.field_1690.field_1894.method_23481(true);
        mc.field_1690.field_1832.method_23481(false);
    }

    private void stopMoving() {
        if (mc.field_1724 == null) {
            return;
        }
        mc.field_1690.field_1894.method_23481(false);
    }

    private void equipWeapon() {
        for (int i = 0; i <= 8; i++) {
            class_1799 stack = mc.field_1724.method_31548().method_5438(i);
            if (!stack.method_7960() && ((stack.method_7909() instanceof class_1829) || (stack.method_7909() instanceof class_1743))) {
                if (mc.field_1724.method_31548().field_7545 != i) {
                    mc.field_1724.method_31548().field_7545 = i;
                    NetworkUtil.sendPacket((class_2596<?>) new class_2868(i));
                    return;
                }
                return;
            }
        }
    }

    private void breakBamboo(class_2338 target) {
        class_243 eyePos = mc.field_1724.method_33571();
        class_243 blockCenter = class_243.method_24953(target);
        double dx = blockCenter.field_1352 - eyePos.field_1352;
        double dy = blockCenter.field_1351 - eyePos.field_1351;
        double dz = blockCenter.field_1350 - eyePos.field_1350;
        double hDist = Math.sqrt((dx * dx) + (dz * dz));
        float yaw = ((float) Math.toDegrees(Math.atan2(dz, dx))) - 90.0f;
        float pitch = (float) (-Math.toDegrees(Math.atan2(dy, hDist)));
        RotationStrategy strategy = new RotationStrategy(new InstantRotation(), MoveFixModule.enabled(), MoveFixModule.isFree()).ticksUntilReset(2);
        RotationManager.getInstance().addRotation(new Rotation(yaw, pitch), strategy, TaskPriority.NORMAL, this);
        mc.field_1761.method_2910(target, class_2350.field_11033);
        mc.field_1724.method_6104(class_1268.field_5808);
    }
}
