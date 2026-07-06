package sweetie.leonware.client.features.modules.combat;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1297;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_2828;
import net.minecraft.class_2868;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_746;
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
import sweetie.leonware.client.features.modules.movement.MoveFixModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/AutoWebModule.class */
@ModuleRegister(name = "Auto Web", category = Category.COMBAT)
public class AutoWebModule extends Module {
    private static final AutoWebModule instance = new AutoWebModule();
    private final ModeSetting placeMode = new ModeSetting("Place Mode").values("Legs", "Full").value("Legs");
    private final ModeSetting swapMode = new ModeSetting("Swap Mode").values("Packet", "Vanilla").value("Packet");
    private final MultiBooleanSetting targets = new MultiBooleanSetting("Targets").value(new BooleanSetting("Self").value((Boolean) false), new BooleanSetting("Players").value((Boolean) true));
    private final BooleanSetting predict = new BooleanSetting("Predict").value((Boolean) true);
    private final SliderSetting delay = new SliderSetting("Delay").value(Float.valueOf(0.0f)).range(0.0f, 500.0f).step(10.0f);
    private final ModeSetting rotationMode = new ModeSetting("Rotation").values("None", "Packet", "Smooth").value("Packet");
    private final SliderSetting smoothYaw = new SliderSetting("Smooth Yaw Speed").value(Float.valueOf(40.0f)).range(5.0f, 180.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.rotationMode.is("Smooth"));
    });
    private final SliderSetting smoothPitch = new SliderSetting("Smooth Pitch Speed").value(Float.valueOf(20.0f)).range(5.0f, 90.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.rotationMode.is("Smooth"));
    });
    private final BooleanSetting stopOnAura = new BooleanSetting("Stop on Aura").value((Boolean) true);
    private final SliderSetting range = new SliderSetting("Range").value(Float.valueOf(4.5f)).range(2.0f, 6.0f).step(0.1f);
    private final SliderSetting blocksPerTick = new SliderSetting("Blocks/Tick").value(Float.valueOf(8.0f)).range(1.0f, 12.0f).step(1.0f);
    private final TimerUtil placeTimer = new TimerUtil();
    private class_2338 pendingPlacement = null;

    @Generated
    public static AutoWebModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v21, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v26, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public AutoWebModule() {
        addSettings(this.placeMode, this.swapMode, this.targets, this.predict, this.delay, this.rotationMode, this.smoothYaw, this.smoothPitch, this.stopOnAura, this.range, this.blocksPerTick);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        super.onEnable();
        this.pendingPlacement = null;
        this.placeTimer.reset();
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        super.onDisable();
        this.pendingPlacement = null;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener rotationEvent = RotationUpdateEvent.getInstance().subscribe(new Listener(event -> {
            class_2338 targetBlock;
            class_3965 hit;
            if (mc.field_1724 == null || mc.field_1687 == null || shouldStop() || !this.rotationMode.is("Smooth") || (targetBlock = getNextPlacePos()) == null || (hit = getPlaceResult(targetBlock)) == null) {
                return;
            }
            Rotation targetRotation = RotationUtil.rotationAt(hit.method_17784());
            RotationStrategy strategy = new RotationStrategy(new SmoothRotation(this.smoothYaw.getValue().floatValue(), this.smoothPitch.getValue().floatValue()), MoveFixModule.enabled(), MoveFixModule.isFree()).ticksUntilReset(3);
            RotationManager.getInstance().addRotation(targetRotation, strategy, TaskPriority.HIGH, this);
            this.pendingPlacement = targetBlock;
        }));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event2 -> {
            int webSlot;
            class_2338 targetBlock;
            class_3965 hit;
            if (mc.field_1724 == null || mc.field_1687 == null || shouldStop() || !this.placeTimer.finished((long) this.delay.getValue().floatValue()) || (webSlot = findWebSlot()) == -1) {
                return;
            }
            if (this.rotationMode.is("Smooth")) {
                if (this.pendingPlacement == null) {
                    return;
                }
                class_3965 hit2 = getPlaceResult(this.pendingPlacement);
                if (hit2 == null) {
                    this.pendingPlacement = null;
                    return;
                }
                Rotation current = RotationManager.getInstance().getRotation();
                Rotation needed = RotationUtil.rotationAt(hit2.method_17784());
                float diff = (float) Math.hypot(class_3532.method_15393(current.getYaw() - needed.getYaw()), class_3532.method_15393(current.getPitch() - needed.getPitch()));
                if (diff > 20.0f) {
                    return;
                }
                doPlaceBlock(hit2, webSlot);
                this.placeTimer.reset();
                this.pendingPlacement = null;
                return;
            }
            int placed = 0;
            int maxPerTick = (int) this.blocksPerTick.getValue().floatValue();
            while (placed < maxPerTick && (targetBlock = getNextPlacePos()) != null && (hit = getPlaceResult(targetBlock)) != null) {
                if (this.rotationMode.is("Packet")) {
                    float[] angle = calculateAngle(hit.method_17784());
                    NetworkUtil.sendPacket((class_2596<?>) new class_2828.class_2831(angle[0], angle[1], mc.field_1724.method_24828(), mc.field_1724.field_5976));
                }
                doPlaceBlock(hit, webSlot);
                placed++;
            }
            if (placed > 0) {
                this.placeTimer.reset();
            }
        }));
        addEvents(rotationEvent, updateEvent);
    }

    private void doPlaceBlock(class_3965 hit, int webSlot) {
        int prevSlot = mc.field_1724.method_31548().field_7545;
        boolean needSwap = prevSlot != webSlot;
        if (needSwap && webSlot >= 9) {
            int hotbar = InventoryUtil.findBestSlotInHotBar();
            if (hotbar == -1) {
                hotbar = 8;
            }
            int fHotbar = hotbar;
            Runnable action = () -> {
                InventoryUtil.swapSlots(webSlot, fHotbar);
                switchAndPlace(fHotbar, prevSlot, hit);
                InventoryUtil.swapSlots(webSlot, fHotbar);
            };
            if (SlownessManager.isEnabled()) {
                SlownessManager.applySlowness(15L, action);
                return;
            } else {
                action.run();
                return;
            }
        }
        if (needSwap) {
            switchAndPlace(webSlot, prevSlot, hit);
        } else {
            place(hit);
        }
    }

    private void switchAndPlace(int toSlot, int restoreSlot, class_3965 hit) {
        switch (this.swapMode.getValue()) {
            case "Packet":
                NetworkUtil.sendPacket((class_2596<?>) new class_2868(toSlot));
                place(hit);
                NetworkUtil.sendPacket((class_2596<?>) new class_2868(restoreSlot));
                break;
            case "Vanilla":
                mc.field_1724.method_31548().field_7545 = toSlot;
                place(hit);
                mc.field_1724.method_31548().field_7545 = restoreSlot;
                break;
        }
    }

    private void place(class_3965 hit) {
        class_1269 result = mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, hit);
        if (result.method_23665()) {
            mc.field_1724.method_6104(class_1268.field_5808);
        }
    }

    private class_2338 getNextPlacePos() {
        class_1297 target = findTarget();
        if (target == null) {
            return null;
        }
        List<class_2338> positions = getWebPositions(target);
        for (class_2338 pos : positions) {
            if (getPlaceResult(pos) != null) {
                return pos;
            }
        }
        return null;
    }

    private class_1297 findTarget() {
        boolean targetSelf = this.targets.isEnabled("Self");
        boolean targetPlayers = this.targets.isEnabled("Players");
        List<class_1297> candidates = new ArrayList<>();
        if (targetSelf && mc.field_1724 != null) {
            class_2338 selfLegs = mc.field_1724.method_24515();
            if (!mc.field_1687.method_8320(selfLegs).method_27852(class_2246.field_10343)) {
                candidates.add(mc.field_1724);
            }
        }
        if (targetPlayers) {
            for (class_746 class_746Var : mc.field_1687.method_18456()) {
                if (class_746Var != mc.field_1724 && class_746Var.method_5805() && !class_746Var.method_7325() && mc.field_1724.method_5739(class_746Var) <= this.range.getValue().floatValue()) {
                    candidates.add(class_746Var);
                }
            }
        }
        if (candidates.isEmpty()) {
            return null;
        }
        candidates.sort(Comparator.comparingDouble(e -> {
            return mc.field_1724.method_5858(e);
        }));
        return candidates.get(0);
    }

    private List<class_2338> getWebPositions(class_1297 target) {
        class_243 targetPos;
        List<class_2338> positions = new ArrayList<>();
        if (this.predict.getValue().booleanValue() && target != mc.field_1724) {
            class_243 velocity = new class_243(target.method_23317() - target.field_6014, target.method_23318() - target.field_6036, target.method_23321() - target.field_5969);
            targetPos = target.method_19538().method_1019(velocity);
        } else {
            targetPos = target.method_19538();
        }
        class_2338 legsPos = class_2338.method_49638(targetPos);
        if (canPlaceWebAt(legsPos)) {
            positions.add(legsPos);
        }
        if (this.placeMode.is("Full")) {
            class_2338 bodyPos = legsPos.method_10084();
            if (canPlaceWebAt(bodyPos)) {
                positions.add(bodyPos);
            }
        }
        class_243 eyePos = mc.field_1724.method_33571();
        positions.sort(Comparator.comparingDouble(p -> {
            return eyePos.method_1025(class_243.method_24953(p));
        }));
        return positions;
    }

    private boolean canPlaceWebAt(class_2338 pos) {
        if (squaredDistanceFromEyes(class_243.method_24953(pos)) > this.range.getValue().floatValue() * this.range.getValue().floatValue()) {
            return false;
        }
        class_2680 state = mc.field_1687.method_8320(pos);
        return state.method_45474() || state.method_26215();
    }

    private class_3965 getPlaceResult(class_2338 bp) {
        if (!mc.field_1687.method_8320(bp).method_45474()) {
            return null;
        }
        ArrayList<SupportBlock> supports = getSupportBlocks(bp);
        if (supports.isEmpty()) {
            return null;
        }
        class_243 eyePos = mc.field_1724.method_33571();
        for (SupportBlock support : supports) {
            class_243 hitVec = new class_243(((double) support.position.method_10263()) + 0.5d + (((double) support.facing.method_62675().method_10263()) * 0.5d), ((double) support.position.method_10264()) + 0.5d + (((double) support.facing.method_62675().method_10264()) * 0.5d), ((double) support.position.method_10260()) + 0.5d + (((double) support.facing.method_62675().method_10260()) * 0.5d));
            if (squaredDistanceFromEyes(hitVec) <= this.range.getValue().floatValue() * this.range.getValue().floatValue()) {
                class_3965 wallCheck = mc.field_1687.method_17742(new class_3959(eyePos, hitVec, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, mc.field_1724));
                boolean blocked = (wallCheck == null || wallCheck.method_17783() != class_239.class_240.field_1332 || wallCheck.method_17777().equals(support.position)) ? false : true;
                if (!blocked) {
                    return new class_3965(hitVec, support.facing, support.position, false);
                }
            }
        }
        return null;
    }

    private ArrayList<SupportBlock> getSupportBlocks(class_2338 bp) {
        ArrayList<SupportBlock> list = new ArrayList<>();
        checkSupport(list, bp.method_10074(), class_2350.field_11036);
        checkSupport(list, bp.method_10084(), class_2350.field_11033);
        checkSupport(list, bp.method_10095(), class_2350.field_11035);
        checkSupport(list, bp.method_10072(), class_2350.field_11043);
        checkSupport(list, bp.method_10067(), class_2350.field_11034);
        checkSupport(list, bp.method_10078(), class_2350.field_11039);
        return list;
    }

    private void checkSupport(ArrayList<SupportBlock> list, class_2338 neighbor, class_2350 facing) {
        class_2680 state = mc.field_1687.method_8320(neighbor);
        if (state.method_26212(mc.field_1687, neighbor)) {
            list.add(new SupportBlock(neighbor, facing));
        }
    }

    private int findWebSlot() {
        for (int i = 0; i < 9; i++) {
            if (mc.field_1724.method_31548().method_5438(i).method_31574(class_1802.field_8786)) {
                return i;
            }
        }
        for (int i2 = 9; i2 < 36; i2++) {
            if (mc.field_1724.method_31548().method_5438(i2).method_31574(class_1802.field_8786)) {
                return i2;
            }
        }
        return -1;
    }

    private boolean shouldStop() {
        if (this.stopOnAura.getValue().booleanValue()) {
            AuraModule aura = AuraModule.getInstance();
            if (aura.isEnabled() && aura.target != null) {
                return true;
            }
            return false;
        }
        return false;
    }

    private float squaredDistanceFromEyes(class_243 vec) {
        double dx = vec.field_1352 - mc.field_1724.method_23317();
        double dy = vec.field_1351 - (mc.field_1724.method_23318() + ((double) mc.field_1724.method_18381(mc.field_1724.method_18376())));
        double dz = vec.field_1350 - mc.field_1724.method_23321();
        return (float) ((dx * dx) + (dy * dy) + (dz * dz));
    }

    private float[] calculateAngle(class_243 to) {
        class_243 from = mc.field_1724.method_33571();
        double diffX = to.field_1352 - from.field_1352;
        double diffY = (to.field_1351 - from.field_1351) * (-1.0d);
        double diffZ = to.field_1350 - from.field_1350;
        double dist = Math.sqrt((diffX * diffX) + (diffZ * diffZ));
        float yaw = (float) class_3532.method_15338(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0d);
        float pitch = (float) class_3532.method_15350(class_3532.method_15338(Math.toDegrees(Math.atan2(diffY, dist))), -90.0d, 90.0d);
        return new float[]{yaw, pitch};
    }

    @Override // sweetie.leonware.api.module.Module
    public String getDisplayInfo() {
        return this.placeMode.getValue();
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/AutoWebModule$SupportBlock.class */
    private static final class SupportBlock extends Record {
        private final class_2338 position;
        private final class_2350 facing;

        private SupportBlock(class_2338 position, class_2350 facing) {
            this.position = position;
            this.facing = facing;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, SupportBlock.class), SupportBlock.class, "position;facing", "FIELD:Lsweetie/leonware/client/features/modules/combat/AutoWebModule$SupportBlock;->position:Lnet/minecraft/class_2338;", "FIELD:Lsweetie/leonware/client/features/modules/combat/AutoWebModule$SupportBlock;->facing:Lnet/minecraft/class_2350;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, SupportBlock.class), SupportBlock.class, "position;facing", "FIELD:Lsweetie/leonware/client/features/modules/combat/AutoWebModule$SupportBlock;->position:Lnet/minecraft/class_2338;", "FIELD:Lsweetie/leonware/client/features/modules/combat/AutoWebModule$SupportBlock;->facing:Lnet/minecraft/class_2350;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, SupportBlock.class, Object.class), SupportBlock.class, "position;facing", "FIELD:Lsweetie/leonware/client/features/modules/combat/AutoWebModule$SupportBlock;->position:Lnet/minecraft/class_2338;", "FIELD:Lsweetie/leonware/client/features/modules/combat/AutoWebModule$SupportBlock;->facing:Lnet/minecraft/class_2350;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_2338 position() {
            return this.position;
        }

        public class_2350 facing() {
            return this.facing;
        }
    }
}
