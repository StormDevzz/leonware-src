/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
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
import sweetie.leonware.client.features.modules.movement.speed.SpeedMode;
import sweetie.leonware.client.features.modules.movement.speed.modes.SpeedAresMine;
import sweetie.leonware.client.features.modules.movement.speed.modes.SpeedAresMine2;
import sweetie.leonware.client.features.modules.movement.speed.modes.SpeedGrim;
import sweetie.leonware.client.features.modules.movement.speed.modes.SpeedMetaHvH;
import sweetie.leonware.client.features.modules.movement.speed.modes.SpeedNCP;
import sweetie.leonware.client.features.modules.movement.speed.modes.SpeedReallyworld;
import sweetie.leonware.client.features.modules.movement.speed.modes.SpeedVanilla;

@ModuleRegister(name="Speed", category=Category.MOVEMENT)
public class SpeedModule
extends Module {
    private static final SpeedModule instance = new SpeedModule();
    private final SpeedNCP speedNCP = new SpeedNCP(() -> this.getMode().is("NoCheatPlus"));
    private final SpeedAresMine speedAresMine = new SpeedAresMine(() -> this.getMode().is("AresMine"));
    private final SpeedAresMine2 speedAresMine2 = new SpeedAresMine2(() -> this.getMode().is("AresMine2"));
    private final SpeedGrim speedGrim = new SpeedGrim(() -> this.getMode().is("Grim"));
    private final SpeedVanilla speedVanilla = new SpeedVanilla(() -> this.getMode().is("Vanilla"));
    private final SpeedMetaHvH speedMetaHvH = new SpeedMetaHvH(() -> this.getMode().is("MetaHvH"));
    private final SpeedReallyworld speedReallyworld = new SpeedReallyworld(() -> this.getMode().is("Reallyworld"));
    private final SpeedMode[] modes = new SpeedMode[]{this.speedVanilla, this.speedNCP, this.speedAresMine, this.speedAresMine2, this.speedGrim, this.speedMetaHvH, this.speedReallyworld};
    private SpeedMode currentMode = this.speedGrim;
    private final ModeSetting mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").value("Grim").values(Choice.getValues(this.modes)).onAction(() -> {
        SpeedMode found = (SpeedMode)Choice.getChoiceByName((String)this.getMode().getValue(), this.modes);
        this.currentMode = found != null ? found : this.speedGrim;
    });

    @Override
    public void onEnable() {
        super.onEnable();
        if (this.currentMode != null) {
            this.currentMode.onEnable();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (this.currentMode != null) {
            this.currentMode.onDisable();
        }
    }

    @Override
    public void toggle() {
        super.toggle();
        if (this.currentMode != null) {
            this.currentMode.toggle();
        }
    }

    @Override
    public void onEvent() {
        EventListener onMovePostEvent = OnMovePostEvent.getInstance().subscribe(new Listener<OnMovePostEvent.OnMovePostEventData>(event -> {
            SpeedMode patt0$temp = this.currentMode;
            if (patt0$temp instanceof SpeedReallyworld) {
                SpeedReallyworld rw = (SpeedReallyworld)patt0$temp;
                rw.onMovePost((OnMovePostEvent.OnMovePostEventData)event);
            }
        }));
        EventListener movementInputEvent = MovementInputEvent.getInstance().subscribe(new Listener<MovementInputEvent.MovementInputEventData>(event -> {
            SpeedMode patt0$temp = this.currentMode;
            if (patt0$temp instanceof SpeedReallyworld) {
                SpeedReallyworld rw = (SpeedReallyworld)patt0$temp;
                rw.onMoveInput();
            }
        }));
        EventListener postMotionEvent = PostMotionEvent.getInstance().subscribe(new Listener<PostMotionEvent>(event -> {
            SpeedMode patt0$temp = this.currentMode;
            if (patt0$temp instanceof SpeedReallyworld) {
                SpeedReallyworld rw = (SpeedReallyworld)patt0$temp;
                rw.onPostMotion();
            }
        }));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (this.currentMode != null) {
                this.currentMode.onUpdate();
            }
        }));
        EventListener travelEvent = TravelEvent.getInstance().subscribe(new Listener<TravelEvent>(event -> {
            if (this.currentMode != null) {
                this.currentMode.onTravel();
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            SpeedMode patt1$temp;
            SpeedMode patt0$temp = this.currentMode;
            if (patt0$temp instanceof SpeedMetaHvH) {
                SpeedMetaHvH meta = (SpeedMetaHvH)patt0$temp;
                meta.onPacket((PacketEvent.PacketEventData)event);
            }
            if ((patt1$temp = this.currentMode) instanceof SpeedReallyworld) {
                SpeedReallyworld rw = (SpeedReallyworld)patt1$temp;
                rw.onPacket((PacketEvent.PacketEventData)event);
            }
        }));
        EventListener keyEvent = KeyEvent.getInstance().subscribe(new Listener<KeyEvent.KeyEventData>(event -> {
            SpeedMode patt0$temp = this.currentMode;
            if (patt0$temp instanceof SpeedMetaHvH) {
                SpeedMetaHvH meta = (SpeedMetaHvH)patt0$temp;
                meta.onKey((KeyEvent.KeyEventData)event);
            }
        }));
        this.addEvents(onMovePostEvent, movementInputEvent, postMotionEvent, updateEvent, travelEvent, packetEvent, keyEvent);
    }

    public SpeedModule() {
        this.addSettings(this.mode);
        this.addSettings(this.speedNCP.getSettings());
        this.addSettings(this.speedAresMine.getSettings());
        this.addSettings(this.speedAresMine2.getSettings());
        this.addSettings(this.speedGrim.getSettings());
        this.addSettings(this.speedVanilla.getSettings());
        this.addSettings(this.speedMetaHvH.getSettings());
        this.currentMode = this.speedGrim;
    }

    @Generated
    public static SpeedModule getInstance() {
        return instance;
    }

    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }
}

