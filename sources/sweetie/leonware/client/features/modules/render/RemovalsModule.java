package sweetie.leonware.client.features.modules.render;

import java.util.Arrays;
import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/RemovalsModule.class */
@ModuleRegister(name = "Removals", category = Category.RENDER)
public class RemovalsModule extends Module {
    private static final RemovalsModule instance = new RemovalsModule();
    private final String[] elements = {"Fire overlay", "Hurt camera", "Inwall overlay", "Water overlay", "Scoreboard", "Glow effect", "Bad effects", "Boss bar", "Gui background"};
    private final MultiBooleanSetting remove = new MultiBooleanSetting("Remove").value((BooleanSetting[]) Arrays.stream(this.elements).map(name -> {
        return new BooleanSetting(name).value((Boolean) false);
    }).toArray(x$0 -> {
        return new BooleanSetting[x$0];
    }));

    @Generated
    public static RemovalsModule getInstance() {
        return instance;
    }

    public RemovalsModule() {
        addSettings(this.remove);
    }

    public boolean isFireOverlay() {
        return isEnabled() && this.remove.isEnabled("Fire overlay");
    }

    public boolean isHurtCamera() {
        return isEnabled() && this.remove.isEnabled("Hurt camera");
    }

    public boolean isInwallOverlay() {
        return isEnabled() && this.remove.isEnabled("Inwall overlay");
    }

    public boolean isWaterOverlay() {
        return isEnabled() && this.remove.isEnabled("Water overlay");
    }

    public boolean isScoreboard() {
        return isEnabled() && this.remove.isEnabled("Scoreboard");
    }

    public boolean isGlowEffect() {
        return isEnabled() && this.remove.isEnabled("Glow effect");
    }

    public boolean isBadEffects() {
        return isEnabled() && this.remove.isEnabled("Bad effects");
    }

    public boolean isBossBar() {
        return isEnabled() && this.remove.isEnabled("Boss bar");
    }

    public boolean isGuiBackground() {
        return isEnabled() && this.remove.isEnabled("Gui background");
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}
