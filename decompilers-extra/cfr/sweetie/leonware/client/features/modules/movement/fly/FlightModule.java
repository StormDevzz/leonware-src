/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.client.features.modules.movement.fly;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.move.MotionEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.system.backend.Choice;
import sweetie.leonware.client.features.modules.movement.fly.FlightMode;
import sweetie.leonware.client.features.modules.movement.fly.modes.FlightFakeAltTab;
import sweetie.leonware.client.features.modules.movement.fly.modes.FlightGrim;
import sweetie.leonware.client.features.modules.movement.fly.modes.FlightVanilla;

@ModuleRegister(name="Flight", category=Category.MOVEMENT)
public class FlightModule
extends Module {
    private static final FlightModule instance = new FlightModule();
    private final FlightGrim flightGrim = new FlightGrim(() -> this.getMode().is("Grim"), this);
    private final FlightVanilla flightVanilla = new FlightVanilla(() -> this.getMode().is("Vanilla"));
    private final FlightFakeAltTab flightFakeAltTab = new FlightFakeAltTab(() -> this.getMode().is("FakeAltTab"));
    private final FlightMode[] modes = new FlightMode[]{this.flightVanilla, this.flightGrim, this.flightFakeAltTab};
    private FlightMode currentMode = this.flightGrim;
    private final ModeSetting mode = new ModeSetting("Mode").value(this.flightGrim.getName()).values(Choice.getValues(this.modes)).onAction(() -> {
        this.currentMode = (FlightMode)Choice.getChoiceByName((String)this.getMode().getValue(), this.modes);
    });

    public FlightModule() {
        this.addSettings(this.mode);
        this.addSettings(this.flightGrim.getSettings());
        this.addSettings(this.flightVanilla.getSettings());
    }

    @Override
    public void toggle() {
        super.toggle();
        this.currentMode.toggle();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.currentMode.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.currentMode.onDisable();
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> this.currentMode.onUpdate()));
        EventListener motionEvent = MotionEvent.getInstance().subscribe(new Listener<MotionEvent.MotionEventData>(event -> this.currentMode.onMotion((MotionEvent.MotionEventData)event)));
        this.addEvents(updateEvent, motionEvent);
    }

    public boolean isVanillaFlying() {
        return this.isEnabled() && this.mode.is("Vanilla") && FlightModule.mc.field_1724 != null && FlightModule.mc.field_1724.method_31549().field_7479;
    }

    @Generated
    public static FlightModule getInstance() {
        return instance;
    }

    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }
}

