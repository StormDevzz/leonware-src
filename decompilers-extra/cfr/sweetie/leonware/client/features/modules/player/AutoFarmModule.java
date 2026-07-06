/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1268
 *  net.minecraft.class_1661
 *  net.minecraft.class_1792
 *  net.minecraft.class_1794
 *  net.minecraft.class_1802
 *  net.minecraft.class_1922
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2266
 *  net.minecraft.class_2282
 *  net.minecraft.class_2302
 *  net.minecraft.class_2338
 *  net.minecraft.class_2344
 *  net.minecraft.class_2350
 *  net.minecraft.class_2374
 *  net.minecraft.class_238
 *  net.minecraft.class_2382
 *  net.minecraft.class_2420
 *  net.minecraft.class_2421
 *  net.minecraft.class_243
 *  net.minecraft.class_2445
 *  net.minecraft.class_2473
 *  net.minecraft.class_2492
 *  net.minecraft.class_2513
 *  net.minecraft.class_2523
 *  net.minecraft.class_2680
 *  net.minecraft.class_2769
 *  net.minecraft.class_2944
 *  net.minecraft.class_3486
 *  net.minecraft.class_3532
 *  net.minecraft.class_3746
 *  net.minecraft.class_3830
 *  net.minecraft.class_3965
 *  net.minecraft.class_5800
 */
package sweetie.leonware.client.features.modules.player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1661;
import net.minecraft.class_1792;
import net.minecraft.class_1794;
import net.minecraft.class_1802;
import net.minecraft.class_1922;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2266;
import net.minecraft.class_2282;
import net.minecraft.class_2302;
import net.minecraft.class_2338;
import net.minecraft.class_2344;
import net.minecraft.class_2350;
import net.minecraft.class_2374;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_2420;
import net.minecraft.class_2421;
import net.minecraft.class_243;
import net.minecraft.class_2445;
import net.minecraft.class_2473;
import net.minecraft.class_2492;
import net.minecraft.class_2513;
import net.minecraft.class_2523;
import net.minecraft.class_2680;
import net.minecraft.class_2769;
import net.minecraft.class_2944;
import net.minecraft.class_3486;
import net.minecraft.class_3532;
import net.minecraft.class_3746;
import net.minecraft.class_3830;
import net.minecraft.class_3965;
import net.minecraft.class_5800;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.client.features.modules.combat.AuraModule;

@ModuleRegister(name="Auto Farm", category=Category.PLAYER)
public class AutoFarmModule
extends Module {
    private static final AutoFarmModule instance = new AutoFarmModule();
    private final Map<class_2338, Integer> mossMap = new HashMap<class_2338, Integer>();
    private final SliderSetting range = new SliderSetting("\u0414\u0430\u043b\u044c\u043d\u043e\u0441\u0442\u044c").value(Float.valueOf(4.5f)).range(1.0f, 6.0f).step(0.1f);
    private final ModeSetting rotation = new ModeSetting("\u0412\u0440\u0430\u0449\u0435\u043d\u0438\u0435").values("\u0411\u0435\u0437", "\u041e\u0431\u044b\u0447\u043d\u0430\u044f", "\u0421\u0430\u0439\u043b\u0435\u043d\u0442").value("\u0411\u0435\u0437");
    public final BooleanSetting walk = new BooleanSetting("\u0425\u043e\u0434\u0438\u0442\u044c").value(false);
    private final SliderSetting walkDistance = new SliderSetting("\u0414\u0438\u0441\u0442\u0430\u043d\u0446\u0438\u044f").value(Float.valueOf(5.0f)).range(1.0f, 10.0f).step(1.0f).setVisible(this.walk::getValue);
    private final BooleanSetting pitchControl = new BooleanSetting("Pitch Lock").value(false).setVisible(this.walk::getValue);
    private final SliderSetting pitchValue = new SliderSetting("Pitch Value").value(Float.valueOf(0.0f)).range(-90.0f, 90.0f).step(1.0f).setVisible(() -> (Boolean)this.walk.getValue() != false && (Boolean)this.pitchControl.getValue() != false);
    private final BooleanSetting yawControl = new BooleanSetting("Yaw Lock").value(false).setVisible(this.walk::getValue);
    private final SliderSetting yawValue = new SliderSetting("Yaw Value").value(Float.valueOf(0.0f)).range(-180.0f, 180.0f).step(1.0f).setVisible(() -> (Boolean)this.walk.getValue() != false && (Boolean)this.yawControl.getValue() != false);
    private final BooleanSetting stopIfTarget = new BooleanSetting("\u041d\u0435 \u0445\u043e\u0434\u0438\u0442\u044c \u0435\u0441\u043b\u0438 \u0442\u0430\u0440\u0433\u0435\u0442").value(true).setVisible(this.walk::getValue);
    private int walkState = 0;
    private int walkWaitTicks = 0;
    private class_243 startPos = null;
    private final MultiBooleanSetting \u0432\u0441\u043f\u0430\u0448\u043a\u0430\u041d\u0430\u0441\u0442\u0440 = new MultiBooleanSetting("\u0412\u0441\u043f\u0430\u0448\u043a\u0430").value(new BooleanSetting("\u0412\u043a\u043b\u044e\u0447\u0435\u043d\u043e").value(true), new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u043f\u043e\u043b\u0438\u0442\u043e\u0435").value(false));
    private final MultiBooleanSetting \u0441\u0431\u043e\u0440\u041d\u0430\u0441\u0442\u0440 = new MultiBooleanSetting("\u0421\u0431\u043e\u0440").value(new BooleanSetting("\u0412\u043a\u043b\u044e\u0447\u0435\u043d\u043e").value(true), new BooleanSetting("\u0423\u0440\u043e\u0436\u0430\u0439").value(true), new BooleanSetting("\u0411\u0430\u0445\u0447\u0435\u0432\u044b\u0435").value(true), new BooleanSetting("\u0410\u0434\u0441\u043a\u0430\u044f \u043f\u0448\u0435\u043d\u0438\u0446\u0430").value(true), new BooleanSetting("\u041a\u0430\u043a\u0430\u043e").value(true), new BooleanSetting("\u042f\u0433\u043e\u0434\u044b").value(false), new BooleanSetting("\u0422\u0440\u043e\u0441\u0442\u043d\u0438\u043a").value(false), new BooleanSetting("\u041a\u0430\u043a\u0442\u0443\u0441").value(false));
    private final MultiBooleanSetting \u043f\u043e\u0441\u0430\u0434\u043a\u0430\u041d\u0430\u0441\u0442\u0440 = new MultiBooleanSetting("\u041f\u043e\u0441\u0430\u0434\u043a\u0430").value(new BooleanSetting("\u0412\u043a\u043b\u044e\u0447\u0435\u043d\u043e").value(true), new BooleanSetting("\u0423\u0440\u043e\u0436\u0430\u0439").value(true), new BooleanSetting("\u0411\u0430\u0445\u0447\u0435\u0432\u044b\u0435").value(true), new BooleanSetting("\u0410\u0434\u0441\u043a\u0430\u044f \u043f\u0448\u0435\u043d\u0438\u0446\u0430").value(true));
    private final MultiBooleanSetting \u043a\u043e\u0441\u0442\u044f\u043d\u0430\u044f\u041d\u0430\u0441\u0442\u0440 = new MultiBooleanSetting("\u041a\u043e\u0441\u0442\u043d\u0430\u044f \u043c\u0443\u043a\u0430").value(new BooleanSetting("\u0412\u043a\u043b\u044e\u0447\u0435\u043d\u043e").value(true), new BooleanSetting("\u0423\u0440\u043e\u0436\u0430\u0439").value(true), new BooleanSetting("\u0411\u0430\u0445\u0447\u0435\u0432\u044b\u0435").value(true), new BooleanSetting("\u041a\u0430\u043a\u0430\u043e").value(true), new BooleanSetting("\u042f\u0433\u043e\u0434\u044b").value(false), new BooleanSetting("\u0413\u0440\u0438\u0431\u044b").value(false), new BooleanSetting("\u0421\u0430\u0436\u0435\u043d\u0446\u044b").value(false), new BooleanSetting("\u041c\u043e\u0445").value(false));

    public AutoFarmModule() {
        this.addSettings(this.range, this.rotation, this.\u0432\u0441\u043f\u0430\u0448\u043a\u0430\u041d\u0430\u0441\u0442\u0440, this.\u0441\u0431\u043e\u0440\u041d\u0430\u0441\u0442\u0440, this.\u043f\u043e\u0441\u0430\u0434\u043a\u0430\u041d\u0430\u0441\u0442\u0440, this.\u043a\u043e\u0441\u0442\u044f\u043d\u0430\u044f\u041d\u0430\u0441\u0442\u0440, this.walk, this.walkDistance, this.pitchControl, this.pitchValue, this.yawControl, this.yawValue, this.stopIfTarget);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (((Boolean)this.walk.getValue()).booleanValue() && AutoFarmModule.mc.field_1724 != null) {
            this.walkState = 1;
            this.walkWaitTicks = 20;
            this.startPos = AutoFarmModule.mc.field_1724.method_19538();
        } else {
            this.walkState = 0;
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.mossMap.clear();
        if (this.walkState != 0 && AutoFarmModule.mc.field_1690 != null) {
            AutoFarmModule.mc.field_1690.field_1894.method_23481(false);
            AutoFarmModule.mc.field_1690.field_1881.method_23481(false);
            this.walkState = 0;
        }
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            class_2338 bestBlock;
            int slot;
            if (AutoFarmModule.mc.field_1724 == null || AutoFarmModule.mc.field_1687 == null) {
                return;
            }
            if (((Boolean)this.walk.getValue()).booleanValue()) {
                boolean hasTarget;
                boolean bl = hasTarget = AuraModule.getInstance().target != null;
                if (((Boolean)this.stopIfTarget.getValue()).booleanValue() && hasTarget) {
                    if (this.walkState > 0) {
                        AutoFarmModule.mc.field_1690.field_1894.method_23481(false);
                        AutoFarmModule.mc.field_1690.field_1881.method_23481(false);
                        this.walkState = 1;
                        this.walkWaitTicks = 20;
                    }
                } else {
                    if (((Boolean)this.yawControl.getValue()).booleanValue()) {
                        AutoFarmModule.mc.field_1724.method_36456(((Float)this.yawValue.getValue()).floatValue());
                    }
                    if (((Boolean)this.pitchControl.getValue()).booleanValue()) {
                        AutoFarmModule.mc.field_1724.method_36457(((Float)this.pitchValue.getValue()).floatValue());
                    }
                    if (this.walkState == 0) {
                        this.walkState = 1;
                        this.walkWaitTicks = 15;
                        this.startPos = AutoFarmModule.mc.field_1724.method_19538();
                    }
                    if (this.walkState > 0) {
                        AutoFarmModule.mc.field_1724.method_5728(false);
                    }
                    if (this.walkState == 1) {
                        if (this.walkWaitTicks > 0) {
                            --this.walkWaitTicks;
                        } else {
                            this.walkState = 2;
                            this.startPos = AutoFarmModule.mc.field_1724.method_19538();
                            AutoFarmModule.mc.field_1690.field_1894.method_23481(true);
                        }
                    } else if (this.walkState == 2) {
                        if (AutoFarmModule.mc.field_1724.method_19538().method_1022(this.startPos) >= (double)((Float)this.walkDistance.getValue()).floatValue()) {
                            AutoFarmModule.mc.field_1690.field_1894.method_23481(false);
                            this.walkState = 3;
                            AutoFarmModule.mc.field_1690.field_1881.method_23481(true);
                        }
                    } else if (this.walkState == 3 && AutoFarmModule.mc.field_1724.method_19538().method_1022(this.startPos) <= 0.5) {
                        AutoFarmModule.mc.field_1690.field_1881.method_23481(false);
                        this.walkState = 1;
                        this.walkWaitTicks = 20;
                    }
                }
            }
            this.mossMap.entrySet().removeIf(e -> e.setValue((Integer)e.getValue() - 1) == 0);
            float rangeValue = ((Float)this.range.getValue()).floatValue();
            int ceilRange = class_3532.method_15386((float)rangeValue);
            if (this.\u043a\u043e\u0441\u0442\u044f\u043d\u0430\u044f\u041d\u0430\u0441\u0442\u0440.isEnabled("\u0412\u043a\u043b\u044e\u0447\u0435\u043d\u043e") && this.\u043a\u043e\u0441\u0442\u044f\u043d\u0430\u044f\u041d\u0430\u0441\u0442\u0440.isEnabled("\u041c\u043e\u0445") && (slot = this.findSlot(class_1802.field_8324)) != -1 && (bestBlock = (class_2338)class_2338.method_25998((class_2338)class_2338.method_49638((class_2374)AutoFarmModule.mc.field_1724.method_33571()), (int)ceilRange, (int)ceilRange, (int)ceilRange).filter(b -> AutoFarmModule.mc.field_1724.method_33571().method_1022(class_243.method_24953((class_2382)b)) <= (double)rangeValue && !this.mossMap.containsKey(b)).map(b -> {
                int spots = this.getMossSpots((class_2338)b);
                return spots > 10 ? b.method_10062() : null;
            }).filter(b -> b != null).min((a, b) -> b.method_10265((class_2382)a)).orElse(null)) != null) {
                if (!AutoFarmModule.mc.field_1687.method_22347(bestBlock.method_10084())) {
                    AutoFarmModule.mc.field_1761.method_2902(bestBlock.method_10084(), class_2350.field_11036);
                }
                this.applyRotation(class_243.method_26410((class_2382)bestBlock, (double)1.0));
                this.selectSlot(slot);
                AutoFarmModule.mc.field_1761.method_2896(AutoFarmModule.mc.field_1724, class_1268.field_5808, new class_3965(class_243.method_26410((class_2382)bestBlock, (double)1.0), class_2350.field_11036, bestBlock, false));
                this.mossMap.put(bestBlock, 100);
                return;
            }
            for (class_2338 pos : class_2338.method_25996((class_2338)class_2338.method_49638((class_2374)AutoFarmModule.mc.field_1724.method_33571()), (int)ceilRange, (int)ceilRange, (int)ceilRange)) {
                int slot2;
                if (AutoFarmModule.mc.field_1724.method_33571().method_1022(class_243.method_24953((class_2382)pos)) > (double)rangeValue) continue;
                class_2680 state = AutoFarmModule.mc.field_1687.method_8320(pos);
                class_2248 block = state.method_26204();
                if (this.\u0432\u0441\u043f\u0430\u0448\u043a\u0430\u041d\u0430\u0441\u0442\u0440.isEnabled("\u0412\u043a\u043b\u044e\u0447\u0435\u043d\u043e") && this.canTill(block) && AutoFarmModule.mc.field_1687.method_22347(pos.method_10084()) && (!this.\u0432\u0441\u043f\u0430\u0448\u043a\u0430\u041d\u0430\u0441\u0442\u0440.isEnabled("\u0422\u043e\u043b\u044c\u043a\u043e \u043f\u043e\u043b\u0438\u0442\u043e\u0435") || this.isWatered(pos)) && (slot2 = this.findSlot((class_1792 item) -> item instanceof class_1794)) != -1) {
                    this.applyRotation(class_243.method_26410((class_2382)pos, (double)1.0));
                    this.selectSlot(slot2);
                    AutoFarmModule.mc.field_1761.method_2896(AutoFarmModule.mc.field_1724, class_1268.field_5808, new class_3965(class_243.method_26410((class_2382)pos, (double)1.0), class_2350.field_11036, pos, false));
                    return;
                }
                if (this.\u0441\u0431\u043e\u0440\u041d\u0430\u0441\u0442\u0440.isEnabled("\u0412\u043a\u043b\u044e\u0447\u0435\u043d\u043e") && (this.\u0441\u0431\u043e\u0440\u041d\u0430\u0441\u0442\u0440.isEnabled("\u0423\u0440\u043e\u0436\u0430\u0439") && block instanceof class_2302 && ((class_2302)block).method_9825(state) || this.\u0441\u0431\u043e\u0440\u041d\u0430\u0441\u0442\u0440.isEnabled("\u0411\u0430\u0445\u0447\u0435\u0432\u044b\u0435") && (block instanceof class_2445 || block == class_2246.field_46283) || this.\u0441\u0431\u043e\u0440\u041d\u0430\u0441\u0442\u0440.isEnabled("\u0410\u0434\u0441\u043a\u0430\u044f \u043f\u0448\u0435\u043d\u0438\u0446\u0430") && block instanceof class_2421 && (Integer)state.method_11654((class_2769)class_2421.field_11306) >= 3 || this.\u0441\u0431\u043e\u0440\u041d\u0430\u0441\u0442\u0440.isEnabled("\u041a\u0430\u043a\u0430\u043e") && block instanceof class_2282 && (Integer)state.method_11654((class_2769)class_2282.field_10779) >= 2 || this.\u0441\u0431\u043e\u0440\u041d\u0430\u0441\u0442\u0440.isEnabled("\u042f\u0433\u043e\u0434\u044b") && block instanceof class_3830 && (Integer)state.method_11654((class_2769)class_3830.field_17000) >= 3 || this.\u0441\u0431\u043e\u0440\u041d\u0430\u0441\u0442\u0440.isEnabled("\u0422\u0440\u043e\u0441\u0442\u043d\u0438\u043a") && this.shouldHarvestTallCrop(pos, block, class_2523.class) || this.\u0441\u0431\u043e\u0440\u041d\u0430\u0441\u0442\u0440.isEnabled("\u041a\u0430\u043a\u0442\u0443\u0441") && this.shouldHarvestTallCrop(pos, block, class_2266.class))) {
                    this.applyRotation(class_243.method_24953((class_2382)pos));
                    AutoFarmModule.mc.field_1761.method_2902(pos, class_2350.field_11036);
                    return;
                }
                if (this.\u043f\u043e\u0441\u0430\u0434\u043a\u0430\u041d\u0430\u0441\u0442\u0440.isEnabled("\u0412\u043a\u043b\u044e\u0447\u0435\u043d\u043e") && AutoFarmModule.mc.field_1687.method_8333(null, new class_238(pos.method_10084()), entity -> true).isEmpty()) {
                    if (block instanceof class_2344 && AutoFarmModule.mc.field_1687.method_22347(pos.method_10084()) && (slot2 = this.findSlot((class_1792 item) -> this.\u043f\u043e\u0441\u0430\u0434\u043a\u0430\u041d\u0430\u0441\u0442\u0440.isEnabled("\u0423\u0440\u043e\u0436\u0430\u0439") && (item == class_1802.field_8317 || item == class_1802.field_8179 || item == class_1802.field_8567 || item == class_1802.field_8309) || this.\u043f\u043e\u0441\u0430\u0434\u043a\u0430\u041d\u0430\u0441\u0442\u0440.isEnabled("\u0411\u0430\u0445\u0447\u0435\u0432\u044b\u0435") && (item == class_1802.field_46249 || item == class_1802.field_46250))) != -1) {
                        this.applyRotation(class_243.method_26410((class_2382)pos.method_10084(), (double)1.0));
                        this.selectSlot(slot2);
                        AutoFarmModule.mc.field_1761.method_2896(AutoFarmModule.mc.field_1724, class_1268.field_5808, new class_3965(class_243.method_26410((class_2382)pos.method_10084(), (double)1.0), class_2350.field_11033, pos.method_10084(), false));
                        return;
                    }
                    if (block instanceof class_2492 && AutoFarmModule.mc.field_1687.method_22347(pos.method_10084()) && this.\u043f\u043e\u0441\u0430\u0434\u043a\u0430\u041d\u0430\u0441\u0442\u0440.isEnabled("\u0410\u0434\u0441\u043a\u0430\u044f \u043f\u0448\u0435\u043d\u0438\u0446\u0430") && (slot2 = this.findSlot((class_1792 item) -> item == class_1802.field_8790)) != -1) {
                        this.applyRotation(class_243.method_26410((class_2382)pos.method_10084(), (double)1.0));
                        this.selectSlot(slot2);
                        AutoFarmModule.mc.field_1761.method_2896(AutoFarmModule.mc.field_1724, class_1268.field_5808, new class_3965(class_243.method_26410((class_2382)pos.method_10084(), (double)1.0), class_2350.field_11033, pos.method_10084(), false));
                        return;
                    }
                }
                if (!this.\u043a\u043e\u0441\u0442\u044f\u043d\u0430\u044f\u041d\u0430\u0441\u0442\u0440.isEnabled("\u0412\u043a\u043b\u044e\u0447\u0435\u043d\u043e") || (slot2 = this.findSlot((class_1792 item) -> item == class_1802.field_8324)) == -1 || !(this.\u043a\u043e\u0441\u0442\u044f\u043d\u0430\u044f\u041d\u0430\u0441\u0442\u0440.isEnabled("\u0423\u0440\u043e\u0436\u0430\u0439") && block instanceof class_2302 && !((class_2302)block).method_9825(state) || this.\u043a\u043e\u0441\u0442\u044f\u043d\u0430\u044f\u041d\u0430\u0441\u0442\u0440.isEnabled("\u0411\u0430\u0445\u0447\u0435\u0432\u044b\u0435") && block instanceof class_2513 && (Integer)state.method_11654((class_2769)class_2513.field_11584) < 7 || this.\u043a\u043e\u0441\u0442\u044f\u043d\u0430\u044f\u041d\u0430\u0441\u0442\u0440.isEnabled("\u041a\u0430\u043a\u0430\u043e") && block instanceof class_2282 && (Integer)state.method_11654((class_2769)class_2282.field_10779) < 2 || this.\u043a\u043e\u0441\u0442\u044f\u043d\u0430\u044f\u041d\u0430\u0441\u0442\u0440.isEnabled("\u042f\u0433\u043e\u0434\u044b") && block instanceof class_3830 && (Integer)state.method_11654((class_2769)class_3830.field_17000) < 3 || this.\u043a\u043e\u0441\u0442\u044f\u043d\u0430\u044f\u041d\u0430\u0441\u0442\u0440.isEnabled("\u0413\u0440\u0438\u0431\u044b") && block instanceof class_2420) && (!this.\u043a\u043e\u0441\u0442\u044f\u043d\u0430\u044f\u041d\u0430\u0441\u0442\u0440.isEnabled("\u0421\u0430\u0436\u0435\u043d\u0446\u044b") || !(block instanceof class_2473) && !(block instanceof class_5800) || !this.canPlaceSapling(pos))) continue;
                this.applyRotation(class_243.method_26410((class_2382)pos, (double)1.0));
                this.selectSlot(slot2);
                AutoFarmModule.mc.field_1761.method_2896(AutoFarmModule.mc.field_1724, class_1268.field_5808, new class_3965(class_243.method_26410((class_2382)pos, (double)1.0), class_2350.field_11036, pos, false));
                return;
            }
        }));
        this.addEvents(updateEvent);
    }

    private void applyRotation(class_243 target) {
        Rotation rot = RotationUtil.rotationAt(target);
        switch ((String)this.rotation.getValue()) {
            case "\u041e\u0431\u044b\u0447\u043d\u0430\u044f": {
                AutoFarmModule.mc.field_1724.method_36456(rot.getYaw());
                AutoFarmModule.mc.field_1724.method_36457(rot.getPitch());
                break;
            }
            case "\u0421\u0430\u0439\u043b\u0435\u043d\u0442": {
                RotationManager.getInstance().addRotation(rot, RotationStrategy.TARGET, TaskPriority.NORMAL, this);
            }
        }
    }

    private boolean shouldHarvestTallCrop(class_2338 pos, class_2248 posBlock, Class<? extends class_2248> blockClass) {
        return posBlock.getClass().equals(blockClass) && AutoFarmModule.mc.field_1687.method_8320(pos.method_10074()).method_26204().getClass().equals(blockClass) && !AutoFarmModule.mc.field_1687.method_8320(pos.method_10087(2)).method_26204().getClass().equals(blockClass);
    }

    private int getMossSpots(class_2338 pos) {
        if (AutoFarmModule.mc.field_1687.method_8320(pos).method_26204() != class_2246.field_28681 || AutoFarmModule.mc.field_1687.method_8320(pos.method_10084()).method_26214((class_1922)AutoFarmModule.mc.field_1687, pos) != 0.0f) {
            return 0;
        }
        return (int)class_2338.method_25998((class_2338)pos, (int)3, (int)4, (int)3).filter(b -> this.isMossGrowableOn(AutoFarmModule.mc.field_1687.method_8320(b).method_26204()) && AutoFarmModule.mc.field_1687.method_22347(b.method_10084())).count();
    }

    private boolean isMossGrowableOn(class_2248 block) {
        return block == class_2246.field_10340 || block == class_2246.field_10474 || block == class_2246.field_10115 || block == class_2246.field_10508 || block == class_2246.field_10566 || block == class_2246.field_10253 || block == class_2246.field_10402 || block == class_2246.field_10219 || block == class_2246.field_10520 || block == class_2246.field_28685;
    }

    private boolean canPlaceSapling(class_2338 pos) {
        return class_2338.method_17962((int)(pos.method_10263() - 1), (int)(pos.method_10264() + 1), (int)(pos.method_10260() - 1), (int)(pos.method_10263() + 1), (int)(pos.method_10264() + 5), (int)(pos.method_10260() + 1)).allMatch(b -> class_2944.method_27371((class_3746)AutoFarmModule.mc.field_1687, (class_2338)b));
    }

    private boolean canTill(class_2248 block) {
        return block == class_2246.field_10566 || block == class_2246.field_10219 || block == class_2246.field_10253 || block == class_2246.field_28685 || block == class_2246.field_10194;
    }

    private boolean isWatered(class_2338 pos) {
        return class_2338.method_17962((int)(pos.method_10263() - 4), (int)pos.method_10264(), (int)(pos.method_10260() - 4), (int)(pos.method_10263() + 4), (int)pos.method_10264(), (int)(pos.method_10260() + 4)).anyMatch(b -> AutoFarmModule.mc.field_1687.method_8316(b).method_15767(class_3486.field_15517));
    }

    private int findSlot(class_1792 item) {
        class_1661 inventory = AutoFarmModule.mc.field_1724.method_31548();
        for (int i = 0; i < 9; ++i) {
            if (inventory.method_5438(i).method_7909() != item) continue;
            return i;
        }
        return -1;
    }

    private int findSlot(Predicate<class_1792> predicate) {
        class_1661 inventory = AutoFarmModule.mc.field_1724.method_31548();
        for (int i = 0; i < 9; ++i) {
            if (!predicate.test(inventory.method_5438(i).method_7909())) continue;
            return i;
        }
        return -1;
    }

    private void selectSlot(int slot) {
        AutoFarmModule.mc.field_1724.method_31548().field_7545 = slot;
    }

    public boolean isWalking() {
        return this.isEnabled() && (Boolean)this.walk.getValue() != false && this.walkState > 0;
    }

    @Generated
    public static AutoFarmModule getInstance() {
        return instance;
    }
}

