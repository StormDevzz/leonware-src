/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1268
 *  net.minecraft.class_1269
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_1802
 *  net.minecraft.class_1922
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_2374
 *  net.minecraft.class_2382
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_243
 *  net.minecraft.class_2680
 *  net.minecraft.class_2828$class_2831
 *  net.minecraft.class_2868
 *  net.minecraft.class_3532
 *  net.minecraft.class_3959
 *  net.minecraft.class_3959$class_242
 *  net.minecraft.class_3959$class_3960
 *  net.minecraft.class_3965
 */
package sweetie.leonware.client.features.modules.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1802;
import net.minecraft.class_1922;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2374;
import net.minecraft.class_2382;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_2828;
import net.minecraft.class_2868;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.other.RotationUpdateEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.other.NetworkUtil;
import sweetie.leonware.api.utils.other.SlownessManager;
import sweetie.leonware.api.utils.player.InventoryUtil;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.rotation.rotations.SmoothRotation;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.client.features.modules.movement.MoveFixModule;

@ModuleRegister(name="Auto Web", category=Category.COMBAT)
public class AutoWebModule
extends Module {
    private static final AutoWebModule instance = new AutoWebModule();
    private final ModeSetting placeMode = new ModeSetting("Place Mode").values("Legs", "Full").value("Legs");
    private final ModeSetting swapMode = new ModeSetting("Swap Mode").values("Packet", "Vanilla").value("Packet");
    private final MultiBooleanSetting targets = new MultiBooleanSetting("Targets").value(new BooleanSetting("Self").value(false), new BooleanSetting("Players").value(true));
    private final BooleanSetting predict = new BooleanSetting("Predict").value(true);
    private final SliderSetting delay = new SliderSetting("Delay").value(Float.valueOf(0.0f)).range(0.0f, 500.0f).step(10.0f);
    private final ModeSetting rotationMode = new ModeSetting("Rotation").values("None", "Packet", "Smooth").value("Packet");
    private final SliderSetting smoothYaw = new SliderSetting("Smooth Yaw Speed").value(Float.valueOf(40.0f)).range(5.0f, 180.0f).step(1.0f).setVisible(() -> this.rotationMode.is("Smooth"));
    private final SliderSetting smoothPitch = new SliderSetting("Smooth Pitch Speed").value(Float.valueOf(20.0f)).range(5.0f, 90.0f).step(1.0f).setVisible(() -> this.rotationMode.is("Smooth"));
    private final BooleanSetting stopOnAura = new BooleanSetting("Stop on Aura").value(true);
    private final SliderSetting range = new SliderSetting("Range").value(Float.valueOf(4.5f)).range(2.0f, 6.0f).step(0.1f);
    private final SliderSetting blocksPerTick = new SliderSetting("Blocks/Tick").value(Float.valueOf(8.0f)).range(1.0f, 12.0f).step(1.0f);
    private final TimerUtil placeTimer = new TimerUtil();
    private class_2338 pendingPlacement = null;

    public AutoWebModule() {
        this.addSettings(this.placeMode, this.swapMode, this.targets, this.predict, this.delay, this.rotationMode, this.smoothYaw, this.smoothPitch, this.stopOnAura, this.range, this.blocksPerTick);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.pendingPlacement = null;
        this.placeTimer.reset();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.pendingPlacement = null;
    }

    @Override
    public void onEvent() {
        EventListener rotationEvent = RotationUpdateEvent.getInstance().subscribe(new Listener<RotationUpdateEvent>(event -> {
            if (AutoWebModule.mc.field_1724 == null || AutoWebModule.mc.field_1687 == null) {
                return;
            }
            if (this.shouldStop()) {
                return;
            }
            if (!this.rotationMode.is("Smooth")) {
                return;
            }
            class_2338 targetBlock = this.getNextPlacePos();
            if (targetBlock == null) {
                return;
            }
            class_3965 hit = this.getPlaceResult(targetBlock);
            if (hit == null) {
                return;
            }
            Rotation targetRotation = RotationUtil.rotationAt(hit.method_17784());
            RotationStrategy strategy = new RotationStrategy(new SmoothRotation(((Float)this.smoothYaw.getValue()).floatValue(), ((Float)this.smoothPitch.getValue()).floatValue()), MoveFixModule.enabled(), MoveFixModule.isFree()).ticksUntilReset(3);
            RotationManager.getInstance().addRotation(targetRotation, strategy, TaskPriority.HIGH, this);
            this.pendingPlacement = targetBlock;
        }));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (AutoWebModule.mc.field_1724 == null || AutoWebModule.mc.field_1687 == null) {
                return;
            }
            if (this.shouldStop()) {
                return;
            }
            if (!this.placeTimer.finished((long)((Float)this.delay.getValue()).floatValue())) {
                return;
            }
            int webSlot = this.findWebSlot();
            if (webSlot == -1) {
                return;
            }
            if (this.rotationMode.is("Smooth")) {
                if (this.pendingPlacement == null) {
                    return;
                }
                class_3965 hit = this.getPlaceResult(this.pendingPlacement);
                if (hit == null) {
                    this.pendingPlacement = null;
                    return;
                }
                Rotation current = RotationManager.getInstance().getRotation();
                Rotation needed = RotationUtil.rotationAt(hit.method_17784());
                float diff = (float)Math.hypot(class_3532.method_15393((float)(current.getYaw() - needed.getYaw())), class_3532.method_15393((float)(current.getPitch() - needed.getPitch())));
                if (diff > 20.0f) {
                    return;
                }
                this.doPlaceBlock(hit, webSlot);
                this.placeTimer.reset();
                this.pendingPlacement = null;
            } else {
                class_3965 hit;
                class_2338 targetBlock;
                int placed;
                int maxPerTick = (int)((Float)this.blocksPerTick.getValue()).floatValue();
                for (placed = 0; placed < maxPerTick && (targetBlock = this.getNextPlacePos()) != null && (hit = this.getPlaceResult(targetBlock)) != null; ++placed) {
                    if (this.rotationMode.is("Packet")) {
                        float[] angle = this.calculateAngle(hit.method_17784());
                        NetworkUtil.sendPacket(new class_2828.class_2831(angle[0], angle[1], AutoWebModule.mc.field_1724.method_24828(), AutoWebModule.mc.field_1724.field_5976));
                    }
                    this.doPlaceBlock(hit, webSlot);
                }
                if (placed > 0) {
                    this.placeTimer.reset();
                }
            }
        }));
        this.addEvents(rotationEvent, updateEvent);
    }

    private void doPlaceBlock(class_3965 hit, int webSlot) {
        boolean needSwap;
        int prevSlot = AutoWebModule.mc.field_1724.method_31548().field_7545;
        boolean bl = needSwap = prevSlot != webSlot;
        if (needSwap && webSlot >= 9) {
            int hotbar = InventoryUtil.findBestSlotInHotBar();
            if (hotbar == -1) {
                hotbar = 8;
            }
            int fHotbar = hotbar;
            int fWebSlot = webSlot;
            Runnable action = () -> {
                InventoryUtil.swapSlots(fWebSlot, fHotbar);
                this.switchAndPlace(fHotbar, prevSlot, hit);
                InventoryUtil.swapSlots(fWebSlot, fHotbar);
            };
            if (SlownessManager.isEnabled()) {
                SlownessManager.applySlowness(15L, action);
            } else {
                action.run();
            }
            return;
        }
        if (needSwap) {
            this.switchAndPlace(webSlot, prevSlot, hit);
        } else {
            this.place(hit);
        }
    }

    private void switchAndPlace(int toSlot, int restoreSlot, class_3965 hit) {
        switch ((String)this.swapMode.getValue()) {
            case "Packet": {
                NetworkUtil.sendPacket(new class_2868(toSlot));
                this.place(hit);
                NetworkUtil.sendPacket(new class_2868(restoreSlot));
                break;
            }
            case "Vanilla": {
                AutoWebModule.mc.field_1724.method_31548().field_7545 = toSlot;
                this.place(hit);
                AutoWebModule.mc.field_1724.method_31548().field_7545 = restoreSlot;
            }
        }
    }

    private void place(class_3965 hit) {
        class_1269 result = AutoWebModule.mc.field_1761.method_2896(AutoWebModule.mc.field_1724, class_1268.field_5808, hit);
        if (result.method_23665()) {
            AutoWebModule.mc.field_1724.method_6104(class_1268.field_5808);
        }
    }

    private class_2338 getNextPlacePos() {
        class_1297 target = this.findTarget();
        if (target == null) {
            return null;
        }
        List<class_2338> positions = this.getWebPositions(target);
        for (class_2338 pos : positions) {
            if (this.getPlaceResult(pos) == null) continue;
            return pos;
        }
        return null;
    }

    private class_1297 findTarget() {
        class_2338 selfLegs;
        boolean targetSelf = this.targets.isEnabled("Self");
        boolean targetPlayers = this.targets.isEnabled("Players");
        ArrayList<Object> candidates = new ArrayList<Object>();
        if (targetSelf && AutoWebModule.mc.field_1724 != null && !AutoWebModule.mc.field_1687.method_8320(selfLegs = AutoWebModule.mc.field_1724.method_24515()).method_27852(class_2246.field_10343)) {
            candidates.add(AutoWebModule.mc.field_1724);
        }
        if (targetPlayers) {
            for (class_1657 player : AutoWebModule.mc.field_1687.method_18456()) {
                if (player == AutoWebModule.mc.field_1724 || !player.method_5805() || player.method_7325() || AutoWebModule.mc.field_1724.method_5739((class_1297)player) > ((Float)this.range.getValue()).floatValue()) continue;
                candidates.add(player);
            }
        }
        if (candidates.isEmpty()) {
            return null;
        }
        candidates.sort(Comparator.comparingDouble(e -> AutoWebModule.mc.field_1724.method_5858(e)));
        return (class_1297)candidates.get(0);
    }

    private List<class_2338> getWebPositions(class_1297 target) {
        class_2338 bodyPos;
        class_243 targetPos;
        ArrayList<class_2338> positions = new ArrayList<class_2338>();
        if (((Boolean)this.predict.getValue()).booleanValue() && target != AutoWebModule.mc.field_1724) {
            class_243 velocity = new class_243(target.method_23317() - target.field_6014, target.method_23318() - target.field_6036, target.method_23321() - target.field_5969);
            targetPos = target.method_19538().method_1019(velocity);
        } else {
            targetPos = target.method_19538();
        }
        class_2338 legsPos = class_2338.method_49638((class_2374)targetPos);
        if (this.canPlaceWebAt(legsPos)) {
            positions.add(legsPos);
        }
        if (this.placeMode.is("Full") && this.canPlaceWebAt(bodyPos = legsPos.method_10084())) {
            positions.add(bodyPos);
        }
        class_243 eyePos = AutoWebModule.mc.field_1724.method_33571();
        positions.sort(Comparator.comparingDouble(p -> eyePos.method_1025(class_243.method_24953((class_2382)p))));
        return positions;
    }

    private boolean canPlaceWebAt(class_2338 pos) {
        if (this.squaredDistanceFromEyes(class_243.method_24953((class_2382)pos)) > ((Float)this.range.getValue()).floatValue() * ((Float)this.range.getValue()).floatValue()) {
            return false;
        }
        class_2680 state = AutoWebModule.mc.field_1687.method_8320(pos);
        return state.method_45474() || state.method_26215();
    }

    private class_3965 getPlaceResult(class_2338 bp) {
        if (!AutoWebModule.mc.field_1687.method_8320(bp).method_45474()) {
            return null;
        }
        ArrayList<SupportBlock> supports = this.getSupportBlocks(bp);
        if (supports.isEmpty()) {
            return null;
        }
        class_243 eyePos = AutoWebModule.mc.field_1724.method_33571();
        for (SupportBlock support : supports) {
            class_3965 wallCheck;
            boolean blocked;
            class_243 hitVec = new class_243((double)support.position.method_10263() + 0.5 + (double)support.facing.method_62675().method_10263() * 0.5, (double)support.position.method_10264() + 0.5 + (double)support.facing.method_62675().method_10264() * 0.5, (double)support.position.method_10260() + 0.5 + (double)support.facing.method_62675().method_10260() * 0.5);
            if (this.squaredDistanceFromEyes(hitVec) > ((Float)this.range.getValue()).floatValue() * ((Float)this.range.getValue()).floatValue() || (blocked = (wallCheck = AutoWebModule.mc.field_1687.method_17742(new class_3959(eyePos, hitVec, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, (class_1297)AutoWebModule.mc.field_1724))) != null && wallCheck.method_17783() == class_239.class_240.field_1332 && !wallCheck.method_17777().equals((Object)support.position))) continue;
            return new class_3965(hitVec, support.facing, support.position, false);
        }
        return null;
    }

    private ArrayList<SupportBlock> getSupportBlocks(class_2338 bp) {
        ArrayList<SupportBlock> list = new ArrayList<SupportBlock>();
        this.checkSupport(list, bp.method_10074(), class_2350.field_11036);
        this.checkSupport(list, bp.method_10084(), class_2350.field_11033);
        this.checkSupport(list, bp.method_10095(), class_2350.field_11035);
        this.checkSupport(list, bp.method_10072(), class_2350.field_11043);
        this.checkSupport(list, bp.method_10067(), class_2350.field_11034);
        this.checkSupport(list, bp.method_10078(), class_2350.field_11039);
        return list;
    }

    private void checkSupport(ArrayList<SupportBlock> list, class_2338 neighbor, class_2350 facing) {
        class_2680 state = AutoWebModule.mc.field_1687.method_8320(neighbor);
        if (state.method_26212((class_1922)AutoWebModule.mc.field_1687, neighbor)) {
            list.add(new SupportBlock(neighbor, facing));
        }
    }

    private int findWebSlot() {
        int i;
        for (i = 0; i < 9; ++i) {
            if (!AutoWebModule.mc.field_1724.method_31548().method_5438(i).method_31574(class_1802.field_8786)) continue;
            return i;
        }
        for (i = 9; i < 36; ++i) {
            if (!AutoWebModule.mc.field_1724.method_31548().method_5438(i).method_31574(class_1802.field_8786)) continue;
            return i;
        }
        return -1;
    }

    private boolean shouldStop() {
        AuraModule aura;
        return (Boolean)this.stopOnAura.getValue() != false && (aura = AuraModule.getInstance()).isEnabled() && aura.target != null;
    }

    private float squaredDistanceFromEyes(class_243 vec) {
        double dx = vec.field_1352 - AutoWebModule.mc.field_1724.method_23317();
        double dy = vec.field_1351 - (AutoWebModule.mc.field_1724.method_23318() + (double)AutoWebModule.mc.field_1724.method_18381(AutoWebModule.mc.field_1724.method_18376()));
        double dz = vec.field_1350 - AutoWebModule.mc.field_1724.method_23321();
        return (float)(dx * dx + dy * dy + dz * dz);
    }

    private float[] calculateAngle(class_243 to) {
        class_243 from = AutoWebModule.mc.field_1724.method_33571();
        double diffX = to.field_1352 - from.field_1352;
        double diffY = (to.field_1351 - from.field_1351) * -1.0;
        double diffZ = to.field_1350 - from.field_1350;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)class_3532.method_15338((double)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0));
        float pitch = (float)class_3532.method_15350((double)class_3532.method_15338((double)Math.toDegrees(Math.atan2(diffY, dist))), (double)-90.0, (double)90.0);
        return new float[]{yaw, pitch};
    }

    @Override
    public String getDisplayInfo() {
        return (String)this.placeMode.getValue();
    }

    @Generated
    public static AutoWebModule getInstance() {
        return instance;
    }

    private record SupportBlock(class_2338 position, class_2350 facing) {
    }
}

