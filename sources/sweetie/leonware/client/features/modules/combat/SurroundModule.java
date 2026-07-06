package sweetie.leonware.client.features.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_2828;
import net.minecraft.class_3532;
import net.minecraft.class_3965;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.player.InventoryUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.display.BoxRender;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.rotation.rotations.SmoothRotation;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.api.utils.world.HoleUtility;
import sweetie.leonware.client.features.modules.movement.MoveFixModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/SurroundModule.class */
@ModuleRegister(name = "Surround", category = Category.COMBAT)
public class SurroundModule extends Module {
    public static SurroundModule instance = new SurroundModule();
    private int tickDelay;
    private double lastY;
    private final SliderSetting blocksPerTick = new SliderSetting("Blocks/Tick").value(Float.valueOf(4.0f)).range(1.0f, 12.0f).step(1.0f);
    private final SliderSetting placeDelay = new SliderSetting("Delay").value(Float.valueOf(1.0f)).range(0.0f, 10.0f).step(1.0f);
    private final ModeSetting centerMode = new ModeSetting("Center").value("Motion").values("Teleport", "Motion", "Disabled");
    private final BooleanSetting onYChange = new BooleanSetting("Disable on Jump").value((Boolean) true);
    private final BooleanSetting rotate = new BooleanSetting("Rotate").value((Boolean) true);
    private final BooleanSetting render = new BooleanSetting("Render").value((Boolean) true);
    private int startSlot = -1;
    private final List<class_2338> placingPos = new ArrayList();
    private final List<class_2338> completedPos = new ArrayList();

    @Generated
    public static SurroundModule getInstance() {
        return instance;
    }

    public SurroundModule() {
        addSettings(this.blocksPerTick, this.placeDelay, this.centerMode, this.onYChange, this.rotate, this.render);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        if (mc.field_1724 == null) {
            return;
        }
        this.tickDelay = 0;
        this.lastY = mc.field_1724.method_23318();
        this.startSlot = mc.field_1724.method_31548().field_7545;
        int obsidian = findObsidian();
        if (obsidian != -1 && obsidian != this.startSlot) {
            InventoryUtil.swapToSlot(obsidian, true);
        }
        if (this.centerMode.is("Teleport")) {
            double x = ((double) class_3532.method_15357(mc.field_1724.method_23317())) + 0.5d;
            double z = ((double) class_3532.method_15357(mc.field_1724.method_23321())) + 0.5d;
            mc.field_1724.method_30634(x, mc.field_1724.method_23318(), z);
            mc.field_1724.field_3944.method_52787(new class_2828.class_2829(x, mc.field_1724.method_23318(), z, mc.field_1724.method_24828(), mc.field_1724.field_5976));
        }
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        if (mc.field_1724 != null && this.startSlot != -1 && this.startSlot != mc.field_1724.method_31548().field_7545) {
            InventoryUtil.swapToSlot(this.startSlot, true);
        }
        this.startSlot = -1;
        this.placingPos.clear();
        this.completedPos.clear();
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            if (this.onYChange.getValue().booleanValue() && mc.field_1724.method_23318() != this.lastY) {
                toggle();
                return;
            }
            this.lastY = mc.field_1724.method_23318();
            if (this.centerMode.is("Motion")) {
                doMotionCenter();
            }
            List<class_2338> allTargets = HoleUtility.getSurroundPoses(mc.field_1724.method_19538());
            this.placingPos.clear();
            this.completedPos.clear();
            for (class_2338 pos : allTargets) {
                class_2680 state = mc.field_1687.method_8320(pos);
                if (!state.method_45474()) {
                    if (state.method_26204() == class_2246.field_10540 || state.method_26204() == class_2246.field_22423 || state.method_26204() == class_2246.field_9987) {
                        this.completedPos.add(pos);
                    }
                } else {
                    this.placingPos.add(pos);
                }
            }
            if (this.tickDelay > 0) {
                this.tickDelay--;
                return;
            }
            int slot = findObsidian();
            if (slot == -1) {
                return;
            }
            int placed = 0;
            for (class_2338 pos2 : this.placingPos) {
                if (placed >= this.blocksPerTick.getValue().intValue()) {
                    break;
                } else if (mc.field_1687.method_8320(pos2).method_45474() && placeBlockManual(pos2)) {
                    placed++;
                }
            }
            this.tickDelay = this.placeDelay.getValue().intValue();
        }));
        EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener(event2 -> {
            if (!this.render.getValue().booleanValue() || mc.field_1724 == null) {
                return;
            }
            RenderUtil.BOX.setup3DRender(event2.matrixStack());
            Color placingColor = new Color(255, 0, 0, 100);
            for (class_2338 pos : this.placingPos) {
                drawCenteredBox(pos, placingColor);
            }
            Color uiColor = UIColors.positiveColor(100);
            for (class_2338 pos2 : this.completedPos) {
                drawCenteredBox(pos2, uiColor);
            }
        }));
        addEvents(updateEvent, renderEvent);
    }

    private void drawCenteredBox(class_2338 pos, Color color) {
        float x = pos.method_10263();
        float y = pos.method_10264();
        float z = pos.method_10260();
        RenderUtil.BOX.drawBox(x, y, z, x + 29.0f, y + 29.0f, z + 29.0f, 1.5f, color, BoxRender.Render.FILL, 0.0f);
        RenderUtil.BOX.drawBox(x, y, z, x + 29.0f, y + 29.0f, z + 29.0f, 2.0f, color.darker(), BoxRender.Render.OUTLINE, 0.0f);
    }

    private boolean placeBlockManual(class_2338 pos) {
        for (class_2350 side : class_2350.values()) {
            class_2338 neighbor = pos.method_10093(side);
            if (!mc.field_1687.method_8320(neighbor).method_45474()) {
                class_243 hitVec = class_243.method_24953(neighbor).method_1019(class_243.method_24954(side.method_10153().method_62675()).method_1021(0.5d));
                if (this.rotate.getValue().booleanValue()) {
                    Rotation r = RotationUtil.rotationAt(hitVec);
                    RotationManager.getInstance().addRotation(new Rotation.VecRotation(r, hitVec), null, new RotationStrategy(new SmoothRotation(), MoveFixModule.enabled(), true), TaskPriority.CRITICAL, this);
                }
                mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, new class_3965(hitVec, side.method_10153(), neighbor, false));
                mc.field_1724.method_6104(class_1268.field_5808);
                return true;
            }
        }
        return false;
    }

    private void doMotionCenter() {
        double targetX = ((double) class_3532.method_15357(mc.field_1724.method_23317())) + 0.5d;
        double targetZ = ((double) class_3532.method_15357(mc.field_1724.method_23321())) + 0.5d;
        double dx = targetX - mc.field_1724.method_23317();
        double dz = targetZ - mc.field_1724.method_23321();
        if (Math.abs(dx) > 0.1d || Math.abs(dz) > 0.1d) {
            mc.field_1724.method_18800(dx * 0.45d, mc.field_1724.method_18798().field_1351, dz * 0.45d);
        }
    }

    private int findObsidian() {
        for (int i = 0; i < 9; i++) {
            if (mc.field_1724.method_31548().method_5438(i).method_31574(class_1802.field_8281)) {
                return i;
            }
        }
        return -1;
    }
}
