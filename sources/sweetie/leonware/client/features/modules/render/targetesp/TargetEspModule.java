package sweetie.leonware.client.features.modules.render.targetesp;

import lombok.Generated;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.client.features.modules.render.targetesp.modes.TargetEspCircle;
import sweetie.leonware.client.features.modules.render.targetesp.modes.TargetEspCrystals;
import sweetie.leonware.client.features.modules.render.targetesp.modes.TargetEspGhost2;
import sweetie.leonware.client.features.modules.render.targetesp.modes.TargetEspGhosts;
import sweetie.leonware.client.features.modules.render.targetesp.modes.TargetEspTexture;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/targetesp/TargetEspModule.class */
@ModuleRegister(name = "Target Esp", category = Category.RENDER)
public class TargetEspModule extends Module {
    public static final TargetEspModule instance = new TargetEspModule();
    private final TargetEspTexture espTexture = new TargetEspTexture();
    private final TargetEspCrystals espCrystals = new TargetEspCrystals();
    private final TargetEspGhosts espGhosts = new TargetEspGhosts();
    private final TargetEspGhost2 espGhost2 = new TargetEspGhost2();
    private final TargetEspCircle espCircle = new TargetEspCircle();
    private TargetEspMode currentMode = this.espTexture;
    private final ModeSetting mode = new ModeSetting("Mode").value("Crystals").values("Marker", "Crystals", "Ghosts", "Ghost2", "Circle");
    private final ModeSetting animation = new ModeSetting("Animation").value("In").values("In", "Out", "None");
    private final SliderSetting duration = new SliderSetting("Duration").value(Float.valueOf(3.0f)).range(1.0f, 20.0f).step(1.0f);
    private final SliderSetting crystalsCount = new SliderSetting("Amount").value(Float.valueOf(14.0f)).range(1.0f, 36.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Crystals"));
    });
    private final SliderSetting crystalsSpeed = new SliderSetting("Speed").value(Float.valueOf(3.0f)).range(0.0f, 5.0f).step(0.5f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Crystals"));
    });
    public final BooleanSetting lastPosition = new BooleanSetting("Last position").value((Boolean) true);

    @Generated
    public static TargetEspModule getInstance() {
        return instance;
    }

    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }

    /* JADX WARN: Type inference failed for: r1v21, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v26, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public TargetEspModule() {
        addSettings(this.mode, this.animation, this.duration, this.crystalsCount, this.crystalsSpeed, this.lastPosition);
        this.mode.onAction2(() -> {
            TargetEspMode targetEspMode;
            switch (this.mode.getValue()) {
                case "Crystals":
                    targetEspMode = this.espCrystals;
                    break;
                case "Ghosts":
                    targetEspMode = this.espGhosts;
                    break;
                case "Ghost2":
                    targetEspMode = this.espGhost2;
                    break;
                case "Circle":
                    targetEspMode = this.espCircle;
                    break;
                default:
                    targetEspMode = this.espTexture;
                    break;
            }
            this.currentMode = targetEspMode;
        });
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        addEvents(Render3DEvent.getInstance().subscribe(new Listener(event -> {
            TargetEspMode.updatePositions();
            this.currentMode.onRender3D(event);
        })), UpdateEvent.getInstance().subscribe(new Listener(event2 -> {
            this.currentMode.updateAnimation(this.duration.getValue().longValue() * 50, this.animation.getValue(), 1.0f, 0.0f, 2.0f);
            this.currentMode.updateTarget();
            this.currentMode.onUpdate();
        })));
    }

    public int getCrystalsCount() {
        return this.crystalsCount.getValue().intValue();
    }

    public float getCrystalsSpeed() {
        return this.crystalsSpeed.getValue().floatValue();
    }
}
