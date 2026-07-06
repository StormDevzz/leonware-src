/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1802
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_2848
 *  net.minecraft.class_2848$class_2849
 */
package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1802;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2848;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.move.MoveEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.other.SlownessManager;
import sweetie.leonware.api.utils.player.InventoryUtil;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.client.features.modules.combat.elytratarget.ElytraTargetModule;
import sweetie.leonware.client.features.modules.player.ElytraSwapModule;

@ModuleRegister(name="Elytra Motion", category=Category.MOVEMENT)
public class ElytraMotionModule
extends Module {
    private static final ElytraMotionModule instance = new ElytraMotionModule();
    private final SliderSetting distance = new SliderSetting("Distance").value(Float.valueOf(3.0f)).range(0.1f, 6.0f).step(0.1f);
    private final BooleanSetting swapChestplate = new BooleanSetting("Swap chestplate").value(true);
    private final BooleanSetting useFirework = new BooleanSetting("Use firework").value(true).setVisible(this.swapChestplate::getValue);
    private final BooleanSetting legit = new BooleanSetting("Legit").value(false).setVisible(() -> (Boolean)this.swapChestplate.getValue() != false && (Boolean)this.useFirework.getValue() != false);
    private boolean swappedToChestplate = false;
    private final InventoryUtil.ItemUsage itemUsage = new InventoryUtil.ItemUsage(class_1802.field_8639, this);

    public ElytraMotionModule() {
        this.addSettings(this.distance, this.swapChestplate, this.useFirework, this.legit);
        this.itemUsage.setUseRotation(false);
    }

    @Override
    public void onEvent() {
        EventListener moveEvent = MoveEvent.getInstance().subscribe(new Listener<MoveEvent.MoveEventData>(0, event -> {
            float targetDistance;
            AuraModule aura = AuraModule.getInstance();
            class_1309 target = aura.target;
            if (!aura.isEnabled() || target == null) {
                return;
            }
            class_243 targetPos = RotationUtil.getSpot((class_1297)target);
            ElytraTargetModule elytraTarget = ElytraTargetModule.getInstance();
            if (elytraTarget.isEnabled() && elytraTarget.elytraRotationProcessor.using()) {
                targetPos = elytraTarget.elytraRotationProcessor.getPredictedPos(target);
            }
            if ((targetDistance = (float)targetPos.method_1022(ElytraMotionModule.mc.field_1724.method_33571())) < ((Float)this.distance.getValue()).floatValue() && (this.swappedToChestplate || ElytraMotionModule.mc.field_1724.method_6128())) {
                if (InventoryUtil.hasElytraEquipped() && ((Boolean)this.swapChestplate.getValue()).booleanValue()) {
                    this.swapToChestplate();
                    this.swappedToChestplate = true;
                }
                event.set(class_243.field_1353);
                MoveEvent.getInstance().setCancel(true);
            } else if (this.swappedToChestplate) {
                this.swapToElytra();
                this.swappedToChestplate = false;
            }
        }));
        this.addEvents(moveEvent);
    }

    private void fullySwap() {
        if (SlownessManager.isEnabled()) {
            SlownessManager.applySlowness(10L, () -> ElytraSwapModule.getInstance().swapChestplate());
        } else {
            ElytraSwapModule.getInstance().swapChestplate();
        }
    }

    private void swapToChestplate() {
        if (!InventoryUtil.hasElytraEquipped() || ElytraSwapModule.getInstance().findChestplateSlot() == -1) {
            return;
        }
        this.fullySwap();
    }

    private void swapToElytra() {
        if (InventoryUtil.hasElytraEquipped() || ElytraSwapModule.getInstance().findElytraSlot() == -1) {
            return;
        }
        this.fullySwap();
        if (!ElytraMotionModule.mc.field_1724.method_6128()) {
            ElytraMotionModule.mc.field_1724.method_23669();
            ElytraMotionModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2848((class_1297)ElytraMotionModule.mc.field_1724, class_2848.class_2849.field_12982));
            if (((Boolean)this.useFirework.getValue()).booleanValue()) {
                this.itemUsage.handleUse((Boolean)this.legit.getValue());
            }
        }
    }

    @Generated
    public static ElytraMotionModule getInstance() {
        return instance;
    }
}

