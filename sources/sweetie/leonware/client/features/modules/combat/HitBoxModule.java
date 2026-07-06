package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/HitBoxModule.class */
@ModuleRegister(name = "HitBox", category = Category.COMBAT)
public class HitBoxModule extends Module {
    private static final HitBoxModule instance = new HitBoxModule();
    private final ModeSetting mode = new ModeSetting("Режим").value("Статический").values("Статический", "Динамический");
    public final SliderSetting staticSize = new SliderSetting("Размер").value(Float.valueOf(0.2f)).range(0.0f, 1.0f).step(0.05f);
    public final SliderSetting minSize = new SliderSetting("Минимально").value(Float.valueOf(0.1f)).range(0.0f, 0.5f).step(0.05f);
    public final SliderSetting maxSize = new SliderSetting("Максимально").value(Float.valueOf(0.5f)).range(0.0f, 1.0f).step(0.05f);

    @Generated
    public static HitBoxModule getInstance() {
        return instance;
    }

    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }

    public HitBoxModule() {
        addSettings(this.mode, this.staticSize, this.minSize, this.maxSize);
    }

    public static float getExpand() {
        if (!getInstance().isEnabled()) {
            return 0.0f;
        }
        String currentMode = getInstance().mode.getValue();
        if (currentMode.equals("Статический")) {
            return getInstance().staticSize.getValue().floatValue();
        }
        float min = getInstance().minSize.getValue().floatValue();
        float max = getInstance().maxSize.getValue().floatValue();
        long time = System.currentTimeMillis();
        float cycle = (time % 1000) / 1000.0f;
        float sine = (float) Math.sin(((double) cycle) * 3.141592653589793d * 2.0d);
        float normalized = (sine + 1.0f) / 2.0f;
        return min + ((max - min) * normalized);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}
