package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_3532;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.move.SprintEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationPlan;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.client.features.modules.player.AutoFarmModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/SprintModule.class */
@ModuleRegister(name = "Sprint", category = Category.MOVEMENT)
public class SprintModule extends Module {
    private static final SprintModule instance = new SprintModule();
    public final ModeSetting mode = new ModeSetting("Mode").value("Legit").values("Legit", "Packet", "None");
    public final BooleanSetting keepSprint = new BooleanSetting("Keep Sprint").value((Boolean) false);

    @Generated
    public static SprintModule getInstance() {
        return instance;
    }

    public SprintModule() {
        addSettings(this.mode, this.keepSprint);
        setEnabled(true);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener sprintEvent = SprintEvent.getInstance().subscribe(new Listener(1, event -> {
            InventoryMoveModule invMove = InventoryMoveModule.getInstance();
            if (invMove.isEnabled() && invMove.isGrim() && invMove.multiActionsC.getValue().booleanValue() && invMove.isMovementAllowedInScreen()) {
                event.setSprint(false);
                mc.field_1724.method_5728(false);
                return;
            }
            if (invMove.isEnabled() && invMove.isAresMine() && invMove.isProcessing()) {
                event.setSprint(false);
                mc.field_1724.method_5728(false);
            } else if (AutoFarmModule.getInstance().isEnabled() && AutoFarmModule.getInstance().walk.getValue().booleanValue()) {
                event.setSprint(false);
                mc.field_1724.method_5728(false);
            } else if (!isSprintAllowed()) {
                event.setSprint(true);
            }
        }));
        addEvents(sprintEvent);
    }

    public boolean isSprintAllowed() {
        AuraModule auraModule = AuraModule.getInstance();
        boolean auraCheck = this.mode.is("Legit") && auraModule.target != null && auraModule.isEnabled() && auraModule.combatExecutor.combatManager().clickScheduler().isOneTickBeforeAttack();
        return !(mc.field_1690.field_1867.method_1434() && mc.field_1724.field_3913.field_3905 == 0.0f) && (hasForwardMovement() || auraCheck);
    }

    public boolean hasForwardMovement() {
        RotationManager rotationManager = RotationManager.getInstance();
        RotationPlan plan = rotationManager.getCurrentRotationPlan();
        if (plan == null || (plan.provider() instanceof StrafeModule) || plan.moveCorrection() || this.mode.is("None")) {
            return false;
        }
        Rotation currentRotation = rotationManager.getCurrentRotation() != null ? rotationManager.getCurrentRotation() : new Rotation(mc.field_1724.method_36454(), mc.field_1724.method_36455());
        float deltaYaw = mc.field_1724.method_36454() - currentRotation.getYaw();
        float forward = mc.field_1724.field_3913.field_3905;
        float sideways = mc.field_1724.field_3913.field_3907;
        boolean hasForwardMovement = (forward * class_3532.method_15362(deltaYaw * 0.017453292f)) + (sideways * class_3532.method_15374(deltaYaw * 0.017453292f)) > 1.0E-5f;
        return !hasForwardMovement;
    }
}
