// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement.speed;

import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.system.backend.Choice;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.events.client.KeyEvent;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.move.TravelEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.events.player.move.PostMotionEvent;
import sweetie.leonware.api.event.events.player.other.MovementInputEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.move.OnMovePostEvent;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.client.features.modules.movement.speed.modes.SpeedReallyworld;
import sweetie.leonware.client.features.modules.movement.speed.modes.SpeedMetaHvH;
import sweetie.leonware.client.features.modules.movement.speed.modes.SpeedVanilla;
import sweetie.leonware.client.features.modules.movement.speed.modes.SpeedGrim;
import sweetie.leonware.client.features.modules.movement.speed.modes.SpeedAresMine2;
import sweetie.leonware.client.features.modules.movement.speed.modes.SpeedAresMine;
import sweetie.leonware.client.features.modules.movement.speed.modes.SpeedNCP;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Speed", category = Category.MOVEMENT)
public class SpeedModule extends Module
{
    private static final SpeedModule instance;
    private final SpeedNCP speedNCP;
    private final SpeedAresMine speedAresMine;
    private final SpeedAresMine2 speedAresMine2;
    private final SpeedGrim speedGrim;
    private final SpeedVanilla speedVanilla;
    private final SpeedMetaHvH speedMetaHvH;
    private final SpeedReallyworld speedReallyworld;
    private final SpeedMode[] modes;
    private SpeedMode currentMode;
    private final ModeSetting mode;
    
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
        final EventListener onMovePostEvent = OnMovePostEvent.getInstance().subscribe(new Listener<OnMovePostEvent.OnMovePostEventData>(event -> {
            final SpeedMode patt0$temp = this.currentMode;
            if (patt0$temp instanceof final SpeedReallyworld rw) {
                rw.onMovePost(event);
            }
            return;
        }));
        final EventListener movementInputEvent = MovementInputEvent.getInstance().subscribe(new Listener<MovementInputEvent.MovementInputEventData>(event -> {
            final SpeedMode patt0$temp2 = this.currentMode;
            if (patt0$temp2 instanceof final SpeedReallyworld rw2) {
                rw2.onMoveInput();
            }
            return;
        }));
        final EventListener postMotionEvent = PostMotionEvent.getInstance().subscribe(new Listener<PostMotionEvent>(event -> {
            final SpeedMode patt0$temp3 = this.currentMode;
            if (patt0$temp3 instanceof final SpeedReallyworld rw3) {
                rw3.onPostMotion();
            }
            return;
        }));
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (this.currentMode != null) {
                this.currentMode.onUpdate();
            }
            return;
        }));
        final EventListener travelEvent = TravelEvent.getInstance().subscribe(new Listener<TravelEvent>(event -> {
            if (this.currentMode != null) {
                this.currentMode.onTravel();
            }
            return;
        }));
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            final SpeedMode patt0$temp4 = this.currentMode;
            if (patt0$temp4 instanceof final SpeedMetaHvH meta) {
                meta.onPacket(event);
            }
            final SpeedMode patt1$temp = this.currentMode;
            if (patt1$temp instanceof final SpeedReallyworld rw4) {
                rw4.onPacket(event);
            }
            return;
        }));
        final EventListener keyEvent = KeyEvent.getInstance().subscribe(new Listener<KeyEvent.KeyEventData>(event -> {
            final SpeedMode patt0$temp5 = this.currentMode;
            if (patt0$temp5 instanceof final SpeedMetaHvH meta2) {
                meta2.onKey(event);
            }
            return;
        }));
        this.addEvents(onMovePostEvent, movementInputEvent, postMotionEvent, updateEvent, travelEvent, packetEvent, keyEvent);
    }
    
    public SpeedModule() {
        this.speedNCP = new SpeedNCP(() -> this.getMode().is("NoCheatPlus"));
        this.speedAresMine = new SpeedAresMine(() -> this.getMode().is("AresMine"));
        this.speedAresMine2 = new SpeedAresMine2(() -> this.getMode().is("AresMine2"));
        this.speedGrim = new SpeedGrim(() -> this.getMode().is("Grim"));
        this.speedVanilla = new SpeedVanilla(() -> this.getMode().is("Vanilla"));
        this.speedMetaHvH = new SpeedMetaHvH(() -> this.getMode().is("MetaHvH"));
        this.speedReallyworld = new SpeedReallyworld(() -> this.getMode().is("Reallyworld"));
        this.modes = new SpeedMode[] { this.speedVanilla, this.speedNCP, this.speedAresMine, this.speedAresMine2, this.speedGrim, this.speedMetaHvH, this.speedReallyworld };
        this.currentMode = this.speedGrim;
        this.mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").value("Grim").values(Choice.getValues((Choice[])this.modes)).onAction(() -> {
            final SpeedMode found = (SpeedMode)Choice.getChoiceByName((String)this.getMode().getValue(), (Choice[])this.modes);
            this.currentMode = ((found != null) ? found : this.speedGrim);
            return;
        });
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
        return SpeedModule.instance;
    }
    
    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }
    
    static {
        instance = new SpeedModule();
    }
}
