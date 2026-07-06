// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_1802;
import net.minecraft.class_3965;
import net.minecraft.class_1268;
import net.minecraft.class_1309;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.client.features.modules.movement.MoveFixModule;
import sweetie.leonware.api.utils.rotation.rotations.SmoothRotation;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_2350;
import sweetie.leonware.api.utils.render.display.BoxRender;
import net.minecraft.class_2680;
import java.util.Iterator;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.utils.color.UIColors;
import java.awt.Color;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_2246;
import sweetie.leonware.api.utils.world.HoleUtility;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.player.InventoryUtil;
import sweetie.leonware.api.module.setting.Setting;
import java.util.ArrayList;
import net.minecraft.class_2338;
import java.util.List;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Surround", category = Category.COMBAT)
public class SurroundModule extends Module
{
    public static SurroundModule instance;
    private final SliderSetting blocksPerTick;
    private final SliderSetting placeDelay;
    private final ModeSetting centerMode;
    private final BooleanSetting onYChange;
    private final BooleanSetting rotate;
    private final BooleanSetting render;
    private int tickDelay;
    private double lastY;
    private int startSlot;
    private final List<class_2338> placingPos;
    private final List<class_2338> completedPos;
    
    public SurroundModule() {
        this.blocksPerTick = new SliderSetting("Blocks/Tick").value(4.0f).range(1.0f, 12.0f).step(1.0f);
        this.placeDelay = new SliderSetting("Delay").value(1.0f).range(0.0f, 10.0f).step(1.0f);
        this.centerMode = new ModeSetting("Center").value("Motion").values("Teleport", "Motion", "Disabled");
        this.onYChange = new BooleanSetting("Disable on Jump").value(true);
        this.rotate = new BooleanSetting("Rotate").value(true);
        this.render = new BooleanSetting("Render").value(true);
        this.startSlot = -1;
        this.placingPos = new ArrayList<class_2338>();
        this.completedPos = new ArrayList<class_2338>();
        this.addSettings(this.blocksPerTick, this.placeDelay, this.centerMode, this.onYChange, this.rotate, this.render);
    }
    
    @Override
    public void onEnable() {
        if (SurroundModule.mc.field_1724 == null) {
            return;
        }
        this.tickDelay = 0;
        this.lastY = SurroundModule.mc.field_1724.method_23318();
        this.startSlot = SurroundModule.mc.field_1724.method_31548().field_7545;
        final int obsidian = this.findObsidian();
        if (obsidian != -1 && obsidian != this.startSlot) {
            InventoryUtil.swapToSlot(obsidian, true);
        }
        if (this.centerMode.is("Teleport")) {
            final double x = class_3532.method_15357(SurroundModule.mc.field_1724.method_23317()) + 0.5;
            final double z = class_3532.method_15357(SurroundModule.mc.field_1724.method_23321()) + 0.5;
            SurroundModule.mc.field_1724.method_30634(x, SurroundModule.mc.field_1724.method_23318(), z);
            SurroundModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(x, SurroundModule.mc.field_1724.method_23318(), z, SurroundModule.mc.field_1724.method_24828(), SurroundModule.mc.field_1724.field_5976));
        }
    }
    
    @Override
    public void onDisable() {
        if (SurroundModule.mc.field_1724 != null && this.startSlot != -1 && this.startSlot != SurroundModule.mc.field_1724.method_31548().field_7545) {
            InventoryUtil.swapToSlot(this.startSlot, true);
        }
        this.startSlot = -1;
        this.placingPos.clear();
        this.completedPos.clear();
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (SurroundModule.mc.field_1724 == null || SurroundModule.mc.field_1687 == null) {
                return;
            }
            else if (this.onYChange.getValue() && SurroundModule.mc.field_1724.method_23318() != this.lastY) {
                this.toggle();
                return;
            }
            else {
                this.lastY = SurroundModule.mc.field_1724.method_23318();
                if (this.centerMode.is("Motion")) {
                    this.doMotionCenter();
                }
                final List<class_2338> allTargets = HoleUtility.getSurroundPoses(SurroundModule.mc.field_1724.method_19538());
                this.placingPos.clear();
                this.completedPos.clear();
                allTargets.iterator();
                final Iterator iterator;
                while (iterator.hasNext()) {
                    final class_2338 pos = iterator.next();
                    final class_2680 state = SurroundModule.mc.field_1687.method_8320(pos);
                    if (!state.method_45474()) {
                        if (state.method_26204() == class_2246.field_10540 || state.method_26204() == class_2246.field_22423 || state.method_26204() == class_2246.field_9987) {
                            this.completedPos.add(pos);
                        }
                        else {
                            continue;
                        }
                    }
                    else {
                        this.placingPos.add(pos);
                    }
                }
                if (this.tickDelay > 0) {
                    --this.tickDelay;
                    return;
                }
                else {
                    final int slot = this.findObsidian();
                    if (slot == -1) {
                        return;
                    }
                    else {
                        int placed = 0;
                        this.placingPos.iterator();
                        final Iterator iterator2;
                        while (iterator2.hasNext()) {
                            final class_2338 pos2 = iterator2.next();
                            if (placed >= this.blocksPerTick.getValue().intValue()) {
                                break;
                            }
                            else if (!SurroundModule.mc.field_1687.method_8320(pos2).method_45474()) {
                                continue;
                            }
                            else if (this.placeBlockManual(pos2)) {
                                ++placed;
                            }
                            else {
                                continue;
                            }
                        }
                        this.tickDelay = this.placeDelay.getValue().intValue();
                        return;
                    }
                }
            }
        }));
        final EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            if (!this.render.getValue() || SurroundModule.mc.field_1724 == null) {
                return;
            }
            else {
                RenderUtil.BOX.setup3DRender(event.matrixStack());
                final Color placingColor = new Color(255, 0, 0, 100);
                this.placingPos.iterator();
                final Iterator iterator3;
                while (iterator3.hasNext()) {
                    final class_2338 pos3 = iterator3.next();
                    this.drawCenteredBox(pos3, placingColor);
                }
                final Color uiColor = UIColors.positiveColor(100);
                this.completedPos.iterator();
                final Iterator iterator4;
                while (iterator4.hasNext()) {
                    final class_2338 pos4 = iterator4.next();
                    this.drawCenteredBox(pos4, uiColor);
                }
                return;
            }
        }));
        this.addEvents(updateEvent, renderEvent);
    }
    
    private void drawCenteredBox(final class_2338 pos, final Color color) {
        final float x = (float)pos.method_10263();
        final float y = (float)pos.method_10264();
        final float z = (float)pos.method_10260();
        RenderUtil.BOX.drawBox(x, y, z, x + 29.0f, y + 29.0f, z + 29.0f, 1.5f, color, BoxRender.Render.FILL, 0.0f);
        RenderUtil.BOX.drawBox(x, y, z, x + 29.0f, y + 29.0f, z + 29.0f, 2.0f, color.darker(), BoxRender.Render.OUTLINE, 0.0f);
    }
    
    private boolean placeBlockManual(final class_2338 pos) {
        for (final class_2350 side : class_2350.values()) {
            final class_2338 neighbor = pos.method_10093(side);
            if (!SurroundModule.mc.field_1687.method_8320(neighbor).method_45474()) {
                final class_243 hitVec = class_243.method_24953((class_2382)neighbor).method_1019(class_243.method_24954(side.method_10153().method_62675()).method_1021(0.5));
                if (this.rotate.getValue()) {
                    final Rotation r = RotationUtil.rotationAt(hitVec);
                    RotationManager.getInstance().addRotation(new Rotation.VecRotation(r, hitVec), null, new RotationStrategy(new SmoothRotation(), MoveFixModule.enabled(), true), TaskPriority.CRITICAL, this);
                }
                SurroundModule.mc.field_1761.method_2896(SurroundModule.mc.field_1724, class_1268.field_5808, new class_3965(hitVec, side.method_10153(), neighbor, false));
                SurroundModule.mc.field_1724.method_6104(class_1268.field_5808);
                return true;
            }
        }
        return false;
    }
    
    private void doMotionCenter() {
        final double targetX = class_3532.method_15357(SurroundModule.mc.field_1724.method_23317()) + 0.5;
        final double targetZ = class_3532.method_15357(SurroundModule.mc.field_1724.method_23321()) + 0.5;
        final double dx = targetX - SurroundModule.mc.field_1724.method_23317();
        final double dz = targetZ - SurroundModule.mc.field_1724.method_23321();
        if (Math.abs(dx) > 0.1 || Math.abs(dz) > 0.1) {
            SurroundModule.mc.field_1724.method_18800(dx * 0.45, SurroundModule.mc.field_1724.method_18798().field_1351, dz * 0.45);
        }
    }
    
    private int findObsidian() {
        for (int i = 0; i < 9; ++i) {
            if (SurroundModule.mc.field_1724.method_31548().method_5438(i).method_31574(class_1802.field_8281)) {
                return i;
            }
        }
        return -1;
    }
    
    @Generated
    public static SurroundModule getInstance() {
        return SurroundModule.instance;
    }
    
    static {
        SurroundModule.instance = new SurroundModule();
    }
}
