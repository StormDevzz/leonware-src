package sweetie.leonware.client.features.modules.player;

import java.util.Arrays;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1747;
import net.minecraft.class_1799;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import net.minecraft.class_3532;
import net.minecraft.class_3965;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.MovementInputEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.other.NetworkUtil;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.rotation.rotations.ScaffoldLegitRotation;
import sweetie.leonware.api.utils.task.TaskPriority;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/ScaffoldModule.class */
@ModuleRegister(name = "Scaffold", category = Category.PLAYER)
public class ScaffoldModule extends Module {
    private static final ScaffoldModule instance = new ScaffoldModule();
    private static final List<class_2248> BLACKLIST = Arrays.asList(class_2246.field_10034, class_2246.field_10443, class_2246.field_10380, class_2246.field_10102, class_2246.field_10534, class_2246.field_10255, class_2246.field_10029, class_2246.field_10343, class_2246.field_9980, class_2246.field_10181, class_2246.field_16333, class_2246.field_16334, class_2246.field_10158, class_2246.field_10484, class_2246.field_10592, class_2246.field_10332, class_2246.field_10026, class_2246.field_10397, class_2246.field_10470, class_2246.field_22130, class_2246.field_22131, class_2246.field_10081, class_2246.field_10535, class_2246.field_10105, class_2246.field_10414, class_2246.field_10327, class_2246.field_10394, class_2246.field_10575, class_2246.field_10217, class_2246.field_10276, class_2246.field_10385, class_2246.field_10160, class_2246.field_10336, class_2246.field_10099, class_2246.field_10363, class_2246.field_10057, class_2246.field_10494, class_2246.field_10214, class_2246.field_10479, class_2246.field_10428, class_2246.field_10477, class_2246.field_21211);
    private final ModeSetting rotationMode = new ModeSetting("Rotation Mode").value("Silent").values("None", "Packet", "Silent");
    private final SliderSetting placeDelay = new SliderSetting("Place Delay").value(Float.valueOf(50.0f)).range(0.0f, 200.0f).step(5.0f);
    private final BooleanSetting keepY = new BooleanSetting("Keep Y").value((Boolean) true);
    private final BooleanSetting safeWalk = new BooleanSetting("Safe Walk").value((Boolean) true);
    private final BooleanSetting placeInAir = new BooleanSetting("Place in Air").value((Boolean) false).setVisible(() -> {
        return Boolean.valueOf(this.rotationMode.is("None"));
    });
    private final TimerUtil placeTimer = new TimerUtil();
    private int originalSlot = -1;
    private int tickDelay = 0;
    private float savedY = 0.0f;

    @Generated
    public static ScaffoldModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v13, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    public ScaffoldModule() {
        addSettings(this.rotationMode, this.placeDelay, this.keepY, this.safeWalk, this.placeInAir);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        super.onEnable();
        if (mc.field_1724 == null || mc.field_1687 == null) {
            setEnabled(false);
            return;
        }
        this.originalSlot = mc.field_1724.method_31548().field_7545;
        this.savedY = (float) Math.floor(mc.field_1724.method_23318() - 1.0d);
        this.tickDelay = 0;
        this.placeTimer.reset();
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        super.onDisable();
        if (mc.field_1724 != null && this.originalSlot != -1) {
            mc.field_1724.method_31548().field_7545 = this.originalSlot;
        }
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            Rotation safeRotation;
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            if (this.tickDelay > 0) {
                this.tickDelay--;
                return;
            }
            if (mc.field_1724.method_24828() && this.keepY.getValue().booleanValue()) {
                this.savedY = (float) Math.floor(mc.field_1724.method_23318() - 1.0d);
            }
            class_2338 below = getPredictedPos();
            if (mc.field_1687.method_8320(below).method_26215()) {
                int slot = findBlockSlot();
                if (slot == -1) {
                    slot = findInventoryBlock();
                }
                if (slot == -1) {
                    return;
                }
                if (mc.field_1724.method_31548().field_7545 != slot) {
                    mc.field_1724.method_31548().field_7545 = slot;
                }
                if (this.placeTimer.finished(this.placeDelay.getValue().longValue())) {
                    class_3965 hit = findHit(below);
                    if (hit == null && this.rotationMode.is("None") && this.placeInAir.getValue().booleanValue()) {
                        hit = createAirPlacement(below);
                    }
                    if (hit == null) {
                        return;
                    }
                    Rotation rawRotation = RotationUtil.rotationAt(hit.method_17784());
                    float normalizedYaw = class_3532.method_15393(rawRotation.getYaw());
                    float normalizedPitch = class_3532.method_15363(rawRotation.getPitch(), -90.0f, 90.0f);
                    safeRotation = new Rotation(normalizedYaw, normalizedPitch);
                    switch (this.rotationMode.getValue()) {
                        case "Silent":
                            applySilentRotation(safeRotation);
                            break;
                        case "Packet":
                            applyPacketRotation(safeRotation);
                            break;
                    }
                    class_1269 result = mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, hit);
                    if (result.method_23665()) {
                        mc.field_1724.method_6104(class_1268.field_5808);
                        this.placeTimer.reset();
                        this.tickDelay = 1;
                    }
                }
            }
        }));
        EventListener moveInputEvent = MovementInputEvent.getInstance().subscribe(new Listener(event2 -> {
            if (mc.field_1724 == null || mc.field_1687 == null || !this.safeWalk.getValue().booleanValue()) {
                return;
            }
            class_2338 pos = mc.field_1724.method_24515().method_10074();
            boolean atEdge = mc.field_1687.method_8320(pos).method_27852(class_2246.field_10124) && mc.field_1724.method_24828();
            if (atEdge) {
                event2.setSneak(true);
            }
        }));
        addEvents(updateEvent, moveInputEvent);
    }

    private void applySilentRotation(Rotation rotation) {
        RotationStrategy strategy = new RotationStrategy(new ScaffoldLegitRotation(), true).ticksUntilReset(3);
        RotationManager.getInstance().addRotation(rotation, strategy, TaskPriority.NORMAL, this);
    }

    private void applyPacketRotation(Rotation rotation) {
        class_243 playerPos = mc.field_1724.method_19538();
        class_2828.class_2830 packet = new class_2828.class_2830(playerPos.field_1352, playerPos.field_1351, playerPos.field_1350, rotation.getYaw(), rotation.getPitch(), mc.field_1724.method_24828(), mc.field_1724.field_5976);
        NetworkUtil.sendPacket((class_2596<?>) packet);
    }

    private class_2338 getPredictedPos() {
        int targetY;
        class_243 vel = mc.field_1724.method_18798();
        int dx = (int) Math.round(vel.field_1352);
        int dz = (int) Math.round(vel.field_1350);
        if (this.keepY.getValue().booleanValue()) {
            targetY = (int) this.savedY;
        } else {
            targetY = class_3532.method_15357(mc.field_1724.method_23318() - 1.0d);
        }
        class_2338 playerPos = mc.field_1724.method_24515();
        return new class_2338(playerPos.method_10263() + dx, targetY, playerPos.method_10260() + dz);
    }

    private class_3965 createAirPlacement(class_2338 target) {
        class_243 hitVec = class_243.method_24953(target);
        return new class_3965(hitVec, class_2350.field_11036, target, false);
    }

    private int findInventoryBlock() {
        for (int i = 0; i < 27; i++) {
            class_1799 stack = mc.field_1724.method_31548().method_5438(i + 9);
            if (stack.method_7947() > 0) {
                class_1747 class_1747VarMethod_7909 = stack.method_7909();
                if (class_1747VarMethod_7909 instanceof class_1747) {
                    class_1747 blockItem = class_1747VarMethod_7909;
                    if (!BLACKLIST.contains(blockItem.method_7711())) {
                        return i + 9;
                    }
                } else {
                    continue;
                }
            }
        }
        return -1;
    }

    private int findBlockSlot() {
        for (int i = 0; i < 9; i++) {
            class_1799 stack = mc.field_1724.method_31548().method_5438(i);
            if (stack.method_7947() > 0) {
                class_1747 class_1747VarMethod_7909 = stack.method_7909();
                if (class_1747VarMethod_7909 instanceof class_1747) {
                    class_1747 blockItem = class_1747VarMethod_7909;
                    if (!BLACKLIST.contains(blockItem.method_7711())) {
                        return i;
                    }
                } else {
                    continue;
                }
            }
        }
        return -1;
    }

    private class_3965 findHit(class_2338 target) {
        class_2350[] faces = {class_2350.field_11033, class_2350.field_11043, class_2350.field_11035, class_2350.field_11039, class_2350.field_11034};
        for (class_2350 face : faces) {
            class_2338 neighbour = target.method_10093(face);
            if (!mc.field_1687.method_8320(neighbour).method_26215()) {
                class_243 hitVec = class_243.method_24953(neighbour).method_1019(class_243.method_24954(face.method_62675()).method_1021(0.5d));
                return new class_3965(hitVec, face.method_10153(), neighbour, false);
            }
        }
        return null;
    }

    public boolean isSafeWalk() {
        return isEnabled() && this.safeWalk.getValue().booleanValue();
    }
}
