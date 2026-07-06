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
import sweetie.leonware.client.features.modules.movement.fly.modes.FlightFakeAltTab;
import sweetie.leonware.client.features.modules.movement.fly.modes.FlightGrim;
import sweetie.leonware.client.features.modules.movement.fly.modes.FlightVanilla;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/fly/FlightModule.class */
@ModuleRegister(name = "Flight", category = Category.MOVEMENT)
public class FlightModule extends Module {
    private static final FlightModule instance = new FlightModule();
    private final FlightGrim flightGrim = new FlightGrim(() -> {
        return Boolean.valueOf(getMode().is("Grim"));
    }, this);
    private final FlightVanilla flightVanilla = new FlightVanilla(() -> {
        return Boolean.valueOf(getMode().is("Vanilla"));
    });
    private final FlightFakeAltTab flightFakeAltTab = new FlightFakeAltTab(() -> {
        return Boolean.valueOf(getMode().is("FakeAltTab"));
    });
    private final FlightMode[] modes = {this.flightVanilla, this.flightGrim, this.flightFakeAltTab};
    private FlightMode currentMode = this.flightGrim;
    private final ModeSetting mode = new ModeSetting("Mode").value(this.flightGrim.getName()).values(Choice.getValues(this.modes)).onAction2(() -> {
        this.currentMode = (FlightMode) Choice.getChoiceByName(getMode().getValue(), (Choice[]) this.modes);
    });

    @Generated
    public static FlightModule getInstance() {
        return instance;
    }

    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }

    /* JADX WARN: Type inference failed for: r1v10, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    public FlightModule() {
        addSettings(this.mode);
        addSettings(this.flightGrim.getSettings());
        addSettings(this.flightVanilla.getSettings());
    }

    @Override // sweetie.leonware.api.module.Module
    public void toggle() {
        super.toggle();
        this.currentMode.toggle();
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        super.onEnable();
        this.currentMode.onEnable();
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        super.onDisable();
        this.currentMode.onDisable();
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            this.currentMode.onUpdate();
        }));
        EventListener motionEvent = MotionEvent.getInstance().subscribe(new Listener(event2 -> {
            this.currentMode.onMotion(event2);
        }));
        addEvents(updateEvent, motionEvent);
    }

    public boolean isVanillaFlying() {
        return isEnabled() && this.mode.is("Vanilla") && mc.field_1724 != null && mc.field_1724.method_31549().field_7479;
    }
}
