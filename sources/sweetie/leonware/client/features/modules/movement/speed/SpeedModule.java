package sweetie.leonware.client.features.modules.movement.speed;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.KeyEvent;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.move.OnMovePostEvent;
import sweetie.leonware.api.event.events.player.move.PostMotionEvent;
import sweetie.leonware.api.event.events.player.move.TravelEvent;
import sweetie.leonware.api.event.events.player.other.MovementInputEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.system.backend.Choice;
import sweetie.leonware.client.features.modules.movement.speed.modes.SpeedAresMine;
import sweetie.leonware.client.features.modules.movement.speed.modes.SpeedAresMine2;
import sweetie.leonware.client.features.modules.movement.speed.modes.SpeedGrim;
import sweetie.leonware.client.features.modules.movement.speed.modes.SpeedMetaHvH;
import sweetie.leonware.client.features.modules.movement.speed.modes.SpeedNCP;
import sweetie.leonware.client.features.modules.movement.speed.modes.SpeedReallyworld;
import sweetie.leonware.client.features.modules.movement.speed.modes.SpeedVanilla;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/speed/SpeedModule.class */
@ModuleRegister(name = "Speed", category = Category.MOVEMENT)
public class SpeedModule extends Module {
    private static final SpeedModule instance = new SpeedModule();
    private SpeedMode currentMode;
    private final SpeedNCP speedNCP = new SpeedNCP(() -> {
        return Boolean.valueOf(getMode().is("NoCheatPlus"));
    });
    private final SpeedAresMine speedAresMine = new SpeedAresMine(() -> {
        return Boolean.valueOf(getMode().is("AresMine"));
    });
    private final SpeedAresMine2 speedAresMine2 = new SpeedAresMine2(() -> {
        return Boolean.valueOf(getMode().is("AresMine2"));
    });
    private final SpeedGrim speedGrim = new SpeedGrim(() -> {
        return Boolean.valueOf(getMode().is("Grim"));
    });
    private final SpeedVanilla speedVanilla = new SpeedVanilla(() -> {
        return Boolean.valueOf(getMode().is("Vanilla"));
    });
    private final SpeedMetaHvH speedMetaHvH = new SpeedMetaHvH(() -> {
        return Boolean.valueOf(getMode().is("MetaHvH"));
    });
    private final SpeedReallyworld speedReallyworld = new SpeedReallyworld(() -> {
        return Boolean.valueOf(getMode().is("Reallyworld"));
    });
    private final SpeedMode[] modes = {this.speedVanilla, this.speedNCP, this.speedAresMine, this.speedAresMine2, this.speedGrim, this.speedMetaHvH, this.speedReallyworld};
    private final ModeSetting mode = new ModeSetting("Режим").value("Grim").values(Choice.getValues(this.modes)).onAction2(() -> {
        SpeedMode found = (SpeedMode) Choice.getChoiceByName(getMode().getValue(), (Choice[]) this.modes);
        this.currentMode = found != null ? found : this.speedGrim;
    });

    @Generated
    public static SpeedModule getInstance() {
        return instance;
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        super.onEnable();
        if (this.currentMode != null) {
            this.currentMode.onEnable();
        }
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        super.onDisable();
        if (this.currentMode != null) {
            this.currentMode.onDisable();
        }
    }

    @Override // sweetie.leonware.api.module.Module
    public void toggle() {
        super.toggle();
        if (this.currentMode != null) {
            this.currentMode.toggle();
        }
    }

    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener onMovePostEvent = OnMovePostEvent.getInstance().subscribe(new Listener(event -> {
            SpeedMode patt0$temp = this.currentMode;
            if (patt0$temp instanceof SpeedReallyworld) {
                SpeedReallyworld rw = (SpeedReallyworld) patt0$temp;
                rw.onMovePost(event);
            }
        }));
        EventListener movementInputEvent = MovementInputEvent.getInstance().subscribe(new Listener(event2 -> {
            SpeedMode patt0$temp = this.currentMode;
            if (patt0$temp instanceof SpeedReallyworld) {
                SpeedReallyworld rw = (SpeedReallyworld) patt0$temp;
                rw.onMoveInput();
            }
        }));
        EventListener postMotionEvent = PostMotionEvent.getInstance().subscribe(new Listener(event3 -> {
            SpeedMode patt0$temp = this.currentMode;
            if (patt0$temp instanceof SpeedReallyworld) {
                SpeedReallyworld rw = (SpeedReallyworld) patt0$temp;
                rw.onPostMotion();
            }
        }));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event4 -> {
            if (this.currentMode != null) {
                this.currentMode.onUpdate();
            }
        }));
        EventListener travelEvent = TravelEvent.getInstance().subscribe(new Listener(event5 -> {
            if (this.currentMode != null) {
                this.currentMode.onTravel();
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(event6 -> {
            SpeedMode patt0$temp = this.currentMode;
            if (patt0$temp instanceof SpeedMetaHvH) {
                SpeedMetaHvH meta = (SpeedMetaHvH) patt0$temp;
                meta.onPacket(event6);
            }
            SpeedMode patt1$temp = this.currentMode;
            if (patt1$temp instanceof SpeedReallyworld) {
                SpeedReallyworld rw = (SpeedReallyworld) patt1$temp;
                rw.onPacket(event6);
            }
        }));
        EventListener keyEvent = KeyEvent.getInstance().subscribe(new Listener(event7 -> {
            SpeedMode patt0$temp = this.currentMode;
            if (patt0$temp instanceof SpeedMetaHvH) {
                SpeedMetaHvH meta = (SpeedMetaHvH) patt0$temp;
                meta.onKey(event7);
            }
        }));
        addEvents(onMovePostEvent, movementInputEvent, postMotionEvent, updateEvent, travelEvent, packetEvent, keyEvent);
    }

    /* JADX WARN: Type inference failed for: r1v14, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    public SpeedModule() {
        this.currentMode = this.speedGrim;
        addSettings(this.mode);
        addSettings(this.speedNCP.getSettings());
        addSettings(this.speedAresMine.getSettings());
        addSettings(this.speedAresMine2.getSettings());
        addSettings(this.speedGrim.getSettings());
        addSettings(this.speedVanilla.getSettings());
        addSettings(this.speedMetaHvH.getSettings());
        this.currentMode = this.speedGrim;
    }
}
