// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Camera Tweaks", category = Category.RENDER)
public class CameraClipModule extends Module
{
    private static final CameraClipModule instance;
    public final SliderSetting distance;
    public final SliderSetting zoom;
    public final SliderSetting smooth;
    public final BooleanSetting smoothFirstPerson;
    public final BooleanSetting keepHandsInThirdPerson;
    
    public CameraClipModule() {
        this.distance = new SliderSetting("\u0414\u0438\u0441\u0442\u0430\u043d\u0446\u0438\u044f").value(4.0f).range(1.0f, 20.0f).step(0.5f);
        this.zoom = new SliderSetting("\u0417\u0443\u043c").value(1.0f).range(0.1f, 4.0f).step(0.1f);
        this.smooth = new SliderSetting("\u041f\u043b\u0430\u0432\u043d\u043e\u0441\u0442\u044c").value(0.2f).range(0.05f, 1.0f).step(0.05f);
        this.smoothFirstPerson = new BooleanSetting("\u041f\u043b\u0430\u0432\u043d\u043e\u0441\u0442\u044c \u043e\u0442 1 \u043b\u0438\u0446\u0430").value(false);
        this.keepHandsInThirdPerson = new BooleanSetting("\u0421\u043e\u0445\u0440\u0430\u043d\u044f\u0442\u044c \u0440\u0443\u043a\u0438 \u0432 3 \u043b\u0438\u0446\u0435").value(false);
        this.addSettings(this.distance, this.zoom, this.smooth, this.smoothFirstPerson, this.keepHandsInThirdPerson);
    }
    
    @Override
    public void onEvent() {
    }
    
    @Generated
    public static CameraClipModule getInstance() {
        return CameraClipModule.instance;
    }
    
    static {
        instance = new CameraClipModule();
    }
}
