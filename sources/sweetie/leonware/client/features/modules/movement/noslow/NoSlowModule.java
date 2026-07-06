package sweetie.leonware.client.features.modules.movement.noslow;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.system.backend.Choice;
import sweetie.leonware.client.features.modules.movement.noslow.modes.NoSlowCancel;
import sweetie.leonware.client.features.modules.movement.noslow.modes.NoSlowFuntime;
import sweetie.leonware.client.features.modules.movement.noslow.modes.NoSlowGrim;
import sweetie.leonware.client.features.modules.movement.noslow.modes.NoSlowSlotUpdate;
import sweetie.leonware.client.features.modules.movement.noslow.modes.NoSlowV3;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/noslow/NoSlowModule.class */
@ModuleRegister(name = "No Slow", category = Category.MOVEMENT)
public class NoSlowModule extends Module {
    private static final NoSlowModule instance = new NoSlowModule();
    private NoSlowMode currentMode;
    private final NoSlowCancel noSlowCancel = new NoSlowCancel();
    private final NoSlowSlotUpdate noSlowSlotUpdate = new NoSlowSlotUpdate();
    private final NoSlowGrim noSlowGrim = new NoSlowGrim();
    private final NoSlowV3 noSlowGrimV3 = new NoSlowV3();
    private final NoSlowFuntime noSlowFunTime = new NoSlowFuntime();
    private final NoSlowMode[] modes = {this.noSlowCancel, this.noSlowSlotUpdate, this.noSlowGrim, this.noSlowGrimV3, this.noSlowFunTime};
    private final ModeSetting mode = new ModeSetting("Mode").value("Cancel").values(Choice.getValues(this.modes)).onAction2(() -> {
        NoSlowMode found = (NoSlowMode) Choice.getChoiceByName(getMode().getValue(), (Choice[]) this.modes);
        this.currentMode = found != null ? found : this.noSlowCancel;
    });
    private final ModeSetting grimMode = new ModeSetting("Grim mode").value("Tick").values("Tick", "TickS", "Old", "Drop").setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Grim"));
    }).onAction2(() -> {
        NoSlowGrim.BypassType bypassType;
        NoSlowGrim noSlowGrim = this.noSlowGrim;
        switch (getGrimMode().getValue()) {
            case "Tick":
                bypassType = NoSlowGrim.BypassType.TICK;
                break;
            case "TickS":
                bypassType = NoSlowGrim.BypassType.TICKS;
                break;
            case "Drop":
                bypassType = NoSlowGrim.BypassType.DROP;
                break;
            default:
                bypassType = NoSlowGrim.BypassType.OLD;
                break;
        }
        noSlowGrim.bypassType = bypassType;
    });

    @Generated
    public static NoSlowModule getInstance() {
        return instance;
    }

    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }

    @Generated
    public ModeSetting getGrimMode() {
        return this.grimMode;
    }

    /* JADX WARN: Type inference failed for: r1v12, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v17, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    public NoSlowModule() {
        this.currentMode = this.noSlowCancel;
        addSettings(this.mode, this.grimMode);
        this.currentMode = this.noSlowCancel;
    }

    public boolean doUseNoSlow() {
        return isEnabled() && mc.field_1724 != null && this.currentMode != null && mc.field_1724.method_6115() && this.currentMode.slowingCancel();
    }

    public NoSlowV3 getNoSlowV3() {
        return this.noSlowGrimV3;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (this.currentMode != null) {
                this.currentMode.onUpdate();
            }
        }));
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener(event2 -> {
            if (this.currentMode != null) {
                this.currentMode.onTick();
            }
        }));
        addEvents(updateEvent, tickEvent);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        this.noSlowFunTime.onDisable();
        super.onDisable();
    }
}
