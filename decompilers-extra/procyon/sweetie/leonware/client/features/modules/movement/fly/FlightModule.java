// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement.fly;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.events.player.move.MotionEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.system.backend.Choice;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.client.features.modules.movement.fly.modes.FlightFakeAltTab;
import sweetie.leonware.client.features.modules.movement.fly.modes.FlightVanilla;
import sweetie.leonware.client.features.modules.movement.fly.modes.FlightGrim;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Flight", category = Category.MOVEMENT)
public class FlightModule extends Module
{
    private static final FlightModule instance;
    private final FlightGrim flightGrim;
    private final FlightVanilla flightVanilla;
    private final FlightFakeAltTab flightFakeAltTab;
    private final FlightMode[] modes;
    private FlightMode currentMode;
    private final ModeSetting mode;
    
    public FlightModule() {
        this.flightGrim = new FlightGrim(() -> this.getMode().is("Grim"), this);
        this.flightVanilla = new FlightVanilla(() -> this.getMode().is("Vanilla"));
        this.flightFakeAltTab = new FlightFakeAltTab(() -> this.getMode().is("FakeAltTab"));
        this.modes = new FlightMode[] { this.flightVanilla, this.flightGrim, this.flightFakeAltTab };
        this.currentMode = this.flightGrim;
        this.mode = new ModeSetting("Mode").value(this.flightGrim.getName()).values(Choice.getValues((Choice[])this.modes)).onAction(() -> this.currentMode = (FlightMode)Choice.getChoiceByName((String)this.getMode().getValue(), (Choice[])this.modes));
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
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> this.currentMode.onUpdate()));
        final EventListener motionEvent = MotionEvent.getInstance().subscribe(new Listener<MotionEvent.MotionEventData>(event -> this.currentMode.onMotion(event)));
        this.addEvents(updateEvent, motionEvent);
    }
    
    public boolean isVanillaFlying() {
        return this.isEnabled() && this.mode.is("Vanilla") && FlightModule.mc.field_1724 != null && FlightModule.mc.field_1724.method_31549().field_7479;
    }
    
    @Generated
    public static FlightModule getInstance() {
        return FlightModule.instance;
    }
    
    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }
    
    static {
        instance = new FlightModule();
    }
}
