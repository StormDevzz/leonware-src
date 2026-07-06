package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/CameraClipModule.class */
@ModuleRegister(name = "Camera Tweaks", category = Category.RENDER)
public class CameraClipModule extends Module {
    private static final CameraClipModule instance = new CameraClipModule();
    public final SliderSetting distance = new SliderSetting("Дистанция").value(Float.valueOf(4.0f)).range(1.0f, 20.0f).step(0.5f);
    public final SliderSetting zoom = new SliderSetting("Зум").value(Float.valueOf(1.0f)).range(0.1f, 4.0f).step(0.1f);
    public final SliderSetting smooth = new SliderSetting("Плавность").value(Float.valueOf(0.2f)).range(0.05f, 1.0f).step(0.05f);
    public final BooleanSetting smoothFirstPerson = new BooleanSetting("Плавность от 1 лица").value((Boolean) false);
    public final BooleanSetting keepHandsInThirdPerson = new BooleanSetting("Сохранять руки в 3 лице").value((Boolean) false);

    @Generated
    public static CameraClipModule getInstance() {
        return instance;
    }

    public CameraClipModule() {
        addSettings(this.distance, this.zoom, this.smooth, this.smoothFirstPerson, this.keepHandsInThirdPerson);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}
