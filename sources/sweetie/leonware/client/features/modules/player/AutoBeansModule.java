package sweetie.leonware.client.features.modules.player;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalNear;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1713;
import net.minecraft.class_1743;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2282;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import org.newsclub.net.unix.AFVSOCKSocketAddress;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.utils.other.TextUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/AutoBeansModule.class */
@ModuleRegister(name = "Auto Beans", category = Category.PLAYER)
public class AutoBeansModule extends Module {
    private static final AutoBeansModule instance = new AutoBeansModule();
    private final BooleanSetting useAxe = new BooleanSetting("Использовать топор").value((Boolean) true);
    public class_2338 pos1 = null;
    public class_2338 pos2 = null;
    private State state = State.SCAN;
    private final Set<class_2338> skipSet = new HashSet();
    private class_2338 targetBean = null;
    private class_2338 targetLog = null;
    private class_2350 targetFacing = null;
    private long waitTimer = 0;
    private long stuckTimer = 0;
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

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/AutoBeansModule$State.class */
    private enum State {
        SCAN,
        PATH,
        HARVEST,
        REPLANT,
        WAIT
    }

    @Generated
    public static AutoBeansModule getInstance() {
        return instance;
    }

    public AutoBeansModule() {
        addSettings(this.useAxe);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        if (this.pos1 == null || this.pos2 == null) {
            TextUtil.sendMessage("§cAutoBean: Сначала установите точки через $autobean pos1 и $autobean pos2");
            toggle();
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
        clearTarget();
        this.waitTimer = 0L;
        this.stuckTimer = System.currentTimeMillis();
        this.lastPos = null;
        this.harvestedCount = 0;
        this.replantedCount = 0;
        this.actionDelayTicks = 0;
        this.actionRetries = 0;
        configureBaritone();
        TextUtil.sendMessage("§aAutoBean: Включён. Зона определена.");
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        cancelBaritone();
        restoreBaritone();
        TextUtil.sendMessage("§cAutoBean: Выключен. Собрано: " + this.harvestedCount + " | Посажено: " + this.replantedCount);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        addEvents(UpdateEvent.getInstance().subscribe(new Listener(event -> {
            tick();
        })));
    }

    private void tick() {
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return;
        }
        if (this.actionDelayTicks > 0) {
            this.actionDelayTicks--;
        }
        switch (this.state.ordinal()) {
            case 0:
                doScan();
                break;
            case 1:
                doPath();
                break;
            case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                doHarvest();
                break;
            case 3:
                doReplant();
                break;
            case 4:
                if (System.currentTimeMillis() >= this.waitTimer) {
                    this.skipSet.clear();
                    this.state = State.SCAN;
                }
                break;
        }
    }

    private void doScan() {
        List<class_2338> visibleBeans = new ArrayList<>();
        List<class_2338> hiddenBeans = new ArrayList<>();
        for (int x = this.minX; x <= this.maxX; x++) {
            for (int y = this.minY; y <= this.maxY; y++) {
                for (int z = this.minZ; z <= this.maxZ; z++) {
                    class_2338 pos = new class_2338(x, y, z);
                    if (!this.skipSet.contains(pos)) {
                        class_2680 bs = mc.field_1687.method_8320(pos);
                        if (bs.method_26204() == class_2246.field_10302 && ((Integer) bs.method_11654(class_2282.field_10779)).intValue() == 2) {
                            if (isVisible(pos)) {
                                visibleBeans.add(pos);
                            } else {
                                hiddenBeans.add(pos);
                            }
                        }
                    }
                }
            }
        }
        if (!visibleBeans.isEmpty()) {
            visibleBeans.sort(Comparator.comparingDouble(p -> {
                return mc.field_1724.method_33571().method_1022(class_243.method_24953(p));
            }));
            setTarget(visibleBeans.get(0));
            if (mc.field_1724.method_33571().method_1022(class_243.method_24953(this.targetBean)) <= 4.0d) {
                cancelBaritone();
                this.state = State.HARVEST;
                this.actionRetries = 0;
                return;
            } else {
                this.state = State.PATH;
                startPathing(this.targetBean);
                return;
            }
        }
        if (!hiddenBeans.isEmpty()) {
            hiddenBeans.sort(Comparator.comparingDouble(p2 -> {
                return mc.field_1724.method_33571().method_1022(class_243.method_24953(p2));
            }));
            setTarget(hiddenBeans.get(0));
            this.state = State.PATH;
            startPathing(this.targetBean);
            return;
        }
        this.waitTimer = System.currentTimeMillis() + 1000;
        this.state = State.WAIT;
    }

    private void doPath() {
        if (this.targetBean == null) {
            this.state = State.SCAN;
            return;
        }
        double dist = mc.field_1724.method_33571().method_1022(class_243.method_24953(this.targetBean));
        if (dist <= 4.0d && isVisible(this.targetBean)) {
            cancelBaritone();
            this.state = State.HARVEST;
            this.actionRetries = 0;
        } else if (isStuck()) {
            this.skipSet.add(this.targetBean);
            clearTarget();
        } else if (!isBaritonePathing()) {
            startPathing(this.targetBean);
        }
    }

    private void doHarvest() {
        if (this.targetBean == null) {
            this.state = State.SCAN;
            return;
        }
        class_2680 bs = mc.field_1687.method_8320(this.targetBean);
        if (bs.method_26204() != class_2246.field_10302 || ((Integer) bs.method_11654(class_2282.field_10779)).intValue() != 2) {
            this.harvestedCount++;
            this.actionRetries = 0;
            this.state = State.REPLANT;
        } else {
            if (this.actionRetries > 12) {
                this.skipSet.add(this.targetBean);
                clearTarget();
                return;
            }
            lookAt(class_243.method_24953(this.targetBean));
            if (this.useAxe.getValue().booleanValue()) {
                equipBestAxe();
            }
            mc.field_1761.method_2910(this.targetBean, this.targetFacing.method_10153());
            mc.field_1724.method_6104(class_1268.field_5808);
            this.actionRetries++;
            this.actionDelayTicks = 6;
        }
    }

    private void doReplant() {
        if (this.targetBean == null || this.targetLog == null) {
            clearTarget();
            return;
        }
        class_2680 currentBlock = mc.field_1687.method_8320(this.targetBean);
        if (currentBlock.method_26204() == class_2246.field_10302) {
            this.replantedCount++;
            clearTarget();
            return;
        }
        class_2680 logState = mc.field_1687.method_8320(this.targetLog);
        if (!currentBlock.method_45474() || this.actionRetries > 12 || !isJungleLog(logState)) {
            this.skipSet.add(this.targetBean);
            clearTarget();
            return;
        }
        int beansSlot = findCocoaBeansHotbar();
        if (beansSlot == -1) {
            beansSlot = moveBeansToHotbar();
            if (beansSlot == -1) {
                TextUtil.sendMessage("§eAutoBean: Нет какао-бобов.");
                toggle();
                return;
            }
        }
        class_243 hitVec = class_243.method_24953(this.targetLog).method_1031(((double) this.targetFacing.method_10148()) * 0.5d, ((double) this.targetFacing.method_10164()) * 0.5d, ((double) this.targetFacing.method_10165()) * 0.5d);
        mc.field_1724.method_31548().field_7545 = beansSlot;
        lookAt(hitVec);
        mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, new class_3965(hitVec, this.targetFacing.method_10153(), this.targetLog, false));
        mc.field_1724.method_6104(class_1268.field_5808);
        this.actionRetries++;
        this.actionDelayTicks = 8;
    }

    private void setTarget(class_2338 pos) {
        this.targetBean = pos;
        class_2680 bs = mc.field_1687.method_8320(pos);
        if (bs.method_26204() == class_2246.field_10302) {
            this.targetFacing = bs.method_11654(class_2282.field_11177);
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
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return false;
        }
        class_243 eye = mc.field_1724.method_33571();
        class_243 target = class_243.method_24953(pos);
        class_3959 context = new class_3959(eye, target, class_3959.class_3960.field_17559, class_3959.class_242.field_1348, mc.field_1724);
        class_3965 result = mc.field_1687.method_17742(context);
        if (result.method_17783() == class_239.class_240.field_1333) {
            return true;
        }
        class_2338 hit = result.method_17777();
        if (hit.equals(pos)) {
            return true;
        }
        class_2248 block = mc.field_1687.method_8320(hit).method_26204();
        return block == class_2246.field_10302;
    }

    private int findCocoaBeansHotbar() {
        for (int i = 0; i < 9; i++) {
            if (mc.field_1724.method_31548().method_5438(i).method_31574(class_1802.field_8116)) {
                return i;
            }
        }
        return -1;
    }

    private int moveBeansToHotbar() {
        for (int i = 9; i < 36; i++) {
            if (mc.field_1724.method_31548().method_5438(i).method_31574(class_1802.field_8116)) {
                int slot = findSafeHotbarSlot();
                if (slot == -1) {
                    return -1;
                }
                mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, i, slot, class_1713.field_7791, mc.field_1724);
                return slot;
            }
        }
        return -1;
    }

    private int findSafeHotbarSlot() {
        for (int i = 0; i < 9; i++) {
            if (mc.field_1724.method_31548().method_5438(i).method_7960()) {
                return i;
            }
        }
        for (int i2 = 0; i2 < 9; i2++) {
            if (i2 != mc.field_1724.method_31548().field_7545) {
                class_1792 item = mc.field_1724.method_31548().method_5438(i2).method_7909();
                if (!(item instanceof class_1743)) {
                    return i2;
                }
            }
        }
        return -1;
    }

    private void equipBestAxe() {
        int tier;
        int best = -1;
        int bestTier = -1;
        for (int i = 0; i < 9; i++) {
            class_1792 item = mc.field_1724.method_31548().method_5438(i).method_7909();
            if ((item instanceof class_1743) && (tier = getAxeTier(item)) > bestTier) {
                bestTier = tier;
                best = i;
            }
        }
        if (best != -1) {
            mc.field_1724.method_31548().field_7545 = best;
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
        return item == class_1802.field_8406 ? 0 : -1;
    }

    private boolean isJungleLog(class_2680 state) {
        class_2248 b = state.method_26204();
        return b == class_2246.field_10306 || b == class_2246.field_10254 || b == class_2246.field_10303 || b == class_2246.field_10084;
    }

    private void lookAt(class_243 target) {
        if (mc.field_1724 == null) {
            return;
        }
        class_243 eye = mc.field_1724.method_33571();
        class_243 dir = target.method_1020(eye);
        double h = Math.sqrt((dir.field_1352 * dir.field_1352) + (dir.field_1350 * dir.field_1350));
        float yaw = ((float) Math.toDegrees(Math.atan2(dir.field_1350, dir.field_1352))) - 90.0f;
        float pitch = (float) (-Math.toDegrees(Math.atan2(dir.field_1351, h)));
        mc.field_1724.method_36456(yaw);
        mc.field_1724.method_36457(class_3532.method_15363(pitch, -90.0f, 90.0f));
    }

    private void startPathing(class_2338 pos) {
        configureBaritone();
        BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalNear(pos, 1));
        this.lastPos = mc.field_1724 != null ? mc.field_1724.method_24515() : pos;
        this.stuckTimer = System.currentTimeMillis();
    }

    private boolean isBaritonePathing() {
        return BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().isActive();
    }

    private void cancelBaritone() {
        BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
    }

    private boolean isStuck() {
        if (mc.field_1724 == null) {
            return false;
        }
        class_2338 cur = mc.field_1724.method_24515();
        if (this.lastPos == null) {
            this.lastPos = cur;
            this.stuckTimer = System.currentTimeMillis();
            return false;
        }
        if (System.currentTimeMillis() - this.stuckTimer <= 4000) {
            return false;
        }
        if (Math.sqrt(cur.method_10262(this.lastPos)) <= 1.5d) {
            return true;
        }
        this.stuckTimer = System.currentTimeMillis();
        this.lastPos = cur;
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
}
