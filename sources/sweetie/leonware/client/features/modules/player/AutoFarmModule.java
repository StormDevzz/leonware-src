package sweetie.leonware.client.features.modules.player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1661;
import net.minecraft.class_1792;
import net.minecraft.class_1794;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2266;
import net.minecraft.class_2282;
import net.minecraft.class_2302;
import net.minecraft.class_2338;
import net.minecraft.class_2344;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2420;
import net.minecraft.class_2421;
import net.minecraft.class_243;
import net.minecraft.class_2445;
import net.minecraft.class_2473;
import net.minecraft.class_2492;
import net.minecraft.class_2513;
import net.minecraft.class_2523;
import net.minecraft.class_2680;
import net.minecraft.class_2944;
import net.minecraft.class_3486;
import net.minecraft.class_3532;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/AutoFarmModule.class */
@ModuleRegister(name = "Auto Farm", category = Category.PLAYER)
public class AutoFarmModule extends Module {
    private static final AutoFarmModule instance = new AutoFarmModule();
    private final Map<class_2338, Integer> mossMap = new HashMap();
    private final SliderSetting range = new SliderSetting("Дальность").value(Float.valueOf(4.5f)).range(1.0f, 6.0f).step(0.1f);
    private final ModeSetting rotation = new ModeSetting("Вращение").values("Без", "Обычная", "Сайлент").value("Без");
    public final BooleanSetting walk = new BooleanSetting("Ходить").value((Boolean) false);
    private final SliderSetting walkDistance;
    private final BooleanSetting pitchControl;
    private final SliderSetting pitchValue;
    private final BooleanSetting yawControl;
    private final SliderSetting yawValue;
    private final BooleanSetting stopIfTarget;
    private int walkState;
    private int walkWaitTicks;
    private class_243 startPos;

    /* JADX INFO: renamed from: вспашкаНастр, reason: contains not printable characters */
    private final MultiBooleanSetting f0;

    /* JADX INFO: renamed from: сборНастр, reason: contains not printable characters */
    private final MultiBooleanSetting f1;

    /* JADX INFO: renamed from: посадкаНастр, reason: contains not printable characters */
    private final MultiBooleanSetting f2;

    /* JADX INFO: renamed from: костянаяНастр, reason: contains not printable characters */
    private final MultiBooleanSetting f3;

    @Generated
    public static AutoFarmModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v14, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v17, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v22, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v25, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v30, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v33, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    public AutoFarmModule() {
        SliderSetting sliderSettingStep = new SliderSetting("Дистанция").value(Float.valueOf(5.0f)).range(1.0f, 10.0f).step(1.0f);
        BooleanSetting booleanSetting = this.walk;
        Objects.requireNonNull(booleanSetting);
        this.walkDistance = sliderSettingStep.setVisible(booleanSetting::getValue);
        BooleanSetting booleanSettingValue = new BooleanSetting("Pitch Lock").value((Boolean) false);
        BooleanSetting booleanSetting2 = this.walk;
        Objects.requireNonNull(booleanSetting2);
        this.pitchControl = booleanSettingValue.setVisible(booleanSetting2::getValue);
        this.pitchValue = new SliderSetting("Pitch Value").value(Float.valueOf(0.0f)).range(-90.0f, 90.0f).step(1.0f).setVisible(() -> {
            return Boolean.valueOf(this.walk.getValue().booleanValue() && this.pitchControl.getValue().booleanValue());
        });
        BooleanSetting booleanSettingValue2 = new BooleanSetting("Yaw Lock").value((Boolean) false);
        BooleanSetting booleanSetting3 = this.walk;
        Objects.requireNonNull(booleanSetting3);
        this.yawControl = booleanSettingValue2.setVisible(booleanSetting3::getValue);
        this.yawValue = new SliderSetting("Yaw Value").value(Float.valueOf(0.0f)).range(-180.0f, 180.0f).step(1.0f).setVisible(() -> {
            return Boolean.valueOf(this.walk.getValue().booleanValue() && this.yawControl.getValue().booleanValue());
        });
        BooleanSetting booleanSettingValue3 = new BooleanSetting("Не ходить если таргет").value((Boolean) true);
        BooleanSetting booleanSetting4 = this.walk;
        Objects.requireNonNull(booleanSetting4);
        this.stopIfTarget = booleanSettingValue3.setVisible(booleanSetting4::getValue);
        this.walkState = 0;
        this.walkWaitTicks = 0;
        this.startPos = null;
        this.f0 = new MultiBooleanSetting("Вспашка").value(new BooleanSetting("Включено").value((Boolean) true), new BooleanSetting("Только политое").value((Boolean) false));
        this.f1 = new MultiBooleanSetting("Сбор").value(new BooleanSetting("Включено").value((Boolean) true), new BooleanSetting("Урожай").value((Boolean) true), new BooleanSetting("Бахчевые").value((Boolean) true), new BooleanSetting("Адская пшеница").value((Boolean) true), new BooleanSetting("Какао").value((Boolean) true), new BooleanSetting("Ягоды").value((Boolean) false), new BooleanSetting("Тростник").value((Boolean) false), new BooleanSetting("Кактус").value((Boolean) false));
        this.f2 = new MultiBooleanSetting("Посадка").value(new BooleanSetting("Включено").value((Boolean) true), new BooleanSetting("Урожай").value((Boolean) true), new BooleanSetting("Бахчевые").value((Boolean) true), new BooleanSetting("Адская пшеница").value((Boolean) true));
        this.f3 = new MultiBooleanSetting("Костная мука").value(new BooleanSetting("Включено").value((Boolean) true), new BooleanSetting("Урожай").value((Boolean) true), new BooleanSetting("Бахчевые").value((Boolean) true), new BooleanSetting("Какао").value((Boolean) true), new BooleanSetting("Ягоды").value((Boolean) false), new BooleanSetting("Грибы").value((Boolean) false), new BooleanSetting("Саженцы").value((Boolean) false), new BooleanSetting("Мох").value((Boolean) false));
        addSettings(this.range, this.rotation, this.f0, this.f1, this.f2, this.f3, this.walk, this.walkDistance, this.pitchControl, this.pitchValue, this.yawControl, this.yawValue, this.stopIfTarget);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        super.onEnable();
        if (this.walk.getValue().booleanValue() && mc.field_1724 != null) {
            this.walkState = 1;
            this.walkWaitTicks = 20;
            this.startPos = mc.field_1724.method_19538();
            return;
        }
        this.walkState = 0;
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        super.onDisable();
        this.mossMap.clear();
        if (this.walkState != 0 && mc.field_1690 != null) {
            mc.field_1690.field_1894.method_23481(false);
            mc.field_1690.field_1881.method_23481(false);
            this.walkState = 0;
        }
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            int slot;
            int slot2;
            int slot3;
            int slot4;
            int slot5;
            class_2338 bestBlock;
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            if (this.walk.getValue().booleanValue()) {
                boolean hasTarget = AuraModule.getInstance().target != null;
                if (this.stopIfTarget.getValue().booleanValue() && hasTarget) {
                    if (this.walkState > 0) {
                        mc.field_1690.field_1894.method_23481(false);
                        mc.field_1690.field_1881.method_23481(false);
                        this.walkState = 1;
                        this.walkWaitTicks = 20;
                    }
                } else {
                    if (this.yawControl.getValue().booleanValue()) {
                        mc.field_1724.method_36456(this.yawValue.getValue().floatValue());
                    }
                    if (this.pitchControl.getValue().booleanValue()) {
                        mc.field_1724.method_36457(this.pitchValue.getValue().floatValue());
                    }
                    if (this.walkState == 0) {
                        this.walkState = 1;
                        this.walkWaitTicks = 15;
                        this.startPos = mc.field_1724.method_19538();
                    }
                    if (this.walkState > 0) {
                        mc.field_1724.method_5728(false);
                    }
                    if (this.walkState == 1) {
                        if (this.walkWaitTicks > 0) {
                            this.walkWaitTicks--;
                        } else {
                            this.walkState = 2;
                            this.startPos = mc.field_1724.method_19538();
                            mc.field_1690.field_1894.method_23481(true);
                        }
                    } else if (this.walkState == 2) {
                        if (mc.field_1724.method_19538().method_1022(this.startPos) >= this.walkDistance.getValue().floatValue()) {
                            mc.field_1690.field_1894.method_23481(false);
                            this.walkState = 3;
                            mc.field_1690.field_1881.method_23481(true);
                        }
                    } else if (this.walkState == 3 && mc.field_1724.method_19538().method_1022(this.startPos) <= 0.5d) {
                        mc.field_1690.field_1881.method_23481(false);
                        this.walkState = 1;
                        this.walkWaitTicks = 20;
                    }
                }
            }
            this.mossMap.entrySet().removeIf(e -> {
                return ((Integer) e.setValue(Integer.valueOf(((Integer) e.getValue()).intValue() - 1))).intValue() == 0;
            });
            float rangeValue = this.range.getValue().floatValue();
            int ceilRange = class_3532.method_15386(rangeValue);
            if (this.f3.isEnabled("Включено") && this.f3.isEnabled("Мох") && (slot5 = findSlot(class_1802.field_8324)) != -1 && (bestBlock = (class_2338) class_2338.method_25998(class_2338.method_49638(mc.field_1724.method_33571()), ceilRange, ceilRange, ceilRange).filter(b -> {
                return mc.field_1724.method_33571().method_1022(class_243.method_24953(b)) <= ((double) rangeValue) && !this.mossMap.containsKey(b);
            }).map(b2 -> {
                int spots = getMossSpots(b2);
                if (spots > 10) {
                    return b2.method_10062();
                }
                return null;
            }).filter(b3 -> {
                return b3 != null;
            }).min((a, b4) -> {
                return b4.method_10265(a);
            }).orElse(null)) != null) {
                if (!mc.field_1687.method_22347(bestBlock.method_10084())) {
                    mc.field_1761.method_2902(bestBlock.method_10084(), class_2350.field_11036);
                }
                applyRotation(class_243.method_26410(bestBlock, 1.0d));
                selectSlot(slot5);
                mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, new class_3965(class_243.method_26410(bestBlock, 1.0d), class_2350.field_11036, bestBlock, false));
                this.mossMap.put(bestBlock, 100);
                return;
            }
            for (class_2338 pos : class_2338.method_25996(class_2338.method_49638(mc.field_1724.method_33571()), ceilRange, ceilRange, ceilRange)) {
                if (mc.field_1724.method_33571().method_1022(class_243.method_24953(pos)) <= rangeValue) {
                    class_2680 state = mc.field_1687.method_8320(pos);
                    class_2302 class_2302VarMethod_26204 = state.method_26204();
                    if (this.f0.isEnabled("Включено") && canTill(class_2302VarMethod_26204) && mc.field_1687.method_22347(pos.method_10084()) && ((!this.f0.isEnabled("Только политое") || isWatered(pos)) && (slot4 = findSlot(item -> {
                        return item instanceof class_1794;
                    })) != -1)) {
                        applyRotation(class_243.method_26410(pos, 1.0d));
                        selectSlot(slot4);
                        mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, new class_3965(class_243.method_26410(pos, 1.0d), class_2350.field_11036, pos, false));
                        return;
                    }
                    if (this.f1.isEnabled("Включено") && ((this.f1.isEnabled("Урожай") && (class_2302VarMethod_26204 instanceof class_2302) && class_2302VarMethod_26204.method_9825(state)) || ((this.f1.isEnabled("Бахчевые") && ((class_2302VarMethod_26204 instanceof class_2445) || class_2302VarMethod_26204 == class_2246.field_46283)) || ((this.f1.isEnabled("Адская пшеница") && (class_2302VarMethod_26204 instanceof class_2421) && ((Integer) state.method_11654(class_2421.field_11306)).intValue() >= 3) || ((this.f1.isEnabled("Какао") && (class_2302VarMethod_26204 instanceof class_2282) && ((Integer) state.method_11654(class_2282.field_10779)).intValue() >= 2) || ((this.f1.isEnabled("Ягоды") && (class_2302VarMethod_26204 instanceof class_3830) && ((Integer) state.method_11654(class_3830.field_17000)).intValue() >= 3) || ((this.f1.isEnabled("Тростник") && shouldHarvestTallCrop(pos, class_2302VarMethod_26204, class_2523.class)) || (this.f1.isEnabled("Кактус") && shouldHarvestTallCrop(pos, class_2302VarMethod_26204, class_2266.class))))))))) {
                        applyRotation(class_243.method_24953(pos));
                        mc.field_1761.method_2902(pos, class_2350.field_11036);
                        return;
                    }
                    if (this.f2.isEnabled("Включено") && mc.field_1687.method_8333((class_1297) null, new class_238(pos.method_10084()), entity -> {
                        return true;
                    }).isEmpty()) {
                        if ((class_2302VarMethod_26204 instanceof class_2344) && mc.field_1687.method_22347(pos.method_10084()) && (slot3 = findSlot(item2 -> {
                            return (this.f2.isEnabled("Урожай") && (item2 == class_1802.field_8317 || item2 == class_1802.field_8179 || item2 == class_1802.field_8567 || item2 == class_1802.field_8309)) || (this.f2.isEnabled("Бахчевые") && (item2 == class_1802.field_46249 || item2 == class_1802.field_46250));
                        })) != -1) {
                            applyRotation(class_243.method_26410(pos.method_10084(), 1.0d));
                            selectSlot(slot3);
                            mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, new class_3965(class_243.method_26410(pos.method_10084(), 1.0d), class_2350.field_11033, pos.method_10084(), false));
                            return;
                        } else if ((class_2302VarMethod_26204 instanceof class_2492) && mc.field_1687.method_22347(pos.method_10084()) && this.f2.isEnabled("Адская пшеница") && (slot2 = findSlot(item3 -> {
                            return item3 == class_1802.field_8790;
                        })) != -1) {
                            applyRotation(class_243.method_26410(pos.method_10084(), 1.0d));
                            selectSlot(slot2);
                            mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, new class_3965(class_243.method_26410(pos.method_10084(), 1.0d), class_2350.field_11033, pos.method_10084(), false));
                            return;
                        }
                    }
                    if (this.f3.isEnabled("Включено") && (slot = findSlot(item4 -> {
                        return item4 == class_1802.field_8324;
                    })) != -1) {
                        if ((!this.f3.isEnabled("Урожай") || !(class_2302VarMethod_26204 instanceof class_2302) || class_2302VarMethod_26204.method_9825(state)) && ((!this.f3.isEnabled("Бахчевые") || !(class_2302VarMethod_26204 instanceof class_2513) || ((Integer) state.method_11654(class_2513.field_11584)).intValue() >= 7) && ((!this.f3.isEnabled("Какао") || !(class_2302VarMethod_26204 instanceof class_2282) || ((Integer) state.method_11654(class_2282.field_10779)).intValue() >= 2) && ((!this.f3.isEnabled("Ягоды") || !(class_2302VarMethod_26204 instanceof class_3830) || ((Integer) state.method_11654(class_3830.field_17000)).intValue() >= 3) && (!this.f3.isEnabled("Грибы") || !(class_2302VarMethod_26204 instanceof class_2420)))))) {
                            if (this.f3.isEnabled("Саженцы") && ((class_2302VarMethod_26204 instanceof class_2473) || (class_2302VarMethod_26204 instanceof class_5800))) {
                                if (canPlaceSapling(pos)) {
                                }
                            }
                        }
                        applyRotation(class_243.method_26410(pos, 1.0d));
                        selectSlot(slot);
                        mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, new class_3965(class_243.method_26410(pos, 1.0d), class_2350.field_11036, pos, false));
                        return;
                    }
                }
            }
        }));
        addEvents(updateEvent);
    }

    private void applyRotation(class_243 target) {
        Rotation rot;
        rot = RotationUtil.rotationAt(target);
        switch (this.rotation.getValue()) {
            case "Обычная":
                mc.field_1724.method_36456(rot.getYaw());
                mc.field_1724.method_36457(rot.getPitch());
                break;
            case "Сайлент":
                RotationManager.getInstance().addRotation(rot, RotationStrategy.TARGET, TaskPriority.NORMAL, this);
                break;
        }
    }

    private boolean shouldHarvestTallCrop(class_2338 pos, class_2248 posBlock, Class<? extends class_2248> blockClass) {
        return posBlock.getClass().equals(blockClass) && mc.field_1687.method_8320(pos.method_10074()).method_26204().getClass().equals(blockClass) && !mc.field_1687.method_8320(pos.method_10087(2)).method_26204().getClass().equals(blockClass);
    }

    private int getMossSpots(class_2338 pos) {
        if (mc.field_1687.method_8320(pos).method_26204() != class_2246.field_28681 || mc.field_1687.method_8320(pos.method_10084()).method_26214(mc.field_1687, pos) != 0.0f) {
            return 0;
        }
        return (int) class_2338.method_25998(pos, 3, 4, 3).filter(b -> {
            return isMossGrowableOn(mc.field_1687.method_8320(b).method_26204()) && mc.field_1687.method_22347(b.method_10084());
        }).count();
    }

    private boolean isMossGrowableOn(class_2248 block) {
        return block == class_2246.field_10340 || block == class_2246.field_10474 || block == class_2246.field_10115 || block == class_2246.field_10508 || block == class_2246.field_10566 || block == class_2246.field_10253 || block == class_2246.field_10402 || block == class_2246.field_10219 || block == class_2246.field_10520 || block == class_2246.field_28685;
    }

    private boolean canPlaceSapling(class_2338 pos) {
        return class_2338.method_17962(pos.method_10263() - 1, pos.method_10264() + 1, pos.method_10260() - 1, pos.method_10263() + 1, pos.method_10264() + 5, pos.method_10260() + 1).allMatch(b -> {
            return class_2944.method_27371(mc.field_1687, b);
        });
    }

    private boolean canTill(class_2248 block) {
        return block == class_2246.field_10566 || block == class_2246.field_10219 || block == class_2246.field_10253 || block == class_2246.field_28685 || block == class_2246.field_10194;
    }

    private boolean isWatered(class_2338 pos) {
        return class_2338.method_17962(pos.method_10263() - 4, pos.method_10264(), pos.method_10260() - 4, pos.method_10263() + 4, pos.method_10264(), pos.method_10260() + 4).anyMatch(b -> {
            return mc.field_1687.method_8316(b).method_15767(class_3486.field_15517);
        });
    }

    private int findSlot(class_1792 item) {
        class_1661 inventory = mc.field_1724.method_31548();
        for (int i = 0; i < 9; i++) {
            if (inventory.method_5438(i).method_7909() == item) {
                return i;
            }
        }
        return -1;
    }

    private int findSlot(Predicate<class_1792> predicate) {
        class_1661 inventory = mc.field_1724.method_31548();
        for (int i = 0; i < 9; i++) {
            if (predicate.test(inventory.method_5438(i).method_7909())) {
                return i;
            }
        }
        return -1;
    }

    private void selectSlot(int slot) {
        mc.field_1724.method_31548().field_7545 = slot;
    }

    public boolean isWalking() {
        return isEnabled() && this.walk.getValue().booleanValue() && this.walkState > 0;
    }
}
