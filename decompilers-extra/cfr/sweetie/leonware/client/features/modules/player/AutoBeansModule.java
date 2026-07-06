/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  baritone.api.BaritoneAPI
 *  baritone.api.pathing.goals.Goal
 *  baritone.api.pathing.goals.GoalNear
 *  lombok.Generated
 *  net.minecraft.class_1268
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_1713
 *  net.minecraft.class_1743
 *  net.minecraft.class_1792
 *  net.minecraft.class_1802
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2282
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_2382
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_243
 *  net.minecraft.class_2680
 *  net.minecraft.class_2769
 *  net.minecraft.class_3532
 *  net.minecraft.class_3959
 *  net.minecraft.class_3959$class_242
 *  net.minecraft.class_3959$class_3960
 *  net.minecraft.class_3965
 */
package sweetie.leonware.client.features.modules.player;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.goals.GoalNear;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1743;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2282;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2382;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_2769;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.utils.other.TextUtil;

@ModuleRegister(name="Auto Beans", category=Category.PLAYER)
public class AutoBeansModule
extends Module {
    private static final AutoBeansModule instance = new AutoBeansModule();
    private final BooleanSetting useAxe = new BooleanSetting("\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u044c \u0442\u043e\u043f\u043e\u0440").value(true);
    public class_2338 pos1 = null;
    public class_2338 pos2 = null;
    private State state = State.SCAN;
    private final Set<class_2338> skipSet = new HashSet<class_2338>();
    private class_2338 targetBean = null;
    private class_2338 targetLog = null;
    private class_2350 targetFacing = null;
    private long waitTimer = 0L;
    private long stuckTimer = 0L;
    private class_2338 lastPos = null;
    private int harvestedCount = 0;
    private int replantedCount = 0;
    private int actionDelayTicks = 0;
    private int actionRetries = 0;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private int minZ;
    private int maxZ;

    public AutoBeansModule() {
        this.addSettings(this.useAxe);
    }

    @Override
    public void onEnable() {
        if (this.pos1 == null || this.pos2 == null) {
            TextUtil.sendMessage("\u00a7cAutoBean: \u0421\u043d\u0430\u0447\u0430\u043b\u0430 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u0438\u0442\u0435 \u0442\u043e\u0447\u043a\u0438 \u0447\u0435\u0440\u0435\u0437 $autobean pos1 \u0438 $autobean pos2");
            this.toggle();
            return;
        }
        this.minX = Math.min(this.pos1.method_10263(), this.pos2.method_10263()) - 1;
        this.maxX = Math.max(this.pos1.method_10263(), this.pos2.method_10263()) + 1;
        this.minY = Math.min(this.pos1.method_10264(), this.pos2.method_10264()) - 1;
        this.maxY = Math.max(this.pos1.method_10264(), this.pos2.method_10264()) + 4;
        this.minZ = Math.min(this.pos1.method_10260(), this.pos2.method_10260()) - 1;
        this.maxZ = Math.max(this.pos1.method_10260(), this.pos2.method_10260()) + 1;
        this.state = State.SCAN;
        this.skipSet.clear();
        this.clearTarget();
        this.waitTimer = 0L;
        this.stuckTimer = System.currentTimeMillis();
        this.lastPos = null;
        this.harvestedCount = 0;
        this.replantedCount = 0;
        this.actionDelayTicks = 0;
        this.actionRetries = 0;
        this.configureBaritone();
        TextUtil.sendMessage("\u00a7aAutoBean: \u0412\u043a\u043b\u044e\u0447\u0451\u043d. \u0417\u043e\u043d\u0430 \u043e\u043f\u0440\u0435\u0434\u0435\u043b\u0435\u043d\u0430.");
    }

    @Override
    public void onDisable() {
        this.cancelBaritone();
        this.restoreBaritone();
        TextUtil.sendMessage("\u00a7cAutoBean: \u0412\u044b\u043a\u043b\u044e\u0447\u0435\u043d. \u0421\u043e\u0431\u0440\u0430\u043d\u043e: " + this.harvestedCount + " | \u041f\u043e\u0441\u0430\u0436\u0435\u043d\u043e: " + this.replantedCount);
    }

    @Override
    public void onEvent() {
        this.addEvents(UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> this.tick())));
    }

    private void tick() {
        if (AutoBeansModule.mc.field_1724 == null || AutoBeansModule.mc.field_1687 == null) {
            return;
        }
        if (this.actionDelayTicks > 0) {
            --this.actionDelayTicks;
            return;
        }
        switch (this.state.ordinal()) {
            case 0: {
                this.doScan();
                break;
            }
            case 1: {
                this.doPath();
                break;
            }
            case 2: {
                this.doHarvest();
                break;
            }
            case 3: {
                this.doReplant();
                break;
            }
            case 4: {
                if (System.currentTimeMillis() < this.waitTimer) break;
                this.skipSet.clear();
                this.state = State.SCAN;
            }
        }
    }

    private void doScan() {
        ArrayList<class_2338> visibleBeans = new ArrayList<class_2338>();
        ArrayList<class_2338> hiddenBeans = new ArrayList<class_2338>();
        for (int x = this.minX; x <= this.maxX; ++x) {
            for (int y = this.minY; y <= this.maxY; ++y) {
                for (int z = this.minZ; z <= this.maxZ; ++z) {
                    class_2680 bs;
                    class_2338 pos = new class_2338(x, y, z);
                    if (this.skipSet.contains(pos) || (bs = AutoBeansModule.mc.field_1687.method_8320(pos)).method_26204() != class_2246.field_10302 || (Integer)bs.method_11654((class_2769)class_2282.field_10779) != 2) continue;
                    if (this.isVisible(pos)) {
                        visibleBeans.add(pos);
                        continue;
                    }
                    hiddenBeans.add(pos);
                }
            }
        }
        if (!visibleBeans.isEmpty()) {
            visibleBeans.sort(Comparator.comparingDouble(p -> AutoBeansModule.mc.field_1724.method_33571().method_1022(class_243.method_24953((class_2382)p))));
            this.setTarget((class_2338)visibleBeans.get(0));
            if (AutoBeansModule.mc.field_1724.method_33571().method_1022(class_243.method_24953((class_2382)this.targetBean)) <= 4.0) {
                this.cancelBaritone();
                this.state = State.HARVEST;
                this.actionRetries = 0;
            } else {
                this.state = State.PATH;
                this.startPathing(this.targetBean);
            }
        } else if (!hiddenBeans.isEmpty()) {
            hiddenBeans.sort(Comparator.comparingDouble(p -> AutoBeansModule.mc.field_1724.method_33571().method_1022(class_243.method_24953((class_2382)p))));
            this.setTarget((class_2338)hiddenBeans.get(0));
            this.state = State.PATH;
            this.startPathing(this.targetBean);
        } else {
            this.waitTimer = System.currentTimeMillis() + 1000L;
            this.state = State.WAIT;
        }
    }

    private void doPath() {
        if (this.targetBean == null) {
            this.state = State.SCAN;
            return;
        }
        double dist = AutoBeansModule.mc.field_1724.method_33571().method_1022(class_243.method_24953((class_2382)this.targetBean));
        if (dist <= 4.0 && this.isVisible(this.targetBean)) {
            this.cancelBaritone();
            this.state = State.HARVEST;
            this.actionRetries = 0;
            return;
        }
        if (this.isStuck()) {
            this.skipSet.add(this.targetBean);
            this.clearTarget();
            return;
        }
        if (!this.isBaritonePathing()) {
            this.startPathing(this.targetBean);
        }
    }

    private void doHarvest() {
        if (this.targetBean == null) {
            this.state = State.SCAN;
            return;
        }
        class_2680 bs = AutoBeansModule.mc.field_1687.method_8320(this.targetBean);
        if (bs.method_26204() != class_2246.field_10302 || (Integer)bs.method_11654((class_2769)class_2282.field_10779) != 2) {
            ++this.harvestedCount;
            this.actionRetries = 0;
            this.state = State.REPLANT;
            return;
        }
        if (this.actionRetries > 12) {
            this.skipSet.add(this.targetBean);
            this.clearTarget();
            return;
        }
        this.lookAt(class_243.method_24953((class_2382)this.targetBean));
        if (((Boolean)this.useAxe.getValue()).booleanValue()) {
            this.equipBestAxe();
        }
        AutoBeansModule.mc.field_1761.method_2910(this.targetBean, this.targetFacing.method_10153());
        AutoBeansModule.mc.field_1724.method_6104(class_1268.field_5808);
        ++this.actionRetries;
        this.actionDelayTicks = 6;
    }

    private void doReplant() {
        if (this.targetBean == null || this.targetLog == null) {
            this.clearTarget();
            return;
        }
        class_2680 currentBlock = AutoBeansModule.mc.field_1687.method_8320(this.targetBean);
        if (currentBlock.method_26204() == class_2246.field_10302) {
            ++this.replantedCount;
            this.clearTarget();
            return;
        }
        class_2680 logState = AutoBeansModule.mc.field_1687.method_8320(this.targetLog);
        if (!currentBlock.method_45474() || this.actionRetries > 12 || !this.isJungleLog(logState)) {
            this.skipSet.add(this.targetBean);
            this.clearTarget();
            return;
        }
        int beansSlot = this.findCocoaBeansHotbar();
        if (beansSlot == -1 && (beansSlot = this.moveBeansToHotbar()) == -1) {
            TextUtil.sendMessage("\u00a7eAutoBean: \u041d\u0435\u0442 \u043a\u0430\u043a\u0430\u043e-\u0431\u043e\u0431\u043e\u0432.");
            this.toggle();
            return;
        }
        class_243 hitVec = class_243.method_24953((class_2382)this.targetLog).method_1031((double)this.targetFacing.method_10148() * 0.5, (double)this.targetFacing.method_10164() * 0.5, (double)this.targetFacing.method_10165() * 0.5);
        AutoBeansModule.mc.field_1724.method_31548().field_7545 = beansSlot;
        this.lookAt(hitVec);
        AutoBeansModule.mc.field_1761.method_2896(AutoBeansModule.mc.field_1724, class_1268.field_5808, new class_3965(hitVec, this.targetFacing.method_10153(), this.targetLog, false));
        AutoBeansModule.mc.field_1724.method_6104(class_1268.field_5808);
        ++this.actionRetries;
        this.actionDelayTicks = 8;
    }

    private void setTarget(class_2338 pos) {
        this.targetBean = pos;
        class_2680 bs = AutoBeansModule.mc.field_1687.method_8320(pos);
        if (bs.method_26204() == class_2246.field_10302) {
            this.targetFacing = (class_2350)bs.method_11654((class_2769)class_2282.field_11177);
            this.targetLog = pos.method_10093(this.targetFacing);
        }
    }

    private void clearTarget() {
        this.targetBean = null;
        this.targetLog = null;
        this.targetFacing = null;
        this.state = State.SCAN;
    }

    private boolean isVisible(class_2338 pos) {
        class_243 target;
        if (AutoBeansModule.mc.field_1724 == null || AutoBeansModule.mc.field_1687 == null) {
            return false;
        }
        class_243 eye = AutoBeansModule.mc.field_1724.method_33571();
        class_3959 context = new class_3959(eye, target = class_243.method_24953((class_2382)pos), class_3959.class_3960.field_17559, class_3959.class_242.field_1348, (class_1297)AutoBeansModule.mc.field_1724);
        class_3965 result = AutoBeansModule.mc.field_1687.method_17742(context);
        if (result.method_17783() == class_239.class_240.field_1333) {
            return true;
        }
        class_2338 hit = result.method_17777();
        if (hit.equals((Object)pos)) {
            return true;
        }
        class_2248 block = AutoBeansModule.mc.field_1687.method_8320(hit).method_26204();
        return block == class_2246.field_10302;
    }

    private int findCocoaBeansHotbar() {
        for (int i = 0; i < 9; ++i) {
            if (!AutoBeansModule.mc.field_1724.method_31548().method_5438(i).method_31574(class_1802.field_8116)) continue;
            return i;
        }
        return -1;
    }

    private int moveBeansToHotbar() {
        for (int i = 9; i < 36; ++i) {
            if (!AutoBeansModule.mc.field_1724.method_31548().method_5438(i).method_31574(class_1802.field_8116)) continue;
            int slot = this.findSafeHotbarSlot();
            if (slot == -1) {
                return -1;
            }
            AutoBeansModule.mc.field_1761.method_2906(AutoBeansModule.mc.field_1724.field_7498.field_7763, i, slot, class_1713.field_7791, (class_1657)AutoBeansModule.mc.field_1724);
            return slot;
        }
        return -1;
    }

    private int findSafeHotbarSlot() {
        int i;
        for (i = 0; i < 9; ++i) {
            if (!AutoBeansModule.mc.field_1724.method_31548().method_5438(i).method_7960()) continue;
            return i;
        }
        for (i = 0; i < 9; ++i) {
            class_1792 item;
            if (i == AutoBeansModule.mc.field_1724.method_31548().field_7545 || (item = AutoBeansModule.mc.field_1724.method_31548().method_5438(i).method_7909()) instanceof class_1743) continue;
            return i;
        }
        return -1;
    }

    private void equipBestAxe() {
        int best = -1;
        int bestTier = -1;
        for (int i = 0; i < 9; ++i) {
            int tier;
            class_1792 item = AutoBeansModule.mc.field_1724.method_31548().method_5438(i).method_7909();
            if (!(item instanceof class_1743) || (tier = this.getAxeTier(item)) <= bestTier) continue;
            bestTier = tier;
            best = i;
        }
        if (best != -1) {
            AutoBeansModule.mc.field_1724.method_31548().field_7545 = best;
        }
    }

    private int getAxeTier(class_1792 item) {
        if (item == class_1802.field_22025) {
            return 5;
        }
        if (item == class_1802.field_8556) {
            return 4;
        }
        if (item == class_1802.field_8475) {
            return 3;
        }
        if (item == class_1802.field_8062) {
            return 2;
        }
        if (item == class_1802.field_8825) {
            return 1;
        }
        if (item == class_1802.field_8406) {
            return 0;
        }
        return -1;
    }

    private boolean isJungleLog(class_2680 state) {
        class_2248 b = state.method_26204();
        return b == class_2246.field_10306 || b == class_2246.field_10254 || b == class_2246.field_10303 || b == class_2246.field_10084;
    }

    private void lookAt(class_243 target) {
        if (AutoBeansModule.mc.field_1724 == null) {
            return;
        }
        class_243 eye = AutoBeansModule.mc.field_1724.method_33571();
        class_243 dir = target.method_1020(eye);
        double h = Math.sqrt(dir.field_1352 * dir.field_1352 + dir.field_1350 * dir.field_1350);
        float yaw = (float)Math.toDegrees(Math.atan2(dir.field_1350, dir.field_1352)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(dir.field_1351, h)));
        AutoBeansModule.mc.field_1724.method_36456(yaw);
        AutoBeansModule.mc.field_1724.method_36457(class_3532.method_15363((float)pitch, (float)-90.0f, (float)90.0f));
    }

    private void startPathing(class_2338 pos) {
        this.configureBaritone();
        BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath((Goal)new GoalNear(pos, 1));
        this.lastPos = AutoBeansModule.mc.field_1724 != null ? AutoBeansModule.mc.field_1724.method_24515() : pos;
        this.stuckTimer = System.currentTimeMillis();
    }

    private boolean isBaritonePathing() {
        return BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().isActive();
    }

    private void cancelBaritone() {
        BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
    }

    private boolean isStuck() {
        if (AutoBeansModule.mc.field_1724 == null) {
            return false;
        }
        class_2338 cur = AutoBeansModule.mc.field_1724.method_24515();
        if (this.lastPos == null) {
            this.lastPos = cur;
            this.stuckTimer = System.currentTimeMillis();
            return false;
        }
        if (System.currentTimeMillis() - this.stuckTimer > 4000L) {
            if (Math.sqrt(cur.method_10262((class_2382)this.lastPos)) <= 1.5) {
                return true;
            }
            this.stuckTimer = System.currentTimeMillis();
            this.lastPos = cur;
        }
        return false;
    }

    private void configureBaritone() {
        BaritoneAPI.getSettings().allowBreak.value = false;
        BaritoneAPI.getSettings().allowPlace.value = false;
        BaritoneAPI.getSettings().sprintInWater.value = false;
    }

    private void restoreBaritone() {
        BaritoneAPI.getSettings().allowBreak.value = true;
        BaritoneAPI.getSettings().allowPlace.value = true;
        BaritoneAPI.getSettings().sprintInWater.value = true;
    }

    @Generated
    public static AutoBeansModule getInstance() {
        return instance;
    }

    private static enum State {
        SCAN,
        PATH,
        HARVEST,
        REPLANT,
        WAIT;

    }
}

