package sweetie.leonware.client.features.modules.movement;

import java.util.Objects;
import lombok.Generated;
import net.minecraft.class_1309;
import net.minecraft.class_1802;
import net.minecraft.class_243;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/ElytraMotionModule.class */
@ModuleRegister(name = "Elytra Motion", category = Category.MOVEMENT)
public class ElytraMotionModule extends Module {
    private static final ElytraMotionModule instance = new ElytraMotionModule();
    private final SliderSetting distance = new SliderSetting("Distance").value(Float.valueOf(3.0f)).range(0.1f, 6.0f).step(0.1f);
    private final BooleanSetting swapChestplate = new BooleanSetting("Swap chestplate").value((Boolean) true);
    private final BooleanSetting useFirework;
    private final BooleanSetting legit;
    private boolean swappedToChestplate;
    private final InventoryUtil.ItemUsage itemUsage;

    @Generated
    public static ElytraMotionModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v8, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    public ElytraMotionModule() {
        BooleanSetting booleanSettingValue = new BooleanSetting("Use firework").value((Boolean) true);
        BooleanSetting booleanSetting = this.swapChestplate;
        Objects.requireNonNull(booleanSetting);
        this.useFirework = booleanSettingValue.setVisible(booleanSetting::getValue);
        this.legit = new BooleanSetting("Legit").value((Boolean) false).setVisible(() -> {
            return Boolean.valueOf(this.swapChestplate.getValue().booleanValue() && this.useFirework.getValue().booleanValue());
        });
        this.swappedToChestplate = false;
        this.itemUsage = new InventoryUtil.ItemUsage(class_1802.field_8639, this);
        addSettings(this.distance, this.swapChestplate, this.useFirework, this.legit);
        this.itemUsage.setUseRotation(false);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener moveEvent = MoveEvent.getInstance().subscribe(new Listener(0, event -> {
            AuraModule aura = AuraModule.getInstance();
            class_1309 target = aura.target;
            if (!aura.isEnabled() || target == null) {
                return;
            }
            class_243 targetPos = RotationUtil.getSpot(target);
            ElytraTargetModule elytraTarget = ElytraTargetModule.getInstance();
            if (elytraTarget.isEnabled() && elytraTarget.elytraRotationProcessor.using()) {
                targetPos = elytraTarget.elytraRotationProcessor.getPredictedPos(target);
            }
            float targetDistance = (float) targetPos.method_1022(mc.field_1724.method_33571());
            if (targetDistance < this.distance.getValue().floatValue() && (this.swappedToChestplate || mc.field_1724.method_6128())) {
                if (InventoryUtil.hasElytraEquipped() && this.swapChestplate.getValue().booleanValue()) {
                    swapToChestplate();
                    this.swappedToChestplate = true;
                }
                event.set(class_243.field_1353);
                MoveEvent.getInstance().setCancel(true);
                return;
            }
            if (this.swappedToChestplate) {
                swapToElytra();
                this.swappedToChestplate = false;
            }
        }));
        addEvents(moveEvent);
    }

    private void fullySwap() {
        if (SlownessManager.isEnabled()) {
            SlownessManager.applySlowness(10L, () -> {
                ElytraSwapModule.getInstance().swapChestplate();
            });
        } else {
            ElytraSwapModule.getInstance().swapChestplate();
        }
    }

    private void swapToChestplate() {
        if (!InventoryUtil.hasElytraEquipped() || ElytraSwapModule.getInstance().findChestplateSlot() == -1) {
            return;
        }
        fullySwap();
    }

    private void swapToElytra() {
        if (InventoryUtil.hasElytraEquipped() || ElytraSwapModule.getInstance().findElytraSlot() == -1) {
            return;
        }
        fullySwap();
        if (!mc.field_1724.method_6128()) {
            mc.field_1724.method_23669();
            mc.field_1724.field_3944.method_52787(new class_2848(mc.field_1724, class_2848.class_2849.field_12982));
            if (this.useFirework.getValue().booleanValue()) {
                this.itemUsage.handleUse(this.legit.getValue().booleanValue());
            }
        }
    }
}
