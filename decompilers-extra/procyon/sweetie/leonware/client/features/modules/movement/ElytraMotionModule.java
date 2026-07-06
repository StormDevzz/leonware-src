// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_2596;
import net.minecraft.class_2848;
import sweetie.leonware.client.features.modules.player.ElytraSwapModule;
import sweetie.leonware.api.utils.other.SlownessManager;
import net.minecraft.class_1309;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_243;
import sweetie.leonware.client.features.modules.combat.elytratarget.ElytraTargetModule;
import net.minecraft.class_1297;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.api.event.events.player.move.MoveEvent;
import sweetie.leonware.api.module.setting.Setting;
import net.minecraft.class_1802;
import java.util.function.Supplier;
import java.util.Objects;
import sweetie.leonware.api.utils.player.InventoryUtil;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Elytra Motion", category = Category.MOVEMENT)
public class ElytraMotionModule extends Module
{
    private static final ElytraMotionModule instance;
    private final SliderSetting distance;
    private final BooleanSetting swapChestplate;
    private final BooleanSetting useFirework;
    private final BooleanSetting legit;
    private boolean swappedToChestplate;
    private final InventoryUtil.ItemUsage itemUsage;
    
    public ElytraMotionModule() {
        this.distance = new SliderSetting("Distance").value(3.0f).range(0.1f, 6.0f).step(0.1f);
        this.swapChestplate = new BooleanSetting("Swap chestplate").value(true);
        final BooleanSetting value = new BooleanSetting("Use firework").value(true);
        final BooleanSetting swapChestplate = this.swapChestplate;
        Objects.requireNonNull(swapChestplate);
        this.useFirework = value.setVisible((Supplier<Boolean>)swapChestplate::getValue);
        this.legit = new BooleanSetting("Legit").value(false).setVisible(() -> this.swapChestplate.getValue() && this.useFirework.getValue());
        this.swappedToChestplate = false;
        this.itemUsage = new InventoryUtil.ItemUsage(class_1802.field_8639, this);
        this.addSettings(this.distance, this.swapChestplate, this.useFirework, this.legit);
        this.itemUsage.setUseRotation(false);
    }
    
    @Override
    public void onEvent() {
        final EventListener moveEvent = MoveEvent.getInstance().subscribe(new Listener<MoveEvent.MoveEventData>(0, event -> {
            final AuraModule aura = AuraModule.getInstance();
            final class_1309 target = aura.target;
            if (!aura.isEnabled() || target == null) {
                return;
            }
            else {
                class_243 targetPos = RotationUtil.getSpot((class_1297)target);
                final ElytraTargetModule elytraTarget = ElytraTargetModule.getInstance();
                if (elytraTarget.isEnabled() && elytraTarget.elytraRotationProcessor.using()) {
                    targetPos = elytraTarget.elytraRotationProcessor.getPredictedPos(target);
                }
                final float targetDistance = (float)targetPos.method_1022(ElytraMotionModule.mc.field_1724.method_33571());
                if (targetDistance < this.distance.getValue() && (this.swappedToChestplate || ElytraMotionModule.mc.field_1724.method_6128())) {
                    if (InventoryUtil.hasElytraEquipped() && this.swapChestplate.getValue()) {
                        this.swapToChestplate();
                        this.swappedToChestplate = true;
                    }
                    event.set(class_243.field_1353);
                    MoveEvent.getInstance().setCancel(true);
                }
                else if (this.swappedToChestplate) {
                    this.swapToElytra();
                    this.swappedToChestplate = false;
                }
                return;
            }
        }));
        this.addEvents(moveEvent);
    }
    
    private void fullySwap() {
        if (SlownessManager.isEnabled()) {
            SlownessManager.applySlowness(10L, () -> ElytraSwapModule.getInstance().swapChestplate());
        }
        else {
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
            if (this.useFirework.getValue()) {
                this.itemUsage.handleUse(this.legit.getValue());
            }
        }
    }
    
    @Generated
    public static ElytraMotionModule getInstance() {
        return ElytraMotionModule.instance;
    }
    
    static {
        instance = new ElytraMotionModule();
    }
}
