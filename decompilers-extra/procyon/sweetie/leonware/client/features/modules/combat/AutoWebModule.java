// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_1802;
import net.minecraft.class_1922;
import net.minecraft.class_2350;
import net.minecraft.class_239;
import net.minecraft.class_3959;
import net.minecraft.class_2680;
import net.minecraft.class_2382;
import net.minecraft.class_2374;
import net.minecraft.class_243;
import java.util.Comparator;
import net.minecraft.class_1657;
import net.minecraft.class_2246;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_1269;
import net.minecraft.class_1268;
import net.minecraft.class_2868;
import sweetie.leonware.api.utils.other.SlownessManager;
import sweetie.leonware.api.utils.player.InventoryUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import net.minecraft.class_3965;
import sweetie.leonware.api.event.EventListener;
import net.minecraft.class_2596;
import sweetie.leonware.api.utils.other.NetworkUtil;
import net.minecraft.class_2828;
import net.minecraft.class_3532;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;
import sweetie.leonware.client.features.modules.movement.MoveFixModule;
import sweetie.leonware.api.utils.rotation.rotations.SmoothRotation;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.event.events.other.RotationUpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import net.minecraft.class_2338;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Auto Web", category = Category.COMBAT)
public class AutoWebModule extends Module
{
    private static final AutoWebModule instance;
    private final ModeSetting placeMode;
    private final ModeSetting swapMode;
    private final MultiBooleanSetting targets;
    private final BooleanSetting predict;
    private final SliderSetting delay;
    private final ModeSetting rotationMode;
    private final SliderSetting smoothYaw;
    private final SliderSetting smoothPitch;
    private final BooleanSetting stopOnAura;
    private final SliderSetting range;
    private final SliderSetting blocksPerTick;
    private final TimerUtil placeTimer;
    private class_2338 pendingPlacement;
    
    public AutoWebModule() {
        this.placeMode = new ModeSetting("Place Mode").values("Legs", "Full").value("Legs");
        this.swapMode = new ModeSetting("Swap Mode").values("Packet", "Vanilla").value("Packet");
        this.targets = new MultiBooleanSetting("Targets").value(new BooleanSetting("Self").value(false), new BooleanSetting("Players").value(true));
        this.predict = new BooleanSetting("Predict").value(true);
        this.delay = new SliderSetting("Delay").value(0.0f).range(0.0f, 500.0f).step(10.0f);
        this.rotationMode = new ModeSetting("Rotation").values("None", "Packet", "Smooth").value("Packet");
        this.smoothYaw = new SliderSetting("Smooth Yaw Speed").value(40.0f).range(5.0f, 180.0f).step(1.0f).setVisible(() -> this.rotationMode.is("Smooth"));
        this.smoothPitch = new SliderSetting("Smooth Pitch Speed").value(20.0f).range(5.0f, 90.0f).step(1.0f).setVisible(() -> this.rotationMode.is("Smooth"));
        this.stopOnAura = new BooleanSetting("Stop on Aura").value(true);
        this.range = new SliderSetting("Range").value(4.5f).range(2.0f, 6.0f).step(0.1f);
        this.blocksPerTick = new SliderSetting("Blocks/Tick").value(8.0f).range(1.0f, 12.0f).step(1.0f);
        this.placeTimer = new TimerUtil();
        this.pendingPlacement = null;
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
        final EventListener rotationEvent = RotationUpdateEvent.getInstance().subscribe(new Listener<RotationUpdateEvent>(event -> {
            if (AutoWebModule.mc.field_1724 == null || AutoWebModule.mc.field_1687 == null) {
                return;
            }
            else if (this.shouldStop()) {
                return;
            }
            else if (!this.rotationMode.is("Smooth")) {
                return;
            }
            else {
                final class_2338 targetBlock = this.getNextPlacePos();
                if (targetBlock == null) {
                    return;
                }
                else {
                    final class_3965 hit = this.getPlaceResult(targetBlock);
                    if (hit == null) {
                        return;
                    }
                    else {
                        final Rotation targetRotation = RotationUtil.rotationAt(hit.method_17784());
                        new RotationStrategy(new SmoothRotation(this.smoothYaw.getValue(), this.smoothPitch.getValue()), MoveFixModule.enabled(), MoveFixModule.isFree());
                        final RotationStrategy rotationStrategy;
                        final RotationStrategy strategy = rotationStrategy.ticksUntilReset(3);
                        RotationManager.getInstance().addRotation(targetRotation, strategy, TaskPriority.HIGH, this);
                        this.pendingPlacement = targetBlock;
                        return;
                    }
                }
            }
        }));
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (AutoWebModule.mc.field_1724 == null || AutoWebModule.mc.field_1687 == null) {
                return;
            }
            else if (this.shouldStop()) {
                return;
            }
            else if (!this.placeTimer.finished((long)(float)this.delay.getValue())) {
                return;
            }
            else {
                final int webSlot = this.findWebSlot();
                if (webSlot == -1) {
                    return;
                }
                else {
                    if (this.rotationMode.is("Smooth")) {
                        if (this.pendingPlacement != null) {
                            final class_3965 hit2 = this.getPlaceResult(this.pendingPlacement);
                            if (hit2 == null) {
                                this.pendingPlacement = null;
                            }
                            else {
                                final Rotation current = RotationManager.getInstance().getRotation();
                                final Rotation needed = RotationUtil.rotationAt(hit2.method_17784());
                                final float diff = (float)Math.hypot(class_3532.method_15393(current.getYaw() - needed.getYaw()), class_3532.method_15393(current.getPitch() - needed.getPitch()));
                                if (diff <= 20.0f) {
                                    this.doPlaceBlock(hit2, webSlot);
                                    this.placeTimer.reset();
                                    this.pendingPlacement = null;
                                }
                            }
                        }
                    }
                    else {
                        int placed = 0;
                        final int maxPerTick = (int)(float)this.blocksPerTick.getValue();
                        while (placed < maxPerTick) {
                            final class_2338 targetBlock2 = this.getNextPlacePos();
                            if (targetBlock2 == null) {
                                break;
                            }
                            else {
                                final class_3965 hit3 = this.getPlaceResult(targetBlock2);
                                if (hit3 == null) {
                                    break;
                                }
                                else {
                                    if (this.rotationMode.is("Packet")) {
                                        final float[] angle = this.calculateAngle(hit3.method_17784());
                                        NetworkUtil.sendPacket((class_2596<?>)new class_2828.class_2831(angle[0], angle[1], AutoWebModule.mc.field_1724.method_24828(), AutoWebModule.mc.field_1724.field_5976));
                                    }
                                    this.doPlaceBlock(hit3, webSlot);
                                    ++placed;
                                }
                            }
                        }
                        if (placed > 0) {
                            this.placeTimer.reset();
                        }
                    }
                    return;
                }
            }
        }));
        this.addEvents(rotationEvent, updateEvent);
    }
    
    private void doPlaceBlock(final class_3965 hit, final int webSlot) {
        final int prevSlot = AutoWebModule.mc.field_1724.method_31548().field_7545;
        final boolean needSwap = prevSlot != webSlot;
        if (needSwap && webSlot >= 9) {
            int hotbar = InventoryUtil.findBestSlotInHotBar();
            if (hotbar == -1) {
                hotbar = 8;
            }
            final int fHotbar = hotbar;
            final int fWebSlot = webSlot;
            final Runnable action = () -> {
                InventoryUtil.swapSlots(fWebSlot, fHotbar);
                this.switchAndPlace(fHotbar, prevSlot, hit);
                InventoryUtil.swapSlots(fWebSlot, fHotbar);
                return;
            };
            if (SlownessManager.isEnabled()) {
                SlownessManager.applySlowness(15L, action);
            }
            else {
                action.run();
            }
            return;
        }
        if (needSwap) {
            this.switchAndPlace(webSlot, prevSlot, hit);
        }
        else {
            this.place(hit);
        }
    }
    
    private void switchAndPlace(final int toSlot, final int restoreSlot, final class_3965 hit) {
        final String s = this.swapMode.getValue();
        switch (s) {
            case "Packet": {
                NetworkUtil.sendPacket((class_2596<?>)new class_2868(toSlot));
                this.place(hit);
                NetworkUtil.sendPacket((class_2596<?>)new class_2868(restoreSlot));
                break;
            }
            case "Vanilla": {
                AutoWebModule.mc.field_1724.method_31548().field_7545 = toSlot;
                this.place(hit);
                AutoWebModule.mc.field_1724.method_31548().field_7545 = restoreSlot;
                break;
            }
        }
    }
    
    private void place(final class_3965 hit) {
        final class_1269 result = AutoWebModule.mc.field_1761.method_2896(AutoWebModule.mc.field_1724, class_1268.field_5808, hit);
        if (result.method_23665()) {
            AutoWebModule.mc.field_1724.method_6104(class_1268.field_5808);
        }
    }
    
    private class_2338 getNextPlacePos() {
        final class_1297 target = this.findTarget();
        if (target == null) {
            return null;
        }
        final List<class_2338> positions = this.getWebPositions(target);
        for (final class_2338 pos : positions) {
            if (this.getPlaceResult(pos) != null) {
                return pos;
            }
        }
        return null;
    }
    
    private class_1297 findTarget() {
        final boolean targetSelf = this.targets.isEnabled("Self");
        final boolean targetPlayers = this.targets.isEnabled("Players");
        final List<class_1297> candidates = new ArrayList<class_1297>();
        if (targetSelf && AutoWebModule.mc.field_1724 != null) {
            final class_2338 selfLegs = AutoWebModule.mc.field_1724.method_24515();
            if (!AutoWebModule.mc.field_1687.method_8320(selfLegs).method_27852(class_2246.field_10343)) {
                candidates.add((class_1297)AutoWebModule.mc.field_1724);
            }
        }
        if (targetPlayers) {
            for (final class_1657 player : AutoWebModule.mc.field_1687.method_18456()) {
                if (player == AutoWebModule.mc.field_1724) {
                    continue;
                }
                if (!player.method_5805()) {
                    continue;
                }
                if (player.method_7325()) {
                    continue;
                }
                if (AutoWebModule.mc.field_1724.method_5739((class_1297)player) > this.range.getValue()) {
                    continue;
                }
                candidates.add((class_1297)player);
            }
        }
        if (candidates.isEmpty()) {
            return null;
        }
        candidates.sort(Comparator.comparingDouble(e -> AutoWebModule.mc.field_1724.method_5858(e)));
        return candidates.get(0);
    }
    
    private List<class_2338> getWebPositions(final class_1297 target) {
        final List<class_2338> positions = new ArrayList<class_2338>();
        class_243 targetPos;
        if (this.predict.getValue() && target != AutoWebModule.mc.field_1724) {
            final class_243 velocity = new class_243(target.method_23317() - target.field_6014, target.method_23318() - target.field_6036, target.method_23321() - target.field_5969);
            targetPos = target.method_19538().method_1019(velocity);
        }
        else {
            targetPos = target.method_19538();
        }
        final class_2338 legsPos = class_2338.method_49638((class_2374)targetPos);
        if (this.canPlaceWebAt(legsPos)) {
            positions.add(legsPos);
        }
        if (this.placeMode.is("Full")) {
            final class_2338 bodyPos = legsPos.method_10084();
            if (this.canPlaceWebAt(bodyPos)) {
                positions.add(bodyPos);
            }
        }
        final class_243 eyePos = AutoWebModule.mc.field_1724.method_33571();
        positions.sort(Comparator.comparingDouble(p -> eyePos.method_1025(class_243.method_24953((class_2382)p))));
        return positions;
    }
    
    private boolean canPlaceWebAt(final class_2338 pos) {
        if (this.squaredDistanceFromEyes(class_243.method_24953((class_2382)pos)) > this.range.getValue() * this.range.getValue()) {
            return false;
        }
        final class_2680 state = AutoWebModule.mc.field_1687.method_8320(pos);
        return state.method_45474() || state.method_26215();
    }
    
    private class_3965 getPlaceResult(final class_2338 bp) {
        if (!AutoWebModule.mc.field_1687.method_8320(bp).method_45474()) {
            return null;
        }
        final ArrayList<SupportBlock> supports = this.getSupportBlocks(bp);
        if (supports.isEmpty()) {
            return null;
        }
        final class_243 eyePos = AutoWebModule.mc.field_1724.method_33571();
        for (final SupportBlock support : supports) {
            final class_243 hitVec = new class_243(support.position.method_10263() + 0.5 + support.facing.method_62675().method_10263() * 0.5, support.position.method_10264() + 0.5 + support.facing.method_62675().method_10264() * 0.5, support.position.method_10260() + 0.5 + support.facing.method_62675().method_10260() * 0.5);
            if (this.squaredDistanceFromEyes(hitVec) > this.range.getValue() * this.range.getValue()) {
                continue;
            }
            final class_3965 wallCheck = AutoWebModule.mc.field_1687.method_17742(new class_3959(eyePos, hitVec, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, (class_1297)AutoWebModule.mc.field_1724));
            final boolean blocked = wallCheck != null && wallCheck.method_17783() == class_239.class_240.field_1332 && !wallCheck.method_17777().equals((Object)support.position);
            if (!blocked) {
                return new class_3965(hitVec, support.facing, support.position, false);
            }
        }
        return null;
    }
    
    private ArrayList<SupportBlock> getSupportBlocks(final class_2338 bp) {
        final ArrayList<SupportBlock> list = new ArrayList<SupportBlock>();
        this.checkSupport(list, bp.method_10074(), class_2350.field_11036);
        this.checkSupport(list, bp.method_10084(), class_2350.field_11033);
        this.checkSupport(list, bp.method_10095(), class_2350.field_11035);
        this.checkSupport(list, bp.method_10072(), class_2350.field_11043);
        this.checkSupport(list, bp.method_10067(), class_2350.field_11034);
        this.checkSupport(list, bp.method_10078(), class_2350.field_11039);
        return list;
    }
    
    private void checkSupport(final ArrayList<SupportBlock> list, final class_2338 neighbor, final class_2350 facing) {
        final class_2680 state = AutoWebModule.mc.field_1687.method_8320(neighbor);
        if (state.method_26212((class_1922)AutoWebModule.mc.field_1687, neighbor)) {
            list.add(new SupportBlock(neighbor, facing));
        }
    }
    
    private int findWebSlot() {
        for (int i = 0; i < 9; ++i) {
            if (AutoWebModule.mc.field_1724.method_31548().method_5438(i).method_31574(class_1802.field_8786)) {
                return i;
            }
        }
        for (int i = 9; i < 36; ++i) {
            if (AutoWebModule.mc.field_1724.method_31548().method_5438(i).method_31574(class_1802.field_8786)) {
                return i;
            }
        }
        return -1;
    }
    
    private boolean shouldStop() {
        if (this.stopOnAura.getValue()) {
            final AuraModule aura = AuraModule.getInstance();
            if (aura.isEnabled() && aura.target != null) {
                return true;
            }
        }
        return false;
    }
    
    private float squaredDistanceFromEyes(final class_243 vec) {
        final double dx = vec.field_1352 - AutoWebModule.mc.field_1724.method_23317();
        final double dy = vec.field_1351 - (AutoWebModule.mc.field_1724.method_23318() + AutoWebModule.mc.field_1724.method_18381(AutoWebModule.mc.field_1724.method_18376()));
        final double dz = vec.field_1350 - AutoWebModule.mc.field_1724.method_23321();
        return (float)(dx * dx + dy * dy + dz * dz);
    }
    
    private float[] calculateAngle(final class_243 to) {
        final class_243 from = AutoWebModule.mc.field_1724.method_33571();
        final double diffX = to.field_1352 - from.field_1352;
        final double diffY = (to.field_1351 - from.field_1351) * -1.0;
        final double diffZ = to.field_1350 - from.field_1350;
        final double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)class_3532.method_15338(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        final float pitch = (float)class_3532.method_15350(class_3532.method_15338(Math.toDegrees(Math.atan2(diffY, dist))), -90.0, 90.0);
        return new float[] { yaw, pitch };
    }
    
    @Override
    public String getDisplayInfo() {
        return this.placeMode.getValue();
    }
    
    @Generated
    public static AutoWebModule getInstance() {
        return AutoWebModule.instance;
    }
    
    static {
        instance = new AutoWebModule();
    }
    
    record SupportBlock(class_2338 position, class_2350 facing) {}
}
