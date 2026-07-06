// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import sweetie.leonware.api.utils.rotation.manager.RotationPlan;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.client.features.modules.player.AutoFarmModule;
import sweetie.leonware.api.event.events.player.move.SprintEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Sprint", category = Category.MOVEMENT)
public class SprintModule extends Module
{
    private static final SprintModule instance;
    public final ModeSetting mode;
    public final BooleanSetting keepSprint;
    
    public SprintModule() {
        this.mode = new ModeSetting("Mode").value("Legit").values("Legit", "Packet", "None");
        this.keepSprint = new BooleanSetting("Keep Sprint").value(false);
        this.addSettings(this.mode, this.keepSprint);
        this.setEnabled(true);
    }
    
    @Override
    public void onEvent() {
        final EventListener sprintEvent = SprintEvent.getInstance().subscribe(new Listener<SprintEvent.SprintEventData>(1, event -> {
            final InventoryMoveModule invMove = InventoryMoveModule.getInstance();
            if (invMove.isEnabled() && invMove.isGrim() && invMove.multiActionsC.getValue() && invMove.isMovementAllowedInScreen()) {
                event.setSprint(false);
                SprintModule.mc.field_1724.method_5728(false);
                return;
            }
            else if (invMove.isEnabled() && invMove.isAresMine() && invMove.isProcessing()) {
                event.setSprint(false);
                SprintModule.mc.field_1724.method_5728(false);
                return;
            }
            else if (AutoFarmModule.getInstance().isEnabled() && AutoFarmModule.getInstance().walk.getValue()) {
                event.setSprint(false);
                SprintModule.mc.field_1724.method_5728(false);
                return;
            }
            else {
                if (!this.isSprintAllowed()) {
                    event.setSprint(true);
                }
                return;
            }
        }));
        this.addEvents(sprintEvent);
    }
    
    public boolean isSprintAllowed() {
        final AuraModule auraModule = AuraModule.getInstance();
        final boolean auraCheck = this.mode.is("Legit") && auraModule.target != null && auraModule.isEnabled() && auraModule.combatExecutor.combatManager().clickScheduler().isOneTickBeforeAttack();
        return (!SprintModule.mc.field_1690.field_1867.method_1434() || SprintModule.mc.field_1724.field_3913.field_3905 != 0.0f) && (this.hasForwardMovement() || auraCheck);
    }
    
    public boolean hasForwardMovement() {
        final RotationManager rotationManager = RotationManager.getInstance();
        final RotationPlan plan = rotationManager.getCurrentRotationPlan();
        if (plan == null || plan.provider() instanceof StrafeModule || plan.moveCorrection() || this.mode.is("None")) {
            return false;
        }
        final Rotation currentRotation = (rotationManager.getCurrentRotation() != null) ? rotationManager.getCurrentRotation() : new Rotation(SprintModule.mc.field_1724.method_36454(), SprintModule.mc.field_1724.method_36455());
        final float deltaYaw = SprintModule.mc.field_1724.method_36454() - currentRotation.getYaw();
        final float forward = SprintModule.mc.field_1724.field_3913.field_3905;
        final float sideways = SprintModule.mc.field_1724.field_3913.field_3907;
        final boolean hasForwardMovement = forward * class_3532.method_15362(deltaYaw * 0.017453292f) + sideways * class_3532.method_15374(deltaYaw * 0.017453292f) > 1.0E-5f;
        return !hasForwardMovement;
    }
    
    @Generated
    public static SprintModule getInstance() {
        return SprintModule.instance;
    }
    
    static {
        instance = new SprintModule();
    }
}
