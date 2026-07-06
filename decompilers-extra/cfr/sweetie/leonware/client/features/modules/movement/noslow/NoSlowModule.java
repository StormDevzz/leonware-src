/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
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
import sweetie.leonware.client.features.modules.movement.noslow.NoSlowMode;
import sweetie.leonware.client.features.modules.movement.noslow.modes.NoSlowCancel;
import sweetie.leonware.client.features.modules.movement.noslow.modes.NoSlowFuntime;
import sweetie.leonware.client.features.modules.movement.noslow.modes.NoSlowGrim;
import sweetie.leonware.client.features.modules.movement.noslow.modes.NoSlowSlotUpdate;
import sweetie.leonware.client.features.modules.movement.noslow.modes.NoSlowV3;

@ModuleRegister(name="No Slow", category=Category.MOVEMENT)
public class NoSlowModule
extends Module {
    private static final NoSlowModule instance = new NoSlowModule();
    private final NoSlowCancel noSlowCancel = new NoSlowCancel();
    private final NoSlowSlotUpdate noSlowSlotUpdate = new NoSlowSlotUpdate();
    private final NoSlowGrim noSlowGrim = new NoSlowGrim();
    private final NoSlowV3 noSlowGrimV3 = new NoSlowV3();
    private final NoSlowFuntime noSlowFunTime = new NoSlowFuntime();
    private final NoSlowMode[] modes = new NoSlowMode[]{this.noSlowCancel, this.noSlowSlotUpdate, this.noSlowGrim, this.noSlowGrimV3, this.noSlowFunTime};
    private NoSlowMode currentMode = this.noSlowCancel;
    private final ModeSetting mode = new ModeSetting("Mode").value("Cancel").values(Choice.getValues(this.modes)).onAction(() -> {
        NoSlowMode found = (NoSlowMode)Choice.getChoiceByName((String)this.getMode().getValue(), this.modes);
        this.currentMode = found != null ? found : this.noSlowCancel;
    });
    private final ModeSetting grimMode = ((ModeSetting)new ModeSetting("Grim mode").value("Tick").values("Tick", "TickS", "Old", "Drop").setVisible(() -> this.mode.is("Grim"))).onAction(() -> {
        this.noSlowGrim.bypassType = switch ((String)this.getGrimMode().getValue()) {
            case "Tick" -> NoSlowGrim.BypassType.TICK;
            case "TickS" -> NoSlowGrim.BypassType.TICKS;
            case "Drop" -> NoSlowGrim.BypassType.DROP;
            default -> NoSlowGrim.BypassType.OLD;
        };
    });

    public NoSlowModule() {
        this.addSettings(this.mode, this.grimMode);
        this.currentMode = this.noSlowCancel;
    }

    public boolean doUseNoSlow() {
        return this.isEnabled() && NoSlowModule.mc.field_1724 != null && this.currentMode != null && NoSlowModule.mc.field_1724.method_6115() && this.currentMode.slowingCancel();
    }

    public NoSlowV3 getNoSlowV3() {
        return this.noSlowGrimV3;
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (this.currentMode != null) {
                this.currentMode.onUpdate();
            }
        }));
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            if (this.currentMode != null) {
                this.currentMode.onTick();
            }
        }));
        this.addEvents(updateEvent, tickEvent);
    }

    @Override
    public void onDisable() {
        this.noSlowFunTime.onDisable();
        super.onDisable();
    }

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
}

