/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="Camera Tweaks", category=Category.RENDER)
public class CameraClipModule
extends Module {
    private static final CameraClipModule instance = new CameraClipModule();
    public final SliderSetting distance = new SliderSetting("\u0414\u0438\u0441\u0442\u0430\u043d\u0446\u0438\u044f").value(Float.valueOf(4.0f)).range(1.0f, 20.0f).step(0.5f);
    public final SliderSetting zoom = new SliderSetting("\u0417\u0443\u043c").value(Float.valueOf(1.0f)).range(0.1f, 4.0f).step(0.1f);
    public final SliderSetting smooth = new SliderSetting("\u041f\u043b\u0430\u0432\u043d\u043e\u0441\u0442\u044c").value(Float.valueOf(0.2f)).range(0.05f, 1.0f).step(0.05f);
    public final BooleanSetting smoothFirstPerson = new BooleanSetting("\u041f\u043b\u0430\u0432\u043d\u043e\u0441\u0442\u044c \u043e\u0442 1 \u043b\u0438\u0446\u0430").value(false);
    public final BooleanSetting keepHandsInThirdPerson = new BooleanSetting("\u0421\u043e\u0445\u0440\u0430\u043d\u044f\u0442\u044c \u0440\u0443\u043a\u0438 \u0432 3 \u043b\u0438\u0446\u0435").value(false);

    public CameraClipModule() {
        this.addSettings(this.distance, this.zoom, this.smooth, this.smoothFirstPerson, this.keepHandsInThirdPerson);
    }

    @Override
    public void onEvent() {
    }

    @Generated
    public static CameraClipModule getInstance() {
        return instance;
    }
}

